package service;

import repository.LaporanOrganizerRepository;

public class LaporanOrganizerService {

    LaporanOrganizerRepository repo =
            new LaporanOrganizerRepository();

    /*
     * =========================
     * LAPORAN PENJUALAN
     * =========================
     */
    public void laporanPenjualan(
            int organizerId
    ) {

        repo.laporanPenjualan(
                organizerId
        );
    }

    /*
     * =========================
     * STATISTIK TICKET
     * =========================
     */
    public void statistikTicket(
            int organizerId
    ) {

        repo.statistikTicket(
                organizerId
        );
    }
}