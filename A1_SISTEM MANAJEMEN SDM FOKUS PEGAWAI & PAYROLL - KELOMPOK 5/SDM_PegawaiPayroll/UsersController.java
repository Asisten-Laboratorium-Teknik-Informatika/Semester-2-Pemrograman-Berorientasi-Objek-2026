package SDM_PegawaiPayroll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class UsersController {

    Connection conn;

    Scanner input = new Scanner(System.in);

    public UsersController() {

        conn = KoneksiDatabase.connect();

    }

    public void menu() {

        int pilih;

        do {

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== MENU USERS =====" + Warna.RESET);
            System.out.println(Warna.BIRU + "1. Tambah Users" + Warna.RESET);
            System.out.println(Warna.BIRU + "2. Tampil Users" + Warna.RESET);
            System.out.println(Warna.BIRU + "3. Update Users" + Warna.RESET);
            System.out.println(Warna.BIRU + "4. Hapus Users" + Warna.RESET);
            System.out.println(Warna.MERAH + "0. Kembali" + Warna.RESET);

            System.out.print(Warna.KUNING + "Pilih Menu : " + Warna.RESET);
            pilih = Integer.parseInt(input.nextLine());

            switch (pilih) {

                case 1:

                    tambahUsers();
                    break;

                case 2:

                    tampilUsers();
                    break;

                case 3:

                    updateUsers();
                    break;

                case 4:

                    hapusUsers();
                    break;

                case 0:

                    System.out.println(Warna.MERAH + "Kembali" + Warna.RESET);
                    break;

                default:

                    System.out.println(Warna.MERAH + "Menu tidak tersedia!" + Warna.RESET);

            }

        }

        while (pilih != 0);

    }

    // TAMBAH
    public void tambahUsers() {

        try {

            System.out.print(Warna.PINK + "ID Pegawai : "  + Warna.RESET);
            int idPegawai = Integer.parseInt(input.nextLine());

            System.out.print(Warna.PINK + "Username : "  + Warna.RESET);
            String username = input.nextLine();

            System.out.print(Warna.PINK + "Password : "  + Warna.RESET);
            String password = input.nextLine();

            System.out.print(Warna.PINK + "Role(admin/pegawai) : "  + Warna.RESET);
            String role =input.nextLine();

            String sql =
                    "insert into project.users "
                    + "(id_pegawai,"
                    + "username,user_password,role) "
                    + "values(?,?,?,?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, idPegawai);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setString(4, role);

            ps.executeUpdate();

            System.out.println(Warna.HIJAU + "Users berhasil ditambah!"  + Warna.RESET);

        } catch (Exception e) {

            System.out.println(e);

        }

    }

    // TAMPIL
    public void tampilUsers() {

        try {

            String sql =
                    "select * from project.users";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println(Warna.CYAN + Warna.BOLD + "\n========= DATA USERS ========="  + Warna.RESET);

            while (rs.next()) {

                System.out.println(Warna.BIRU +
                        rs.getString("id_user")
                        + " | "
                        + rs.getInt("id_pegawai")
                        + " | "
                        + rs.getString("username")
                        + " | "
                        + rs.getString(
                                "user_password")
                        + " | "
                        + rs.getString("role") + Warna.RESET
                );

            }

        } catch (Exception e) {

            System.out.println(e);

        }

    }

    // UPDATE

    public void updateUsers() {

        try {

            System.out.print(Warna.PINK + "Masukkan ID User : "  + Warna.RESET);
            String idUser =input.nextLine();

            System.out.print(Warna.PINK + "Username Baru : "  + Warna.RESET);
            String username = input.nextLine();

            System.out.print(Warna.PINK + "Password Baru : "  + Warna.RESET);
            String password =input.nextLine();

            System.out.print(Warna.PINK + "Role Baru : " + Warna.RESET);
            String role = input.nextLine();

            String sql =
                    "update project.users set "
                    + "username=?, "
                    + "user_password=?, "
                    + "role=? "
                    + "where id_user=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);
            ps.setString(4, idUser);
            ps.executeUpdate();

            System.out.println(Warna.HIJAU + "Users berhasil diupdate!"  + Warna.RESET);

        } catch (Exception e) {

            System.out.println(e);

        }

    }

    // HAPUS
    public void hapusUsers() {

        try {

            System.out.print(Warna.PINK + "Masukkan ID User : "  + Warna.RESET);

            String idUser = input.nextLine();

            String sql =
                    "delete from project.users "
                    + "where id_user=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, idUser);
            ps.executeUpdate();

            System.out.println(Warna.MERAH + "Users berhasil dihapus!"  + Warna.RESET);

        } catch (Exception e) {

            System.out.println(e);

        }

    }

}
