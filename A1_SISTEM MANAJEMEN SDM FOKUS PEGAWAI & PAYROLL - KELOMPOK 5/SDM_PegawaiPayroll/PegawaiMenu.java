package SDM_PegawaiPayroll;

import java.util.Scanner;

public class PegawaiMenu {

    Scanner input = new Scanner(System.in);

    public void menuPegawai(int idPegawai) {

        int pilih;

        do {

            System.out.println(Warna.CYAN + Warna.BOLD + "\n==== MENU PEGAWAI ====" + Warna.RESET);
            System.out.println(Warna.BIRU + "1. Lihat Profil" + Warna.RESET);
            System.out.println(Warna.BIRU + "2. Absensi" + Warna.RESET);
            System.out.println(Warna.BIRU + "3. Lihat Tunjangan");
            System.out.println(Warna.BIRU + "4. Pinjaman Karyawan");
            System.out.println(Warna.BIRU + "5. Lihat Lembur" + Warna.RESET);
            System.out.println(Warna.BIRU + "6. Cetak Slip Gaji" + Warna.RESET);
            System.out.println(Warna.BIRU + "7. Ajukan Cuti" + Warna.RESET);
            System.out.println(Warna.MERAH + "0. Logout" + Warna.RESET);

            System.out.print(Warna.KUNING + "Pilih Menu : " + Warna.RESET);
            pilih = input.nextInt();
            input.nextLine();

            switch(pilih) {

                case 1:

                    PegawaiController pg = new PegawaiController();
                    pg.lihatProfil(idPegawai);

                    break;

                case 2:

                    AbsensiController ac = new AbsensiController();
                    ac.menuPegawai(idPegawai);

                    break;


                case 3:

                    PegawaiTunjanganController pt = new PegawaiTunjanganController();
                    pt.lihatTunjanganSaya(idPegawai);

                    break;

                case 4:

                    PegawaiPotonganController pp = new PegawaiPotonganController();
                    pp.pinjaman(idPegawai);

                    break;
                
                case 5:

                    LemburController lc = new LemburController();
                    lc.menuPegawai(idPegawai);

                    break;

                case 6:

                    PayrollController pc = new PayrollController();
                    pc.lihatSlipGaji(idPegawai);

                    break;
                
                case 7:

                    CutiController cc = new CutiController();
                    cc.menuPegawai(idPegawai);

                    break;

                case 0:

                    System.out.println(Warna.HIJAU + "Logout berhasil." + Warna.RESET);
                    break;

                default:

                    System.out.println(Warna.MERAH +"Menu tidak tersedia!" + Warna.RESET);

            }

        }

        while(pilih != 0);

    }

}
