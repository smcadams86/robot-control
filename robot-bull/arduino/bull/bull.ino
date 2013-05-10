
int leftMotor = 12;
int rightMotor = 13;
int leftMotorReversed = 11;
int rightMotorReversed = 10;

void setup() {
  Serial.begin(9600);
  pinMode(leftMotor, OUTPUT);
  pinMode(rightMotor, OUTPUT);
  pinMode(leftMotorReversed, OUTPUT);
  pinMode(rightMotorReversed, OUTPUT);
}

void loop() {
    //setLeftMotor(HIGH);
    //setRightMotor(HIGH);
  while(Serial.available() > 0) {
    char command = Serial.read();
    
    switch(command) {
    case 'L':
      setLeftMotorReversed(HIGH);
      setRightMotorReversed(LOW);
      setLeftMotor(HIGH);
      setRightMotor(HIGH);
      break;
    case 'R':
      setLeftMotorReversed(LOW);
      setRightMotorReversed(HIGH);
      setLeftMotor(HIGH);
      setRightMotor(HIGH);
      break;
    case 'F':
      setLeftMotorReversed(LOW);
      setRightMotorReversed(LOW);
      setLeftMotor(HIGH);
      setRightMotor(HIGH);
      break;
    case 'B':
      setLeftMotorReversed(HIGH);
      setRightMotorReversed(HIGH);
      setLeftMotor(HIGH);
      setRightMotor(HIGH);
      break;
    case 'S':
      setLeftMotor(LOW);
      setRightMotor(LOW);
      break; 
    }
  }
  /*
  digitalWrite(leftMotor, HIGH);
   delay(2000);
   digitalWrite(leftMotor, LOW);
   delay(2000);
   */

}

void setLeftMotor(char state) {
  digitalWrite(leftMotor, state); 
}

void setRightMotor(char state) {
 digitalWrite(rightMotor, state); 
}

void setLeftMotorReversed(char state) {
  digitalWrite(leftMotorReversed, state); 
}

void setRightMotorReversed(char state) {
 digitalWrite(rightMotorReversed, state); 
}

