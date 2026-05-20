import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class InsertKlinik {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.print("Nama Klinik : ");
        String nama = input.nextLine();

        System.out.print("Lokasi Klinik : ");
        String lokasi = input.nextLine();
        try {

            Connection conn = Koneksi.connect();

            String sql = "INSERT INTO sbd.klinik(nama_klinik, lokasi_klinik) VALUES (?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, nama);
            ps.setString(2, lokasi);

            ps.executeUpdate();

            System.out.println("Data klinik berhasil ditambahkan!");

            input.close();

        } catch (Exception e) {

            System.out.println("Gagal insert data klinik!");
            e.printStackTrace();

        }
    }
}