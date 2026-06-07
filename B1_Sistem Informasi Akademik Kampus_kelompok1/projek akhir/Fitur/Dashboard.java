package Fitur;

import Entitas.DosenPembimbing;
import Entitas.Jadwal;
import Entitas.KRS;
import Entitas.KelasKuliah;
import Entitas.Mahasiswa;
import Entitas.Dosen;
import Entitas.Nilai;
import Entitas.Presensi;
import Entitas.TahunAkademik;

import java.util.Scanner;

public class Dashboard {

    public static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        menu();
    }

    public static void menu() {
        int pilih;
        do {
            TahunAkademik ta = new TahunAkademik();
            String[] infoTahun = ta.getInfoTahunAktif();
            String tahun    = (infoTahun != null) ? infoTahun[0] : "-";
            String semester = (infoTahun != null) ? infoTahun[1] : "-";

            Tampilan.tampilHeader(Session.getNama(), Session.getRole(), tahun, semester);

            if ("Mahasiswa".equals(Session.getRole())) {
                Tampilan.tampilMenuMahasiswa();
            } else if ("Dosen".equals(Session.getRole())) {
                Tampilan.tampilMenuDosen();
            } else if ("Admin".equals(Session.getRole())) {
                Tampilan.tampilMenuAdmin();
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

            if ("Mahasiswa".equals(Session.getRole())) {
                pilih = handleMahasiswa(pilih);
            } else if ("Dosen".equals(Session.getRole())) {
                pilih = handleDosen(pilih);
            } else {
                pilih = handleAdmin(pilih);
            }

        } while (pilih != 0);
    }

    // ===================== HANDLER MAHASISWA =====================
    private static int handleMahasiswa(int pilih) {
        switch (pilih) {
            case 1:
                new Mahasiswa().lihatDataDiri(Session.getNim());
                break;
            case 2:
                new KelasKuliah().lihatKelas(Session.getNim());
                break;
            case 3:
                menuKelolaKRS();
                break;
            case 4:
                new Jadwal().lihatJadwalMahasiswa(Session.getNim());
                break;
            case 5:
                new Presensi().rekapPresensiMahasiswa(Session.getNim());
                break;
            case 6:
                new Nilai().pilihKHSMahasiswa(Session.getNim());
                break;
            case 7:
                new Nilai().lihatTranskrip(Session.getNim());
                break;
            case 8:
                logout();
                return -1;
            case 0:
                Tampilan.sukses(
                    "~~~ Terima kasih telah mengunjungi Sistem Informasi Akademik USU. ~~~\n" +
                    "Sampai jumpa, " + Session.getNama() + "."
                );
                System.exit(0);
                break;
            default:
                Tampilan.gagal("Menu tidak tersedia, coba lagi.");
        }
        return pilih;
    }

    // ===================== MENU KELOLA KRS =====================
    private static void menuKelolaKRS() {
        int pilih;
        do {
            System.out.println("\n===== KELOLA KRS =====");
            System.out.println("1. Ambil KRS");
            System.out.println("2. Lihat KRS Saya");
            System.out.println("0. Kembali");
            System.out.print("Pilih Menu: ");

            try {
                pilih = Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                Tampilan.gagal("Input tidak valid, masukkan angka!");
                pilih = -1;
                continue;
            }

            switch (pilih) {
                case 1:
                    new KelasKuliah().lihatKelas(Session.getNim());

                    System.out.print("\nMasukkan ID Kelas Kuliah yang ingin diambil: ");
                    String idKelas = input.nextLine().trim();

                    if (idKelas.isEmpty()) {
                        Tampilan.gagal("ID Kelas tidak boleh kosong!");
                        break;
                    }

                    new KRS().ambilKRS(Session.getNim(), idKelas);
                    break;

                case 2:
                    new KRS().lihatKRS(Session.getNim());
                    break;

                case 0:
                    break;

                default:
                    Tampilan.gagal("Menu tidak tersedia!");
            }
        } while (pilih != 0);
    }

    // ===================== HANDLER DOSEN =====================
    private static int handleDosen(int pilih) {
        switch (pilih) {
            case 1:
                new Dosen().lihatDataDiri(Session.getNidn());
                break;
            case 2:
                new Mahasiswa().lihatMahasiswa();
                break;
            case 3:
                new KelasKuliah().lihatKelasByDosen(Session.getNidn());
                break;
            case 4:
                new KRS().kelolaKRS(Session.getNidn());
                break;
            case 5:
                new Nilai().inputNilai(Session.getNidn());
                break;
            case 6:
                new Presensi().inputPresensi(Session.getNidn());
                break;
            case 7:
                new Presensi().rekapKehadiranDosen(Session.getNidn());
                break;
            case 8:
                new DosenPembimbing().lihatMahasiswaBimbingan(Session.getNidn());
                break;
            case 9:
                logout();
                return -1;
            case 0:
                Tampilan.sukses(
                    "~~~ Terima kasih telah mengunjungi Sistem Informasi Akademik USU. ~~~\n" +
                    "Sampai jumpa, " + Session.getNama() + "."
                );
                System.exit(0);
                break;
            default:
                Tampilan.gagal("Menu tidak tersedia, silakan coba lagi.");
        }
        return pilih;
    }

    // ===================== HANDLER ADMIN =====================
    private static int handleAdmin(int pilih) {
        switch (pilih) {
            case 1:
                menuDataMaster();
                break;
            case 2:
                menuKelolaAkun();
                break;
            case 3:
                DosenPembimbing.assignMahasiswa();
                break;
            case 4:
                DosenPembimbing.gantiPembimbing();
                break;
            case 5:
                new DosenPembimbing().lihatDosenPembimbing();
                break;
            case 6:
                logout();
                return -1;
            case 0:
                Tampilan.sukses(
                    "~~~ Terima kasih telah mengunjungi Sistem Informasi Akademik USU. ~~~\n" +
                    "Sampai jumpa, " + Session.getNama() + "."
                );
                System.exit(0);
                break;
            default:
                Tampilan.gagal("Menu tidak tersedia.");
        }
        return pilih;
    }

    // ===================== MENU DATA MASTER (Admin) =====================
    private static void menuDataMaster() {
        int pilih;
        do {
            System.out.println("\n===== DATA MASTER =====");
            System.out.println("1. Tambah Mahasiswa");
            System.out.println("2. Tambah Dosen");
            System.out.println("3. Tambah Program Studi");
            System.out.println("4. Tambah Mata Kuliah");
            System.out.println("5. Tambah Ruangan");
            System.out.println("6. Tambah Tahun Akademik");
            System.out.println("7. Tambah Kelas Kuliah");
            System.out.println("8. Tambah Jadwal");
            System.out.println("9. Tunjuk Dosen Sebagai Pembimbing");
            System.out.println("0. Kembali");
            System.out.print("Pilih Menu: ");

            try {
                pilih = Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                Tampilan.gagal("Input tidak valid!");
                pilih = -1;
                continue;
            }

            switch (pilih) {
                case 1: Entitas.Mahasiswa.daftarMahasiswa();         break;
                case 2: Entitas.Dosen.daftarDosen();                 break;
                case 3: Entitas.ProgramStudi.tambahProgramStudi();   break;
                case 4: Entitas.MataKuliah.tambahMataKuliah();       break;
                case 5: Entitas.Ruangan.tambahRuangan();             break;
                case 6: Entitas.TahunAkademik.tambahTahunAkademik(); break;
                case 7: Entitas.KelasKuliah.tambahKelasKuliah();     break;
                case 8: Entitas.Jadwal.tambahJadwal();               break;
                case 9: DosenPembimbing.tunjukPembimbing();          break;
                case 0: break;
                default: Tampilan.gagal("Menu tidak tersedia!");
            }
        } while (pilih != 0);
    }

    // ===================== MENU KELOLA AKUN (Admin) =====================
    private static void menuKelolaAkun() {
        int pilih;
        do {
            System.out.println("\n===== KELOLA AKUN =====");
            System.out.println("1. Buat Akun Baru");
            System.out.println("0. Kembali");
            System.out.print("Pilih Menu: ");

            try {
                pilih = Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                Tampilan.gagal("Input tidak valid!");
                pilih = -1;
                continue;
            }

            switch (pilih) {
                case 1: Entitas.Akun.buatAkun(); break;
                case 0: break;
                default: Tampilan.gagal("Menu tidak tersedia!");
            }
        } while (pilih != 0);
    }

    // ===================== LOGOUT =====================
    public static void logout() {
        Tampilan.peringatan("Logout berhasil! Sampai jumpa, " + Session.getNama() + ".");
        Session.clear();
        new Login().loginUser();
    }
}