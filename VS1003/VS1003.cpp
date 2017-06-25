#include <Arduino.h>
#include <VS1003.h>
#include <SPI.h>
#include <SD.h>
#include "WizFi310.h"

int _XCS, _XDCS, _DREQ, _RESET, _CARDCS;
/*XCS:Control Chip Select Pin (for accessing SPI Control/Status registers)
XDCS:Data Chip Select / BSYNC Pin
DREQ:Data Request Pin: Player asks for more data
RESET:Reset is active low
*/
volatile boolean need_data = TRUE;//make volatile to use in interupt
static VS1003 *myself;
//if current streaming is finished
volatile bool streamingFinished = true;
//whether interupts are enabled or not
bool usingInterupts = false;
//whether song is playing or paused.
volatile bool playing = false;
WiFiClient WifiClient;
int cur;
int length;
bool isFirst = true;

VS1003::VS1003(const int temp_XCS,const int temp_XDCS,const int temp_DREQ,const int temp_RESET,const int temp_CARDCS)
{
  _XCS = temp_XCS;
  _XDCS = temp_XDCS;
  _DREQ = temp_DREQ;
  _RESET = temp_RESET;
  _CARDCS = temp_CARDCS;
};

void VS1003::begin(){
  pinMode(_DREQ, INPUT);//set DREQ as input so it can tell when the module is requesting more data
  pinMode(_XCS, OUTPUT);//set XCS as output
  pinMode(_XDCS, OUTPUT);//set XDCS as outpit
  pinMode(_RESET, OUTPUT);//set reset as output so it can reset the module

  SPI.begin();
  SPI.setBitOrder(MSBFIRST);
  SPI.setDataMode(SPI_MODE0);

  SPI.setClockDivider(SPI_CLOCK_DIV16); //Set SPI bus speed to 1MHz (16MHz / 16 = 1MHz)
  SPI.transfer(0xFF); //Throw a dummy byte at the bus

  //Initialize VS1053 chip
  digitalWrite(_RESET, HIGH);
  digitalWrite(_XCS, HIGH); //Deselect Control
  digitalWrite(_XDCS, HIGH); //Deselect Data
  SPI.setClockDivider(SPI_CLOCK_DIV32); //Set SPI bus speed to 1MHz (16MHz / 16 = 1MHz)
  SPI.transfer(0xFF); //Throw a dummy byte at the bus

  Serial.println(F("Initalizing VS1003 Chip!!"));//F() stores it in flash memory
  //max SCI reads are CLKI/7. Input clock is 12.288MHz.
  //Internal clock multiplier is 1.0x after power up.
  //Therefore, max SPI speed is 1.75MHz. We will use 1MHz to be safe.

  //Initialize VS1003 chip
  delay(10);
  digitalWrite(_RESET, HIGH); //Bring up VS1003
  setVolume(40, 40); //Set initial volume
  int Mode = readRegister(SCI_MODE);
  int Status = readRegister(SCI_STATUS);
  int Clock = readRegister(SCI_CLOCKF);
  //Serial.print(F("SCI_Mode (0x4800) = 0x"));
  Serial.println(Mode, HEX);
  //Serial.print(F("SCI_Status (0x48) = 0x"));
  Serial.println(Status, HEX);
  int vsVersion = (Status >> 4) & 0x000F; //Mask out only the four version bits
  //Serial.print(F("VS Version (VS1003 is 3) = "));
  Serial.println(vsVersion, DEC); //The 1003 should respond with 3. VS1001 = 0, VS1011 = 1, VS1002 = 2, VS1003 = 3
  //Serial.print(F("SCI_ClockF = 0x"));
  Serial.println(Clock, HEX);

  //Now that we have the VS1003 up and running, increase the internal clock multiplier and up our SPI rate
  writeRegister(SCI_CLOCKF, 0x80, 0x00); //Set multiplier to 3.0x
  //From page 12 of datasheet, max SCI reads are CLKI/7. Input clock is 12.288MHz.
  //Internal clock multiplier is now 3x.
  //Therefore, max SPI speed is 5MHz. 4MHz will be safe.
  SPI.setClockDivider(SPI_CLOCK_DIV16); //Set SPI bus speed to safe level
  Clock = readRegister(SCI_CLOCKF);
  //Serial.print(F("SCI_ClockF = 0x"));
  Serial.println(Clock, HEX);

  isFirst = true;

  //Vs1003 IC setup complete
};

static void feeder(void){
	if (digitalRead(_DREQ) && !streamingFinished && playing)
	{
		myself->fillBuffer();
	}
};

//Read the 16-bit value of a VS10xx register
unsigned int VS1003::readRegister (unsigned char addressbyte){
  while (!digitalRead(_DREQ)) ; //Wait for DREQ to go high indicating IC is available
  digitalWrite(_XCS, LOW); //Select control
  //SCI consists of instruction byte, address byte, and 16-bit data word.
  SPI.transfer(0x03); //Read instruction
  SPI.transfer(addressbyte);
  unsigned char response1 = SPI.transfer(0xFF); //Read the first byte
  unsigned char response2 = SPI.transfer(0xFF); //Read the second byte
  digitalWrite(_XCS, HIGH); //Deselect Control
  return ((unsigned int)response1 << 8) | (response2);
};

void VS1003::checkRegisters(bool logData){
	for (unsigned int i = 0; i <65536; i++){
		byte t = i;
		writeRegister(SCI_AICTRL0, i >> 8, t);
		delay(10);
		//Serial.print("SCI_AICTRL0 register, should be 0x");
		//Serial.print(i,HEX);
		unsigned int xx = readRegister(SCI_AICTRL0);
		//Serial.print(" , is 0x");
		Serial.println(xx, HEX);
	}
}

//Write to VS1003 register
//SCI: Data transfers are always 16bit. When a new SCI operation comes in
//DREQ goes low. We then have to wait for DREQ to go high again.
//XCS should be low for the full duration of operation.
void VS1003::writeRegister(unsigned char addressbyte, unsigned char highbyte, unsigned char lowbyte){
  //Wait for DREQ to go high indicating IC is available
  while (!digitalRead(_DREQ)) ;
  //Select control
  digitalWrite(_XCS, LOW);
  //SCI consists of instruction byte, address byte, and 16-bit data word.
  //Write instruction
  SPI.transfer(0x02);
  SPI.transfer(addressbyte);
  SPI.transfer(highbyte);
  SPI.transfer(lowbyte);
  //Wait for DREQ to go high indicating command is complete
  while (!digitalRead(_DREQ)) ;
  //Deselect Control
  digitalWrite(_XCS, HIGH);
};

//Set VS10xx Volume Register
void VS1003::setVolume(unsigned char leftchannel, unsigned char rightchannel){
  writeRegister(SCI_VOL, leftchannel, rightchannel);
};

//PlaySong pulls 32 byte chunks from the SD card and throws them at the VS1003
//We monitor the DREQ (data request pin). If it goes low then we determine if
//we need new data or not. If yes, pull new from SD card. Then throw the data
//at the VS1003 until it is full. This is all done through interupts
int VS1003::playStreamingAudio(WiFiClient _client, int _cur, int _length){
	WifiClient = _client;
	cur = _cur;
	length = _length;
  Serial.println("Streaming connect");
  need_data = TRUE;
  streamingFinished = false;
  if(usingInterupts){
    playing = true;
    while (!needData());
    //read new data and feeds it to the VS1003 module to intitate interupts
    fillBuffer();
    Serial.println("Playing using interupts");
  }
  else if(!usingInterupts){
    Serial.println("Playing manually");
    while(true){
      noInterrupts();
      if(needData()){
        fillBuffer();
      }
      if(streamingFinished){
        break;
      }
      interrupts();
      delay(5);
      //delay so that if the code uses interupts, they can be triggered often enough
      //as when it fills the buffer, spi operations disable interupts
    }
  }
  
  return cur;
};

bool VS1003::needData(){
  return digitalRead(_DREQ);
};

void VS1003::fillBuffer(void){
    uint8_t mp3DataBuffer[32]; //Buffer of 32 bytes. VS1003 can take 32 bytes at a go.
    while(needData()){
      //Go out to SD card and try reading 32 new bytes of the song
	  if (isFirst == true) {
		  if (!firstRead(mp3DataBuffer, sizeof(mp3DataBuffer))) {
          //Oh no! There is no data left to read!
          //Time to exit
		      while (!needData()); //Wait for DREQ to go high indicating transfer is complete
		      digitalWrite(_XDCS, HIGH); //Deselect Data
		      streamingFinished = true;
		      Serial.println("Streaming finished!!");
          playing = false;
        }
	  }
	  else {
		if (!read(mp3DataBuffer, sizeof(mp3DataBuffer))) {
          //Oh no! There is no data left to read!
          //Time to exit
		      while (!needData()); //Wait for DREQ to go high indicating transfer is complete
		      digitalWrite(_XDCS, HIGH); //Deselect Data
		      streamingFinished = true;
		      Serial.println("Streaming finished!!");
          playing = false;
        }
	  }
        
      feedData(mp3DataBuffer,sizeof(mp3DataBuffer));
      need_data = TRUE;
    }
};

bool VS1003::read(uint8_t* buf, uint8_t buf_size)
{
	if (cur >= length)
		return false;
	
	uint8_t data = 0;
	
	for (int i = 0; i < buf_size; i++) {
		if (cur < length && WifiClient.available()) {
			data = (uint8_t)WifiClient.read();
			cur++;
			buf[i] = data;
		}
		else {
			buf[i] = 0;
		}
	}
	
	return true;
}

bool VS1003::firstRead(uint8_t* buf, uint8_t buf_size)
{
	isFirst = false;
	if (cur >= length)
		return false;
	
	uint8_t data = 0;
	buf[0] = (uint8_t)'I';
	buf[1] = (uint8_t)'D';
	buf[2] = (uint8_t)'3';
	cur = 3;
	
	for (int i = 3; i < buf_size; i++) {
		if (cur < length && WifiClient.available()) {
			data = (uint8_t)WifiClient.read();
			cur++;
			buf[i] = data;
		}
		else {
			buf[i] = 0;
		}
	}
	
	return true;
}

void VS1003::pause(){
  Serial.println("Pausing current song...");
  playing = false;
};

void VS1003::play(){
  if(playing == false){
    Serial.println("Playing current song...");
    playing = true;
    while (!needData());
    fillBuffer();
  }
};

void VS1003::stop(){
  playing = false;
  streamingFinished = true;
  Serial.println("Streamming stopped...");
};

void VS1003::feedData(uint8_t *buffer, uint8_t buffsiz){
  SPI.beginTransaction(VS1003_DATA_SPI_SETTING);
  //Once DREQ is released (high) we now feed 32 bytes of data to the VS1003 from our SD read buffer
  digitalWrite(_XDCS, LOW); //Select Data
  for (int y = 0 ; y < buffsiz ; y++){
    SPI.transfer(buffer[y]); // Send SPI byte
  }
  digitalWrite(_XDCS, HIGH); //Deselect Data
  SPI.endTransaction();
};

void VS1003::useInterrupt(){
  myself = this;
  int8_t irq = digitalPinToInterrupt(_DREQ);//convert digital pin to interupt number
  //if the pin specified isn't an irq pin
  if(irq == -1)
  {
    //Serial.print("Error setting up interupt pin, pin " );
    //Serial.print(irq);
    Serial.println(" it not interupt compatable");
    usingInterupts = false;
    return;
  }

  //Serial.print("Using IRQ pin with interrupt on pin ");
  Serial.println(_DREQ);
  attachInterrupt(irq, feeder, CHANGE);//attach an interrupt to the _DREQ pin
  SPI.usingInterrupt(irq);//needed so interupts don't interupt SPI transactions
  usingInterupts = true;
};

void VS1003::reset(){
	digitalWrite(_RESET, LOW); //Put VS1003 into hardware reset
	SPI.setClockDivider(SPI_CLOCK_DIV16); //Set SPI bus speed to 1MHz (16MHz / 16 = 1MHz)
	SPI.transfer(0xFF); //Throw a dummy byte at the bus
						//Initialize VS1003 chip
	delay(10);
	digitalWrite(_RESET, HIGH); //Bring up VS1003
	writeRegister(SCI_CLOCKF, 0x60, 0x00); //Set multiplier to 3.0x
	SPI.setClockDivider(SPI_CLOCK_DIV4); //Set SPI bus speed to 4MHz (16MHz / 4 = 4MHz)
	setVolume(40, 40); //Set initial volume
};
