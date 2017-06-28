#include "WizFi310.h"
#include "utility/WizFi310Drv.h"
#include <ArduinoJson.h>

#define MAX_STR_LENGTH (2048)
#define MAX_SSID_LENGTH (100)
#define MAX_PWD_LENGTH (100)
#define MAX_TEXT_LENGTH (301)

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
char ttsAddr[] = "api.voicerss.org";
int serverPort = 8080;

WiFiServer APServer(8080);
WiFiClient client;

// use a ring buffer to increase speed and reduce memory allocation
WizFiRingBuffer buf(100);

void printWifiStatus();
void sendHttpMacResponse(WiFiClient client);

void setup()
{
  Serial.println("Start");
  Serial.begin(115200);
  Serial3.begin(115200);

  Serial.print("WL_CONNECTED : ");
  Serial.println(WL_CONNECTED);

  //APServerStart();
  strcpy(clientSsid, "U+Net81F3");
  strcpy(clientPwd, "4000005619");
  clientStart();
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

  // start the web server on port 80
  APServer.begin();
  Serial.println("Server started");
}

void clientStart() {
  //initClient();
  WiFi.init(&Serial3);

    // check for the presence of the shield
    if (WiFi.status() == WL_NO_SHIELD) {
        Serial.println("WiFi shield not present");
        //SerialUSB.println("WiFi shield not present");
        // don't continue
        while (true);
    }

    // attempt to connect to WiFi network
    while ( status != WL_CONNECTED) {
        Serial.print("Attempting to connect to WPA SSID: ");
        Serial.println(clientSsid);
        // Connect to WPA/WPA2 network
        status = WiFi.begin(clientSsid, clientPwd);
    }

    // you're connected now, so print out the data
    Serial.println("You're connected to the network");
    
    Serial.println();
    Serial.println("Starting connection to server...");
    
    requestDust(126.9658, 37.5714);
    //requestSpeech(String("오늘 날씨는 맑음 미세먼지 보통입니다 버스 도착까지 10분전입니다"), String("ko-kr"));
}

void requestDust(double lon, double lat) 
{  
  // if you get a connection, report back via serial
  if (client.connect(serverAddr, 8080)) {
    Serial.println("Connected to server");
    // Make a HTTP request
    client.print("GET /dust?lon=");
    client.print(lon);
    client.print("&lat=");
    client.print(lat);
    client.println(" HTTP/1.1");
    client.println("Host: arduino.cc");
    client.println("Connection: close");
    client.println();
  }
}

void requestSpeech(String text, String lang) 
{  
  // if you get a connection, report back via serial
  if (client.connect(ttsAddr, 80)) {
    Serial.println("Connected to server");
    // Make a HTTP request
    //Serial.println(ttsAddr);
    //Serial.print("GET /translate_tts?ie=UTF-8&q=Hello%20person&tl=en&total=1&idx=0&textlen=12&tk=554200.929215&client=tw-ob");
    //client.print(text.length());
    //client.print("&client=tw-ob&q=");
    //client.print(text);
    //client.print("&tl=");
    //client.print(lang);
    //Serial.println(" HTTP/1.1");
    //Serial.println("cache-control: no-cache");
    //Serial.println("postman-token: 39436dfd-900f-4cf4-9bc8-f6373f004992");
    //Serial.println();
    client.println("GET /?key=fb82492d22b541ce8983bbea7d0d984b&hl=en-us&src=Hello&c=WAV&f=8khz_8bit_mono HTTP/1.1");
    client.println("Host: arduino.cc");
    client.println("Connection: close");
    client.println("cache-control: no-cache");
    //client.print(text.length());
    //client.print("&client=tw-ob&q=");
    //client.print(text);
    //client.print("&tl=");
    //client.print(lang);
    //client.println(" HTTP/1.1");
    client.println();
  }
}

void initClient() 
{
  status = 0;
  for(int i = 0; i < MAX_SOCK_NUM; i++) 
    WizFi310Drv::_state[i] = -1;
  IPAddress ip(192, 168, 219, 141);
  IPAddress subnet(255, 255, 255, 0);
  IPAddress gw(192, 168, 219, 1);
  WiFi.config(ip, subnet, gw);
}

void sendHttpMacResponse(WiFiClient client)
{
    client.print(mac);
}

int strLength(char str[], int max_size)  
{
  for (int i = 0; i < max_size; i++) {
    if (str[i] == '\0')
      return i;
  }
 
  return -1;
}

