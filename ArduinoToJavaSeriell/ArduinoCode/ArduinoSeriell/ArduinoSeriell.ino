int value = 0;
int prtRPM = 0;
int stbRPM = 0;
int updateFrequenzy = 3; //Hz

int rnd1 = 0;
int rnd2 = 0;


void setup() {
  Serial.begin(9600);

}

void loop() {
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






  delay(1000 / updateFrequenzy);
}
