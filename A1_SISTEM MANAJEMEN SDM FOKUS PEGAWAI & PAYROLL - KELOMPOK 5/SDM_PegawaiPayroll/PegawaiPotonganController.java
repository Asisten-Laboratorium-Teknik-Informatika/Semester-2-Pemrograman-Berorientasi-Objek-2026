package SDM_PegawaiPayroll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class PegawaiPotonganController {

    Connection conn;

    Scanner input = new Scanner(System.in);

    public PegawaiPotonganController() {

        conn = KoneksiDatabase.connect();

    }

    public void menu() {

        int pilih;

        do {

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== MENU PEGAWAI POTONGAN =====" + Warna.RESET);
            System.out.println(Warna.BIRU + "1. Tambah" + Warna.RESET);
            System.out.println(Warna.BIRU + "2. Tampil" + Warna.RESET);
            System.out.println(Warna.BIRU + "3. Update" +  Warna.RESET);
            System.out.println(Warna.BIRU + "4. Hapus" + Warna.RESET);
            System.out.println(Warna.MERAH +"0. Kembali" + Warna.RESET);
            System.out.print(Warna.KUNING + "Pilih Menu : " + Warna.RESET);

            pilih = Integer.parseInt(input.nextLine());

            switch(pilih) {

                case 1:

                    tambah();
                    break;

                case 2:

                    tampil();
                    break;

                case 3:

                    update();
                    break;

                case 4:

                    hapus();
                    break;

                case 0:

                    System.out.println(Warna.MERAH + "Kembali" + Warna.RESET);
                    break;

                default:

                    System.out.println(Warna.MERAH + "Menu tidak tersedia!" + Warna.RESET);

            }

        }

        while(pilih != 0);

    }

    // TAMBAH
    public void tambah() {

        try {

            System.out.print(Warna.PINK + "ID Pegawai : " + Warna.RESET);
            int idPegawai = Integer.parseInt(input.nextLine());

            System.out.print(Warna.PINK + "ID Potongan : " + Warna.RESET);
            int idPotongan = Integer.parseInt(input.nextLine());

            String sql =

            "insert into "
            + "project.pegawai_potongan "
            + "(id_pegawai,"
            + "id_potongan) "
            + "values(?,?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,idPegawai);
            ps.setInt(2,idPotongan);
            ps.executeUpdate();

            System.out.println(Warna.HIJAU + "Data berhasil ditambah!" + Warna.RESET);

        } catch(Exception e) {

            System.out.println(e);

        }

    }

    // TAMPIL
    public void tampil() {

        try {

            String sql =

            "select * "
            + "from "
            + "project.pegawai_potongan "
            + "order by "
            + "id_pegawai_potongan";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== DATA PEGAWAI POTONGAN =====" + Warna.RESET);

            while(rs.next()) {

                System.out.println(Warna.BIRU + 
                rs.getString("id_pegawai_potongan")
                + " | "
                + rs.getInt("id_pegawai")
                + " | "
                + rs.getInt("id_potongan") + Warna.RESET);

            }

        } catch(Exception e) {

            System.out.println(e);

        }

    }

    // UPDATE
    public void update() {

        try {

            System.out.print(Warna.PINK + "ID Pegawai Potongan : " + Warna.RESET);
            String idPP = input.nextLine();

            System.out.print(Warna.PINK + "ID Pegawai Baru : " + Warna.RESET);
            int idPegawai =Integer.parseInt(input.nextLine());

            System.out.print(Warna.PINK + "ID Potongan Baru : " + Warna.RESET);
            int idPotongan =Integer.parseInt(input.nextLine());

            String sql =

            "update "
            + "project.pegawai_potongan "
            + "set id_pegawai=?, "
            + "id_potongan=? "
            + "where "
            + "id_pegawai_potongan=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,idPegawai);
            ps.setInt(2,idPotongan);
            ps.setString(3,idPP);

            ps.executeUpdate();

            System.out.println(Warna.HIJAU + "Data berhasil diupdate!" + Warna.RESET);

        } catch(Exception e) {

            System.out.println(e);

        }

    }

    // HAPUS
    public void hapus() {

        try {

            System.out.print(Warna.PINK + "ID Pegawai Potongan : " + Warna.RESET);

            int idPP = Integer.parseInt(input.nextLine());

            String sql =

            "delete from "
            + "project.pegawai_potongan "
            + "where "
            + "id_pegawai_potongan = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,idPP);

            ps.executeUpdate();

            System.out.println(Warna.MERAH + "Data berhasil dihapus!" + Warna.RESET);

        } catch(Exception e) {

            System.out.println(e);

        }

    }

    // LIHAT POTONGAN PEGAWAI
    public void lihatPotonganSaya(int idPegawai){

        try {

            String sql =

            "select nama_potongan, jumlah " 
            + "from project.view_potongan_pegawai " 
            + "where id_pegawai=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt( 1, idPegawai );

            ResultSet rs = ps.executeQuery();

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== POTONGAN SAYA =====" + Warna.RESET);

            double total = 0;

            while(rs.next()){
                double jumlah = rs.getDouble( "jumlah" ); 

                total += jumlah; 
                
                System.out.println(Warna.BIRU + rs.getString( "nama_potongan" ) 
                + " | Rp." + 
                String.format( "%,.0f", jumlah ) + Warna.RESET); } 
                System.out.println(Warna.BIRU + "\nTOTAL POTONGAN : Rp." + 
                String.format( "%,.0f", total ) + Warna.RESET);

        } catch(Exception e){

            System.out.println(e);
        }
    }

    // Ajukan Pinjaman
    public void pinjaman(int idPegawai){

        try {

            System.out.print(Warna.PINK + "Jumlah Pinjaman : Rp." + Warna.RESET); 
            double jumlah = Double.parseDouble( input.nextLine() ); 

            if(jumlah > 5000000){ 

                System.out.println(Warna.MERAH + "Pinjaman maksimal Rp5.000.000!" + Warna.RESET); 
                
                return; 
        }

        if(jumlah <= 0){

            System.out.println(Warna.MERAH + "Jumlah Pinjaman tidak valid!" + Warna.RESET); 
            return;

        }

        String cariSql =

        "select id_potongan "
        + "from project.potongan "
        + "where nama_potongan="
        + "'Pinjaman Karyawan'";

        PreparedStatement cari = conn.prepareStatement(cariSql);

        ResultSet rs = cari.executeQuery();

        if(!rs.next()){

            System.out.println(Warna.MERAH + "Pinjaman Karyawan tidak ditemukan!" + Warna.RESET);

            return;
        }

        int idPotongan = rs.getInt("id_potongan");

        String sql =

        "insert into project.pegawai_potongan " + 
        "(id_pegawai, id_potongan, jumlah) " + 
        "values(?,?,?)";

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, idPegawai );
        ps.setInt(2,idPotongan);
        ps.setDouble( 3, jumlah);
        ps.executeUpdate();

        System.out.println(Warna.HIJAU + "Pinjaman berhasil!" + Warna.RESET);

        } catch(Exception e) {

            System.out.println(e);
        }
    }

}
