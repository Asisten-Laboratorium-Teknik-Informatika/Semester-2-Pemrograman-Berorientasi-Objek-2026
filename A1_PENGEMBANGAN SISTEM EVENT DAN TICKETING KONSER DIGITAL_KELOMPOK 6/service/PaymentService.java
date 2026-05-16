package service;

import repository.PaymentRepository;

public class PaymentService {

    PaymentRepository repo =
            new PaymentRepository();

    public void tampilPaymentPending(
            int userId
    ) {

        repo.tampilPaymentPending(
                userId
        );
    }

    public void tampilPaymentBerhasil(
            int userId
    ) {

        repo.tampilPaymentBerhasil(
                userId
        );
    }

    public boolean bayarTicket(

        int paymentId,
        String metode,
        double uangBayar

) {

    return repo.bayarTicket(

            paymentId,
            metode,
            uangBayar
    );
}

    public void tampilTicketSaya(
            int userId
    ) {

        repo.tampilTicketSaya(
                userId
        );
    }

    public boolean cetakTicketSaya(
            int userId
    ) {

        return repo.cetakTicketSaya(
                userId
        );
    }
}