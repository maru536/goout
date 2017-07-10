#ifndef VS1003_H
#define VS1003_H
#include <Arduino.h>
#include <SD.h>
#include "WizFi310.h"

class VS1003
{
  public:
    VS1003(const int temp_XCS,const int temp_XDCS,const int temp_DREQ,const int temp_RESET,const int temp_CARDCS);
    void begin(void);//Initializes the vs1003 chip
    void setVolume(unsigned char leftchannel, unsigned char rightchannel);//sets the volume
    bool needData(void);//returns if the VS1003 module wants data
    void useInterrupt(void);//enables interupt song playing
	  void checkRegisters(bool logData);//performs a check of the VS1003 module registers, if logData is true it will write to uSD card
	  void reset();//used to hardreset the VS1003 module
    void feedData(uint8_t *buffer, uint8_t buffsiz);//used to feed data to the VS1003 module
    void play();//plays the song if it is pause
    void pause();//pauses the song currently playing
    void stop();//stops the song
    void fillBuffer(void);//called by interupt to fill VS1003 buffer
	bool read(uint8_t* buf, uint8_t buf_size);
	bool firstRead(uint8_t* buf, uint8_t buf_size);
	int playStreamingAudio(WiFiClient _client, uint16_t _cur, uint16_t _length);
  private:
    #define SCI_MODE 0x00
    #define SCI_STATUS 0x01
    #define SCI_BASS 0x02
    #define SCI_CLOCKF 0x03
    #define SCI_DECODE_TIME 0x04
    #define SCI_AUDATA 0x05
    #define SCI_WRAM 0x06
    #define SCI_WRAMADDR 0x07
    #define SCI_HDAT0 0x08
    #define SCI_HDAT1 0x09
    #define SCI_AIADDR 0x0A
    #define SCI_VOL 0x0B
    #define SCI_AICTRL0 0x0C
    #define SCI_AICTRL1 0x0D
    #define SCI_AICTRL2 0x0E
    #define SCI_AICTRL3 0x0F
    #define VS1003_CONTROL_SPI_SETTING  SPISettings(250000,  MSBFIRST, SPI_MODE0)
    #define VS1003_DATA_SPI_SETTING     SPISettings(8000000, MSBFIRST, SPI_MODE0)

    #define VS1003_DATABUFFERLEN 32
    #define TRUE 1
    #define FALSE 0
    int replenishTime;
    int idleTime;

    unsigned int readRegister (unsigned char addressbyte);
    void writeRegister(unsigned char addressbyte, unsigned char highbyte, unsigned char lowbyte);

};

#endif
