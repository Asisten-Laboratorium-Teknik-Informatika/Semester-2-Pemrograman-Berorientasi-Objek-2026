package Entitas;

import Koneksi.Koneksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class MataKuliah {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("=== DATA MATA KULIAH ===\n");

        System.out.print("ID mata kuliah: ");
        String id_mata_kuliah = input.nextLine();

        System.out.print("ID program studi: ");
        String id_program_studi = input.nextLine();

        System.out.print("NIDN: ");
        String nidn = input.nextLine();

        System.out.print("Nama mata kuliah: ");
        String nama_mata_kuliah = input.nextLine();

        System.out.print("Jumlah SKS: ");
        String sks = input.nextLine();

        System.out.print("Semester: ");
        short semester_ke = input.nextShort();
        input.nextLine();
       
        try {
            Connection conn = Koneksi.connect();
            
            if (conn == null) {
                System.out.println("Koneksi gagal!");
                return;
            }
            
            String query =
            "INSERT INTO b1.mata_kuliah(id_mata_kuliah, id_program_studi, nidn, nama_mata_kuliah, sks, semester_ke) VALUES(?, ?, ?, ?, ?::sks_enum, ?)";
            PreparedStatement ps_mata_kuliah = conn.prepareStatement(query);

            ps_mata_kuliah.setString(1, id_mata_kuliah);
            ps_mata_kuliah.setString(2, id_program_studi);
            ps_mata_kuliah.setString(3, nidn);
            ps_mata_kuliah.setString(4, nama_mata_kuliah);
            ps_mata_kuliah.setString(5, sks);
            ps_mata_kuliah.setShort(6, semester_ke);

            int baris = ps_mata_kuliah.executeUpdate();
            
            if (baris > 0) {
                System.out.println("===> Data berhasil ditambahkan! Silakan cek database.");
            }

        } catch (Exception e) {
            System.out.println("=== Terjadi kesalahan pada data! ===\n" + e.getMessage());
            e.printStackTrace();
        }
    }
}