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

public class Presensi {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("=== DATA PRESENSI ===\n");
        System.out.print("ID Presensi: ");                               String id_presensi = input.nextLine();
        System.out.print("ID Detail KRS: ");                             String id_detail_krs = input.nextLine();
        System.out.print("ID Jadwal: ");                                 String id_jadwal = input.nextLine();
        System.out.print("Status Kehadiran (Hadir/Izin/Sakit/Absen): "); String status_kehadiran = input.nextLine();
        System.out.print("Pertemuan Ke: ");                              short pertemuan = Short.parseShort(input.nextLine().trim());

        try {
            Connection conn = Koneksi.connect();

            if (conn == null) {
                System.out.println("Koneksi gagal!");
                return;
            }

            String query =
            "INSERT INTO b1.presensi(id_presensi, id_detail_krs, id_jadwal, status_kehadiran, pertemuan) " +
            "VALUES(?, ?, ?, ?::b1.status_kehadiran_enum, ?)";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, id_presensi); ps.setString(2, id_detail_krs);
            ps.setString(3, id_jadwal);   ps.setString(4, status_kehadiran);
            ps.setShort(5, pertemuan);
            
            if (ps.executeUpdate() > 0)
                System.out.println("===> Data berhasil ditambahkan! Silakan cek database.");

        } catch (Exception e) {
            System.out.println("=== Terjadi kesalahan pada data! ===\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===================== REKAP PRESENSI SEMUA =====================
    public void rekapPresensi() {
        try (Connection conn = Koneksi.connect()) {
            String sql =
            "SELECT status_kehadiran, COUNT(*) AS jumlah " +
            "FROM b1.presensi GROUP BY status_kehadiran";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            List<String[]> rows = new ArrayList<>();
            while (rs.next()) {
                rows.add(new String[]{
                    rs.getString("status_kehadiran"),
                    String.valueOf(rs.getInt("jumlah"))
                });
            }

            System.out.println("\n=== REKAP PRESENSI ===");
            if (rows.isEmpty()) {
                Tampilan.peringatan("\nTidak ada data presensi.");
            } else {
                String[] headers = {"Status", "Jumlah"};
                Tampilan.tampilTabel(headers, rows.toArray(new String[0][]));
            }

        } catch (Exception e) {
            Tampilan.gagal("Gagal menampilkan rekap presensi!");
            e.printStackTrace();
        }
    }

    // ===================== REKAP PRESENSI MAHASISWA =====================
    public void rekapPresensiMahasiswa(String nim) {
        String sql =
            "SELECT mk.nama_mata_kuliah, " +
            "COUNT(*) AS total_pertemuan, " +
            "SUM(CASE WHEN p.status_kehadiran = 'Hadir' THEN 1 ELSE 0 END) AS hadir, " +
            "SUM(CASE WHEN p.status_kehadiran = 'Izin'  THEN 1 ELSE 0 END) AS izin, " +
            "SUM(CASE WHEN p.status_kehadiran = 'Sakit' THEN 1 ELSE 0 END) AS sakit, " +
            "SUM(CASE WHEN p.status_kehadiran = 'Absen' THEN 1 ELSE 0 END) AS absen " +
            "FROM b1.presensi p " +
            "JOIN b1.detail_krs dk   ON p.id_detail_krs = dk.id_detail_krs " +
            "JOIN b1.krs k           ON dk.id_krs = k.id_krs " +
            "JOIN b1.kelas_kuliah kk ON dk.id_kelas_kuliah = kk.id_kelas_kuliah " +
            "JOIN b1.mata_kuliah mk  ON kk.id_mata_kuliah = mk.id_mata_kuliah " +
            "WHERE k.nim = ? " +
            "GROUP BY mk.nama_mata_kuliah";

        try (Connection conn = Koneksi.connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nim);
            ResultSet rs = ps.executeQuery();

            List<String[]> rows = new ArrayList<>();
            while (rs.next()) {
                int total = rs.getInt("total_pertemuan");
                int hadir = rs.getInt("hadir");
                double persen = total > 0 ? (hadir * 100.0 / total) : 0;
                
                // Tandai dengan simbol (!) jika kehadiran < 75%
                String warning = persen < 75 ? " (!)" : "";

                rows.add(new String[]{
                    rs.getString("nama_mata_kuliah"),
                    String.valueOf(total),
                    hadir + warning,
                    String.valueOf(rs.getInt("izin")),
                    String.valueOf(rs.getInt("sakit")),
                    String.valueOf(rs.getInt("absen"))
                });
            }

            System.out.println("\n=== REKAP PRESENSI SAYA ===");
            if (rows.isEmpty()) {
                Tampilan.peringatan("\nBelum ada data presensi.");
            } else {
                String[] headers = {"Mata Kuliah", "Total", "Hadir", "Izin", "Sakit", "Absen"};
                Tampilan.tampilTabel(headers, rows.toArray(new String[0][]));
                Tampilan.peringatan("\n(!) = Kehadiran di bawah 75%");
            }

        } catch (Exception e) {
            Tampilan.gagal("Gagal menampilkan rekap presensi!");
            e.printStackTrace();
        }
    }

    // ===================== INPUT PRESENSI (untuk Dosen) =====================
    public void inputPresensi(String nidn) {
        Scanner input = new Scanner(System.in);

        try (Connection conn = Koneksi.connect()) {
            // Tampilkan kelas milik dosen
            String sqlKelas =
                "SELECT kk.id_kelas_kuliah, mk.nama_mata_kuliah, kk.nama_kelas " +
                "FROM b1.kelas_kuliah kk " +
                "JOIN b1.mata_kuliah mk ON kk.id_mata_kuliah = mk.id_mata_kuliah " +
                "WHERE kk.nidn = ?";

            PreparedStatement ps1 = conn.prepareStatement(sqlKelas);
            ps1.setString(1, nidn);
            ResultSet rs1 = ps1.executeQuery();

            List<String[]> rowsKelas = new ArrayList<>();
            while (rs1.next()) {
                rowsKelas.add(new String[]{
                    rs1.getString("id_kelas_kuliah"),
                    rs1.getString("nama_mata_kuliah"),
                    rs1.getString("nama_kelas")
                });
            }

            System.out.println("\n=== KELAS SAYA ===");
            if (rowsKelas.isEmpty()) {
                Tampilan.peringatan("\nAnda belum memiliki kelas.");
                return;
            }

            String[] headersKelas = {"ID Kelas", "Mata Kuliah", "Kelas"};
            Tampilan.tampilTabel(headersKelas, rowsKelas.toArray(new String[0][]));

            System.out.print("Masukkan ID Kelas Kuliah: "); String idKelas = input.nextLine().trim();
            System.out.print("Pertemuan (1-16): ");         short pertemuan = Short.parseShort(input.nextLine().trim());

            // Tampilkan mahasiswa di kelas
            String sqlMhs =
                "SELECT dk.id_detail_krs, m.nim, m.nama " +
                "FROM b1.krs k " +
                "JOIN b1.detail_krs dk ON k.id_krs = dk.id_krs " +
                "JOIN b1.mahasiswa m ON k.nim = m.nim " +
                "WHERE dk.id_kelas_kuliah = ? AND k.status_persetujuan = 'Disetujui'";

            PreparedStatement ps2 = conn.prepareStatement(sqlMhs);
            ps2.setString(1, idKelas);
            ResultSet rs2 = ps2.executeQuery();

            List<String[]> listMhs = new ArrayList<>();
            while (rs2.next()) {
                listMhs.add(new String[]{
                    rs2.getString("id_detail_krs"),
                    rs2.getString("nim"),
                    rs2.getString("nama")
                });
            }

            System.out.println("\n=== DAFTAR MAHASISWA ===");
            if (listMhs.isEmpty()) {
                Tampilan.gagal("\nTidak ada mahasiswa di kelas ini.");
                return;
            }

            // Tampilkan tabel mahasiswa (tanpa id_krs)
            String[] headersMhs = {"NIM", "Nama"};
            String[][] rowsMhs = listMhs.stream()
                .map(m -> new String[]{m[1], m[2]})
                .toArray(String[][]::new);
            Tampilan.tampilTabel(headersMhs, rowsMhs);

            String sqlJadwal = "SELECT id_jadwal FROM b1.jadwal WHERE id_kelas_kuliah = ? LIMIT 1";
            
            PreparedStatement ps3 = conn.prepareStatement(sqlJadwal);
            ps3.setString(1, idKelas);
            ResultSet rs3 = ps3.executeQuery();
            
            String idJadwal = rs3.next() ? rs3.getString("id_jadwal") : null;

            if (idJadwal == null) {
                Tampilan.gagal("Jadwal untuk kelas ini tidak ditemukan.");
                return;
            }

            // Input presensi per mahasiswa
            for (String[] mhs : listMhs) {
                System.out.print("Status kehadiran " + mhs[2] + " (Hadir/Izin/Sakit/Absen): "); String status = input.nextLine().trim();

                String idPresensi = String.format("PR%08d", System.currentTimeMillis() % 100_000_000L);
                String sqlInsert =
                "INSERT INTO b1.presensi(id_presensi, id_detail_krs, id_jadwal, status_kehadiran, pertemuan) " +
                "VALUES(?, ?, ?, ?::b1.status_kehadiran_enum, ?)";

                PreparedStatement ps4 = conn.prepareStatement(sqlInsert);
                ps4.setString(1, idPresensi); ps4.setString(2, mhs[0]);
                ps4.setString(3, idJadwal);   ps4.setString(4, status);
                ps4.setShort(5, pertemuan);
                ps4.executeUpdate();
            }

            Tampilan.sukses("Presensi pertemuan ke-" + pertemuan + " berhasil disimpan!");

        } catch (Exception e) {
            Tampilan.gagal("Gagal input presensi!");
            e.printStackTrace();
        }
    }

    // ===================== REKAP KEHADIRAN DOSEN =====================
    public void rekapKehadiranDosen(String nidn) {
        String sql =
            "SELECT m.nim, m.nama, mk.nama_mata_kuliah, " +
            "COUNT(*) AS total, " +
            "SUM(CASE WHEN p.status_kehadiran = 'Hadir' THEN 1 ELSE 0 END) AS hadir, " +
            "SUM(CASE WHEN p.status_kehadiran = 'Izin'  THEN 1 ELSE 0 END) AS izin, " +
            "SUM(CASE WHEN p.status_kehadiran = 'Sakit' THEN 1 ELSE 0 END) AS sakit, " +
            "SUM(CASE WHEN p.status_kehadiran = 'Absen' THEN 1 ELSE 0 END) AS absen " +
            "FROM b1.presensi p " +
            "JOIN b1.detail_krs dk    ON p.id_detail_krs = dk.id_detail_krs " +
            "JOIN b1.krs k           ON dk.id_krs = k.id_krs " +
            "JOIN b1.mahasiswa m     ON k.nim = m.nim " +
            "JOIN b1.kelas_kuliah kk ON dk.id_kelas_kuliah = kk.id_kelas_kuliah " +
            "JOIN b1.mata_kuliah mk  ON kk.id_mata_kuliah = mk.id_mata_kuliah " +
            "WHERE kk.nidn = ? " +
            "GROUP BY m.nim, m.nama, mk.nama_mata_kuliah " +
            "ORDER BY mk.nama_mata_kuliah, m.nim";

        try (Connection conn = Koneksi.connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nidn);
            ResultSet rs = ps.executeQuery();

            List<String[]> rows = new ArrayList<>();
            while (rs.next()) {
                int total = rs.getInt("total");
                int hadir = rs.getInt("hadir");
                double persen = total > 0 ? (hadir * 100.0 / total) : 0;
                String warning = persen < 75 ? " (!)" : "";

                rows.add(new String[]{
                    rs.getString("nim"),
                    rs.getString("nama_mata_kuliah"),
                    String.valueOf(total),
                    hadir + warning,
                    String.valueOf(rs.getInt("izin")),
                    String.valueOf(rs.getInt("sakit")),
                    String.valueOf(rs.getInt("absen"))
                });
            }

            System.out.println("\n=== REKAP KEHADIRAN KELAS ===");
            if (rows.isEmpty()) {
                Tampilan.peringatan("\nBelum ada data presensi.");
            } else {
                String[] headers = {"NIM", "Mata Kuliah", "Total", "Hadir", "Izin", "Sakit", "Absen"};
                Tampilan.tampilTabel(headers, rows.toArray(new String[0][]));
                Tampilan.peringatan("\n(!) = Kehadiran di bawah 75%");
            }

        } catch (Exception e) {
            Tampilan.gagal("Gagal menampilkan rekap kehadiran!");
            e.printStackTrace();
        }
    }
}