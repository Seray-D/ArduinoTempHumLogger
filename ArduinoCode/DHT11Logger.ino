#include <Wire.h>
#include <LiquidCrystal_I2C.h>
#include <DHT.h>

// DHT ayarları
#define DHTPIN 3
#define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);

// LCD ekran (20x4, I2C adres 0x27)
LiquidCrystal_I2C lcd(0x27, 20, 4);

// Kırmızı LED pini
const int redLedPin = 14;
const int buzzerPin = 12;

// Sıcaklık ve nem eşik değerleri
const float tempLimit =30.0;
const float humLimit = 70.0;

void setup() {
  Serial.begin(9600);
  dht.begin();

  pinMode(redLedPin, OUTPUT);
  pinMode(buzzerPin, OUTPUT);

  lcd.init();
  lcd.backlight();
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("Starting DHT11...");
  delay(2000);
}

void loop() {
  float temperature = dht.readTemperature();
  float humidity = dht.readHumidity();

  lcd.clear();

  if (isnan(temperature) || isnan(humidity)) {
    Serial.println("Sensor read failed!");
    lcd.setCursor(0, 0);
    lcd.print("Sensor read error");
    delay(2000);
    return;
  }

  // LCD'ye sıcaklık ve nem değerlerini yaz
  lcd.setCursor(0, 0);
  lcd.print("Temp: ");
  lcd.print(temperature);
  lcd.print((char)223);  // derece sembolü
  lcd.print("C");

  lcd.setCursor(0, 1);
  lcd.print("Humidity: ");
  lcd.print(humidity);
  lcd.print(" %");

  // Eşik kontrolü
  if (temperature > tempLimit || humidity > humLimit) {
    digitalWrite(redLedPin, HIGH);
    digitalWrite(buzzerPin, HIGH);

    lcd.setCursor(0, 2);
    lcd.print("! LIMIT EXCEEDED !");

    lcd.setCursor(0, 3);
    if (temperature > tempLimit && humidity > humLimit) {
      lcd.print("Temp & Hum High");
    } else if (temperature > tempLimit) {
      lcd.print("Temp too high");
    } else {
      lcd.print("Humidity too high");
    }

    delay(5000);  // Uyarı süresi
    digitalWrite(buzzerPin, LOW);
  } else {
    digitalWrite(redLedPin, LOW);
    digitalWrite(buzzerPin, LOW);
  }

  // Javaya satırı gönder
  Serial.print(temperature, 2); // 2 ondalık basamak
  Serial.print(",");
  Serial.println(humidity, 2);

  delay(60000);  // 1 dakika bekle 60 000ms
}
