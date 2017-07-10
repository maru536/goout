//파라미터에 바이트 정의한것 넣으면 해당 led에 점등
void ledON(byte b)
{
  //OR연산을 통해 해당바이트에 쓴다
  ledByte |= b;
  //전원 LED는 항상 들어와야하므로 추가한다
  ledByte |= powerByte;
  //레지스터 동작
  digitalWrite(STORE_CLK, false);
  shiftOut(LED_DATA, SHIFT_CLK, LSBFIRST, ledByte);
  digitalWrite(STORE_CLK, true);
}

//파라미터에 바이트 정의한것 넣으면 해당 led에 소등
void ledOFF(byte b)
{
  //파리미터의 바이트를 끈다
  ledByte &= ~b;
  //레지스터 동작
  digitalWrite(STORE_CLK, false);
  shiftOut(LED_DATA, SHIFT_CLK, LSBFIRST, ledByte);
  digitalWrite(STORE_CLK, true);
}

void welcomeLight()
{
  for (int i = 0 ; i < 3 ; i++)
  {
    digitalWrite(STORE_CLK, false);
    shiftOut(LED_DATA, SHIFT_CLK, LSBFIRST, allByte);
    digitalWrite(STORE_CLK, true);
    delay(300);

    digitalWrite(STORE_CLK, false);
    shiftOut(LED_DATA, SHIFT_CLK, LSBFIRST, delByte);
    digitalWrite(STORE_CLK, true);
    delay(300);
  }
}

void wifiWarningLight()
{
  ledON(wifiByte);
  delay(200);
  ledOFF(wifiByte);
  delay(200);
}

void pirDetectLight()
{
  digitalWrite(STORE_CLK, false);
  shiftOut(LED_DATA, SHIFT_CLK, LSBFIRST, pirDetectByte);
  digitalWrite(STORE_CLK, true);
  //  delay(300);
}

void overTimeLedOff() {
  if (humanDetectedTime)
  {
    // 현재시간과 비교해서 역치값 이상일시
    if ((millis() - humanDetectedTime) > LED_ON_TIME)
    {
      // LED를 전부 끈다
      ledOFF(ledByte);
      humanDetectedTime = 0;
    }
  }
  ledON(powerByte);
}

bool requestInfo(char* deviceId)
{
  pos = 0;
  bracketCount = 0;
  isEndContent = false;
  isStartContent = false;
  if (client.connect(GOOUT_SERVER, GOOUT_PORT)) {
    client.print("GET /led_sentence?deviceId=");
    client.print(deviceId);
    client.println(" HTTP/1.1");
    client.println("Host: 1");
    client.println();
    
    Serial.println(F("Request to Text server"));
    return true;
  }
  else {
    Serial.println(F("Text Connect Fail"));
    return false;
  }
}

void getInfoState() {
  if (client.available()) {
    c = (char)client.read();

    if (!isStartContent) {
      if (c == '{') {
        bracketCount++;
        isStartContent = true;
        buf[pos++] = c;
      }
    }
    else {
      // you got two newline characters in a row
      // that's the end of the HTTP request, so send a response
      if (bracketCount != 0) {
        if (c == '{') {
          bracketCount++; 
        } 
        else if (c == '}') {
          bracketCount--;
        }
        
        buf[pos++] = c;
        if (bracketCount == 0) {
          buf[pos++] = 0;
          isEndContent = true;
        }
      }
    }
  }

  if (!client.connected() || isEndContent == true) {
    Serial.println();
    Serial.println(F("Info Response done!"));
    isEndContent = true;
    client.stop();
  }
}

byte processInfo()
{
  bool first, second, third;

  processJson(&first, &second, &third);

  if (first) {
    //Serial.println(F("First On"));
    ledByte |= 1 << 5;
  }

  if (second) {
    //Serial.println(F("Second On"));
    ledByte |= 1 << 4;
  }

  if (third) {
    //Serial.println(F("Third On"));
    ledByte |= 1 << 3;
  }

  //Serial.println(buf);
  
  return ledByte;
}

bool requestNoti(char* id)
{
  pos = 0;
  bracketCount = 0;
  isEndContent = false;
  isStartContent = false;
  Serial.println(FCM_SERVER);
  if (client.connectSSL("fcm.googleapis.com", 443)) {
    strcpy(buf, "{\"to\": \"");
    strcat(buf, id);
    strcat(buf, "\"}");
    //strcpy(buf, "{\"to\": \"ft24r0IhzZc:APA91bE09vVIdleEtYPK5tCOvzzVFvek3mzPYKIy_WmyoHM96k7s5n9l0KJDDIHHp4S8t1Z27lBGymtW17Ze4DAtI0zY6vVb-n8SpMwOF1kc3YGA5mlAzgCmbeMAaZyWTOv4aUuy2l8I\", \"data\": {\"message\": \"test1\"}}");
    client.println(F("POST /fcm/send HTTP/1.1"));
    client.println(F("Host: fcm.googleapis.com"));
    client.println(F("Content-Type: application/json"));
    client.println(F("Authorization: key=AAAA5sg2OQw:APA91bE_cHSew6Je4CcAcFbGq__QmpsXt_jyYMoUQt9vhgMDarqWlE2wopdp3TCSz_FD9mvg_Kjvf_82zcEn9hJ20mNx-FVlNAgc9NND29U9DNGSyBJmmeqfA5gDw-dkpKy2Vt10b_xd"));
    client.print("Content-Length: ");
    client.println(strlen(buf));
    client.println();
    client.println(buf); 
    
    
    Serial.println(F("Request to FCM server"));
    return true;
  }
  else {
    Serial.println(F("FCM Connect Fail"));
    return false;
  }
}

void getFCMInfo() {
  if (client.available()) {
    c = (char)client.read();

    if (!isStartContent) {
      if (c == '{') {
        bracketCount++;
        isStartContent = true;
        buf[pos++] = c;
      }
    }
    else {
      // you got two newline characters in a row
      // that's the end of the HTTP request, so send a response
      if (bracketCount != 0) {
        if (c == '{') {
          bracketCount++; 
        } 
        else if (c == '}') {
          bracketCount--;
        }
        
        buf[pos++] = c;
        if (bracketCount == 0) {
          buf[pos] = 0;
          isEndContent = true;
        }
      }
    }
  }

  if (!client.connected() || isEndContent == true) {
    Serial.println();
    Serial.println(F("Noti Response done!"));
    isEndContent = true;
    client.stop();
  }
}

void processJson(bool* first, bool* second, bool* third) {
  char id[MAX_ID_SIZE] = "";
  int len = strlen(buf);
  int i, j, k;
  for (i = 0; i < strlen(buf); i++) {
    if (buf[i] == '"') {
      if (buf[i + 1] == 'f' && buf[i + 2] == 'i' && buf[i + 3] == 'r'
       && buf[i + 4] == 's' && buf[i + 5] == 't' && buf[i + 6] == 'L'
        && buf[i + 7] == 'E' && buf[i + 8] == 'D' && buf[i + 9] == '"') {
        i += 10;
        for (j = i; j < i + 5; j++) {
          if (buf[j] == 't') {
            *first = true;
            i = j + 4;
            break;
          }
          else if (buf[j] == 'f') {
            *first = false;
            i = j + 5;
            break;
          }
        }
      } 
      else if (buf[i + 1] == 's' && buf[i + 2] == 'e' && buf[i + 3] == 'c'
       && buf[i + 4] == 'o' && buf[i + 5] == 'n' && buf[i + 6] == 'd'
        && buf[i + 7] == 'L' && buf[i + 8] == 'E' && buf[i + 9] == 'D' && buf[i + 10] == '"') {
        i += 11;
        for (j = i; j < i + 5; j++) {
          if (buf[j] == 't') {
            *second = true;
            i = j + 4;
            break;
          }
          else if (buf[j] == 'f') {
            *second = false;
            i = j + 5;
            break;
          }
        }
      } 
      else if (buf[i + 1] == 't' && buf[i + 2] == 'h' && buf[i + 3] == 'i'
       && buf[i + 4] == 'r' && buf[i + 5] == 'd' && buf[i + 6] == 'L'
        && buf[i + 7] == 'E' && buf[i + 8] == 'D' && buf[i + 9] == '"') {
        i += 10;
        for (j = i; j < i + 5; j++) {
          if (buf[j] == 't') {
            *third = true;
            i = j + 4;
            break;
          }
          else if (buf[j] == 'f') {
            *third = false;
            i = j + 5;
            break;
          }
        }
      } 
      else if (buf[i + 1] == 's' && buf[i + 2] == 'e' && buf[i + 3] == 'n'
       && buf[i + 4] == 't' && buf[i + 5] == 'e' && buf[i + 6] == 'n'
        && buf[i + 7] == 'c' && buf[i + 8] == 'e' && buf[i + 9] == '"') {
        i += 10;
        for (j = i; j < i + 5; j++) {
          if (buf[j] == '"') {
            for (k = j + 1; k < len; k++) {
              if (buf[k] == '"') {
                break;
              }
              if (buf[k] == ',') {
                id[k - j - 1] = ' ';
              }
              else {
                id[k - j - 1] = buf[k];
              }
            }
            id[k - j - 1] = 0;
            break;
          }
        }
      }
    }
  }

  strcpy(buf, id);
  Serial.println(buf);
}


