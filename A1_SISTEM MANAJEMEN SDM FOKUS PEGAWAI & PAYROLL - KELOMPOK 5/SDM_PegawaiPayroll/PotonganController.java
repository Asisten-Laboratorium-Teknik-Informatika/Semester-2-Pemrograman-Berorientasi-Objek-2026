package SDM_PegawaiPayroll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class PotonganController {

    Connection conn;

    Scanner input = new Scanner(System.in);

    public PotonganController() {

        conn = KoneksiDatabase.connect();

    }

    public void menu() {

        int pilih;

        do {

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== MENU POTONGAN =====" + Warna.RESET);
            System.out.println(Warna.BIRU + "1. Tambah Potongan" + Warna.RESET);
            System.out.println(Warna.BIRU + "2. Tampil Potongan" + Warna.RESET);
            System.out.println(Warna.BIRU + "3. Update Potongan" + Warna.RESET);
            System.out.println(Warna.BIRU + "4. Hapus Potongan" + Warna.RESET);
            System.out.println(Warna.MERAH + "0. Kembali" + Warna.RESET);
            System.out.print(Warna.KUNING + "Pilih Menu : " + Warna.RESET);

            pilih = Integer.parseInt(input.nextLine());

            switch(pilih) {

                case 1:

                    tambahPotongan();
                    break;

                case 2:

                    tampilPotongan();
                    break;

                case 3:

                    updatePotongan();
                    break;

                case 4:

                    hapusPotongan();
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
    public void tambahPotongan() {

        try {

            System.out.print(Warna.PINK + "Nama Potongan : " + Warna.RESET);
            String nama = input.nextLine();

            System.out.print(Warna.PINK + "Jumlah Potongan : " + Warna.RESET);
            double jumlah = Double.parseDouble(input.nextLine());

            String sql =

            "insert into "
            + "project.potongan "
            + "(nama_potongan,"
            + "jumlah) "
            + "values(?,?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1,nama);
            ps.setDouble(2,jumlah);
            ps.executeUpdate();

            System.out.println(Warna.HIJAU + "Potongan berhasil ditambah!" + Warna.RESET);

        } catch(Exception e) {

            System.out.println(e);

        }

    }

    // TAMPIL
    public void tampilPotongan() {

        try {

            String sql =

            "select * "
            + "from project.potongan "
            + "order by id_potongan";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== DATA POTONGAN =====" + Warna.RESET);

            while(rs.next()) {

                System.out.println(Warna.BIRU +
                        rs.getInt("id_potongan")
                        + " | "
                        + rs.getString("nama_potongan")
                        + " | Rp."
                        + String.format("%.0f,.", rs.getDouble("jumlah")) + Warna.RESET
                );

            }

        } catch(Exception e) {

            System.out.println(e);

        }

    }

    // UPDATE
    public void updatePotongan() {

        try {

            System.out.print(Warna.PINK + "ID Potongan : " + Warna.RESET);
            int idPotongan = Integer.parseInt(input.nextLine());

            System.out.print(Warna.PINK + "Nama Potongan Baru : " + Warna.RESET);
            String nama = input.nextLine();

            System.out.print(Warna.PINK + "Jumlah Baru : " + Warna.RESET);
            double jumlah = Double.parseDouble(input.nextLine());

            String sql =

            "update "
            + "project.potongan "
            + "set nama_potongan=?, "
            + "jumlah=? "
            + "where id_potongan=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1,nama);
            ps.setDouble(2,jumlah);
            ps.setInt(3,idPotongan);

            ps.executeUpdate();

            System.out.println(Warna.HIJAU + "Potongan berhasil diupdate!" + Warna.RESET);

        } catch(Exception e) {

            System.out.println(e);

        }

    }

    // HAPUS
    public void hapusPotongan() {

        try {

            System.out.print(Warna.PINK + "ID Potongan : " + Warna.RESET);

            int idPotongan = Integer.parseInt(input.nextLine());

            String sql =

            "delete from "
            + "project.potongan "
            + "where id_potongan=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,idPotongan);
            ps.executeUpdate();

            System.out.println(Warna.MERAH + "Potongan berhasil dihapus!" + Warna.RESET);

        } catch(Exception e) {

            System.out.println(e);

        }

    }

}
