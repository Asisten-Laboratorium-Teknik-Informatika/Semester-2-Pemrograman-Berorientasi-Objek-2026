package menu;

import service.RiwayatBookingService;
import session.LoginSession;
import util.Loading;
import util.Tampilan;

public class MenuRiwayatBooking {

    RiwayatBookingService service =
            new RiwayatBookingService();

    public void tampilRiwayat() {

        Tampilan.judul(
                "RIWAYAT BOOKING"
        );

        Loading.tampil(
                "Mengambil riwayat booking"
        );

        service.tampilRiwayat(
                LoginSession.getUserId()
        );
    }
}