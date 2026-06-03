package Entitas;

import Fitur.Tampilan;
import Koneksi.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class KRS {
    private static String inputWajib(Scanner input, String pesan) {
        String data;
        do {
            System.out.print(pesan);
            data = input.nextLine().trim();
            if (data.isEmpty()) System.out.println("Input tidak boleh kosong!");
        } while (data.isEmpty());
        return data;
    }

    // ===================== MAIN (INSERT KRS) =====================
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("=== DATA KRS ===\n");
        String id_krs   = inputWajib(input, "ID KRS: ");
        String nim      = inputWajib(input, "NIM: ");
        String id_tahun = inputWajib(input, "ID Tahun: ");
        String status   = inputWajib(input, "Status (Disetujui/Ditolak/Menunggu): ");

        try {
            Connection conn = Koneksi.connect();
            if (conn == null) { System.out.println("Koneksi gagal!"); return; }

            String query =
                "INSERT INTO b1.krs(id_krs, nim, id_tahun, status_persetujuan) " +
                "VALUES(?, ?, ?, ?::status_persetujuan_enum)";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, id_krs);   ps.setString(2, nim);
            ps.setString(3, id_tahun); ps.setString(4, status);

            if (ps.executeUpdate() > 0)
                System.out.println("===> Data berhasil ditambahkan! Silakan cek database.");

        } catch (Exception e) {
            System.out.println("=== Terjadi kesalahan pada data! ===\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===================== LIHAT KRS =====================
    // Hanya menampilkan KRS milik mahasiswa yang login (filter NIM)
    // dan hanya yang sudah disetujui (filter status_persetujuan = 'Disetujui')
    public void lihatKRS(String nim) {
        if (nim == null || nim.trim().isEmpty()) {
            Tampilan.gagal("NIM tidak boleh kosong!");
            return;
        }

        try (Connection conn = Koneksi.connect()) {
            String sql =
                "SELECT k.id_krs, k.nim, mk.nama_mata_kuliah, " +
                "k.tanggal_ambil, k.status_persetujuan " +
                "FROM b1.krs k " +
                "JOIN b1.mahasiswa m     ON k.nim = m.nim " +
                "JOIN b1.detail_krs dk   ON k.id_krs = dk.id_krs " +
                "JOIN b1.kelas_kuliah kk ON dk.id_kelas_kuliah = kk.id_kelas_kuliah " +
                "JOIN b1.mata_kuliah mk  ON kk.id_mata_kuliah = mk.id_mata_kuliah " +
                "WHERE k.nim = ? " +
                "AND k.status_persetujuan = 'Disetujui' " +  // hanya tampilkan yang sudah disetujui
                "ORDER BY k.tanggal_ambil DESC";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nim.trim());
            ResultSet rs = ps.executeQuery();

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

            System.out.println("\n=== DATA KRS SAYA (DISETUJUI) ===");
            if (rows.isEmpty()) {
                Tampilan.peringatan("\nTidak ada KRS yang sudah disetujui.");
            } else {
                String[] headers = {"ID KRS", "NIM", "Mata Kuliah", "Tanggal", "Status"};
                Tampilan.tampilTabel(headers, rows.toArray(new String[0][]));
            }

        } catch (Exception e) {
            Tampilan.gagal("Gagal menampilkan data KRS!");
            e.printStackTrace();
        }
    }

    // ===================== AMBIL KRS =====================
    public boolean ambilKRS(String nim, String idKelasKuliah) {
        if (nim == null || nim.trim().isEmpty()) {
            Tampilan.gagal("NIM tidak boleh kosong!");
            return false;
        }
        if (idKelasKuliah == null || idKelasKuliah.trim().isEmpty()) {
            Tampilan.gagal("ID Kelas Kuliah tidak boleh kosong!");
            return false;
        }

        nim           = nim.trim();
        idKelasKuliah = idKelasKuliah.trim();

        // Cek apakah id_kelas_kuliah ada di database
        try (Connection conn = Koneksi.connect()) {
            String cekKelas = "SELECT COUNT(*) FROM b1.kelas_kuliah WHERE id_kelas_kuliah = ?";
            PreparedStatement psKelas = conn.prepareStatement(cekKelas);
            psKelas.setString(1, idKelasKuliah);
            ResultSet rsKelas = psKelas.executeQuery();
            if (rsKelas.next() && rsKelas.getInt(1) == 0) {
                Tampilan.gagal("ID Kelas Kuliah tidak ditemukan!");
                return false;
            }
        } catch (Exception e) {
            Tampilan.gagal("Gagal memeriksa data kelas kuliah!");
            e.printStackTrace();
            return false;
        }

        // Ambil id tahun akademik yang sedang aktif
        TahunAkademik tahunDAO = new TahunAkademik();
        String idTahunAktif = tahunDAO.getIdTahunAktif();

        if (idTahunAktif == null) {
            Tampilan.gagal("Tidak bisa ambil KRS. Tidak ada tahun akademik aktif.");
            return false;
        }

        // Query cek apakah mahasiswa sudah pernah ambil mata kuliah yang sama (bukan yang ditolak)
        String cekDuplikat =
            "SELECT COUNT(*) AS total " +
            "FROM b1.detail_krs dk " +
            "JOIN b1.krs k           ON dk.id_krs = k.id_krs " +
            "JOIN b1.kelas_kuliah kk ON dk.id_kelas_kuliah = kk.id_kelas_kuliah " +
            "WHERE k.nim = ? " +
            "AND kk.id_mata_kuliah = ( " +
            "   SELECT id_mata_kuliah FROM b1.kelas_kuliah WHERE id_kelas_kuliah = ? " +
            ") " +
            "AND k.status_persetujuan != 'Ditolak'";

        // Query cek kuota kelas
        String cekKuota =
            "SELECT kk.kuota, " +
            "COALESCE(( " +
            "   SELECT COUNT(*) " +
            "   FROM b1.detail_krs dk " +
            "   JOIN b1.krs k ON dk.id_krs = k.id_krs " +
            "   WHERE dk.id_kelas_kuliah = ? " +
            "   AND k.status_persetujuan = 'Disetujui' " +
            "),0) AS terisi " +
            "FROM b1.kelas_kuliah kk " +
            "JOIN b1.tahun_akademik ta ON kk.id_tahun = ta.id_tahun " +
            "WHERE kk.id_kelas_kuliah = ? " +
            "AND ta.status = 'Aktif'";

        try (Connection conn = Koneksi.connect()) {
            // Cek duplikat mata kuliah
            PreparedStatement psDuplikat = conn.prepareStatement(cekDuplikat);
            psDuplikat.setString(1, nim);
            psDuplikat.setString(2, idKelasKuliah);
            ResultSet rsDuplikat = psDuplikat.executeQuery();

            if (rsDuplikat.next() && rsDuplikat.getInt("total") > 0) {
                Tampilan.gagal("Anda sudah mengambil mata kuliah ini!");
                return false;
            }

            // Cek kuota kelas
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

            // Generate ID KRS unik
            String idKRS = String.format("KRS%07d", System.currentTimeMillis() % 10000000L);

            // Insert ke tabel krs (status default: Menunggu)
            PreparedStatement psKRS = conn.prepareStatement(
                "INSERT INTO b1.krs (id_krs, nim, id_tahun, tanggal_ambil) VALUES (?, ?, ?, CURRENT_DATE)");
            psKRS.setString(1, idKRS);
            psKRS.setString(2, nim);
            psKRS.setString(3, idTahunAktif);
            psKRS.executeUpdate();

            // Generate ID detail KRS unik
            String idDetail = String.format("DET%07d", System.currentTimeMillis() % 10000000L);

            // Insert ke tabel detail_krs
            PreparedStatement psDetail = conn.prepareStatement(
                "INSERT INTO b1.detail_krs (id_detail_krs, id_krs, id_kelas_kuliah) VALUES (?, ?, ?)");
            psDetail.setString(1, idDetail);
            psDetail.setString(2, idKRS);
            psDetail.setString(3, idKelasKuliah);
            psDetail.executeUpdate();

            Tampilan.sukses("KRS berhasil diambil! Menunggu persetujuan dari dosen.");
            return true;

        } catch (Exception e) {
            Tampilan.gagal("Gagal mengambil KRS!");
            e.printStackTrace();
            return false;
        }
    }

    // ===================== KELOLA KRS (Dosen) =====================
    // Dosen hanya bisa melihat dan memproses KRS dari kelas yang dia ajar
    public void kelolaKRS(String nidn) {
        if (nidn == null || nidn.trim().isEmpty()) {
            Tampilan.gagal("NIDN tidak boleh kosong!");
            return;
        }

        Scanner input = new Scanner(System.in);

        try (Connection conn = Koneksi.connect()) {
            // Tampilkan semua KRS yang menunggu persetujuan dari dosen yang login
            String sql =
                "SELECT k.id_krs, m.nim, m.nama, mk.nama_mata_kuliah, k.tanggal_ambil " +
                "FROM b1.krs k " +
                "JOIN b1.mahasiswa m     ON k.nim = m.nim " +
                "JOIN b1.detail_krs dk   ON k.id_krs = dk.id_krs " +
                "JOIN b1.kelas_kuliah kk ON dk.id_kelas_kuliah = kk.id_kelas_kuliah " +
                "JOIN b1.mata_kuliah mk  ON kk.id_mata_kuliah = mk.id_mata_kuliah " +
                "WHERE kk.nidn = ? AND k.status_persetujuan = 'Menunggu' " +
                "ORDER BY k.tanggal_ambil DESC";

            PreparedStatement ps1 = conn.prepareStatement(sql);
            ps1.setString(1, nidn.trim());
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
                Tampilan.peringatan("\nTidak ada KRS yang menunggu persetujuan.");
                return;
            }

            String[] headers = {"ID KRS", "NIM", "Mata Kuliah", "Tgl Ambil"};
            Tampilan.tampilTabel(headers, rows.toArray(new String[0][]));

            String idKRS = inputWajib(input, "Masukkan ID KRS yang akan diproses: ");

            // Validasi ID KRS harus dari daftar yang tampil (bukan wewenang dosen lain)
            boolean idValid = false;
            for (String[] row : rows) {
                if (row[0].equals(idKRS)) {
                    idValid = true;
                    break;
                }
            }

            if (!idValid) {
                Tampilan.gagal("ID KRS tidak ditemukan atau bukan wewenang Anda!");
                return;
            }

            // Input keputusan dosen
            String keputusan;
            do {
                keputusan = inputWajib(input, "Keputusan (Disetujui/Ditolak): ");
                if (!keputusan.equals("Disetujui") && !keputusan.equals("Ditolak")) {
                    Tampilan.gagal("Keputusan tidak valid! Harus 'Disetujui' atau 'Ditolak'.");
                }
            } while (!keputusan.equals("Disetujui") && !keputusan.equals("Ditolak"));

            // Update status persetujuan KRS
            PreparedStatement ps2 = conn.prepareStatement(
                "UPDATE b1.krs SET status_persetujuan = ?::status_persetujuan_enum WHERE id_krs = ?");
            ps2.setString(1, keputusan);
            ps2.setString(2, idKRS);
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