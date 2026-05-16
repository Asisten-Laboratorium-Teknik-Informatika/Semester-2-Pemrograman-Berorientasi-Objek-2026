package menu;

import java.util.Scanner;

import service.OrganizerSponsorService;
import util.Loading;
import util.Tampilan;

public class MenuKelolaSponsor {

    Scanner input =
            new Scanner(System.in);

    OrganizerSponsorService service =
            new OrganizerSponsorService();

    public void tampilMenu(
            int organizerId
    ) {

        while (true) {

            Tampilan.judul("KELOLA SPONSOR");

            System.out.println("1. Tambah Sponsor");
            System.out.println("2. Lihat Sponsor");
            System.out.println("3. Update Status Sponsor");
            System.out.println("4. Hapus Sponsor");
            System.out.println("0. Kembali");

            System.out.print("\nPilih menu : ");

            int pilih =
                    input.nextInt();

            input.nextLine();

            switch (pilih) {

                case 1 -> tambahSponsor();

                case 2 -> {
                    Loading.tampil("Mengambil data sponsor");

                    service.tampilSponsor(
                            organizerId
                    );
                }

                case 3 -> updateStatusSponsor();

                case 4 -> hapusSponsor();

                case 0 -> {
                    return;
                }

                default -> Tampilan.gagal(
                        "Menu tidak tersedia"
                );
            }
        }
    }

    private void tambahSponsor() {

        Tampilan.judul("TAMBAH SPONSOR");

        System.out.print("Event ID : ");
        int eventId = input.nextInt();

        System.out.print("Tier ID : ");
        int tierId = input.nextInt();

        input.nextLine();

        System.out.print("Nama Perusahaan : ");
        String company = input.nextLine();

        System.out.print("Contact Person : ");
        String contact = input.nextLine();

        System.out.print("Email : ");
        String email = input.nextLine();

        System.out.print("Contribution : ");
        double amount = input.nextDouble();

        input.nextLine();

        System.out.println("\nFormat Date : YYYY-MM-DD");
        System.out.println("Contoh      : 2026-01-01");

        System.out.print("\nStart Date : ");
        String start = input.nextLine();

        System.out.print("End Date   : ");
        String end = input.nextLine();

        Loading.tampil("Menambahkan sponsor");

        boolean hasil =
                service.tambahSponsor(
                        eventId,
                        tierId,
                        company,
                        contact,
                        email,
                        amount,
                        start,
                        end
                );

        if (hasil) {

            Tampilan.sukses(
                    "Sponsor berhasil ditambahkan"
            );

        } else {

            Tampilan.gagal(
                    "Gagal tambah sponsor"
            );
        }
    }

    private void updateStatusSponsor() {

        Tampilan.judul("UPDATE SPONSOR");

        System.out.print("Sponsor ID : ");
        int sponsorId = input.nextInt();

        input.nextLine();

        System.out.println("\nPilihan Status :");
        System.out.println("1. pending");
        System.out.println("2. active");
        System.out.println("3. ended");

        System.out.print("\nMasukkan pilihan : ");
        int pilih = input.nextInt();

        input.nextLine();

        String status = "";

        switch (pilih) {

            case 1 -> status = "pending";
            case 2 -> status = "active";
            case 3 -> status = "ended";

            default -> {
                Tampilan.gagal("Pilihan tidak valid");
                return;
            }
        }

        Loading.tampil("Mengupdate status sponsor");

        boolean hasil =
                service.updateStatusSponsor(
                        sponsorId,
                        status
                );

        if (hasil) {

            Tampilan.sukses(
                    "Status sponsor berhasil diupdate"
            );

        } else {

            Tampilan.gagal(
                    "Gagal update sponsor"
            );
        }
    }

    private void hapusSponsor() {

        Tampilan.judul("HAPUS SPONSOR");

        System.out.print("Sponsor ID : ");
        int sponsorId = input.nextInt();

        input.nextLine();

        Loading.tampil("Menghapus sponsor");

        boolean hasil =
                service.hapusSponsor(
                        sponsorId
                );

        if (hasil) {

            Tampilan.sukses(
                    "Sponsor berhasil dihapus"
            );

        } else {

            Tampilan.gagal(
                    "Gagal hapus sponsor"
            );
        }
    }
}