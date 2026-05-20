import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.util.Scanner;

public class InsertResep {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.print("ID Rekam Medis : ");
        int idRekamMedis = input.nextInt();
        input.nextLine();

        System.out.print("Tanggal Resep (yyyy-mm-dd) : ");
        String tanggal = input.nextLine();
        try {

            Connection conn = Koneksi.connect();

            Date tanggalSql = Date.valueOf(tanggal);

            String sql = "INSERT INTO sbd.resep(id_rekam_medis, tanggal_resep) VALUES (?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, idRekamMedis);
            ps.setDate(2, tanggalSql);

            ps.executeUpdate();

            System.out.println("Data resep berhasil ditambahkan!");

            input.close();

        } catch (Exception e) {

            System.out.println("Gagal insert data resep!");
            e.printStackTrace();

        }
    }
}