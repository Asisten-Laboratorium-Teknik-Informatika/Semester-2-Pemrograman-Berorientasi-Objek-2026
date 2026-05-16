package menu;

import java.util.Scanner;

import model.User;
import service.AuthService;
import util.Loading;
import util.Tampilan;

public class MenuLogin {

    Scanner input =
            new Scanner(System.in);

    AuthService authService =
            new AuthService();

    public void tampilLogin() {

        Tampilan.judul(
                "LOGIN"
        );

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

        Loading.tampil(
                "Memeriksa akun"
        );

        User user =
                authService.login(
                        email,
                        password
                );

        if (user != null) {

            Tampilan.sukses(
                    "Login berhasil"
            );

            String role =
                    user.getRole();

            switch (role) {

                case "admin" -> new DashboardAdmin()
                        .tampilDashboard();

                case "staff" -> new DashboardStaff()
                        .tampilDashboard();

                case "organizer" -> new DashboardOrganizer()
                        .tampilDashboard();

                default -> new DashboardUser()
                        .tampilDashboard();
            }

        } else {

            Tampilan.gagal(
                    "Email atau password salah"
            );
        }
    }
}