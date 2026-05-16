package menu;

import java.util.Scanner;

import repository.TicketRepository;
import service.BookingService;
import session.LoginSession;
import util.Loading;
import util.Tampilan;
import service.WaitlistService;

public class MenuBeliTiket {

    Scanner input = new Scanner(System.in);

    TicketRepository ticketRepo =
            new TicketRepository();
        
    BookingService bookingService =
            new BookingService();

        WaitlistService waitlistService =
        new WaitlistService();

    public void tampilMenu() {

        Tampilan.judul("BELI TICKET");

        System.out.print(
                "Masukkan ID Event : "
        );

        int eventId =
                input.nextInt();

        /*
         * =========================
         * TAMPILKAN TICKET
         * =========================
         */
        ticketRepo.tampilTicketByEvent(
                eventId
        );

        System.out.print(
                "\nPilih ID Ticket : "
        );

        int ticketId =
                input.nextInt();

        System.out.print(
                "Jumlah Ticket   : "
        );

        int jumlah =
                input.nextInt();

        input.nextLine();

        System.out.print(
                "Kode Discount (Opsional) : "
        );

        String discountCode =
                input.nextLine();

        /*
         * =========================
         * BELI TICKET
         * =========================
         */
        Loading.tampil(
        "Memproses pembelian tiket"
);

        boolean hasil =
        bookingService.beliTicket(

                LoginSession.getUserId(),

                eventId,

                ticketId,

                jumlah,

                discountCode
        );

/*
 * =========================
 * WAITLIST
 * =========================
 */
if (!hasil) {

    Tampilan.info(
        "Masuk waitlist?"
);

    System.out.println(
            "1. Ya"
    );

    System.out.println(
            "0. Tidak"
    );

    System.out.print(
            "\nPilih : "
    );

    int pilihWaitlist =
            input.nextInt();

    input.nextLine();

    if (pilihWaitlist == 1) {

        waitlistService.masukWaitlist(

                LoginSession.getUserId(),

                ticketId
        );
    }
}
    }
}