package Fitur;

public class Tampilan {

    // ===================== HEADER =====================
    public static void tampilHeader(String nama, String role, String tahun, String semester_aktif) {
        String judulSistem = "SISTEM INFORMASI AKADEMIK - UNIVERSITAS SUMATERA UTARA";
        String infoUser    = "  Nama : " + nama + "   |   Role : " + role;
        String infoTahun   = "  Tahun : " + tahun + "   |   Semester : " + semester_aktif;

        // Lebar otomatis mengikuti konten terpanjang
        int isiMax = judulSistem.length();
        if (infoUser.length()  > isiMax) isiMax = infoUser.length();
        if (infoTahun.length() > isiMax) isiMax = infoTahun.length();
        int lebar = isiMax + 2;

        String garis = "═".repeat(lebar);

        System.out.println("╔" + garis + "╗");
        System.out.println("║" + tengah(judulSistem, lebar) + "║");
        System.out.println("╠" + garis + "╣");
        System.out.println("║" + padKanan(infoUser,  lebar) + "║");
        System.out.println("║" + padKanan(infoTahun, lebar) + "║");
        System.out.println("╚" + garis + "╝");
        System.out.println();
    }

    // ===================== MENU MAHASISWA =====================
    public static void tampilMenuMahasiswa() {
        String[] menu = {
            "[1] Lihat Data Diri",
            "[2] Lihat Kelas Tersedia",
            "[3] Lihat KRS",
            "[4] Ambil KRS",
            "[5] Lihat Jadwal",
            "[6] Lihat Nilai",
            "[7] Rekap Presensi",
            "[8] Logout",
            "[0] Keluar"
        };
        tampilMenu("MENU MAHASISWA", menu);
    }

    // ===================== MENU DOSEN =====================
    public static void tampilMenuDosen() {
        String[] menu = {
            "[1] Lihat Data Mahasiswa",
            "[2] Lihat Kelas Saya",
            "[3] Kelola KRS Mahasiswa",
            "[4] Input Nilai",
            "[5] Input Presensi",
            "[6] Rekap Kehadiran",
            "[7] Logout",
            "[0] Keluar"
        };
        tampilMenu("MENU DOSEN", menu);
    }

    // ===================== MENU DINAMIS =====================
    private static void tampilMenu(String judul, String[] items) {
        int lebar = judul.length();
        for (String item : items) {
            if (item.length() > lebar) lebar = item.length();
        }
        lebar += 4;

        String garis = "═".repeat(lebar);
        System.out.println("╔" + garis + "╗");
        System.out.println("║" + tengah(judul, lebar) + "║");
        System.out.println("╠" + garis + "╣");
        for (String item : items) {
            System.out.println("║  " + padKanan(item, lebar - 2) + "║");
        }
        System.out.println("╚" + garis + "╝");
        System.out.print("Pilih menu: ");
    }

    // ===================== TABEL DATA DINAMIS =====================
    public static void tampilTabel(String[] headers, String[][] rows) {
        int[] lebar = new int[headers.length];

        for (int i = 0; i < headers.length; i++) {
            lebar[i] = headers[i].length();
        }

        for (String[] row : rows) {
            for (int i = 0; i < row.length && i < lebar.length; i++) {
                String val = row[i] != null ? row[i] : "-";
                if (val.length() > lebar[i]) lebar[i] = val.length();
            }
        }

        String garis = buatGaris(lebar);

        System.out.println(garis);
        System.out.print("| ");
        for (int i = 0; i < headers.length; i++) {
            System.out.print(padKanan(headers[i], lebar[i]) + " | ");
        }
        System.out.println();
        System.out.println(garis);

        if (rows.length == 0) {
            int totalLebar = 0;
            for (int l : lebar) totalLebar += l + 3;
            System.out.println("| " + padKanan("Tidak ada data.", totalLebar - 1) + " |");
        } else {
            for (String[] row : rows) {
                System.out.print("| ");
                for (int i = 0; i < lebar.length; i++) {
                    String val = (i < row.length && row[i] != null) ? row[i] : "-";
                    System.out.print(padKanan(val, lebar[i]) + " | ");
                }
                System.out.println();
            }
        }
        System.out.println(garis);
    }

    // ===================== PESAN =====================
    public static void sukses(String pesan) {
        System.out.println(pesan);
    }

    public static void gagal(String pesan) {
        System.out.println(pesan);
    }

    public static void peringatan(String pesan) {
        System.out.println(pesan);
    }

    // ===================== HELPER =====================
    private static String tengah(String teks, int lebar) {
        if (teks.length() >= lebar) return teks;
        int sisa  = lebar - teks.length();
        int kiri  = sisa / 2;
        int kanan = sisa - kiri;
        return " ".repeat(kiri) + teks + " ".repeat(kanan);
    }

    private static String padKanan(String teks, int lebar) {
        if (teks.length() >= lebar) return teks;
        return teks + " ".repeat(lebar - teks.length());
    }

    private static String buatGaris(int[] lebar) {
        StringBuilder sb = new StringBuilder("+");
        for (int l : lebar) {
            sb.append("-".repeat(l + 2)).append("+");
        }
        return sb.toString();
    }
}