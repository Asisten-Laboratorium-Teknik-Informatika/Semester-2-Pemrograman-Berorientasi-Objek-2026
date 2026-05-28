package Fitur;

import Koneksi.Koneksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Login {
    Connection conn = Koneksi.connect();
    Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        Login lg = new Login();
        lg.loginUser();
    }

    public void loginUser() {
        Scanner input = Dashboard.input;
        boolean berhasil = false;

        while (!berhasil) {
        try {
            // Tampilan header login pakai Tampilan.java
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║             SELAMAT DATANG           ║");
            System.out.println("║     Sistem Informasi Akademik USU    ║");
            System.out.println("╚══════════════════════════════════════╝");

            System.out.print("Username: ");
            String username = input.nextLine();
            System.out.print("Password: ");
            String password = input.nextLine();

            String sql =
                "SELECT a.username, a.role_akun AS role, a.nim, a.nidn, " +
                "CASE " +
                "WHEN a.role_akun = 'Mahasiswa' THEN m.nama " +
                "WHEN a.role_akun = 'Dosen' THEN d.nama " +
                "END AS nama " +
                "FROM b1.akun a " +
                "LEFT JOIN b1.mahasiswa m ON a.nim = m.nim " +
                "LEFT JOIN b1.dosen d ON a.nidn = d.nidn " +
                "WHERE a.username = ? AND a.password_akun = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Simpan semua data ke Session
                Session.username = rs.getString("username");
                Session.role     = rs.getString("role");
                Session.nama     = rs.getString("nama");
                Session.nim      = rs.getString("nim");
                Session.nidn     = rs.getString("nidn");

                Tampilan.sukses("Login berhasil! Selamat datang, " + Session.nama + ".");
                Dashboard.menu();
            } else {
                Tampilan.gagal("Username atau password salah!");
                loginUser();
            }

        } catch (Exception e) {
            Tampilan.gagal("Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
        }
    }
}