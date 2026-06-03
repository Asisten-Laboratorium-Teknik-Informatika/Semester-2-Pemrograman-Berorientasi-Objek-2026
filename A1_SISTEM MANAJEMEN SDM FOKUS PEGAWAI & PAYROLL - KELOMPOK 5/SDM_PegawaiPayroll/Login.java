package SDM_PegawaiPayroll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Login {

    Scanner input = new Scanner(System.in);

    public void login() {

        System.out.println(Warna.CYAN + Warna.BOLD + "\n===== LOGIN =====" + Warna.RESET);

        System.out.print(Warna.KUNING + "Username : " + Warna.RESET);
        String username = input.nextLine();

        System.out.print(Warna.KUNING + "Password : " + Warna.RESET);
        String password = input.nextLine();

        try {

            Connection conn = KoneksiDatabase.connect();

            String sql =
                    "SELECT id_user,id_pegawai,username,role "
                  + "FROM project.users "
                  + "WHERE username=? "
                  + "AND user_password=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {

                int idPegawai = rs.getInt("id_pegawai");

                String role = rs.getString("role");

                System.out.println(Warna.HIJAU + "\nLogin Berhasil!" + Warna.RESET);

                System.out.println("Role : " + role);

                if(role.equalsIgnoreCase("admin")) {

                    AdminMenu admin = new AdminMenu();
                    admin.menuAdmin();

                }

                else if(
                        role.equalsIgnoreCase("pegawai")) {

                    PegawaiMenu pegawai = new PegawaiMenu();

                    pegawai.menuPegawai(idPegawai);
                }

            }

            else {

                System.out.println(Warna.MERAH + "\nUsername / Password salah!" + Warna.RESET);
            }

        }

        catch(SQLException e) {

            System.out.println(Warna.MERAH + "Error Login!" + Warna.RESET);

            System.out.println(e.getMessage());

        }

    }

}
