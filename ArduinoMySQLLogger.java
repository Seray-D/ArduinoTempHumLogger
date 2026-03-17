package ArduinoMySQLLogger;

import com.fazecast.jSerialComm.SerialPort;
import java.sql.*;
import java.util.Scanner;

public class ArduinoMySQLLogger {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/sensor_db?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "S7300#jqer";

    public static void main(String[] args) {
        SerialPort comPort = SerialPort.getCommPort("COM4");
        comPort.setBaudRate(9600);

        if (!comPort.openPort()) {
            System.err.println("❌ Seri port 'COM4' açılamadı!");
            System.err.println("Lütfen Arduino'nun bağlı olduğu doğru portu kontrol edin.");
            System.err.println("Aygıt Yöneticisi -> Bağlantı Noktaları kısmından COM numarasını kontrol wt.");
        
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Scanner scanner = new Scanner(comPort.getInputStream())) {

            System.out.println("✅ Veri kaydı başladı...");
           
            String sql = "INSERT INTO readings (temperature, humidity) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            while (true) {
                if (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    


                    if (line.equalsIgnoreCase("Error") || line.isEmpty()) {
                        System.out.println("⚠️ Sensor error veya boş satır geldi, atlanıyor.");
                        continue;
                    }

                    String[] parts = line.split(",");
                    if (parts.length != 2) {
                        System.out.println("❗ Veri formatı hatası: " + line);
                        continue;
                    }

                    try {
                        float temperature = Float.parseFloat(parts[0]);
                        float humidity = Float.parseFloat(parts[1]);

                        pstmt.setFloat(1, temperature);
                        pstmt.setFloat(2, humidity);
                        pstmt.executeUpdate();

                        System.out.printf("Eklendi: Sıcaklık=%.2f°C, Nem=%.2f%%\n", temperature, humidity);
                    } catch (NumberFormatException e) {
                        System.out.println("❗ Sayısal dönüşüm hatası: " + line);
                    }
                }
                Thread.sleep(100);  // Döngüyü 100 ms yavaşlat
            }
        } catch (SQLException e) {
            System.err.println("❌ Veritabanı bağlantı veya sorgu hatası:");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("❌ Thread kesintisi:");
            e.printStackTrace();
        } finally {
            comPort.closePort();
            System.out.println("Seri port kapatıldı.");
        }
        
    }
    
}
