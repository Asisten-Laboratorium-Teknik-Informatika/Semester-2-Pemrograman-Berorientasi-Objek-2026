package menu;

import java.util.Scanner;
import session.LoginSession;
import util.Tampilan;

public class MenuCustomer {

    private Scanner input =
            new Scanner(System.in);

    public void tampilMenuCustomer() {

        int pilih;

        do {

            System.out.println();
            Tampilan.judul("MENU CUSTOMER");

            System.out.println(
                    "Login sebagai : "
                    + LoginSession.getNama()
            );

            System.out.println("1. Lihat Event");
            System.out.println("2. Beli Tiket");
            System.out.println("3. Riwayat Pembelian");
            System.out.println("4. Refund Tiket");
            System.out.println("5. Logout");
            System.out.println("0. Keluar");

            System.out.print("Pilih menu : ");
            pilih = input.nextInt();
            input.nextLine();

            switch (pilih) {

                case 1:

                    System.out.println(
                            "Daftar event"
                    );
                    break;

                case 2:

                    System.out.println(
                            "Beli tiket"
                    );
                    break;

                case 3:

                    System.out.println(
                            "Riwayat pembelian"
                    );
                    break;

                case 4:

                    System.out.println(
                            "Refund tiket"
                    );
                    break;

                case 5:

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