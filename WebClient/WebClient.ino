#include <VS1003.h>
#include "WizFi310.h"
#include <SPI.h>

#define MAX_HEADER_SIZE (15)
#define MAX_NUMBER_SIZE (10)
#define MAX_MP3_HEADER_SIZE (3)
#define SERIAL_PRINT 0

#define DREQ 3 //Data Request Pin: Player asks for more data
#define XCS 10 //Data Chip Select / BSYNC Pin
#define XDCS 12 //Control Chip Select Pin (for accessing SPI Control/Status registers)
#define RESET 7 //Reset is active low
#define CARDCS 4


int status = WL_IDLE_STATUS;       // the Wifi radio's status
char ssid[] = "U+Net81F3";    // your network SSID (name)
char pass[] = "4000005619";          // your network password
char server[] = "api.voicerss.org";
char length_buf[MAX_NUMBER_SIZE] = "";

int content_length = 0;
int content_idx = 0;
bool isGetLength = false;
bool isStartContent = false;
bool isEndContent = false;

VS1003 player(XCS, XDCS, DREQ, RESET, CARDCS); //create an instance of the libary

WiFiClient client;

void setup()
{
  Serial.begin(115200);
  Serial3.begin(115200);
  
  WiFi.init(&Serial3);
  player.begin();
  player.useInterrupt();
  //Set the volume
  player.setVolume(40,40);
  
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

  // attempt to connect to WiFi network
  while ( status != WL_CONNECTED) {
    Serial.print("Attempting to connect to WPA SSID: ");
    Serial.println(ssid);
    // Connect to WPA/WPA2 network
    status = WiFi.begin(ssid, pass);
  }

  // you're connected now, so print out the data
  Serial.println("You're connected to the network");
    
  printWifiStatus();
    
  Serial.println();
  Serial.println("Starting connection to server...");
    
  // if you get a connection, report back via serial
  if (client.connect(server, 80)) {
    Serial.println("Connected to server");
    // Make a HTTP request
    client.println("GET /?key=fb82492d22b541ce8983bbea7d0d984b&hl=ko-kr&src=%EB%82%A0%EC%94%A8%20%EB%A7%91%EC%9D%8C%20%EB%AF%B8%EC%84%B8%EB%A8%BC%EC%A7%80%20%EB%82%98%EC%81%A8&c=mp3&f=8khz_8bit_mono&r=3 HTTP/1.1");
    client.println("Host: api.voicerss.org");
    client.println("Connection: keep-alive");
    client.println("Upgrade-Insecure-Requests: 1");
    client.println("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
    client.println("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
    client.println("Accept-Encoding: gzip, deflate, sdch");
    client.println("Accept-Language: ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4");
    client.println();
  }
}

void loop()
{
  while (client.available()) {
    char c = client.read();
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
              length_buf[j] = '\0';
              content_length = atoi(length_buf);
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
        if ((c = client.read()) == 'I' && (c = client.read()) == 'D' && (c = client.read()) == '3') {
          #if SERIAL_PRINT == 1
          Serial.print("ID3");
          #endif
          isStartContent = true;
          content_idx = 2;
          content_idx = player.playStreamingAudio(client, content_idx, content_length);
          Serial.println("");
          Serial.println(content_idx);
        }
      }
    }

    /*if (content_idx >= content_length) {
      isEndContent = true;
      break;
    }*/

    if (isStartContent == true) {
      #if SERIAL_PRINT == 1
      Serial.print(c);
      #endif
      content_idx++;
      if (content_idx >= content_length) {
        isEndContent = true;
        break; 
      }
    }
  }

  // if the server's disconnected, stop the client
  if (isEndContent == true) {
    Serial.println();
    Serial.println("Disconnecting from server...");
    Serial.print("Content-Length: ");
    Serial.println(content_length);
    client.stop();

    content_length = 0;
    isGetLength = false;
    isStartContent = false;
    isEndContent = false;

    // do nothing forevermore
    while (true);
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