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
        System.out.print("ID Program Studi: ");                                   String id_program_studi = input.nextLine().trim();
        System.out.print("NIM: ");                                                String nim = input.nextLine().trim();
        System.out.print("Nama: ");                                               String nama = input.nextLine().trim();
        System.out.print("Akun E-Mail: ");                                        String email = input.nextLine().trim();
        System.out.print("Jenis Kelamin (Laki-Laki/Perempuan): ");                String jenis_kelamin = input.nextLine().trim();
        System.out.print("Nomor Handphone (kosongkan jika tidak ingin mengisi): "); String no_hp = input.nextLine();
        System.out.print("Tanggal Lahir (YYYY-MM-DD): ");                         String tanggal_lahir = input.nextLine().trim();

        java.sql.Date tanggalLahir;

        if (id_program_studi.isEmpty()
            || nim.isEmpty()
            || nama.isEmpty()
            || email.isEmpty()
            || jenis_kelamin.isEmpty()
            || tanggal_lahir.isEmpty()) {

            System.out.println("Semua data wajib diisi, kecuali nomor handphone.");
            return;
        }

        if (!nim.matches("\\d+")) {
            System.out.println("NIM hanya boleh berisi angka!");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            System.out.println("Format email tidak valid!");
            return;
        }

        if (!jenis_kelamin.equalsIgnoreCase("Laki-Laki")
                && !jenis_kelamin.equalsIgnoreCase("Perempuan")) {

            System.out.println("Jenis kelamin harus Laki-Laki atau Perempuan!");
            return;
        }

        if (!no_hp.isEmpty() && !no_hp.matches("\\d+")) {
            System.out.println("Nomor handphone hanya boleh berisi angka!");
            return;
        }

        try {
            tanggalLahir = java.sql.Date.valueOf(tanggal_lahir);

            if (tanggalLahir.after(new java.sql.Date(System.currentTimeMillis()))) {
                System.out.println("Tanggal lahir tidak boleh melebihi tanggal hari ini!");
                return;
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Format tanggal harus YYYY-MM-DD!");
            return;
        }

        try {
            Connection conn = Koneksi.connect();

            if (conn == null) {
                System.out.println("Koneksi gagal!");
                return;
            }

            String query =
                "INSERT INTO b1.mahasiswa(id_program_studi, nim, nama, email, jenis_kelamin, no_hp, tanggal_lahir) " +
                "VALUES(?, ?, ?, ?, ?::jenis_kelamin_mahasiswa_enum, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, id_program_studi); ps.setString(2, nim);
            ps.setString(3, nama); ps.setString(4, email);
            ps.setString(5, jenis_kelamin); ps.setString(6, no_hp);
            ps.setDate(7, tanggalLahir);

            if (no_hp.isEmpty()) {
                ps.setString(6, null);
            } else {
                ps.setString(6, no_hp);
            }
            int baris = ps.executeUpdate();

            if (baris > 0) {
                System.out.println("\n=== DATA MAHASISWA BERHASIL DISIMPAN ===");
                System.out.println("NIM  : " + nim);
                System.out.println("Nama : " + nama);
            } else {
                System.out.println("Data mahasiswa gagal disimpan.");
            }

        } catch (Exception e) {
            System.out.println("=== Terjadi kesalahan pada data! ===\n " + e.getMessage());
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
                Tampilan.peringatan("\nTidak ada data mahasiswa.");
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
                "SELECT m.nim, m.nama, m.email, m.jenis_kelamin, m.no_hp, m.tanggal_lahir, p.nama_program_studi, p.jenjang, p.fakultas " +
                "FROM b1.mahasiswa m " +
                "JOIN b1.program_studi p ON m.id_program_studi = p.id_program_studi " +
                "WHERE m.nim = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nim);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] headers = {"Identitas", "Data"};
                String[][] rows = {
                    {"NIM",            rs.getString("nim")},
                    {"Nama",           rs.getString("nama")},
                    {"E-Mail",         rs.getString("email")},
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
                Tampilan.gagal("\nData mahasiswa tidak ditemukan.");
            }

        } catch (Exception e) {
            Tampilan.gagal("Gagal menampilkan data diri!");
            e.printStackTrace();
        }
    }
}