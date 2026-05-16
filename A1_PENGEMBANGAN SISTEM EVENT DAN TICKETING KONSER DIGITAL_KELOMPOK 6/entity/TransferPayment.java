package entity;

import util.Tampilan;

public class TransferPayment extends PaymentMethod {

    public TransferPayment(
            double amount
    ) {

        super(amount);
    }

    @Override
    public void bayar() {

        Tampilan.sukses(
        "Pembayaran Transfer berhasil sebesar Rp "
        + amount
);
    }
}