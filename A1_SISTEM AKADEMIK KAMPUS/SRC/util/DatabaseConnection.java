package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
        "jdbc:postgresql://localhost:5432/ProjectAkhir?currentSchema=kelompok4";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "CEMEN";

    // BUG FIX: jangan pakai static singleton — tiap panggil getConnection()
    // beri koneksi baru agar tidak tabrakan antar operasi & tidak stale.
    // Jika ingin connection pool, ganti dengan HikariCP atau c3p0.
    private DatabaseConnection() {}

    public static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("[DB] Koneksi ke PostgreSQL BERHASIL!");
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("[ERROR] Driver PostgreSQL tidak ditemukan!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("[ERROR] Gagal konek ke database!");
            System.err.println("URL      : " + URL);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tutup koneksi dengan aman — panggil di blok finally setelah selesai pakai.
     *
     * @param conn objek Connection yang akan ditutup; boleh null (diabaikan)
     *
     * Signature ini sengaja menerima parameter Connection — BUKAN tanpa argumen —
     * karena setiap pemanggil bertanggung jawab atas koneksinya sendiri.
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try { conn.close(); }
            catch (SQLException e) { e.printStackTrace(); }
        }
    }
}