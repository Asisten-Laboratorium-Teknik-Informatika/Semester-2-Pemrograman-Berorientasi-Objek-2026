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

public class KelasKuliah {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("=== DATA KELAS KULIAH ===\n");
        System.out.print("ID kelas kuliah: "); String id_kelas_kuliah = input.nextLine();
        System.out.print("ID mata kuliah: ");  String id_mata_kuliah = input.nextLine();
        System.out.print("NIDN: ");            String nidn = input.nextLine();
        System.out.print("ID tahun: ");        String id_tahun = input.nextLine();
        System.out.print("ID ruangan: ");      String id_ruangan = input.nextLine();
        System.out.print("Nama kelas: ");      String nama_kelas = input.nextLine();
        System.out.print("Kuota: ");
        short kuota = input.nextShort();
        input.nextLine();

        try {
            Connection conn = Koneksi.connect();
            if (conn == null) { System.out.println("Koneksi gagal!"); return; }
            String query =
                "INSERT INTO b1.kelas_kuliah(id_kelas_kuliah, id_mata_kuliah, nidn, id_tahun, id_ruangan, nama_kelas, kuota) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, id_kelas_kuliah); ps.setString(2, id_mata_kuliah);
            ps.setString(3, nidn); ps.setString(4, id_tahun);
            ps.setString(5, id_ruangan); ps.setString(6, nama_kelas);
            ps.setShort(7, kuota);
            if (ps.executeUpdate() > 0)
                System.out.println("===> Data berhasil ditambahkan!");
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===================== LIHAT SEMUA KELAS =====================
    public void lihatKelas() {
        try (Connection conn = Koneksi.connect()) {
            String sql =
                "SELECT kk.id_kelas_kuliah, mk.nama_mata_kuliah, kk.nama_kelas, mk.sks, kk.kuota, " +
                "(SELECT COUNT(*) FROM b1.krs r WHERE r.id_kelas_kuliah = kk.id_kelas_kuliah " +
                " AND r.status_persetujuan = 'Disetujui') AS terisi " +
                "FROM b1.kelas_kuliah kk " +
                "JOIN b1.mata_kuliah mk ON kk.id_mata_kuliah = mk.id_mata_kuliah " +
                "ORDER BY kk.id_kelas_kuliah";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            List<String[]> rows = new ArrayList<>();
            while (rs.next()) {
                int kuota  = rs.getInt("kuota");
                int terisi = rs.getInt("terisi");
                rows.add(new String[]{
                    rs.getString("id_kelas_kuliah"),
                    rs.getString("nama_mata_kuliah"),
                    rs.getString("nama_kelas"),
                    rs.getString("sks"),
                    terisi + "/" + kuota
                });
            }

            System.out.println("\n=== KELAS KULIAH TERSEDIA ===");
            if (rows.isEmpty()) {
                Tampilan.peringatan("Tidak ada kelas tersedia.");
            } else {
                String[] headers = {"ID Kelas", "Mata Kuliah", "Kelas", "SKS", "Kuota/Terisi"};
                Tampilan.tampilTabel(headers, rows.toArray(new String[0][]));
            }

        } catch (Exception e) {
            Tampilan.gagal("Gagal menampilkan data kelas!");
            e.printStackTrace();
        }
    }

    // ===================== LIHAT KELAS MILIK DOSEN =====================
    public void lihatKelasByDosen(String nidn) {
        try (Connection conn = Koneksi.connect()) {
            String sql =
                "SELECT kk.id_kelas_kuliah, mk.nama_mata_kuliah, kk.nama_kelas, mk.sks, kk.kuota, " +
                "(SELECT COUNT(*) FROM b1.krs r WHERE r.id_kelas_kuliah = kk.id_kelas_kuliah " +
                " AND r.status_persetujuan = 'Disetujui') AS terisi " +
                "FROM b1.kelas_kuliah kk " +
                "JOIN b1.mata_kuliah mk ON kk.id_mata_kuliah = mk.id_mata_kuliah " +
                "WHERE kk.nidn = ? ORDER BY kk.id_kelas_kuliah";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nidn);
            ResultSet rs = ps.executeQuery();

            List<String[]> rows = new ArrayList<>();
            while (rs.next()) {
                int kuota  = rs.getInt("kuota");
                int terisi = rs.getInt("terisi");
                rows.add(new String[]{
                    rs.getString("id_kelas_kuliah"),
                    rs.getString("nama_mata_kuliah"),
                    rs.getString("nama_kelas"),
                    rs.getString("sks"),
                    terisi + "/" + kuota
                });
            }

            System.out.println("\n=== KELAS SAYA ===");
            if (rows.isEmpty()) {
                Tampilan.peringatan("Anda belum memiliki kelas.");
            } else {
                String[] headers = {"ID Kelas", "Mata Kuliah", "Kelas", "SKS", "Kuota/Terisi"};
                Tampilan.tampilTabel(headers, rows.toArray(new String[0][]));
            }

        } catch (Exception e) {
            Tampilan.gagal("Gagal menampilkan kelas dosen!");
            e.printStackTrace();
        }
    }
}