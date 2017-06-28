//파라미터에 바이트 정의한것 넣으면 해당 led에 점등
void ledON(byte b)
{
  //OR연산을 통해 해당바이트에 쓴다
  ledByte |= b;
  //전원 LED는 항상 들어와야하므로 추가한다
  //  ledByte |= powerByte;
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
  //전원 LED는 항상 들어와야하므로 추가한다
  //  ledByte |= powerByte;
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

