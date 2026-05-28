package Entitas;

import Fitur.Tampilan;
import Koneksi.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class KRS {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("=== DATA KRS ===\n");
        System.out.print("ID KRS: ");          String id_krs = input.nextLine();
        System.out.print("NIM: ");             String nim = input.nextLine();
        System.out.print("ID kelas kuliah: "); String id_kelas_kuliah = input.nextLine();
        System.out.print("Status: ");          String status = input.nextLine();

        try {
            Connection conn = Koneksi.connect();
            if (conn == null) { System.out.println("Koneksi gagal!"); return; }
            String query =
                "INSERT INTO b1.krs(id_krs, nim, id_kelas_kuliah, status_persetujuan) " +
                "VALUES(?, ?, ?, ?::status_persetujuan_enum)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, id_krs); ps.setString(2, nim);
            ps.setString(3, id_kelas_kuliah); ps.setString(4, status);
            if (ps.executeUpdate() > 0)
                System.out.println("===> Data berhasil ditambahkan!");
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===================== LIHAT KRS =====================
    public void lihatKRS() {
        try (Connection conn = Koneksi.connect()) {
            String sql =
                "SELECT k.id_krs, k.nim, m.nama, mk.nama_mata_kuliah, " +
                "k.tanggal_ambil, k.status_persetujuan " +
                "FROM b1.krs k " +
                "JOIN b1.mahasiswa m     ON k.nim = m.nim " +
                "JOIN b1.kelas_kuliah kk ON k.id_kelas_kuliah = kk.id_kelas_kuliah " +
                "JOIN b1.mata_kuliah mk  ON kk.id_mata_kuliah = mk.id_mata_kuliah " +
                "ORDER BY k.tanggal_ambil DESC";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            List<String[]> rows = new ArrayList<>();
            while (rs.next()) {
                rows.add(new String[]{
                    rs.getString("id_krs"),
                    rs.getString("nim"),
                    rs.getString("nama_mata_kuliah"),
                    rs.getString("tanggal_ambil"),
                    rs.getString("status_persetujuan")
                });
            }

            System.out.println("\n=== DATA KRS ===");
            if (rows.isEmpty()) {
                Tampilan.peringatan("Tidak ada data KRS.");
            } else {
                String[] headers = {"ID KRS", "NIM", "Mata Kuliah", "Tgl Ambil", "Status"};
                Tampilan.tampilTabel(headers, rows.toArray(new String[0][]));
            }

        } catch (Exception e) {
            Tampilan.gagal("Gagal menampilkan data KRS!");
            e.printStackTrace();
        }
    }

    // ===================== AMBIL KRS =====================
    public boolean ambilKRS(String nim, String idKelasKuliah) {
        TahunAkademik tahunDAO = new TahunAkademik();
        String idTahunAktif = tahunDAO.getIdTahunAktif();

        if (idTahunAktif == null) {
            Tampilan.gagal("Tidak bisa ambil KRS. Tidak ada tahun akademik aktif.");
            return false;
        }

        String cekKuota =
            "SELECT kk.kuota, " +
            "(SELECT COUNT(*) FROM b1.krs WHERE id_kelas_kuliah = ? AND status_persetujuan = 'Disetujui') AS terisi " +
            "FROM b1.kelas_kuliah kk " +
            "JOIN b1.tahun_akademik ta ON kk.id_tahun = ta.id_tahun " +
            "WHERE kk.id_kelas_kuliah = ? AND ta.status = 'Aktif'";

        try (Connection conn = Koneksi.connect()) {
            PreparedStatement ps1 = conn.prepareStatement(cekKuota);
            ps1.setString(1, idKelasKuliah);
            ps1.setString(2, idKelasKuliah);
            ResultSet rs = ps1.executeQuery();

            if (!rs.next()) {
                Tampilan.gagal("Kelas tidak ditemukan atau bukan tahun akademik aktif.");
                return false;
            }

            int kuota  = rs.getInt("kuota");
            int terisi = rs.getInt("terisi");

            if (terisi >= kuota) {
                Tampilan.gagal("Kelas penuh! Kuota: " + kuota + ", Terisi: " + terisi);
                return false;
            }

            String idKRS = String.format("KRS%07d", System.currentTimeMillis() % 10_000_000L);
            PreparedStatement ps2 = conn.prepareStatement(
                "INSERT INTO b1.krs (id_krs, nim, id_kelas_kuliah) VALUES (?, ?, ?)");
            ps2.setString(1, idKRS); ps2.setString(2, nim); ps2.setString(3, idKelasKuliah);
            ps2.executeUpdate();

            Tampilan.sukses("KRS berhasil diambil! Menunggu persetujuan dosen.");
            return true;

        } catch (Exception e) {
            Tampilan.gagal("Gagal mengambil KRS!");
            e.printStackTrace();
            return false;
        }
    }

    // ===================== KELOLA KRS (Dosen) =====================
    public void kelolaKRS(String nidn) {
        Scanner input = new Scanner(System.in);

        try (Connection conn = Koneksi.connect()) {
            String sql =
                "SELECT k.id_krs, m.nim, m.nama, mk.nama_mata_kuliah, k.tanggal_ambil " +
                "FROM b1.krs k " +
                "JOIN b1.mahasiswa m     ON k.nim = m.nim " +
                "JOIN b1.kelas_kuliah kk ON k.id_kelas_kuliah = kk.id_kelas_kuliah " +
                "JOIN b1.mata_kuliah mk  ON kk.id_mata_kuliah = mk.id_mata_kuliah " +
                "WHERE kk.nidn = ? AND k.status_persetujuan = 'Menunggu' " +
                "ORDER BY k.tanggal_ambil";

            PreparedStatement ps1 = conn.prepareStatement(sql);
            ps1.setString(1, nidn);
            ResultSet rs = ps1.executeQuery();

            List<String[]> rows = new ArrayList<>();
            while (rs.next()) {
                rows.add(new String[]{
                    rs.getString("id_krs"),
                    rs.getString("nim"),
                    rs.getString("nama_mata_kuliah"),
                    rs.getString("tanggal_ambil")
                });
            }

            System.out.println("\n=== KRS MENUNGGU PERSETUJUAN ===");
            if (rows.isEmpty()) {
                Tampilan.peringatan("Tidak ada KRS yang menunggu persetujuan.");
                return;
            }

            String[] headers = {"ID KRS", "NIM", "Mata Kuliah", "Tgl Ambil"};
            Tampilan.tampilTabel(headers, rows.toArray(new String[0][]));

            System.out.print("Masukkan ID KRS yang akan diproses: ");
            String idKRS = input.nextLine().trim();
            System.out.print("Keputusan (Disetujui/Ditolak): ");
            String keputusan = input.nextLine().trim();

            if (!keputusan.equals("Disetujui") && !keputusan.equals("Ditolak")) {
                Tampilan.gagal("Keputusan tidak valid! Harus 'Disetujui' atau 'Ditolak'.");
                return;
            }

            PreparedStatement ps2 = conn.prepareStatement(
                "UPDATE b1.krs SET status_persetujuan = ?::status_persetujuan_enum WHERE id_krs = ?");
            ps2.setString(1, keputusan); ps2.setString(2, idKRS);
            int baris = ps2.executeUpdate();

            if (baris > 0) {
                if (keputusan.equals("Disetujui")) Tampilan.sukses("KRS berhasil disetujui!");
                else Tampilan.peringatan("KRS telah ditolak.");
            } else {
                Tampilan.gagal("ID KRS tidak ditemukan.");
            }

        } catch (Exception e) {
            Tampilan.gagal("Gagal memproses KRS!");
            e.printStackTrace();
        }
    }
}