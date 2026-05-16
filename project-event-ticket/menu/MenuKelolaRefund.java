package menu;

import java.util.Scanner;

import service.AdminService;
import session.LoginSession;
import util.Loading;
import util.Tampilan;

public class MenuKelolaRefund {

    Scanner input =
            new Scanner(System.in);

    AdminService service =
            new AdminService();

    public void tampilMenu() {

        Tampilan.judul(
                "KELOLA REFUND"
        );

        Loading.tampil(
                "Mengambil data refund"
        );

        service.tampilRefund();

        System.out.print(
                "\nMasukkan Refund ID : "
        );

        int refundId =
                input.nextInt();

        input.nextLine();

        System.out.println(
                "\n1. Approve Refund"
        );

        System.out.println(
                "2. Reject Refund"
        );

        System.out.print(
                "\nPilih menu : "
        );

        int pilih =
                input.nextInt();

        input.nextLine();

        switch (pilih) {

            case 1 -> {

                Loading.tampil(
                        "Menyetujui refund"
                );

                service.approveRefund(
                        refundId,
                        LoginSession.getUserId()
                );

                Tampilan.sukses(
                        "Refund berhasil diproses"
                );
            }

            case 2 -> {

                System.out.print(
                        "\nAlasan reject : "
                );

                String alasan =
                        input.nextLine();

                Loading.tampil(
                        "Menolak refund"
                );

                service.rejectRefund(
                        refundId,
                        alasan,
                        LoginSession.getUserId()
                );

                Tampilan.sukses(
                        "Refund berhasil ditolak"
                );
            }

            default -> Tampilan.gagal(
                    "Menu tidak tersedia"
            );
        }
    }
}