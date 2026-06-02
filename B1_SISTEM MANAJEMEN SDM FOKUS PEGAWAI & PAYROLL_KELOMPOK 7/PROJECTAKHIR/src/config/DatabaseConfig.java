package config;
import java.sql.*;

public class DatabaseConfig {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/PROJECTAKHIR";
        return DriverManager.getConnection(url, "postgres", "12345");
    }
}
