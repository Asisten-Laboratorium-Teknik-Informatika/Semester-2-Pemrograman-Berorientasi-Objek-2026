package service;

import repository.BookingRepository;

public class BookingService {

    BookingRepository repo =
            new BookingRepository();

    public boolean beliTicket(

            int userId,
            int eventId,
            int ticketId,
            int jumlah,
            String discountCode

    ) {

        return repo.beliTicket(

                userId,
                eventId,
                ticketId,
                jumlah,
                discountCode
        );
    }
}