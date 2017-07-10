This library is for the VS1003 audio decoding chip:

INITALIZING CLASS:
  VS1003(XCS, XDCS, DREQ, RESET, CARDCS);

FUNCTIONS:
  PlaySong(fileName); plays a compatable file, returns nothing.
  SetVolume(leftchannel volume, rightchannel volume); set the volume, high number equals softer, returns nothing.
  begin() initializes the module and SD card.
  AutoPlay() plays all compatible files after each other.
