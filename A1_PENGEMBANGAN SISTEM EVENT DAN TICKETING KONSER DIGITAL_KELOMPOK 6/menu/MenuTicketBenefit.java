package menu;

import java.util.Scanner;

import service.TicketBenefitService;
import util.Loading;
import util.Tampilan;

public class MenuTicketBenefit {

    Scanner input =
            new Scanner(System.in);

    TicketBenefitService service =
            new TicketBenefitService();

    public void tampilMenu() {

        while (true) {

            Tampilan.judul(
                    "KELOLA BENEFIT TICKET"
            );

            System.out.println("1. Tambah Benefit");
            System.out.println("2. Lihat Benefit");
            System.out.println("3. Tambah Benefit ke Ticket");
            System.out.println("4. Lihat Benefit Ticket");
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
                            "Mengambil data benefit"
                    );

                    service.tampilBenefit();
                }

                case 3 -> tambahBenefitTicket();

                case 4 -> lihatBenefitTicket();

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
                "TAMBAH BENEFIT"
        );

        System.out.print(
                "Nama Benefit : "
        );

        String nama =
                input.nextLine();

        System.out.print(
                "Deskripsi : "
        );

        String deskripsi =
                input.nextLine();

        System.out.println(
                "\nKategori:"
        );

        System.out.println("1. akses");
        System.out.println("2. merchandise");
        System.out.println("3. konsumsi");
        System.out.println("4. fasilitas");
        System.out.println("5. eksklusif");

        System.out.print(
                "\nPilih kategori : "
        );

        int pilih =
                input.nextInt();

        input.nextLine();

        String kategori = switch (pilih) {

            case 1 -> "akses";
            case 2 -> "merchandise";
            case 3 -> "konsumsi";
            case 4 -> "fasilitas";
            case 5 -> "eksklusif";
            default -> "";
        };

        if (kategori.isBlank()) {

            Tampilan.gagal(
                    "Kategori tidak valid"
            );

            return;
        }

        Loading.tampil(
                "Menambahkan benefit"
        );

        boolean hasil =
                service.tambahBenefit(
                        nama,
                        deskripsi,
                        kategori
                );

        if (hasil) {

            Tampilan.sukses(
                    "Benefit berhasil ditambahkan"
            );

        } else {

            Tampilan.gagal(
                    "Gagal menambahkan benefit"
            );
        }
    }

    private void tambahBenefitTicket() {

        Tampilan.judul(
                "TAMBAH BENEFIT KE TICKET"
        );

        System.out.print(
                "Ticket ID : "
        );

        int ticketId =
                input.nextInt();

        System.out.print(
                "Benefit ID : "
        );

        int benefitId =
                input.nextInt();

        input.nextLine();

        Loading.tampil(
                "Menambahkan benefit ke ticket"
        );

        boolean hasil =
                service.tambahBenefitKeTicket(
                        ticketId,
                        benefitId
                );

        if (hasil) {

            Tampilan.sukses(
                    "Benefit berhasil ditambahkan ke ticket"
            );

        } else {

            Tampilan.gagal(
                    "Gagal menambahkan benefit ke ticket"
            );
        }
    }

    private void lihatBenefitTicket() {

        Tampilan.judul(
                "LIHAT BENEFIT TICKET"
        );

        System.out.print(
                "Ticket ID : "
        );

        int ticketId =
                input.nextInt();

        input.nextLine();

        Loading.tampil(
                "Mengambil benefit ticket"
        );

        service.tampilBenefitTicket(
                ticketId
        );
    }
}