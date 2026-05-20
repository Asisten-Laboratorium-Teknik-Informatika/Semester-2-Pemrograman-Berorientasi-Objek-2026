import java.util.Scanner;

public class PasswordMask {

    public static String inputPassword(Scanner input) {

        String password =
            input.nextLine();

        // Hapus input asli
        System.out.print(
            "\033[1A"
        );

        System.out.print(
            "\033[2K"
        );

        // Tampilkan *
        String bintang = "";

        for (
            int i = 0;
            i < password.length();
            i++
        ) {

            bintang += "*";
        }

        System.out.println(
            "Password : "
            + bintang
        );

        return password;
    }
}