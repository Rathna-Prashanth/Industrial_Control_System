#include <LiquidCrystal.h>

const int rs = 12, en = 11, d4 = 5, d5 = 4, d6 = 3, d7 = 2;
LiquidCrystal lcd(rs, en, d4, d5, d6, d7);
char data = 0;
const int buttonPin1 = 6;
const int buttonPin2 = 11;
const int buttonPin3 = 7;
const int buttonPin4 = 8;
const int buttonPin5 = 9;
const int buttonPin6 = 10;
const int buttonPin7 = 13;


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
    if (string == "n1") {
      digitalWrite(buttonPin1, HIGH);
      lcd.print("Turning ON");
      lcd.setCursor(0,1);
      lcd.print("Switch 1");
    } else if (string == "n2") {
      digitalWrite(buttonPin2, HIGH);
      lcd.print("Turning ON");
      lcd.setCursor(0,1);
      lcd.print("Switch 2");  
    } else if (string == "n3") {
      digitalWrite(buttonPin3, HIGH);
      lcd.print("Turning ON");
      lcd.setCursor(0,1);
      lcd.print("Switch 3");  
    } else if (string == "n4") {
      digitalWrite(buttonPin4, HIGH);
      lcd.print("Turning ON");
      lcd.setCursor(0,1);
      lcd.print("Switch 4");  
    }   
    else if (string == "n5") {
      digitalWrite(buttonPin5, HIGH);
      lcd.print("Turning ON");
      lcd.setCursor(0,1);
      lcd.print("Switch 4");  
    }   
    else if (string == "n6") {
      digitalWrite(buttonPin6, HIGH);
      lcd.print("Turning ON");
      lcd.setCursor(0,1);
      lcd.print("Switch 4");  
    }   
    else if (string == "n7") {
      digitalWrite(buttonPin7, HIGH);
      lcd.print("Turning ON");
      lcd.setCursor(0,1);
      lcd.print("Switch 4");  
    }   

    
     else if (string == "o1") {
      digitalWrite(buttonPin1, LOW);
      lcd.print("Turning OFF");
      lcd.setCursor(0,1);
      lcd.print("Switch 1");  
    } else if (string == "o2") {
      digitalWrite(buttonPin2, LOW);
      lcd.print("Turning OFF");
      lcd.setCursor(0,1);
      lcd.print("Switch 2");  
    } else if (string == "o3") {
      digitalWrite(buttonPin3, LOW);
      lcd.print("Turning OFF");
      lcd.setCursor(0,1);
      lcd.print("Switch 3");  
    } else if (string == "o4") {
      digitalWrite(buttonPin4, LOW);
      lcd.print("Turning OFF");
      lcd.setCursor(0,1);
      lcd.print("Switch 4");  
    } 
    else if (string == "o5") {
      digitalWrite(buttonPin5, LOW);
      lcd.print("Turning OFF");
      lcd.setCursor(0,1);
      lcd.print("Switch 4");  
    } 
    else if (string == "aa") {
      digitalWrite(buttonPin6, LOW);
      lcd.print("Turning OFF");
      lcd.setCursor(0,1);
      lcd.print("Switch 4");  
    } 
    else if (string == "o7") {
      digitalWrite(buttonPin7, LOW);
      lcd.print("Turning OFF");
      lcd.setCursor(0,1);
      lcd.print("Switch 4");  
    } 
  } 
}
