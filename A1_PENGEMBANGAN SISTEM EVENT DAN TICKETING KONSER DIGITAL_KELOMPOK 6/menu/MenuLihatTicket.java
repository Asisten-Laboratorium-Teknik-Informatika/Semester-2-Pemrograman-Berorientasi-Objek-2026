package menu;

import repository.EventRepository;
import service.TicketService;
import session.LoginSession;

public class MenuLihatTicket {

    TicketService service =
            new TicketService();

    EventRepository repo =
            new EventRepository();

    public void tampilTicket() {

        int organizerId =
                repo.getOrganizerId(
                        LoginSession.getUserId()
                );

        service.tampilTicketOrganizer(
                organizerId
        );
    }
}