package service;

import repository.SponsorBenefitRepository;

public class SponsorBenefitService {

    SponsorBenefitRepository repo =
            new SponsorBenefitRepository();

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

    public boolean tambahBenefitTier(

            int tierId,
            int benefitId

    ) {

        return repo.tambahBenefitTier(
                tierId,
                benefitId
        );
    }

    public void tampilBenefitTier(
            int tierId
    ) {

        repo.tampilBenefitTier(
                tierId
        );
    }
}