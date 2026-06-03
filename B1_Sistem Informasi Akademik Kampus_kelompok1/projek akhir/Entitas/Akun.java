package Entitas;

import Koneksi.Koneksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Akun {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.println("=== DATA AKUN ===\n");

        System.out.print("USERNAME: ");
        String username = input.nextLine().trim();

        System.out.print("PASSWORD: ");
        String password_akun = input.nextLine().trim();

        System.out.print("Role Anda (Mahasiswa/Dosen): ");
        String role_akun = input.nextLine().trim();

        String nim = null;
        String nidn = null;

        // ================= VALIDASI INPUT =================

        if (username.isEmpty()) {
            System.out.println("Username tidak boleh kosong!");
            return;
        }

        if (username.length() > 50) {
            System.out.println("Username maksimal 50 karakter!");
            return;
        }

        if (password_akun.isEmpty()) {
            System.out.println("Password tidak boleh kosong!");
            return;
        }

        if (password_akun.length() < 6) {
            System.out.println("Password minimal 6 karakter!");
            return;
        }

        if (password_akun.length() > 100) {
            System.out.println("Password maksimal 100 karakter!");
            return;
        }

        if (role_akun.isEmpty()) {
            System.out.println("Role tidak boleh kosong!");
            return;
        }

        role_akun =
                role_akun.substring(0, 1).toUpperCase()
                + role_akun.substring(1).toLowerCase();

        if (role_akun.equalsIgnoreCase("Mahasiswa")) {

            System.out.print("NIM: ");
            nim = input.nextLine().trim();

            if (nim.isEmpty()) {
                System.out.println("NIM tidak boleh kosong!");
                return;
            }

        } else if (role_akun.equalsIgnoreCase("Dosen")) {

            System.out.print("NIDN: ");
            nidn = input.nextLine().trim();

            if (nidn.isEmpty()) {
                System.out.println("NIDN tidak boleh kosong!");
                return;
            }

        } else {
            System.out.println("Role tidak valid!");
            return;
        }

        try {

            Connection conn = Koneksi.connect();

            if (conn == null) {
                System.out.println("Koneksi gagal!");
                return;
            }

            // ================= CEK USERNAME =================

            String cekUsername =
                    "SELECT COUNT(*) FROM b1.akun WHERE username = ?";

            try (PreparedStatement ps = conn.prepareStatement(cekUsername)) {

                ps.setString(1, username);

                ResultSet rs = ps.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Username sudah digunakan!");
                    return;
                }
            }

            // ================= CEK MAHASISWA =================

            if (role_akun.equals("Mahasiswa")) {

                String cekMahasiswa =
                        "SELECT COUNT(*) FROM b1.mahasiswa WHERE nim = ?";

                try (PreparedStatement ps = conn.prepareStatement(cekMahasiswa)) {

                    ps.setString(1, nim);

                    ResultSet rs = ps.executeQuery();

                    if (rs.next() && rs.getInt(1) == 0) {
                        System.out.println("NIM tidak ditemukan!");
                        return;
                    }
                }

                String cekAkunMahasiswa =
                        "SELECT COUNT(*) FROM b1.akun WHERE nim = ?";

                try (PreparedStatement ps = conn.prepareStatement(cekAkunMahasiswa)) {

                    ps.setString(1, nim);

                    ResultSet rs = ps.executeQuery();

                    if (rs.next() && rs.getInt(1) > 0) {
                        System.out.println("Mahasiswa sudah memiliki akun!");
                        return;
                    }
                }
            }

            // ================= CEK DOSEN =================

            if (role_akun.equals("Dosen")) {

                String cekDosen =
                        "SELECT COUNT(*) FROM b1.dosen WHERE nidn = ?";

                try (PreparedStatement ps = conn.prepareStatement(cekDosen)) {

                    ps.setString(1, nidn);

                    ResultSet rs = ps.executeQuery();

                    if (rs.next() && rs.getInt(1) == 0) {
                        System.out.println("NIDN tidak ditemukan!");
                        return;
                    }
                }

                String cekAkunDosen =
                        "SELECT COUNT(*) FROM b1.akun WHERE nidn = ?";

                try (PreparedStatement ps = conn.prepareStatement(cekAkunDosen)) {

                    ps.setString(1, nidn);

                    ResultSet rs = ps.executeQuery();

                    if (rs.next() && rs.getInt(1) > 0) {
                        System.out.println("Dosen sudah memiliki akun!");
                        return;
                    }
                }
            }

            // ================= INSERT DATA =================

            String query =
                    "INSERT INTO b1.akun(username, password_akun, role_akun, nim, nidn) " +
                    "VALUES(?, ?, ?::role_akun_enum, ?, ?)";

            try (PreparedStatement psAkun = conn.prepareStatement(query)) {

                psAkun.setString(1, username);
                psAkun.setString(2, password_akun);
                psAkun.setString(3, role_akun);
                psAkun.setString(4, nim);
                psAkun.setString(5, nidn);

                int baris = psAkun.executeUpdate();

                if (baris > 0) {

                    System.out.println("\n=== DATA AKUN BERHASIL DISIMPAN ===");
                    System.out.println("Username : " + username);
                    System.out.println("Role     : " + role_akun);

                    if (nim != null) {
                        System.out.println("NIM      : " + nim);
                    }

                    if (nidn != null) {
                        System.out.println("NIDN     : " + nidn);
                    }

                } else {
                    System.out.println("Data akun gagal disimpan.");
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "=== Terjadi kesalahan pada data! ===\n"
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }
}