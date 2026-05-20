import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class InsertPetugas {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.print("Nama Petugas : ");
        String nama = input.nextLine();

        System.out.print("Jabatan : ");
        String jabatan = input.nextLine();

        System.out.print("No HP : ");
        String hp = input.nextLine();
        try {

            Connection conn = Koneksi.connect();

            String sql = "INSERT INTO sbd.petugas(nama_petugas, jabatan, no_hp) VALUES (?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, nama);
            ps.setString(2, jabatan);
            ps.setString(3, hp);

            ps.executeUpdate();

            System.out.println("Data petugas berhasil ditambahkan!");

            input.close();

        } catch (Exception e) {

            System.out.println("Gagal insert data petugas!");
            e.printStackTrace();

        }
    }
}