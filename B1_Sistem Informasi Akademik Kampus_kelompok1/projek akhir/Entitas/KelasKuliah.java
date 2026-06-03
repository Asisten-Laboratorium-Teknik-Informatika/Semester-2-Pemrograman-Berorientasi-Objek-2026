package Entitas;

import Fitur.Tampilan;
import Koneksi.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class KelasKuliah {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("=== DATA KELAS KULIAH ===\n");
        System.out.print("ID Kelas Kuliah : "); String id_kelas_kuliah = input.nextLine().trim();
        System.out.print("ID Mata Kuliah  : "); String id_mata_kuliah  = input.nextLine().trim();
        System.out.print("NIDN            : "); String nidn            = input.nextLine().trim();
        System.out.print("ID Tahun        : "); String id_tahun        = input.nextLine().trim();
        System.out.print("ID Ruangan      : "); String id_ruangan      = input.nextLine().trim();
        System.out.print("Nama Kelas      : "); String nama_kelas      = input.nextLine().trim();
        System.out.print("Kuota           : "); String kuota           = input.nextLine().trim();

        if (id_kelas_kuliah.isEmpty() || id_mata_kuliah.isEmpty() || nidn.isEmpty()
                || id_tahun.isEmpty() || id_ruangan.isEmpty() || nama_kelas.isEmpty()
                || kuota.isEmpty()) {
            System.out.println("Semua data wajib diisi!");
            return;
        }

        short kuota_kelas;
        try {
            kuota_kelas = Short.parseShort(kuota);
        } catch (NumberFormatException e) {
            System.out.println("Kuota harus berupa angka!");
            return;
        }

        if (kuota_kelas <= 0)        { System.out.println("Kuota harus lebih dari 0!");        return; }
        if (kuota_kelas > 100)       { System.out.println("Kuota maksimal 100 mahasiswa!");     return; }
        if (nama_kelas.length() > 5) { System.out.println("Nama kelas maksimal 5 karakter!");  return; }

        try (Connection conn = Koneksi.connect()) {
            if (conn == null) { System.out.println("Koneksi gagal!"); return; }

            String cekKelas = "SELECT COUNT(*) FROM b1.kelas_kuliah WHERE id_kelas_kuliah = ?";
            try (PreparedStatement ps = conn.prepareStatement(cekKelas)) {
                ps.setString(1, id_kelas_kuliah);
                ResultSet rs = ps.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) { System.out.println("ID Kelas Kuliah sudah digunakan!"); return; }
            }

            String cekMK = "SELECT COUNT(*) FROM b1.mata_kuliah WHERE id_mata_kuliah = ?";
            try (PreparedStatement ps = conn.prepareStatement(cekMK)) {
                ps.setString(1, id_mata_kuliah);
                ResultSet rs = ps.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) { System.out.println("ID Mata Kuliah tidak ditemukan!"); return; }
            }

            String cekDosen = "SELECT COUNT(*) FROM b1.dosen WHERE nidn = ?";
            try (PreparedStatement ps = conn.prepareStatement(cekDosen)) {
                ps.setString(1, nidn);
                ResultSet rs = ps.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) { System.out.println("NIDN tidak ditemukan!"); return; }
            }

            String cekTahun = "SELECT COUNT(*) FROM b1.tahun_akademik WHERE id_tahun = ? AND status = 'Aktif'";
            try (PreparedStatement ps = conn.prepareStatement(cekTahun)) {
                ps.setString(1, id_tahun);
                ResultSet rs = ps.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) { System.out.println("Tahun akademik tidak ditemukan atau tidak aktif!"); return; }
            }

            String cekRuangan = "SELECT COUNT(*) FROM b1.ruangan WHERE id_ruangan = ?";
            try (PreparedStatement ps = conn.prepareStatement(cekRuangan)) {
                ps.setString(1, id_ruangan);
                ResultSet rs = ps.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) { System.out.println("ID Ruangan tidak ditemukan!"); return; }
            }

            String query =
                "INSERT INTO b1.kelas_kuliah " +
                "(id_kelas_kuliah, id_mata_kuliah, nidn, id_tahun, id_ruangan, nama_kelas, kuota) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, id_kelas_kuliah); ps.setString(2, id_mata_kuliah);
                ps.setString(3, nidn);            ps.setString(4, id_tahun);
                ps.setString(5, id_ruangan);      ps.setString(6, nama_kelas);
                ps.setShort(7, kuota_kelas); // ← diperbaiki

                if (ps.executeUpdate() > 0) {
                    System.out.println("\n=== DATA KELAS KULIAH BERHASIL DISIMPAN ===");
                    System.out.println("ID Kelas       : " + id_kelas_kuliah);
                    System.out.println("ID Mata Kuliah : " + id_mata_kuliah);
                    System.out.println("NIDN           : " + nidn);
                    System.out.println("ID Tahun       : " + id_tahun);
                    System.out.println("ID Ruangan     : " + id_ruangan);
                    System.out.println("Nama Kelas     : " + nama_kelas);
                    System.out.println("Kuota          : " + kuota);
                }
            }

        } catch (Exception e) {
            System.out.println("\n=== Terjadi kesalahan pada data! ===\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===================== LIHAT SEMUA KELAS (Mahasiswa) =====================
    public void lihatKelas(String nim) {
        try (Connection conn = Koneksi.connect()) {
            String sql =
                "SELECT kk.id_kelas_kuliah, mk.nama_mata_kuliah, d.nama AS nama_dosen, " +
                "r.nama_ruangan, kk.nama_kelas, kk.kuota, " +
                "COALESCE((SELECT COUNT(*) FROM b1.detail_krs dk " +
                "   JOIN b1.krs k ON dk.id_krs = k.id_krs " +
                "   WHERE dk.id_kelas_kuliah = kk.id_kelas_kuliah " +
                "   AND k.status_persetujuan = 'Disetujui'), 0) AS terisi " +
                "FROM b1.kelas_kuliah kk " +
                "JOIN b1.mata_kuliah mk      ON kk.id_mata_kuliah = mk.id_mata_kuliah " +
                "JOIN b1.dosen d             ON kk.nidn = d.nidn " +
                "JOIN b1.ruangan r           ON kk.id_ruangan = r.id_ruangan " +
                "JOIN b1.tahun_akademik ta   ON kk.id_tahun = ta.id_tahun " +
                "WHERE ta.status = 'Aktif' " +
                "AND kk.id_mata_kuliah NOT IN ( " +
                "   SELECT kk2.id_mata_kuliah " +
                "   FROM b1.detail_krs dk2 " +
                "   JOIN b1.krs k2           ON dk2.id_krs = k2.id_krs " +
                "   JOIN b1.kelas_kuliah kk2 ON dk2.id_kelas_kuliah = kk2.id_kelas_kuliah " +
                "   WHERE k2.nim = ? AND k2.status_persetujuan != 'Ditolak' " +
                ") " +
                "ORDER BY mk.nama_mata_kuliah";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nim); // ← tambahkan ini
            ResultSet rs = ps.executeQuery();

            List<String[]> rows = new ArrayList<>();
            while (rs.next()) {
                rows.add(new String[]{
                    rs.getString("id_kelas_kuliah"),
                    rs.getString("nama_mata_kuliah"),
                    rs.getString("nama_dosen"),
                    rs.getString("nama_ruangan"),
                    rs.getString("nama_kelas"),
                    rs.getInt("terisi") + "/" + rs.getInt("kuota")
                });
            }

            System.out.println("\n=== DAFTAR KELAS KULIAH TERSEDIA ===");
            if (rows.isEmpty()) {
                Tampilan.peringatan("Tidak ada kelas kuliah yang tersedia.");
            } else {
                String[] headers = {"ID Kelas", "Mata Kuliah", "Dosen", "Ruangan", "Kelas", "Terisi"};
                Tampilan.tampilTabel(headers, rows.toArray(new String[0][]));
            }

        } catch (Exception e) {
            Tampilan.gagal("Gagal menampilkan data kelas kuliah!");
            e.printStackTrace();
        }
    }

    // ===================== LIHAT KELAS BY DOSEN =====================
    public void lihatKelasByDosen(String nidn) {
        if (nidn == null || nidn.trim().isEmpty()) {
            Tampilan.gagal("NIDN tidak boleh kosong!");
            return;
        }

        try (Connection conn = Koneksi.connect()) {
            String sql =
                "SELECT kk.id_kelas_kuliah, mk.nama_mata_kuliah, r.nama_ruangan, " +
                "kk.nama_kelas, kk.kuota, " +
                "COALESCE((SELECT COUNT(*) " +
                "           FROM b1.detail_krs dk " +
                "           JOIN b1.krs k ON dk.id_krs = k.id_krs " +
                "           WHERE dk.id_kelas_kuliah = kk.id_kelas_kuliah " +
                "           AND k.status_persetujuan = 'Disetujui'), 0) AS terisi " +
                "FROM b1.kelas_kuliah kk " +
                "JOIN b1.mata_kuliah mk      ON kk.id_mata_kuliah = mk.id_mata_kuliah " +
                "JOIN b1.ruangan r           ON kk.id_ruangan = r.id_ruangan " +
                "JOIN b1.tahun_akademik ta   ON kk.id_tahun = ta.id_tahun " +
                "WHERE kk.nidn = ? AND ta.status = 'Aktif' " +
                "ORDER BY mk.nama_mata_kuliah";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nidn.trim());
            ResultSet rs = ps.executeQuery();

            List<String[]> rows = new ArrayList<>();
            while (rs.next()) {
                rows.add(new String[]{
                    rs.getString("id_kelas_kuliah"),
                    rs.getString("nama_mata_kuliah"),
                    rs.getString("nama_ruangan"),
                    rs.getString("nama_kelas"),
                    rs.getInt("terisi") + "/" + rs.getInt("kuota")
                });
            }

            System.out.println("\n=== KELAS KULIAH SAYA ===");
            if (rows.isEmpty()) {
                Tampilan.peringatan("Tidak ada kelas kuliah yang diampu.");
            } else {
                String[] headers = {"ID Kelas", "Mata Kuliah", "Ruangan", "Kelas", "Terisi"};
                Tampilan.tampilTabel(headers, rows.toArray(new String[0][]));
            }

        } catch (Exception e) {
            Tampilan.gagal("Gagal menampilkan data kelas kuliah!");
            e.printStackTrace();
        }
    }
}