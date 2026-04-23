#include <Arduino.h>
#include <WiFi.h>
#include <HTTPClient.h>
#include  <ArduinoJson.h>

#include <Adafruit_SH110X.h>
#include <Wire.h>
#include <Adafruit_GFX.h>
#include <DHT.h>
const char* ssid = "P325-2";
const char* pass = "@123456789";
const char* ip = "http://192.168.1.8:8080/api/sensor/moisture";
// Cấu hình OLED
#define SCreen_Width 128
#define SCreen_Height 64
#define OLED_RESET -1
Adafruit_SH1106G display = Adafruit_SH1106G(SCreen_Width,SCreen_Height,&Wire, OLED_RESET);

// Cấu hình DHT11
#define DHTPIN 4
#define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);
// Cảm biến độ ẩm đất
const int pinAnalog = 34; //A0
const int pinDigital = 25;//D0



void setup() {
  Serial.begin(115200);
  // Khởi tạo DHT11
  dht.begin();
  // Khởi tạo OLED
  delay(350); 
  if (!display.begin(0x3C, true)) {
    Serial.println(F("SH110X fail"));
  }
  display.clearDisplay();
  display.display();

  // Kết nối WiFi
  WiFi.begin(ssid,pass);
  
  while(WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.print(".");
  }
  pinMode(pinDigital,INPUT); // D0 là chân nhập tín hiệu số
  Serial.println("\nWifi đã kết nối!");
  
}

void loop() {
  // 1. Đọc dữ liệu từ các cảm biến
  float temp = dht.readTemperature();
  float hum = dht.readHumidity();
  int soilValue = analogRead(pinAnalog); // cảm bien do am anlog digiatla
  int soilDigital = digitalRead(pinDigital);
  String soilStat = (soilDigital == HIGH) ? "KHO" : "UOT";

  // 2. Hiển thị lên màn hình OLED
  display.clearDisplay();
  display.setTextSize(1);
  display.setTextColor(SH110X_WHITE);
  display.setCursor(0, 0);
  display.println("AGRI-TECH PROJECT");

  display.println("---------------------");

  display.printf("Temp: %.1f C\n", temp);
  display.printf("Humi: %.1f %%\n", hum);
  display.printf("Soil: %d\n", soilValue);
  display.printf("Stat: %s\n", soilStat.c_str());
  display.display();

  // 3. Gửi dữ liệu qua Spring Boot (JSON)
  if(WiFi.status() == WL_CONNECTED) {
    HTTPClient Client; // Nen dung chu thuong cho ten doi tuong
    Client.begin(ip);
    Client.addHeader("Content-Type", "application/json");
    // Doc gia tri cam bien
    // doc gia tri A0
  // int giaTriAlog = analogRead(pinAnalog);
  // // doc trang thai  0 / 1
  // int giaDiaLog = digitalRead(pinDigital);


// Dong goi JSON (Dung JsonDocument cho phien ban moi) hoac  StaticJsonDocument

  JsonDocument doc;
    doc["sensorId"] = "ESP32_GARDEN_01";
    doc["temperature"] = temp;
    doc["humidity"] = hum;
    doc["soilMoisture"] = soilValue;
    doc["status"] = soilStat;
  String requestBody;
  serializeJson(doc,requestBody);

  // Gui du lieu
  int HttpResponse = Client.POST(requestBody);
  if (HttpResponse > 0) {
      Serial.printf("Gửi thành công, Code: %d\n", HttpResponse);
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

  // if (isnan(hum) || isnan(temp)) {
  //   Serial.println("Lỗi: Không đọc được dữ liệu từ DHT11! Hãy kiểm tra dây nối.");
  // } else {
  //   Serial.print("Độ ẩm: ");
  //   Serial.print(hum);
  //   Serial.print("%  |  Nhiệt độ: ");
  //   Serial.print(temp);
  //   Serial.println("°C");
  // }