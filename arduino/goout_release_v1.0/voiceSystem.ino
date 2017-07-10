#define IS_ALNUM(ch) \
        ( ch >= 'a' && ch <= 'z' ) || \
        ( ch >= 'A' && ch <= 'Z' ) || \
        ( ch >= '0' && ch <= '9' ) || \
        ( ch >= '-' && ch <= '.' ) 

void audioInit()
{
  player.begin();
  player.useInterrupt();
  //Set the volume
  player.setVolume(0, 0);
}

bool requestTTS(char* text) {
  content_idx = 0;
  content_length = 0;
  isGetLength = false;
  isStartContent = false;
  isEndContent = false;
  if (client.connect(TTS_SERVER, TTS_PORT)) {
    char* url;
    url = url_encode(text);
    Serial.println(url);
    // Make a HTTP request
    client.print("GET /?key=fb82492d22b541ce8983bbea7d0d984b&hl=ko-kr&src=");
    client.print(url);
    client.println("&c=mp3&f=8khz_8bit_mono&r=1 HTTP/1.1");
    client.println("Host: api.voicerss.org");
    client.println();
    Serial.println(F("TTS Server Connected"));
    free(url);
    return true;
  }
  else {
    Serial.println(F("Fail to Connect TTS server"));
    return false;
  } 
}

void playTTS(){
  if (client.available()) {
    c = (char)client.read();
    
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
              buf[j] = 0;
              content_length = strToUint16(buf, sizeof(buf));
              isGetLength = true;
              break;
            }
            buf[j] = c;
          }
        }
        else {
          break;
        }
      }
    }

    if (isGetLength == true && isStartContent == false && c == '\n') {
      for (int i = 0; i < 3; i++) {
        if ((c = client.read()) == '\n') {
          isStartContent = true;
          content_idx = 0;
          content_idx = player.playStreamingAudio(client, content_idx, content_length);
        }
      }
    }

    if (isStartContent == true) {
      //content_idx++;
      if (content_idx >= content_length) {
        isEndContent = true;
      }
    }
  }

  // if the server's disconnected, stop the client
  if (!client.connected() || isEndContent == true) {
    Serial.println();
    Serial.println(F("Disconnecting from TTS Server..."));
    Serial.print(F("Content-Length: "));
    Serial.println(content_length);
    Serial.print(F("Content-Idx: "));
    Serial.println(content_idx);
    client.stop();
    isEndContent = true;
  } 
}

char* url_encode( const char* str ){
    int i, j = 0, len;
    char* tmp;
    
    len = strlen( str );
    tmp = (char*) malloc( (sizeof(char) * 3 * len) +1 );

    for( i = 0 ; i < len ; i++ ){
        if( IS_ALNUM( str[i] ) ) {
          tmp[j] = str[i];
        }
        else{
          snprintf( &tmp[j], 4, "%%%02X\n", (unsigned char)str[i] );
          j += 2;
        }
        j++;
    }
    
    tmp[j] = 0;
    return tmp;
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
