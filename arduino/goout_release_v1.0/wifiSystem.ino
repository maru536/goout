
void wifiInit(char* ssid, char* pwd)
{
  Serial3.begin(115200);
  WiFi.init(&Serial3);
  //initClient();
  
  // check for the presence of the shield
  if (WiFi.status() == WL_NO_SHIELD) {
    Serial.println(F("WiFi shield not present"));
    while (true);
  }

  // attempt to connect to WiFi network
  while ( status != WL_CONNECTED) {
    Serial.print(F("Attempting to connect to WPA SSID: "));
    Serial.println(ssid);
    // Connect to WPA/WPA2 network
    status = WiFi.begin(ssid, pwd);
  }

  Serial.println(F("You're connected to the network"));
  printWifiStatus();
  Serial.println();
  Serial.println(F("Starting connection to server..."));
  ledON(wifiByte);
}

void printWifiStatus()
{
  // print the SSID of the network you're attached to
  Serial.print(F("SSID: "));
  Serial.println(WiFi.SSID());
  // print your WiFi shield's IP address
  IPAddress ip = WiFi.localIP();
  Serial.print(F("IP Address: "));
  Serial.println(ip);
  // print the received signal strength
  long rssi = WiFi.RSSI();
  Serial.print(F("Signal strength (RSSI):"));
  Serial.print(rssi);
  Serial.println(F(" dBm"));
}


void APServerStart() {
  Serial3.begin(115200);
  WiFi.init(&Serial3);
  
  // check for the presence of the shield
  if (WiFi.status() == WL_NO_SHIELD) {
    Serial.println(F("WiFi shield not present"));
    while (true); // don't continue
  }

  Serial.print(F("Attempting to start AP "));
  Serial.println(AP_SSID);
  
  // start access point
  status = WiFi.beginAP(AP_SSID, 10, AP_PWD, WIZ_TYPE_WPA2_MIXED);

  Serial.println(F("Access point started"));

  // start the web server on port 80
  APServer.begin();
  Serial.println(F("Server started"));
  mode = AP_MODE;
}
void initClient() 
{
  Serial3.begin(115200);
  WiFi.init(&Serial3);
  status = 0;
  for(int i = 0; i < MAX_SOCK_NUM; i++) 
    WizFi310Drv::_state[i] = -1;
}

void clientStart(char* clientSsid, char* clientPwd) {
  initClient();

    // check for the presence of the shield
    if (WiFi.status() == WL_NO_SHIELD) {
        Serial.println(F("WiFi shield not present"));
        //SerialUSB.println("WiFi shield not present");
        // don't continue
        while (true);
    }

    // attempt to connect to WiFi network
    while ( status != WL_CONNECTED) {
        Serial.print(F("Attempting to connect to WPA SSID: "));
        Serial.println(clientSsid);
        // Connect to WPA/WPA2 network
        status = WiFi.begin(clientSsid, clientPwd);
    }

    // you're connected now, so print out the data
    Serial.println(F("You're connected to the network"));
    
    Serial.println();
    Serial.println(F("Starting connection to server..."));
    mode = DETECT_MODE;
}

void getWiFiInfo() {
  if (APServer.available()) {                               // if you get a client,
    client = APServer.available();
    Serial.println(F("New client"));             // print a message out the serial port
    bracketCount = pos = 0;
    while (client.connected()) {              // loop while the client's connected
      if (client.available()) {               // if there's bytes to read from the client,
        char c = client.read();               // read a byte, then

        // you got two newline characters in a row
        // that's the end of the HTTP request, so send a response
        if (c == '{') {
          bracketCount++; 
        } 
        else if (c == '}') {
          bracketCount--;
        }
              
        buf[pos++] = c;
        if (bracketCount == 0) {
          buf[pos] = 0;
          processWiFiJson();
                  
          if(strlen(buf) > 0 && strlen(deviceId) > 0 && strlen(pwd)) {
            Serial.println(F("Available"));
            sendHttpMacResponse(client);
            // close the connection
            client.stop();
            Serial.println(F("Client disconnected"));
            clientStart(buf, pwd);
            break;
          }
          else {
            Serial.println(F("InDefined Request!"));
          }
        }
      }
    }
  }
}

void sendHttpMacResponse(WiFiClient client)
{
    client.print(WiFi.macAddress());
}

void processWiFiJson() {
  char ssid[MAX_SSID_SIZE] = "";
  int len = strlen(buf);
  int i, j, k;
  for (i = 0; i < strlen(buf); i++) {
    if (buf[i] == '"') {
      if (buf[i + 1] == 'd' && buf[i + 2] == 'e' && buf[i + 3] == 'v'
       && buf[i + 4] == 'i' && buf[i + 5] == 'c' && buf[i + 6] == 'e'
        && buf[i + 7] == 'I' && buf[i + 8] == 'd' && buf[i + 9] == '"') {
        i += 10;
        for (j = i; j < i + 5; j++) {
          if (buf[j] == '"') {
            for (k = j + 1; k < len; k++) {
              if (buf[k] == '"') {
                break;
              }
              
              deviceId[k - j - 1] = buf[k];
            }
            deviceId[k - j - 1] = 0;
            i = k;
            break;
          }
        }
      } 
      else if (buf[i + 1] == 'p' && buf[i + 2] == 'w' && buf[i + 3] == 'd' && buf[i + 4] == '"') {
        i += 5;
        for (j = i; j < i + 5; j++) {
          if (buf[j] == '"') {
            for (k = j + 1; k < len; k++) {
              if (buf[k] == '"') {
                break;
              }
              pwd[k - j - 1] = buf[k];
            }
            pwd[k - j - 1] = 0;
            i = k;
            break;
          }
        }
      } 
      else if (buf[i + 1] == 's' && buf[i + 2] == 's' && buf[i + 3] == 'i'
       && buf[i + 4] == 'd' && buf[i + 5] == '"') {
        i += 6;
        for (j = i; j < i + 5; j++) {
          if (buf[j] == '"') {
            for (k = j + 1; k < len; k++) {
              if (buf[k] == '"') {
                break;
              }
              ssid[k - j - 1] = buf[k];
            }
            ssid[k - j - 1] = 0;
            i = k;
            break;
          }
        }
      }
    }
  }

  strcpy(buf, ssid);
}
