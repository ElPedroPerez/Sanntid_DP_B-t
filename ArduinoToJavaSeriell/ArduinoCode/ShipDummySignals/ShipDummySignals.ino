int value = 0;

int fb_speedSB = 1000;
int fb_speedPS = 0;
int cmd_speedSB = 0;
int cmd_speedPS = 0;


int fb_podPosSB = 180;
int fb_podPosPS = 0;
int cmd_podPosSB = 0;
int cmd_podPosPS = 0;
int fb_speedPodRotPS = 0;
int fb_heading = 75;


int const updateFrequenzy = 10; //Hz
int const dataSize = 10;

void setup()
{
  Serial.begin(115200);
  delay(2000);
  //Serial.println("Starting up");
}

//<key:value>

void sendDataOverSeriell(String data[10])
{
  String dataString = "";
  dataString = String("<");

  //Serial.println(dataSize);

  // Serial.print("<");
  for (byte i = 0; i < 10; i++)
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
  String data[10];


  fb_speedPS = fb_speedPS + 1;
  fb_speedSB = fb_speedSB + 1;
  fb_podPosPS = fb_podPosPS + 1;
  fb_podPosSB = fb_podPosSB + 1;
  fb_heading = fb_heading +1;
  //fb_speedPodRotPS = fb_speedPodRotPS +1;



  if (fb_speedPS >= 6000)
  {
    fb_speedPS = 0;
  }
  if (fb_speedSB >= 6000)
  {
    fb_speedSB = 0;
  }
  if (fb_podPosSB >= 360)
  {
    fb_podPosSB = 0;
  }
  if (fb_podPosPS >= 360)
  {
    fb_podPosPS = 0;
  }
  if (fb_speedPodRotPS >= 100)
  {
    fb_speedPodRotPS = 0;
  }
  if (fb_heading >= 360)
  {
    fb_heading = 0;
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
  data[8] = "fb_heading";
  data[9] = String (fb_heading);

  //Serial.println("Sending");
  sendDataOverSeriell(data);
  delay(1000 / updateFrequenzy);
}



