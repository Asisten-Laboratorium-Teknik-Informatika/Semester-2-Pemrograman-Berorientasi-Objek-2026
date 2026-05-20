public class UI {

    // =========================
    // WARNA TERMINAL
    // =========================

    public static final String RESET  = "\u001B[0m";
    public static final String CYAN   = "\u001B[36m";
    public static final String GREEN  = "\u001B[32m";
    public static final String RED    = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";
    public static final String WHITE  = "\u001B[37m";
    public static final String PURPLE = "\u001B[35m";
    public static final String BLUE   = "\u001B[34m";
    public static final String PINK   = "\u001B[95m";

    // =========================
    // HEADER BIOSKOP
    // =========================

    public static void header() {

        System.out.println(CYAN);

        System.out.println(
            "\n=========================================================="
        );

        System.out.println(
            "                    BIOSKOP M-TIX"
        );

        System.out.println(
            "=========================================================="
        );

        System.out.println(RESET);

        System.out.println(
            YELLOW +
            "             PREMIUM CINEMA BOOKING SYSTEM"
            + RESET
        );

        System.out.println(
            WHITE +
            "\n=========================================================="
            + RESET
        );

        System.out.println(
            "        IMAX | DOLBY ATMOS | SWEETBOX | PREMIERE"
        );

        System.out.println(
            WHITE +
            "==========================================================\n"
            + RESET
        );
    }

    // =========================
    // SUCCESS MESSAGE
    // =========================

    public static void sukses(String msg) {

        System.out.println(
            GREEN +
            "\n[SUCCESS] "
            + msg
            + RESET
        );
    }

    // =========================
    // ERROR MESSAGE
    // =========================

    public static void error(String msg) {

        System.out.println(
            RED +
            "\n[ERROR] "
            + msg
            + RESET
        );
    }

    // =========================
    // WARNING MESSAGE
    // =========================

    public static void warning(String msg) {

        System.out.println(
            YELLOW +
            "\n[WARNING] "
            + msg
            + RESET
        );
    }

    // =========================
    // INFO MESSAGE
    // =========================

    public static void info(String msg) {

        System.out.println(
            CYAN +
            "\n[INFO] "
            + msg
            + RESET
        );
    }

    // =========================
    // LOADING
    // =========================

    public static void loading() {

        try {

            System.out.print(
                CYAN +
                "\nLoading"
                + RESET
            );

            for (int i = 0; i < 6; i++) {

                Thread.sleep(400);

                System.out.print(".");
            }

            System.out.println();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =========================
    // FOOTER
    // =========================

    public static void footer() {

        System.out.println(
            WHITE +
            "\n=========================================================="
            + RESET
        );

        System.out.println(
            CYAN +
            "             TERIMA KASIH TELAH MENGGUNAKAN"
            + RESET
        );

        System.out.println(
            GREEN +
            "                    BIOSKOP M-TIX"
            + RESET
        );

        System.out.println(
            WHITE +
            "==========================================================\n"
            + RESET
        );
    }
}