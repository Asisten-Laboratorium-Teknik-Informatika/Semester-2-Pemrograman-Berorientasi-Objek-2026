package Fitur;

import Entitas.Jadwal;
import Entitas.KRS;
import Entitas.KelasKuliah;
import Entitas.Mahasiswa;
import Entitas.Nilai;
import Entitas.Presensi;
import Entitas.TahunAkademik;
import java.util.Scanner;

public class Dashboard {

    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        menu();
    }

    public static void menu() {
        int pilih;
        do {
            // Ambil tahun dan semester aktif
            TahunAkademik ta = new TahunAkademik();
            String[] infoTahun = ta.getInfoTahunAktif();
            String tahun    = (infoTahun != null) ? infoTahun[0] : "-";
            String semester = (infoTahun != null) ? infoTahun[1] : "-";

            // Panggil header dengan tahun & semester
            Tampilan.tampilHeader(Session.nama, Session.role, tahun, semester);

            if ("Mahasiswa".equals(Session.role)) {
                Tampilan.tampilMenuMahasiswa();
            } else if ("Dosen".equals(Session.role)) {
                Tampilan.tampilMenuDosen();
            } else {
                Tampilan.gagal("Role tidak dikenali!");
                break;
            }

            try {
                pilih = Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                Tampilan.gagal("Input tidak valid, masukkan angka!");
                pilih = -1;
                continue;
            }

            if ("Mahasiswa".equals(Session.role)) {
                pilih = handleMahasiswa(pilih);
            } else {
                pilih = handleDosen(pilih);
            }

        } while (pilih != 0);
    }

    // ===================== HANDLER MAHASISWA =====================
    private static int handleMahasiswa(int pilih) {
        switch (pilih) {
            case 1:
                new Mahasiswa().lihatDataDiri(Session.nim);
                break;
            case 2:
                new KelasKuliah().lihatKelas();
                break;
            case 3:
                new KRS().lihatKRS();
                break;
            case 4:
                System.out.print("Masukkan ID Kelas Kuliah: ");
                String idKelas = input.nextLine().trim();
                if (idKelas.isEmpty()) {
                    Tampilan.gagal("ID Kelas tidak boleh kosong!");
                    break;
                }
                new KRS().ambilKRS(Session.nim, idKelas);
                break;
            case 5:
                new Jadwal().lihatJadwalMahasiswa(Session.nim);
                break;
            case 6:
                new Nilai().lihatNilaiMahasiswa(Session.nim);
                break;
            case 7:
                new Presensi().rekapPresensiMahasiswa(Session.nim);
                break;
            case 8:
                logout();
                return -1;
            case 0:
                Tampilan.sukses("Terima kasih telah mengunjungi Sistem Informasi Akademik USU!");
                System.exit(0);
                break;
            default:
                Tampilan.gagal("Menu tidak tersedia, coba lagi.");
        }
        return pilih;
    }

    // ===================== HANDLER DOSEN =====================
    private static int handleDosen(int pilih) {
        switch (pilih) {
            case 1:
                new Mahasiswa().lihatMahasiswa();
                break;
            case 2:
                new KelasKuliah().lihatKelasByDosen(Session.nidn);
                break;
            case 3:
                new KRS().kelolaKRS(Session.nidn);
                break;
            case 4:
                new Nilai().inputNilai(Session.nidn);
                break;
            case 5:
                new Presensi().inputPresensi(Session.nidn);
                break;
            case 6:
                new Presensi().rekapKehadiranDosen(Session.nidn);
                break;
            case 7:
                logout();
                return -1;
            case 0:
                Tampilan.sukses("Terima kasih telah mengunjungi Sistem Informasi Akademik USU!");
                System.exit(0);
                break;
            default:
                Tampilan.gagal("Menu tidak tersedia, coba lagi.");
        }
        return pilih;
    }

    // ===================== LOGOUT =====================
    public static void logout() {
        Tampilan.peringatan("Logout berhasil! Sampai jumpa, " + Session.nama + ".");
        Session.username = null;
        Session.role     = null;
        Session.nama     = null;
        Session.nim      = null;
        Session.nidn     = null;
        new Login().loginUser();
    }
}