package menu;

import java.util.Scanner;

import model.User;
import session.LoginSession;
import util.Tampilan;
import service.AuthService;

public class MenuOrganizer {

    Scanner input = new Scanner(System.in);

    AuthService authService = new AuthService();

    public void tampilLoginOrganizer() {

       Tampilan.judul("LOGIN ORGANIZER");

        System.out.print("Email    : ");
        String email = input.nextLine();

        System.out.print("Password : ");
        String password = input.nextLine();

        User user = authService.login(
                email,
                password
        );

        if (user != null) {

            if (
                    LoginSession
                            .getRole()
                            .equalsIgnoreCase(
                                    "organizer"
                            )
            ) {

                System.out.println(
                        "\nLogin organizer berhasil!"
                );

                System.out.println(
                        "Welcome Organizer : "
                        + LoginSession.getNama()
                );

                DashboardOrganizer dashboard
                        = new DashboardOrganizer();

                dashboard.tampilDashboard();

            } else {

                System.out.println(
                        "\nAkun ini bukan organizer!"
                );

                LoginSession.logout();
            }

        } else {

            System.out.println(
                    "\nEmail / Password salah!"
            );
        }
    }
}