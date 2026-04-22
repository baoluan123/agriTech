#include <Arduino.h>
#include <WiFi.h>
#include <HTTPClient.h>
#include  <ArduinoJson.h>
const char* ssid = "P325-2";
const char* pass = "@123456789";

const char* ip = "http://192.168.1.8:8080/api/sensor/moisture";

const int pinAnalog = 34; //A0
const int pinDigital = 25;//D0



void setup() {
  Serial.begin(115200);
  WiFi.begin(ssid,pass);
  while(WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.print(".");
  }
  pinMode(pinDigital,INPUT); // D0 là chân nhập tín hiệu số
  Serial.println("\nWifi đã kết nối!");
  
}

void loop() {
  if(WiFi.status() == WL_CONNECTED) {
    HTTPClient Client; // Nen dung chu thuong cho ten doi tuong
    Client.begin(ip);
    Client.addHeader("Content-Type", "application/json");
    // Doc gia tri cam bien
    // doc gia tri A0
  int giaTriAlog = analogRead(pinAnalog);
  // doc trang thai  0 / 1
  int giaDiaLog = digitalRead(pinDigital);
// Dong goi JSON (Dung JsonDocument cho phien ban moi) hoac  StaticJsonDocument
  JsonDocument doc;
  doc["sensorId"] = "ESP32_GARDEN_01";
  doc["value"] = giaTriAlog;
  doc["status"] = (giaDiaLog == HIGH) ? "KHO" : "UOT"; // Gui luon trang thai qua cho ngau
  String requestBody;
  serializeJson(doc,requestBody);

  // Gui du lieu
  int HttpResponse = Client.POST(requestBody);
  if (HttpResponse > 0) {
      Serial.printf("Gửi thành công, Code: %d\n", HttpResponse,giaTriAlog);
    } else {
      Serial.printf("Lỗi gửi dữ liệu: %s\n",Client.errorToString(HttpResponse).c_str());
    }
    Client.end();

  
  } else {
    Serial.println("Mat ket noi Wifi, dang thu lai...");
    WiFi.begin(ssid, pass);
  }
  delay(10000);
}

 // in ra so sanh du lieu
  // Serial.print("Dữ liệu (A0): ");
  // Serial.print(giaTriAlog);
  // Serial.print(" | Báo động (D0): ");
  // if(giaDiaLog == HIGH) {
  //   Serial.println("-> [!] DAT DANG KHO");
  // } else {
  //   Serial.println("-> [!] DAT DANG uot");
  // }
  // }