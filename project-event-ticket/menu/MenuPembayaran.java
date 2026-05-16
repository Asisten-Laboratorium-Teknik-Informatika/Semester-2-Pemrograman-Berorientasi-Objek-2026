package menu;

import entity.EwalletPayment;
import entity.PaymentMethod;
import entity.QrisPayment;
import entity.TransferPayment;

import java.util.Scanner;

import service.PaymentService;
import session.LoginSession;
import util.Loading;
import util.Tampilan;

public class MenuPembayaran {

    Scanner input =
            new Scanner(System.in);

    PaymentService service =
            new PaymentService();

    public void tampilMenu() {

        Tampilan.judul("PEMBAYARAN");

        service.tampilPaymentPending(
                LoginSession.getUserId()
        );

        System.out.print(
                "\nMasukkan ID Payment : "
        );

        int paymentId =
                input.nextInt();

        input.nextLine();

        System.out.print(
                "Masukkan Total Bayar : "
        );

        double uangBayar =
                input.nextDouble();

        input.nextLine();

        System.out.println(
                "\nMetode Pembayaran:"
        );

        System.out.println(
                "1. transfer"
        );

        System.out.println(
                "2. qris"
        );

        System.out.println(
                "3. ewallet"
        );

        System.out.print(
                "\nPilih metode : "
        );

        int pilih =
                input.nextInt();

        input.nextLine();

        String metode;

        PaymentMethod payment;

        switch (pilih) {

            case 1 -> {

                metode =
                        "transfer";

                payment =
                        new TransferPayment(
                                uangBayar
                        );
            }

            case 2 -> {

                metode =
                        "qris";

                payment =
                        new QrisPayment(
                                uangBayar
                        );
            }

            case 3 -> {

                metode =
                        "ewallet";

                payment =
                        new EwalletPayment(
                                uangBayar
                        );
            }

            default -> {

                Tampilan.gagal(
        "Metode tidak valid"
);

                return;
            }
        }

        payment.bayar();

        Loading.tampil(
        "Memproses pembayaran"
);

        service.bayarTicket(
                paymentId,
                metode,
                uangBayar
        );
    }
}