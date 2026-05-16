package menu;

import java.util.Scanner;

import repository.OrganizerRepository;
import session.LoginSession;
import util.Loading;
import util.Tampilan;

public class DashboardOrganizer {

    Scanner input =
            new Scanner(System.in);

    OrganizerRepository organizerRepo =
            new OrganizerRepository();

    public void tampilDashboard() {

        int organizerId =
                organizerRepo.getOrganizerIdByUserId(
                        LoginSession.getUserId()
                );

        if (organizerId == 0) {

            Tampilan.gagal(
                    "Data organizer tidak ditemukan"
            );

            return;
        }

        Loading.tampil(
                "Membuka dashboard organizer"
        );

        while (true) {

            Tampilan.judul(
                    "DASHBOARD ORGANIZER"
            );

            System.out.println(
                    "Login : "
                    + LoginSession.getNama()
            );

            System.out.println("\n1. Kelola Event");
            System.out.println("2. Kelola Ticket");
            System.out.println("3. Kelola Staff Event");
            System.out.println("4. Kelola Sponsor");
            System.out.println("5. Kelola Discount");
            System.out.println("6. Laporan Penjualan");
            System.out.println("7. Kelola Benefit Ticket");
            System.out.println("8. Benefit Sponsor");
            System.out.println("0. Logout");

            System.out.print(
                    "\nPilih menu : "
            );

            int pilih =
                    input.nextInt();

            input.nextLine();

            switch (pilih) {

                case 1 -> new MenuKelolaEvent()
                        .tampilMenu();

                case 2 -> new MenuKelolaTicketOrganizer()
                        .tampilMenu(
                                organizerId
                        );

                case 3 -> new MenuKelolaStaffOrganizer()
                        .tampilMenu(
                                organizerId
                        );

                case 4 -> new MenuKelolaSponsor()
                        .tampilMenu(
                                organizerId
                        );

                case 5 -> new MenuKelolaDiscount()
                        .tampilMenu(
                                organizerId
                        );

                case 6 -> new MenuLaporanOrganizer()
                        .tampilMenu(
                                organizerId
                        );

                case 7 -> new MenuTicketBenefit()
                        .tampilMenu();

                case 8 -> new MenuSponsorBenefit()
                        .tampilMenu();

                case 0 -> {

                    Loading.tampil(
                            "Logout organizer"
                    );

                    LoginSession.logout();

                    Tampilan.sukses(
                            "Logout berhasil"
                    );

                    return;
                }

                default -> Tampilan.gagal(
                        "Menu tidak tersedia"
                );
            }
        }
    }
}