package Entitas;

import Koneksi.Koneksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class ProgramStudi {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("=== DATA PROGRAM STUDI ===\n");

        System.out.print("ID program studi: ");
        String id_program_studi = input.nextLine();

        System.out.print("Nama program studi: ");
        String nama_program_studi = input.nextLine();

        System.out.print("Nama fakultas: ");
        String fakultas = input.nextLine();

        System.out.print("Jenjang (D3/D4/S1/S2/S3): ");
        String jenjang = input.nextLine();

        System.out.print("Akreditasi (Unggul/Baik sekali/Baik/Buruk): ");
        String akreditasi = input.nextLine();

        try {
            Connection conn = Koneksi.connect();
            
            if (conn == null) {
                System.out.println("Koneksi gagal!");
                return;
            }
            
            String query =
            "INSERT INTO b1.program_studi(id_program_studi, nama_program_studi, fakultas, jenjang, akreditasi) VALUES(?, ?, ?, ?::jenjang_enum, ?::akreditasi_enum)";
            PreparedStatement ps_program_studi = conn.prepareStatement(query);

            ps_program_studi.setString(1, id_program_studi);
            ps_program_studi.setString(2, nama_program_studi);
            ps_program_studi.setString(3, fakultas);
            ps_program_studi.setString(4, jenjang);
            ps_program_studi.setString(5, akreditasi);

            int baris = ps_program_studi.executeUpdate();
            
            if (baris > 0) {
                System.out.println("===> Data berhasil ditambahkan! Silakan cek database.");
            }

        } catch (Exception e) {
            System.out.println("=== Terjadi kesalahan pada data! ===\n" + e.getMessage());
            e.printStackTrace();
        }
    }
}