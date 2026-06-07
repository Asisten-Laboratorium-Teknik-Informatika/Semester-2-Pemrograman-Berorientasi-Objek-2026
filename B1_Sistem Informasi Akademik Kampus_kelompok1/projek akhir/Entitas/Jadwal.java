package Entitas;

import Fitur.Tampilan;
import Koneksi.Koneksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Jadwal {

    private String idJadwal;
    private String idKelasKuliah;
    private String hari;
    private Time   jamMulai;
    private Time   jamSelesai;

    public Jadwal() {}

    public Jadwal(String idJadwal, String idKelasKuliah,
                  String hari, Time jamMulai, Time jamSelesai) {
        this.idJadwal      = idJadwal;
        this.idKelasKuliah = idKelasKuliah;
        this.hari          = hari;
        this.jamMulai      = jamMulai;
        this.jamSelesai    = jamSelesai;
    }

    public String getIdJadwal()      { return idJadwal; }
    public String getIdKelasKuliah() { return idKelasKuliah; }
    public String getHari()          { return hari; }
    public Time   getJamMulai()      { return jamMulai; }
    public Time   getJamSelesai()    { return jamSelesai; }

    public void setIdJadwal(String idJadwal)           { this.idJadwal = idJadwal; }
    public void setIdKelasKuliah(String idKelasKuliah) { this.idKelasKuliah = idKelasKuliah; }
    public void setHari(String hari)                   { this.hari = hari; }
    public void setJamMulai(Time jamMulai)             { this.jamMulai = jamMulai; }
    public void setJamSelesai(Time jamSelesai)         { this.jamSelesai = jamSelesai; }

    // ===================== TAMBAH JADWAL =====================
    public static void tambahJadwal() {
        Scanner input = new Scanner(System.in);

        System.out.println("=== DATA JADWAL ===\n");
        System.out.print("ID Jadwal: ");           String id_jadwal       = input.nextLine().trim();
        System.out.print("ID Kelas Kuliah: ");     String id_kelas_kuliah = input.nextLine().trim();
        System.out.print("Hari (Senin-Jumat): ");  String hari            = input.nextLine().trim();
        System.out.print("Jam Mulai (HH:MM): ");   String jam_mulai       = input.nextLine().trim();
        System.out.print("Jam Selesai (HH:MM): "); String jam_selesai     = input.nextLine().trim();

        if (id_jadwal.isEmpty() || id_kelas_kuliah.isEmpty()
                || hari.isEmpty() || jam_mulai.isEmpty() || jam_selesai.isEmpty()) {
            System.out.println("Semua data wajib diisi!");
            return;
        }

        hari = hari.substring(0, 1).toUpperCase() + hari.substring(1).toLowerCase();

        if (!hari.equals("Senin") && !hari.equals("Selasa") && !hari.equals("Rabu")
                && !hari.equals("Kamis") && !hari.equals("Jumat")) {
            System.out.println("Hari tidak valid!");
            return;
        }

        Time jamMulai;
        Time jamSelesai;

        try {
            jamMulai   = Time.valueOf(jam_mulai   + ":00");
            jamSelesai = Time.valueOf(jam_selesai + ":00");
        } catch (Exception e) {
            System.out.println("Format jam harus HH:MM");
            return;
        }

        if (!jamSelesai.after(jamMulai)) {
            System.out.println("Jam selesai harus lebih besar dari jam mulai!");
            return;
        }

        try (Connection conn = Koneksi.connect()) {
            if (conn == null) { System.out.println("Koneksi database gagal!"); return; }

            // Cek duplikat ID jadwal
            String cekJadwal = "SELECT COUNT(*) FROM b1.jadwal WHERE id_jadwal = ?";
            try (PreparedStatement ps = conn.prepareStatement(cekJadwal)) {
                ps.setString(1, id_jadwal);
                ResultSet rs = ps.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("ID Jadwal sudah digunakan!"); return;
                }
            }

            // Cek kelas kuliah 
            String cekKelas = "SELECT COUNT(*) FROM b1.kelas_kuliah WHERE id_kelas_kuliah = ?";
            try (PreparedStatement ps = conn.prepareStatement(cekKelas)) {
                ps.setString(1, id_kelas_kuliah);
                ResultSet rs = ps.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    System.out.println("ID Kelas Kuliah tidak ditemukan!"); return;
                }
            }

            String query =
                "INSERT INTO b1.jadwal(id_jadwal, id_kelas_kuliah, hari, jam_mulai, jam_selesai) " +
                "VALUES (?, ?, ?::b1.hari_enum, ?, ?)";

            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, id_jadwal);
                ps.setString(2, id_kelas_kuliah);
                ps.setString(3, hari);
                ps.setTime(4, jamMulai);
                ps.setTime(5, jamSelesai);

                if (ps.executeUpdate() > 0) {
                    System.out.println("\n=== DATA JADWAL BERHASIL DISIMPAN ===");
                    System.out.println("ID Jadwal       : " + id_jadwal);
                    System.out.println("ID Kelas Kuliah : " + id_kelas_kuliah);
                    System.out.println("Hari            : " + hari);
                    System.out.println("Jam Mulai       : " + jam_mulai);
                    System.out.println("Jam Selesai     : " + jam_selesai);
                }
            }

        } catch (Exception e) {
            System.out.println("=== Terjadi kesalahan pada data! ===" + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===================== LIHAT SEMUA JADWAL =====================
    public void lihatJadwal() {
        try (Connection conn = Koneksi.connect()) {
            String sql =
                "SELECT id_jadwal, hari, jam_mulai, jam_selesai " +
                "FROM b1.jadwal " +
                "ORDER BY hari, jam_mulai";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            List<String[]> rows = new ArrayList<>();
            while (rs.next()) {
                rows.add(new String[]{
                    rs.getString("id_jadwal"),
                    rs.getString("hari"),
                    rs.getString("jam_mulai"),
                    rs.getString("jam_selesai")
                });
            }

            System.out.println("\n=== DATA JADWAL ===");
            if (rows.isEmpty()) {
                Tampilan.peringatan("Tidak ada data jadwal.");
            } else {
                String[] headers = {"ID Jadwal", "Hari", "Jam Mulai", "Jam Selesai"};
                Tampilan.tampilTabel(headers, rows.toArray(new String[0][]));
            }

        } catch (Exception e) {
            Tampilan.gagal("Gagal menampilkan data jadwal!");
            e.printStackTrace();
        }
    }

    // ===================== LIHAT JADWAL MAHASISWA =====================
    public void lihatJadwalMahasiswa(String nim) {
        if (nim == null || nim.trim().isEmpty()) {
            Tampilan.gagal("NIM tidak boleh kosong!");
            return;
        }

        String sql =
            "SELECT * FROM b1.view_jadwal_mahasiswa " +
            "WHERE nim = ? " +
            "ORDER BY hari, jam_mulai";

        try (Connection conn = Koneksi.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nim);
            ResultSet rs = ps.executeQuery();

            List<String[]> rows = new ArrayList<>();
            while (rs.next()) {
                rows.add(new String[]{
                    rs.getString("hari"),
                    rs.getString("jam_mulai"),
                    rs.getString("jam_selesai"),
                    rs.getString("nama_mata_kuliah"),
                    rs.getString("nama_ruangan"),
                    rs.getString("nama_dosen")
                });
            }

            System.out.println("\n=== JADWAL SAYA ===");
            if (rows.isEmpty()) {
                Tampilan.peringatan("Belum ada jadwal. KRS mungkin belum disetujui.");
            } else {
                String[] headers = {"Hari", "Mulai", "Selesai", "Mata Kuliah", "Ruangan", "Dosen"};
                Tampilan.tampilTabel(headers, rows.toArray(new String[0][]));
            }

        } catch (Exception e) {
            Tampilan.gagal("Gagal menampilkan jadwal!");
            e.printStackTrace();
        }
    }
}