package Entitas;

import Koneksi.Koneksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class TahunAkademik {
    public void tampilkanTahunAktif() {
        String sql = "SELECT * FROM b1.view_tahun_aktif";
        try (Connection conn = Koneksi.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            System.out.println("\n===== TAHUN AKADEMIK AKTIF =====");
            if (rs.next()) {
                System.out.println("Tahun    : " + rs.getString("tahun"));
                System.out.println("Semester : " + rs.getString("semester_aktif"));
                System.out.println("Status   : " + rs.getString("status"));
            } else {
                System.out.println("Tidak ada tahun akademik aktif.");
            }
            System.out.println("================================");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // METHOD KRS.java UNTUK VALIDASI
    public String getIdTahunAktif() {
        String sql = "SELECT id_tahun FROM b1.view_tahun_aktif LIMIT 1";
        try (Connection conn = Koneksi.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("id_tahun");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String[] getInfoTahunAktif() {
    String sql = "SELECT tahun, semester_aktif FROM b1.view_tahun_aktif LIMIT 1";
        try (Connection conn = Koneksi.connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
        
            if (rs.next()) {
            return new String[]{
                rs.getString("tahun"),
                rs.getString("semester_aktif")
            };
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("=== DATA TAHUN AKADEMIK ===\n");

        System.out.print("ID tahun: ");
        String id_tahun = input.nextLine();

        System.out.print("Tahun: ");
        String tahun = input.nextLine();

        System.out.print("Semester (Ganjil/Genap): ");
        String semester_aktif = input.nextLine();

        System.out.print("Status: ");
        String status = input.nextLine();

        try {
            Connection conn = Koneksi.connect();
            
            if (conn == null) {
                System.out.println("Koneksi gagal!");
                return;
            }
            
            String query =
            "INSERT INTO b1.tahun_akademik(id_tahun, tahun, semester_aktif, status) VALUES(?, ?, ?::semester_aktif_enum, ?::status_enum)";
            PreparedStatement ps_tahun_akademik = conn.prepareStatement(query);

            ps_tahun_akademik.setString(1, id_tahun);
            ps_tahun_akademik.setString(2, tahun);
            ps_tahun_akademik.setString(3, semester_aktif);
            ps_tahun_akademik.setString(4, status);

            int baris = ps_tahun_akademik.executeUpdate();
            
            if (baris > 0) {
                System.out.println("===> Data berhasil ditambahkan! Silakan cek database.");
            }

        } catch (Exception e) {
            System.out.println("=== Terjadi kesalahan pada data! ===\n" + e.getMessage());
            e.printStackTrace();
        }
    }
}