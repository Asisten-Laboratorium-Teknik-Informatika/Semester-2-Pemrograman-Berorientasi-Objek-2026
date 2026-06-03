package SDM_PegawaiPayroll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class LemburController {

    Connection conn;
    Scanner input = new Scanner(System.in);

    public LemburController() {

        conn = KoneksiDatabase.connect();

    }

    // MENU ADMIN 
    public void menu() {

        int pilih;

        do {

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== MENU LEMBUR =====" + Warna.RESET);
            System.out.println(Warna.BIRU + "1. Tambah Lembur" + Warna.RESET);
            System.out.println(Warna.BIRU + "2. Tampil Lembur" + Warna.RESET);
            System.out.println(Warna.BIRU + "3. Update Lembur" + Warna.RESET);
            System.out.println(Warna.BIRU + "4. Hapus Lembur" + Warna.RESET);
            System.out.println(Warna.MERAH + "0. Kembali" + Warna.RESET);
            System.out.print(Warna.KUNING + "Pilih : " + Warna.RESET);

            pilih = Integer.parseInt(input.nextLine());

            switch(pilih){

                case 1:

                    tambahLembur();
                    break;

                case 2:

                    tampilLembur();
                    break;

                case 3:

                    updateLembur();
                    break;

                case 4:

                    hapusLembur();
                    break;

                case 0:

                    System.out.println(Warna.MERAH + "Kembali" + Warna.RESET);

                    break;
            }
        }

        while(pilih != 0);
    }

    // MENU PEGAWAI 
    public void menuPegawai(int idPegawai){

        int pilih;

        do{

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== MENU LEMBUR PEGAWAI =====" + Warna.RESET);
            System.out.println(Warna.BIRU + "1. Input Lembur" + Warna.RESET);
            System.out.println(Warna.BIRU + "2. Lihat Lembur Saya" + Warna.RESET);
            System.out.println(Warna.MERAH + "0. Kembali" + Warna.RESET);
            System.out.print(Warna.KUNING + "Pilih : " + Warna.RESET);

            pilih = Integer.parseInt(input.nextLine());

            switch(pilih){

                case 1:

                    inputLemburPegawai(idPegawai);
                    break;

                case 2:

                    lihatLemburPegawai(idPegawai);
                    break;

                case 0:

                    System.out.println(Warna.MERAH + "Kembali" + Warna.RESET);
                    break;
            }
        }

        while(pilih != 0);

    }

    // TAMBAH ADMIN 
    public void tambahLembur() {

        try {

            System.out.print(Warna.PINK + "ID Pegawai : " + Warna.RESET);
            int idPegawai = Integer.parseInt(input.nextLine());

            System.out.print(Warna.PINK + "Tanggal (YYYY-MM-DD) : " + Warna.RESET);
            String tanggal = input.nextLine();

            System.out.print(Warna.PINK + "Jam Lembur : " + Warna.RESET);
            int jamLembur = Integer.parseInt(input.nextLine());

            String sql =

            "insert into project.lembur "
            + "(id_pegawai,"
            + "tanggal_lembur,jam_lembur) "
            + "values(?,?,?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,idPegawai);
            ps.setDate(2,java.sql.Date.valueOf(tanggal));
            ps.setInt(3,jamLembur);
            ps.executeUpdate();

            System.out.println(Warna.HIJAU + "Data lembur berhasil ditambah!" + Warna.RESET);
        }

        catch(Exception e){

            System.out.println(e);
        }
    }

    // TAMPIL 
    public void tampilLembur(){

        try{

            String sql =
            "select * "
            + "from project.lembur "
            + "order by id_lembur";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== DATA LEMBUR =====" + Warna.RESET);

            while(rs.next()){

                System.out.println(Warna.BIRU +
                        rs.getInt("id_lembur")
                        + " | "
                        + rs.getInt("id_pegawai")
                        + " | "
                        + rs.getDate("tanggal_lembur")
                        + " | "
                        + rs.getInt("jam_lembur") + Warna.RESET
                );
            }

        } catch(Exception e){

            System.out.println(e);
        }
    }

    // UPDATE 
    public void updateLembur(){

        try{

            System.out.print(Warna.PINK + "ID Lembur : " + Warna.RESET);
            int idLembur =Integer.parseInt(input.nextLine());

            System.out.print(Warna.PINK + "Jam Lembur Baru : " + Warna.RESET);
            int jamBaru = Integer.parseInt(input.nextLine());

            String sql =
            "update project.lembur "
            + "set jam_lembur = ? "
            + "where id_lembur = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,jamBaru);
            ps.setInt(2,idLembur);
            ps.executeUpdate();

            System.out.println(Warna.HIJAU + "Lembur berhasil diupdate!" + Warna.RESET);
        }

        catch(Exception e){

            System.out.println(e);
        }
    }

    // HAPUS 
    public void hapusLembur(){

        try{

            System.out.print(Warna.PINK + "ID Lembur : " + Warna.RESET);
            int idLembur =Integer.parseInt(input.nextLine());

            String sql =
            "delete from project.lembur "
            + "where id_lembur = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,idLembur);
            ps.executeUpdate();

            System.out.println(Warna.MERAH + "Lembur berhasil dihapus!" + Warna.RESET);

        } catch(Exception e){

            System.out.println(e);

        }

    }

    // INPUT PEGAWAI 
    public void inputLemburPegawai(int idPegawai){

        try{

            System.out.print(Warna.PINK + "Tanggal (YYYY-MM-DD) : " + Warna.RESET);
            String tanggal = input.nextLine();

            System.out.print(Warna.PINK + "Jam Lembur : " + Warna.RESET);
            int jamLembur = Integer.parseInt(input.nextLine());

            String sql =
            "insert into project.lembur "
            + "(id_pegawai,"
            + "tanggal_lembur,jam_lembur) "
            + "values(?,?,?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,idPegawai);
            ps.setDate(2,java.sql.Date.valueOf(tanggal));
            ps.setInt(3,jamLembur);
            ps.executeUpdate();

            System.out.println(Warna.HIJAU + "Lembur berhasil disimpan!" + Warna.RESET);

        } catch(Exception e){

            System.out.println(e);
        }
    }

    // LIHAT LEMBUR SAYA 
    public void lihatLemburPegawai(int idPegawai){

        try{

            String sql =
            "select * "
            + "from project.lembur "
            + "where id_pegawai=? "
            + "order by tanggal_lembur";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,idPegawai);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                System.out.println(Warna.CYAN + Warna.BOLD +
                        rs.getInt("id_lembur")
                        + " | "
                        + rs.getDate("tanggal_lembur")
                        + " | "
                        + rs.getInt("jam_lembur")
                        + " jam" + Warna.RESET
                );
            }

        } catch(Exception e){

            System.out.println(e);
        }
    }
}
