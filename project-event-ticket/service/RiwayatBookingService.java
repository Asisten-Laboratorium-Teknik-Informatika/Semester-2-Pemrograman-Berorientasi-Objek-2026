package service;

import repository.RiwayatBookingRepository;

public class RiwayatBookingService {

    RiwayatBookingRepository repo =
            new RiwayatBookingRepository();

    public void tampilRiwayat(
            int userId
    ) {

        repo.tampilRiwayat(userId);
    }
}