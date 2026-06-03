package SDM_PegawaiPayroll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class LaporanPayrollController {

    Connection conn;

    Scanner input = new Scanner(System.in);

    public LaporanPayrollController() {

        conn = KoneksiDatabase.connect();
    }

    public void menu() {

        int pilih;

        do {

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== LAPORAN PAYROLL =====" + Warna.RESET);
            System.out.println(Warna.KUNING + Warna.BOLD + "1. Tambah Laporan Payroll" + Warna.RESET);
            System.out.println(Warna.KUNING + Warna.BOLD + "2. Laporan Semua Payroll" + Warna.RESET);
            System.out.println(Warna.KUNING + Warna.BOLD + "3. Laporan Periode" + Warna.RESET);
            System.out.println(Warna.KUNING + Warna.BOLD + "4. Total Pengeluaran Gaji" + Warna.RESET);
            System.out.println(Warna.KUNING + Warna.BOLD + "5. Cetak Laporan Payroll" + Warna.RESET);
            System.out.println(Warna.KUNING + Warna.BOLD + "6. Hapus Laporan Payroll" + Warna.RESET);
            System.out.println(Warna.MERAH + Warna.BOLD + "0. Kembali" + Warna.RESET);
            System.out.print(Warna.UNGU + Warna.BOLD + "Pilih : " + Warna.RESET);

            pilih = Integer.parseInt(input.nextLine());

            switch(pilih){

                case 1:

                    tambahLaporanPayroll();
                    break;

                case 2:

                    laporanSemua();
                    break;

                case 3:

                    laporanPeriode();
                    break;

                case 4:

                    totalPengeluaran();
                    break;

                case 5:

                    cetakLaporan();
                    break;

                case 6:

                    hapusLaporan();
                    break;

                case 0:

                    System.out.println("Kembali");
                    break;

                default:

                    System.out.println(Warna.MERAH + "Menu tidak tersedia!" + Warna.RESET);
            }

        }

        while(pilih != 0);

    }

    // TAMBAH LAPORAN PAYROLL
    public void tambahLaporanPayroll(){

        try{

            System.out.print(Warna.PINK + "Periode : " + Warna.RESET);

            String periode = input.nextLine();

            String sql =

            "insert into "
            + "project.laporan_payroll "
            + "(periode, total_pengeluaran, keterangan) "

            + "select "
            + "pr.periode, "
            + "sum(pr.total_gaji), "
            + "'Laporan Payroll Bulanan' "
            + "from project.payroll pr "
            + "join project.pegawai p "
            + "on pr.id_pegawai=p.id_pegawai "
            + "join project.jabatan j "
            + "on p.id_jabatan=j.id_jabatan "
            + "where pr.periode=? "
            + "group by pr.periode";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1,periode);

            int hasil = ps.executeUpdate();

            if(hasil>0){

                System.out.println(Warna.HIJAU + "Laporan payroll berhasil ditambah!" + Warna.RESET);

            }

            else{

                System.out.println(Warna.MERAH + "Data payroll tidak ditemukan!" + Warna.RESET);

            }

        } catch(Exception e){

            System.out.println(e);

        }

    }

    // LAPORAN SEMUA PAYROLL
    public void laporanSemua(){

        try{

            String sql =

            "select p.nama_pegawai,"
            + "j.nama_jabatan,"
            + "pr.periode,"
            + "pr.total_gaji "
            + "from project.payroll pr "
            + "join project.pegawai p "
            + "on pr.id_pegawai=p.id_pegawai "
            + "join project.jabatan j "
            + "on p.id_jabatan=j.id_jabatan "
            + "order by pr.id_payroll";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println(Warna.CYAN + "\n===== DATA PAYROLL =====" + Warna.RESET);

            while(rs.next()){

                System.out.println(Warna.BIRU +
                rs.getString("nama_pegawai")
                + " | "
                + rs.getString("nama_jabatan")
                + " | "
                + rs.getString("periode")
                + " | Rp."
                + String.format("%,.0f", rs.getDouble("total_gaji")) + Warna.RESET
                );

            }

        } catch(Exception e){

            System.out.println(e);

        }

    }



    // LAPORAN PERIODE
    public void laporanPeriode(){

        try{

            System.out.print(Warna.PINK + Warna.BOLD + "Masukkan Periode : " + Warna.RESET);
            String periode = input.nextLine();

            String sql =

            "select p.nama_pegawai,"
            + "pr.total_gaji "
            + "from project.payroll pr "
            + "join project.pegawai p "
            + "on pr.id_pegawai="
            + "p.id_pegawai "
            + "where pr.periode=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1,periode);

            ResultSet rs = ps.executeQuery();

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== LAPORAN PERIODE ====="+ Warna.RESET);

            while(rs.next()){

                System.out.println(Warna.BIRU +
                rs.getString(
                "nama_pegawai")
                + " | Rp."
                + String.format("%,.0f", rs.getDouble(
                "total_gaji")) + Warna.RESET
                );

            }

        } catch(Exception e){

            System.out.println(e);

        }

    }

    // TOTAL PENGELUARAN GAJI
    public void totalPengeluaran(){

        try{

            System.out.print(Warna.PINK + "Periode : " + Warna.RESET);
            String periode = input.nextLine();

            String sql =

            "select sum(total_gaji) "
            + " as total "
            + "from project.payroll "
            + "where periode = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1,periode);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){

                System.out.println(Warna.HIJAU + "\nTOTAL PENGELUARAN : Rp." + String.format("%,.0f", rs.getDouble("total")) + Warna.RESET);

            }

        } catch(Exception e){

            System.out.println(e);

        }

    }

    // CETAK LAPORAN
    public void cetakLaporan(){

    try{

        String sql =

        "select p.id_payroll,"
        + "pg.nama_pegawai,"
        + "j.nama_jabatan,"
        + "p.periode,"
        + "p.total_gaji "
        + "from project.payroll p "
        + "join project.pegawai pg "
        + "on p.id_pegawai=pg.id_pegawai "
        + "join project.jabatan j "
        + "on pg.id_jabatan=j.id_jabatan "
        + "order by p.id_payroll";

        PreparedStatement ps = conn.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        System.out.println(Warna.CYAN + Warna.BOLD + "\n========== CETAK LAPORAN PAYROLL ==========" + Warna.RESET);

        while(rs.next()){

            System.out.println(Warna.KUNING + Warna.BOLD +
            "ID Laporan : "
            + rs.getInt("id_payroll")
            + "\nNama : "
            + rs.getString("nama_pegawai")
            + "\nJabatan : "
            + rs.getString("nama_jabatan")
            + "\nPeriode : "
            + rs.getString("periode")
            + "\nTotal Gaji : Rp."
            + String.format("%,.0f",
              rs.getDouble("total_gaji")
            ) + Warna.RESET
            + "\n-------------------------------------"
            );

        }

    } catch(Exception e){

        System.out.println(e);

    }

    }

    // HAPUS LAPORAN PAYROLL
    public void hapusLaporan(){

    try{

        System.out.print(Warna.PINK + "ID Payroll : " + Warna.RESET);

        int idLaporan = Integer.parseInt(input.nextLine());

        String sql =

        "delete from "
        + "project.payroll "
        + "where id_laporan = ?";

        PreparedStatement ps =conn.prepareStatement(sql);

        ps.setInt(1,idLaporan);

        int hasil = ps.executeUpdate();

        if(hasil > 0){

            System.out.println(Warna.HIJAU + "Payroll berhasil dihapus!" + Warna.RESET);

        }

        else{

            System.out.println(Warna.MERAH + "ID Payroll tidak ditemukan!" + Warna.RESET);

        }

    } catch(Exception e){

        System.out.println(e);

    }

}

}