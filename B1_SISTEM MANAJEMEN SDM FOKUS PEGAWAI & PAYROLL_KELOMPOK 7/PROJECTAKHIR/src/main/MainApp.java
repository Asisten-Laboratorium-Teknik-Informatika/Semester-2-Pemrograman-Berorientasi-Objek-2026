package main;
import model.*;
import config.DatabaseConfig;
import java.sql.*;
import java.util.Scanner;

public class MainApp {
    private static User currentUser;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            if (currentUser == null) {
                System.out.println("\n=== HR MANAGEMENT SYSTEM KELOMPOK 7 ===");
                System.out.println("1. Login Sistem\n2. Registrasi Pegawai Baru\n3. Keluar");
                String p = scanner.nextLine();
                if (p.equals("1")) login();
                else if (p.equals("2")) Pegawai.registrasiMandiri();
                else System.exit(0);
            } else {
                if (!currentUser.tampilkanMenu()) currentUser = null;
            }
        }
    }

    private static void login() {
        System.out.print("Email   : "); String em = scanner.nextLine();
        System.out.print("Password: "); String ps = scanner.nextLine();

        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "SELECT u.*, p.id_departemen, p.id_jabatan FROM kel7.user_akun u " + 
                        "LEFT JOIN kel7.pegawai p ON u.id_akun = p.id_akun " +
                        "WHERE u.email = ? AND u.password = ? AND u.is_approved = TRUE";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, em); pst.setString(2, ps);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id_akun");
                String nama = rs.getString("nama");
                String role = rs.getString("role");
                int deptId = rs.getInt("id_departemen");
                int jabId = rs.getInt("id_jabatan");

                if (role.equals("operator")) {
                    currentUser = new Operator(id, nama, em);
                } else if (role.equals("bos")) {
                    currentUser = new Bos(id, nama, em);
                } else {
                    if (deptId == 1) { 
                        currentUser = new Keuangan(id, nama, em, jabId); 
                    } else {
                        currentUser = new Pegawai(id, nama, em);
                    }
                }
                System.out.println("Login Sukses! Selamat datang, " + nama);
            } else {
                System.out.println("Gagal Login! Data salah atau akun belum disetujui Admin.");
            }
        } catch (Exception e) { System.out.println("Koneksi Database Gagal: " + e.getMessage()); }
    }
}