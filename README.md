
# Arduino Temperature and Humidity Logger

This project measures temperature and humidity using a DHT11 sensor with Arduino, displays the data on an LCD screen, and logs it into a MySQL database.

## Project Structure

- ArduinoCode/DHT11Logger.ino → Arduino code to read sensor data and display on LCD  
- ArduinoMySQLLogger.java → Java code to read Arduino serial data and log into MySQL  

## Requirements

- Arduino IDE  
- Arduino board (UNO, Nano, etc.)  
- DHT11 sensor  
- LCD display (I2C 20x4)  
- Java 8+  
- MySQL database  
- jSerialComm library  

## Usage

1. Upload `DHT11Logger.ino` to Arduino.  
2. Create MySQL database `sensor_db` and table `readings`:

```sql
CREATE TABLE readings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    temperature FLOAT,
    humidity FLOAT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
