package service;

import repository.RefundRepository;

public class RefundService {

    RefundRepository repo =
            new RefundRepository();

    public boolean requestRefund(
            int paymentId,
            String alasan
    ) {

        return repo.requestRefund(
                paymentId,
                alasan
        );
    }

    public void tampilDataRefund() {

        repo.tampilDataRefund();
    }

    public void tampilRiwayatRefund(
            int userId
    ) {

        repo.tampilRiwayatRefund(
                userId
        );
    }
}