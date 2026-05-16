package entity;

import util.Tampilan;

public class QrisPayment extends PaymentMethod {

    public QrisPayment(
            double amount
    ) {

        super(amount);
    }

    @Override
public void bayar() {

    Tampilan.sukses(
            "Pembayaran QRIS sebesar Rp "
            + amount
    );
}
}