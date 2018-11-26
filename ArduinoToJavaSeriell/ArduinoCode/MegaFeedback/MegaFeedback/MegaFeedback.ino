/*
  Rotary Encode Speed

  Calculate the speed from pulses by an rotary encoder or other pulsesensor.

  https://github.com/BenTommyE/BenRotaryEncoder/
  This example code is in the public domain.

  modified 27 Mars 2017
  by Ben-Tommy Eriksen
*/


#define ENCODER_A_SB 18
#define ENCODER_B_SB 19

#define ENCODER_A_PS 20
#define ENCODER_B_PS 21

volatile unsigned int counterSBSpeed = 0;  //This variable will increase on the rotation of encoder
volatile  int counterSBAngle = 0;


volatile unsigned int counterPSSpeed = 0;
volatile  int counterPSAngle = 0;

byte const updateFrequenzy = 45; //Hz
unsigned long lastTime = 0;         // will store last time value was sendt
const long interval = 100;         // interval at which to send result in milli second.  1000 ms = 1 second
byte const dataSize = 8;

void setup() {
  Serial.begin (115200);

  pinMode(2, INPUT);           // set pin to input
  digitalWrite(2, HIGH);       // turn on pullup resistors

  pinMode(3, INPUT);
  digitalWrite(3, HIGH);

  pinMode(ENCODER_B_PS, INPUT);
  digitalWrite(ENCODER_B_PS, HIGH);

  pinMode(ENCODER_A_PS, INPUT);
  digitalWrite(ENCODER_A_PS, HIGH);

  pinMode(ENCODER_B_SB, INPUT);
  digitalWrite(ENCODER_B_SB, HIGH);

  pinMode(ENCODER_A_SB, INPUT);
  digitalWrite(ENCODER_A_SB, HIGH);

  //Setting up interrupt
  //A rising pulse from encodenren activated ai0(). AttachInterrupt 0 is DigitalPin nr 2 on moust Arduino.
  attachInterrupt(0, ai0, RISING);
  attachInterrupt(1, ai1, RISING);
  attachInterrupt(2, ai2, RISING);
  attachInterrupt(3, ai3, RISING);
  attachInterrupt(4, ai4, RISING);
  attachInterrupt(5, ai5, RISING);

}

void loop() {

  // Chedk if it's time to sendt data
  if (timeIntervall()) {
    sendData();
    resetSampling();

  }
}

void ai0()
{
  // ai0 is activated if DigitalPin nr 2 is going from LOW to HIGH

  counterSBSpeed++;
}

void ai1()
{
  counterPSSpeed++;
}

void ai2()
{
  if (digitalRead(ENCODER_A_PS) == LOW) {
    counterPSAngle++;
  } else {
    counterPSAngle--;
  }
  if (counterPSAngle > 2000)
  {
    counterPSAngle = 0;
  }
  if (counterPSAngle < 0)
  {
    counterPSAngle = 2000;
  }
}

void ai3()
{
  if (digitalRead(ENCODER_B_PS) == LOW) {
    counterPSAngle--;
  } else {
    counterPSAngle++;
  }
  if (counterPSAngle > 2000)
  {
    counterPSAngle = 0;
  }
  if (counterPSAngle < 0)
  {
    counterPSAngle = 2000;
  }
}


void ai4()
{
  if (digitalRead(ENCODER_A_SB) == LOW) {
    counterSBAngle++;
  } else {
    counterSBAngle--;
  }
  if (counterSBAngle > 2000)
  {
    counterSBAngle = 0;
  }
  if (counterSBAngle < 0)
  {
    counterSBAngle = 2000;
  }
}

void ai5()
{
  if (digitalRead(ENCODER_B_SB) == LOW) {
    counterSBAngle--;
  } else {
    counterSBAngle++;
  }
  if (counterSBAngle > 2000)
  {
    counterSBAngle = 0;
  }
  if (counterSBAngle < 0)
  {
    counterSBAngle = 2000;
  }
}



void sendData() {
  //delay(1000 / updateFrequenzy);
  int degreePS = map(counterPSAngle, 0, 2000, 0, 359);
  int degreeSB = map(counterSBAngle, 0, 2000, 0, 359);

  //  Serial.print("Pulse pr second = ");
  //  Serial.print(counter);      // Sending cout / time



  //rpm = ((counter * 60) / 2000);
  //float speedOut = map(counter, 0, 2000, 0, 100);      // change from 400 pulse pr / second to 100 m/h
  double speedOutSB = (counterSBSpeed / 16.00) * 10.00 * 60.00;
  double speedOutPS = (counterPSSpeed / 16.00) * 10.00 * 60.00;

  // Serial.print("\t RPM_PS = ");
  // Serial.println(speedOutSB);
  // Serial.print("\t PS Angle = ");
  // Serial.println(degreePS);

  //Serial.print("\t RPM_SB = ");
  //Serial.println(counterPSSpeed);
  // Serial.print("\t SB Angle = ");
  //Serial.println(degreeSB);

  parseData(speedOutSB, speedOutPS, degreePS, degreeSB);
}

void resetSampling() {
  counterSBSpeed = 0;
  counterPSSpeed = 0;
}

boolean timeIntervall() {
  unsigned long currentTime = millis();

  // Chedk if it's time to make an interupt
  if (currentTime - lastTime >= interval) {
    lastTime = lastTime + interval;

    return true;
  } else if (currentTime < lastTime) {
    // After 50 day millis() will start 0 again
    lastTime = 0;

  } else {
    return false;
  }

}

void sendDataOverSeriell(String data[8])
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

void parseData(int speedOutSB, int speedOutPS, int degreePS, int degreeSB)
{
  String data[8];
  data[0] = "fb_speedPS";
  data[1] = String(speedOutPS);
  data[2] = "fb_speedSB";
  data[3] = String(speedOutSB);
  data[4] = "fb_podPosPS";
  data[5] = String(degreeSB);
  data[6] = "fb_podPosSB";
  data[7] = String(degreeSB);


  //Serial.println("Sending");
  sendDataOverSeriell(data);
}

