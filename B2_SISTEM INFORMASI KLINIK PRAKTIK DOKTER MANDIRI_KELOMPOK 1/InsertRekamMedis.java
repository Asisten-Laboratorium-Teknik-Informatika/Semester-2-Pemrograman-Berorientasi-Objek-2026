import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.util.Scanner;

public class InsertRekamMedis {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.print("ID Pendaftaran : ");
        int idPendaftaran = input.nextInt();

        System.out.print("ID Pasien : ");
        int idPasien = input.nextInt();

        System.out.print("ID Dokter : ");
        int idDokter = input.nextInt();
        input.nextLine();

        System.out.print("Tanggal Periksa (yyyy-mm-dd) : ");
        String tanggal = input.nextLine();

        System.out.print("Diagnosa : ");
        String diagnosa = input.nextLine();

        System.out.print("Tindakan : ");
        String tindakan = input.nextLine();

        System.out.print("Catatan Dokter : ");
        String catatan = input.nextLine();
        try {

            Connection conn = Koneksi.connect();

            Date tanggalSql = Date.valueOf(tanggal);

            String sql = "INSERT INTO sbd.rekam_medis(id_pendaftaran, id_pasien, id_dokter, tanggal_periksa, diagnosa, tindakan, catatan_dokter) VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, idPendaftaran);
            ps.setInt(2, idPasien);
            ps.setInt(3, idDokter);
            ps.setDate(4, tanggalSql);
            ps.setString(5, diagnosa);
            ps.setString(6, tindakan);
            ps.setString(7, catatan);

            ps.executeUpdate();

            System.out.println("Data rekam medis berhasil ditambahkan!");

            input.close();

        } catch (Exception e) {

            System.out.println("Gagal insert data rekam medis!");
            e.printStackTrace();

        }
    }
}