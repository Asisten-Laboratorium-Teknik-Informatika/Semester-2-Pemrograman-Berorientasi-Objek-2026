package menu;

import java.util.Scanner;

import repository.EventRepository;
import service.TicketService;
import session.LoginSession;
import util.Tampilan;

public class MenuTambahTicket {

    Scanner input = new Scanner(System.in);

    TicketService service =
            new TicketService();

    EventRepository eventRepo =
            new EventRepository();

    public void tampilMenu() {

        Tampilan.judul("TAMBAH TICKET");

        int organizerId =
                eventRepo.getOrganizerId(
                        LoginSession.getUserId()
                );

        service.tampilEventOrganizer(
                organizerId
        );

        System.out.print(
                "\nPilih ID Event : "
        );

        int eventId =
                input.nextInt();

        input.nextLine();

        System.out.print(
                "Jenis Ticket (regular/vip/vvip) : "
        );

        String type =
                input.nextLine();

        System.out.print(
                "Harga Ticket : "
        );

        double harga =
                input.nextDouble();

        System.out.print(
                "Total Quota  : "
        );

        int quota =
                input.nextInt();

        input.nextLine();

        System.out.print(
                "Sale Start (yyyy-mm-dd hh:mm:ss) : "
        );

        String saleStart =
                input.nextLine();

        System.out.print(
                "Sale End   (yyyy-mm-dd hh:mm:ss) : "
        );

        String saleEnd =
                input.nextLine();

        boolean berhasil =
                service.tambahTicket(

                        eventId,

                        type,

                        harga,

                        quota,

                        saleStart,

                        saleEnd
                );

        if (berhasil) {

            System.out.println(
                    "\nTicket berhasil dibuat!"
            );

        } else {

            System.out.println(
                    "\nTicket gagal dibuat!"
            );
        }
    }
}