package SDM_PegawaiPayroll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class AbsensiController {

    Connection conn;
    Scanner input = new Scanner(System.in);

    public AbsensiController() {

        conn = KoneksiDatabase.connect();

    }

    // MENU ADMIN
    public void menu() {

        int pilih;

        do{

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== MENU ABSENSI ====="  + Warna.RESET);
            System.out.println(Warna.BIRU + "1. Tambah Absensi" + Warna.RESET);
            System.out.println(Warna.BIRU + "2. Tampil Absensi" + Warna.RESET);
            System.out.println(Warna.BIRU + "3. Update Absensi" + Warna.RESET);
            System.out.println(Warna.BIRU + "4. Hapus Absensi" + Warna.RESET);
            System.out.println(Warna.BIRU + "5. Rekap Absensi" + Warna.RESET);
            System.out.println(Warna.MERAH + "0. Kembali" + Warna.RESET);
            System.out.print(Warna.KUNING + "Pilih : "  + Warna.RESET);

            pilih =
            Integer.parseInt(input.nextLine());

            switch(pilih){

                case 1:

                    tambahAbsensi();
                    break;

                case 2:

                    tampilAbsensi();
                    break;

                case 3:

                    updateAbsensi();
                    break;

                case 4:

                    hapusAbsensi();
                    break;

                case 5:

                    rekapAbsensi();
                    break;

                case 0:

                    System.out.println(Warna.MERAH + "Kembali"  + Warna.RESET);
                    break;

                default:

                    System.out.println(Warna.MERAH + "Menu tidak tersedia!"  + Warna.RESET);

            }

        }

        while(pilih!=0);

    }

    // MENU PEGAWAI
    public void menuPegawai(int idPegawai){

        int pilih;

        do{

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== MENU ABSENSI PEGAWAI ====="  + Warna.RESET);
            System.out.println(Warna.BIRU + "1. Absen Hari Ini" + Warna.RESET);
            System.out.println(Warna.BIRU + "2. Lihat Absensi Saya" + Warna.RESET);
            System.out.println(Warna.BIRU + "0. Kembali" + Warna.RESET);
            System.out.print(Warna.KUNING + "Pilih : " + Warna.RESET);

            pilih = Integer.parseInt(input.nextLine());

            switch(pilih){

                case 1:

                    tambahAbsensiPegawai(idPegawai);
                    break;

                case 2:

                    lihatAbsensiPegawai(idPegawai);
                    break;

                case 0:

                    System.out.println(Warna.MERAH + "Kembali" + Warna.RESET);
                    break;

                default:

                    System.out.println(Warna.MERAH + "Menu tidak tersedia!" + Warna.RESET);

            }
        }

        while(pilih!=0);

    }

    // TAMBAH ADMIN
    public void tambahAbsensi(){

        try{

            System.out.print(Warna.PINK + "ID Pegawai : " + Warna.RESET);
            int idPegawai = Integer.parseInt(input.nextLine());

            System.out.print(Warna.PINK + "Tanggal (YYYY-MM-DD) : " + Warna.RESET);
            String tanggal = input.nextLine();

            System.out.print(Warna.PINK + "Status : " + Warna.RESET);
            String status = input.nextLine();

            String sql =

            "insert into project.absensi "
            + "(id_pegawai,"
            + "tanggal,status) "
            + "values(?,?,?)";

            PreparedStatement ps =
            conn.prepareStatement(sql);

            ps.setInt(1, idPegawai);
            ps.setDate(2, java.sql.Date.valueOf(tanggal));
            ps.setString(3, status);
            ps.executeUpdate();

            System.out.println(Warna.HIJAU + "Absensi berhasil ditambah!" + Warna.RESET);

        }

        catch(Exception e){

            System.out.println(e);

        }

    }

    // ABSEN PEGAWAI
    public void tambahAbsensiPegawai(int idPegawai){

        try{

            System.out.print(Warna.PINK + "Tanggal (YYYY-MM-DD): " + Warna.RESET);

            String tanggal = input.nextLine();

            System.out.print(Warna.PINK + "Status : " + Warna.RESET);

            String status = input.nextLine();

        // CEK ABSEN DULU

        String cekSql =

        "select * "
        + "from project.absensi "
        + "where id_pegawai = ? "
        + "and tanggal = ?";

        PreparedStatement cek = conn.prepareStatement(cekSql);

        cek.setInt(1,idPegawai);
        cek.setDate(2,java.sql.Date.valueOf(tanggal));

        ResultSet rs = cek.executeQuery();

        if(rs.next()){

            System.out.println(Warna.MERAH + "Kamu sudah absen!" + Warna.RESET);

            return;

        }

        // INSERT ABSEN

        String sql =

        "insert into project.absensi "
        + "(id_pegawai,"
        + "tanggal,status) "
        + "values(?,?,?)";

        PreparedStatement ps =
                conn.prepareStatement(sql);

        ps.setInt(1,idPegawai);
        ps.setDate(2,java.sql.Date.valueOf(tanggal));
        ps.setString(3,status);
        ps.executeUpdate();

        System.out.println(Warna.HIJAU + "Absensi berhasil!" + Warna.RESET);

    }

    catch(Exception e){

        System.out.println(e);

        }
    }

    // TAMPIL ADMIN
    public void tampilAbsensi(){

        try{

            String sql =

            "select * "
            + "from project.absensi "
            + "order by id_absensi";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                System.out.println(Warna.CYAN + Warna.BOLD +

                rs.getInt("id_absensi")
                + " | "
                + rs.getInt("id_pegawai")
                + " | "
                + rs.getDate("tanggal")
                + " | "
                + rs.getString("status")+ Warna.RESET);

            }

        }

        catch(Exception e){

            System.out.println(e);

        }

    }

    // TAMPIL ABSENSI PEGAWAI
    public void lihatAbsensiPegawai(int idPegawai){

        try{

            String sql =

            "select * "
            + "from project.absensi "
            + "where id_pegawai = ? "
            + "order by tanggal";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,idPegawai);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                System.out.println(Warna.CYAN + Warna.BOLD + rs.getDate("tanggal")
                + " | " + rs.getString("status") + Warna.RESET);
            }

        }

        catch(Exception e){

            System.out.println(e);

        }

    }

    // UPDATE
    public void updateAbsensi(){

        try{

            System.out.print(Warna.PINK + "ID Absensi : " + Warna.RESET);
            int idAbsensi = Integer.parseInt(input.nextLine());

            System.out.print(Warna.PINK + "Status Baru : " + Warna.RESET);
            String status = input.nextLine();

            String sql =

            "update project.absensi "
            + "set status = ? "
            + "where id_absensi = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1,status);
            ps.setInt(2,idAbsensi);
            ps.executeUpdate();

            System.out.println(Warna.HIJAU + "Absensi berhasil diupdate!" + Warna.RESET);

        }

        catch(Exception e){

            System.out.println(e);

        }

    }

    // HAPUS
    public void hapusAbsensi(){

        try{

            System.out.print(Warna.PINK + "ID Pegawai : " + Warna.RESET);
            int idPegawai = Integer.parseInt(input.nextLine());

            System.out.print(Warna.PINK + "Tanggal (YYYY-MM-DD): " + Warna.RESET);
            String tanggal = input.nextLine();

            String sql =

            "delete from project.absensi "
            + "where id_pegawai = ? " 
            + "and tanggal = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,idPegawai);
            ps.setDate(2, java.sql.Date.valueOf(tanggal));
            ps.executeUpdate();
        

            System.out.println(Warna.MERAH + "Absensi berhasil dihapus!" + Warna.RESET);
        }

        catch(Exception e){

            System.out.println(e);

        }

    }

    // REKAP
    public void rekapAbsensi(){

        try{

             System.out.println(Warna.CYAN + Warna.BOLD + "\n===== ABSENSI PEGAWAI ====="  + Warna.RESET);

            System.out.print(Warna.PINK + "Bulan : " + Warna.RESET);
            int bulan = Integer.parseInt(input.nextLine());

            System.out.print(Warna.PINK + "Tahun : " + Warna.RESET);
            int tahun = Integer.parseInt(input.nextLine());

            String sql =

            "select p.id_pegawai,"
            + "p.nama_pegawai,"
            + "count(case "
            + "when a.status='Hadir' "
            + "then 1 end) as Hadir "
            + "from project.pegawai p "
            + "join project.absensi a "
            + "on p.id_pegawai="
            + "a.id_pegawai "
            + "where extract("
            + "month from a.tanggal) = ? "
            + "and extract("
            + "year from a.tanggal) = ? "
            + "group by "
            + "p.id_pegawai,"
            + "p.nama_pegawai";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,bulan);
            ps.setInt(2,tahun);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                System.out.println(Warna.CYAN + Warna.BOLD +
                rs.getInt("id_pegawai")
                + " | "
                + rs.getString("nama_pegawai")
                + " | Hadir : "
                + rs.getInt("hadir") + Warna.RESET);

            }

        }

        catch(Exception e){

            System.out.println(e);

        }

    }

}
