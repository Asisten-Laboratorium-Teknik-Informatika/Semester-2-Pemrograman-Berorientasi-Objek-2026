package menu;

import java.util.Scanner;

import service.DiscountService;
import util.Loading;
import util.Tampilan;

public class MenuKelolaDiscount {

    Scanner input =
            new Scanner(System.in);

    DiscountService service =
            new DiscountService();

    public void tampilMenu(
            int organizerId
    ) {

        while (true) {

            Tampilan.judul("KELOLA DISCOUNT");

            System.out.println("1. Tambah Discount");
            System.out.println("2. Lihat Discount");
            System.out.println("3. Hapus Discount");
            System.out.println("0. Kembali");

            System.out.print("\nPilih menu : ");

            int pilih =
                    input.nextInt();

            input.nextLine();

            switch (pilih) {

                case 1 -> tambahDiscount(
                        organizerId
                );

                case 2 -> {

                    Loading.tampil(
                            "Mengambil data discount"
                    );

                    service.tampilDiscount(
                            organizerId
                    );
                }

                case 3 -> hapusDiscount();

                case 0 -> {
                    return;
                }

                default -> Tampilan.gagal(
                        "Menu tidak tersedia"
                );
            }
        }
    }

    private void tambahDiscount(
            int organizerId
    ) {

        Tampilan.judul("TAMBAH DISCOUNT");

        System.out.print("Event ID : ");

        int eventId =
                input.nextInt();

        input.nextLine();

        System.out.print("Code : ");

        String code =
                input.nextLine();

        System.out.println("\nJenis Discount");
        System.out.println("1. percentage");
        System.out.println("2. fixed");

        System.out.print("\nPilih : ");

        int pilih =
                input.nextInt();

        input.nextLine();

        String type = "";

        switch (pilih) {

            case 1 -> type = "percentage";

            case 2 -> type = "fixed";

            default -> {

                Tampilan.gagal(
                        "Pilihan tidak valid"
                );

                return;
            }
        }

        System.out.print("Value Discount : ");

        double value =
                input.nextDouble();

        System.out.print("Minimum Purchase : ");

        double minPurchase =
                input.nextDouble();

        System.out.print("Usage Limit : ");

        int usageLimit =
                input.nextInt();

        input.nextLine();

        System.out.println("\nFormat Date : YYYY-MM-DD");

        System.out.print("Start Date : ");

        String startDate =
                input.nextLine();

        System.out.print("End Date   : ");

        String endDate =
                input.nextLine();

        Loading.tampil(
                "Menambahkan discount"
        );

        boolean hasil =
                service.tambahDiscount(
                        code,
                        organizerId,
                        eventId,
                        type,
                        value,
                        minPurchase,
                        usageLimit,
                        startDate,
                        endDate
                );

        if (hasil) {

            Tampilan.sukses(
                    "Discount berhasil ditambahkan"
            );

        } else {

            Tampilan.gagal(
                    "Gagal tambah discount"
            );
        }
    }

    private void hapusDiscount() {

        Tampilan.judul("HAPUS DISCOUNT");

        System.out.print("Discount ID : ");

        int discountId =
                input.nextInt();

        input.nextLine();

        Loading.tampil(
                "Menghapus discount"
        );

        boolean hasil =
                service.hapusDiscount(
                        discountId
                );

        if (hasil) {

            Tampilan.sukses(
                    "Discount berhasil dihapus"
            );

        } else {

            Tampilan.gagal(
                    "Gagal hapus discount"
            );
        }
    }
}