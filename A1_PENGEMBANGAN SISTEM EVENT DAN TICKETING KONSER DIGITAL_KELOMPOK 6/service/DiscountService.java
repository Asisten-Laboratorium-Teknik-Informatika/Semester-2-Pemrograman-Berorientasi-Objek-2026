package service;

import repository.DiscountRepository;

public class DiscountService {

    DiscountRepository repo =
            new DiscountRepository();

    public boolean tambahDiscount(

            String code,
            int organizerId,
            int eventId,
            String type,
            double value,
            double minPurchase,
            int usageLimit,
            String startDate,
            String endDate

    ) {

        return repo.tambahDiscount(

                code,
                organizerId,
                eventId,
                type,
                value,
                minPurchase,
                usageLimit,
                startDate,
                endDate
        );
    }

    public void tampilDiscount(
            int organizerId
    ) {

        repo.tampilDiscount(
                organizerId
        );
    }

    public boolean hapusDiscount(
            int discountId
    ) {

        return repo.hapusDiscount(
                discountId
        );
    }
}