package menu;

import java.util.Scanner;

import service.SponsorBenefitService;
import util.Loading;
import util.Tampilan;

public class MenuSponsorBenefit {

    Scanner input =
            new Scanner(System.in);

    SponsorBenefitService service =
            new SponsorBenefitService();

    public void tampilMenu() {

        while (true) {

            Tampilan.judul(
                    "KELOLA BENEFIT SPONSOR"
            );

            System.out.println("1. Tambah Benefit");
            System.out.println("2. Lihat Benefit");
            System.out.println("3. Tambah Benefit ke Tier");
            System.out.println("4. Lihat Benefit Tier");
            System.out.println("0. Kembali");

            System.out.print(
                    "\nPilih menu : "
            );

            int pilih =
                    input.nextInt();

            input.nextLine();

            switch (pilih) {

                case 1 -> tambahBenefit();

                case 2 -> {

                    Loading.tampil(
                            "Mengambil data benefit sponsor"
                    );

                    service.tampilBenefit();
                }

                case 3 -> tambahBenefitTier();

                case 4 -> lihatBenefitTier();

                case 0 -> {
                    return;
                }

                default -> Tampilan.gagal(
                        "Menu tidak tersedia"
                );
            }
        }
    }

    private void tambahBenefit() {

        Tampilan.judul(
                "TAMBAH BENEFIT SPONSOR"
        );

        System.out.print(
                "Nama Benefit : "
        );

        String nama =
                input.nextLine();

        System.out.print(
                "Description : "
        );

        String desc =
                input.nextLine();

        System.out.println(
                "\nKategori:"
        );

        System.out.println("1. branding");
        System.out.println("2. media");
        System.out.println("3. booth");
        System.out.println("4. promosi");

        System.out.print(
                "\nPilih kategori : "
        );

        int pilih =
                input.nextInt();

        input.nextLine();

        String kategori = switch (pilih) {

            case 1 -> "branding";
            case 2 -> "media";
            case 3 -> "booth";
            case 4 -> "promosi";
            default -> "";
        };

        if (kategori.isBlank()) {

            Tampilan.gagal(
                    "Kategori tidak valid"
            );

            return;
        }

        Loading.tampil(
                "Menambahkan benefit sponsor"
        );

        service.tambahBenefit(
                nama,
                desc,
                kategori
        );

        Tampilan.sukses(
                "Proses tambah benefit selesai"
        );
    }

    private void tambahBenefitTier() {

        Tampilan.judul(
                "TAMBAH BENEFIT KE TIER"
        );

        System.out.print(
                "Tier ID : "
        );

        int tierId =
                input.nextInt();

        System.out.print(
                "Benefit ID : "
        );

        int benefitId =
                input.nextInt();

        input.nextLine();

        Loading.tampil(
                "Menambahkan benefit ke tier"
        );

        service.tambahBenefitTier(
                tierId,
                benefitId
        );

        Tampilan.sukses(
                "Proses tambah benefit ke tier selesai"
        );
    }

    private void lihatBenefitTier() {

        Tampilan.judul(
                "LIHAT BENEFIT TIER"
        );

        System.out.print(
                "Tier ID : "
        );

        int tierId =
                input.nextInt();

        input.nextLine();

        Loading.tampil(
                "Mengambil benefit tier"
        );

        service.tampilBenefitTier(
                tierId
        );
    }
}