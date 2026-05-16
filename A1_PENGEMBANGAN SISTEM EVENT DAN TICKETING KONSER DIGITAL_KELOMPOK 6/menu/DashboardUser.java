package menu;

import java.util.Scanner;

import service.WaitlistService;
import session.LoginSession;
import util.Loading;
import util.Tampilan;

public class DashboardUser {

    Scanner input =
            new Scanner(System.in);

    public void tampilDashboard() {

        Loading.tampil(
                "Membuka dashboard user"
        );

        while (true) {

            Tampilan.judul(
                    "DASHBOARD USER"
            );

            System.out.println(
                    "Login : "
                    + LoginSession.getNama()
            );

            System.out.println("\n1. Lihat Event");
            System.out.println("2. Beli Ticket");
            System.out.println("3. Pembayaran");
            System.out.println("4. Ticket Saya");
            System.out.println("5. Refund");
            System.out.println("6. Riwayat Refund");
            System.out.println("7. Waitlist Saya");
            System.out.println("0. Logout");

            System.out.print(
                    "\nPilih menu : "
            );

            int pilih =
                    input.nextInt();

            input.nextLine();

            switch (pilih) {

                case 1 -> new MenuLihatEvent()
                        .tampilEvent();

                case 2 -> new MenuBeliTiket()
                        .tampilMenu();

                case 3 -> new MenuPembayaran()
                        .tampilMenu();

                case 4 -> new MenuTicketSaya()
                        .tampilTicket();

                case 5 -> new MenuRefund()
                        .tampilMenu();

                case 6 -> new MenuRiwayatRefund()
                        .tampilRiwayat();

                case 7 -> {

                    Loading.tampil(
                            "Mengambil data waitlist"
                    );

                    WaitlistService service =
                            new WaitlistService();

                    service.tampilWaitlistUser(
                            LoginSession.getUserId()
                    );
                }

                case 0 -> {

                    Loading.tampil(
                            "Logout akun"
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