int value = 0;

int fb_speedSB = 0;
int fb_speedPS = 50;
int cmd_speedSB = 80;
int cmd_speedPS = 80;


int fb_podPosSB = 0;
int fb_podPosPS = 50;
int cmd_podPosSB = 180;
int cmd_podPosPS = 200;


int const updateFrequenzy = 100; //Hz
int const dataSize = 8;

void setup()
{
  Serial.begin(57600);
  delay(2000);
  //Serial.println("Starting up");
}

//<key:value>

void sendDataOverSeriell(String data[dataSize])
{
  String dataString = "";
  dataString = String("<");

  //Serial.println(dataSize);

  // Serial.print("<");
  for (byte i = 0; i < dataSize; i++)
  {
    dataString = String(dataString + data[i]);
    if (i < dataSize - 1)
    {
      dataString = String(dataString + ":");
    }
    // dataString = String(dataString );
  }
  dataString = String(dataString + ">");
  Serial.print(dataString);



}

void loop()
{
  String data[dataSize];


  fb_speedPS = fb_speedPS + 1;
  fb_speedSB = fb_speedSB + 1;
  fb_podPosPS = fb_podPosPS + 1;
  fb_podPosSB = fb_podPosSB + 1;



  if (fb_speedPS >= 2000)
  {
    fb_speedPS = 0;
  }
  if (fb_speedSB >= 2000)
  {
    fb_speedSB = 50;
  }
  if (fb_podPosSB >= 360)
  {
    fb_podPosSB = 0;
  }
  if (fb_podPosPS >= 360)
  {
    fb_podPosPS = 0;
  }



  //Serial.println(prtRPM);

  data[0] = "fb_speedPS";
  data[1] = String(fb_speedPS);
  data[2] = "fb_speedSB";
  data[3] = String(fb_speedSB);
  data[4] = "fb_podPosPS";
  data[5] = String(fb_podPosPS);
  data[6] = "fb_podPosSB";
  data[7] = String(fb_podPosSB);

  //Serial.println("Sending");
  sendDataOverSeriell(data);
  delay(1000 / updateFrequenzy);
}



