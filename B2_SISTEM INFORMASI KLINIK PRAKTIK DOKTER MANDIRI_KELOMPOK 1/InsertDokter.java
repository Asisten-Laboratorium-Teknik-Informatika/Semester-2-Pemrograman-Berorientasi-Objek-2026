import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class InsertDokter {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.print("Nama drDokter : ");
        String nama = input.nextLine();

        System.out.print("Kategori : ");
        String kategori = input.nextLine();

        System.out.print("No HP : ");
        String hp = input.nextLine();
        try {

            Connection conn = Koneksi.connect();

            String sql = "INSERT INTO sbd.dokter(nama_dokter, kategori, no_hp) VALUES (?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, nama);
            ps.setString(2, kategori);
            ps.setString(3, hp);

            ps.executeUpdate();

            System.out.println("Data dokter berhasil ditambahkan!");

            input.close();

        } catch (Exception e) {

            System.out.println("Gagal insert data dokter!");
            e.printStackTrace();

        }
    }
}