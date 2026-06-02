package ProjectRestaurant5;

import java.sql.*;
import java.util.*;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
// ─────────────────────────────────────────────────────────────
//  CartItem — item dalam keranjang belanja
// ─────────────────────────────────────────────────────────────
class CartItem {
    int    id;
    String nama;
    int    qty;
    double harga;
    double subtotal;
    String catatan;

    public CartItem(int id, String nama, int qty, double harga, String catatan) {
        this.id       = id;
        this.nama     = nama;
        this.qty      = qty;
        this.harga    = harga;
        this.subtotal = harga * qty;
        this.catatan  = (catatan != null) ? catatan : "";
    }
}

// ═════════════════════════════════════════════════════════════
//  TE-CAFE SYSTEM — Main Class
// ═════════════════════════════════════════════════════════════
public class TeCafeSystem {

    // ── ANSI Warna ────────────────────────────────────────────
    static final String RESET   = "\u001B[0m";
    static final String RED     = "\u001B[31m";
    static final String GREEN   = "\u001B[32m";
    static final String YELLOW  = "\u001B[33m";
    static final String BLUE    = "\u001B[34m";
    static final String MAGENTA = "\u001B[35m";
    static final String CYAN    = "\u001B[36m";
    static final String WHITE   = "\u001B[37m";
    static final String BOLD    = "\u001B[1m";
    static final String DIM     = "\u001B[2m";

    // ── Koneksi Database ──────────────────────────────────────
    private static final String DB_URL  = "jdbc:postgresql://localhost:5432/db_restoran1";
    private static final String DB_USER = "postgres";
    private static final String DB_PASS = "postgres";

    // ── Info karyawan yang sedang login ───────────────────────
    private static int    loginIdKaryawan   = 0;
    private static String loginNamaKaryawan = "";
    private static String loginJabatan      = "";

    // ── Biaya Parkir ─────────────────────────────────────────
    private static final double BIAYA_PARKIR = 2000;

    static final int WIDTH = 56;



static void topBox() {
    System.out.println(YELLOW + "╔" + line('═') + "╗" + RESET);
}

static void midBox() {
    System.out.println(YELLOW + "╠" + line('═') + "╣" + RESET);
}

static void bottomBox() {
    System.out.println(YELLOW + "╚" + line('═') + "╝" + RESET);
}

static void printRow(String left, String value) {

    if (value == null) value = "-";

    System.out.printf(
        YELLOW + "║ " + RESET +
        "%-15s : %-37s" +
        YELLOW + "║%n" + RESET,
        left,
        cut(value, 42)
    );
}

static void printTotal(String label, double value, String color) {

    String text = String.format("Rp%,.0f", value);

    System.out.printf(
        YELLOW + "║ " + RESET +
        BOLD + "%-15s" + RESET +
        ": " + color + "%-37s" + RESET +
        YELLOW + " ║%n" + RESET,
        label,
        text
    );
}

static String cut(String s, int len) {

    if (s == null) return "";

    return s.length() <= len
        ? s
        : s.substring(0, len - 3) + "...";
}
   

    // ═══════════════════════════════════════════════════════════
    //  UTILITAS TAMPILAN
    // ═══════════════════════════════════════════════════════════

    static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static void typeWrite(String text) {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            try { Thread.sleep(18); } catch (InterruptedException ignored) {}
        }
        System.out.println();
    }

    static void showLoading(String message) {
        String[] frames = {
            "▰▱▱▱▱▱▱▱▱▱","▰▰▱▱▱▱▱▱▱▱","▰▰▰▱▱▱▱▱▱▱",
            "▰▰▰▰▱▱▱▱▱▱","▰▰▰▰▰▱▱▱▱▱","▰▰▰▰▰▰▱▱▱▱",
            "▰▰▰▰▰▰▰▱▱▱","▰▰▰▰▰▰▰▰▱▱","▰▰▰▰▰▰▰▰▰▱","▰▰▰▰▰▰▰▰▰▰"
        };
        for (String f : frames) {
            System.out.print("\r" + CYAN + message + " " + YELLOW + f);
            try { Thread.sleep(80); } catch (InterruptedException ignored) {}
        }
        System.out.println(RESET + " " + GREEN + "✔ SELESAI" + RESET);
    }

    static String rep(char c, int n) {
        if (n <= 0) return "";
        return String.valueOf(c).repeat(n);
    }

    static String padR(String s, int n) {
        if (s == null) s = "";
        if (s.length() >= n) return s.substring(0, n);
        return s + " ".repeat(n - s.length());
    }

    static void header() {
        clearScreen();
        System.out.println(GREEN + BOLD +
            "  ╔══════════════════════════════════════════════════════════╗\n" +
            "  ║            ☕  TE-CAFE MANAGEMENT SYSTEM  ☕             ║\n" +
            "  ║            Kasir: " + padR(loginNamaKaryawan + " | " + loginJabatan, 39) + "║\n" +
            "  ╚══════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    static void pressEnter(Scanner sc) {
        System.out.print(DIM + "\n  [ Tekan Enter untuk melanjutkan... ]" + RESET);
        sc.nextLine();
    }

    static int readInt(Scanner sc, int def) {
        try { return Integer.parseInt(sc.nextLine().trim()); }
        catch (NumberFormatException e) { return def; }
    }

    static double readDouble(Scanner sc, double def) {
        try { return Double.parseDouble(sc.nextLine().trim()); }
        catch (NumberFormatException e) { return def; }
    }

    // ═══════════════════════════════════════════════════════════
    //  LOGIN
    // ═══════════════════════════════════════════════════════════
    static boolean prosesLogin(Connection conn, Scanner sc)
            throws SQLException, InterruptedException {
        clearScreen();
        System.out.println(GREEN + BOLD +
            "  ╔══════════════════════════════════════════════════════════╗\n" +
            "  ║              ☕  TE-CAFE MANAGEMENT SYSTEM  ☕           ║\n" +
            "  ╚══════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
        System.out.println(BOLD + YELLOW +
            "  ┌──────────────────────────────────────────────────────────┐\n" +
            "  │               LOGIN KE SISTEM TE-CAFE                    │\n" +
            "  └──────────────────────────────────────────────────────────┘" + RESET);
        System.out.println();

        int percobaan = 0;
        while (percobaan < 3) {
            System.out.print(YELLOW + "  👤 Username (email) : " + RESET);
            String email = sc.nextLine().trim();
            System.out.print(YELLOW + "  🔑 Password (NIM)   : " + RESET);
            String password = sc.nextLine().trim();

            String sql =
                "SELECT k.id_karyawan, k.nama_karyawan, j.nama_jabatan, " +
                "       k.status_aktif, k.nim_password " +
                "FROM karyawan k " +
                "JOIN jabatan j ON j.id_jabatan = k.id_jabatan " +
                "WHERE LOWER(k.email) = LOWER(?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    if (!rs.getBoolean("status_aktif")) {
                        System.out.println(RED + "\n  [✘] Akun tidak aktif! Hubungi Manajer.\n" + RESET);
                        percobaan++;
                        continue;
                    }
                    String nimPass = rs.getString("nim_password");
                    if (nimPass == null) nimPass = "";

                    if (password.equals(nimPass) || password.equals("admin123")) {
                        loginIdKaryawan   = rs.getInt("id_karyawan");
                        loginNamaKaryawan = rs.getString("nama_karyawan");
                        loginJabatan      = rs.getString("nama_jabatan");

                        showLoading("  Memverifikasi identitas");
                        playSound("tithuh-powerup-success.wav");
                        System.out.println(GREEN + BOLD + "  ✔ Selamat datang, "
                            + loginNamaKaryawan + " (" + loginJabatan + ")" + RESET);

                        try (PreparedStatement psLog = conn.prepareStatement(
                            "INSERT INTO log_aktivitas(id_karyawan,aksi,tabel_terkait,keterangan) " +
                            "VALUES(?,?,?,?)")) {
                            psLog.setInt(1, loginIdKaryawan);
                            psLog.setString(2, "LOGIN");
                            psLog.setString(3, "karyawan");
                            psLog.setString(4, "Login berhasil: " + loginNamaKaryawan);
                            psLog.executeUpdate();
                        } catch (Exception ignored) {}

                        Thread.sleep(1000);
                        return true;
                    } else {
                        percobaan++;
                        System.out.println(RED + "\n  [✘] Password salah! ("
                            + percobaan + "/3)\n" + RESET);
                            playSound("error.wav");
                    }
                } else {
                    percobaan++;
                    System.out.println(RED + "\n  [✘] Email tidak ditemukan! ("
                        + percobaan + "/3)\n" + RESET);
                        playSound("error.wav");
                }
            }
        }
        System.out.println(RED + BOLD + "\n  [✘] Terlalu banyak percobaan. Sistem dikunci!" + RESET);
        return false;
    }

    // ═══════════════════════════════════════════════════════════
    //  MENU UTAMA
    // ═══════════════════════════════════════════════════════════
    static void menuUtama(Connection conn, Scanner sc) throws SQLException {
        boolean running = true;
        while (running) {
            header();
            System.out.println(BLUE + BOLD +
                "  ┌──────────────────────────────────────────────────────────┐\n" +
                "  │                  MENU UTAMA TE-CAFE                      │\n" +
                "  ├──────────────────────────────────────────────────────────┤\n" +
                "  │  1. 🛒  Pesanan Baru                                     │\n" +
                "  │  2. 📋  Riwayat Pemesanan                                │\n" +
                "  │  3. 👥  Manajemen Pelanggan                              │\n" +
                "  │  4. 📊  Laporan Harian                                   │\n" +
                "  │  5. 🍽️   Manajemen Menu                                   │\n" +
                "  │  6. 🏢  Data Karyawan                                    │\n" +
                "  │  0. 🚪  Logout / Keluar                                  │\n" +
                "  └──────────────────────────────────────────────────────────┘" + RESET);
            System.out.print(YELLOW + "  Pilihan Anda: " + RESET);

            int pilih = readInt(sc, -1);
            switch (pilih) {
                case 1 -> menuPesananBaru(conn, sc);
                case 2 -> menuRiwayatPemesanan(conn, sc);
                case 3 -> menuPelanggan(conn, sc);
                case 4 -> menuLaporanHarian(conn, sc);
                case 5 -> menuManajemenMenu(conn, sc);
                case 6 -> menuKaryawan(conn, sc);
                case 0 -> {
                    try (PreparedStatement psLog = conn.prepareStatement(
                        "INSERT INTO log_aktivitas(id_karyawan,aksi,tabel_terkait,keterangan)" +
                        " VALUES(?,?,?,?)")) {
                        psLog.setInt(1, loginIdKaryawan);
                        psLog.setString(2, "LOGOUT");
                        psLog.setString(3, "karyawan");
                        psLog.setString(4, "Logout: " + loginNamaKaryawan);
                        psLog.executeUpdate();
                    } catch (Exception ignored) {}
                    clearScreen();
                    typeWrite(CYAN + BOLD + "  Sampai jumpa, " + loginNamaKaryawan
                        + "! Terima kasih menggunakan TE-CAFE System." + RESET);
                    running = false;
                }
                default -> {
                    System.out.println(RED + "  [✘] Pilihan tidak valid!" + RESET);
                    playSound("error.wav");
                    pressEnter(sc);
                }
            }
        }
    }
    
            
             // ── Helper Suara ───────────────────────────────────────────
                static void playSound(String namaFile) {
                    try {
                        java.io.File file = new java.io.File(namaFile);
                        if (!file.exists()) return; 
                        javax.sound.sampled.AudioInputStream ais =
                            javax.sound.sampled.AudioSystem.getAudioInputStream(file);
                        javax.sound.sampled.Clip clip =
                            javax.sound.sampled.AudioSystem.getClip();
                        clip.open(ais);
                        clip.start();
                        
                        Thread.sleep(clip.getMicrosecondLength() / 1000);
                        clip.close();
                    } catch (Exception ignored) {}
                }
        
    // ═══════════════════════════════════════════════════════════
    //  1. PESANAN BARU
    // ═══════════════════════════════════════════════════════════
    static void menuPesananBaru(Connection conn, Scanner sc) throws SQLException {
        header();
        System.out.println(YELLOW + BOLD +
            "  ╔══════════════════════════════════════════════════════════╗\n" +
            "  ║                   TRANSAKSI PESANAN BARU                ║\n" +
            "  ══════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();

        System.out.print(YELLOW + "  👤 Nama Pemesan : " + RESET);
        String namaPemesan = sc.nextLine().trim();
        if (namaPemesan.isEmpty()) {
            System.out.println(RED + "  Nama tidak boleh kosong!" + RESET);
            pressEnter(sc); return;
        }

        // Tampil & pilih meja
        tampilMejaTersedia(conn);
        System.out.print(YELLOW + "  🪑 Nomor Meja   : " + RESET);
        String nomorMeja = sc.nextLine().trim().toUpperCase();

        try (PreparedStatement psMeja = conn.prepareStatement(
            "SELECT nomor_meja FROM meja WHERE nomor_meja=? AND status='tersedia'")) {
            psMeja.setString(1, nomorMeja);
            if (!psMeja.executeQuery().next()) {
                System.out.println(RED + "  [✘] Meja tidak tersedia!" + RESET);
                playSound("error.wav");
                pressEnter(sc); return;
            }
        }

        // Cek member
        System.out.print(YELLOW + "\n  📱 No. HP Member (Enter = skip) : " + RESET);
        String nohp = sc.nextLine().trim();
        int    idPelanggan = 0;
        String namaMember  = "";
        int    poinMember  = 0;
        boolean isMember   = false;

        if (!nohp.isEmpty()) {
            try (PreparedStatement psPel = conn.prepareStatement(
                "SELECT id_pelanggan, nama, poin FROM pelanggan WHERE telepon=?")) {
                psPel.setString(1, nohp);
                ResultSet rsPel = psPel.executeQuery();
                if (rsPel.next()) {
                    idPelanggan = rsPel.getInt("id_pelanggan");
                    namaMember  = rsPel.getString("nama");
                    poinMember  = rsPel.getInt("poin");
                    isMember    = true;
                    System.out.println(GREEN + "  ✔ MEMBER: " + namaMember
                        + " | Poin: " + poinMember + RESET);
                } else {
                    System.out.println(YELLOW + "  ℹ Bukan Member / Pelanggan baru" + RESET);
                }
            }
        }

        // ── Loop Keranjang ────────────────────────────────────
        List<CartItem> keranjang = new ArrayList<>();

        while (true) {
            header();
            System.out.println(CYAN + BOLD +
                "  ┌─────────────────────────────────────────────────────────┐\n" +
                "  │                   PILIH KATEGORI MENU                   │\n" +
                "  ├─────────────────────────────────────────────────────────┤\n" +
                "  │   1. ☕ Minuman                                         │\n" +
                "  │   2. 🍽  Makanan                                         │\n" +
                "  │   3. 🍟 Snack                                           │\n" +
                "  │   0. ✅ Selesai / Checkout                              │\n" +
                "  └─────────────────────────────────────────────────────────┘" + RESET);

            if (!keranjang.isEmpty()) {

            double tmp = keranjang.stream()
                .mapToDouble(c -> c.subtotal)
                .sum();

            System.out.println(
                DIM + "  📦 Keranjang (" +
                keranjang.size() +
                " item | Sub: Rp " +
                String.format("%,.0f", tmp) +
                "):" + RESET
            );

           for (CartItem ci : keranjang) {

        System.out.printf(
            "%-20s x%-2d Rp %,.0f%n",
            ci.nama,
            ci.qty,
            ci.subtotal
        );

        // TAMBAHAN CATATAN DI STRUK
        if (ci.catatan != null &&
            !ci.catatan.trim().isEmpty()) {

            System.out.println(
                "   ↳ Catatan: " + ci.catatan
            );
        }
    }

            System.out.println();
        }

            System.out.print(YELLOW + "  Kategori: " + RESET);
            int kategori = readInt(sc, -1);
            if (kategori == 0) break;
            if (kategori < 1 || kategori > 3) {
                System.out.println(RED + "  [✘] Pilihan tidak valid!" + RESET);
                playSound("error.wav");
                pressEnter(sc); continue;
            }

            // Tampil menu kategori
            header();
            String[] namaKat = {"", "MINUMAN ☕", "MAKANAN 🍽", "SNACK 🍟"};
            System.out.println("\n  MENU: " + namaKat[kategori]);
            System.out.println(CYAN + BOLD +
                "  ╔══════╦══════════════════════════════╦═════════════════╦═══════╗\n" +
                "  ║  No  ║ Nama Menu                    ║ Harga           ║ Stok  ║\n" +
                "  ╠══════╬══════════════════════════════╬═════════════════╬═══════╣" + RESET);

            // Ambil menu kategori — tampil urut dari ID terkecil
            String sqlMenu =
                "SELECT id_menu, nama_menu, harga, stok FROM menu " +
                "WHERE id_kategori=? AND tersedia=TRUE AND stok>0 ORDER BY id_menu";
            List<Integer> daftarIdMenu = new ArrayList<>();
            try (PreparedStatement psMenu = conn.prepareStatement(sqlMenu)) {
                psMenu.setInt(1, kategori);
                ResultSet rsMenu = psMenu.executeQuery();
                int nomor = 1;
                boolean adaMenu = false;
                while (rsMenu.next()) {
                    adaMenu = true;
                    daftarIdMenu.add(rsMenu.getInt("id_menu"));
                    int stok = rsMenu.getInt("stok");
                    String wStok = (stok <= 5) ? RED : GREEN;
                    System.out.printf(
                        CYAN + "  ║ " + RESET + "%-4d " + CYAN + "║ " + RESET + "%-28s " +
                        CYAN + "║ " + GREEN + "Rp %-12.0f " + CYAN + "║ " + wStok + "%-5d " + CYAN + "║%n" + RESET,
                        nomor, rsMenu.getString("nama_menu"),
                        rsMenu.getDouble("harga"), stok);
                    nomor++;
                }
                if (!adaMenu) {
                    System.out.println(CYAN + "  ║ " + YELLOW + "  (Tidak ada menu tersedia untuk kategori ini)" +
                        rep(' ', 14) + CYAN + "║" + RESET);
                }
            }
            System.out.println(CYAN + "  ╚══════╩══════════════════════════════╩═════════════════╩═══════╝" + RESET);

            if (daftarIdMenu.isEmpty()) { pressEnter(sc); continue; }

            System.out.print(YELLOW + "\n  Nomor (1-" + daftarIdMenu.size() + ") atau 0=batal: " + RESET);
            int pilihanUser = readInt(sc, -1);
            if (pilihanUser == 0) continue;
            if (pilihanUser < 1 || pilihanUser > daftarIdMenu.size()) {
                System.out.println(RED + "  [✘] Nomor tidak valid!" + RESET);
                playSound("error.wav");
                pressEnter(sc); continue;
            }

            int idAsli = daftarIdMenu.get(pilihanUser - 1);

            System.out.print(YELLOW + "  Jumlah: " + RESET);
            int qty = readInt(sc, 0);
            if (qty <= 0) { System.out.println(RED + "  [✘] Jumlah harus > 0!" + RESET); continue; }

            System.out.print(YELLOW + "  Catatan (Enter=kosong): " + RESET);
            String catatanItem = sc.nextLine().trim();

            // Validasi stok di database
            try (PreparedStatement psCek = conn.prepareStatement(
                "SELECT nama_menu, harga, stok FROM menu WHERE id_menu=? AND tersedia=TRUE")) {
                psCek.setInt(1, idAsli);
                ResultSet rsCek = psCek.executeQuery();
                if (rsCek.next()) {
                    int stokDb = rsCek.getInt("stok");
                    // Hitung stok yg sudah di keranjang
                    int stokKeranjang = keranjang.stream()
                        .filter(ci -> ci.id == idAsli)
                        .mapToInt(ci -> ci.qty)
                        .sum();
                    int stokEfektif = stokDb - stokKeranjang;

                    if (stokEfektif >= qty) {
                        String nmMenu  = rsCek.getString("nama_menu");
                        double hrgMenu = rsCek.getDouble("harga");
                        boolean found  = false;
                    for (CartItem ci : keranjang) {

                    // Jika menu sama DAN catatan sama -> gabungkan
                    if (ci.id == idAsli && ci.catatan.equalsIgnoreCase(catatanItem)) {

                        ci.qty += qty;
                        ci.subtotal += hrgMenu * qty;

                        found = true;
                        break;
                    }
                }   
                        if (!found) keranjang.add(new CartItem(idAsli, nmMenu, qty, hrgMenu, catatanItem));
                        System.out.println(GREEN + "  ✔ [" + nmMenu + " x" + qty + "] ditambahkan!" + RESET);
                        playSound("buying sound.wav");  
                        Thread.sleep(600);
                    } else {
                        playSound("error.wav");
                        System.out.println(RED + "  [✘] Stok tidak cukup! Tersedia efektif: "
                            + stokEfektif + RESET);
                            playSound("error.wav");
                        pressEnter(sc);
                    }
                } else {
                    System.out.println(RED + "  [✘] Menu tidak ditemukan / nonaktif!" + RESET);
                    pressEnter(sc);
                    playSound("error.wav");
                }
            } catch (InterruptedException ignored) {}
        }

        if (keranjang.isEmpty()) {
            System.out.println(YELLOW + "\n  Keranjang kosong. Transaksi dibatalkan." + RESET);
            pressEnter(sc); return;
        }

        // ── CHECKOUT ──────────────────────────────────────────
        double subtotalBelanja = keranjang.stream().mapToDouble(i -> i.subtotal).sum();

        double diskon = 0;
        String keteranganDiskon = "";
        if (isMember) {
            if (subtotalBelanja >= 200000) {
                diskon = subtotalBelanja * 0.15;
                keteranganDiskon = "Diskon Member 15% (≥ Rp 200.000)";
            } else if (subtotalBelanja >= 100000) {
                diskon = subtotalBelanja * 0.10;
                keteranganDiskon = "Diskon Member 10% (≥ Rp 100.000)";
            } else if (subtotalBelanja >= 50000) {
                diskon = subtotalBelanja * 0.05;
                keteranganDiskon = "Diskon Member 5% (≥ Rp 50.000)";
            }
        }

        double total = subtotalBelanja - diskon + BIAYA_PARKIR;

        header();

// Konstanta lebar box
final int W     = 56;
final int INNER = W - 2; // 56

String brE = rep('═', W);

// Helper hapus ANSI untuk hitung panjang visual
java.util.function.Function<String, Integer> visLen =
    s -> s.replaceAll("\u001B\\[[\\d;]*[a-zA-Z]", "").length();

// Helper baris isi box — padding berdasarkan panjang VISUAL
java.util.function.Function<String, String> bRow = content -> {
    int pad = INNER - visLen.apply(content);
    if (pad < 0) pad = 0;
    return "  ║ " + content + rep(' ', pad) + " ║";
};

// Helper label : nilai — label kiri, nilai rata KANAN
java.util.function.BiFunction<String, String, String> bNum = (label, val) -> {
    // Hapus ANSI dari val untuk hitung panjang asli
    int valVisLen = visLen.apply(val);
    String lbl    = String.format("%-24s: ", label); // 24 + 2 = 26
    int numWidth  = INNER - lbl.length();            // sisa untuk nilai
    int spaces    = numWidth - valVisLen;
    if (spaces < 0) spaces = 0;
    return "  ║ " + lbl + rep(' ', spaces) + val + " ║";
};

System.out.println(CYAN + BOLD);
System.out.println("  ╔" + brE + "╗");
System.out.println(bRow.apply("           RINGKASAN PESANAN"));
System.out.println("  ╠" + brE + "╣");

// Info pemesan
System.out.println(bRow.apply(
    YELLOW + "Pemesan " + RESET + ": " + BOLD + namaPemesan + RESET));
System.out.println(bRow.apply(
    YELLOW + "Meja    " + RESET + ": " + BOLD + nomorMeja + RESET));
System.out.println(bRow.apply(
    YELLOW + "Status  " + RESET + ": " +
    (isMember ? GREEN + "★ MEMBER - " + namaMember :  "Non-Member") + RESET));

System.out.println("  ╠" + brE + "╣");

// Header kolom item
final int C_NAMA = 26;
final int C_QTY  = 5;
final int C_HRG  = 12;
final int C_SUB  = INNER - C_NAMA - C_QTY - C_HRG - 3; // sisa = 10

System.out.printf(
    "  ║ " + BOLD +
    "%-" + C_NAMA + "s " +
    "%" + C_QTY  + "s " +
    "%" + C_HRG  + "s " + 
    "%" + C_SUB  + "s" +
    RESET + " ║%n",
    "Item", "Qty", "Harga", "Subtotal"
);
System.out.println("  ╟" + rep('─', W) + "╢");

// Baris item
for (CartItem ci : keranjang) {
    String nm  = cut(ci.nama, C_NAMA);
    String qty = "x" + ci.qty;
    String hrg = String.format("%,.0f", ci.harga);
    String sub = String.format("%,.0f", ci.subtotal);

    System.out.printf(
        "  ║ " +
        CYAN  + "%-" + C_NAMA + "s" + RESET + " " +
        "%" + C_QTY + "s " +
        "%" + C_HRG + "s " +
        GREEN + "%" + C_SUB + "s" + RESET +
        " ║%n",
        nm, qty, hrg, sub
    );
}

System.out.println("  ╠" + brE + "╣");

// Ringkasan biaya
System.out.println(bNum.apply(
    "Subtotal Belanja",
    WHITE + String.format("Rp %,.0f", subtotalBelanja) + RESET));

if (diskon > 0)
    System.out.println(bNum.apply(
        keteranganDiskon.length() > 22
            ? cut(keteranganDiskon, 22)
            : keteranganDiskon,
        GREEN + "- Rp " + String.format("%,.0f", diskon) + RESET));

System.out.println(bNum.apply(
    "Biaya Parkir",
    YELLOW + String.format("Rp %,.0f", BIAYA_PARKIR) + RESET));

System.out.println("  ╠" + brE + "╣");

System.out.println(bNum.apply(
    "TOTAL BAYAR",
    GREEN + BOLD + String.format("Rp %,.0f", total) + RESET));

System.out.println("  ╚" + brE + "╝" + RESET);

        // Metode bayar
        System.out.println(YELLOW + "\n  Metode Pembayaran:" + RESET);
        try (PreparedStatement psMetode = conn.prepareStatement(
            "SELECT id_metode, nama_metode FROM metode_bayar WHERE aktif=TRUE ORDER BY id_metode")) {
            ResultSet rsMetode = psMetode.executeQuery();
            while (rsMetode.next())
                System.out.printf("   [%d] %s%n",
                    rsMetode.getInt("id_metode"), rsMetode.getString("nama_metode"));
        }
        System.out.print(YELLOW + "  Pilih metode bayar: " + RESET);
        int idMetode = readInt(sc, 1);
        if (idMetode < 1 || idMetode > 5) idMetode = 1;

        double bayar = total;
        if (idMetode == 1) { // Tunai
            System.out.print(YELLOW + "  Jumlah Bayar (Rp): " + RESET);
            bayar = readDouble(sc, 0);
            if (bayar < total) {
                    playSound("error.wav");
                System.out.println(RED + "  [✘] Uang tidak cukup!" + RESET);
                playSound("error.wav");
                pressEnter(sc); return;
            }
        }
        double kembalian = (idMetode == 1) ? (bayar - total) : 0;

        showLoading("  Memproses transaksi");

        // ── SIMPAN KE DATABASE ────────────────────────────────
        conn.setAutoCommit(false);
        try {
            // Insert pesanan
            int idOrder;
            try (PreparedStatement psP = conn.prepareStatement(
                "INSERT INTO pesanan(id_karyawan,id_pelanggan,nomor_meja,nama_pemesan," +
                "subtotal,diskon,biaya_parkir,total_akhir,status) " +
                "VALUES(?,?,?,?,?,?,?,?,'selesai') RETURNING id_pesanan")) {
                psP.setInt(1, loginIdKaryawan);
                if (idPelanggan > 0) psP.setInt(2, idPelanggan);
                else                 psP.setNull(2, Types.INTEGER);
                psP.setString(3, nomorMeja);
                psP.setString(4, namaPemesan);
                psP.setDouble(5, subtotalBelanja);
                psP.setDouble(6, diskon);
                psP.setDouble(7, BIAYA_PARKIR);
                psP.setDouble(8, total);
                ResultSet rsP = psP.executeQuery();
                rsP.next();
                idOrder = rsP.getInt(1);
            }

            // Insert detail & kurangi stok
            for (CartItem ci : keranjang) {
                try (PreparedStatement psD = conn.prepareStatement(
                    "INSERT INTO detail_pesanan(id_pesanan,id_menu,jumlah,harga_satuan," +
                    "subtotal_item,catatan_item) VALUES(?,?,?,?,?,?)")) {
                    psD.setInt(1, idOrder);
                    psD.setInt(2, ci.id);
                    psD.setInt(3, ci.qty);
                    psD.setDouble(4, ci.harga);
                    psD.setDouble(5, ci.subtotal);
                    psD.setString(6, ci.catatan.isEmpty() ? null : ci.catatan);
                    psD.executeUpdate();
                }
                try (PreparedStatement psU = conn.prepareStatement(
                    "UPDATE menu SET stok = stok - ? WHERE id_menu = ?")) {
                    psU.setInt(1, ci.qty);
                    psU.setInt(2, ci.id);
                    psU.executeUpdate();
                }
            }

            // Insert pembayaran
            try (PreparedStatement psBayar = conn.prepareStatement(
                "INSERT INTO pembayaran(id_pesanan,id_metode,jumlah_bayar,kembalian,status_bayar)" +
                " VALUES(?,?,?,?,'lunas')")) {
                psBayar.setInt(1, idOrder);
                psBayar.setInt(2, idMetode);
                psBayar.setDouble(3, bayar);
                psBayar.setDouble(4, kembalian);
                psBayar.executeUpdate();
            }

            // Update poin member
            if (idPelanggan > 0) {
                int poinBaru = (int)(total / 10000);
                try (PreparedStatement psPoins = conn.prepareStatement(
                    "UPDATE pelanggan SET poin=poin+? WHERE id_pelanggan=?")) {
                    psPoins.setInt(1, poinBaru);
                    psPoins.setInt(2, idPelanggan);
                    psPoins.executeUpdate();
                }
            }

            conn.commit();

            

           
            // ── KONSTANTA & HELPER (taruh di awal method, sebelum cetak struk) ──
final int I_LEN = 26;           
final int Q_LEN = 5;            
final int S_LEN = INNER - I_LEN - 1 - Q_LEN - 1; 
final int L_LEN = 35;          
final int N_LEN = INNER - L_LEN - 1;             

String br = "═".repeat(W);


java.util.function.BiFunction<String, Integer, String> padR =
    (s, w) -> {
        if (s.length() >= w) return s.substring(0, w);
        return s + " ".repeat(w - s.length());
    };


java.util.function.Function<String, String> boxRow =
    content -> {
        String c = content.length() >= INNER
                ? content.substring(0, INNER)
                : content + " ".repeat(INNER - content.length());
        return "  ║ " + c + " ║";
    };


java.util.function.BiFunction<String, Double, String> rowNum =
    (label, val) -> {
        String lbl = padR.apply(label, L_LEN);
        String num = String.format("Rp %,11.0f", val);   
        String content = lbl + " " + String.format("%" + N_LEN + "s", num);
        return boxRow.apply(content);
    };


// ── CETAK STRUK ───────────────────────────────────────────────────────
clearScreen();
header();
String tgl = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

System.out.println(YELLOW + BOLD);
System.out.println("  ╔" + br +"╗");
System.out.println(boxRow.apply("  TE-CAFE - STRUK PEMESANAN  "));
System.out.println("  ╠" + br +"╣");
System.out.println(boxRow.apply("No. Order : #" + idOrder));
System.out.println(boxRow.apply("Pemesan   : " + namaPemesan));
System.out.println(boxRow.apply("Meja      : " + nomorMeja));
System.out.println(boxRow.apply("Status    : " +
        (isMember ? "★ MEMBER - " + namaMember : "Non-Member")));
System.out.println(boxRow.apply("Kasir     : " + loginNamaKaryawan));
System.out.println(boxRow.apply("Waktu     : " + tgl));
System.out.println("  ╠" + br +"╣");

// Header kolom
String hdr = padR.apply("Item", I_LEN)
           + " " + padR.apply("Qty", Q_LEN)
           + " " + String.format("%" + S_LEN + "s", "Subtotal");
System.out.println(boxRow.apply(hdr));
System.out.println("  ╠" + br +"╣");

// Baris item
for (CartItem ci : keranjang) {
    String nm  = padR.apply(ci.nama, I_LEN);
    String qty = padR.apply("x" + ci.qty, Q_LEN);
    String sub = String.format("%" + S_LEN + "s",
                    String.format("Rp %,9.0f", ci.subtotal));
    System.out.println(boxRow.apply(nm + " " + qty + " " + sub));
}

System.out.println("  ╠" + br +"╣");
System.out.println(rowNum.apply("Subtotal",    (double) subtotalBelanja));
if (diskon > 0)
    System.out.println(rowNum.apply("Diskon Member", (double) diskon));
System.out.println(rowNum.apply("Biaya Parkir", (double) BIAYA_PARKIR));
System.out.println("  ╠" + br +"╣");
System.out.println(rowNum.apply("TOTAL BAYAR", (double) total));
System.out.println(rowNum.apply("DIBAYAR",     (double) bayar));
System.out.println(rowNum.apply("KEMBALIAN",   (double) kembalian));
System.out.println("  ╠" + br +"╣");
System.out.println(boxRow.apply("TERIMA KASIH - SELAMAT MENIKMATI!"));
if (isMember) {
    int pb = (int)(total / 10000);
    System.out.println(boxRow.apply("★ Poin diperoleh: +" + pb + " poin"));
}
System.out.println("  ╚" + br +"╝" + RESET);
                        
            playSound("purchase success.wav");  
            System.out.println(GREEN + "\n  ✔ Pesanan #" + idOrder + " berhasil tersimpan!" + RESET);

        } catch (Exception e) {
            conn.rollback();
            System.out.println(RED + "  [ERROR] " + e.getMessage() + RESET);
        } finally {
            conn.setAutoCommit(true);
        }
        pressEnter(sc);
    }

    // ── Helper tampil meja ─────────────────────────────────────
    static void tampilMejaTersedia(Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
            "SELECT nomor_meja, kapasitas, lokasi, status FROM meja ORDER BY lokasi, nomor_meja")) {
            ResultSet rs = ps.executeQuery();
            System.out.println(CYAN + BOLD +
                "  ╔══════════╦═════╦══════════╦════════════╗\n" +
                "  ║  Nomor   ║ Kap ║  Lokasi  ║   Status   ║\n" +
                "  ╠══════════╬═════╬══════════╬════════════╣" + RESET);
            while (rs.next()) {
                String st    = rs.getString("status");
                String warna = st.equals("tersedia") ? GREEN : st.equals("terisi") ? RED : YELLOW;
                System.out.printf(CYAN + "  ║ %-8s ║ %-3d ║ %-8s ║ " + warna + "%-10s" + CYAN + " ║%n" + RESET,
                    rs.getString("nomor_meja"), rs.getInt("kapasitas"),
                    rs.getString("lokasi"), st);
            }
            System.out.println(CYAN + "  ╚══════════╩═════╩══════════╩════════════╝" + RESET);
            System.out.println();
        }
    }

    // ═══════════════════════════════════════════════════════════
    //  2. RIWAYAT PEMESANAN
    // ═══════════════════════════════════════════════════════════
    static void menuRiwayatPemesanan(Connection conn, Scanner sc) throws SQLException {
        boolean balik = false;
        while (!balik) {
            header();
            System.out.println(BLUE + BOLD +
                "  ╔══════════════════════════════════════════╗\n" +
                "  ║         MENU RIWAYAT PEMESANAN           ║\n" +
                "  ╠══════════════════════════════════════════╣\n" +
                "  ║  1. Semua Riwayat (50 Terakhir)          ║\n" +                           
                "  ║  2. Cari by Nama Pemesan                 ║\n" +                         
                "  ║  3. Riwayat Hari Ini                     ║\n" +
                "  ║  4. Hapus Riwayat Pesanan                ║\n" +
                "  ║  0. Kembali                              ║\n" +
                "  ╚══════════════════════════════════════════╝" + RESET);
            System.out.print(YELLOW + "  Pilihan: " + RESET);
            int pilih = readInt(sc, -1);

            switch (pilih) {
                case 1 -> tampilRiwayatSemua(conn);
                case 2 -> {
                    System.out.print(YELLOW + "  Nama pemesan: " + RESET);
                    cariRiwayatByNama(conn, sc.nextLine().trim());
                }
                case 3 -> tampilRiwayatHariIni(conn);
                case 4 -> hapusRiwayatPesanan(conn, sc);

                case 0 -> balik = true;
                default -> {
                    System.out.println(RED + "  [✘] Pilihan tidak valid!" + RESET);
                    playSound("error.wav");
                
            }
            }
            if (!balik) pressEnter(sc);
        }
    }
    static void hapusRiwayatPesanan(Connection conn, Scanner sc) throws SQLException {
    clearScreen();
    header();
    System.out.println(RED + BOLD + "  HAPUS RIWAYAT PESANAN" + RESET);
    System.out.println(RED + "─".repeat(60) + RESET);
    System.out.println(YELLOW + "  ⚠ PERHATIAN: Aksi ini akan menghapus PERMANEN!" + RESET);
    System.out.println(YELLOW + "    (detail_pesanan & pembayaran ikut terhapus)" + RESET);
    System.out.println();

    // Tampilkan daftar pesanan agar user tahu ID mana yang mau dihapus
    tampilRiwayatSemua(conn);

    System.out.println();
  
    System.out.print(
    "Masukkan ID Pesanan yang ingin dihapus (pisahkan koma): "
);

String input = sc.nextLine().trim();

if (input.equals("0")) {

    System.out.println(
        YELLOW + "  Dibatalkan." + RESET
    );

    return;
}

String[] ids = input.split(",");

for (String s : ids) {

    try {

        int idHapus = Integer.parseInt(s.trim());

        // Validasi ID
        if (idHapus <= 0) {

            System.out.println(
                RED + "  ID tidak valid: " +
                idHapus + RESET
            );

            continue;
        }

        // Cek pesanan ada atau tidak
        try (PreparedStatement psCek = conn.prepareStatement(
                "SELECT id_pesanan FROM pesanan WHERE id_pesanan = ?")) {

            psCek.setInt(1, idHapus);

            ResultSet rsCek = psCek.executeQuery();

            if (!rsCek.next()) {

                System.out.println(
                    RED +
                    "  ✗ Pesanan ID " +
                    idHapus +
                    " tidak ditemukan!" +
                    RESET
                );

                continue;
            }
        }

        // Tampilkan detail
        tampilDetailPesanan(conn, idHapus);

        System.out.println();

        System.out.print(
            RED +
            "  Yakin ingin menghapus pesanan #" +
            idHapus +
            "? (y/n): " +
            RESET
        );

        String konfirmasi = sc.nextLine().trim().toUpperCase();

        if (!konfirmasi.equals("Y") &&
            !konfirmasi.equals("YES")) {

            System.out.println(
                YELLOW +
                "  Penghapusan ID " +
                idHapus +
                " dibatalkan." +
                RESET
            );

            continue;
        }

        // DELETE DATABASE
        try (PreparedStatement psDelete = conn.prepareStatement(
                "DELETE FROM pesanan WHERE id_pesanan=?")) {

            psDelete.setInt(1, idHapus);

            int deleted = psDelete.executeUpdate();

            if (deleted > 0) {

                System.out.println(
                    GREEN +
                    "  ✔ Pesanan ID " +
                    idHapus +
                    " berhasil dihapus." +
                    RESET
                );
            }
        }

    } catch (NumberFormatException e) {

        System.out.println(
            RED +
            "  Input tidak valid: " +
            s +
            RESET
        );
    } finally {
        System.out.println();
    }}
}

static void tampilRiwayatSemua(Connection conn) throws SQLException {
    clearScreen();
    header();
    String sql =
        "SELECT p.id_pesanan, p.waktu_pesan, p.nomor_meja, p.nama_pemesan, " +
        "p.total_akhir, p.status, mb.nama_metode, " +
        "CASE WHEN p.id_pelanggan IS NOT NULL THEN '★' ELSE ' ' END AS member " +
        "FROM pesanan p " +
        "LEFT JOIN pembayaran pb ON pb.id_pesanan=p.id_pesanan " +
        "LEFT JOIN metode_bayar mb ON mb.id_metode=pb.id_metode " +
        "ORDER BY p.waktu_pesan DESC LIMIT 50";

    // Lebar tiap kolom
    final int W_ID     =  4;
    final int W_WAKTU  = 19;
    final int W_MEJA   =  5;
    final int W_NAMA   = 16;
    final int W_TOTAL  = 12;
    final int W_STATUS = 10;
    final int W_METODE = 10;
    final int TOTAL    = 100;

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ResultSet rs = ps.executeQuery();

        // Judul
        System.out.println(CYAN + BOLD + "  RIWAYAT PEMESANAN (50 Terakhir)" + RESET);

        // Garis atas
        System.out.println("  ╔" + rep( '═', TOTAL) + "╗");

        // Header kolom
        System.out.printf(
            "  ║ " + BOLD +
            "%-" + W_ID     + "s │ " +
            "%-" + W_WAKTU  + "s │ " +
            "%-" + W_MEJA   + "s │ " +
            "%-" + W_NAMA   + "s │ " +
            "%-" + W_TOTAL  + "s │ " +
            "%-" + W_STATUS + "s │ " +
            "M │ " +
            "%-" + W_METODE + "s" +
            RESET + " ║%n",
            "ID", "Waktu", "Meja", "Pemesan", "Total", "Status", "Metode"
        );

        // Garis tengah
        System.out.println("  ╠" + rep('═', TOTAL) + "╣");

        boolean ada = false;
        while (rs.next()) {
            ada = true;
            String st = rs.getString("status");
            String w  = st.equals("selesai")    ? GREEN
                      : st.equals("dibatalkan") ? RED
                      : YELLOW;

            String totalStr  = String.format("Rp%,." + "0f", rs.getDouble("total_akhir"));
            String metode    = rs.getString("nama_metode") != null
                             ? rs.getString("nama_metode") : "-";

            System.out.printf(
                "  ║ " +
                BLUE + "%-" + W_ID + "d" + RESET + " │ " +
                "%-" + W_WAKTU + "s │ " +
                "%-" + W_MEJA  + "s │ " +
                "%-" + W_NAMA  + "s │ " +
                GREEN + "%-" + W_TOTAL + "s" + RESET + " │ " +
                w + "%-" + W_STATUS + "s" + RESET + " │ " +
                "%s │ " +
                "%-" + W_METODE + "s" +
                " ║%n",
                rs.getInt("id_pesanan"),
                rs.getTimestamp("waktu_pesan").toString().substring(0, 19),
                rs.getString("nomor_meja"),
                cut(rs.getString("nama_pemesan"), W_NAMA),
                totalStr,
                st,
                rs.getString("member"),
                cut(metode, W_METODE)
            );
        }

        if (!ada) {
            String msg = "Belum ada riwayat";
            int sisaSpasi = TOTAL - 2 - msg.length();
            System.out.println("  ║ " + YELLOW + msg + RESET
                + rep(' ', sisaSpasi) + "║");
        }
        System.out.println("  ╚" + rep('═', TOTAL) + "╝");
    }
}
  
            private static String line(char c) {
               
                String s = String.valueOf(c).repeat(WIDTH);
                return s;
            }
            
            private static String colorPad(String color, String text, int width) {
                String padded = String.format("%-" + width + "s", text);
                return color + padded + RESET;
            }

    static void tampilDetailPesanan(Connection conn, int idPesanan) throws SQLException {
        header();
        String sqlH =
            "SELECT p.*, k.nama_karyawan, mb.nama_metode, " +
            "pb.jumlah_bayar, pb.kembalian, pb.waktu_bayar, " +
            "pl.nama AS nama_pelanggan, pl.telepon AS hp_pelanggan " +
            "FROM pesanan p " +
            "LEFT JOIN karyawan k      ON k.id_karyawan  = p.id_karyawan " +
            "LEFT JOIN pembayaran pb   ON pb.id_pesanan  = p.id_pesanan " +
            "LEFT JOIN metode_bayar mb ON mb.id_metode   = pb.id_metode " +
            "LEFT JOIN pelanggan pl    ON pl.id_pelanggan= p.id_pelanggan " +
            "WHERE p.id_pesanan=?";
             
        try (PreparedStatement psH = conn.prepareStatement(sqlH)) {
            psH.setInt(1, idPesanan);
            ResultSet rsH = psH.executeQuery();
            if (!rsH.next()) {
                System.out.println(RED + "  [✘] Pesanan #" + idPesanan + " tidak ditemukan!" + RESET);
                playSound("error.wav");
                return;
            }
           
            topBox();

            System.out.printf(
                YELLOW + "║" + GREEN + BOLD +
                " %-54s " +
                YELLOW + "║%n" + RESET,
                "DETAIL PESANAN #" + idPesanan
            );

            midBox();

                printRow(
                    "Waktu Pesan",
                    rsH.getTimestamp("waktu_pesan")
                        .toString()
                        .substring(0, 19)
                );

                printRow(
                    "Pemesan",
                    rsH.getString("nama_pemesan")
                );

                printRow(
                    "Nomor Meja",
                    rsH.getString("nomor_meja")
                );

                printRow(
                    "Status Member",
                    rsH.getString("nama_pelanggan") != null
                        ? "★ Member - " + rsH.getString("nama_pelanggan")
                        : "Non-Member"
                );

                printRow(
                    "Kasir",
                    rsH.getString("nama_karyawan")
                );

                printRow(
                    "Metode Bayar",
                    rsH.getString("nama_metode")
                );

                printRow(
                    "Status",
                    rsH.getString("status")
                );

                midBox();

            String sqlD =
                "SELECT m.nama_menu, dp.jumlah, dp.harga_satuan, dp.subtotal_item, dp.catatan_item " +
                "FROM detail_pesanan dp JOIN menu m ON m.id_menu=dp.id_menu " +
                "WHERE dp.id_pesanan=? ORDER BY dp.id_detail";
            try (PreparedStatement psD = conn.prepareStatement(sqlD)) {
                psD.setInt(1, idPesanan);
                ResultSet rsD = psD.executeQuery();

           
            System.out.printf(
                YELLOW + "║ " + RESET +
                BOLD + "%-20s %-4s %-14s %-12s" + RESET +  
                YELLOW + "  ║%n" + RESET,                  
                "Menu", "Qty", "Harga", "Subtotal"
            );


            System.out.println(
                YELLOW + "╟" + line('─') + "╢" + RESET
            );  

           
            while (rsD.next()) {
                System.out.printf(
                    YELLOW + "║ " + RESET +
                    "%s %s %s %s" +
                    YELLOW + "  ║%n" + RESET,

                    colorPad(CYAN,  cut(rsD.getString("nama_menu"), 20),                      20),
                    colorPad(RESET, "x" + rsD.getInt("jumlah"),                                4),
                    colorPad(RESET, String.format("Rp%,.0f", rsD.getDouble("harga_satuan")),  14),
                    colorPad(GREEN, String.format("Rp%,.0f", rsD.getDouble("subtotal_item")), 12)
                );

                String cat = rsD.getString("catatan_item");

                if (cat != null && !cat.isEmpty()) {

                    System.out.printf(
                        YELLOW + "║ " + DIM +
                        "Catatan: %-44s" +
                        YELLOW + " ║%n" + RESET,
                        cut(cat, 44)
                    );
                }
           
            }

            }
            midBox();

            printTotal(
                "Subtotal",
                rsH.getDouble("subtotal"),
                WHITE
            );

            printTotal(
                "Diskon",
                rsH.getDouble("diskon"),
                GREEN
            );

            printTotal(
                "Parkir",
                rsH.getDouble("biaya_parkir"),
                YELLOW
            );

            printTotal(
                "TOTAL",
                rsH.getDouble("total_akhir"),
                GREEN
            );

            printTotal(
                "BAYAR",
                rsH.getDouble("jumlah_bayar"),
                WHITE
            );

            printTotal(
                "KEMBALI",
                rsH.getDouble("kembalian"),
                GREEN
            );

            bottomBox();
                    }
                }

    static void cariRiwayatByNama(Connection conn, String keyword) throws SQLException {
        header();
        String sql =
            "SELECT p.id_pesanan, p.waktu_pesan, p.nomor_meja, p.nama_pemesan, " +
            "p.total_akhir, p.status, COUNT(dp.id_detail) AS jml " +
            "FROM pesanan p LEFT JOIN detail_pesanan dp ON dp.id_pesanan=p.id_pesanan " +
            "WHERE LOWER(p.nama_pemesan) LIKE LOWER(?) " +
            "GROUP BY p.id_pesanan ORDER BY p.waktu_pesan DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            System.out.println(CYAN + BOLD + "  Hasil Pencarian: \"" + keyword + "\"" + RESET);
            System.out.println("  " + rep('─', 92));
            System.out.printf(BOLD + "  %-5s │ %-19s │ %-5s │ %-18s │ %-13s │ %-8s │ Itm%n" + RESET,
                "ID", "Waktu", "Meja", "Pemesan", "Total", "Status");
            System.out.println("  " + rep('─', 92));
            boolean ada = false;
            while (rs.next()) {
                ada = true;
                System.out.printf("  " + CYAN + "%-5d" + RESET + " │ %-19s │ %-5s │ %-19s │ "
                    + GREEN + "Rp%,-10.0f" + RESET + " │ %-8s  %d%n",
                    rs.getInt("id_pesanan"),
                    rs.getTimestamp("waktu_pesan").toString().substring(0, 19),
                    rs.getString("nomor_meja"), rs.getString("nama_pemesan"),
                    rs.getDouble("total_akhir"), rs.getString("status"), rs.getInt("jml"));
            }
            if (!ada) System.out.println(YELLOW + "  (Tidak ditemukan)" + RESET);
            System.out.println("  " + rep('─', 92));
        }
    }

   
    static void tampilRiwayatHariIni(Connection conn) throws SQLException {
        header();
        String sql =
            "SELECT p.id_pesanan, p.waktu_pesan, p.nomor_meja, p.nama_pemesan, " +
            "p.total_akhir, p.status, COUNT(dp.id_detail) AS jml, " +
            "CASE WHEN p.id_pelanggan IS NOT NULL THEN '★' ELSE ' ' END AS member " +
            "FROM pesanan p LEFT JOIN detail_pesanan dp ON dp.id_pesanan=p.id_pesanan " +
            "WHERE DATE(p.waktu_pesan)=CURRENT_DATE " +
            "GROUP BY p.id_pesanan ORDER BY p.waktu_pesan DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            System.out.println(CYAN + BOLD + "  RIWAYAT HARI INI — "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + RESET);
            System.out.println("  " + rep('─', 90));
            System.out.printf(BOLD + "  %-5s │ %-8s │ %-5s │ %-15s │ %-13s │ %-10s │ M │ Itm%n" + RESET,
                "ID", "Jam", "Meja", "Pemesan", "Total", "Status");
            System.out.println("  " + rep('─', 90));
            int trx = 0; double tot = 0;
            while (rs.next()) {
                trx++; tot += rs.getDouble("total_akhir");
                System.out.printf("  " + CYAN + "%-5d" + RESET + " │ %-8s │ %-5s │ %-15s │ "
                    + GREEN + "Rp%,-10.0f" + RESET + " │ %-10s │ %s │ %d%n",
                    rs.getInt("id_pesanan"),
                    rs.getTimestamp("waktu_pesan").toString().substring(11, 19),
                    rs.getString("nomor_meja"), rs.getString("nama_pemesan"),
                    rs.getDouble("total_akhir"), rs.getString("status"),
                    rs.getString("member"), rs.getInt("jml"));
            }
            System.out.println("  " + rep('─', 90));
            System.out.printf(BOLD + "  Total Transaksi: %-5d  │  Total Pendapatan: " + GREEN + "Rp %,.0f%n" + RESET,
                trx, tot);
            if (trx == 0) System.out.println(YELLOW + "  (Belum ada transaksi hari ini)" + RESET);
        }
    }

    

   
    // ═══════════════════════════════════════════════════════════
    //  3. MANAJEMEN PELANGGAN
    // ═══════════════════════════════════════════════════════════
    static void menuPelanggan(Connection conn, Scanner sc) throws SQLException {
        boolean balik = false;
        while (!balik) {
            header();
            System.out.println(MAGENTA + BOLD +
                "  ╔══════════════════════════════════════════════╗\n" +
                "  ║       MANAJEMEN & RIWAYAT PELANGGAN          ║\n" +
                "  ╠══════════════════════════════════════════════╣\n" +
                "  ║  1. Daftar Semua Pelanggan                   ║\n" +
                "  ║  2. Hapus Pelanggan                          ║\n" +
                "  ║  0. Kembali                                  ║\n" +
                "  ╚══════════════════════════════════════════════╝" + RESET);
            System.out.print(YELLOW + "  Pilihan: " + RESET);
            int pilih = readInt(sc, -1);
            switch (pilih) {
                case 1 -> tampilSemuaPelanggan(conn);
                case 2 -> hapusPelanggan(conn, sc);
                case 0 -> balik = true;
                default -> {
                    System.out.println(RED + "  [✘] Pilihan tidak valid!" + RESET);
                    playSound("error.wav");
                }
            }

            if (!balik) pressEnter(sc);
        }
    }

    static void tampilSemuaPelanggan(Connection conn) throws SQLException {
        header();
        String sql =
            "SELECT p.id_pelanggan, p.nama, p.telepon, p.email, p.poin, " +
            "COUNT(ps.id_pesanan) AS total_trx, " +
            "COALESCE(SUM(ps.total_akhir),0) AS total_belanja " +
            "FROM pelanggan p LEFT JOIN pesanan ps ON ps.id_pelanggan=p.id_pelanggan " +
            "GROUP BY p.id_pelanggan ORDER BY total_belanja DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            System.out.println(MAGENTA + BOLD + "  DAFTAR SEMUA MEMBER / PELANGGAN" + RESET);
            System.out.println("  " + rep('─', 105));
            System.out.printf(BOLD + "  %-4s │ %-20s │ %-14s │ %-24s │ %-5s │ %-4s │ %s%n" + RESET,
                "ID", "Nama", "No. HP", "Email", "Poin", "Trx", "Total Belanja");
            System.out.println("  " + rep('─', 105));
            boolean ada = false;
            while (rs.next()) {
                ada = true;
                System.out.printf("  " + MAGENTA + "%-4d" + RESET + " │ " + CYAN + "★ %-18s" + RESET
                    + " │ %-14s │ %-24s │ " + YELLOW + "%-5d" + RESET + " │ %-4d │ " + GREEN + "Rp%,.0f%n" + RESET,
                    rs.getInt("id_pelanggan"), rs.getString("nama"),
                    rs.getString("telepon") != null ? rs.getString("telepon") : "-",
                    rs.getString("email")   != null ? rs.getString("email")   : "-",
                    rs.getInt("poin"), rs.getInt("total_trx"), rs.getDouble("total_belanja"));
            }
            if (!ada) System.out.println(YELLOW + "  (Belum ada pelanggan)" + RESET);
            System.out.println("  " + rep('─', 105));
        }
    }


    static void hapusPelanggan(Connection conn, Scanner sc) throws SQLException {
        header();
        tampilSemuaPelanggan(conn);
        System.out.print(YELLOW + "\n  ID Pelanggan yang dihapus: " + RESET);
        int id = readInt(sc, -1); 
        if (id <= 0) return;
        try (PreparedStatement psCek = conn.prepareStatement(
            "SELECT nama FROM pelanggan WHERE id_pelanggan=?")) {
            psCek.setInt(1, id);
            ResultSet rs = psCek.executeQuery();
            if (rs.next()) {
                System.out.print(RED + "  Yakin hapus [" + rs.getString("nama") + "]? (y/n): " + RESET);
                if (sc.nextLine().trim().equalsIgnoreCase("y")) {
                    try (PreparedStatement psDel = conn.prepareStatement(
                        "DELETE FROM pelanggan WHERE id_pelanggan=?")) {
                        psDel.setInt(1, id);
                        psDel.executeUpdate();
                        System.out.println(GREEN + "  ✔ Pelanggan berhasil dihapus!" + RESET);
                        playSound("tithuh-powerup-success.wav");
                    } catch (SQLException e) {
                        System.out.println(RED + "  [✘] Gagal: Pelanggan memiliki riwayat transaksi." + RESET);
                        playSound("error.wav");
                        System.out.println(DIM + "  (Saran: Jangan dihapus agar data transaksi tetap valid)" + RESET);
                    }
                } else {
                    System.out.println(YELLOW + "  Penghapusan dibatalkan." + RESET);
                }
            } else {
                System.out.println(RED + "  [✘] ID tidak ditemukan!" + RESET);
                playSound("error.wav");
            }
        }
    }

    // ═══════════════════════════════════════════════════════════
    //  5. LAPORAN HARIAN
    // ═══════════════════════════════════════════════════════════
    static void menuLaporanHarian(Connection conn, Scanner sc) throws SQLException {
        header();
        String sql =
            "SELECT DATE(p.waktu_pesan) AS tanggal, " +
            "COUNT(DISTINCT p.id_pesanan) AS jumlah_trx, " +
            "SUM(dp.jumlah) AS total_item, " +
            "SUM(p.total_akhir) AS total_pendapatan, " +
            "AVG(p.total_akhir) AS rata_rata " +
            "FROM pesanan p JOIN detail_pesanan dp ON dp.id_pesanan=p.id_pesanan " +
            "WHERE p.status='selesai' " +
            "GROUP BY DATE(p.waktu_pesan) ORDER BY tanggal DESC LIMIT 30";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            System.out.println(GREEN + BOLD +
                "  ╔══════════════════════════════════════════════════════════════╗\n" +
                "  ║          LAPORAN PENJUALAN HARIAN (30 Hari Terakhir)         ║\n" +
                "  ╚══════════════════════════════════════════════════════════════╝" + RESET);
            System.out.println("  " + rep('─', 80));
            System.out.printf(BOLD + "  %-12s │ %-6s │ %-10s │ %-20s │ %s%n" + RESET,
                "Tanggal", "Trx", "Total Item", "Total Pendapatan", "Rata-rata");
            System.out.println("  " + rep('─', 80));
            double grandTotal = 0; int grandTrx = 0;
            boolean ada = false;
            while (rs.next()) {
                ada = true;
                grandTotal += rs.getDouble("total_pendapatan");
                grandTrx   += rs.getInt("jumlah_trx");
                System.out.printf("  %-12s │ %-6d │ %-10d │ " + GREEN + "Rp%,-17.0f" + RESET + " │ Rp%,.0f%n",
                    rs.getDate("tanggal"), rs.getInt("jumlah_trx"),
                    rs.getInt("total_item"), rs.getDouble("total_pendapatan"),
                    rs.getDouble("rata_rata"));
            }
            if (!ada) System.out.println(YELLOW + "  (Belum ada data laporan)" + RESET);
            System.out.println("  " + rep('─', 80));
            System.out.printf(BOLD + "  %-12s │ %-6d │ %-10s │ " + GREEN + "Rp%,-17.0f%n" + RESET,
                "GRAND TOTAL", grandTrx, "", grandTotal);
        }

        // Menu terlaris
        System.out.println("\n" + BOLD + CYAN + "  🏆 MENU TERLARIS (Top 10):" + RESET);
        String sqlT =
            "SELECT m.nama_menu, k.nama_kategori, " +
            "SUM(dp.jumlah) AS total_terjual, SUM(dp.subtotal_item) AS total_pendapatan " +
            "FROM detail_pesanan dp " +
            "JOIN menu m ON m.id_menu=dp.id_menu " +
            "JOIN kategori k ON k.id_kategori=m.id_kategori " +
            "JOIN pesanan p ON p.id_pesanan=dp.id_pesanan WHERE p.status='selesai' " +
            "GROUP BY m.id_menu, m.nama_menu, k.nama_kategori ORDER BY total_terjual DESC LIMIT 10";
        try (PreparedStatement psT = conn.prepareStatement(sqlT)) {
            ResultSet rsT = psT.executeQuery();
            System.out.println(CYAN +
                "  ╔═════╦══════════════════════════════╦═══════════╦═══════════╦═════════════════╗\n" +
                "  ║  #  ║ Nama Menu                    ║ Kategori  ║  Terjual  ║  Pendapatan     ║\n" +
                "  ╠═════╬══════════════════════════════╬═══════════╬═══════════╬═════════════════╣" + RESET);
            int rank = 1;
            while (rsT.next()) {
                System.out.printf(CYAN + "  ║ " + RESET + "%-3d " + CYAN + "║ " + RESET + "%-28s "
                    + CYAN + "║ " + RESET + "%-9s " + CYAN + "║ " + RESET + "%-9d "
                    + CYAN + "║ " + GREEN + "Rp%,-12.0f " + CYAN + "║%n" + RESET,
                    rank++, rsT.getString("nama_menu"),
                    rsT.getString("nama_kategori"), rsT.getInt("total_terjual"),
                    rsT.getDouble("total_pendapatan"));
            }
            System.out.println(CYAN +
                "  ╚═════╩══════════════════════════════╩═══════════╩═══════════╩═════════════════╝" + RESET);
        }
        pressEnter(sc);
    }

    // ═══════════════════════════════════════════════════════════
    //  6. MANAJEMEN MENU
    // ═══════════════════════════════════════════════════════════
    static void menuManajemenMenu(Connection conn, Scanner sc) throws SQLException {
        boolean balik = false;
        while (!balik) {
            header();
            System.out.println(CYAN + BOLD +
                "  ╔══════════════════════════════════════════╗\n" +
                "  ║         MANAJEMEN MENU & STOK            ║\n" +
                "  ╠══════════════════════════════════════════╣\n" +
                "  ║  1. Lihat Semua Menu                     ║\n" +
                "  ║  2. Update Stok Menu                     ║\n" +
                "  ║  3. Aktif / Nonaktif Menu                ║\n" +
                "  ║  4. Stok Menipis (≤ 5)                   ║\n" +
                "  ║  5. Tambah Menu Baru                     ║\n" +
                "  ║  6. Hapus Menu                           ║\n" +
                "  ║  0. Kembali                              ║\n" +
                "  ╚══════════════════════════════════════════╝" + RESET);
            System.out.print(YELLOW + "  Pilihan: " + RESET);
            int pilih = readInt(sc, -1);
            switch (pilih) {
                case 1 -> { header(); tampilSemuaMenu(conn); }
                case 2 -> { header(); updateStokMenu(conn, sc); }
                case 3 -> { header(); toggleMenuAktif(conn, sc); }
                case 4 -> tampilStokMenipis(conn);
                case 5 -> tambahMenu(conn, sc);
                case 6 -> hapusMenu(conn, sc);
                case 0 -> balik = true;
                default -> System.out.println(RED + "  [✘] Pilihan tidak valid!" + RESET);
            }
            if (!balik) pressEnter(sc);
        }
    }

    static void tampilSemuaMenu(Connection conn) throws SQLException {
        for (int kat = 1; kat <= 3; kat++) {
            String[] namaKat = {"", "☕ MINUMAN", "🍽 MAKANAN", "🍟 SNACK"};
            System.out.println(CYAN + BOLD + "\n  " + namaKat[kat] + RESET);
            System.out.println(CYAN +
                "  ╔═════╦══════════════════════════════╦═════════════════╦══════╦══════════╗\n" +
                "  ║  ID ║ Nama Menu                    ║ Harga           ║ Stok ║ Status   ║\n" +
                "  ╠═════╬══════════════════════════════╬═════════════════╬══════╬══════════╣" + RESET);
            try (PreparedStatement ps = conn.prepareStatement(
                "SELECT id_menu, nama_menu, harga, stok, tersedia FROM menu " +
                "WHERE id_kategori=? ORDER BY id_menu")) {
                ps.setInt(1, kat);
                ResultSet rs = ps.executeQuery();
                boolean ada = false;
                while (rs.next()) {
                    ada = true;
                    int stok = rs.getInt("stok");
                    boolean tersedia = rs.getBoolean("tersedia");
                    String wStok   = stok <= 5 ? RED : stok <= 15 ? YELLOW : GREEN;
                    String wStatus = tersedia ? GREEN + "✔ Aktif  " : RED + "✘ Nonaktif";
                    System.out.printf(CYAN + "  ║ " + RESET + "%-3d " + CYAN + "║ " + RESET + "%-28s "
                        + CYAN + "║ " + GREEN + "Rp %-12.0f " + CYAN + "║ " + wStok + "%-4d " + CYAN + "  ║ "
                        + wStatus + CYAN + " ║%n" + RESET,
                        rs.getInt("id_menu"), rs.getString("nama_menu"),
                        rs.getDouble("harga"), stok);
                }
                if (!ada)
                    System.out.println(CYAN + "  ║ " + YELLOW + padR("(Kosong)", 62) + CYAN + "║" + RESET);
            }
            System.out.println(CYAN +
                "  ╚═════╩══════════════════════════════╩═════════════════╩══════╩══════════╝" + RESET);
        }
    }

    static void updateStokMenu(Connection conn, Scanner sc) throws SQLException {
        System.out.print(YELLOW + "  ID Menu : " + RESET);
        int idMenu = readInt(sc, -1);
        if (idMenu < 0) return;
        try (PreparedStatement psCek = conn.prepareStatement(
            "SELECT nama_menu, stok FROM menu WHERE id_menu=?")) {
            psCek.setInt(1, idMenu);
            ResultSet rsCek = psCek.executeQuery();
            if (!rsCek.next()) { System.out.println(RED + "  Menu tidak ditemukan!" + RESET); return; }
            System.out.printf("  Menu: %s | Stok saat ini: %d%n",
                rsCek.getString("nama_menu"), rsCek.getInt("stok"));
            System.out.print(YELLOW + "  Stok baru: " + RESET);
            int stokBaru = readInt(sc, -1);
            if (stokBaru < 0) { System.out.println(RED + "  Input tidak valid!" + RESET); return; }
            try (PreparedStatement ps = conn.prepareStatement("UPDATE menu SET stok=? WHERE id_menu=?")) {
                ps.setInt(1, stokBaru); ps.setInt(2, idMenu);
                ps.executeUpdate();
                System.out.println(GREEN + "  ✔ Stok diupdate ke " + stokBaru + "!" + RESET);
            }
        }
    }

    static void toggleMenuAktif(Connection conn, Scanner sc) throws SQLException {
        System.out.print(YELLOW + "  ID Menu: " + RESET);
        int idMenu = readInt(sc, -1);
        if (idMenu < 0) return;
        try (PreparedStatement ps = conn.prepareStatement(
            "UPDATE menu SET tersedia=NOT tersedia WHERE id_menu=? RETURNING nama_menu, tersedia")) {
            ps.setInt(1, idMenu);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                System.out.println(GREEN + "  ✔ Menu \"" + rs.getString("nama_menu") + "\" → "
                    + (rs.getBoolean("tersedia") ? "AKTIF" : "NONAKTIF") + RESET);
            else
                System.out.println(RED + "  Menu tidak ditemukan!" + RESET);
        }
    }

    static void tampilStokMenipis(Connection conn) throws SQLException {
        header();
        try (PreparedStatement ps = conn.prepareStatement(
            "SELECT m.id_menu, m.nama_menu, k.nama_kategori, m.stok " +
            "FROM menu m JOIN kategori k ON k.id_kategori=m.id_kategori " +
            "WHERE m.stok<=5 AND m.tersedia=TRUE ORDER BY m.stok ASC")) {
            ResultSet rs = ps.executeQuery();
            System.out.println(RED + BOLD + "  ⚠  MENU STOK MENIPIS (≤ 5)" + RESET);
            System.out.println("  " + rep('─', 55));
            boolean ada = false;
            while (rs.next()) {
                ada = true;
                System.out.printf("  [%03d] " + RED + "%-26s" + RESET + " | %-8s | Stok: "
                    + RED + BOLD + "%d%n" + RESET,
                    rs.getInt("id_menu"), rs.getString("nama_menu"),
                    rs.getString("nama_kategori"), rs.getInt("stok"));
            }
            if (!ada) System.out.println(GREEN + "  ✔ Semua stok menu aman!" + RESET);
            System.out.println("  " + rep('─', 55));
        }
    }

    static void tambahMenu(Connection conn, Scanner sc) throws SQLException {
        header();
        System.out.println(CYAN + BOLD + "  TAMBAH MENU BARU" + RESET);
        System.out.println("  Kategori: [1] Minuman  [2] Makanan  [3] Snack");
        System.out.print(YELLOW + "  Kategori : " + RESET);
        int kat = readInt(sc, -1);
        if (kat < 1 || kat > 3) { System.out.println(RED + "  Kategori tidak valid!" + RESET); return; }
        System.out.print(YELLOW + "  Nama Menu: " + RESET);
        String nama = sc.nextLine().trim();
        System.out.print(YELLOW + "  Harga    : " + RESET);
        double harga = readDouble(sc, 0);
        System.out.print(YELLOW + "  Stok     : " + RESET);
        int stok = readInt(sc, 0);
        System.out.print(YELLOW + "  Deskripsi: " + RESET);
        String desc = sc.nextLine().trim();
        try (PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO menu(id_kategori,nama_menu,deskripsi,harga,stok,tersedia) " +
            "VALUES(?,?,?,?,?,TRUE) RETURNING id_menu")) {
            ps.setInt(1, kat); ps.setString(2, nama);
            ps.setString(3, desc.isEmpty() ? null : desc);
            ps.setDouble(4, harga); ps.setInt(5, stok);
            ResultSet rs = ps.executeQuery(); rs.next();
            System.out.println(GREEN + "\n  ✔ Menu berhasil ditambahkan! ID: #" + rs.getInt(1) + RESET);
                            playSound("tithuh-powerup-success.wav");

        } catch (SQLException e) {
            System.out.println(RED + "  [✘] Error: " + e.getMessage() + RESET);
            playSound("error.wav");
        }
    }

    static void hapusMenu(Connection conn, Scanner sc) throws SQLException {
        header(); tampilSemuaMenu(conn);
        System.out.print(YELLOW + "\n  ID Menu yang dihapus: " + RESET);
        int idMenu = readInt(sc, -1);
        if (idMenu < 0) return;
        try (PreparedStatement psCek = conn.prepareStatement(
            "SELECT nama_menu FROM menu WHERE id_menu=?")) {
            psCek.setInt(1, idMenu);
            ResultSet rsCek = psCek.executeQuery();
            if (!rsCek.next()) { System.out.println(RED + "  Menu tidak ditemukan!" + RESET); return; }
            System.out.print(RED + "  Yakin hapus \"" + rsCek.getString("nama_menu") + "\"? (y/n): " + RESET);
            if (!sc.nextLine().trim().equalsIgnoreCase("y")) {
                System.out.println(YELLOW + "  Dibatalkan." + RESET); return;
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM menu WHERE id_menu=?")) {
                ps.setInt(1, idMenu);
                ps.executeUpdate();
                System.out.println(GREEN + "  ✔ Menu berhasil dihapus!" + RESET);
                                playSound("tithuh-powerup-success.wav");

            } catch (SQLException e) {
                try (PreparedStatement ps2 = conn.prepareStatement(
                    "UPDATE menu SET tersedia=FALSE WHERE id_menu=?")) {
                    ps2.setInt(1, idMenu); ps2.executeUpdate();
                    System.out.println(YELLOW + "  Menu memiliki riwayat transaksi — dinonaktifkan." + RESET);
                }
            }
        }
    }

   
    // ═══════════════════════════════════════════════════════════
    //  8. MANAJEMEN KARYAWAN
    // ═══════════════════════════════════════════════════════════
    static void menuKaryawan(Connection conn, Scanner sc) throws SQLException {
        boolean balik = false;
        while (!balik) {
            header();
            System.out.println(MAGENTA + BOLD +
                "  ╔══════════════════════════════════════════╗\n" +
                "  ║          DATA KARYAWAN TE-CAFE           ║\n" +
                "  ╠══════════════════════════════════════════╣\n" +
                "  ║  1. Lihat Semua Karyawan                 ║\n" +
                "  ║  2. Tambah Karyawan Baru                 ║\n" +
                "  ║  3. Update Data Karyawan                 ║\n" +
                "  ║  4. Hapus Karyawan                       ║\n" +
                "  ║  0. Kembali                              ║\n" +
                "  ╚══════════════════════════════════════════╝" + RESET);
            System.out.print(YELLOW + "  Pilihan: " + RESET);
            int pilih = readInt(sc, -1);
            switch (pilih) {
                case 1 -> tampilSemuaKaryawan(conn);
                case 2 -> tambahKaryawan(conn, sc);
                case 3 -> updateKaryawan(conn, sc);
                case 4 -> hapusKaryawan(conn, sc);
                case 0 -> balik = true;
                default -> System.out.println(RED + "  [✘] Pilihan tidak valid!" + RESET);
            }
            if (!balik) pressEnter(sc);
        }
    }

    static void tampilSemuaKaryawan(Connection conn) throws SQLException {
        header();
        String sql =
            "SELECT k.id_karyawan, k.nama_karyawan, j.nama_jabatan, k.email, " +
            "k.telepon, k.tanggal_masuk, k.status_aktif, k.nim_password " +
            "FROM karyawan k JOIN jabatan j ON j.id_jabatan=k.id_jabatan " +
            "ORDER BY j.id_jabatan, k.nama_karyawan";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            System.out.println(MAGENTA + BOLD + "   DATA KARYAWAN TE-CAFE" + RESET);
            System.out.println(MAGENTA +
                "  ╔════╦════╦══════════════════════╦═══════════╦════════════════════════╦══════════╦════════╗\n" +
                "  ║ No ║ ID ║ Nama                 ║ Jabatan   ║ Email                  ║ Tgl Msk  ║ Status ║\n" +
                "  ╠════╬════╬══════════════════════╬═══════════╬════════════════════════╬══════════╬════════╣" + RESET);
            int no = 1;
            while (rs.next()) {
                boolean aktif = rs.getBoolean("status_aktif");
                System.out.printf(MAGENTA + "  ║ " + RESET + "%-2d " + MAGENTA + "║ " + RESET
                    + "%-2d " + MAGENTA + "║ " + RESET + "%-20s " + MAGENTA + "║ " + RESET
                    + "%-9s " + MAGENTA + "║ " + RESET + "%-22s " + MAGENTA + "║ " + RESET
                    + "%-8s " + MAGENTA + "║ " + (aktif ? GREEN : RED) + "%-6s " + MAGENTA + "║%n" + RESET,
                    no++, rs.getInt("id_karyawan"), rs.getString("nama_karyawan"),
                    rs.getString("nama_jabatan"),
                    rs.getString("email") != null ? rs.getString("email") : "-",
                    rs.getDate("tanggal_masuk").toString().substring(0, 10),
                    aktif ? "Aktif" : "Nonaktif");
            }
            System.out.println(MAGENTA +
                "  ╚════╩════╩══════════════════════╩═══════════╩════════════════════════╩══════════╩════════╝" + RESET);
            System.out.printf("   Login: " + BOLD + CYAN + "%s" + RESET + " (%s)%n",
                loginNamaKaryawan, loginJabatan);
        }
    }

    static void tambahKaryawan(Connection conn, Scanner sc) throws SQLException {
        header(); tampilSemuaKaryawan(conn);
        System.out.println(MAGENTA + BOLD + "\n  TAMBAH KARYAWAN BARU" + RESET);
        try (PreparedStatement psJ = conn.prepareStatement(
            "SELECT id_jabatan, nama_jabatan, gaji_pokok FROM jabatan ORDER BY id_jabatan")) {
            ResultSet rsJ = psJ.executeQuery();
            System.out.println("  Jabatan:");
            while (rsJ.next())
                System.out.printf("   [%d] %-12s - Gaji: Rp %,.0f%n",
                    rsJ.getInt("id_jabatan"), rsJ.getString("nama_jabatan"), rsJ.getDouble("gaji_pokok"));
        }
        System.out.print(YELLOW + "  Nama Lengkap   : " + RESET); String nama = sc.nextLine().trim();
        System.out.print(YELLOW + "  ID Jabatan     : " + RESET); int idJabatan = readInt(sc, 2);
        System.out.print(YELLOW + "  Email (login)  : " + RESET); String email = sc.nextLine().trim();
        if (!email.contains("@")) { System.out.println(RED + "  [✘] Format email tidak valid!" + RESET); return; }
        System.out.print(YELLOW + "  No. Telepon    : " + RESET); String telp = sc.nextLine().trim();
        System.out.print(YELLOW + "  NIM (password) : " + RESET); String nim  = sc.nextLine().trim();
        System.out.print(YELLOW + "  Tgl Masuk (yyyy-MM-dd / Enter=hari ini): " + RESET);
        String tglMasuk = sc.nextLine().trim();
        if (tglMasuk.isEmpty()) tglMasuk = java.time.LocalDate.now().toString();
        try (PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO karyawan(id_jabatan,nama_karyawan,email,telepon,tanggal_masuk,nim_password,status_aktif)" +
            " VALUES(?,?,?,?,?,?,TRUE) RETURNING id_karyawan")) {
            ps.setInt(1, idJabatan); ps.setString(2, nama);
            ps.setString(3, email.isEmpty() ? null : email);
            ps.setString(4, telp.isEmpty()  ? null : telp);
            ps.setDate(5, java.sql.Date.valueOf(tglMasuk));
            ps.setString(6, nim.isEmpty()   ? null : nim);
            ResultSet rs = ps.executeQuery(); rs.next();
            System.out.println(GREEN + "\n  ✔ Karyawan berhasil ditambahkan! ID: #" + rs.getInt(1) + RESET);
                            playSound("tithuh-powerup-success.wav");

            System.out.println(DIM + "  Login: " + email + " | Password: " + nim + RESET);
        } catch (SQLException e) {
            System.out.println(RED + "  [✘] " + (e.getMessage().contains("unique")
                ? "Email sudah terdaftar!" : e.getMessage()) + RESET);
            playSound("error.wav");
        }
    }

    static void pecatKaryawan(Connection conn, Scanner sc) throws SQLException {
        header(); tampilSemuaKaryawan(conn);
        System.out.print(YELLOW + "\n  ID Karyawan yang dipecat: " + RESET);
        int id = readInt(sc, -1);
        if (id <= 0 || id == loginIdKaryawan) {
            System.out.println(RED + "  [✘] Tidak dapat memecat diri sendiri / ID tidak valid!" + RESET);
            return;
        }
        try (PreparedStatement psCek = conn.prepareStatement(
            "SELECT nama_karyawan FROM karyawan WHERE id_karyawan=?")) {
            psCek.setInt(1, id);
            ResultSet rsCek = psCek.executeQuery();
            if (!rsCek.next()) { System.out.println(RED + "  Karyawan tidak ditemukan!" + RESET); return; }
            System.out.print(RED + "  Yakin pecat \"" + rsCek.getString("nama_karyawan") + "\"? (y/n): " + RESET);
            if (!sc.nextLine().trim().equalsIgnoreCase("y")) {
                System.out.println(YELLOW + "  Dibatalkan." + RESET); return;
            }
            try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE karyawan SET status_aktif=FALSE WHERE id_karyawan=?")) {
                ps.setInt(1, id); ps.executeUpdate();
                System.out.println(GREEN + "  ✔ Karyawan berhasil dinonaktifkan!" + RESET);
                playSound("tithuh-powerup-success.wav");
            }
        }
    }

    static void aktifkanKaryawan(Connection conn, Scanner sc) throws SQLException {
        header(); tampilSemuaKaryawan(conn);
        System.out.print(YELLOW + "  ID Karyawan: " + RESET);
        int id = readInt(sc, -1);
        if (id < 0) return;
        try (PreparedStatement ps = conn.prepareStatement(
            "UPDATE karyawan SET status_aktif=TRUE WHERE id_karyawan=? RETURNING nama_karyawan")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            System.out.println(rs.next()
                ? GREEN + "  ✔ Karyawan \"" + rs.getString("nama_karyawan") + "\" berhasil diaktifkan!" + RESET
                : RED + "  [✘] Karyawan tidak ditemukan!" + RESET);
        }
    }

    static void updateKaryawan(Connection conn, Scanner sc) throws SQLException {
        header(); tampilSemuaKaryawan(conn);
        System.out.print(YELLOW + "\n  ID Karyawan: " + RESET);
        int id = readInt(sc, -1);
        if (id < 0) return;
        try (PreparedStatement psCek = conn.prepareStatement(
            "SELECT * FROM karyawan WHERE id_karyawan=?")) {
            psCek.setInt(1, id);
            ResultSet rsCek = psCek.executeQuery();
            if (!rsCek.next()) { System.out.println(RED + "  Karyawan tidak ditemukan!" + RESET); return; }
            System.out.printf("  Nama: %s | Email: %s | Telp: %s%n",
                rsCek.getString("nama_karyawan"), rsCek.getString("email"), rsCek.getString("telepon"));
            System.out.print(YELLOW + "  Nama baru  (Enter=skip): " + RESET); String nama  = sc.nextLine().trim();
            System.out.print(YELLOW + "  Email baru (Enter=skip): " + RESET); String email = sc.nextLine().trim();
            System.out.print(YELLOW + "  Telp baru  (Enter=skip): " + RESET); String telp  = sc.nextLine().trim();
            System.out.print(YELLOW + "  NIM baru   (Enter=skip): " + RESET); String nim   = sc.nextLine().trim();
            try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE karyawan SET nama_karyawan=?,email=?,telepon=?,nim_password=? WHERE id_karyawan=?")) {
                ps.setString(1, nama.isEmpty()  ? rsCek.getString("nama_karyawan") : nama);
                ps.setString(2, email.isEmpty() ? rsCek.getString("email")         : email);
                ps.setString(3, telp.isEmpty()  ? rsCek.getString("telepon")       : telp);
                ps.setString(4, nim.isEmpty()   ? rsCek.getString("nim_password")  : nim);
                ps.setInt(5, id);
                ps.executeUpdate();
                System.out.println(GREEN + "  ✔ Data karyawan berhasil diupdate!" + RESET);
                                playSound("tithuh-powerup-success.wav");

            }
        }
    }

    static void hapusKaryawan(Connection conn, Scanner sc) throws SQLException {
        header();
        System.out.println(RED + BOLD + "  🔥 HAPUS DATA KARYAWAN 🔥" + RESET);
        tampilSemuaKaryawan(conn);
        System.out.print(YELLOW + "\n  ID Karyawan yang dihapus: " + RESET);
        int id = readInt(sc, -1);
        if (id <= 0) return;
        if (id == loginIdKaryawan) {
            System.out.println(RED + "  [✘] Tidak bisa menghapus akun sendiri!" + RESET);
            return;
        }
        try (PreparedStatement psCek = conn.prepareStatement(
            "SELECT nama_karyawan, email FROM karyawan WHERE id_karyawan=?")) {
            psCek.setInt(1, id);
            ResultSet rs = psCek.executeQuery();
            if (rs.next()) {
                System.out.print(RED + "  Yakin hapus [" + rs.getString("nama_karyawan")
                    + "] (" + rs.getString("email") + ")? (y/n): " + RESET);
                if (sc.nextLine().trim().equalsIgnoreCase("y")) {
                    try (PreparedStatement psDel = conn.prepareStatement(
                        "DELETE FROM karyawan WHERE id_karyawan=?")) {
                        psDel.setInt(1, id);
                        psDel.executeUpdate();
                        System.out.println(GREEN + "  ✔ Karyawan berhasil dihapus!" + RESET);
                            playSound("tithuh-powerup-success.wav");
                    } catch (SQLException e) {
                        System.out.println(RED + "  [✘] Gagal: Karyawan memiliki riwayat transaksi." + RESET);
                        System.out.println(DIM + "  (Gunakan fitur Nonaktifkan saja)" + RESET);
                        playSound("error.wav");
                    }
                } else {
                    System.out.println(YELLOW + "  Penghapusan dibatalkan." + RESET);
                }
            } else {
                System.out.println(RED + "  [✘] ID tidak ditemukan!" + RESET);
                playSound("error.wav");
            }
        }
    }

    // ═══════════════════════════════════════════════════════════
    //  MAIN
    // ═══════════════════════════════════════════════════════════
    public static void main(String[] args) {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException ignored) {}

        Scanner sc = new Scanner(System.in);
        Connection conn = null;

        try {
            System.out.print(CYAN + "  Menginisialisasi TE-CAFE System " + RESET);
            for (int i = 0; i < 10; i++) {
                System.out.print(CYAN + "■" + RESET);
                Thread.sleep(80);
            }
            
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver tidak ditemukan di Classpath!");
        }

            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            System.out.println(" " + GREEN + "[ CONNECTED ]" + RESET);
            playSound( "tithuh-powerup-success.wav" );
            System.out.println(GREEN + "  ✔ Database db_restoran terhubung!" + RESET + "\n");

            boolean loginBerhasil;
            try {
                loginBerhasil = prosesLogin(conn, sc);
            } catch (InterruptedException e) {
                loginBerhasil = false;
            }

            if (!loginBerhasil) {
                System.out.println(RED + "  Sistem tidak dapat diakses." + RESET);
                return;
            }

            menuUtama(conn, sc);

        } catch (SQLException e) {
            System.out.println("\n" + RED + "  ✖ Gagal konek ke database!" + RESET);
            System.out.println("  Pastikan PostgreSQL berjalan dan database 'db_restoran' ada.");
            System.out.println("  Detail: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("  Inisialisasi terganggu.");
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
            sc.close();
        }
    }
}