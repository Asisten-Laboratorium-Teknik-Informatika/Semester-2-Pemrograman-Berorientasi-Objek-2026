package src.main.java.com.perpus.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {
    private static final String URL = "jdbc:postgresql://localhost:5432/SistemPerpustakaan";
    private static final String DB_USER = "postgres"; 
    private static final String DB_PASSWORD = "postgres"; 

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver PostgreSQL tidak ditemukan!", e);
        }
    }
}
