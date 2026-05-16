package service;

import repository.OrganizerStaffRepository;

public class OrganizerStaffService {

    OrganizerStaffRepository repo =
            new OrganizerStaffRepository();

    public boolean tambahStaff(

            int userId,
            int eventId,
            String role,
            String area

    ) {

        return repo.tambahStaff(

                userId,
                eventId,
                role,
                area
        );
    }

    public void tampilStaff(
            int organizerId
    ) {

        repo.tampilStaff(
                organizerId
        );
    }

    public boolean hapusStaff(
            int staffId
    ) {

        return repo.hapusStaff(
                staffId
        );
    }
}