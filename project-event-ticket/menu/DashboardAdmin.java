package menu;

import java.util.Scanner;

import session.LoginSession;
import util.Loading;
import util.Tampilan;

public class DashboardAdmin {

    Scanner input =
            new Scanner(System.in);

    public void tampilDashboard() {

        Loading.tampil(
                "Membuka dashboard admin"
        );

        while (true) {

            Tampilan.judul(
                    "DASHBOARD ADMIN"
            );

            System.out.println(
                    "Selamat datang "
                    + LoginSession.getNama()
            );

            System.out.println("\n1. Lihat Semua Event");
            System.out.println("2. Kelola Refund");
            System.out.println("3. Monitoring Payment");
            System.out.println("4. Data User");
            System.out.println("5. Data Organizer");
            System.out.println("0. Logout");

            System.out.print(
                    "\nPilih menu : "
            );

            int pilih =
                    input.nextInt();

            input.nextLine();

            switch (pilih) {

                case 1 -> new MenuLihatEvent()
                        .tampilEvent();

                case 2 -> new MenuKelolaRefund()
                        .tampilMenu();

                case 3 -> new MenuMonitoringPayment()
                        .tampilMenu();

                case 4 -> new MenuDataUser()
                        .tampilMenu();

                case 5 -> new MenuDataOrganizer()
                        .tampilMenu();

                case 0 -> {

                    Loading.tampil(
                            "Logout admin"
                    );

                    LoginSession.logout();

                    Tampilan.sukses(
                            "Logout berhasil"
                    );

                    return;
                }

                default -> Tampilan.gagal(
                        "Menu tidak tersedia"
                );
            }
        }
    }
}