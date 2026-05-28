package Entitas;

import Koneksi.Koneksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class Ruangan {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("=== DATA RUANGAN ===\n");

        System.out.print("ID ruangan: ");
        String id_ruangan = input.nextLine();

        System.out.print("Ruang: ");
        String nama_ruangan = input.nextLine();

        System.out.print("Gedung: ");
        String gedung = input.nextLine();

        System.out.print("Kapasitas: ");
        short kapasitas = input.nextShort();
        input.nextLine();
        
        try {
            Connection conn = Koneksi.connect();
            
            if (conn == null) {
                System.out.println("Koneksi gagal!");
                return;
            }
            
            String query =
            "INSERT INTO b1.ruangan(id_ruangan, nama_ruangan, gedung, kapasitas) VALUES(?, ?, ?, ?)";
            PreparedStatement ps_ruangan = conn.prepareStatement(query);

            ps_ruangan.setString(1, id_ruangan);
            ps_ruangan.setString(2, nama_ruangan);
            ps_ruangan.setString(3, gedung);
            ps_ruangan.setShort(4, kapasitas);

            int baris = ps_ruangan.executeUpdate();
            
            if (baris > 0) {
                System.out.println("===> Data berhasil ditambahkan! Silakan cek database.");
            }

        } catch (Exception e) {
            System.out.println("=== Terjadi kesalahan pada data! ===\n" + e.getMessage());
            e.printStackTrace();
        }
    }
}