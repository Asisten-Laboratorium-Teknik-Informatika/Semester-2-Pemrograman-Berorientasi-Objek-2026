package menu;

import java.util.Scanner;
import session.LoginSession;
import util.Tampilan;

public class MenuAdmin {

    private Scanner input =
            new Scanner(System.in);

    public void tampilMenuAdmin() {

        int pilih;

        do {

            System.out.println();
            Tampilan.judul("MENU ADMIN");

            System.out.println(
                    "Login sebagai : "
                    + LoginSession.getNama()
            );

            System.out.println("1. Kelola Event");
            System.out.println("2. Kelola Sponsor");
            System.out.println("3. Monitoring Event");
            System.out.println("4. Logout");
            System.out.println("0. Keluar");

            System.out.print("Pilih menu : ");
            pilih = input.nextInt();
            input.nextLine();

            switch (pilih) {

                case 1:

                    System.out.println(
                            "Menu kelola event"
                    );
                    break;

                case 2:

                    System.out.println(
                            "Menu sponsor"
                    );
                    break;

                case 3:

                    System.out.println(
                            "Monitoring event"
                    );
                    break;

                case 4:

                    LoginSession.logout();

                    System.out.println(
                            "Logout berhasil"
                    );

                    return;

                case 0:

                    System.exit(0);
                    break;

                default:

                    System.out.println(
                            "Menu tidak tersedia"
                    );
            }

        } while (true);
    }
}