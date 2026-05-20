import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.util.Scanner;

public class InsertPendaftaran {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.print("ID Pasien : ");
        int idPasien = input.nextInt();

        System.out.print("ID Klinik : ");
        int idKlinik = input.nextInt();
        input.nextLine();

        System.out.print("Tanggal Daftar (yyyy-mm-dd) : ");
        String tanggal = input.nextLine();

        System.out.print("Status Pendaftaran : ");
        String status = input.nextLine();

        System.out.print("Keluhan Awal : ");
        String keluhan = input.nextLine();
        try {

            Connection conn = Koneksi.connect();

            Date tanggalSql = Date.valueOf(tanggal);

            String sql = "INSERT INTO sbd.pendaftaran(id_pasien, id_klinik, tanggal_daftar, status_pendaftaran, keluhan_awal) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, idPasien);
            ps.setInt(2, idKlinik);
            ps.setDate(3, tanggalSql);
            ps.setString(4, status);
            ps.setString(5, keluhan);

            ps.executeUpdate();

            System.out.println("Data pendaftaran berhasil ditambahkan!");

            input.close();

        } catch (Exception e) {

            System.out.println("Gagal insert data pendaftaran!");
            e.printStackTrace();

        }
    }
}