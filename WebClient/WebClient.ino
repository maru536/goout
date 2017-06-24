#include "WizFi310.h"
#include <TMRpcm.h>
#include <SD.h>
#include <SPI.h>
File uFile;

#define SD_ChipSelectPin 53  //example uses hardware SS pin 53 on Mega2560

TMRpcm audio;
char *filename = "myMp3.mp3";

char ssid[] = "U+Net81F3";    // your network SSID (name)
char pass[] = "4000005619";          // your network password
int status = WL_IDLE_STATUS;       // the Wifi radio's status

char server[] = "api.voicerss.org";
char buf[5] = "";


bool flag = false;
bool flag1 = false;
bool flag2 = false;
char output[2] = "";

// Initialize the Ethernet client object
WiFiClient client;

void printWifiStatus();

void setup()
{
    Serial.begin(115200);
    Serial3.begin(115200);
    audio.speakerPin = 11; //5,6,11 or 46 on Mega, 9 on Uno, Nano, etc
    pinMode(12,OUTPUT);  //Pin pairs: 9,10 Mega: 5-2,6-7,11-12,46-45
    WiFi.init(&Serial3);
    flag = false;
    flag1 = false;
    flag2 = false;

    /* Standard SD Lib */
    /*pinMode(13,OUTPUT); //LED Connected to analog pin 0
   if (!SD.begin(SD_ChipSelectPin)) {  return;
   }else{Serial.println("SD OK"); }

  uFile = SD.open(filename, FILE_WRITE);
  if(!uFile){ Serial.print("Failed to open"); return; }*/


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
        client.println("GET /?key=fb82492d22b541ce8983bbea7d0d984b&hl=ko-kr&src=%EC%95%88%EB%85%95%ED%95%98%EC%84%B8%EC%9A%94&c=mp3&f=8khz_8bit_mono HTTP/1.1");
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
    // if there are incoming bytes available
    // from the server, read them and print them
    //if (!client.available() && flag1 == false) {
    if (!client.available()) {
      //if (flag2 == true)
        //flag1 = true;
    }
    char hex[2] = "";
    while (client.available()) {
      //flag2 = true;
        char c = 0;
        uint8_t u = 0;

        if (!flag && (c = client.read()) == 'I') {
          if (client.available() && (c = client.read()) == 'D') {
            if (client.available() && (c = client.read()) == '3') {
              /*if (client.available() && (c = client.read()) == 'F') {
                flag = true;
                //아래 부분의 Serial을 file output으로 (fileName).wav에 write
                Serial.print("RIFF");
              }*/

              flag = true;
              //아래 부분의 Serial을 file output으로 (fileName).wav에 write
              uFile.write("ID3");
              printHex(uint8ToHex((uint8_t)'I'));
              printHex(uint8ToHex((uint8_t)'D'));
              printHex(uint8ToHex((uint8_t)'3'));
            }
          }
        }

        if (flag) {
          //아래 부분의 Serial을 file output으로 (fileName).wav에 write
          //uFile.write(client.read());
          c = client.read();
          printHex(uint8ToHex((uint8_t)c));
          //Serial.println(hex);
        }
    }

    /*if (flag1 == true) {
      uFile.close();
      Serial.print("mp3 file written, ready to finalize."); 
      Serial.println();
      Serial.println("Disconnecting from server...");
      client.stop();
      flag = false;
      flag1 = false;
      flag2 = false;
    }*/

    // if the server's disconnected, stop the client
    if (flag1 == false && !client.connected()) {
    flag1 = true;
    Serial.println();
    Serial.println("Disconnecting from server...");
    client.stop();
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

char* uint8ToHex(uint8_t input) 
{
  output[0] = hex(input / 16);
  output[1] = hex(input % 16);

  return output;
}

void printHex(char* hex) 
{
  Serial.print(hex[0]);
  Serial.println(hex[1]);
}

char hex(int input) {
  if (input < 10)
    return '0'+input;
  else 
    return 'A'+input-10;
}



