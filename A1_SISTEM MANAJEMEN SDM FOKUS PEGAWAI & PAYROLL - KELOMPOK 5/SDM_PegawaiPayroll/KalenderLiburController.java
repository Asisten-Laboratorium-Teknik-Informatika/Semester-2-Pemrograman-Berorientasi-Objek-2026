package SDM_PegawaiPayroll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class KalenderLiburController {

    Connection conn;

    Scanner input = new Scanner(System.in);

    public KalenderLiburController() {

        conn = KoneksiDatabase.connect();

    }

    public void menu() {

        int pilih;

        do {

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== MENU KALENDER LIBUR =====" + Warna.RESET);
            System.out.println(Warna.BIRU + "1. Tambah Libur" + Warna.RESET);
            System.out.println(Warna.BIRU + "2. Tampil Libur" + Warna.RESET);
            System.out.println(Warna.BIRU + "3. Update Libur" + Warna.RESET);
            System.out.println(Warna.BIRU + "4. Hapus Libur" + Warna.RESET);
            System.out.println(Warna.MERAH + "0. Kembali" + Warna.RESET);
            System.out.print(Warna.KUNING + "Pilih Menu : " + Warna.RESET);

            pilih = Integer.parseInt(input.nextLine());

            switch(pilih) {

                case 1:

                    tambahLibur();
                    break;

                case 2:

                    tampilLibur();
                    break;

                case 3:

                    updateLibur();
                    break;

                case 4:

                    hapusLibur();
                    break;

                case 0:

                    System.out.println(Warna.MERAH + "Kembali" + Warna.RESET);

                    break;

                default:

                    System.out.println(Warna.MERAH + "Menu tidak tersedia!" + Warna.RESET);

            }

        }

        while(pilih != 0);

    }

    // TAMBAH
    public void tambahLibur() {

        try {

            System.out.print(Warna.PINK + "Tanggal Libur (YYYY-MM-DD) : " + Warna.RESET);
            String tanggal = input.nextLine();

            System.out.print(Warna.PINK + "Nama Libur : " + Warna.RESET);
            String nama = input.nextLine();

            System.out.print(Warna.PINK + "Jenis Libur : " + Warna.RESET);
            String jenis = input.nextLine();

            String sql =

            "insert into "
            + "project.kalender_libur "
            + "(tanggal_libur,"
            + "nama_libur,"
            + "jenis_libur) "
            + "values(?,?,?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setDate(1,java.sql.Date.valueOf(tanggal));
            ps.setString(2,nama);
            ps.setString(3,jenis);
            ps.executeUpdate();

            System.out.println(Warna.MERAH +"Data libur berhasil ditambah!" + Warna.RESET);

        } catch(Exception e) {

            System.out.println(e);

        }

    }

    // TAMPIL
    public void tampilLibur() {

        try {

            String sql =

            "select * from project.kalender_libur "
            + "order by id_libur";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== KALENDER LIBUR =====" + Warna.RESET);

            while(rs.next()) {

                System.out.println(Warna.BIRU +
                        rs.getInt("id_libur")
                        + " | "
                        + rs.getDate("tanggal_libur")
                        + " | "
                        + rs.getString("nama_libur")
                        + " | "
                        + rs.getString("jenis_libur") + Warna.RESET
                );

            }

        } catch(Exception e) {

            System.out.println(e);

        }

    }

    // UPDATE
    public void updateLibur() {

        try {

            System.out.print(Warna.PINK +"ID Kalender Libur : " + Warna.RESET);
            int idLibur = Integer.parseInt(input.nextLine());

            System.out.print(Warna.PINK +"Nama Libur Baru : " + Warna.RESET);
            String nama = input.nextLine();

            String sql =

            "update "
            + "project.kalender_libur "
            + "set nama_libur = ? "
            + "where id_libur = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1,nama);
            ps.setInt(2,idLibur);
            ps.executeUpdate();

            System.out.println(Warna.HIJAU + "Data libur berhasil diupdate!" + Warna.RESET);

        } catch(Exception e) {

            System.out.println(e);

        }

    }

    // HAPUS
    public void hapusLibur() {

        try {

            System.out.print(Warna.PINK + "ID Kalender Libur : " + Warna.RESET);

            int idLibur =Integer.parseInt(input.nextLine());

            String sql =

            "delete from "
            + "project.kalender_libur "
            + "where id_libur=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,idLibur);
            ps.executeUpdate();

            System.out.println(Warna.MERAH + "Data libur berhasil dihapus!" + Warna.RESET);

        } catch(Exception e) {

            System.out.println(e);

        }

    }

}
