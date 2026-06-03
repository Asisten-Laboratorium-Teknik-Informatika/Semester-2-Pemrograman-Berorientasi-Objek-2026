package SDM_PegawaiPayroll;

import java.util.Scanner;

public class AdminMenu {

    Scanner input = new Scanner(System.in);

    public void menuAdmin() {

        int pilih;

        do {

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== MENU ADMIN =====" + Warna.RESET);
            System.out.println(Warna.BIRU + "1.  Departemen" + Warna.RESET);
            System.out.println(Warna.BIRU + "2.  Jabatan" + Warna.RESET);
            System.out.println(Warna.BIRU + "3.  Pegawai" + Warna.RESET);
            System.out.println(Warna.BIRU + "4.  Users" + Warna.RESET);
            System.out.println(Warna.BIRU + "5.  Absensi" + Warna.RESET);
            System.out.println(Warna.BIRU + "6.  Cuti"  + Warna.RESET);
            System.out.println(Warna.BIRU + "7.  Kalender Libur" + Warna.RESET);
            System.out.println(Warna.BIRU + "8.  Tunjangan" + Warna.RESET);
            System.out.println(Warna.BIRU + "9.  Potongan" + Warna.RESET);
            System.out.println(Warna.BIRU + "10. Pegawai Tunjangan" + Warna.RESET);
            System.out.println(Warna.BIRU + "11. Pegawai Potongan" + Warna.RESET);
            System.out.println(Warna.BIRU + "12. Lembur" + Warna.RESET);
            System.out.println(Warna.BIRU + "13. Payroll" + Warna.RESET);
            System.out.println(Warna.BIRU + "14. Laporan Payroll" + Warna.RESET);
            System.out.println(Warna.BIRU + "15. Filter Pegawai" + Warna.RESET);
            System.out.println(Warna.MERAH + "0.  Logout" + Warna.RESET);

            System.out.print(Warna.KUNING + "Pilih Menu : " + Warna.RESET);
            pilih = input.nextInt();
            input.nextLine();

            switch(pilih) {

                case 1:

                    DepartemenController dc = new DepartemenController();
                    dc.menu();

                    break;

                case 2:

                    JabatanController jc = new JabatanController();
                    jc.menu();

                    break;

                case 3:

                    PegawaiController pc = new PegawaiController();
                    pc.menu();

                    break;

                case 4:

                    UsersController uc = new UsersController();
                    uc.menu();

                    break;

                case 5:

                    AbsensiController ac = new AbsensiController();
                    ac.menu();

                    break;

                case 6:

                    CutiController cc = new CutiController();
                    cc.menu();

                    break;

                case 7:

                    KalenderLiburController kl = new KalenderLiburController();
                    kl.menu();

                    break;

                case 8:

                    TunjanganController tc = new TunjanganController();
                    tc.menu();

                    break;

                case 9:

                    PotonganController poc = new PotonganController();
                    poc.menu();

                    break;

                case 10:

                    PegawaiTunjanganController pt = new PegawaiTunjanganController();
                    pt.menu();

                    break;

                case 11:

                    PegawaiPotonganController pp = new PegawaiPotonganController();
                    pp.menu();

                    break;

                case 12:

                    LemburController lc = new LemburController();
                    lc.menu();

                    break;

                case 13:

                    PayrollController pay = new PayrollController();
                    pay.menu();

                    break;

                case 14:

                    LaporanPayrollController lp = new LaporanPayrollController();
                    lp.menu();

                    break;

                case 15:

                    StatistikController st = new StatistikController();
                    st.menu();

                    break;

                case 0:

                    System.out.println(Warna.HIJAU +"Logout berhasil." + Warna.RESET);
                    
                    break;

                default:

                    System.out.println(Warna.MERAH +"Menu tidak tersedia!" + Warna.RESET);
            }

        }

        while(pilih != 0);

    }

}
