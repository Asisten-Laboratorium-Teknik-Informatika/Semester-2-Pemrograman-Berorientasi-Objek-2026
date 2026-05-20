import java.sql.Connection;
import java.sql.PreparedStatement;
import java.math.BigDecimal;
import java.util.Scanner;

public class InsertObat {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.print("Nama Obat : ");
        String namaObat = input.nextLine();

        System.out.print("Dosis : ");
        String dosis = input.nextLine();

        System.out.print("Stok : ");
        int stok = input.nextInt();

        System.out.print("Harga Obat : ");
        BigDecimal harga = input.nextBigDecimal();
        try {

            Connection conn = Koneksi.connect();

            String sql = "INSERT INTO sbd.obat(nama_obat, dosis, stok, harga_obat) VALUES (?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, namaObat);
            ps.setString(2, dosis);
            ps.setInt(3, stok);
            ps.setBigDecimal(4, harga);

            ps.executeUpdate();

            System.out.println("Data obat berhasil ditambahkan!");

            input.close();

        } catch (Exception e) {

            System.out.println("Gagal insert data obat!");
            e.printStackTrace();

        }
    }
}