package util;

public class Tampilan {

    public static final String RESET = "\u001B[0m";
    public static final String HIJAU = "\u001B[32m";
    public static final String MERAH = "\u001B[31m";
    public static final String KUNING = "\u001B[33m";
    public static final String BIRU = "\u001B[34m";

    public static void judul(
            String teks
    ) {

        System.out.println(
                BIRU
                + "\n========================================"
                + RESET
        );

        System.out.println(
                BIRU
                + " " + teks
                + RESET
        );

        System.out.println(
                BIRU
                + "========================================"
                + RESET
        );
    }

    public static void sukses(
            String pesan
    ) {

        System.out.println(
                HIJAU
                + "\n[BERHASIL] "
                + pesan
                + RESET
        );
    }

    public static void gagal(
            String pesan
    ) {

        System.out.println(
                MERAH
                + "\n[GAGAL] "
                + pesan
                + RESET
        );
    }

    public static void info(
            String pesan
    ) {

        System.out.println(
                KUNING
                + "\n[INFO] "
                + pesan
                + RESET
        );
    }

    public static String status(
            String status
    ) {

        if (status == null) {

            return "-";
        }

        if (
                status.equalsIgnoreCase("paid")
                || status.equalsIgnoreCase("active")
                || status.equalsIgnoreCase("available")
                || status.equalsIgnoreCase("approved")
        ) {

            return HIJAU + status + RESET;
        }

        if (
                status.equalsIgnoreCase("pending")
                || status.equalsIgnoreCase("requested")
                || status.equalsIgnoreCase("draft")
        ) {

            return KUNING + status + RESET;
        }

        if (
                status.equalsIgnoreCase("cancelled")
                || status.equalsIgnoreCase("rejected")
                || status.equalsIgnoreCase("sold_out")
        ) {

            return MERAH + status + RESET;
        }

        return status;
    }

    public static void garis() {

        System.out.println(
                "----------------------------------------"
        );
    }
}