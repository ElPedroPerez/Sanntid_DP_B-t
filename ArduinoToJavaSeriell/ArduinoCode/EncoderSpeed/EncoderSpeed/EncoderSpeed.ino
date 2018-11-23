/*
  Rotary Encode Speed

  Calculate the speed from pulses by an rotary encoder or other pulsesensor.

  https://github.com/BenTommyE/BenRotaryEncoder/
  This example code is in the public domain.

  modified 27 Mars 2017
  by Ben-Tommy Eriksen
*/

volatile unsigned int counterSBSpeed = 0;  //This variable will increase on the rotation of encoder
volatile unsigned int counterSBAngle = 0;


volatile unsigned int counterPSSpeed = 0;
volatile unsigned int counterPSAngle = 0;


unsigned long lastTime = 0;         // will store last time value was sendt
const long interval = 1000;         // interval at which to send result in milli second.  1000 ms = 1 second


void setup() {
  Serial.begin (9600);

  pinMode(2, INPUT);           // set pin to input
  digitalWrite(2, HIGH);       // turn on pullup resistors

  pinMode(3, INPUT);
  digitalWrite(3, HIGH);

  pinMode(21, INPUT);
  digitalWrite(21, HIGH);

  pinMode(20, INPUT);
  digitalWrite(20, HIGH);

  pinMode(19, INPUT);
  digitalWrite(19, HIGH);

  pinMode(18, INPUT);
  digitalWrite(18, HIGH);

  //Setting up interrupt
  //A rising pulse from encodenren activated ai0(). AttachInterrupt 0 is DigitalPin nr 2 on moust Arduino.
  attachInterrupt(0, ai0, RISING);
  attachInterrupt(1, ai1, RISING);
  attachInterrupt(21, ai2, RISING);
  attachInterrupt(20, ai3, RISING);
  attachInterrupt(19, ai4, RISING);
  attachInterrupt(18, ai5, RISING);

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
  counterPSSpeed++
}

void ai2()
{
  if (digitalRead(21) == LOW) {
    counter++;
  } else {
    counter--;
  }
  if (counter > 2000)
  {
    counter = 0;
  }
  if (counter < 0)
  {
    counter = 2000;
  }
}

void ai3()
{
  if (digitalRead(20) == LOW) {
    counter--;
  } else {
    counter++;
  }
  if (counter > 2000)
  {
    counter = 0;
  }
  if (counter < 0)
  {
    counter = 2000;
  }
}



void sendData() {
  Serial.print("Pulse pr second = ");
  Serial.print(counter);      // Sending cout / time



  //rpm = ((counter * 60) / 2000);
  //float speedOut = map(counter, 0, 2000, 0, 100);      // change from 400 pulse pr / second to 100 m/h
  double speedOutSB = (counterSBSpeed / 16.00) * 60.00;
  double speedOutPS = (counterPSSpeed / 16.00) * 60.00;

  Serial.print("\t RPM_PS = ");
  Serial.println(speedOutSB);

  Serial.print("\t RPM_SB = ");
  Serial.println(counterPSSpeed);

}

void resetSampling() {
  counter = 0;

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
