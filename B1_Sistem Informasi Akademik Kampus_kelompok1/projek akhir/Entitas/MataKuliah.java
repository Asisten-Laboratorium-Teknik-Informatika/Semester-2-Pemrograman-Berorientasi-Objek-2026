package Entitas;

import Koneksi.Koneksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class MataKuliah {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("=== DATA MATA KULIAH ===\n");
        System.out.print("ID Mata Kuliah: ");      String id_mata_kuliah = input.nextLine().trim();
        System.out.print("ID Program Studi: ");    String id_program_studi = input.nextLine().trim();
        System.out.print("NIDN: ");                String nidn = input.nextLine().trim();
        System.out.print("Nama mata kuliah: ");    String nama_mata_kuliah = input.nextLine().trim();
        System.out.print("Jumlah SKS: ");          String sks = input.nextLine().trim();
        System.out.print("Semester: ");            String semester_ke = input.nextLine().trim();
       
        if (id_mata_kuliah.isEmpty()
            || id_program_studi.isEmpty()
            || nidn.isEmpty()
            || nama_mata_kuliah.isEmpty()
            || sks.isEmpty()
            || semester_ke.isEmpty()) {

            System.out.println("Semua data wajib diisi!");
            return;
        }
    
        short semesterKe;
    
        try {
            semesterKe = Short.parseShort(input.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Semester harus berupa angka!");
            return;
        }

        if (semesterKe < 1 || semesterKe > 14) {
            System.out.println("Semester harus antara 1 sampai 14!");
            return;
        }

        if (!sks.equals("1")
            && !sks.equals("2")
            && !sks.equals("3")) {

            System.out.println("SKS harus 1, 2, atau 3!");
            return;
        }
    
        try {
            Connection conn = Koneksi.connect();
            
            if (conn == null) {
                System.out.println("Koneksi gagal!");
                return;
            }
        
            String cekMk =
                "SELECT COUNT(*) FROM b1.mata_kuliah WHERE id_mata_kuliah = ?";

            try (PreparedStatement psCek = conn.prepareStatement(cekMk)) {
                psCek.setString(1, id_mata_kuliah);

                ResultSet rs = psCek.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("ID Mata Kuliah sudah digunakan!");
                    return;
                }
            }
        
            String query =
            "INSERT INTO b1.mata_kuliah(id_mata_kuliah, id_program_studi, nidn, nama_mata_kuliah, sks, semester_ke) " +
            "VALUES(?, ?, ?, ?, ?::sks_enum, ?)";

            PreparedStatement ps_mata_kuliah = conn.prepareStatement(query);
            ps_mata_kuliah.setString(1, id_mata_kuliah); ps_mata_kuliah.setString(2, id_program_studi);
            ps_mata_kuliah.setString(3, nidn);           ps_mata_kuliah.setString(4, nama_mata_kuliah);
            ps_mata_kuliah.setString(5, sks);            ps_mata_kuliah.setShort(6, semesterKe);

            int baris = ps_mata_kuliah.executeUpdate();
            
            if (baris > 0) {
                    System.out.println("\n=== DATA MATA KULIAH BERHASIL DISIMPAN ===");
                    System.out.println("ID MK        : " + id_mata_kuliah);
                    System.out.println("Nama MK      : " + nama_mata_kuliah);
                    System.out.println("SKS          : " + sks);
                    System.out.println("Semester     : " + semesterKe);
            }    

        } catch (Exception e) {
            System.out.println("=== Terjadi kesalahan pada data! ===\n" + e.getMessage());
            e.printStackTrace();
        }
    }
}