import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args)
        throws Exception {

        Scanner input =
            new Scanner(System.in);

        User u =
            new User();

        while (true) {

            // ======================================================
            // MAIN MENU
            // ======================================================

            if (
                User.currentUsername.equals("")
            ) {

                System.out.println(UI.CYAN);

                System.out.println(
                    "\n======================================================"
                );

                System.out.println(
                    "                     BIOSKOP M-TIX"
                );

                System.out.println(
                    "======================================================"
                );

                System.out.println(UI.RESET);

                System.out.println(
                    UI.YELLOW +
                    "\n          PREMIUM CINEMA BOOKING SYSTEM"
                    + UI.RESET
                );

                System.out.println(
                    "\n======================================================"
                );

                System.out.println(
                    "      IMAX | DOLBY ATMOS | SWEETBOX | PREMIERE"
                );

                System.out.println(
                    "======================================================"
                );

                System.out.println(UI.CYAN);

                System.out.println(
                    "\n======================================================"
                );

                System.out.println(
                    "                      MAIN MENU"
                );

                System.out.println(
                    "======================================================"
                );

                System.out.println(UI.RESET);

                System.out.println(
                    "\n1. Register Account"
                );

                System.out.println(
                    "2. Login Account"
                );

                System.out.println(
                    "3. Exit"
                );

                System.out.print(
                    "\nPilih menu : "
                );

                int p =
                    input.nextInt();

                input.nextLine();

                // ======================================================
                // REGISTER
                // ======================================================

                if (p == 1) {

                    System.out.print(
                        "\nMembuka menu register"
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

                    u.register();
                }

                // ======================================================
                // LOGIN
                // ======================================================

                else if (p == 2) {

                    System.out.print(
                        "\nMembuka menu login"
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

                    u.login();
                }

                // ======================================================
                // EXIT
                // ======================================================

                else if (p == 3) {

                    System.out.print(
                        "\nMenutup aplikasi"
                    );

                    for (
                        int i = 0;
                        i < 6;
                        i++
                    ) {

                        Thread.sleep(300);

                        System.out.print(".");
                    }

                    System.out.println(
                        UI.GREEN
                    );

                    System.out.println(
                        "\n\n======================================================"
                    );

                    System.out.println(
                        "      TERIMA KASIH TELAH MENGGUNAKAN"
                    );

                    System.out.println(
                        "              BIOSKOP M-TIX"
                    );

                    System.out.println(
                        "======================================================"
                    );

                    System.out.println(
                        UI.RESET
                    );

                    Thread.sleep(1200);

                    System.exit(0);
                }

                else {

                    UI.error(
                        "Menu tidak tersedia!"
                    );
                }
            }

            // ======================================================
            // DASHBOARD ADMIN
            // ======================================================

            else if (User.isAdmin()) {

                // ======================================================
                // LOADING ADMIN
                // ======================================================

                System.out.print(
                    "\nMembuka dashboard admin"
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
                // TAMPIL DASHBOARD ADMIN
                // ======================================================

                System.out.println(UI.RED);

                System.out.println(
                    "\n======================================================"
                );

                System.out.println(
                    "                    ADMIN DASHBOARD"
                );

                System.out.println(
                    "======================================================"
                );

                System.out.println(UI.RESET);

                System.out.println(
                    "\nLogin sebagai : "
                    + User.currentUsername
                );

                System.out.println(
                    "\n1. Dashboard Admin"
                );

                System.out.println(
                    "2. Lihat Daftar Film"
                );

                System.out.println(
                    "3. Logout"
                );

                System.out.println(
                "4. Atur Film Tayang"
                );

                System.out.print(
                    "\nPilih menu : "
                );

                int admin =
                    input.nextInt();

                input.nextLine();

                // ======================================================
                // MENU ADMIN
                // ======================================================

                if (admin == 1) {

                    Admin a =
                        new Admin();

                    a.menu();
                }

                // ======================================================
                // LIHAT FILM ADMIN
                // ======================================================

                else if (admin == 2) {

                    System.out.print(
                        "\nMembuka daftar film"
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

                    Film f =
                        new Film();

                    f.menuDaftarFilm();
                }

                // ======================================================
                // LOGOUT
                // ======================================================

                else if (admin == 3) {

                    System.out.print(
                        "\nLogout akun"
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

                    u.logout();
                }

                // ======================================================
                // ATUR FILM TAYANG
                // ======================================================

                else if (admin == 4) {

                    System.out.print(
                        "\nMembuka menu atur film"
                    );

                    for (int i = 0; i < 6; i++) {
                        Thread.sleep(300);
                        System.out.print(".");
                    }

                    System.out.println();

                    Film f = new Film();
                    f.aturFilmTayang();
                }

                else {

                    UI.error(
                        "Menu tidak tersedia!"
                    );
                }
            }

            // ======================================================
            // DASHBOARD USER
            // ======================================================

            else {

                // ======================================================
                // LOADING USER
                // ======================================================

                System.out.print(
                    "\nMembuka dashboard user"
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
                // TAMPIL DASHBOARD USER
                // ======================================================

                int poinUser = 0;
                try {
                    Connection c = Koneksi.connect();
                    PreparedStatement ps = c.prepareStatement("SELECT poin FROM users WHERE id_user=?");
                    ps.setInt(1, User.currentUserId);
                    ResultSet rs = ps.executeQuery();
                    if(rs.next()) {
                        poinUser = rs.getInt("poin");
                    }
                } catch (Exception e) {}

                System.out.println(UI.GREEN);

                System.out.println(
                    "\n======================================================"
                );

                System.out.println(
                    "                     USER DASHBOARD"
                );

                System.out.println(
                    "======================================================"
                );

                System.out.println(UI.RESET);

                System.out.println(
                    "\nLogin sebagai : "
                    + User.currentUsername
                );

                System.out.println(
                    "Poin Member   : " + poinUser + " Pts"
                );

                System.out.println(
                    "\n1. Lihat Daftar Film"
                );

                System.out.println(
                    "2. Booking Tiket"
                );

                System.out.println(
                    "3. Beli Snack"
                );

                System.out.println(
                    "4. Logout"
                );

                System.out.println(
                    "5. Riwayat Pembelian"
                );

                System.out.print(
                    "\nPilih menu : "
                );

                int user =
                    input.nextInt();

                input.nextLine();

                // ======================================================
                // LIHAT FILM
                // ======================================================

                if (user == 1) {

                    System.out.print(
                        "\nMembuka daftar film"
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

                    Film f =
                        new Film();

                    f.menuDaftarFilm();
                }

                // ======================================================
                // BOOKING
                // ======================================================

                else if (user == 2) {

                    System.out.print(
                        "\nMembuka studio bioskop"
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

                    TiketBioskop t =
                        new TiketBioskop();

                    t.booking();
                }

                // ======================================================
                // SNACK
                // =====================================================

                else if (user == 3) {

                    System.out.print(
                        "\nMembuka menu snack"
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

                    Snack s =
                        new Snack();

                    s.beliSnack();
                }

                // ======================================================
                // LOGOUT
                // ======================================================

                else if (user == 4) {

                    System.out.print(
                        "\nLogout akun"
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

                    u.logout();
                }

                // ======================================================
                // RIWAYAT PEMBELIAN
                // ======================================================

                else if (user == 5) {

                    System.out.print(
                        "\nMengambil data riwayat"
                    );

                    for (int i = 0; i < 6; i++) {
                        Thread.sleep(300);
                        System.out.print(".");
                    }

                    System.out.println();

                    TiketBioskop t = new TiketBioskop();
                    t.riwayatTiket();

                    Snack s = new Snack();
                    s.riwayatSnack();
                }

                else {

                    UI.error(
                        "Menu tidak tersedia!"
                    );
                }
            }
        }
    }
}