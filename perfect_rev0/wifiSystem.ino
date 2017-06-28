void wifiInit(char* ssid, char* pwd) 
{
  Serial3.begin(115200);
  WiFi.init(&Serial3);
  
  content_length = 0;
  isGetLength = false;
  isStartContent = false;
  isEndContent = false;
  isStartData = false;

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
    status = WiFi.begin(ssid, pwd);
  }
}

bool requestDust(double lon, double lat) 
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
    return true;
  }
  else {
    Serial.println("Fail Connected to server");
    return false;
  }
}

bool requestInfo() {
  Serial.println(WiFi.macAddress());
  if (client.connect(gooutServer, gooutPort)) {
    requestStart = millis();
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
