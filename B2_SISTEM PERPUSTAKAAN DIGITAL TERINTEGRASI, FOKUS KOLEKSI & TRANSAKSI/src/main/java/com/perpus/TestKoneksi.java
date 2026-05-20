package src.main.java.com.perpus;

import src.main.java.com.perpus.config.Koneksi;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class TestKoneksi {
    public static void main(String[] args) {
        System.out.println("=== PENGUJIAN KONEKSI DATABASE PERPUSTAKAAN ===");
        System.out.println("Sedang mencoba menghubungkan ke PostgreSQL...");

        // Mencoba mengambil koneksi dari kelas DatabaseConfig
        try (Connection conn = Koneksi.getConnection()) {
            
            // Jika tidak melempar SQLException, berarti koneksi sukses
            if (conn != null && !conn.isClosed()) {
                System.out.println("\n✓ KONEKSI BERHASIL!");
                System.out.println("----------------------------------------------");
                
                // Mengambil metadata informasi database sebagai bukti fisik koneksi aman
                DatabaseMetaData metaData = conn.getMetaData();
                System.out.println("Nama Database   : " + metaData.getDatabaseProductName());
                System.out.println("Versi Database  : " + metaData.getDatabaseProductVersion());
                System.out.println("Driver JDBC     : " + metaData.getDriverName());
                System.out.println("URL Koneksi     : jdbc:postgresql://localhost:5342/SistemPerpustakaan");
                System.out.println("----------------------------------------------");
                System.out.println("Status: Aplikasi Java siap berinteraksi dengan DB Perpustakaan.");
            }
            
        } catch (SQLException e) {
            System.out.println("\n KONEKSI GAGAL!");
            System.out.println("----------------------------------------------");
            System.out.println("Pesan Error: " + e.getMessage());
            System.out.println("----------------------------------------------");
            System.out.println("Silakan periksa kembali:");
            System.out.println("1. Apakah server PostgreSQL Anda sudah menyala (Running)?");
            System.out.println("2. Apakah port (cth: 5342 / 5432) di DatabaseConfig sudah sesuai?");
            System.out.println("3. Apakah USERNAME dan PASSWORD database sudah benar?");
            System.out.println("4. Apakah file driver 'postgresql-x.x.x.jar' sudah dimasukkan ke Referenced Libraries di VS Code?");
        }
    }
}
