#include "WizFi310.h"
#include "utility/WizFi310Drv.h"
#include <ArduinoJson.h>

#define MAX_STR_LENGTH (2048)
#define MAX_SSID_LENGTH (100)
#define MAX_PWD_LENGTH (100)

char serverSsid[MAX_SSID_LENGTH] = "Wiznet_TestAP";         // your network SSID (name)
char serverPwd[MAX_PWD_LENGTH] = "12345678";        // your network password
char clientSsid[MAX_SSID_LENGTH] = "";
char clientPwd[MAX_PWD_LENGTH] = ""; 
int status = WL_IDLE_STATUS;     // the Wifi radio's status
int reqCount = 0;                // number of requests received
String mac;
char str[MAX_STR_LENGTH] = "";
int bracketCount;
int pos;
char serverAddr[] = "13.124.126.90";
int serverPort = 8080;

WiFiServer APServer(8080);
WiFiClient client;

// use a ring buffer to increase speed and reduce memory allocation
WizFiRingBuffer buf(100);

void printWifiStatus();
void sendHttpMacResponse(WiFiClient client);

void setup()
{
  Serial.begin(115200);
  Serial3.begin(115200);

  Serial.print("WL_CONNECTED : ");
  Serial.println(WL_CONNECTED);

  APServerStart();        
}

void loop()
{
  if (APServer.available()) {                               // if you get a client,
    client = APServer.available();
    Serial.println("New client");             // print a message out the serial port
    bracketCount = pos = 0;
    //str = "";
    buf.init();                               // initialize the circular buffer
    while (client.connected()) {              // loop while the client's connected
      if (client.available()) {               // if there's bytes to read from the client,
        char c = client.read();               // read a byte, then
        buf.push(c);                          // push it to the ring buffer

        Serial.write(c);

        // you got two newline characters in a row
        // that's the end of the HTTP request, so send a response
        if (c == '{') {
          bracketCount++; 
        } 
        else if (c == '}') {
          bracketCount--;
        }
              
        str[pos++] = c;
        if (bracketCount == 0) {
          str[pos] = '\0';
          StaticJsonBuffer<512> jsonBuffer;
          JsonObject& json = jsonBuffer.parseObject(str);
          json.printTo(Serial);
          Serial.println("");
          strncpy(clientSsid, json["ssid"], sizeof(clientSsid));
          strncpy(clientPwd, json["pwd"], sizeof(clientPwd));
          //clientSsid = json["ssid"];
          //clientPwd = json["pwd"];
          Serial.println(clientSsid);
          Serial.println(clientPwd);
                  
          if(strLength(clientSsid, MAX_SSID_LENGTH) > 0 && strLength(clientPwd, MAX_PWD_LENGTH) > 0) {
            Serial.println("Available");
            sendHttpMacResponse(client);
            break;
          }
          else {
            Serial.println("InDefined Request!");
          }
        }
      }
    }

    // give the web browser time to receive the data
    delay(1000);

    // close the connection
    client.stop();
    Serial.println("Client disconnected");
    clientStart();
  }
  else if (client.available()) {
    while (client.available()) {
      char c = client.read();
      Serial.write(c);
    }

    // if the server's disconnected, stop the client
    if (!client.connected()) {
    Serial.println();
    Serial.println("Disconnecting from server...");
    client.stop();

    // do nothing forevermore
      while (true);
    }
  }
}

void APServerStart() {
  WiFi.init(&Serial3);
    
  mac = WiFi.macAddress();

  // check for the presence of the shield
  if (WiFi.status() == WL_NO_SHIELD) {
  Serial.println("WiFi shield not present");
  while (true); // don't continue
  }

  Serial.print("Attempting to start AP ");
  Serial.println(serverSsid);

  IPAddress localIp(192, 168, 10, 1);
  WiFi.configAP(localIp);

  // start access point
  Serial.print("Before beginAP : ");
  Serial.println(status);
  status = WiFi.beginAP(serverSsid, 10, serverPwd, WIZ_TYPE_WPA2_MIXED);
  Serial.print("After beginAP : ");
  Serial.println(status);

  Serial.println("Access point started");
  printAPServerWifiStatus();

  // start the web server on port 80
  APServer.begin();
  Serial.println("Server started");
}

void clientStart() {
  WiFi.init(&Serial3);

  // check for the presence of the shield
  Serial.print("Start Client1 : ");
  Serial.println(status);
  if (WiFi.status() == WL_NO_SHIELD) {
    Serial.println("WiFi shield not present");
    // don't continue
    while (true);
  }
  Serial.print("Start Client2 : ");
  Serial.println(status);
  status = 0;

  // attempt to connect to WiFi network
  while (status != WL_CONNECTED) {
    Serial.print("Attempting to connect to WPA SSID: ");
    Serial.println(clientSsid);
    // Connect to WPA/WPA2 network
    IPAddress localIp(192, 168, 219, 141);
    WiFi.configAP(localIp);
    status = WiFi.begin(clientSsid, clientPwd);
  }
  Serial.print("Check Client1 : ");
  Serial.println(status);

  // you're connected now, so print out the data
  Serial.println("You're connected to the network");
    
  printClientWifiStatus();
    
  Serial.println();
  Serial.println("Starting connection to server...");
    
  // if you get a connection, report back via serial
  /*while (!client.connect(serverAddr, 8080)) {
    Serial.print("c");
  }
  Serial.println("");*/
  if (client.connect(serverAddr, 8080)) {
    Serial.println("Connected to server");
    // Make a HTTP request
    client.println("GET /dust?lon=126.9658000000&lat=37.5714000000 HTTP/1.1");
    client.println("Host: arduino.cc");
    client.println("Connection: close");
    client.println();
  }
}

void sendHttpMacResponse(WiFiClient client)
{
    client.print(mac);
    /*client.print(
    "HTTP/1.1 200 OK\r\n"
    "Content-Type: application/json;charset=UTF-8\r\n"
    "Transfer-Encoding: chunked\r\n"  // the connection will be closed after completion of the response
    "Date: Sun, 04 Jun 2017 12:06:40 GMT\r\n"        // refresh the page automatically every 20 sec
    "\r\n");
    
    client.print("{mac : asd}\r\n");*/
    /*client.print("<!DOCTYPE HTML>\r\n");
    client.print("<html>\r\n");
    client.print("<h1>Hello World!</h1>\r\n");
    client.print("Requests received: ");
    client.print(++reqCount);
    client.print("<br>\r\n");
    client.print("MAC : ");
    client.print(mac);
    client.print("<br>\r\n");
    client.print("Analog input A0: ");
    client.print(analogRead(0));
    client.print("<br>\r\n");
    client.print("</html>\r\n");*/
}

void printAPServerWifiStatus()
{
    // print your WiFi shield's IP address
    IPAddress ip = WiFi.localIP();
    Serial.print("IP Address: ");
    Serial.println(ip);

    // print where to go in the browser
    Serial.println();
    Serial.print("To see this page in action, connect to ");
    Serial.print(serverSsid);
    Serial.print(" and open a browser to http://");
    Serial.println(ip);
    Serial.println();
}

void printClientWifiStatus()
{
    // print the SSID of the network you're attached to
    Serial.print("SSID: ");
    Serial.println(WiFi.SSID());
    
    // print your WiFi shield's IP address
    IPAddress ip = WiFi.localIP();
    Serial.print("IP Address: ");
    Serial.println(ip);
    
    // print the received signal strength
    long rssi = WiFi.RSSI();
    Serial.print("Signal strength (RSSI):");
    Serial.print(rssi);
    Serial.println(" dBm");
}

int strLength(char str[], int max_size)  
{
  for (int i = 0; i < max_size; i++) {
    if (str[i] == '\0')
      return i;
  }
 
  return -1;
}

