import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class TiketBioskop {

    Scanner input =
        new Scanner(System.in);

    // ======================================================
    // BOOKING TIKET
    // ======================================================

    public void booking() {

        try {

            Connection c =
                Koneksi.connect();

            // ======================================================
            // HEADER
            // ======================================================

            System.out.println(UI.CYAN);

            System.out.println(
                "\n============================================================================"
            );

            System.out.println(
                "                         BOOKING TIKET BIOSKOP"
            );

            System.out.println(
                "============================================================================"
            );

            System.out.println(UI.RESET);

            // ======================================================
            // LIST FILM
            // ======================================================

            Film f =
                new Film();

            f.listFilmBooking();

            // ======================================================
            // PILIH FILM
            // ======================================================

            System.out.print(
                "\nPilih ID Film : "
            );

            int filmId =
                input.nextInt();

            // ======================================================
            // PILIH STUDIO
            // ======================================================

            System.out.println(
                "\n============================================================================"
            );

            System.out.println(
                "                              PILIH STUDIO"
            );

            System.out.println(
                "============================================================================"
            );

            System.out.println(
                "1. REGULAR"
            );

            System.out.println(
                "2. PREMIERE"
            );

            System.out.println(
                "3. IMAX"
            );

            System.out.print(
                "\nPilih tipe studio : "
            );

            int tipeStudio = input.nextInt();
            int studioId = 0;
            
            if (tipeStudio == 1) {
                System.out.print("Pilih nomor Studio Regular (1 - 10) : ");
                studioId = input.nextInt();
                if (studioId < 1 || studioId > 10) { UI.error("Studio Regular hanya 1-10!"); return; }
            } else if (tipeStudio == 2) {
                System.out.print("Pilih nomor Studio Premiere (11 - 20) : ");
                studioId = input.nextInt();
                if (studioId < 11 || studioId > 20) { UI.error("Studio Premiere hanya 11-20!"); return; }
            } else if (tipeStudio == 3) {
                System.out.print("Pilih nomor Studio IMAX (21 - 23) : ");
                studioId = input.nextInt();
                if (studioId < 21 || studioId > 23) { UI.error("Studio IMAX hanya 21-23!"); return; }
            } else {
                UI.error("Tipe studio tidak valid!");
                return;
            }

            System.out.println("\nPilih Jam Tayang:");
            System.out.println("1. 10:00  |  2. 13:00  |  3. 16:00  |  4. 19:00");
            System.out.print("Pilih jam : ");
            int jamPil = input.nextInt();
            String jamTayang = (jamPil == 1) ? "10:00" : (jamPil == 2) ? "13:00" : (jamPil == 3) ? "16:00" : "19:00";
            input.nextLine();

            // ======================================================
            // CEK IMAX CONSTRAINT
            // ======================================================
            if (tipeStudio == 3) {
                PreparedStatement cekImax = c.prepareStatement("SELECT is_imax FROM film WHERE id_film = ?");
                cekImax.setInt(1, filmId);
                ResultSet rsImax = cekImax.executeQuery();
                if (rsImax.next()) {
                    boolean isImax = rsImax.getBoolean("is_imax");
                    if (!isImax) {
                        UI.error("Mohon maaf, film ini tidak didukung untuk tayang di Studio IMAX!");
                        System.out.println(UI.YELLOW + "Silahkan ulangi proses booking dan pilih studio Regular atau Premiere.\n" + UI.RESET);
                        return;
                    }
                }
            }

            // ======================================================
            // LIST KURSI DIPILIH
            // ======================================================

            ArrayList<String> kursiDipilih =
                new ArrayList<>();

            // ======================================================
            // JUMLAH TIKET
            // ======================================================

            System.out.print(
                "\nJumlah tiket : "
            );

            int jumlahTiket =
                input.nextInt();

            input.nextLine();

            String semuaKursi = "";

            int totalHarga = 0;
            
            int hargaTiketPertama = 0;

            // ======================================================
            // PILIH KURSI
            // ======================================================

            for (
                int x = 1;
                x <= jumlahTiket;
                x++
            ) {

                tampilKursi(
                    c,
                    filmId,
                    studioId,
                    jamTayang,
                    kursiDipilih
                );

                System.out.print(
                    "\nPilih kursi ke-" + x + " : "
                );

                String kursi =
                    input.nextLine().toUpperCase();

                // ======================================================
                // VALIDASI KURSI
                // ======================================================

                if (!kursi.matches("^[A-H]([1-9]|10)$")) {
                    UI.error("Format kursi tidak valid! (Gunakan baris A-H dan angka 1-10. Contoh: C5)");
                    x--;
                    continue;
                }

                if (kursi.startsWith("A") && !(kursi.equals("A1") || kursi.equals("A3") || kursi.equals("A5") || kursi.equals("A7") || kursi.equals("A9"))) {
                    UI.error("Kursi SweetBox (Baris A) hanya bisa dipilih menggunakan A1, A3, A5, A7, atau A9!");
                    x--;
                    continue;
                }

                // ======================================================
                // AUTO SWEETBOX
                // ======================================================

                if (kursi.equals("A1")) {

                    kursi = "A1-A2";

                } else if (kursi.equals("A3")) {

                    kursi = "A3-A4";

                } else if (kursi.equals("A5")) {

                    kursi = "A5-A6";

                } else if (kursi.equals("A7")) {

                    kursi = "A7-A8";

                } else if (kursi.equals("A9")) {

                    kursi = "A9-A10";
                }

                // ======================================================
                // CEK SUDAH DIPILIH
                // ======================================================

                if (
                    kursiDipilih.contains(kursi)
                ) {

                    UI.error(
                        "Kursi sudah dipilih!"
                    );

                    x--;

                    continue;
                }

                // ======================================================
                // CEK DATABASE
                // ======================================================

                PreparedStatement cek =
                    c.prepareStatement(
                        "SELECT * FROM tiket_bioskop WHERE film_id=? AND studio_id=? AND kursi=? AND jam_tayang=?"
                    );

                cek.setInt(1, filmId);
                cek.setInt(2, studioId);
                cek.setString(3, kursi);
                cek.setString(4, jamTayang);

                ResultSet rs =
                    cek.executeQuery();

                if (rs.next()) {

                    UI.error(
                        "Kursi sudah dibooking!"
                    );

                    x--;

                    continue;
                }

                // ======================================================
                // TAMBAH LIST
                // ======================================================

                kursiDipilih.add(kursi);

                // ======================================================
                // HARGA
                // ======================================================

                int harga = 50000;

                // SWEETBOX
                if (
                    kursi.startsWith("A")
                ) {

                    harga = 150000;
                }

                // PREMIERE
                if (tipeStudio == 2) {

                    harga += 50000;
                }

                // IMAX
                if (tipeStudio == 3) {

                    harga += 75000;
                }

                totalHarga += harga;
                
                if (x == 1) {
                    hargaTiketPertama = harga;
                }

                // ======================================================
                // GABUNG KURSI
                // ======================================================

                if (x == 1) {

                    semuaKursi = kursi;

                } else {

                    semuaKursi += ", " + kursi;
                }

                System.out.println(
                    UI.GREEN +
                    "\nKursi " + kursi + " berhasil dipilih!"
                    + UI.RESET
                );
            }
            
            // ======================================================
            // CEK POIN DARI DATABASE
            // ======================================================
            int poinUser = 0;
            PreparedStatement psUser = c.prepareStatement("SELECT poin FROM users WHERE id_user=?");
            psUser.setInt(1, User.currentUserId);
            ResultSet rsCekUser = psUser.executeQuery();
            if(rsCekUser.next()){
                poinUser = rsCekUser.getInt("poin");
            }

            int poinDipakai = 0;
            int nominalDiskonPoin = 0;
            
            if (poinUser >= 100) {
                int maxKupon = poinUser / 100;
                int maxPotongan = maxKupon * 50000;
                if (maxPotongan > totalHarga) maxPotongan = (totalHarga / 50000) * 50000;
                
                System.out.print(UI.YELLOW + "\nAnda memiliki " + poinUser + " Poin. Tukar kelipatan 100 Poin untuk diskon Rp 50.000? (y/n) : " + UI.RESET);
                String pakai = input.nextLine();
                if (pakai.equalsIgnoreCase("y")) {
                    System.out.print("Berapa kelipatan 100 poin yang ingin dipakai? (Maks " + (maxPotongan / 50000) + ") : ");
                    int jumlahKupon = input.nextInt();
                    input.nextLine();
                    if (jumlahKupon > 0 && jumlahKupon <= (maxPotongan / 50000)) {
                        poinDipakai = jumlahKupon * 100;
                        nominalDiskonPoin = jumlahKupon * 50000;
                        totalHarga -= nominalDiskonPoin;
                        System.out.println(UI.GREEN + poinDipakai + " Poin berhasil ditukar! (Diskon Rp " + nominalDiskonPoin + ")" + UI.RESET);
                    } else {
                        System.out.println(UI.RED + "Jumlah tidak valid, batal menggunakan poin." + UI.RESET);
                    }
                }
            }

            // ======================================================
            // PEMBAYARAN
            // ======================================================

            System.out.println(UI.CYAN);

            System.out.println(
                "\n============================================================================"
            );

            System.out.println(
                "                         METODE PEMBAYARAN"
            );

            System.out.println(
                "============================================================================"
            );

            System.out.println(UI.RESET);

            System.out.println("1. QRIS");
            System.out.println("2. DANA");
            System.out.println("3. OVO");
            System.out.println("4. GOPAY");
            System.out.println("5. CASH");

            System.out.print(
                "\nPilih pembayaran : "
            );

            int pilihBayar =
                input.nextInt();

            input.nextLine();

            String pembayaran = "";

            switch (pilihBayar) {

                case 1:

                    pembayaran = "QRIS";
                    QRCode.tampil();
                    System.out.println(UI.YELLOW + "\nSilahkan scan QR Code di atas." + UI.RESET);
                    System.out.print("Tekan ENTER jika sudah membayar...");
                    input.nextLine();

                    break;

                case 2:

                    pembayaran = "DANA";

                    break;

                case 3:

                    pembayaran = "OVO";

                    break;

                case 4:

                    pembayaran = "GOPAY";

                    break;

                case 5:

                    pembayaran = "CASH";

                    break;

                default:

                    UI.error(
                        "Metode pembayaran tidak valid!"
                    );

                    return;
            }

            // ======================================================
            // TOTAL
            // ======================================================

            int biayaLayanan =
                2500;

            int totalPembayaran =
                totalHarga + biayaLayanan;

            System.out.println(
                "\nTotal Harga      : Rp "
                + totalHarga
            );

            System.out.println(
                "Biaya Layanan    : Rp "
                + biayaLayanan
            );

            System.out.println(
                "Total Pembayaran : Rp "
                + totalPembayaran
            );

            int bayar = 0;
            
            if (pembayaran.equals("QRIS")) {
                bayar = totalPembayaran;
                System.out.println("\nPembayaran QRIS otomatis lunas: Rp " + bayar);
            } else {
                System.out.print(
                    "\nMasukkan uang : Rp "
                );

                bayar =
                    input.nextInt();

                while (bayar < totalPembayaran) {

                    UI.error(
                        "Uang anda kurang!"
                    );

                    System.out.print(
                        "\nMasukkan uang lagi : Rp "
                    );

                    bayar =
                        input.nextInt();
                }
            }

            int kembalian =
                bayar - totalPembayaran;

            // ======================================================
            // SIMPAN DATABASE
            // ======================================================

            String[] listKursi =
                semuaKursi.split(", ");

            for (String kursi : listKursi) {

                int harga = 50000;

                if (
                    kursi.startsWith("A")
                ) {

                    harga = 150000;
                }

                if (tipeStudio == 2) {

                    harga += 50000;
                }

                if (tipeStudio == 3) {

                    harga += 75000;
                }

                PreparedStatement ps =
                    c.prepareStatement(
                        "INSERT INTO tiket_bioskop(user_id,film_id,studio_id,kursi,pembayaran,harga,jam_tayang) VALUES (?,?,?,?,?,?,?)"
                    );

                ps.setInt(1, User.currentUserId);
                ps.setInt(2, filmId);
                ps.setInt(3, studioId);
                ps.setString(4, kursi);
                ps.setString(5, pembayaran);
                ps.setInt(6, harga);
                ps.setString(7, jamTayang);

                ps.executeUpdate();
            }

            // ======================================================
            // UPDATE POIN MEMBER
            // ======================================================
            int poinDidapat = jumlahTiket * 10;
            int finalPoin = poinUser - poinDipakai + poinDidapat;

            PreparedStatement psUpdate = c.prepareStatement("UPDATE users SET poin=? WHERE id_user=?");
            psUpdate.setInt(1, finalPoin);
            psUpdate.setInt(2, User.currentUserId);
            psUpdate.executeUpdate();

            System.out.println(UI.GREEN + "\nSelamat! Anda mendapatkan " + poinDidapat + " Poin dari transaksi ini!" + UI.RESET);

            // ======================================================
            // CETAK TIKET
            // ======================================================

            cetakTiket(
                filmId,
                studioId,
                semuaKursi,
                jamTayang,
                pembayaran,
                totalHarga,
                biayaLayanan,
                totalPembayaran,
                bayar,
                kembalian,
                nominalDiskonPoin
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // ======================================================
    // TAMPIL KURSI
    // ======================================================

    public void tampilKursi(
        Connection c,
        int filmId,
        int studioId,
        String jamTayang,
        ArrayList<String> kursiDipilih
    ) {

        try {

            System.out.println(UI.CYAN);

            System.out.println(
                "\n============================================================================"
            );

            System.out.println(
                "                                 SCREEN"
            );

            System.out.println(
                "============================================================================"
            );

            System.out.println(UI.RESET);

            String studio = "";

            if (studioId >= 1 && studioId <= 10) {

                studio =
                    "STUDIO REGULAR " + studioId;

            } else if (studioId >= 11 && studioId <= 20) {

                studio =
                    "STUDIO PREMIERE " + (studioId - 10);

            } else {

                studio =
                    "STUDIO IMAX " + (studioId - 20);
            }

            System.out.println(
                UI.PINK +
                "\n" + studio
                + UI.RESET
            );

            System.out.println(
                "\nKeterangan:"
            );

            System.out.println(
                UI.BLUE +
                "■ "
                + UI.RESET
                + "Regular"
            );

            System.out.println(
                UI.PINK +
                "■ "
                + UI.RESET
                + "SweetBox"
            );

            System.out.println(
                UI.WHITE +
                "■ "
                + UI.RESET
                + "Sudah Dibooking"
            );

            char[] rows = {
                'H','G','F','E',
                'D','C','B','A'
            };

            for (char row : rows) {

                System.out.print(
                    "\n " + row + "   "
                );

                // ======================================================
                // SWEETBOX
                // ======================================================

                if (row == 'A') {

                    for (
                        int j = 1;
                        j <= 10;
                        j += 2
                    ) {

                        String kursi1 =
                            row + "" + j;

                        String kursi2 =
                            row + "" + (j + 1);

                        String kursi =
                            kursi1 + "-" + kursi2;

                        PreparedStatement ps =
                            c.prepareStatement(
                                "SELECT * FROM tiket_bioskop WHERE film_id=? AND studio_id=? AND kursi=? AND jam_tayang=?"
                            );

                        ps.setInt(1, filmId);
                        ps.setInt(2, studioId);
                        ps.setString(3, kursi);
                        ps.setString(4, jamTayang);

                        ResultSet rs =
                            ps.executeQuery();

                        if (
                            rs.next()
                            ||
                            kursiDipilih.contains(kursi)
                        ) {

                            System.out.print(
                                UI.WHITE +
                                "[" + kursi + "] "
                                + UI.RESET
                            );

                        } else {

                            System.out.print(
                                UI.PINK +
                                "[" + kursi + "] "
                                + UI.RESET
                            );
                        }
                    }
                }

                // ======================================================
                // REGULAR
                // ======================================================

                else {

                    for (
                        int j = 1;
                        j <= 10;
                        j++
                    ) {

                        String kursi =
                            row + "" + j;

                        PreparedStatement ps =
                            c.prepareStatement(
                                "SELECT * FROM tiket_bioskop WHERE film_id=? AND studio_id=? AND kursi=? AND jam_tayang=?"
                            );

                        ps.setInt(1, filmId);
                        ps.setInt(2, studioId);
                        ps.setString(3, kursi);
                        ps.setString(4, jamTayang);

                        ResultSet rs =
                            ps.executeQuery();

                        if (j == 6) {

                            System.out.print(
                                "       "
                            );
                        }

                        if (
                            rs.next()
                            ||
                            kursiDipilih.contains(kursi)
                        ) {

                            System.out.print(
                                UI.WHITE +
                                "[" + kursi + "] "
                                + UI.RESET
                            );

                        } else {

                            System.out.print(
                                UI.BLUE +
                                "[" + kursi + "] "
                                + UI.RESET
                            );
                        }
                    }
                }

                System.out.println();
            }

            System.out.println(UI.CYAN);

            System.out.println(
                "\n============================================================================"
            );

            System.out.println(UI.RESET);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // ======================================================
    // CETAK TIKET
    // ======================================================

    public void cetakTiket(
        int filmId,
        int studioId,
        String semuaKursi,
        String jamTayang,
        String pembayaran,
        int totalHarga,
        int biayaLayanan,
        int totalPembayaran,
        int bayar,
        int kembalian,
        int diskonPoin
    ) {

        String[] listKursi = semuaKursi.split(", ");

        for (String kursi : listKursi) {

            int hargaTiket = 50000;
            if (kursi.startsWith("A")) hargaTiket = 150000;
            if (studioId >= 11 && studioId <= 20) hargaTiket += 50000;
            if (studioId >= 21 && studioId <= 23) hargaTiket += 75000;

            System.out.println(UI.CYAN);

            System.out.println(
                "\n======================================================"
            );

            System.out.println(
                "                 BIOSKOP M-TIX"
            );

            System.out.println(
                "              PREMIUM CINEMA E-TICKET"
            );

            System.out.println(
                "======================================================"
            );

            System.out.println(UI.RESET);

            System.out.println(
                "FILM              : " + filmId
            );

            System.out.println(
                "STUDIO            : " + studioId
            );

            System.out.println(
                "JAM TAYANG        : " + jamTayang
            );

            System.out.println(
                "KURSI             : " + kursi
            );

            System.out.println(
                "METODE PEMBAYARAN : " + pembayaran
            );
            
            System.out.println(
                "HARGA TIKET       : Rp " + hargaTiket
            );

            System.out.println(
                "\n--- RINGKASAN TRANSAKSI SEMUA TIKET ---"
            );

            if (diskonPoin > 0) {
                System.out.println(
                    "DISKON TUKAR POIN : Rp " + diskonPoin
                );
            }

            System.out.println(
                "TOTAL HARGA       : Rp " + totalHarga
            );

            System.out.println(
                "BIAYA LAYANAN     : Rp " + biayaLayanan
            );

            System.out.println(
                "TOTAL PEMBAYARAN  : Rp " + totalPembayaran
            );

            System.out.println(
                "BAYAR             : Rp " + bayar
            );

            System.out.println(
                "KEMBALIAN         : Rp " + kembalian
            );

            System.out.println(UI.CYAN);

            System.out.println(
                "\n======================================================"
            );

            System.out.println(
                "      IMAX | DOLBY ATMOS | SWEETBOX"
            );

            System.out.println(
                "======================================================"
            );

            System.out.println(UI.RESET);
        }
    }

    // ======================================================
    // RIWAYAT TIKET
    // ======================================================

    public void riwayatTiket() {

        try {
            Connection c = Koneksi.connect();
            PreparedStatement ps = c.prepareStatement(
                "SELECT t.kursi, t.pembayaran, t.harga, t.jam_tayang, t.studio_id, f.judul, f.is_imax " +
                "FROM tiket_bioskop t " +
                "JOIN film f ON t.film_id = f.id_film " +
                "WHERE t.user_id = ?"
            );
            ps.setInt(1, User.currentUserId);
            ResultSet rs = ps.executeQuery();

            System.out.println(UI.CYAN + "\n======================================================");
            System.out.println("              RIWAYAT PEMBELIAN TIKET");
            System.out.println("======================================================" + UI.RESET);
            
            boolean ada = false;
            while (rs.next()) {
                ada = true;
                String judul = rs.getString("judul");
                if (rs.getBoolean("is_imax")) judul += " [IMAX]";

                System.out.println("- Film   : " + judul);
                System.out.println("  Jadwal : " + rs.getString("jam_tayang") + " | Studio " + rs.getInt("studio_id"));
                System.out.println("  Kursi  : " + rs.getString("kursi") + " | Bayar: " + rs.getString("pembayaran"));
                System.out.println();
            }
            
            if (!ada) {
                System.out.println("Anda belum pernah membeli tiket.");
            }

        } catch (Exception e) {
            System.err.println("Gagal memuat riwayat tiket.");
        }
    }
}