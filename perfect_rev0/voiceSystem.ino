void audioInit() 
{
  player.begin();
  player.useInterrupt();
  //Set the volume
  player.setVolume(40,40);
}
