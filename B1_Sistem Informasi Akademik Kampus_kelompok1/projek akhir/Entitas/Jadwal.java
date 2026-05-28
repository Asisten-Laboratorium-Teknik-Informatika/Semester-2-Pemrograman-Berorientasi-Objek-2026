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

public class Jadwal {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("=== DATA JADWAL ===\n");
        System.out.print("ID jadwal: ");        String id_jadwal = input.nextLine();
        System.out.print("ID kelas kuliah: ");  String id_kelas_kuliah = input.nextLine();
        System.out.print("ID ruangan: ");       String id_ruangan = input.nextLine();
        System.out.print("Hari: ");             String hari = input.nextLine();
        System.out.print("Jam mulai: ");        String jam_mulai = input.nextLine();
        System.out.print("Jam selesai: ");      String jam_selesai = input.nextLine();

        try {
            Connection conn = Koneksi.connect();
            if (conn == null) { System.out.println("Koneksi gagal!"); return; }
            String query =
                "INSERT INTO b1.jadwal(id_jadwal, id_kelas_kuliah, id_ruangan, hari, jam_mulai, jam_selesai) " +
                "VALUES(?, ?, ?, ?::hari_enum, ?::time, ?::time)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, id_jadwal); ps.setString(2, id_kelas_kuliah);
            ps.setString(3, id_ruangan); ps.setString(4, hari);
            ps.setString(5, jam_mulai); ps.setString(6, jam_selesai);
            if (ps.executeUpdate() > 0)
                System.out.println("===> Data berhasil ditambahkan!");
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===================== LIHAT SEMUA JADWAL =====================
    public void lihatJadwal() {
        try (Connection conn = Koneksi.connect()) {
            String sql =
                "SELECT id_jadwal, hari, jam_mulai, jam_selesai " +
                "FROM b1.jadwal ORDER BY hari, jam_mulai";
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
        String sql =
            "SELECT * FROM b1.view_jadwal_mahasiswa " +
            "WHERE nim = ? ORDER BY hari, jam_mulai";

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