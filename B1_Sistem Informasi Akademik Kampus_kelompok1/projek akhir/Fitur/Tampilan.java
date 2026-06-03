package Fitur;

public class Tampilan {
    // ===================== HEADER =====================
    public static void tampilHeader(String nama, String role, String tahun, String semester_aktif) {
        String judulSistem = "SISTEM INFORMASI AKADEMIK - UNIVERSITAS SUMATERA UTARA";
        String barisNama     = "Nama     : " + nama;
        String barisRole     = "Role     : " + role;
        String barisTahun    = "Tahun    : " + tahun;
        String barisSemester = "Semester : " + semester_aktif;

        int lebar = judulSistem.length();

        lebar = Math.max(lebar, barisNama.length());
        lebar = Math.max(lebar, barisRole.length());
        lebar = Math.max(lebar, barisTahun.length());
        lebar = Math.max(lebar, barisSemester.length());

        lebar += 4; // padding kiri kanan

        String garis = "═".repeat(lebar);

        System.out.println("╔" + garis + "╗");
        System.out.println("║" + center(judulSistem, lebar) + "║");
        System.out.println("╠" + garis + "╣");

        System.out.println("║ " + padKanan(barisNama, lebar - 2) + " ║");
        System.out.println("║ " + padKanan(barisRole, lebar - 2) + " ║");
        System.out.println("║ " + padKanan(barisTahun, lebar - 2) + " ║");
        System.out.println("║ " + padKanan(barisSemester, lebar - 2) + " ║");

        System.out.println("╚" + garis + "╝");
        System.out.println();
    }

    public static String center(String text, int width) {
        if (text.length() >= width) return text;

        int padding = (width - text.length()) / 2;
        int padRight = width - text.length() - padding;

        return " ".repeat(padding) + text + " ".repeat(padRight);
    }

    public static String padKanan(String text, int width) {
        if (text.length() >= width) return text;
        return text + " ".repeat(width - text.length());
    }

    // ===================== TAMPILAN MENU MAHASISWA =====================
    public static void tampilMenuMahasiswa() {
        String[] menu = {
            "[1] Profil Anda",
            "[2] Kelas Kuliah",
            "[3] Kelola KRS",
            "[4] Jadwal",
            "[5] Presensi",
            "[6] Kartu Hasil Studi (KHS)",
            "[7] Transkrip Nilai",
            "[8] Logout",
            "[0] Keluar"
        };
        tampilMenu("MENU MAHASISWA", menu);
    }

    // ===================== MENU DOSEN =====================
    public static void tampilMenuDosen() {
        String[] menu = {
            "[1] Lihat Data Mahasiswa",
            "[2] Kelas Saya",
            "[3] Kelola KRS Mahasiswa",
            "[4] Input Nilai",
            "[5] Input Presensi",
            "[6] Rekap Kehadiran Mahasiswa",
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
            System.out.println("║ " + padKanan(item, lebar - 2) + " ║");
        }

        System.out.println("╚" + garis + "╝");
        System.out.print("Pilih Menu: ");
    }

    // ===================== TABEL DATA DINAMIS =====================
    public static void tampilTabel(String[] headers, String[][] rows) {

        int[] lebar = new int[headers.length];

        for (int i = 0; i < headers.length; i++) {
            lebar[i] = headers[i].length();
        }

        for (String[] row : rows) {
            for (int i = 0; i < row.length && i < lebar.length; i++) {
                String val = (row[i] == null) ? "-" : row[i];
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
            System.out.println("| Tidak ada data |");
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
    public static void sukses(String pesan)     {System.out.println(pesan);}
    public static void gagal(String pesan)      {System.out.println(pesan);}
    public static void peringatan(String pesan) {System.out.println(pesan);}

    // ===================== HELPER =====================
    private static String tengah(String teks, int lebar) {
        if (teks.length() >= lebar) return teks;

        int sisa = lebar - teks.length();
        int kiri = sisa / 2;
        int kanan = sisa - kiri;

        return " ".repeat(kiri) + teks + " ".repeat(kanan);
    }

    private static String buatGaris(int[] lebar) {
        StringBuilder sb = new StringBuilder("+");
        for (int l : lebar) {
            sb.append("-".repeat(l + 2)).append("+");
        }
        return sb.toString();
    }
}