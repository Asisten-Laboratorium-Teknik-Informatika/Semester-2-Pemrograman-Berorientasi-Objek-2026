package service;

import repository.EventOrganizerRepository;

public class EventOrganizerService {

    EventOrganizerRepository repo =
            new EventOrganizerRepository();

    /*
     * =========================
     * TAMBAH EVENT
     * =========================
     */
    public boolean tambahEvent(

            int organizerId,
            int venueId,
            String nama,
            String deskripsi,
            String tanggal,
            String jam

    ) {

        return repo.tambahEvent(

                organizerId,
                venueId,
                nama,
                deskripsi,
                tanggal,
                jam
        );
    }

    /*
     * =========================
     * LIHAT EVENT
     * =========================
     */
    public void tampilEventOrganizer(
            int organizerId
    ) {

        repo.tampilEventOrganizer(
                organizerId
        );
    }

    /*
     * =========================
     * UPDATE STATUS
     * =========================
     */
    public boolean updateStatusEvent(
            int eventId,
            String status
    ) {

        return repo.updateStatusEvent(
                eventId,
                status
        );
    }

    /*
     * =========================
     * HAPUS EVENT
     * =========================
     */
    public boolean hapusEvent(
            int eventId
    ) {

        return repo.hapusEvent(
                eventId
        );
    }
}