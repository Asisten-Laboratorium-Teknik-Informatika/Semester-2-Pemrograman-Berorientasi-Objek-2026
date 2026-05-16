package menu;

import repository.TicketRepository;
import util.Loading;
import util.Tampilan;

public class MenuLihatEvent {

    TicketRepository ticketRepo =
            new TicketRepository();

    public void tampilEvent() {

        Tampilan.judul(
                "LIHAT EVENT"
        );

        Loading.tampil(
                "Mengambil data event"
        );

        ticketRepo.tampilSemuaEvent();
    }
}