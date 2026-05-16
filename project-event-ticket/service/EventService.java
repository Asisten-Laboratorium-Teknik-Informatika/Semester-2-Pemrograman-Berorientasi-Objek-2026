package service;

import repository.EventRepository;

public class EventService {

    EventRepository repo =
            new EventRepository();

    /*
     * =========================
     * GET ORGANIZER ID
     * =========================
     */
    public int getOrganizerId(
            int userId
    ) {

        return repo.getOrganizerId(
                userId
        );
    }

    /*
     * =========================
     * TAMPIL VENUE
     * =========================
     */
    public void tampilVenue() {

        repo.tampilVenue();
    }

    /*
     * =========================
     * TAMBAH EVENT
     * =========================
     */
    public boolean tambahEvent(

            int organizerId,
            int venueId,
            String namaEvent,
            String deskripsi,
            String tanggal,
            String jam

    ) {

        return repo.tambahEvent(

                organizerId,

                venueId,

                namaEvent,

                deskripsi,

                tanggal,

                jam
        );
    }

    /*
     * =========================
     * TAMPIL EVENT
     * =========================
     */
    public void tampilEvent() {

        repo.tampilEvent();
    }
}