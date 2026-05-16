package menu;

import java.util.Scanner;

import service.EventService;
import session.LoginSession;
import util.Tampilan;

public class MenuTambahEvent {

    Scanner input = new Scanner(System.in);

    EventService service =
            new EventService();

    public void tampilMenu() {

        Tampilan.judul("TAMBAH EVENT");

        int organizerId =
                service.getOrganizerId(
                        LoginSession.getUserId()
                );

        /*
         * TAMPIL VENUE
         */

        service.tampilVenue();

        System.out.print(
                "\nPilih ID Venue : "
        );

        int venueId =
                input.nextInt();

        input.nextLine();

        System.out.print(
                "Nama Event     : "
        );

        String namaEvent =
                input.nextLine();

        System.out.print(
                "Deskripsi      : "
        );

        String deskripsi =
                input.nextLine();

        System.out.print(
                "Tanggal Event  : "
        );

        String tanggal =
                input.nextLine();

        System.out.print(
                "Jam Event      : "
        );

        String jam =
                input.nextLine();

        boolean berhasil =
                service.tambahEvent(

                        organizerId,

                        venueId,

                        namaEvent,

                        deskripsi,

                        tanggal,

                        jam
                );

        if (berhasil) {

            System.out.println(
                    "\nEvent berhasil dibuat!"
            );

        } else {

            System.out.println(
                    "\nEvent gagal dibuat!"
            );
        }
    }
}