# Arduino Temperature and Humidity Logger

![Java CI Build](https://github.com/Seray-D/ArduinoTempHumLogger/actions/workflows/ci.yml/badge.svg)
![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)
![Java Version](https://img.shields.io/badge/Java-17%2B-orange.svg)
![Arduino](https://img.shields.io/badge/Arduino-C%2B%2B-teal.svg)

An enterprise-grade, end-to-end IoT data-logging solution. This system captures real-time environmental metrics via a DHT11 sensor connected to an Arduino microcontroller, renders live telemetry on an I2C LCD panel, and persists the data into a MySQL database utilizing a robust Java backend daemon.

---

## Project Architecture

```text
ArduinoTempHumLogger/
├── firmware/                 # Arduino C++ firmware source code
│   └── DHT11Logger/
│       └── DHT11Logger.ino
├── server/                   # Java backend ingestion service (Maven)
│   ├── src/main/java/        # Core serial reading & DB logic
│   ├── src/main/resources/   # Configuration templates
│   └── pom.xml               # Maven dependency management
└── .github/workflows/        # CI/CD automated build pipelines
```

## System Requirements & Tech Stack

* **Microcontroller:** Arduino Board (UNO, Nano, etc.)
* **Sensors & Displays:** DHT11 Temperature & Humidity Sensor, I2C LCD Display (20x4)
* **Backend Runtime:** Java 17+ / Maven
* **Database:** MySQL Server 8.0+
* **Core Libraries:** `jSerialComm` (Serial communication), `mysql-connector-j` (Database driver)

## Getting Started

### 1. Firmware Deployment

* Open `firmware/DHT11Logger/DHT11Logger.ino` using the Arduino IDE.
* Ensure required libraries (DHT sensor library and LiquidCrystal_I2C) are installed.
* Select your target board and COM port, then flash the firmware to your Arduino.

### 2. Database Initialization

Execute the following script in your MySQL environment to set up the persistence layer:

```sql
CREATE DATABASE IF NOT EXISTS sensor_db;
USE sensor_db;

CREATE TABLE readings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    temperature FLOAT NOT NULL,
    humidity FLOAT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 3. Backend Configuration & Execution

1. Navigate to the `server/` directory.
2. Create a configuration file named `config.properties` under `src/main/resources/` based on your local environment:

```properties
db.url=jdbc:mysql://localhost:3306/sensor_db
db.user=your_db_user
db.password=your_db_password
serial.port=COM3
baud.rate=9600
```

Compile and execute the application using Maven:

```bash
mvn clean compile exec:java
```

## License

Distributed under the **MIT License**. See `LICENSE` for more details.
