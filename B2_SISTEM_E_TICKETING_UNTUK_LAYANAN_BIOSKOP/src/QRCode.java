public class QRCode {

    public static void tampil(){

        // Kode warna ANSI untuk QR Code (Hitam di atas Putih)
        String bgWhite = "\u001B[47m";
        String fgBlack = "\u001B[30m";
        String reset = "\u001B[0m";

        String[] qrArt = {
            "                                            ",
            "  ██████████████  ████  ██  ██████████████  ",
            "  ██          ██  ██        ██          ██  ",
            "  ██  ██████  ██    ██████  ██  ██████  ██  ",
            "  ██  ██████  ██  ██    ██  ██  ██████  ██  ",
            "  ██  ██████  ██      ██    ██  ██████  ██  ",
            "  ██          ██  ████████  ██          ██  ",
            "  ██████████████  ██  ██    ██████████████  ",
            "                                            ",
            "  ████    ████████████      ████  ██    ██  ",
            "      ██████  ██  ██  ████████  ████████    ",
            "  ██████    ██████    ██  ██████  ██    ██  ",
            "  ██  ████████  ██████████████████████████  ",
            "                                            ",
            "  ██████████████  ██  ██  ██    ██    ████  ",
            "  ██          ██  ████    ██████  ████      ",
            "  ██  ██████  ██    ██████  ██      ██████  ",
            "  ██  ██████  ██  ████    ████████  ██  ██  ",
            "  ██  ██████  ██    ██  ██    ██████████    ",
            "  ██          ██  ██████████      ████  ██  ",
            "  ██████████████  ████  ██  ████████    ██  ",
            "                                            "
        };

        System.out.println();
        for (String line : qrArt) {
            // Tambahkan spasi di awal agar QR Code tampil lebih di tengah
            System.out.println("        " + bgWhite + fgBlack + line + reset);
        }
    }
}