package SDM_PegawaiPayroll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class PegawaiController {

    Connection conn;
    Scanner input = new Scanner(System.in);

    public PegawaiController(){

        conn = KoneksiDatabase.connect();

    }

    // MENU ADMIN
    public void menu(){

        int pilih;

        do{

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== MENU PEGAWAI =====" + Warna.RESET);
            System.out.println(Warna.BIRU + "1. Tambah Pegawai" + Warna.RESET);
            System.out.println(Warna.BIRU + "2. Tampil Pegawai" + Warna.RESET);
            System.out.println(Warna.BIRU + "3. Update Pegawai" + Warna.RESET);
            System.out.println(Warna.BIRU + "4. Hapus Pegawai" + Warna.RESET);
            System.out.println(Warna.MERAH + "0. Kembali" + Warna.RESET);
            System.out.print(Warna.KUNING + "Pilih : " + Warna.RESET);

            pilih =Integer.parseInt(input.nextLine());

            switch(pilih){

                case 1:

                    tambahPegawai();
                    break;

                case 2:

                    tampilPegawai();
                    break;

                case 3:

                    updatePegawai();
                    break;

                case 4:

                    hapusPegawai();
                    break;

                case 0:

                    System.out.println("Kembali...");
                    break;

                default:

                    System.out.println("Menu tidak tersedia!");
            }
        }

        while(pilih!=0);

    }

    // TAMBAH
    public void tambahPegawai(){

        try{

            System.out.print(Warna.PINK + "ID Jabatan : "+ Warna.RESET);
            int idJabatan = Integer.parseInt(input.nextLine());

            System.out.print(Warna.PINK + "Nama Pegawai : " + Warna.RESET);
            String nama = input.nextLine();

            System.out.print(Warna.PINK + "Alamat : " + Warna.RESET);
            String alamat = input.nextLine();

            System.out.print(Warna.PINK + "No HP : " + Warna.RESET);
            String hp = input.nextLine();

            System.out.print(Warna.PINK + "Email : " + Warna.RESET);
            String email = input.nextLine();

            System.out.print(Warna.PINK + "Tanggal Masuk (YYYY-MM-DD): " + Warna.RESET);
            String tanggal = input.nextLine();

            String sql =

            "insert into "
            + "project.pegawai "
            + "(id_jabatan,"
            + "nama_pegawai,"
            + "alamat,"
            + "no_hp,"
            + "email,"
            + "tanggal_masuk) "
            + "values(?,?,?,?,?,?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,idJabatan);
            ps.setString(2,nama);
            ps.setString(3,alamat);
            ps.setString(4,hp);
            ps.setString(5,email);
            ps.setDate(6,java.sql.Date.valueOf(tanggal));
            ps.executeUpdate();

            System.out.println(Warna.HIJAU + "Pegawai berhasil ditambah!" + Warna.RESET);

        } catch(Exception e){

            System.out.println(e);

        }

    }

    // TAMPIL
    public void tampilPegawai(){

        try{

            String sql =

            "select * "
            + "from project.pegawai "
            + "order by id_pegawai";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== DATA PEGAWAI =====" + Warna.RESET);

            while(rs.next()){

                System.out.println(Warna.BIRU +
                rs.getInt("id_pegawai")
                + " | "
                + rs.getInt("id_jabatan")
                + " | "
                + rs.getString("nama_pegawai")
                + " | "
                + rs.getString("alamat")
                + " | "
                + rs.getString("no_hp")
                + " | "
                + rs.getString("email")
                + " | "
                + rs.getDate("tanggal_masuk") + Warna.RESET
                );

            }

        } catch(Exception e){

            System.out.println(e);

        }

    }

    // UPDATE
    public void updatePegawai(){

        try{

            System.out.print(Warna.PINK + "ID Pegawai : " + Warna.RESET);
            int idPegawai = Integer.parseInt(input.nextLine());

            System.out.print(Warna.PINK + "Nama Baru : " + Warna.RESET);
            String nama = input.nextLine();

            String sql =

            "update project.pegawai "
            + "set nama_pegawai=? "
            + "where id_pegawai=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1,nama);
            ps.setInt(2,idPegawai);
            ps.executeUpdate();

            System.out.println(Warna.HIJAU + "Pegawai berhasil diupdate!" + Warna.RESET);

        } catch(Exception e){

            System.out.println(e);

        }

    }

    // HAPUS
    public void hapusPegawai(){

        try{

            System.out.print(Warna.PINK + "ID Pegawai : " + Warna.RESET);
            int idPegawai = Integer.parseInt(input.nextLine());

            String sql =

            "delete from "
            + "project.pegawai "
            + "where id_pegawai = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,idPegawai);
            ps.executeUpdate();

            System.out.println(Warna.MERAH + "Pegawai berhasil dihapus!" + Warna.RESET);

        } catch(Exception e){

            System.out.println(e);

        }

    }

    // LIHAT PROFIL PEGAWAI
    public void lihatProfil(int idPegawai){

        try{

            String sql =

            "select p.nama_pegawai,"
            + "j.nama_jabatan,"
            + "p.alamat,"
            + "p.no_hp,"
            + "p.email,"
            + "p.tanggal_masuk "
            + "from project.pegawai p "
            + "join project.jabatan j "
            + "on p.id_jabatan="
            + "j.id_jabatan "
            + "where p.id_pegawai=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,idPegawai);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){

                System.out.println(Warna.CYAN + Warna.BOLD + "\n===== PROFIL =====" + Warna.RESET);
                System.out.println(Warna.BIRU + "Nama : "+ rs.getString("nama_pegawai") + Warna.RESET);
                System.out.println(Warna.BIRU + "Jabatan : "+ rs.getString("nama_jabatan") + Warna.RESET);
                System.out.println(Warna.BIRU + "Alamat : "+ rs.getString("alamat") + Warna.RESET);
                System.out.println(Warna.BIRU + "No HP : "+ rs.getString("no_hp") + Warna.RESET);
                System.out.println(Warna.BIRU + "Email : "+ rs.getString("email") + Warna.RESET);
                System.out.println(Warna.BIRU + "Tanggal Masuk : "+ rs.getDate("tanggal_masuk") + Warna.RESET);
            }

        } catch(Exception e){

            System.out.println(e);

        }

    }

}