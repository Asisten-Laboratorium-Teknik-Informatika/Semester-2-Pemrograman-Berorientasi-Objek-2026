import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.util.Scanner;

public class InsertPasien {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.print("Nama Pasien : ");
        String nama = input.nextLine();

        System.out.print("Tanggal Lahir (yyyy-mm-dd) : ");
        Date tanggal = Date.valueOf(input.nextLine());

        System.out.print("Jenis Kelamin : ");
        String jk = input.nextLine();

        System.out.print("Alamat : ");
        String alamat = input.nextLine();

        System.out.print("No HP : ");
        String hp = input.nextLine();
        try {

            Connection conn = Koneksi.connect();

            String sql = "INSERT INTO sbd.pasien(nama_pasien, tanggal_lahir, jenis_kelamin, alamat, no_hp) VALUES";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, nama);
            ps.setDate(2, tanggal);
            ps.setString(3, jk);
            ps.setString(4, alamat);
            ps.setString(5, hp);

            ps.executeUpdate();

            System.out.println("Data pasien berhasil ditambahkan!");

            input.close();

        } catch (Exception e) {

            System.out.println("Gagal insert data pasien!");
            e.printStackTrace();

        }
    }
}