package service;

import repository.CheckinRepository;

public class CheckinService {

    CheckinRepository repo =
            new CheckinRepository();

    public void tampilTicketAktif() {

        repo.tampilTicketAktif();
    }

    public void tampilSemuaTicket() {

        repo.tampilSemuaTicket();
    }

    public boolean prosesCheckin(

            int staffId,
            String ticketCode,
            String metode

    ) {

        return repo.prosesCheckin(

                staffId,

                ticketCode,

                metode
        );
    }
}