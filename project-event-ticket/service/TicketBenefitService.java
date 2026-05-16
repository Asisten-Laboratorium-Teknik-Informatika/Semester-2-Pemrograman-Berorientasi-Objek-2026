package service;

import repository.TicketBenefitRepository;

public class TicketBenefitService {

    TicketBenefitRepository repo =
            new TicketBenefitRepository();

    public boolean tambahBenefit(

            String nama,
            String deskripsi,
            String kategori

    ) {

        return repo.tambahBenefit(
                nama,
                deskripsi,
                kategori
        );
    }

    public void tampilBenefit() {

        repo.tampilBenefit();
    }

    public boolean tambahBenefitKeTicket(

            int ticketId,
            int benefitId

    ) {

        return repo.tambahBenefitKeTicket(
                ticketId,
                benefitId
        );
    }

    public void tampilBenefitTicket(
            int ticketId
    ) {

        repo.tampilBenefitTicket(
                ticketId
        );
    }
}