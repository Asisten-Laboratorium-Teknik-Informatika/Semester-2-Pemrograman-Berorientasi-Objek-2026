import java.util.Scanner;

import menu.MenuLogin;
import menu.MenuRegister;
import util.Tampilan;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        int pilih;

        do {

            Tampilan.judul(
        "EVENT TICKETING"
);

            System.out.println("1. Register User");
            System.out.println("2. Login");
            System.out.println("0. Keluar");

            System.out.print("Pilih menu : ");

            pilih = input.nextInt();
            input.nextLine();

            switch (pilih) {

                case 1:

                    MenuRegister register
                            = new MenuRegister();

                    register.tampilRegister();

                    break;

                case 2:

                    MenuLogin login
                            = new MenuLogin();

                    login.tampilLogin();

                    break;

                case 0:

                    System.out.println(
                            "\nProgram selesai"
                    );

                    break;

                default:

                    System.out.println(
                            "\nMenu tidak tersedia"
                    );
            }

        } while (pilih != 0);
    }
}