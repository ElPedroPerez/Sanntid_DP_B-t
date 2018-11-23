#define outputA 5
#define outputB 3
int counter = 0;
int aState;
int aLastState;
int psPodPosDegrees = 0;
int sbPodPosDegrees = 0;

void setup() {
  pinMode (outputA, INPUT);
  pinMode (outputB, INPUT);

  Serial.begin (9600);
  // Reads the initial state of the outputA
  aLastState = digitalRead(outputA);
}
void loop() {
  aState = digitalRead(outputA); // Reads the "current" state of the outputA
  // If the previous and the current state of the outputA are different, that means a Pulse has occured
  if (aState != aLastState) {
    // If the outputB state is different to the outputA state, that means the encoder is rotating clockwise
    if (digitalRead(outputB) != aState) {
      counter ++;
    } else {
      counter --;
    }
    if (counter > 410)
    {
      counter = 0;
    }
    if (counter < 0)
    {
      counter = 410;
    }

    Serial.print("Position: ");
    //constrain(counter, 0, 1000);
    //counter = map(counter, 0, 360, 0, 420);
    
    Serial.println(counter);
  }
  aLastState = aState; // Updates the previous state of the outputA with the current state
  //int degree = map(counter, 0, 1000, 0, 360);
  //Serial.println(counter);
}

