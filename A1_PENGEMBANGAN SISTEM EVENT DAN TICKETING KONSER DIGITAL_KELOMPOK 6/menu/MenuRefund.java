package menu;

import java.util.Scanner;

import service.PaymentService;
import service.RefundService;
import session.LoginSession;
import util.Loading;
import util.Tampilan;

public class MenuRefund {

    Scanner input =
            new Scanner(System.in);

    RefundService refundService =
            new RefundService();

    PaymentService paymentService =
            new PaymentService();

    public void tampilMenu() {

        Tampilan.judul("REFUND");

        paymentService.tampilPaymentBerhasil(
                LoginSession.getUserId()
        );

        System.out.print(
                "\nMasukkan ID Payment : "
        );

        int paymentId =
                input.nextInt();

        input.nextLine();

        System.out.print(
                "Alasan Refund : "
        );

        String alasan =
                input.nextLine();

        Loading.tampil(
        "Mengajukan refund"
);

        refundService.requestRefund(
                paymentId,
                alasan
        );
    }
}