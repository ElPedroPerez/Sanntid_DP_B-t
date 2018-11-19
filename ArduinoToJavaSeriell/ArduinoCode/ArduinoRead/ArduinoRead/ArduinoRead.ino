#include <Servo.h>

int softSpeedPod = 0;
int cmd_speedSB = 0;
int cmd_speedPS = 0;
int cmd_podPosSB = 0;
int cmd_podPosPS = 0;
int cmd_thrusterSpeed = 0;

Servo podStbSpeedPWM;
Servo podPrtSpeedPWM;

//Pin outputs

//STB = A
//Port = B
static byte podStbRotationSpeedPin = 3;
static byte podPrtRotationSpeedPin = 11;
static byte thrusterSpeedPin = 10;

static byte podStbRotationDirectionPin = 12;
static byte podPrtRotationDirectionPin = 13;
static byte thrusterDirectionPin = 7;

static byte podStbSpeedPin = 5;
static byte podPrtSpeedPin = 6;


const int case_softSpeedPod = 0;
const int case_cmd_podPosSB = 0;
const int case_cmd_podPosPS = 0;
const int case_cmd_thrusterSpeed = 0;

// Example 5 - Receive with start- and end-markers combined with parsing

const byte numChars = 32;
char receivedChars[numChars];
char tempChars[numChars];        // temporary array for use when parsing

// variables to hold the parsed data
char messageFromPC[numChars] = {0};
int integerFromPC = 0;
float floatFromPC = 0.0;

boolean newData = false;
String keyData[10];
int valueData[10];
int arraySize = 0;

//============

void setup() {

  podStbSpeedPWM.attach(podStbSpeedPin);
  podPrtSpeedPWM.attach(podPrtSpeedPin);

  podStbSpeedPWM.write(700);
  podPrtSpeedPWM.write(700);
  Serial.begin(9600);

  pinMode(podStbRotationSpeedPin, OUTPUT);
  pinMode(podPrtRotationSpeedPin, OUTPUT);
  pinMode(thrusterSpeedPin, OUTPUT);

  pinMode(podStbRotationDirectionPin, OUTPUT);
  pinMode(podPrtRotationDirectionPin, OUTPUT);
  pinMode(thrusterDirectionPin, OUTPUT);


  pinMode(podStbSpeedPin, OUTPUT);
  pinMode(podPrtSpeedPin, OUTPUT);


  Serial.println("This demo expects 3 pieces of data - text, an integer and a floating point value");
  Serial.println("Enter data in this style <HelloWorld, 12, 24.7>  ");
  Serial.println();


}

//============

void loop() {
  recvWithStartEndMarkers();
  if (newData == true) {
    strcpy(tempChars, receivedChars);
    // this temporary copy is necessary to protect the original data
    //   because strtok() used in parseData() replaces the commas with \0
    parseData();
    showParsedData();
    newData = false;
  }
}

void updateData()
{
  analogWrite(podStbRotationSpeedPin, 0);


}

//============

void recvWithStartEndMarkers() {
  static boolean recvInProgress = false;
  static byte ndx = 0;
  char startMarker = '<';
  char endMarker = '>';
  char rc;

  while (Serial.available() > 0 && newData == false) {
    rc = Serial.read();

    if (recvInProgress == true) {
      if (rc != endMarker) {
        receivedChars[ndx] = rc;
        ndx++;
        if (ndx >= numChars) {
          ndx = numChars - 1;
        }
      }
      else {
        receivedChars[ndx] = '\0'; // terminate the string
        recvInProgress = false;
        ndx = 0;
        newData = true;
      }
    }

    else if (rc == startMarker) {
      recvInProgress = true;
    }
  }
}

//============

void parseData() {      // split the data into its parts

  char * strtokIndx; // this is used by strtok() as an index
  strtokIndx = strtok(tempChars, ":");
  int i = 0;
  String str = "";
  //while (strtokIndx != NULL)
  // {

  //strtokIndx = strtok(tempChars, ":");     // get the first part - the string
  strcpy(messageFromPC, strtokIndx); // copy it to messageFromPC
  str = messageFromPC;



  strtokIndx = strtok(NULL, ":"); // this continues where the previous call left off
  integerFromPC = atoi(strtokIndx);     // convert this part to an integer

  //}

  if (str.equals("softSpeedPod"))
  {
    integerFromPC = map(integerFromPC, 0, 100, 700, 2000);
    integerFromPC = constrain(integerFromPC, 700, 2000);
    podStbSpeedPWM.write(integerFromPC);
    podPrtSpeedPWM.write(integerFromPC);
  }

  if (str.equals("case_cmd_podPosSB"))
  {
    if (integerFromPC == 0)
    {
      analogWrite(podStbRotationSpeedPin, 0);
    }

    if (integerFromPC == 1)
    {
      digitalWrite(podStbRotationDirectionPin, true);
      analogWrite(podStbRotationSpeedPin, 255);
    }

    if (integerFromPC == 2)
    {
      digitalWrite(podStbRotationDirectionPin, false);
      analogWrite(podStbRotationSpeedPin, 255);

    }
  }

  if (str.equals("case_cmd_podPosPS"))
  {
    if (integerFromPC == 0)
    {
      analogWrite(podPrtRotationSpeedPin, 0);
    }

    if (integerFromPC == 1)
    {
      digitalWrite(podPrtRotationDirectionPin, true);
      analogWrite(podPrtRotationSpeedPin, 255);
    }

    if (integerFromPC == 2)
    {
      digitalWrite(podPrtRotationDirectionPin, false);
      analogWrite(podPrtRotationSpeedPin, 255);

    }
  }

if (str.equals("case_cmd_thrusterSpeed"))
  {
    if (integerFromPC == 0)
    {
      analogWrite(thrusterSpeedPin, 0);
    }

    if (integerFromPC == 1)
    {
      digitalWrite(thrusterDirectionPin, true);
      analogWrite(thrusterSpeedPin, 255);
    }

    if (integerFromPC == 2)
    {
      digitalWrite(thrusterDirectionPin, false);
      analogWrite(thrusterSpeedPin, 255);

    }
  }
  
}

//============

void showParsedData() {

  Serial.println("--------------------------------------");
  Serial.println(arraySize);
  Serial.print("Message ");
  Serial.println(messageFromPC);
  Serial.print("Integer ");
  Serial.println(integerFromPC);

}
