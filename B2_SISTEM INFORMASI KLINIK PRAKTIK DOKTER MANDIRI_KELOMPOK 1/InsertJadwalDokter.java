import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Time;
import java.util.Scanner;

public class InsertJadwalDokter {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.print("Hari :  ");
        String hari = input.nextLine();

        System.out.print("Jam Mulai (HH:MM:SS) : ");
        Time jamMulai = Time.valueOf(input.nextLine());

        System.out.print("Jam Selesai (HH:MM:SS) : ");
        Time jamSelesai = Time.valueOf(input.nextLine());

        System.out.print("ID Dokter : ");
        int idDokter = input.nextInt();

        System.out.print("ID Klinik : ");
        int idKlinik = input.nextInt();
        try {

            Connection conn = Koneksi.connect();

            String sql = "INSERT INTO sbd.jadwal_dokter(hari_praktek, jam_mulai, jam_selesai, id_dokter, id_klinik) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, hari);
            ps.setTime(2, jamMulai);
            ps.setTime(3, jamSelesai);
            ps.setInt(4, idDokter);
            ps.setInt(5, idKlinik);

            ps.executeUpdate();

            System.out.println("Data jadwal dokter berhasil ditambahkan!");

            input.close();

        } catch (Exception e) {

            System.out.println("Gagal insert jadwal dokter!");
            e.printStackTrace();

        }
    }
}