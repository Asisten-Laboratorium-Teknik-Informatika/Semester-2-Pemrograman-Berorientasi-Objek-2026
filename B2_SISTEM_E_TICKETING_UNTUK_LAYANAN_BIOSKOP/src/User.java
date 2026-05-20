import java.sql.*;
import java.util.Scanner;

public class User {

    // ======================================================
    // SESSION LOGIN
    // ======================================================

    public static String currentUsername = "";

    public static boolean adminLogin = false;

    public static int currentUserId = 0;

    Scanner input =
        new Scanner(System.in);

    // ======================================================
    // REGISTER USER
    // ======================================================

    public void register() {

        try {

            Connection c =
                Koneksi.connect();

            // ======================================================
            // HEADER REGISTER
            // ======================================================

            System.out.println(UI.CYAN);

            System.out.println(
                "\n======================================================"
            );

            System.out.println(
                "                  REGISTER ACCOUNT"
            );

            System.out.println(
                "======================================================"
            );

            System.out.println(UI.RESET);

            // ======================================================
            // INPUT USERNAME
            // ======================================================

            System.out.print(
                "\nUsername : "
            );

            String username =
                input.nextLine();

            // ======================================================
            // INPUT PASSWORD
            // ======================================================

            System.out.print(
                "Password : "
            );

            String password =
                PasswordMask.inputPassword(input);

            // ======================================================
            // VALIDASI KOSONG
            // ======================================================

            if (
                username.isEmpty()
                ||
                password.isEmpty()
            ) {

                UI.error(
                    "Username dan password tidak boleh kosong!"
                );

                return;
            }

            // ======================================================
            // CEK USERNAME USER
            // ======================================================

            PreparedStatement cekUser =
                c.prepareStatement(
                    "SELECT * FROM users WHERE username=?"
                );

            cekUser.setString(
                1,
                username
            );

            ResultSet rsUser =
                cekUser.executeQuery();

            if (rsUser.next()) {

                UI.error(
                    "Username sudah digunakan!"
                );

                return;
            }

            // ======================================================
            // CEK USERNAME ADMIN
            // ======================================================

            PreparedStatement cekAdmin =
                c.prepareStatement(
                    "SELECT * FROM admin_bioskop WHERE username=?"
                );

            cekAdmin.setString(
                1,
                username
            );

            ResultSet rsAdmin =
                cekAdmin.executeQuery();

            if (rsAdmin.next()) {

                UI.error(
                    "Username sudah digunakan admin!"
                );

                return;
            }

            // ======================================================
            // LOADING REGISTER
            // ======================================================

            System.out.print(
                "\nMembuat akun"
            );

            for (
                int i = 0;
                i < 6;
                i++
            ) {

                Thread.sleep(300);

                System.out.print(".");
            }

            System.out.println();

            // ======================================================
            // INSERT USER
            // ======================================================

            PreparedStatement ps =
                c.prepareStatement(
                    "INSERT INTO users(username,password) VALUES(?,?)"
                );

            ps.setString(
                1,
                username
            );

            ps.setString(
                2,
                password
            );

            ps.executeUpdate();

            // ======================================================
            // SUCCESS
            // ======================================================

            System.out.println(
                UI.GREEN +
                "\nAkun berhasil dibuat!"
                + UI.RESET
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // ======================================================
    // LOGIN
    // ======================================================

    public boolean login() {

        try {

            Connection c =
                Koneksi.connect();

            // ======================================================
            // HEADER LOGIN
            // ======================================================

            System.out.println(UI.CYAN);

            System.out.println(
                "\n======================================================"
            );

            System.out.println(
                "                      LOGIN USER"
            );

            System.out.println(
                "======================================================"
            );

            System.out.println(UI.RESET);

            // ======================================================
            // INPUT USERNAME
            // ======================================================

            System.out.print(
                "\nUsername : "
            );

            String username =
                input.nextLine();

            // ======================================================
            // INPUT PASSWORD
            // ======================================================

            System.out.print(
                "Password : "
            );

            String password =
                PasswordMask.inputPassword(input);

            // ======================================================
            // LOADING LOGIN
            // ======================================================

            System.out.print(
                "\nMemproses login"
            );

            for (
                int i = 0;
                i < 6;
                i++
            ) {

                Thread.sleep(300);

                System.out.print(".");
            }

            System.out.println();

            // ======================================================
            // LOGIN ADMIN
            // ======================================================

            PreparedStatement admin =
                c.prepareStatement(
                    "SELECT * FROM admin_bioskop WHERE username=? AND password=?"
                );

            admin.setString(
                1,
                username
            );

            admin.setString(
                2,
                password
            );

            ResultSet adminRs =
                admin.executeQuery();

            if (adminRs.next()) {

                currentUsername =
                    username;

                adminLogin = true;

                currentUserId = adminRs.getInt("id_admin");

                System.out.println(
                    UI.GREEN +
                    "\nLogin Admin berhasil!"
                    + UI.RESET
                );

                return true;
            }

            // ======================================================
            // LOGIN USER
            // ======================================================

            PreparedStatement user =
                c.prepareStatement(
                    "SELECT * FROM users WHERE username=? AND password=?"
                );

            user.setString(
                1,
                username
            );

            user.setString(
                2,
                password
            );

            ResultSet userRs =
                user.executeQuery();

            if (userRs.next()) {

                currentUsername =
                    username;

                adminLogin = false;

                currentUserId = userRs.getInt("id_user");

                System.out.println(
                    UI.GREEN +
                    "\nLogin berhasil!"
                    + UI.RESET
                );

                return true;
            }

            // ======================================================
            // LOGIN GAGAL
            // ======================================================

            else {

                UI.error(
                    "Username atau password salah!"
                );

                return false;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    // ======================================================
    // LOGOUT
    // ======================================================

    public void logout() {

        currentUsername = "";

        adminLogin = false;

        currentUserId = 0;

        System.out.println(
            UI.YELLOW +
            "\nLogout berhasil!"
            + UI.RESET
        );
    }

    // ======================================================
    // CEK ADMIN
    // ======================================================

    public static boolean isAdmin() {

        return adminLogin;
    }
}