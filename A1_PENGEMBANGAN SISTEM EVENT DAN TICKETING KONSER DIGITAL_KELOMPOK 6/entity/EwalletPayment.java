package entity;

import util.Tampilan;

public class EwalletPayment extends PaymentMethod {

    public EwalletPayment(
            double amount
    ) {

        super(amount);
    }

    @Override
    public void bayar() {

        Tampilan.sukses(
        "Pembayaran Ewallet berhasil sebesar Rp "
        + amount
);
    }
}