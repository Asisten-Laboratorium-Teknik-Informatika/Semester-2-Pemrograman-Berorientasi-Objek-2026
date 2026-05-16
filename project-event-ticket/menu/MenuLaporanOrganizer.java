package menu;

import java.util.Scanner;

import service.LaporanOrganizerService;
import util.Loading;
import util.Tampilan;

public class MenuLaporanOrganizer {

    Scanner input =
            new Scanner(System.in);

    LaporanOrganizerService service =
            new LaporanOrganizerService();

    public void tampilMenu(
            int organizerId
    ) {

        while (true) {

            Tampilan.judul(
                    "LAPORAN ORGANIZER"
            );

            System.out.println(
                    "1. Laporan Penjualan"
            );

            System.out.println(
                    "2. Statistik Ticket"
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

                case 1 -> tampilLaporanPenjualan(
                        organizerId
                );

                case 2 -> tampilStatistikTicket(
                        organizerId
                );

                case 0 -> {
                    return;
                }

                default -> Tampilan.gagal(
                        "Menu tidak tersedia"
                );
            }
        }
    }

    private void tampilLaporanPenjualan(
            int organizerId
    ) {

        Tampilan.judul(
                "LAPORAN PENJUALAN"
        );

        Loading.tampil(
                "Mengambil laporan penjualan"
        );

        service.laporanPenjualan(
                organizerId
        );
    }

    private void tampilStatistikTicket(
            int organizerId
    ) {

        Tampilan.judul(
                "STATISTIK TICKET"
        );

        Loading.tampil(
                "Mengambil statistik ticket"
        );

        service.statistikTicket(
                organizerId
        );
    }
}