package Entitas;

import Koneksi.Koneksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class Dosen {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("=== DATA DOSEN ===\n");

        System.out.print("ID program studi: ");
        String id_program_studi = input.nextLine();

        System.out.print("NIDN: ");
        String nidn = input.nextLine();

        System.out.print("Nama: ");
        String nama = input.nextLine();

        System.out.print("Akun E-Mail: ");
        String email = input.nextLine();

        System.out.print("Jenis kelamin (Laki-laki/Perempuan): ");
        String jenis_kelamin = input.nextLine();

        System.out.print("Nomor handphone (ketik 0 jika tidak ingin mengisi): ");
        String no_hp = input.nextLine();

        try {
            Connection conn = Koneksi.connect();
            
            if (conn == null) {
                System.out.println("Koneksi gagal!");
                return;
            }
            
            String query =
            "INSERT INTO b1.dosen(id_program_studi, nidn, nama, email, jenis_kelamin, no_hp) VALUES(?, ?, ?, ?, ?::jenis_kelamin_dosen_enum, ?)";
            PreparedStatement ps_dosen = conn.prepareStatement(query);

            ps_dosen.setString(1, id_program_studi);
            ps_dosen.setString(2, nidn);
            ps_dosen.setString(3, nama);
            ps_dosen.setString(4, email);
            ps_dosen.setString(5, jenis_kelamin);
            ps_dosen.setString(6, no_hp);

            int baris = ps_dosen.executeUpdate();
            
            if (baris > 0) {
                System.out.println("===> Data berhasil ditambahkan! Silakan cek database.");
            }

        } catch (Exception e) {
            System.out.println("=== Terjadi kesalahan pada data! ===\n" + e.getMessage());
            e.printStackTrace();
        }
    }
}