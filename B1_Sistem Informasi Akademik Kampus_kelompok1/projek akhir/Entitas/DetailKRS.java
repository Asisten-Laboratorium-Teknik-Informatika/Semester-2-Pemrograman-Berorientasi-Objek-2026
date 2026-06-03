package Entitas;

import Fitur.Tampilan;
import Koneksi.Koneksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DetailKRS {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("=== DATA DETAIL KRS ===\n");
        System.out.print("ID Detail KRS: ");   String id_detail_krs = input.nextLine();
        System.out.print("ID KRS: ");          String id_krs = input.nextLine();
        System.out.print("ID Kelas Kuliah: "); String id_kelas_kuliah = input.nextLine();

        try {
            Connection conn = Koneksi.connect();

            if (conn == null) {
                System.out.println("Koneksi gagal!");
                return;
            }

            String query =
            "INSERT INTO b1.detail_krs(id_detail_krs, id_krs, id_kelas_kuliah) " +
            "VALUES (?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, id_detail_krs);   ps.setString(2, id_krs);
            ps.setString(3, id_kelas_kuliah);

            int baris = ps.executeUpdate();

            if (baris > 0) {
                System.out.println("===> Data berhasil ditambahkan! Silakan cek database.");
            }

        } catch (Exception e) {
            System.out.println("=== Terjadi kesalahan pada data! ===" + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===================== LIHAT DETAIL KRS =====================
    public void lihatDetailKRS() {
        try (Connection conn = Koneksi.connect()) {
            String sql =
                "SELECT dk.id_detail_krs, dk.id_krs, m.nim, m.nama, mk.nama_mata_kuliah, kk.nama_kelas " +
                "FROM b1.detail_krs dk " +
                "JOIN b1.krs k ON dk.id_krs = k.id_krs " +
                "JOIN b1.mahasiswa m ON k.nim = m.nim " +
                "JOIN b1.kelas_kuliah kk ON dk.id_kelas_kuliah = kk.id_kelas_kuliah " +
                "JOIN b1.mata_kuliah mk ON kk.id_mata_kuliah = mk.id_mata_kuliah " +
                "ORDER BY dk.id_detail_krs";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            List<String[]> rows = new ArrayList<>();

            while (rs.next()) {
                rows.add(new String[]{
                    rs.getString("id_detail_krs"),
                    rs.getString("id_krs"),
                    rs.getString("nim"),
                    rs.getString("nama_mata_kuliah"),
                    rs.getString("nama_kelas")
                });
            }

            System.out.println("\n=== DATA DETAIL KRS ===");

            if (rows.isEmpty()) {
                Tampilan.peringatan("\nTidak ada data Detail KRS.");
            } else {
                String[] headers = {"ID Detail", "ID KRS", "NIM", "Mata Kuliah", "Kelas"};
                Tampilan.tampilTabel(headers, rows.toArray(new String[0][]));
            }

        } catch (Exception e) {
            Tampilan.gagal("Gagal menampilkan Detail KRS!");
            e.printStackTrace();
        }
    }

    // ===================== CEK DETAIL KRS =====================
    public boolean cekDetailKRS(String idKRS, String idKelasKuliah) {
        try (Connection conn = Koneksi.connect()) {
            String sql =
                "SELECT 1 " +
                "FROM b1.detail_krs " +
                "WHERE id_krs = ? " +
                "AND id_kelas_kuliah = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idKRS); ps.setString(2, idKelasKuliah);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===================== TAMBAH DETAIL KRS =====================
    public boolean tambahDetailKRS(String idDetailKRS, String idKRS, String idKelasKuliah) {
        try (Connection conn = Koneksi.connect()) {
            String sql =
                "INSERT INTO b1.detail_krs(id_detail_krs, id_krs, id_kelas_kuliah) " +
                "VALUES (?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idDetailKRS);   ps.setString(2, idKRS);
            ps.setString(3, idKelasKuliah);
            
            int hasil = ps.executeUpdate();
            return hasil > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}