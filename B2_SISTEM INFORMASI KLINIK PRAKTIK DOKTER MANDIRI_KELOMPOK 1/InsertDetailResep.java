import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class InsertDetailResep {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.print("ID Resep : ");
        int idResep = input.nextInt();

        System.out.print("ID Obat : ");
        int idObat = input.nextInt();

        System.out.print("Jumlah Obat : ");
        int jumlahObat = input.nextInt();
        input.nextLine();

        System.out.print("Aturan Pakai : ");
        String aturanPakai = input.nextLine();
        try {

            Connection conn = Koneksi.connect();

            String sql = "INSERT INTO sbd.detail_resep(id_resep, id_obat, jumlah_obat, aturan_pakai) VALUES (?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, idResep);
            ps.setInt(2, idObat);
            ps.setInt(3, jumlahObat);
            ps.setString(4, aturanPakai);

            ps.executeUpdate();

            System.out.println("Data detail resep berhasil ditambahkan!");

            input.close();

        } catch (Exception e) {

            System.out.println("Gagal insert data detail resep!");
            e.printStackTrace();

        }
    }
}