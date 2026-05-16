package menu;

import java.util.Scanner;
import session.LoginSession;
import util.Tampilan;

public class MenuStaff {

    private Scanner input =
            new Scanner(System.in);

    public void tampilMenuStaff() {

        int pilih;

        do {

            System.out.println();
            Tampilan.judul("MENU STAFF");

            System.out.println(
                    "Login sebagai : "
                    + LoginSession.getNama()
            );

            System.out.println("1. Check In Tiket");
            System.out.println("2. Validasi Tiket");
            System.out.println("3. Logout");
            System.out.println("0. Keluar");

            System.out.print("Pilih menu : ");
            pilih = input.nextInt();
            input.nextLine();

            switch (pilih) {

                case 1:

                    System.out.println(
                            "Check in tiket"
                    );
                    break;

                case 2:

                    System.out.println(
                            "Validasi tiket"
                    );
                    break;

                case 3:

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