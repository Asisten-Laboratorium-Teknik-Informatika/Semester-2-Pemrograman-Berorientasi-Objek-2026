import java.sql.*;
import java.util.Scanner;

public class Snack {

    Scanner input =
        new Scanner(System.in);

    // ======================================================
    // BELI SNACK
    // ======================================================

    public void beliSnack() {

        while (true) {

            System.out.println(UI.YELLOW);

            System.out.println(
                "\n========================================================"
            );

            System.out.println(
                "                  SNACK BIOSKOP"
            );

            System.out.println(
                "========================================================"
            );

            System.out.println(UI.RESET);

            // ======================================================
            // MENU SNACK
            // ======================================================

            System.out.println(
                "1. Popcorn Caramel  - Rp 45.000"
            );

            System.out.println(
                "2. Popcorn Cheese   - Rp 40.000"
            );

            System.out.println(
                "3. Coca Cola        - Rp 20.000"
            );

            System.out.println(
                "4. Nachos Cheese    - Rp 35.000"
            );

            System.out.println(
                "5. Combo Hemat      - Rp 60.000"
            );

            System.out.println(
                "6. Kembali"
            );

            System.out.print(
                "\nPilih snack : "
            );

            int pilih =
                input.nextInt();

            // ======================================================
            // KEMBALI
            // ======================================================

            if (pilih == 6) {

                break;
            }

            System.out.print(
                "Jumlah beli : "
            );

            int jumlah =
                input.nextInt();

            String namaSnack = "";

            int harga = 0;

            // ======================================================
            // PILIH SNACK
            // ======================================================

            switch (pilih) {

                case 1:

                    namaSnack =
                        "Popcorn Caramel";

                    harga =
                        45000;

                    break;

                case 2:

                    namaSnack =
                        "Popcorn Cheese";

                    harga =
                        40000;

                    break;

                case 3:

                    namaSnack =
                        "Coca Cola";

                    harga =
                        20000;

                    break;

                case 4:

                    namaSnack =
                        "Nachos Cheese";

                    harga =
                        35000;

                    break;

                case 5:

                    namaSnack =
                        "Combo Hemat";

                    harga =
                        60000;

                    break;

                default:

                    UI.error(
                        "Snack tidak tersedia!"
                    );

                    continue;
            }

            // ======================================================
            // TOTAL
            // ======================================================

            int total =
                harga * jumlah;

            // ======================================================
            // TUKAR POIN SNACK
            // ======================================================
            int poinUser = 0;
            try {
                Connection c = Koneksi.connect();
                PreparedStatement ps = c.prepareStatement("SELECT poin FROM users WHERE id_user=?");
                ps.setInt(1, User.currentUserId);
                ResultSet rs = ps.executeQuery();
                if(rs.next()) poinUser = rs.getInt("poin");
            } catch(Exception e){}

            int poinDipakai = 0;
            int nominalDiskonPoin = 0;

            if (poinUser >= 50 && total >= 25000) {
                int maxKupon = poinUser / 50;
                int maxPotongan = maxKupon * 25000;
                if (maxPotongan > total) maxPotongan = (total / 25000) * 25000;
                
                System.out.print(UI.YELLOW + "\nAnda memiliki " + poinUser + " Poin. Tukar kelipatan 50 Poin untuk diskon Rp 25.000? (y/n) : " + UI.RESET);
                input.nextLine(); // clear sisa enter dari jumlah
                String pakai = input.nextLine();
                if (pakai.equalsIgnoreCase("y")) {
                    System.out.print("Berapa kelipatan 50 poin yang ingin dipakai? (Maks " + (maxPotongan/25000) + ") : ");
                    int jumlahKupon = input.nextInt();
                    if (jumlahKupon > 0 && jumlahKupon <= (maxPotongan/25000)) {
                        poinDipakai = jumlahKupon * 50;
                        nominalDiskonPoin = jumlahKupon * 25000;
                        total -= nominalDiskonPoin;
                        System.out.println(UI.GREEN + poinDipakai + " Poin berhasil ditukar! (Diskon Rp " + nominalDiskonPoin + ")" + UI.RESET);
                    } else {
                        System.out.println(UI.RED + "Jumlah tidak valid, batal menggunakan poin." + UI.RESET);
                    }
                }
            }

            // ======================================================
            // PEMBAYARAN
            // ======================================================

            System.out.println(
                "\nTotal Bayar : Rp " + total
            );

            System.out.print(
                "Masukkan uang : Rp "
            );

            int bayar =
                input.nextInt();

            while (bayar < total) {

                UI.error(
                    "Uang anda kurang!"
                );

                System.out.print(
                    "\nMasukkan uang lagi : Rp "
                );

                bayar =
                    input.nextInt();
            }

            int kembalian =
                bayar - total;

            // ======================================================
            // LOADING
            // ======================================================

            System.out.print(
                UI.YELLOW +
                "\nMemproses pesanan"
                + UI.RESET
            );

            try {

                for (int i = 0; i < 5; i++) {

                    Thread.sleep(400);

                    System.out.print(".");
                }

            } catch (Exception e) {

                e.printStackTrace();
            }

            // ======================================================
            // SIMPAN DATABASE
            // ======================================================

            try {
                Connection c = Koneksi.connect();
                PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO pembelian_snack(user_id, nama_snack, jumlah, total_harga) VALUES (?, ?, ?, ?)"
                );
                ps.setInt(1, User.currentUserId);
                ps.setString(2, namaSnack);
                ps.setInt(3, jumlah);
                ps.setInt(4, total);
                ps.executeUpdate();

                // Tambah poin setelah beli snack
                int poinDidapat = jumlah * 5;
                int finalPoin = poinUser - poinDipakai + poinDidapat;
                PreparedStatement psPoint = c.prepareStatement("UPDATE users SET poin=? WHERE id_user=?");
                psPoint.setInt(1, finalPoin);
                psPoint.setInt(2, User.currentUserId);
                psPoint.executeUpdate();
                System.out.println(UI.GREEN + "\nAnda mendapatkan " + poinDidapat + " Poin dari pembelian snack ini!" + UI.RESET);

            } catch (Exception e) {
                System.err.println("Gagal mencatat riwayat ke database");
            }

            // ======================================================
            // STRUK SNACK
            // ======================================================

            System.out.println(UI.YELLOW);

            System.out.println(
                "\n\n========================================================"
            );

            System.out.println(
                "                    STRUK SNACK"
            );

            System.out.println(
                "========================================================"
            );

            System.out.println(UI.RESET);

            System.out.println(
                "Nama Snack   : " + namaSnack
            );

            System.out.println(
                "Jumlah       : " + jumlah
            );

            System.out.println(
                "Harga        : Rp " + harga
            );

            if (nominalDiskonPoin > 0) {
                System.out.println(
                    "Diskon Poin  : Rp " + nominalDiskonPoin
                );
            }

            System.out.println(
                "Total Bayar  : Rp " + total
            );

            System.out.println(
                "Uang Bayar   : Rp " + bayar
            );

            System.out.println(
                "Kembalian    : Rp " + kembalian
            );

            System.out.println(UI.YELLOW);

            System.out.println(
                "========================================================"
            );

            System.out.println(
                "         TERIMA KASIH TELAH MEMBELI"
            );

            System.out.println(
                "========================================================"
            );

            System.out.println(UI.RESET);
        }
    }

    // ======================================================
    // RIWAYAT SNACK
    // ======================================================

    public void riwayatSnack() {

        try {
            Connection c = Koneksi.connect();
            PreparedStatement ps = c.prepareStatement(
                "SELECT * FROM pembelian_snack WHERE user_id = ?"
            );
            ps.setInt(1, User.currentUserId);
            ResultSet rs = ps.executeQuery();

            System.out.println(UI.YELLOW + "\n======================================================");
            System.out.println("              RIWAYAT PEMBELIAN SNACK");
            System.out.println("======================================================" + UI.RESET);
            
            boolean ada = false;
            while (rs.next()) {
                ada = true;
                System.out.println("- " + rs.getString("nama_snack") + " (x" + rs.getInt("jumlah") + ")");
                System.out.println("  Total  : Rp " + rs.getInt("total_harga") + "\n");
            }
            
            if (!ada) {
                System.out.println("Anda belum pernah membeli snack.");
            }

        } catch (Exception e) {
            System.err.println("Gagal memuat riwayat snack.");
        }
    }
}