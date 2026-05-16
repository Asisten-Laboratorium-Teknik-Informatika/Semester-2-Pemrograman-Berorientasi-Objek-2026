package menu;

import java.util.Scanner;

import service.OrganizerTicketService;
import util.Loading;
import util.Tampilan;

public class MenuKelolaTicketOrganizer {

    Scanner input = new Scanner(System.in);

    OrganizerTicketService service =
            new OrganizerTicketService();

    public void tampilMenu(
            int organizerId
    ) {

        while (true) {

            Tampilan.judul("KELOLA TICKET");

            System.out.println(
                    "1. Tambah Ticket"
            );

            System.out.println(
                    "2. Lihat Ticket"
            );

            System.out.println(
                    "3. Update Status Ticket"
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

                case 1 -> tambahTicket();

                case 2 -> {
                        Loading.tampil(
        "Mengambil data ticket"
);
                    service.tampilTicketOrganizer(
                            organizerId
                    );
                }

                case 3 -> updateStatus();

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

    private void tambahTicket() {

        System.out.print(
                "\nID Event : "
        );

        int eventId =
                input.nextInt();

        input.nextLine();

        System.out.print(
                "Type Ticket : "
        );

        String type =
                input.nextLine();

        System.out.print(
                "Harga : "
        );

        double price =
                input.nextDouble();

        System.out.print(
                "Quota : "
        );

        int quota =
                input.nextInt();

        input.nextLine();

        System.out.print(
                "Sale Start (2026-01-01 10:00:00) : "
        );

        String start =
                input.nextLine();

        System.out.print(
                "Sale End   (2026-01-10 10:00:00) : "
        );

        String end =
                input.nextLine();

   
        Loading.tampil(
        "Menambahkan benefit ticket"
);
        boolean hasil =
                service.tambahTicket(

                        eventId,
                        type,
                        price,
                        quota,
                        start,
                        end
                );

        if (hasil) {

            System.out.println(
                    "\nTicket berhasil ditambahkan!"
            );

        } else {

            System.out.println(
                    "\nGagal tambah ticket!"
            );
        }
    }

    private void updateStatus() {

        System.out.print(
                "\nID Ticket : "
        );

        int ticketId =
                input.nextInt();

        input.nextLine();

        System.out.print(
                "Status (available/sold_out/cancelled) : "
        );

        String status =
                input.nextLine();

                Loading.tampil(
        "Mengupdate status ticket"
);

        boolean hasil =
                service.updateStatusTicket(
                        ticketId,
                        status
                );

        if (hasil) {

            System.out.println(
                    "\nStatus berhasil diupdate!"
            );

        } else {

            System.out.println(
                    "\nGagal update status!"
            );
        }
    }
}