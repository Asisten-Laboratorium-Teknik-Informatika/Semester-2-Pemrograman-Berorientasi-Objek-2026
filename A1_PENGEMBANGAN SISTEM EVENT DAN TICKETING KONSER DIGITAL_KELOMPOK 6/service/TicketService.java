package service;

import repository.TicketRepository;

public class TicketService {

    TicketRepository repo =
            new TicketRepository();

    public void tampilEventOrganizer(
            int organizerId
    ) {

        repo.tampilEventOrganizer(
                organizerId
        );
    }

    public boolean tambahTicket(

            int eventId,
            String ticketType,
            double price,
            int quota,
            String saleStart,
            String saleEnd

    ) {

        return repo.tambahTicket(

                eventId,

                ticketType,

                price,

                quota,

                saleStart,

                saleEnd
        );
    }

    public void tampilTicketOrganizer(
            int organizerId
    ) {

        repo.tampilTicketOrganizer(
                organizerId
        );
    }
}