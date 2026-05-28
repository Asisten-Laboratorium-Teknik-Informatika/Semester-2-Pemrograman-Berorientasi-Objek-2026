package Entitas;

import Koneksi.Koneksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class Akun {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("=== DATA AKUN ===\n");

        System.out.print("USERNAME: ");
        String username = input.nextLine();

        System.out.print("Password: ");
        String password_akun = input.nextLine();

        System.out.print("Role (Mahasiswa/Dosen): ");
        String role_akun = input.nextLine();

        String nim = null;
        String nidn = null;

        role_akun =
        role_akun.substring(0,1).toUpperCase()
        + role_akun.substring(1).toLowerCase();

        if (role_akun.equalsIgnoreCase("Mahasiswa")) {
            System.out.print("NIM: ");
            nim = input.nextLine();

        } else if (role_akun.equalsIgnoreCase("Dosen")) {
            System.out.print("NIDN: ");
            nidn = input.nextLine();

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
            
            String query =
            "INSERT INTO b1.akun(username, password_akun, role_akun, nim, nidn) VALUES(?, ?, ?::role_akun_enum, ?, ?)";
            PreparedStatement ps_akun = conn.prepareStatement(query);

            ps_akun.setString(1, username);
            ps_akun.setString(2, password_akun);
            ps_akun.setString(3, role_akun);
            ps_akun.setString(4, nim);
            ps_akun.setString(5, nidn);

            int baris = ps_akun.executeUpdate();
            
            if (baris > 0) {
                System.out.println("===> Data berhasil ditambahkan! Silakan cek database.");
            }

        } catch (Exception e) {
            System.out.println("=== Terjadi kesalahan pada data! ===\n" + e.getMessage());
            e.printStackTrace();
        }
    }
}