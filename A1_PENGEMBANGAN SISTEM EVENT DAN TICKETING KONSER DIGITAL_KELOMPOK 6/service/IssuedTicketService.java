package service;

import repository.IssuedTicketRepository;

public class IssuedTicketService {

    IssuedTicketRepository repo =
            new IssuedTicketRepository();

    public void tampilTicketUser(
            int userId
    ) {

        repo.tampilTicketUser(
                userId
        );
    }
}