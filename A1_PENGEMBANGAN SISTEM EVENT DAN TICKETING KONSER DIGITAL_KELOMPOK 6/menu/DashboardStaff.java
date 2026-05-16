package menu;

import java.util.Scanner;

import service.CheckinService;
import session.LoginSession;
import util.Tampilan;

public class DashboardStaff {

    Scanner input = new Scanner(System.in);

    public void tampilDashboard() {

        while (true) {

            Tampilan.judul("DASHBOARD STAFF");

            System.out.println(
                    "Login : "
                    + LoginSession.getNama()
            );

            System.out.println(
                    "\n1. Lihat Data Ticket"
            );

            System.out.println(
                    "2. Check-In Ticket"
            );

            System.out.println(
                    "0. Logout"
            );

            System.out.print(
                    "\nPilih menu : "
            );

            int pilih =
                    input.nextInt();

            input.nextLine();

            switch (pilih) {

                case 1 -> {

                    CheckinService service =
                            new CheckinService();

                    service.tampilSemuaTicket();
                }

                case 2 -> {

                    MenuCheckin menu =
                            new MenuCheckin();

                    menu.tampilMenu();
                }

                case 0 -> {

                    LoginSession.logout();

                    System.out.println(
                            "\nLogout berhasil!"
                    );

                    return;
                }

                default -> {

                    System.out.println(
                            "\nMenu tidak tersedia!"
                    );
                }
            }
        }
    }
}