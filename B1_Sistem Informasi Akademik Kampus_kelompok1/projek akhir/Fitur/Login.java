package Fitur;

import Koneksi.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Login {

    public static void main(String[] args) {
        new Login().loginUser();
    }

    public void loginUser() {
        Scanner input = Dashboard.input;

        while (true) {
            try {
                System.out.println("╔══════════════════════════════════════╗");
                System.out.println("║           SELAMAT DATANG DI          ║");
                System.out.println("║     Sistem Informasi Akademik USU    ║");
                System.out.println("╚══════════════════════════════════════╝");

                System.out.print("Username: "); String username = input.nextLine();
                System.out.print("Password: "); String password = input.nextLine();

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

                try (Connection conn = Koneksi.connect();
                     PreparedStatement ps = conn.prepareStatement(sql)) {

                    ps.setString(1, username);
                    ps.setString(2, password);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        Session.setUsername(rs.getString("username"));
                        Session.setRole(rs.getString("role"));
                        Session.setNama(rs.getString("nama"));
                        Session.setNim(rs.getString("nim"));
                        Session.setNidn(rs.getString("nidn"));

                        Tampilan.sukses("Login berhasil! Selamat datang, " + Session.getNama() + ".\n");
                        Dashboard.menu();
                        return;
                    } else {
                        Tampilan.gagal("Username atau password salah!");
                    }
                }

            } catch (Exception e) {
                Tampilan.gagal("=== Terjadi kesalahan pada data! ===\n" + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}