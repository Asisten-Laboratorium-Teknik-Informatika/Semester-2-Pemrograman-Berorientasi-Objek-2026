package SDM_PegawaiPayroll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class TunjanganController {

    Connection conn;

    Scanner input = new Scanner(System.in);

    public TunjanganController() {

        conn = KoneksiDatabase.connect();

    }

    public void menu() {

        int pilih;

        do {

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== MENU TUNJANGAN =====" + Warna.RESET);
            System.out.println(Warna.BIRU + "1. Tambah Tunjangan" + Warna.RESET);
            System.out.println(Warna.BIRU + "2. Tampil Tunjangan" + Warna.RESET);
            System.out.println(Warna.BIRU + "3. Update Tunjangan" + Warna.RESET);
            System.out.println(Warna.BIRU + "4. Hapus Tunjangan" + Warna.RESET);
            System.out.println(Warna.MERAH + "0. Kembali" + Warna.RESET);
            System.out.print(Warna.KUNING + "Pilih Menu : " + Warna.RESET);

            pilih = Integer.parseInt(input.nextLine());

            switch(pilih) {

                case 1:

                    tambahTunjangan();
                    break;

                case 2:

                    tampilTunjangan();
                    break;

                case 3:

                    updateTunjangan();
                    break;

                case 4:

                    hapusTunjangan();
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
    public void tambahTunjangan() {

        try {

            System.out.print(Warna.PINK + "Nama Tunjangan : " + Warna.RESET);
            String nama = input.nextLine();

            System.out.print(Warna.PINK + "Jumlah : " + Warna.RESET);
            double jumlah = Double.parseDouble(input.nextLine());

            String sql =

            "insert into project.tunjangan "
            + "(nama_tunjangan,"
            + "jumlah) "
            + "values(?,?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1,nama);
            ps.setDouble(2,jumlah);
            ps.executeUpdate();

            System.out.println(Warna.HIJAU + "Tunjangan berhasil ditambah!" + Warna.RESET);

        } catch(Exception e) {

            System.out.println(e);

        }

    }

    // TAMPIL
    public void tampilTunjangan() {

        try {

            String sql =

            "select * from project.tunjangan "
            + "order by id_tunjangan";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== DATA TUNJANGAN =====" + Warna.RESET);

            while(rs.next()) {

                System.out.println(Warna.BIRU +
                        rs.getInt("id_tunjangan")
                        + " | "
                        + rs.getString("nama_tunjangan")
                        + " | Rp."
                        + String.format("%,.0f", rs.getDouble("jumlah"))+ Warna.RESET
                );

            }

        } catch(Exception e) {

            System.out.println(e);

        }

    }

    // UPDATE
    public void updateTunjangan() {

        try {

            System.out.print(Warna.PINK + "ID Tunjangan : " + Warna.RESET);
            int idTunjangan = Integer.parseInt(input.nextLine());

            System.out.print(Warna.PINK + "Nama Tunjangan Baru : " + Warna.RESET);
            String nama = input.nextLine();

            System.out.print(Warna.PINK + "Jumlah Baru : " + Warna.RESET);
            double jumlah = Double.parseDouble(input.nextLine());

            String sql =

            "update "
            + "project.tunjangan "
            + "set nama_tunjangan=?, "
            + "jumlah =? "
            + "where id_tunjangan=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1,nama);
            ps.setDouble(2,jumlah);
            ps.setInt(3,idTunjangan);
            ps.executeUpdate();

            System.out.println(Warna.HIJAU + "Tunjangan berhasil diupdate!" + Warna.RESET);

        } catch(Exception e) {

            System.out.println(e);

        }

    }

    // HAPUS
    public void hapusTunjangan() {

        try {

            System.out.print(Warna.PINK + "ID Tunjangan : " + Warna.RESET);

            int idTunjangan = Integer.parseInt(input.nextLine());

            String sql =

            "delete from "
            + "project.tunjangan "
            + "where id_tunjangan=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,idTunjangan);
            ps.executeUpdate();

            System.out.println(Warna.MERAH + "Tunjangan berhasil dihapus!" + Warna.RESET);

        } catch(Exception e) {

            System.out.println(e);

        }

    }

}
