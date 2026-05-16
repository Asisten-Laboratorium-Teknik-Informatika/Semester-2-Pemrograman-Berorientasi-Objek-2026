package menu;

import service.RefundService;
import session.LoginSession;
import util.Loading;
import util.Tampilan;

public class MenuRiwayatRefund {

    RefundService service =
            new RefundService();

    public void tampilRiwayat() {

        Tampilan.judul(
                "RIWAYAT REFUND"
        );

        Loading.tampil(
                "Mengambil riwayat refund"
        );

        service.tampilRiwayatRefund(
                LoginSession.getUserId()
        );
    }
}