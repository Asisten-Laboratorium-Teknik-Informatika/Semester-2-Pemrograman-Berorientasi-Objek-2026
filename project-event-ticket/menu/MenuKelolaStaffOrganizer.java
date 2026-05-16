package menu;

import java.util.Scanner;

import service.OrganizerStaffService;
import util.Loading;
import util.Tampilan;

public class MenuKelolaStaffOrganizer {

    Scanner input =
            new Scanner(System.in);

    OrganizerStaffService service =
            new OrganizerStaffService();

    public void tampilMenu(
            int organizerId
    ) {

        while (true) {

            Tampilan.judul(
                    "KELOLA STAFF EVENT"
            );

            System.out.println(
                    "1. Tambah Staff"
            );

            System.out.println(
                    "2. Lihat Staff"
            );

            System.out.println(
                    "3. Hapus Staff"
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

                case 1 -> tambahStaff();

                case 2 -> tampilStaff(
                        organizerId
                );

                case 3 -> hapusStaff();

                case 0 -> {
                    return;
                }

                default -> Tampilan.gagal(
                        "Menu tidak tersedia"
                );
            }
        }
    }

    private void tampilStaff(
            int organizerId
    ) {

        Tampilan.judul(
                "DATA STAFF EVENT"
        );

        Loading.tampil(
                "Mengambil data staff"
        );

        service.tampilStaff(
                organizerId
        );
    }

    private void tambahStaff() {

        Tampilan.judul(
                "TAMBAH STAFF"
        );

        System.out.print(
                "User ID Staff : "
        );

        int userId =
                input.nextInt();

        System.out.print(
                "Event ID : "
        );

        int eventId =
                input.nextInt();

        input.nextLine();

        System.out.print(
                "Role Staff : "
        );

        String role =
                input.nextLine();

        System.out.print(
                "Area Tugas : "
        );

        String area =
                input.nextLine();

        Loading.tampil(
                "Menambahkan staff"
        );

        boolean hasil =
                service.tambahStaff(
                        userId,
                        eventId,
                        role,
                        area
                );

        if (hasil) {

            Tampilan.sukses(
                    "Staff berhasil ditambahkan"
            );

        } else {

            Tampilan.gagal(
                    "Gagal menambahkan staff"
            );
        }
    }

    private void hapusStaff() {

        Tampilan.judul(
                "HAPUS STAFF"
        );

        System.out.print(
                "Staff ID : "
        );

        int staffId =
                input.nextInt();

        input.nextLine();

        Loading.tampil(
                "Menghapus staff"
        );

        boolean hasil =
                service.hapusStaff(
                        staffId
                );

        if (hasil) {

            Tampilan.sukses(
                    "Staff berhasil dihapus"
            );

        } else {

            Tampilan.gagal(
                    "Gagal menghapus staff"
            );
        }
    }
}