import java.sql.Connection;
import java.sql.PreparedStatement;
import java.math.BigDecimal;
import java.util.Scanner;

public class InsertDetailPembayaran {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.print("ID Pembayaran : ");
        int idPembayaran = input.nextInt();
        input.nextLine();

        System.out.print("Jenis Biaya : ");
        String jenisBiaya = input.nextLine();

        System.out.print("Subtotal : ");
        BigDecimal subtotal = input.nextBigDecimal();
        try {

            Connection conn = Koneksi.connect();

            String sql = "INSERT INTO sbd.detail_pembayaran(id_pembayaran, jenis_biaya, subtotal) VALUES (?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, idPembayaran);
            ps.setString(2, jenisBiaya);
            ps.setBigDecimal(3, subtotal);

            ps.executeUpdate();

            System.out.println("Data detail pembayaran berhasil ditambahkan!");

            input.close();

        } catch (Exception e) {

            System.out.println("Gagal insert data detail pembayaran!");
            e.printStackTrace();

        }
    }
}