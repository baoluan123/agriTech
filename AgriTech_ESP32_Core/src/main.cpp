#include <Arduino.h>
#include <Wire.h>

void setup() {
  Serial.begin(115200);
  delay(2000); // Đợi 2 giây cho ổn định
  Serial.println(">>> BUOC 1: Kiem tra Serial OK");

  Wire.begin(); 
  Serial.println(">>> BUOC 2: Khoi tao I2C OK");

  // Quét thử xem có thiết bị nào đang cắm vào I2C không
  byte error, address;
  Serial.println(">>> BUOC 3: Dang quet thiet bi I2C...");
  
  address = 0x3C; // Địa chỉ OLED
  Wire.beginTransmission(address);
  error = Wire.endTransmission();

  if (error == 0) Serial.println(">>> KET QUA: Tim thay OLED tai 0x3C!");
  else Serial.println(">>> KET QUA: KHONG tim thay OLED!");
}

void loop() {
  Serial.print(".");
  delay(1000);
}