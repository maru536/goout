#include <Wire.h> //I2C 통신을 위해서 선언한 라이브러리
#include <VL53L0X.h>
#include <VS1003.h>
#include <SPI.h>
#include "WizFi310.h"

/*PIN 레이아웃*/
#define DREQ       3 // sd card module dreq pin
#define SD_SELECT  4 // sd card select pin
#define PIR_A      5
#define PIR_B      6
#define XRST       7
#define SHIFT_CLK  8 // clk pin
#define STORE_CLK  9 // latch pin(레지스터 latch low,shiftout(data,clk,lsb,byte),latch high)
#define XCS        10
#define LED_DATA   11 // led data pin
#define XDCS       12

#define THRESHOLD  2000 // tof snsr threshold

#define MAX_HEADER_SIZE (15)
#define MAX_BUF_SIZE (32)
#define MAX_NUMBER_SIZE (10)
#define MAX_MP3_HEADER_SIZE (3)
#define MAX_RESPONSE_SIZE (512)
#define SERIAL_PRINT 0

#define IDLE_MODE 100
#define DETECT_MODE 0
#define RECEIVE_DATA_MODE 1

/*LED 쉬프트 연산을 위한 마스크 바이트 정의*/
const byte powerByte = 0x40;
const byte ledAByte = 0x20;
const byte ledBByte = 0x10;
const byte ledCByte = 0x08;
const byte wifiByte = 0x04;
const byte allByte = 0x7C; // welcomeLight로 활용
const byte delByte = 0x00;
const byte pirDetectByte = 0x38; //디버깅용

//led 입력에 사용할 플래그 바이트
byte ledByte = 0x00;

//사람 감지후 1분후 끄기위한 변수
unsigned long humanDetectedTime;
//ON time 정의
const int LED_ON_TIME = 30000;

/*TOF 변수*/
VL53L0X tofSnsr;

boolean triggerTag = false;

// 통합 변수 코드
int status = WL_IDLE_STATUS;       // the Wifi radio's status
char ttsServer[] = "api.voicerss.org";
int ttsPort = 80;
char gooutServer[] = "13.124.126.90";
int gooutPort = 8080;
char length_buf[MAX_NUMBER_SIZE] = "";
char response_buf[MAX_RESPONSE_SIZE] = "";
uint8_t raw_buf[MAX_BUF_SIZE] = "";

uint16_t content_length = 0;
uint16_t content_idx = 0;
bool isGetLength = false;
bool isStartContent = false;
bool isEndContent = false;
bool isStartData = false;
bool isEndLine = false;
bool isStartBody = false;
uint16_t requestStart = 0;
uint16_t responseStart = 0;
int bracketCount = 0;
int pos = 0;
char c;
int mode = 0;
VS1003 player(XCS, XDCS, DREQ, XRST, SD_SELECT); //create an instance of the libary
WiFiClient client;

void setup() {
  Serial.begin(115200);
  
  //LED pin 입,출력 선언한다
  pinMode(SHIFT_CLK, OUTPUT);
  pinMode(STORE_CLK, OUTPUT);
  pinMode(LED_DATA, OUTPUT);

  //PIR SNSR 입력,출력 선언한다
  pinMode(PIR_A, INPUT);
  pinMode(PIR_B, INPUT);

  //TOF센서와 I2C 센서 config한다(주의! TOF센서의 경우 I2C통신으로 in,out 선언불필요)
  settingHDS();
  
  audioInit();
  wifiInit("U+Net81F3", "4000005619");
  
  mode = RECEIVE_DATA_MODE;
  requestDust(126.9658, 37.5714);

  //부팅이 끝나면 웰컴라이트를 킨다
  //welcomeLight();
}

void loop() {
  if (Serial.available()) {
    c = 
  }
  switch (mode) {
    case DETECT_MODE:
    // led를 켜둔 이력이 있다면
    if (humanDetectedTime)
    {
      // 현재시간과 비교해서 역치값 이상일시
      if ((millis() - humanDetectedTime) > LED_ON_TIME)
      {
        // LED를 전부 끈다
        ledOFF(allByte);
        humanDetectedTime = 0;
      }
    }
    
    //전원 LED는 상기시 ON한다
    ledON(powerByte);
    
    //만약 wifi 가 접속불가할 경우
    //  wifiWarningLight();
    //PIR센서가 사람 감지시 led ON
    //  pirDetectLight();
  
    //-----인체감지 판단순서-----//
    Serial.print("PIR SNSR STATE >>> ");
    Serial.println(getPIRValue());
    // 1.만약 PIR 센서에서 TRUE값이 들어오면
    if (getPIRValue()) {
      Serial.println("PIR >>> detect");
      // 2. A,B,C led에 불을 켜고(디버깅 용)
      //    pirDetectLight();
      // 3.TOF 센서의 값을 읽어온다.
      int detectedRange = getTOFValue();
      // 4.만약 TOF센서값이 역치값보다 작을경우
      if (detectedRange < THRESHOLD)
      {
        Serial.print("TOF >>> ");
        Serial.println(detectedRange);
        // 4.모니터링을 하고, 모니터링 한 값을 태그값(triggerTag)에 넣는다.
        triggerTag = monitoring(THRESHOLD);
      } else {
        Serial.println("TOF >>> OVER THRESHOLD");
      }
    } else {
      Serial.println("PIR >>> not detect");
    }
  
    //------인체감지된 이후------//
    if (triggerTag) {
      Serial.println("***********HUMAN DETECTING************");
      //1.서버로부터 데이터를 받는다.
      //2.이전 led를 끄고
      ledOFF(delByte);
      //3.리뉴얼된 자료값에 맞게 led를 켜준다.
      mode = RECEIVE_DATA_MODE;
      requestDust(126.9658, 37.5714);
      //ledON(ledAByte);
      //4.led ON 한 시간을 기억해둔다
      humanDetectedTime = millis();
      //5.보이스 출력을 해준다.
  
      //6.트리거를 끈다
      triggerTag = false;
    }
    break;

    case RECEIVE_DATA_MODE:
    if (client.available()) {
      c = (char)client.read();
      //Serial.print(c);
      
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
          //Serial.println(response_buf);
          isEndContent == true;
          Serial.println("Response!");
          ledON(ledAByte);
        }
      }
    }
  
    if (!client.connected() || isEndContent == true) {
      Serial.println();
      Serial.println("Disconnecting from server...");
      client.stop();
  
      // do nothing forevermore
      //while (true) ;
      mode = DETECT_MODE;
    } 
    break;

    case IDLE_MODE:

    break;
  }
  
}

