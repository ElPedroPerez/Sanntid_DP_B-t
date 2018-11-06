int value = 0;
int prtRPM = 0;
int stbRPM = 0;
int prtDeg = 0;
int stbDeg = 0;
int updateFrequenzy = 3; //Hz

int rnd1 = 0;
int rnd2 = 0;
int rnd3 = 0;
int rnd4 = 0;


void setup()
{
  Serial.begin(9600);
}

//<key:value>

void sendDataOverSeriell(char data[7])
{
  String dataString = "";
  dataString = String("<");

 // Serial.print("<");
  for (byte i = 0; i < sizeof(data); i++)
  {        
    dataString = String(dataString + data[i]); 
    if (i<sizeof(data)-1)
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
  char data[7];
  rnd1 = random(0, 999);
  rnd2 = random(0, 999);
  rnd3 = random(0, 360);
  rnd4 = random(0, 360);
  prtRPM = rnd1;
  stbRPM = rnd2;
  prtDeg = rnd3;
  stbDeg = rnd4;
  data[0] = "stbRPM";
  data[1] = stbRPM;
  data[2] = "prtRPM";
  data[3] = prtRPM;
  data[4] = "prtDeg";
  data[5] = prtDeg;
  data[6] = "stbDeg";
  data[7] = stbDeg;
  
  

  sendDataOverSeriell(data);
  delay(1000 / updateFrequenzy);
}


void oldCode()
{
  rnd1 = random(0, 999);
  rnd2 = random(0, 999);
  prtRPM = rnd1;
  stbRPM = rnd2;

  // PORT main engine
  Serial.print("<");
  if (prtRPM < 10 )
  {
    Serial.print("??");
  }
  if (prtRPM < 100 && prtRPM >= 10 )
  {
    Serial.print("?");
  }
  Serial.print("prtRPM=");
  Serial.print(prtRPM);
  Serial.print(":");

  //STB main engine

  if (stbRPM < 10 )
  {
    Serial.print("??");
  }
  if (stbRPM < 100 && stbRPM >= 10 )
  {
    Serial.print("?");
  }
  Serial.print("stbRPM=");
  Serial.print(stbRPM);
  Serial.print(">:");
}


