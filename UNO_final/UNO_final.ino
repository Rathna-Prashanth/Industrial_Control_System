#include <LiquidCrystal.h>

const int rs = 12, en = 11, d4 = 5, d5 = 4, d6 = 3, d7 = 2;
LiquidCrystal lcd(rs, en, d4, d5, d6, d7);
char data = 0;
const int buttonPin1 = 2;
const int buttonPin2 = 3;
const int buttonPin3 = 4;
const int buttonPin4 = 5;
const int buttonPin5 = 6;
const int buttonPin6 = 7;
const int buttonPin7 = 8;
const int buttonPin8 = 9;

char command;
String string;

void setup() {
  lcd.begin(16, 2);
  Serial.begin(9600);
  pinMode(buttonPin1, OUTPUT);
  pinMode(buttonPin2, OUTPUT);
  pinMode(buttonPin3, OUTPUT);
  pinMode(buttonPin4, OUTPUT);
  pinMode(buttonPin5, OUTPUT);
  pinMode(buttonPin6, OUTPUT);
  pinMode(buttonPin7, OUTPUT);
  pinMode(buttonPin8, OUTPUT);
 
}

void loop() {
  if (Serial.available() > 0) {
    string = "";
    while (Serial.available() > 0) {
      command = ((byte)Serial.read());
      if (command == ':')
        break;
      else
        string.concat(command);
      delay(1);
    }
    lcd.clear();
    if (string == "aa") {
      digitalWrite(buttonPin1, HIGH);
      lcd.print("Turning ON");
      lcd.setCursor(0,1);
      lcd.print("Switch 1");
    } else if (string == "bb") {
      digitalWrite(buttonPin2, HIGH);
      lcd.print("Turning ON");
      lcd.setCursor(0,1);
      lcd.print("Switch 2");  
    } else if (string == "cc") {
      digitalWrite(buttonPin3, HIGH);
      lcd.print("Turning ON");
      lcd.setCursor(0,1);
      lcd.print("Switch 3");  
    } else if (string == "dd") {
      digitalWrite(buttonPin4, HIGH);
      lcd.print("Turning ON");
      lcd.setCursor(0,1);
      lcd.print("Switch 4");  
    }   
     else if (string == "qq") {
      digitalWrite(buttonPin1, LOW);
      lcd.print("Turning OFF");
      lcd.setCursor(0,1);
      lcd.print("Switch 1");  
    } else if (string == "ww") {
      digitalWrite(buttonPin2, LOW);
      lcd.print("Turning OFF");
      lcd.setCursor(0,1);
      lcd.print("Switch 2");  
    } else if (string == "ee") {
      digitalWrite(buttonPin3, LOW);
      lcd.print("Turning OFF");
      lcd.setCursor(0,1);
      lcd.print("Switch 3");  
    } else if (string == "rr") {
      digitalWrite(buttonPin4, LOW);
      lcd.print("Turning OFF");
      lcd.setCursor(0,1);
      lcd.print("Switch 4");  
    } 
  } 
}
