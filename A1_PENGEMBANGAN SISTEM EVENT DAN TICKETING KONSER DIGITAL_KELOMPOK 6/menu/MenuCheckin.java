package menu;

import java.util.Scanner;

import service.CheckinService;
import session.LoginSession;
import util.Tampilan;

public class MenuCheckin {

    Scanner input = new Scanner(System.in);

    CheckinService service =
            new CheckinService();

    public void tampilMenu() {

        Tampilan.judul("MENU CHECKIN");

        System.out.print(
                "Masukkan Ticket Code : "
        );

        String kode =
                input.nextLine();

        Tampilan.judul("METODE CHECKIN");

        System.out.println(
                "1. QR Scan"
        );

        System.out.println(
                "2. Manual"
        );

        System.out.println(
                "3. NFC"
        );

        System.out.print(
                "\nPilih metode : "
        );

        int pilih =
                input.nextInt();

        String metode = "";

        switch (pilih) {

            case 1 -> metode =
                    "qr_scan";

            case 2 -> metode =
                    "manual";

            case 3 -> metode =
                    "nfc";

            default -> {

                System.out.println(
                        "\nMetode tidak tersedia!"
                );

                return;
            }
        }

        service.prosesCheckin(

                LoginSession.getUserId(),

                kode,

                metode
        );
    }
}