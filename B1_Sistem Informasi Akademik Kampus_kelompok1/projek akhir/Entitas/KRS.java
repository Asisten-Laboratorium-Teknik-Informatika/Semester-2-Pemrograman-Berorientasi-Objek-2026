package Entitas;

import Fitur.Tampilan;
import Koneksi.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class KRS — entitas mandiri (bukan turunan Pengguna).
 *
 * ENCAPSULATION: Semua field private, diakses lewat getter/setter.
 *
 * LOGIKA BISNIS (BARU):
 *   - 1 KRS = semua mata kuliah dalam 1 semester sesuai prodi mahasiswa
 *   - Disetujui oleh dosen PEMBIMBING (bukan dosen pengampu)
 *   - Dosen pembimbing berlaku sepanjang kuliah (tidak berubah per semester)
 *   - Mahasiswa cukup tekan "Ambil KRS" → semua kelas otomatis masuk
 */
public class KRS {

    // ===================== FIELD (private → Encapsulation) =====================
    private String idKrs;
    private String nim;
    private String idTahun;
    private String statusPersetujuan;
    private String nidnPembimbing;

    // ===================== CONSTRUCTOR =====================
    public KRS() {}

    public KRS(String idKrs, String nim, String idTahun,
               String statusPersetujuan, String nidnPembimbing) {
        this.idKrs             = idKrs;
        this.nim               = nim;
        this.idTahun           = idTahun;
        this.statusPersetujuan = statusPersetujuan;
        this.nidnPembimbing    = nidnPembimbing;
    }

    // ===================== GETTER & SETTER (Encapsulation) =====================
    public String getIdKrs()             { return idKrs; }
    public String getNim()               { return nim; }
    public String getIdTahun()           { return idTahun; }
    public String getStatusPersetujuan() { return statusPersetujuan; }
    public String getNidnPembimbing()    { return nidnPembimbing; }

    public void setIdKrs(String idKrs)                         { this.idKrs = idKrs; }
    public void setNim(String nim)                             { this.nim = nim; }
    public void setIdTahun(String idTahun)                     { this.idTahun = idTahun; }
    public void setStatusPersetujuan(String statusPersetujuan) { this.statusPersetujuan = statusPersetujuan; }
    public void setNidnPembimbing(String nidnPembimbing)       { this.nidnPembimbing = nidnPembimbing; }

    // ===================== HELPER PRIVATE =====================
    private static String inputWajib(Scanner input, String pesan) {
        String data;
        do {
            System.out.print(pesan);
            data = input.nextLine().trim();
            if (data.isEmpty()) System.out.println("Input tidak boleh kosong!");
        } while (data.isEmpty());
        return data;
    }

    // ===================== TAMBAH KRS MANUAL (untuk admin/testing) =====================
    public static void tambahKRS() {
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
                "VALUES(?, ?, ?, ?::b1.status_persetujuan_enum)";

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


    // ===================== AMBIL KRS (FIXED — mahasiswa pilih kelas sendiri) =====================
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

        try (Connection conn = Koneksi.connect()) {

            // 1. Cek kelas kuliah ada
            PreparedStatement cekKelas = conn.prepareStatement(
                "SELECT COUNT(*) FROM b1.kelas_kuliah WHERE id_kelas_kuliah = ?");
            cekKelas.setString(1, idKelasKuliah);
            ResultSet rsKelas = cekKelas.executeQuery();
            if (rsKelas.next() && rsKelas.getInt(1) == 0) {
                Tampilan.gagal("ID Kelas Kuliah tidak ditemukan!");
                return false;
            }

            // 2. ✅ Cek mahasiswa sudah ambil MATA KULIAH ini (bukan hanya kelas ini)
            //    Cegah mahasiswa ambil Kelas A DAN Kelas B untuk matkul yang sama
            PreparedStatement cekMatkul = conn.prepareStatement(
                "SELECT COUNT(*) AS total " +
                "FROM b1.detail_krs dk " +
                "JOIN b1.krs k           ON dk.id_krs = k.id_krs " +
                "JOIN b1.kelas_kuliah kk ON dk.id_kelas_kuliah = kk.id_kelas_kuliah " +
                "WHERE k.nim = ? " +
                "AND kk.id_mata_kuliah = ( " +
                "   SELECT id_mata_kuliah FROM b1.kelas_kuliah WHERE id_kelas_kuliah = ? " +
                ") " +
                "AND k.status_persetujuan != 'Ditolak'");
            cekMatkul.setString(1, nim);
            cekMatkul.setString(2, idKelasKuliah);
            ResultSet rsMatkul = cekMatkul.executeQuery();
            if (rsMatkul.next() && rsMatkul.getInt("total") > 0) {
                Tampilan.gagal("Anda sudah mengambil mata kuliah ini di kelas lain!");
                return false;
            }

            // 3. Cek kuota kelas
            PreparedStatement cekKuota = conn.prepareStatement(
                "SELECT kk.kuota, " +
                "COALESCE((SELECT COUNT(*) FROM b1.detail_krs dk " +
                "          JOIN b1.krs k ON dk.id_krs = k.id_krs " +
                "          WHERE dk.id_kelas_kuliah = ? " +
                "          AND k.status_persetujuan = 'Disetujui'), 0) AS terisi " +
                "FROM b1.kelas_kuliah kk " +
                "JOIN b1.tahun_akademik ta ON kk.id_tahun = ta.id_tahun " +
                "WHERE kk.id_kelas_kuliah = ? AND ta.status = 'Aktif'");
            cekKuota.setString(1, idKelasKuliah);
            cekKuota.setString(2, idKelasKuliah);
            ResultSet rsKuota = cekKuota.executeQuery();

            if (!rsKuota.next()) {
                Tampilan.gagal("Kelas tidak ditemukan atau bukan tahun akademik aktif.");
                return false;
            }

            int kuota  = rsKuota.getInt("kuota");
            int terisi = rsKuota.getInt("terisi");
            if (terisi >= kuota) {
                Tampilan.gagal("Kelas penuh! Kuota: " + kuota + ", Terisi: " + terisi);
                return false;
            }

            // 4. Dapatkan tahun akademik aktif
            TahunAkademik tahunDAO = new TahunAkademik();
            String idTahunAktif = tahunDAO.getIdTahunAktif();
            if (idTahunAktif == null) {
                Tampilan.gagal("Tidak ada tahun akademik aktif saat ini.");
                return false;
            }

            // 5. Cek apakah mahasiswa sudah punya KRS di semester ini
            //    Kalau sudah ada → tambah detail_krs saja (tidak buat KRS baru)
            PreparedStatement cekKRS = conn.prepareStatement(
                "SELECT id_krs FROM b1.krs WHERE nim = ? AND id_tahun = ?");
            cekKRS.setString(1, nim);
            cekKRS.setString(2, idTahunAktif);
            ResultSet rsKRS = cekKRS.executeQuery();

            String idKRS = null;
            if (rsKRS.next()) {
                idKRS = rsKRS.getString("id_krs");
            }

            String nidnPembimbing = null;

            PreparedStatement psPembimbing = conn.prepareStatement(
                "SELECT nidn FROM b1.dosen_pembimbing WHERE nim = ?"
            );

            psPembimbing.setString(1, nim);

            ResultSet rsPembimbing = psPembimbing.executeQuery();

            if (rsPembimbing.next()) {
                nidnPembimbing = rsPembimbing.getString("nidn");
            }

            if (idKRS == null) {
                // Buat KRS baru
                idKRS = String.format("KRS%07d", System.currentTimeMillis() % 10000000L);

                PreparedStatement psKRS = conn.prepareStatement(
                    "INSERT INTO b1.krs (id_krs, nim, id_tahun, nidn_pembimbing, tanggal_ambil) " +
                    "VALUES (?, ?, ?, ?, CURRENT_DATE)");
                psKRS.setString(1, idKRS);
                psKRS.setString(2, nim);
                psKRS.setString(3, idTahunAktif);
                psKRS.setString(4, nidnPembimbing);
                psKRS.executeUpdate();
            }

            // 6. Tambah detail_krs untuk kelas yang dipilih mahasiswa
            String idDetail = String.format("DET%07d", System.currentTimeMillis() % 10000000L);

            PreparedStatement psDetail = conn.prepareStatement(
                "INSERT INTO b1.detail_krs (id_detail_krs, id_krs, id_kelas_kuliah) " +
                "VALUES (?, ?, ?)");
            psDetail.setString(1, idDetail);
            psDetail.setString(2, idKRS);
            psDetail.setString(3, idKelasKuliah);
            psDetail.executeUpdate();

            Tampilan.sukses("KRS berhasil diambil! Menunggu persetujuan.");
            return true;

        } catch (Exception e) {
            Tampilan.gagal("Gagal mengambil KRS: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ===================== LIHAT KRS MAHASISWA =====================
    public void lihatKRS(String nim) {
        if (nim == null || nim.trim().isEmpty()) {
            Tampilan.gagal("NIM tidak boleh kosong!");
            return;
        }

        try (Connection conn = Koneksi.connect()) {
            String sql =
                "SELECT k.id_krs, ta.tahun, ta.semester_aktif, k.tanggal_ambil, " +
                "k.status_persetujuan, d.nama AS nama_pembimbing, " +
                "COUNT(dk.id_detail_krs) AS total_matkul " +
                "FROM b1.krs k " +
                "JOIN b1.tahun_akademik ta   ON k.id_tahun = ta.id_tahun " +
                "LEFT JOIN b1.dosen d        ON k.nidn_pembimbing = d.nidn " +
                "LEFT JOIN b1.detail_krs dk  ON k.id_krs = dk.id_krs " +
                "WHERE k.nim = ? " +
                "GROUP BY k.id_krs, ta.tahun, ta.semester_aktif, " +
                "k.tanggal_ambil, k.status_persetujuan, d.nama " +
                "ORDER BY k.tanggal_ambil DESC";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nim.trim());
            ResultSet rs = ps.executeQuery();

            List<String[]> rows = new ArrayList<>();
            while (rs.next()) {
                rows.add(new String[]{
                    rs.getString("id_krs"),
                    rs.getString("tahun") + " " + rs.getString("semester_aktif"),
                    rs.getString("tanggal_ambil"),
                    rs.getString("total_matkul"),
                    rs.getString("nama_pembimbing") != null ? rs.getString("nama_pembimbing") : "-",
                    rs.getString("status_persetujuan")
                });
            }

            System.out.println("\n=== DATA KRS SAYA ===");
            if (rows.isEmpty()) {
                Tampilan.peringatan("\nBelum ada KRS yang diambil.");
            } else {
                String[] headers = {
                    "ID KRS", "Tahun/Semester", "Tanggal Ambil",
                    "Total Matkul", "Dosen Pembimbing", "Status"
                };
                Tampilan.tampilTabel(headers, rows.toArray(new String[0][]));
            }

        } catch (Exception e) {
            Tampilan.gagal("Gagal menampilkan data KRS!");
            e.printStackTrace();
        }
    }

    // ===================== KELOLA KRS — DOSEN PEMBIMBING (Logika Baru) =====================
    /**
     * Hanya dosen PEMBIMBING yang bisa setujui/tolak KRS.
     * Filter: k.nidn_pembimbing = ? (bukan kk.nidn = ? seperti logika lama)
     */
    public void kelolaKRS(String nidn) {
        if (nidn == null || nidn.trim().isEmpty()) {
            Tampilan.gagal("NIDN tidak boleh kosong!");
            return;
        }

        Scanner input = new Scanner(System.in);

        try (Connection conn = Koneksi.connect()) {
            String sql =
                "SELECT k.id_krs, m.nim, m.nama, ta.tahun, ta.semester_aktif, " +
                "k.tanggal_ambil, COUNT(dk.id_detail_krs) AS total_matkul " +
                "FROM b1.krs k " +
                "JOIN b1.mahasiswa m         ON k.nim = m.nim " +
                "JOIN b1.tahun_akademik ta   ON k.id_tahun = ta.id_tahun " +
                "LEFT JOIN b1.detail_krs dk  ON k.id_krs = dk.id_krs " +
                "WHERE k.nidn_pembimbing = ? AND k.status_persetujuan = 'Menunggu' " +
                "GROUP BY k.id_krs, m.nim, m.nama, ta.tahun, ta.semester_aktif, k.tanggal_ambil " +
                "ORDER BY k.tanggal_ambil";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nidn.trim());
            ResultSet rs = ps.executeQuery();

            List<String[]> rows = new ArrayList<>();
            while (rs.next()) {
                rows.add(new String[]{
                    rs.getString("id_krs"),
                    rs.getString("nim"),
                    rs.getString("nama"),
                    rs.getString("tahun") + " " + rs.getString("semester_aktif"),
                    rs.getString("tanggal_ambil"),
                    rs.getString("total_matkul")
                });
            }

            System.out.println("\n=== KRS MENUNGGU PERSETUJUAN ===");
            if (rows.isEmpty()) {
                Tampilan.peringatan("\nTidak ada KRS yang menunggu persetujuan.");
                return;
            }

            String[] headers = {"ID KRS", "NIM", "Nama", "Tahun/Semester", "Tanggal", "Total Matkul"};
            Tampilan.tampilTabel(headers, rows.toArray(new String[0][]));

            String idKRS = inputWajib(input, "\nMasukkan ID KRS yang akan diproses : ");

            // Validasi ID KRS harus dari daftar mahasiswa bimbingan dosen ini
            boolean idValid = false;
            for (String[] row : rows) {
                if (row[0].equals(idKRS)) { idValid = true; break; }
            }
            if (!idValid) {
                Tampilan.gagal("ID KRS tidak ditemukan atau bukan wewenang Anda!");
                return;
            }

            // Input keputusan
            String keputusan;
            do {
                keputusan = inputWajib(input, "Keputusan (Disetujui/Ditolak)       : ");
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

    // ===================== PRIVATE HELPER =====================

    private String cariTahunAktif(Connection conn) throws Exception {
        ResultSet rs = conn.createStatement().executeQuery(
            "SELECT id_tahun FROM b1.tahun_akademik WHERE status = 'Aktif' LIMIT 1");
        return rs.next() ? rs.getString("id_tahun") : null;
    }

    private String cariProdiMahasiswa(Connection conn, String nim) throws Exception {
        PreparedStatement ps = conn.prepareStatement(
            "SELECT id_program_studi FROM b1.mahasiswa WHERE nim = ?");
        ps.setString(1, nim);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getString("id_program_studi") : null;
    }

    private boolean sudahAmbilKRS(Connection conn, String nim, String idTahun) throws Exception {
        PreparedStatement ps = conn.prepareStatement(
            "SELECT 1 FROM b1.krs WHERE nim = ? AND id_tahun = ?");
        ps.setString(1, nim);
        ps.setString(2, idTahun);
        return ps.executeQuery().next();
    }

    private List<String> cariKelasAktif(Connection conn, String idProdi, String idTahun) throws Exception {
        PreparedStatement ps = conn.prepareStatement(
            "SELECT kk.id_kelas_kuliah " +
            "FROM b1.kelas_kuliah kk " +
            "JOIN b1.mata_kuliah mk ON kk.id_mata_kuliah = mk.id_mata_kuliah " +
            "WHERE mk.id_program_studi = ? AND kk.id_tahun = ?");
        ps.setString(1, idProdi);
        ps.setString(2, idTahun);
        ResultSet rs = ps.executeQuery();

        List<String> hasil = new ArrayList<>();
        while (rs.next()) hasil.add(rs.getString("id_kelas_kuliah"));
        return hasil;
    }

    private String generateIdKRS(Connection conn) throws Exception {
        ResultSet rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM b1.krs");
        rs.next();
        return String.format("KRS%07d", rs.getInt(1) + 1);
    }

    private int ambilCounterDetail(Connection conn) throws Exception {
        ResultSet rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM b1.detail_krs");
        rs.next();
        return rs.getInt(1) + 1;
    }
}