import java.sql.*;

public class Database {
    private static Connection conn;
    
    public static Connection getConn() {
        if (conn == null) {
            try {
                String url = "jdbc:postgresql://localhost:5432/dbku";
                String user = "postgres";
                String pass = "postgres";
                conn = DriverManager.getConnection(url, user, pass);
                System.out.println("Koneksi database berhasil");
            } catch (SQLException e) {
                System.out.println("Gagal konek database: " + e.getMessage());
            }
        }
        return conn;
    }
    
    public static void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Koneksi ditutup");
            }
        } catch (SQLException e) {
            System.out.println("Error tutup koneksi: " + e.getMessage());
        }
    }
}