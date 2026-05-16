package service;

import repository.OrganizerTicketRepository;

public class OrganizerTicketService {

    OrganizerTicketRepository repo =
            new OrganizerTicketRepository();

    public boolean tambahTicket(

            int eventId,
            String type,
            double price,
            int quota,
            String saleStart,
            String saleEnd

    ) {

        return repo.tambahTicket(

                eventId,
                type,
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

    public boolean updateStatusTicket(
            int ticketId,
            String status
    ) {

        return repo.updateStatusTicket(
                ticketId,
                status
        );
    }
}