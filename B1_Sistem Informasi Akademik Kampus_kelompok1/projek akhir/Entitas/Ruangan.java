package Entitas;
import Koneksi.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Ruangan {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("=== DATA RUANGAN ===\n");
        System.out.print("ID Ruangan: "); String id_ruangan    = input.nextLine();
        System.out.print("Ruang: ");      String nama_ruangan  = input.nextLine();
        System.out.print("Gedung: ");     String gedung        = input.nextLine();
        System.out.print("Kapasitas: ");  String kapasitas     = input.nextLine();

        if (id_ruangan.isEmpty() || nama_ruangan.isEmpty()
                || gedung.isEmpty() || kapasitas.isEmpty()) {
            System.out.println("Semua data wajib diisi!");
            return;
        }

        short kapasitas_kursi;
        try {
            kapasitas_kursi = Short.parseShort(kapasitas);
        } catch (NumberFormatException e) {
            System.out.println("Kapasitas harus berupa angka!");
            return;
        }

        if (kapasitas_kursi <= 0) { System.out.println("Kapasitas harus lebih dari 0!"); return; }
        if (kapasitas_kursi > 150) { System.out.println("Kapasitas terlalu besar!");      return; }

        try {
            Connection conn = Koneksi.connect();
            if (conn == null) { System.out.println("Koneksi gagal!"); return; }

            String cek = "SELECT COUNT(*) FROM b1.ruangan WHERE id_ruangan = ?";
            try (PreparedStatement psCek = conn.prepareStatement(cek)) {
                psCek.setString(1, id_ruangan);
                ResultSet rs = psCek.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("ID Ruangan sudah digunakan!");
                    return;
                }
            }

            String query =
                "INSERT INTO b1.ruangan(id_ruangan, nama_ruangan, gedung, kapasitas) " +
                "VALUES(?, ?, ?, ?)";

            PreparedStatement ps_ruangan = conn.prepareStatement(query);
            ps_ruangan.setString(1, id_ruangan); ps_ruangan.setString(2, nama_ruangan);
            ps_ruangan.setString(3, gedung);     ps_ruangan.setShort(4, kapasitas_kursi);

            int baris = ps_ruangan.executeUpdate();

            if (baris > 0) {
                System.out.println("\n=== DATA RUANGAN BERHASIL DISIMPAN ===");
                System.out.println("ID Ruangan   : " + id_ruangan);
                System.out.println("Nama Ruangan : " + nama_ruangan);
                System.out.println("Gedung       : " + gedung);
                System.out.println("Kapasitas    : " + kapasitas);
            }

        } catch (Exception e) {
            System.out.println("=== Terjadi kesalahan pada data! ===\n" + e.getMessage());
            e.printStackTrace();
        }
    }
}