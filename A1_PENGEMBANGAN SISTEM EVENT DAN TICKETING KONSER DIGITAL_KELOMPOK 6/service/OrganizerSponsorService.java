package service;

import repository.OrganizerSponsorRepository;

public class OrganizerSponsorService {

    OrganizerSponsorRepository repo =
            new OrganizerSponsorRepository();

    /*
     * =========================
     * TAMBAH SPONSOR
     * =========================
     */
    public boolean tambahSponsor(

            int eventId,
            int tierId,
            String company,
            String contact,
            String email,
            double amount,
            String start,
            String end

    ) {

        return repo.tambahSponsor(

                eventId,
                tierId,
                company,
                contact,
                email,
                amount,
                start,
                end
        );
    }

    /*
     * =========================
     * TAMPIL SPONSOR
     * =========================
     */
    public void tampilSponsor(
            int organizerId
    ) {

        repo.tampilSponsorOrganizer(
                organizerId
        );
    }

    /*
     * =========================
     * UPDATE STATUS SPONSOR
     * =========================
     */
    public boolean updateStatusSponsor(
            int sponsorId,
            String status
    ) {

        return repo.updateStatusSponsor(
                sponsorId,
                status
        );
    }

    /*
     * =========================
     * HAPUS SPONSOR
     * =========================
     */
    public boolean hapusSponsor(
            int sponsorId
    ) {

        return repo.hapusSponsor(
                sponsorId
        );
    }
}
