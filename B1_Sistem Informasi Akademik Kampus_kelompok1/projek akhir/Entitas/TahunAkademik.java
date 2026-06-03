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
                System.out.println("ID Tahun : " + rs.getString("id_tahun"));
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

    public String getIdTahunAktif() {
        String sql = "SELECT id_tahun FROM b1.view_tahun_aktif LIMIT 1";

        try (Connection conn = Koneksi.connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getString("id_tahun");
            }

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
        System.out.print("ID Tahun: ");                String id_tahun = input.nextLine().trim();
        System.out.print("Tahun: ");                   String tahun = input.nextLine().trim();
        System.out.print("Semester (Ganjil/Genap): "); String semester_aktif = input.nextLine().trim();
        System.out.print("Status (Aktif/Nonaktif): "); String status = input.nextLine().trim();

        if (id_tahun.isEmpty()
                || tahun.isEmpty()
                || semester_aktif.isEmpty()
                || status.isEmpty()) {

            System.out.println("Semua data wajib diisi!");
            return;
        }

        if (!tahun.matches("\\d{4}/\\d{4}")) {
            System.out.println(
                "Format tahun akademik harus seperti 2025/2026!"
            );
            return;
        }

        semester_aktif =
            semester_aktif.substring(0, 1).toUpperCase()
            + semester_aktif.substring(1).toLowerCase();

        status =
            status.substring(0, 1).toUpperCase()
            + status.substring(1).toLowerCase();

        if (!semester_aktif.equals("Ganjil")
            && !semester_aktif.equals("Genap")) {

            System.out.println("Semester hanya boleh Ganjil atau Genap!");
            return;
        }

        if (!status.equals("Aktif")
                && !status.equals("Nonaktif")) {

            System.out.println(
                "Status hanya boleh Aktif atau Nonaktif!"
            );
            return;
        }

        try {Connection conn = Koneksi.connect();
            if (conn == null) {
                System.out.println("Koneksi gagal!");
                return;
            }
            
            String cekId =
                "SELECT COUNT(*) " +
                "FROM b1.tahun_akademik " +
                "WHERE id_tahun = ?";

            try (PreparedStatement ps = conn.prepareStatement(cekId)) {

                ps.setString(1, id_tahun);

                ResultSet rs = ps.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println(
                        "ID Tahun Akademik sudah digunakan!"
                    );
                    return;
                }
            }

            if (status.equals("Aktif")) {

                String cekAktif =
                    "SELECT COUNT(*) " +
                    "FROM b1.tahun_akademik " +
                    "WHERE status = 'Aktif'";

                try (PreparedStatement ps =
                         conn.prepareStatement(cekAktif)) {

                    ResultSet rs = ps.executeQuery();

                    if (rs.next() && rs.getInt(1) > 0) {

                        System.out.println(
                            "Masih ada tahun akademik yang aktif.\n" +
                            "Nonaktifkan terlebih dahulu sebelum menambah yang baru."
                        );
                        return;
                    }
                }
            }

            String query =
            "INSERT INTO b1.tahun_akademik(id_tahun, tahun, semester_aktif, status) " +
            "VALUES(?, ?, ?::semester_aktif_enum, ?::status_enum)";
        
            PreparedStatement ps_tahun_akademik = conn.prepareStatement(query);
            ps_tahun_akademik.setString(1, id_tahun);        ps_tahun_akademik.setString(2, tahun);
            ps_tahun_akademik.setString(3, semester_aktif);  ps_tahun_akademik.setString(4, status);

            int baris = ps_tahun_akademik.executeUpdate();
            
            if (baris > 0) {
                System.out.println("\n=== DATA TAHUN AKADEMIK BERHASIL DISIMPAN ===");
                System.out.println("ID Tahun : " + id_tahun);
                System.out.println("Tahun    : " + tahun);
                System.out.println("Semester : " + semester_aktif);
                System.out.println("Status   : " + status);
            }

        } catch (Exception e) {
            System.out.println("=== Terjadi kesalahan pada data! ===\n" + e.getMessage());
            e.printStackTrace();
        }
    }
}