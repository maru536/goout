#include <VS1003.h>
#include "WizFi310.h"
#include <SPI.h>
#include <ArduinoJson.h>

#define DREQ       3 //Data Request Pin: Player asks for more data
#define XCS        10 //Data Chip Select / BSYNC Pin
#define XDCS       12 //Control Chip Select Pin (for accessing SPI Control/Status registers)
#define XRST       7
#define CARDCS     4

#define MAX_HEADER_SIZE (15)
#define MAX_NUMBER_SIZE (10)
#define MAX_MP3_HEADER_SIZE (3)

#define IS_ALNUM(ch) \
        ( ch >= 'a' && ch <= 'z' ) || \
        ( ch >= 'A' && ch <= 'Z' ) || \
        ( ch >= '0' && ch <= '9' ) || \
        ( ch >= '-' && ch <= '.' ) 

int status = WL_IDLE_STATUS;       // the Wifi radio's status
char ttsServer[] = "api.voicerss.org";
int ttsPort = 80;
char gooutServer[] = "13.124.126.90";
int gooutPort = 8080;
char length_buf[MAX_NUMBER_SIZE] = "";

uint16_t content_length = 0;
uint16_t content_idx = 0;
bool isGetLength = false;
bool isStartContent = false;
bool isEndContent = false;

int bracketCount = 0;
int pos = 0;
char c;

VS1003 player(XCS, XDCS, DREQ, XRST, CARDCS); //create an instance of the libary

WiFiClient client;

void setup()
{
  Serial.begin(115200);

  audioInit();
  //wifiInit("U+Net81F3", "4000005619");
  wifiInit("hotpot", "asdf1234");
  requestTTS("안녕하세요");
}

void loop()
{
  if (client.available()) {
    char c = (char)client.read();
    
    if (isGetLength == false && c == '\n') {
      for (int i = 0; i < MAX_HEADER_SIZE; i++) {
        if ((c = client.read()) == 'C' && (c = client.read()) == 'o' && (c = client.read()) == 'n' && 
        (c = client.read()) == 't' && (c = client.read()) == 'e' && (c = client.read()) == 'n' && 
        (c = client.read()) == 't' && (c = client.read()) == '-' && (c = client.read()) == 'L' &&
        (c = client.read()) == 'e' && (c = client.read()) == 'n' && (c = client.read()) == 'g' && 
        (c = client.read()) == 't' && (c = client.read()) == 'h' && (c = client.read()) == ':' &&
        (c = client.read()) == ' ') {   
          for(int j = 0; j < MAX_NUMBER_SIZE; j++) {
            if ((c = client.read()) == '\n') {
              length_buf[j] = 0;
              content_length = strToUint16(length_buf, sizeof(length_buf));
              isGetLength = true;
              break;
            }
            length_buf[j] = c;
          }
        }
        else {
          break;
        }
      }
    }

    if (isGetLength == true && isStartContent == false && c == '\n') {
      for (int i = 0; i < MAX_MP3_HEADER_SIZE; i++) {
        if ((c = client.read()) == '\n') {
          isStartContent = true;
          content_idx = 0;
          content_idx = player.playStreamingAudio(client, content_idx, content_length);
        }
      }
    }

    if (isStartContent == true) {
      if (content_idx >= content_length) {
        isEndContent = true;
      }
    }
  }

  // if the server's disconnected, stop the client
  if (!client.connected() || isEndContent == true) {
    Serial.println();
    Serial.println("Disconnecting from server...");
    Serial.print("Content-Length: ");
    Serial.println(content_length);
    Serial.print("Content-Idx: ");
    Serial.println(content_idx);
    client.stop();

    content_length = 0;
    isGetLength = false;
    isStartContent = false;
    isEndContent = false;

    // do nothing forevermore
    while (true);
  } 
}


bool requestTTS(char* str) {
  
  if (client.connect(ttsServer, ttsPort)) {
    char* url;
    url = url_encode(str);
    Serial.println("Connected to TTS Server");
    // Make a HTTP request
    client.print("GET /?key=de834fef8cff4915a795a1192beb0cb1&hl=ko-kr&src=");
    client.print(url);
    client.println("&c=mp3&f=8khz_16bit_mono&r=2 HTTP/1.1");
    client.println("Host: api.voicerss.org");
    client.println("Connection: keep-alive");
    client.println("Upgrade-Insecure-Requests: 1");
    client.println("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
    client.println("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
    client.println("Accept-Encoding: gzip, deflate, sdch");
    client.println("Accept-Language: ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4");
    client.println("");
    free(url);
    return true;
  }
  else {
    Serial.println("Connect Fail!");
    return false;
  }
  
}

char* url_encode( const char* str ){

    int i, j = 0, len;
    
    char* tmp;
    
    len = strlen( str );
    tmp = (char*) malloc( (sizeof(char) * 3 * len) +1 );

    for( i = 0 ; i < len ; i++ ){

        if( IS_ALNUM( str[i] ) )
            tmp[j] = str[i];

        else{
        
            snprintf( &tmp[j], 4, "%%%02X\n", (unsigned char)str[i] );
            j += 2;

        }
        j++;
        
    }
    tmp[j] = 0;
    return tmp;
}

bool requestInfo() {
  Serial.println(WiFi.macAddress());
  if (client.connect(gooutServer, gooutPort)) {
    Serial.println("Connected to Goout Server");
    // Make a HTTP request

    client.print("GET /getconfig?deviceId=");
    client.print(WiFi.macAddress());
    client.println(" HTTP/1.1");
    client.println("");
    return true;
  }
  else {
    return false;
  }
}

void audioInit() 
{
  player.begin();
  player.useInterrupt();
  //Set the volume
  player.setVolume(40,40);
}

void wifiInit(char* ssid, char* pwd) 
{
  Serial3.begin(115200);
  WiFi.init(&Serial3);
  
  content_length = 0;
  isGetLength = false;
  isStartContent = false;
  isEndContent = false;
  // check for the presence of the shield
  if (WiFi.status() == WL_NO_SHIELD) {
    Serial.println("WiFi shield not present");
    // don't continue
    while (true);
  }

  Serial.println(status);
  // attempt to connect to WiFi network
  while ( status != WL_CONNECTED) {
    Serial.print("Attempting to connect to WPA SSID: ");
    Serial.println(ssid);
    // Connect to WPA/WPA2 network
    status = WiFi.begin(ssid, pwd);
  }
}

void printWifiStatus()
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

void requestDust(double lon, double lat) 
{  
  // if you get a connection, report back via serial
  if (client.connect(gooutServer, gooutPort)) {
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

uint16_t strToUint16(char* str, int size)
{
  uint16_t result = 0;
  for (int i = 0; i < size; i++) {
    if (str[i] > '9' || str[i] < '0') {
      break;
    }
        
    result *= 10;
    result += str[i] - '0';
  }

  return result;
}


/*
void loop()
{
  if (client.available()) {
    content_idx += client.read(buffer, sizeof(buffer));
    Serial.print(content_idx);
    Serial.print(" > ");
    Serial.println(buffer);
  }

  if (!client.connected() || isEndContent == true) {
    Serial.println();
    Serial.println("Disconnecting from server...");
    client.stop();

    // do nothing forevermore
    while (true);
  } 
}
*/
/*
void loop()
{
  if (client.available()) {
    c = (char)client.read();
    
    if (!isStartBody) {
      if (c == '{') {
        bracketCount++;
        isStartBody = true;
      }
    }
    else {
      // you got two newline characters in a row
      // that's the end of the HTTP request, so send a response
      if (c == '{') {
        bracketCount++; 
      } 
      else if (c == '}') {
        bracketCount--;
      }
      
      response_buf[pos++] = c;
      if (bracketCount == 0) {
        Serial.println(response_buf);
        isEndContent == true;
        response_buf[pos] = '\0';
        StaticJsonBuffer<512> jsonBuffer;
        JsonObject& json = jsonBuffer.parseObject(response_buf);
        json.printTo(Serial);
        Serial.println("");*/
        /*strncpy(clientSsid, json["ssid"], sizeof(clientSsid));
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

  if (!client.connected() || isEndContent == true) {
    Serial.println();
    Serial.println("Disconnecting from server...");
    client.stop();

    // do nothing forevermore
    while (true);
  } 
}
*/
