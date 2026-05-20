import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.math.BigDecimal;
import java.util.Scanner;

public class InsertPembayaran {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.print("ID Pendaftaran : ");
        int idPendaftaran = input.nextInt();
        input.nextLine();

        System.out.print("Tanggal Bayar (yyyy-mm-dd) : ");
        String tanggalBayar = input.nextLine();

        System.out.print("Metode Pembayaran : ");
        String metode = input.nextLine();

        System.out.print("Status Pembayaran : ");
        String status = input.nextLine();

        System.out.print("Total Bayar : ");
        BigDecimal totalBayar = input.nextBigDecimal();
        try {

            Connection conn = Koneksi.connect();

            Date tanggalBayarSql = Date.valueOf(tanggalBayar);

            String sql = "INSERT INTO sbd.pembayaran(id_pendaftaran, tanggal_bayar, metode_pembayaran, status_pembayaran, total_bayar) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, idPendaftaran);
            ps.setDate(2, tanggalBayarSql);
            ps.setString(3, metode);
            ps.setString(4, status);
            ps.setBigDecimal(5, totalBayar);

            ps.executeUpdate();

            System.out.println("Data pembayaran berhasil ditambahkan!");

            input.close();

        } catch (Exception e) {

            System.out.println("Gagal insert data pembayaran!");
            e.printStackTrace();

        }
    }
}