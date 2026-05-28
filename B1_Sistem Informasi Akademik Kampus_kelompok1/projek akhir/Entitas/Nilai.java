package Entitas;

import Fitur.Tampilan;
import Koneksi.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Nilai {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("=== DATA NILAI ===\n");
        System.out.print("ID nilai: ");
        String id_nilai = input.nextLine();
        System.out.print("ID KRS: ");
        String id_krs = input.nextLine();
        System.out.print("Nilai TUGAS: ");
        String nilai_tugas = input.nextLine();
        System.out.print("Nilai UTS: ");
        String nilai_uts = input.nextLine();
        System.out.print("Nilai UAS: ");
        String nilai_uas = input.nextLine();

        try {
            Connection conn = Koneksi.connect();
            if (conn == null) { System.out.println("Koneksi gagal!"); return; }
            String query =
                "INSERT INTO b1.nilai(id_nilai, id_krs, nilai_tugas, nilai_uts, nilai_uas) " +
                "VALUES(?, ?, ?::numeric, ?::numeric, ?::numeric)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, id_nilai); ps.setString(2, id_krs);
            ps.setString(3, nilai_tugas); ps.setString(4, nilai_uts); ps.setString(5, nilai_uas);
            if (ps.executeUpdate() > 0)
                System.out.println("===> Data berhasil ditambahkan!");
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===================== LIHAT SEMUA NILAI =====================
    public void lihatNilai() {
        try (Connection conn = Koneksi.connect()) {
            String sql = "SELECT id_nilai, nilai_akhir, predikat FROM b1.nilai";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            // Kumpulkan data dulu
            java.util.List<String[]> rows = new java.util.ArrayList<>();
            while (rs.next()) {
                rows.add(new String[]{
                    rs.getString("id_nilai"),
                    rs.getString("nilai_akhir"),
                    rs.getString("predikat")
                });
            }

            String[] headers = {"ID Nilai", "Nilai Akhir", "Predikat"};
            Tampilan.tampilTabel(headers, rows.toArray(new String[0][]));

        } catch (Exception e) {
            Tampilan.gagal("Gagal menampilkan data nilai!");
            e.printStackTrace();
        }
    }

    // ===================== LIHAT NILAI MAHASISWA =====================
    public void lihatNilaiMahasiswa(String nim) {
        String sql =
            "SELECT mk.nama_mata_kuliah, n.nilai_tugas, n.nilai_uts, " +
            "n.nilai_uas, n.nilai_akhir, n.predikat " +
            "FROM b1.nilai n " +
            "JOIN b1.krs k           ON n.id_krs = k.id_krs " +
            "JOIN b1.kelas_kuliah kk ON k.id_kelas_kuliah = kk.id_kelas_kuliah " +
            "JOIN b1.mata_kuliah mk  ON kk.id_mata_kuliah = mk.id_mata_kuliah " +
            "WHERE k.nim = ?";

        try (Connection conn = Koneksi.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nim);
            ResultSet rs = ps.executeQuery();

            java.util.List<String[]> rows = new java.util.ArrayList<>();
            while (rs.next()) {
                rows.add(new String[]{
                    rs.getString("nama_mata_kuliah"),
                    rs.getString("nilai_tugas"),
                    rs.getString("nilai_uts"),
                    rs.getString("nilai_uas"),
                    rs.getString("nilai_akhir"),
                    rs.getString("predikat")
                });
            }

            System.out.println("\n=== NILAI SAYA ===");
            if (rows.isEmpty()) {
                Tampilan.peringatan("Belum ada nilai.");
            } else {
                String[] headers = {"Mata Kuliah", "Tugas", "UTS", "UAS", "Akhir", "Grade"};
                Tampilan.tampilTabel(headers, rows.toArray(new String[0][]));
            }

        } catch (Exception e) {
            Tampilan.gagal("Gagal menampilkan nilai!");
            e.printStackTrace();
        }
    }

    // ===================== INPUT NILAI (untuk Dosen) =====================
    public void inputNilai(String nidn) {
        Scanner input = new Scanner(System.in);

        try (Connection conn = Koneksi.connect()) {
            String sqlKRS =
                "SELECT k.id_krs, m.nim, m.nama, mk.nama_mata_kuliah " +
                "FROM b1.krs k " +
                "JOIN b1.mahasiswa m     ON k.nim = m.nim " +
                "JOIN b1.kelas_kuliah kk ON k.id_kelas_kuliah = kk.id_kelas_kuliah " +
                "JOIN b1.mata_kuliah mk  ON kk.id_mata_kuliah = mk.id_mata_kuliah " +
                "WHERE kk.nidn = ? AND k.status_persetujuan = 'Disetujui' " +
                "AND k.id_krs NOT IN (SELECT id_krs FROM b1.nilai)";

            PreparedStatement ps1 = conn.prepareStatement(sqlKRS);
            ps1.setString(1, nidn);
            ResultSet rs = ps1.executeQuery();

            java.util.List<String[]> rows = new java.util.ArrayList<>();
            while (rs.next()) {
                rows.add(new String[]{
                    rs.getString("id_krs"),
                    rs.getString("nim"),
                    rs.getString("nama_mata_kuliah")
                });
            }

            System.out.println("\n=== MAHASISWA BELUM ADA NILAI ===");
            if (rows.isEmpty()) {
                Tampilan.peringatan("Semua mahasiswa sudah memiliki nilai.");
                return;
            }

            String[] headers = {"ID KRS", "NIM", "Mata Kuliah"};
            Tampilan.tampilTabel(headers, rows.toArray(new String[0][]));

            System.out.print("Masukkan ID KRS yang akan diberi nilai: ");
            String idKRS = input.nextLine().trim();
            System.out.print("Nilai Tugas (0-100): ");
            String nilaiTugas = input.nextLine().trim();
            System.out.print("Nilai UTS (0-100): ");
            String nilaiUTS = input.nextLine().trim();
            System.out.print("Nilai UAS (0-100): ");
            String nilaiUAS = input.nextLine().trim();

            String idNilai = String.format("NL%08d", System.currentTimeMillis() % 100_000_000L);
            String sqlInsert =
                "INSERT INTO b1.nilai(id_nilai, id_krs, nilai_tugas, nilai_uts, nilai_uas) " +
                "VALUES(?, ?, ?::numeric, ?::numeric, ?::numeric)";

            PreparedStatement ps2 = conn.prepareStatement(sqlInsert);
            ps2.setString(1, idNilai); ps2.setString(2, idKRS);
            ps2.setString(3, nilaiTugas); ps2.setString(4, nilaiUTS); ps2.setString(5, nilaiUAS);
            ps2.executeUpdate();

            Tampilan.sukses("Nilai berhasil disimpan! Nilai akhir dihitung otomatis.");

        } catch (Exception e) {
            Tampilan.gagal("Gagal input nilai!");
            e.printStackTrace();
        }
    }
}