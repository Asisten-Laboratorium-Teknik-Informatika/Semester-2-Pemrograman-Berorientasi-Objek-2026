package menu;

import java.util.Scanner;

import repository.OrganizerRepository;
import service.EventOrganizerService;
import session.LoginSession;
import util.Loading;
import util.Tampilan;

public class MenuKelolaEvent {

    Scanner input =
            new Scanner(System.in);

    EventOrganizerService service =
            new EventOrganizerService();

    OrganizerRepository organizerRepo =
            new OrganizerRepository();

    public void tampilMenu() {

        int organizerId =
                organizerRepo.getOrganizerIdByUserId(
                        LoginSession.getUserId()
                );

        if (organizerId == 0) {

            System.out.println(
                    "\nData organizer tidak ditemukan!"
            );

            return;
        }

        while (true) {

            Tampilan.judul("KELOLA EVENT");

            System.out.println(
                    "1. Tambah Event"
            );

            System.out.println(
                    "2. Lihat Event Saya"
            );

            System.out.println(
                    "3. Update Status Event"
            );

            System.out.println(
                    "4. Hapus Event"
            );

            System.out.println(
                    "0. Kembali"
            );

            System.out.print(
                    "\nPilih menu : "
            );

            int pilih =
                    input.nextInt();

            input.nextLine();

            switch (pilih) {
                
                case 1 -> tambahEvent(
                        organizerId
                );
                
                case 2 -> 

                
                service.tampilEventOrganizer(
                        organizerId
                );
                

                case 3 -> updateStatus();

                case 4 -> hapusEvent();

                case 0 -> {
                    return;
                }

                default -> System.out.println(
                        "\nMenu tidak tersedia!"
                );
            }
        }
    }

    private void tambahEvent(
            int organizerId
    ) {

        Tampilan.judul("TAMBAH EVENT");

        System.out.print(
                "Venue ID : "
        );

        int venueId =
                input.nextInt();

        input.nextLine();

        System.out.print(
                "Nama Event : "
        );

        String nama =
                input.nextLine();

        System.out.print(
                "Deskripsi : "
        );

        String deskripsi =
                input.nextLine();

        Tampilan.judul("FORMAT TANGGAL");

        System.out.println(
                "Gunakan format : YYYY-MM-DD"
        );

        System.out.println(
                "Contoh         : 2026-12-06"
        );

        System.out.print(
                "\nTanggal Event : "
        );

        String tanggal =
                input.nextLine();

        Tampilan.judul("FORMAT JAM");

        System.out.println(
                "Gunakan format : HH:MM:SS"
        );

        System.out.println(
                "Contoh         : 20:00:00"
        );

        System.out.print(
                "\nJam Event : "
        );

        String jam =
                input.nextLine();

                Loading.tampil(
        "Menyimpan event"
);
        Loading.tampil(
        "Menyimpan event"
);

        boolean hasil =
                service.tambahEvent(
                        organizerId,
                        venueId,
                        nama,
                        deskripsi,
                        tanggal,
                        jam
                );

        if (hasil) {

            Tampilan.sukses(
        "Berhasil membuat event"
);

        } else {

            Tampilan.gagal(
        "Gagal membuat event"
);
        }
    }

    private void updateStatus() {

        Tampilan.judul("UPDATE STATUS EVENT");

        System.out.print(
                "Event ID : "
        );

        int eventId =
                input.nextInt();

        input.nextLine();

        System.out.println(
                "\nPilihan Status :"
        );

        System.out.println(
                "1. draft"
        );

        System.out.println(
                "2. published"
        );

        System.out.println(
                "3. ongoing"
        );

        System.out.println(
                "4. completed"
        );

        System.out.println(
                "5. cancelled"
        );

        System.out.print(
                "\nMasukkan Status : "
        );

        String status =
                input.nextLine().trim();

                Loading.tampil(
        "Mengupdate status event"
);

        boolean hasil =
                service.updateStatusEvent(
                        eventId,
                        status
                );

        if (hasil) {

            Tampilan.sukses(
        "Update status Berhasil"
);

        } else {

            Tampilan.gagal(
        "Gagal update status event"
);
        }
    }

    private void hapusEvent() {

        Tampilan.judul("HAPUS EVENT");

        System.out.print(
                "Event ID : "
        );

        int eventId =
                input.nextInt();

        input.nextLine();

        Loading.tampil(
        "Menghapus event"
);

        boolean hasil =
                service.hapusEvent(
                        eventId
                );

        if (hasil) {

            Tampilan.sukses(
        "Event berhasil dihapus"
);

        } else {

            Tampilan.gagal(
        "Gagal hapus event"
);
        }
    }
}