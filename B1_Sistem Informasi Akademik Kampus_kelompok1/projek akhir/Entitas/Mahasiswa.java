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

public class Mahasiswa {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("=== DATA MAHASISWA ===\n");
        System.out.print("ID program studi: ");
        String id_program_studi = input.nextLine();
        System.out.print("NIM: ");
        String nim = input.nextLine();
        System.out.print("Nama: ");
        String nama = input.nextLine();
        System.out.print("Akun E-Mail: ");
        String email = input.nextLine();
        System.out.print("Jenis kelamin (Laki-Laki/Perempuan): ");
        String jenis_kelamin = input.nextLine();
        System.out.print("Nomor handphone: ");
        String no_hp = input.nextLine();
        System.out.print("Tanggal lahir (YYYY-MM-DD): ");
        String tanggal_lahir = input.nextLine();

        java.sql.Date tanggalLahir;
        try {
            tanggalLahir = java.sql.Date.valueOf(tanggal_lahir);
        } catch (IllegalArgumentException e) {
            System.out.println("Format tanggal salah!");
            return;
        }

        try {
            Connection conn = Koneksi.connect();
            if (conn == null) { System.out.println("Koneksi gagal!"); return; }
            String query =
                "INSERT INTO b1.mahasiswa(id_program_studi, nim, nama, email, jenis_kelamin, no_hp, tanggal_lahir) " +
                "VALUES(?, ?, ?, ?, ?::jenis_kelamin_mahasiswa_enum, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, id_program_studi); ps.setString(2, nim);
            ps.setString(3, nama); ps.setString(4, email);
            ps.setString(5, jenis_kelamin); ps.setString(6, no_hp);
            ps.setDate(7, tanggalLahir);
            if (ps.executeUpdate() > 0)
                System.out.println("===> Data berhasil ditambahkan!");
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===================== LIHAT SEMUA MAHASISWA =====================
    public void lihatMahasiswa() {
        try (Connection conn = Koneksi.connect()) {
            String sql = "SELECT nim, nama, email FROM b1.mahasiswa ORDER BY nim";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            List<String[]> rows = new ArrayList<>();
            while (rs.next()) {
                rows.add(new String[]{
                    rs.getString("nim"),
                    rs.getString("nama"),
                    rs.getString("email")
                });
            }

            System.out.println("\n=== DATA MAHASISWA ===");
            if (rows.isEmpty()) {
                Tampilan.peringatan("Tidak ada data mahasiswa.");
            } else {
                String[] headers = {"NIM", "Nama", "Email"};
                Tampilan.tampilTabel(headers, rows.toArray(new String[0][]));
            }

        } catch (Exception e) {
            Tampilan.gagal("Gagal menampilkan data mahasiswa!");
            e.printStackTrace();
        }
    }

    // ===================== LIHAT DATA DIRI =====================
    public void lihatDataDiri(String nim) {
        try (Connection conn = Koneksi.connect()) {
            String sql =
                "SELECT m.nim, m.nama, m.email, m.jenis_kelamin, m.no_hp, " +
                "m.tanggal_lahir, p.nama_program_studi, p.jenjang, p.fakultas " +
                "FROM b1.mahasiswa m " +
                "JOIN b1.program_studi p ON m.id_program_studi = p.id_program_studi " +
                "WHERE m.nim = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nim);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Tabel data diri pakai format key-value dinamis
                String[] headers = {"Field", "Data"};
                String[][] rows = {
                    {"NIM",            rs.getString("nim")},
                    {"Nama",           rs.getString("nama")},
                    {"Email",          rs.getString("email")},
                    {"Jenis Kelamin",  rs.getString("jenis_kelamin")},
                    {"No. HP",         rs.getString("no_hp") != null ? rs.getString("no_hp") : "-"},
                    {"Tanggal Lahir",  rs.getString("tanggal_lahir")},
                    {"Program Studi",  rs.getString("nama_program_studi")},
                    {"Jenjang",        rs.getString("jenjang")},
                    {"Fakultas",       rs.getString("fakultas")}
                };
                System.out.println("\n=== DATA DIRI MAHASISWA ===");
                Tampilan.tampilTabel(headers, rows);
            } else {
                Tampilan.gagal("Data mahasiswa tidak ditemukan.");
            }

        } catch (Exception e) {
            Tampilan.gagal("Gagal menampilkan data diri!");
            e.printStackTrace();
        }
    }
}