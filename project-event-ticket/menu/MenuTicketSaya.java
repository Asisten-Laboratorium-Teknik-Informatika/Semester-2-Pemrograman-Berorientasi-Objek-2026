package menu;

import java.util.Scanner;

import service.PaymentService;
import session.LoginSession;
import util.Loading;
import util.Tampilan;

public class MenuTicketSaya {

    Scanner input =
            new Scanner(System.in);

    PaymentService service =
            new PaymentService();

    public void tampilTicket() {

        while (true) {

            Tampilan.judul("TICKET SAYA");

            System.out.println(
                    "1. Lihat Ticket"
            );

            Tampilan.sukses(
        "Ticket berhasil dicetak"
);

            System.out.println(
                    "0. Kembali"
            );

            System.out.print(
                    "\nPilih menu : "
            );

            int pilih =
                    input.nextInt();

            input.nextLine();

            switch (pilih) {

                case 1 -> {

                    service.tampilTicketSaya(
                            LoginSession.getUserId()
                    );
                }

                case 2 -> {
                        Loading.tampil(
        "Mencetak tiket ke file TXT"
);
                    service.cetakTicketSaya(
                            LoginSession.getUserId()
                    );
                }

                case 0 -> {

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