package menu;

import java.util.Scanner;

import service.AuthService;
import util.Loading;
import util.Tampilan;

public class MenuRegister {

    Scanner input =
            new Scanner(System.in);

    AuthService service =
            new AuthService();

    public void tampilRegister() {

        Tampilan.judul(
                "REGISTER"
        );

        System.out.print(
                "Nama     : "
        );

        String nama =
                input.nextLine();

        System.out.print(
                "Email    : "
        );

        String email =
                input.nextLine();

        System.out.print(
                "Password : "
        );

        String password =
                input.nextLine();

        System.out.print(
                "Phone    : "
        );

        String phone =
                input.nextLine();

        Loading.tampil(
                "Membuat akun"
        );

        boolean hasil =
                service.register(
                        nama,
                        email,
                        password,
                        phone
                );

        if (hasil) {

            Tampilan.sukses(
                    "Register berhasil"
            );

        } else {

            Tampilan.gagal(
                    "Register gagal"
            );
        }
    }
}