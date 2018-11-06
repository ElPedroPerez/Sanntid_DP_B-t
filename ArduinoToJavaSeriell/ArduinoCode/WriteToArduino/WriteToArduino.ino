boolean dataRecieved = false;
String data = "";
void setup() {

  Serial.begin(9600);

}

void loop() {
  if (Serial.available() > 0)
  {
    data = Serial.readString();
    Serial.println(data);
    dataRecieved = true;
  }
  delay(100);

  while (dataRecieved)
  {
    Serial.println("Data recieved");
    Serial.println(data);
    delay(500);
  }
}
