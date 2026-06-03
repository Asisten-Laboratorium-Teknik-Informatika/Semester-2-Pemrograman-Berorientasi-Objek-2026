package SDM_PegawaiPayroll;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner input =
                new Scanner(System.in);

        int pilih;

        do {

            System.out.println(

            Warna.CYAN
            + Warna.BOLD
            + "\n╔══════════════════════════════════════╗"
            + "\n║           SISTEM PAYROLL             ║"
            + "\n║        MANAJEMEN SDM PEGAWAI         ║"
            + "\n╚══════════════════════════════════════╝"
            + Warna.RESET

            );

            System.out.println(
                    Warna.HIJAU
                    + "[1] LOGIN"
                    + Warna.RESET
            );

            System.out.println(
                    Warna.MERAH
                    + "[0] KELUAR"
                    + Warna.RESET
            );

            System.out.print(
                    Warna.KUNING
                    + "Pilih menu : "
                    + Warna.RESET
            );

            pilih =
                    Integer.parseInt(
                            input.nextLine()
                    );

            switch(pilih){

                case 1:

                    Login login =
                            new Login();

                    login.login();

                    break;

                case 0:

                    System.out.println(

                    Warna.CYAN
                    + Warna.BOLD
                    + "\n ======================"
                    + "\n|   Program selesai    |"
                    + "\n ======================"
                    + Warna.RESET
                    );

                    break;

                default:

                    System.out.println(

                    Warna.MERAH
                    + "Menu tidak tersedia!"
                    + Warna.RESET

                    );

            }

        }

        while(pilih != 0);

    }

}