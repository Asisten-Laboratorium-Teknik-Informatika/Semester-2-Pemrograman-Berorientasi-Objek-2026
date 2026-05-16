package service;

import repository.WaitlistRepository;

public class WaitlistService {

    WaitlistRepository repo =
            new WaitlistRepository();

    public boolean masukWaitlist(

            int userId,
            int ticketId

    ) {

        return repo.masukWaitlist(
                userId,
                ticketId
        );
    }

    public void tampilWaitlistUser(
            int userId
    ) {

        repo.tampilWaitlistUser(
                userId
        );
    }
}