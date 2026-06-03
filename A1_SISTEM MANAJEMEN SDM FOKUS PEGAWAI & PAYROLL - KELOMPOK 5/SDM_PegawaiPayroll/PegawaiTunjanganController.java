package SDM_PegawaiPayroll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class PegawaiTunjanganController {

    Connection conn;

    Scanner input = new Scanner(System.in);

    public PegawaiTunjanganController() {

        conn = KoneksiDatabase.connect();

    }

    public void menu() {

        int pilih;

        do {

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== MENU PEGAWAI TUNJANGAN =====" + Warna.RESET);
            System.out.println(Warna.BIRU + "1. Tambah" + Warna.RESET);
            System.out.println(Warna.BIRU + "2. Tampil" + Warna.RESET);
            System.out.println(Warna.BIRU +"3. Update" + Warna.RESET);
            System.out.println(Warna.BIRU + "4. Hapus" + Warna.RESET);
            System.out.println(Warna.MERAH + "0. Kembali" + Warna.RESET);
            System.out.print(Warna.KUNING + "Pilih : " + Warna.RESET);

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

                    System.out.println(Warna.MERAH + "Menu tidak ada!" + Warna.RESET);

            }

        }

        while(pilih != 0);

    }

    // TAMBAH
    public void tambah() {

        try {

            System.out.print(Warna.PINK + "ID Pegawai : " + Warna.RESET);
            int idPegawai = Integer.parseInt(input.nextLine());

            System.out.print(Warna.PINK + "ID Tunjangan : " + Warna.RESET);
            int idTunjangan = Integer.parseInt(input.nextLine());


            String cekSql =

            "select * "
            + "from project.pegawai_tunjangan "
            + "where id_pegawai=? "
            + "and id_tunjangan=?";

            PreparedStatement cek = conn.prepareStatement(cekSql);

            cek.setInt(1,idPegawai);
            cek.setInt(2,idTunjangan);

            ResultSet rs = cek.executeQuery();

            if(rs.next()){

                System.out.println(Warna.MERAH + "Tunjangan sudah dimiliki pegawai!" + Warna.RESET);

                return;

            }

            String sql =

            "insert into project.pegawai_tunjangan "
            + "(id_pegawai, "
            + "id_tunjangan) "
            + "values(?,?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,idPegawai);
            ps.setInt(2,idTunjangan);

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

            "select * from project.pegawai_tunjangan "
            + "order by id_pegawai_tunjangan";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== DATA PEGAWAI TUNJANGAN =====" + Warna.RESET);

            while(rs.next()) {

                System.out.println(Warna.BIRU +
                rs.getString("id_pegawai_tunjangan")
                + " | "
                + rs.getInt("id_pegawai")
                + " | "
                + rs.getInt("id_tunjangan") + Warna.RESET
                );

            }

        } catch(Exception e) {

            System.out.println(e);

        }

    }

    // UPDATE
    public void update() {

        try {

            System.out.print(Warna.PINK + "ID Pegawai Tunjangan : " + Warna.RESET);
            String idPT = input.nextLine();

            System.out.print(Warna.PINK + "ID Pegawai Baru : " + Warna.RESET);
            int idPegawai = Integer.parseInt(input.nextLine());

            System.out.print(Warna.PINK + "ID Tunjangan Baru : " + Warna.RESET);
            int idTunjangan = Integer.parseInt(input.nextLine());

            String sql =

            "update "
            + "project.pegawai_tunjangan "
            + "set id_pegawai=?, "
            + "id_tunjangan=? "
            + "where "
            + "id_pegawai_tunjangan=?";

            PreparedStatement ps =conn.prepareStatement(sql);

            ps.setInt(1,idPegawai);
            ps.setInt(2,idTunjangan);
            ps.setString(3,idPT);

            ps.executeUpdate();

            System.out.println(Warna.HIJAU + "Data berhasil diupdate!" + Warna.RESET);

        } catch(Exception e) {

            System.out.println(e);

        }

    }

    // HAPUS
    public void hapus() {

        try {

            System.out.print(Warna.PINK + "ID Pegawai Tunjangan : " + Warna.RESET);

            int idPT = Integer.parseInt(input.nextLine());

            String sql =

            "delete from "
            + "project.pegawai_tunjangan "
            + "where "
            + "id_pegawai_tunjangan=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,idPT);
            ps.executeUpdate();

            System.out.println(Warna.MERAH + "Data berhasil dihapus!" + Warna.RESET);

        } catch(Exception e) {

            System.out.println(e);

        }

    }

    // LIHAT TUNJANGAN PEGAWAI
    public void lihatTunjanganSaya(int idPegawai) {

        try { 

            String sql =

            "select nama_tunjangan, jumlah "
            + "from project.view_tunjangan_pegawai "
            + "where id_pegawai = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt( 1, idPegawai );

            ResultSet rs = ps.executeQuery();

            System.out.println( Warna.CYAN + Warna.BOLD + "\n===== TUNJANGAN SAYA =====" + Warna.RESET );

            double total = 0;

            while(rs.next()){ 
                
            double jumlah = rs.getDouble( "jumlah" ); 
            
            total += jumlah;

            System.out.println(Warna.KUNING + rs.getString( "nama_tunjangan" ) + " | Rp." + 
            String.format( "%,.0f", jumlah ) ); } 
            System.out.println( "\nTOTAL TUNJANGAN : Rp." + String.format( "%,.0f", total ) + Warna.RESET);
        } catch(Exception e){

            System.out.println(e);
        }

    }

    
}