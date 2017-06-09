#include "WizFi310.h"
#include "utility/WizFi310Drv.h"
#include <ArduinoJson.h>

#define MAX_STR_LENGTH (2048)

char ssid[] = "Wiznet_TestAP";         // your network SSID (name)
char pass[] = "12345678";        // your network password
int status = WL_IDLE_STATUS;     // the Wifi radio's status
int reqCount = 0;                // number of requests received
String mac;
char str[MAX_STR_LENGTH] = "";
int bracketCount;
int pos;

WiFiServer server(80);

// use a ring buffer to increase speed and reduce memory allocation
WizFiRingBuffer buf(100);

void printWifiStatus();
void sendHttpResponse(WiFiClient client);

void setup()
{
    Serial.begin(115200);
    Serial3.begin(115200);
    WiFi.init(&Serial3);
    
    mac = WiFi.macAddress();

    // check for the presence of the shield
    if (WiFi.status() == WL_NO_SHIELD) {
    Serial.println("WiFi shield not present");
    while (true); // don't continue
    }

    Serial.print("Attempting to start AP ");
    Serial.println(ssid);

    IPAddress localIp(192, 168, 10, 1);
    WiFi.configAP(localIp);

    // start access point
    status = WiFi.beginAP(ssid, 10, pass, WIZ_TYPE_WPA2_MIXED);

    Serial.println("Access point started");
    printWifiStatus();

    // start the web server on port 80
    server.begin();
    Serial.println("Server started");
}

void loop()
{
    WiFiClient client = server.available();  // listen for incoming clients

    if (client) {                               // if you get a client,
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
              String ssid = json["ssid"];
              String pwd = json["pwd"];
              Serial.println(ssid);
              Serial.println(pwd);
                  
              if(ssid.length() && pwd.length()) {
                Serial.println("Available");
                sendHttpResponse(client);
                break;
              }
              else {
                Serial.println("InDefined Request!");
              }
              pos = 0;
              break;
            }
          }
        }

    // give the web browser time to receive the data
    delay(1000);

    // close the connection
    client.stop();
    Serial.println("Client disconnected");
  }
}

void sendHttpResponse(WiFiClient client)
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

void printWifiStatus()
{
    // print your WiFi shield's IP address
    IPAddress ip = WiFi.localIP();
    Serial.print("IP Address: ");
    Serial.println(ip);

    // print where to go in the browser
    Serial.println();
    Serial.print("To see this page in action, connect to ");
    Serial.print(ssid);
    Serial.print(" and open a browser to http://");
    Serial.println(ip);
    Serial.println();
}

