void settingHDS() {
  //주의할것! i2c통신시 필요한 메서드
  Wire.begin();
  //TOF 센서 롱 레인지 셋팅 최대 200cm(2M)이내
  tofSnsr.init();
  tofSnsr.setTimeout(500);
  tofSnsr.setSignalRateLimit(0.1);
  tofSnsr.setVcselPulsePeriod(VL53L0X::VcselPeriodPreRange, 18);
  tofSnsr.setVcselPulsePeriod(VL53L0X::VcselPeriodFinalRange, 14);
}

boolean getPIRValue() {
  boolean a = digitalRead(PIR_A);
  boolean b = digitalRead(PIR_B);
  return a || b;
}

int getTOFValue() {
  int temp = tofSnsr.readRangeSingleMillimeters();
  if (tofSnsr.timeoutOccurred()) {
    temp = 9999;//setting 9999 timeout code
  }
  return temp;
}

//모니터링 메서드 >> 시간차를 두고 값을 10개 받아, 앞의 5개의 평균(avgFront)과 뒤에 5개 평균(avgRear)을 비교
//근접하고 있다고 판단(150mm이상 근접)된 경우 true를 리턴한다
boolean monitoring(int threshold) {
  int tofValue[10];

  for (int i = 0 ; i < 10 ; i++) {
    tofValue[i] = tofSnsr.readRangeSingleMillimeters();
    delay(50);
  }
  int avgFront = (int)(tofValue[0] + tofValue[1] + tofValue[2] + tofValue[3] + tofValue[4]) / 5;
  int avgRear = (int)(tofValue[5] + tofValue[6] + tofValue[7] + tofValue[8] + tofValue[9]) / 5;

  //Serial.print("avgFront >>> ");
  //Serial.println(avgFront);
  //Serial.print("avgRear >>> ");
  //Serial.println(avgRear);

  //만약 뒤의 평균 + 150mm의 값이 앞의 평균보다 작을경우
  if (avgFront > avgRear + 150)
  {
    return true;
  } else {
    return false;
  }
}


