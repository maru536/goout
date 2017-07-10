#include <Wire.h>
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

#define MAX_BUF_SIZE (220)
#define MAX_NUMBER_SIZE (10)
#define MAX_HEADER_SIZE (15)
#define MAX_SSID_SIZE (20)
#define MAX_PWD_SIZE (15)
#define MAX_WIFI_SIZE (50)
#define MAX_ID_SIZE (160)

#define GOOUT_SERVER "52.78.126.50"
#define GOOUT_PORT 8080
#define TTS_SERVER "api.voicerss.org"
#define TTS_PORT 80
#define AP_SSID "Wiznet_TestAP"
#define AP_PWD "12345678"
#define FCM_SERVER "fcm.googleapis.com"
#define CLIENT_SSID "U+Net81F3"
#define CLIENT_PWD "4000005619"

#define PRINT_SERIAL 0

/*모드 관련 INDEX*/
#define IDLE_MODE 100
#define AP_MODE 0
#define DETECT_MODE 1
#define RECEIVE_DATA_MODE 2

/*RECEIVE_DATA_MODE INDEX*/
#define RECEIVE_INFO 0
#define RECEIVE_TTS 1
#define RECEIVE_FCM 2

/*LED System 관련 변수들*/
const byte powerByte = 0x40;
const byte ledAByte = 0x20;
const byte ledBByte = 0x10;
const byte ledCByte = 0x08;
const byte wifiByte = 0x04;
const byte allByte = 0x7C;
const byte delByte = 0x00;
const byte pirDetectByte = 0x38; //디버깅용
byte ledByte = 0x00; //입력 바이트
unsigned long humanDetectedTime; // 시간초과시 끄기 위한 변수
const int LED_ON_TIME = 30000;// 점등시간

/*HD System 관련 변수들*/
VL53L0X tofSnsr;
boolean triggerTag = false;
#define THRESHOLD  1000 // TOF센서 감지거리

// 통합 변수 코드
int status = WL_IDLE_STATUS;       // the Wifi radio's status

char buf[MAX_BUF_SIZE] = "";
char deviceId[MAX_ID_SIZE] = "fqNDpwVG6gU:APA91bFBApv065xsedzJuU7cLPrgtKbu2mLk_DLlDrritdhxvqh90SJpQpOBE95QNyTlljVbt5_cjvXDaVvAblJZ4YAyFaC9BbEjVYKafxvugjgKVmHhxj2sfPiaAaWW8m-nFlVQjfp4";
char pwd[MAX_PWD_SIZE] = "";

uint16_t content_length = 0;
uint16_t content_idx = 0;
bool isGetLength = false;
bool isStartContent = false;
bool isEndContent = false;
int bracketCount = 0;
int pos = 0;
char c;
int mode = 0;
int recv_mode = 0;
VS1003 player(XCS, XDCS, DREQ, XRST, SD_SELECT); //create an instance of the libary

WiFiServer APServer(8080);
WiFiClient client;

void setup() {
  Serial.begin(115200);
  /*LED pin 입,출력 할당*/
  pinMode(SHIFT_CLK, OUTPUT);
  pinMode(STORE_CLK, OUTPUT);
  pinMode(LED_DATA, OUTPUT);
  ledOFF(allByte);
  /*PIR SNSR 입력,출력 할당*/
  pinMode(PIR_A, INPUT);
  pinMode(PIR_B, INPUT);
  /* HDS 셋팅 */
  settingHDS();
  /* 오디오 셋팅 */
  audioInit();
  /* 모드 셋팅 */
  mode = DETECT_MODE;
  recv_mode = -1;
  /* 와이파이 셋팅 */
  //wifiInit(CLIENT_SSID, CLIENT_PWD);
  APServerStart();
  welcomeLight();  //부팅이 끝나면 웰컴라이트를 킨다
}

void loop() {
  if (Serial.available()) {
    c = (char)Serial.read();

    switch (c) {
      case 'H':
        triggerTag = true;
        break;

      case 'I':
        mode = IDLE_MODE;
        break;

      case 'R':
        Serial.println();
        mode = RECEIVE_DATA_MODE;
        recv_mode = RECEIVE_INFO;
        requestInfo(deviceId);
        break;

      case 'T':
        //Serial.println();
        //mode = RECEIVE_DATA_MODE;
        //recv_mode = RECEIVE_TTS;
        //requestTTS(TEXT);
        break;

      case 'F':
        Serial.println();
        mode = RECEIVE_DATA_MODE;
        recv_mode = RECEIVE_FCM;
        requestNoti(deviceId);
        break;
    }
  }
  overTimeLedOff(); //점등시간 외 led 끄는 메서드(주의! 반드시 loop문 초기에 있어야합니다)
  //형훈 : switch문 2중첩으로 가시성이 안좋아 분리했습니다.
  switch (mode) {
    case DETECT_MODE:
      operateDetectMode();
      break;
      
    case RECEIVE_DATA_MODE:
      operateReceiveDataMode();
      break;
      
    case AP_MODE:
      getWiFiInfo();
      break;
      
    case IDLE_MODE:
    
      break;
  }

}

void operateDetectMode() {
  //-----인체감지 판단순서-----//
  Serial.print(F("PIR SNSR STATE >>> "));
  Serial.println(getPIRValue());
  // 1.만약 PIR 센서에서 TRUE값이 들어오면
  if (getPIRValue()) {
    Serial.println(F("detect"));
    // 2. A,B,C led에 불을 켜고(디버깅 용)
    //    pirDetectLight();
    // 3.TOF 센서의 값을 읽어온다.
    int detectedRange = getTOFValue();
    // 4.만약 TOF센서값이 역치값보다 작을경우
    if (detectedRange < THRESHOLD)
    {
      Serial.print(F("TOF >>> "));
      Serial.println(detectedRange);
      // 4.모니터링을 하고, 모니터링 한 값을 태그값(triggerTag)에 넣는다.
      triggerTag = monitoring(THRESHOLD);
      triggerTag=true;
    } else {
      Serial.println(F("TOF >>> OVER THRESHOLD"));
    }
  } else {
    Serial.println(F("not detect"));
  }

  //------인체감지된 이후------//
  if (triggerTag) {
    Serial.println("HUMAN");
    //1.이전 led를 끄고
    ledOFF(delByte);
    
    if (requestInfo(deviceId)) {
      mode = RECEIVE_DATA_MODE;
      recv_mode = RECEIVE_INFO;
    }
    
    //2.led ON 한 시간을 기억해둔다
    humanDetectedTime = millis();
    //3.트리거를 끈다
    triggerTag = false;
    //4.모드를 변경한다
  }
}

void operateReceiveDataMode()
{
  if (recv_mode == RECEIVE_INFO)
  {
    //서버로부터 led값 전달받는 메서드 getLedDataFromSERVER(), ledSystem탭안에 작성 부탁해요,
    //리턴형태 byte/char(8bit)로 [A|B|C|0|0|0|0|0] 형태로 요청드립니다.
    //ex) A,B점등시 11000000, C만 점등시 00100000
    if (!isEndContent) {
      getInfoState();
    }
    else {
      ledON(processInfo());
      if (requestTTS(buf)) {
        recv_mode = RECEIVE_TTS;
      } else {
        mode = DETECT_MODE;
      }
    }
  }

  if (recv_mode == RECEIVE_TTS) {
    if (!isEndContent) {
      playTTS();
    }
    else {
      if (requestNoti(deviceId)) {
        recv_mode = RECEIVE_FCM;
      } else {
        mode = DETECT_MODE;
      }
      /*if (requestTTS(TEXT)) {
        recv_mode = RECEIVE_TTS;
      } else {
        mode = DETECT_MODE;
      }*/
    }
  }

  if (recv_mode == RECEIVE_FCM) {
    if (!isEndContent) {
      getFCMInfo();
    }
    else {
      client.stop();
      /*if (requestTTS(TEXT)) {
        recv_mode = RECEIVE_TTS;
      } else {
        mode = IDLE_MODE;
      }*/
      mode = DETECT_MODE;
    }
  }
}
