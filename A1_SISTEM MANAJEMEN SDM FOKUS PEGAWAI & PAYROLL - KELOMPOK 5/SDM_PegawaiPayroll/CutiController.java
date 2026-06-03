package SDM_PegawaiPayroll; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class CutiController {

    Connection conn;
    Scanner input = new Scanner(System.in);

    public CutiController(){

        conn = KoneksiDatabase.connect();

    }

    // MENU ADMIN 
    public void menu(){

        int pilih;

        do{

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== MENU CUTI =====" + Warna.RESET);
            System.out.println(Warna.BIRU + "1. Lihat Cuti(Pending)" + Warna.RESET);
            System.out.println(Warna.BIRU + "2. Approve Cuti" + Warna.RESET);
            System.out.println(Warna.BIRU + "3. Reject Cuti" + Warna.RESET);
            System.out.println(Warna.BIRU + "4. Tambah Cuti" + Warna.RESET);
            System.out.println(Warna.BIRU + "5. Hapus Cuti" + Warna.RESET);
            System.out.println(Warna.BIRU + "6. Data Cuti" + Warna.RESET);
            System.out.println(Warna.MERAH + "0. Kembali" + Warna.RESET);
            System.out.print(Warna.KUNING + "Pilih : " + Warna.RESET);

            pilih =
                    Integer.parseInt(input.nextLine());

            switch(pilih){

                case 1:

                    tampilCutiPending();
                    break;

                case 2:

                    approveCuti();
                    break;

                case 3:

                    rejectCuti();
                    break;

                case 4:

                    tambahCutiAdmin();
                    break;

                case 5:

                    hapusCuti();
                    break;

                case 6:

                    tampilDataCuti();
                    break;

                case 0:

                    System.out.println(Warna.MERAH + "Kembali" + Warna.RESET);
                    break;
            }
        }

        while(pilih!=0);
    }

    // MENU PEGAWAI 
    public void menuPegawai(int idPegawai) {

        int pilih;

        do{

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== MENU CUTI PEGAWAI =====" + Warna.RESET);
            System.out.println(Warna.BIRU + "1. Ajukan Cuti" + Warna.RESET);
            System.out.println(Warna.BIRU + "2. Lihat Cuti Saya" + Warna.RESET);
            System.out.println(Warna.MERAH + "0. Kembali" + Warna.RESET);
            System.out.print(Warna.KUNING + "Pilih : " + Warna.RESET);

            pilih =
                    Integer.parseInt(input.nextLine());

            switch(pilih){

                case 1:

                    ajukanCuti(idPegawai);
                    break;

                case 2:

                    lihatCutiPegawai(idPegawai);
                    break;

                case 0:

                    System.out.println(Warna.MERAH + "Kembali" + Warna.RESET);
                    break;
            }
        }

        while(pilih!=0);
    }

    // AJUKAN CUTI 
    public void ajukanCuti(int idPegawai){

        try{

            System.out.print(Warna.PINK + "Tanggal Mulai (YYYY-MM-DD): " + Warna.RESET);
            String mulai = input.nextLine();

            System.out.print(Warna.PINK + "Tanggal Selesai (YYYY-MM-DD): " + Warna.RESET);
            String selesai = input.nextLine();

            System.out.print(Warna.PINK + "Alasan : " + Warna.RESET);
            String alasan = input.nextLine();

            System.out.print(Warna.PINK + "Jumlah Hari : " + Warna.RESET);
            int jumlah = Integer.parseInt(input.nextLine());


            String sql =

            "insert into project.cuti "
            + "(id_pegawai,"
            + "tanggal_mulai,"
            + "tanggal_selesai, alasan, jumlah_hari, status) "
            + "values(?,?,?,?,?,'PENDING')";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,idPegawai);
            ps.setDate(2,java.sql.Date.valueOf(mulai));
            ps.setDate(3,java.sql.Date.valueOf(selesai));
            ps.setString(4,alasan);
            ps.setInt(5,jumlah);
            ps.executeUpdate();

            System.out.println(Warna.HIJAU + "Pengajuan Cuti Berhasil Dikirim!" + Warna.RESET);

        } catch(Exception e) {

            System.out.println(e);
        }
    }

    // LIHAT CUTI PEGAWAI
    public void lihatCutiPegawai(int idPegawai){

        try{

            String sql =
            "select * "
            + "from project.cuti "
            + "where id_pegawai = ? "
            + "order by id_cuti desc";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idPegawai);

            ResultSet rs = ps.executeQuery();

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== DATA CUTI =====" + Warna.RESET);

            while(rs.next()){

                System.out.println(Warna.CYAN + Warna.BOLD +
                        rs.getInt("id_cuti")
                        + " | "
                        + rs.getDate("tanggal_mulai")
                        + " | "
                        + rs.getDate("tanggal_selesai")
                        + " | "
                        + rs.getString("alasan")
                        + " | "
                        + rs.getString("status") 
                        + " | "
                        + rs.getString("catatan")+ Warna.RESET
                );
            }

        } catch(Exception e){

            System.out.println(e);
        }
    }

    // CUTI PENDING (ADMIN) 
    public void tampilCutiPending() {

        try {

            String sql =

            "select * from project.cuti "
            + "where status = 'PENDING' "
            + "order by id_cuti";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            System.out.println(Warna.HIJAU + "Cuti Pending" + Warna.RESET);

            while (rs.next()) {

                System.out.println(Warna.CYAN + Warna.BOLD +
                        rs.getInt("id_cuti") + " | " +
                        rs.getInt("id_pegawai") + " | " +
                        rs.getDate("tanggal_mulai") + " - " +
                        rs.getDate("tanggal_selesai") + " | " +
                        rs.getString("alasan") + Warna.RESET
                );
            }

        } catch(Exception e){
            System.out.println(e);
        }
    }

    // APPROVE CUTI 
    public void approveCuti(){

        try{

            System.out.print(Warna.PINK + "ID Cuti : " + Warna.RESET);
            int idCuti = Integer.parseInt(input.nextLine());

            String sql =

            "update project.cuti "
            + "set status = 'APPROVED' "
            + "catatan = null"
            + "where id_cuti = ? ";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,idCuti);
            ps.executeUpdate();

            System.out.println(Warna.HIJAU + "Cuti Disetujui!" + Warna.RESET);

        } catch(Exception e){

            System.out.println(e);
        }
    }

    // REJECT CUTI 
    public void rejectCuti(){

        try{

            System.out.print(Warna.PINK + "ID Cuti : " + Warna.RESET);
            int idCuti = Integer.parseInt(input.nextLine());

            System.out.print(Warna.PINK + "Catatan Penolakan : " + Warna.RESET);
            String catatan = input.nextLine();

            String sql =

            "update project.cuti "
            + "set status = 'REJECT', "
            + "catatan = ? "
            + "where id_cuti = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1,catatan);
            ps.setInt(2,idCuti);
            ps.executeUpdate();

            System.out.println(Warna.MERAH + "Cuti Ditolak" + Warna.RESET);

        } catch(Exception e){

            System.out.println(e);
        }
    }

    // INPUT CUTI ADMIN
    public void tambahCutiAdmin(){

        try{

            System.out.print(Warna.PINK + "ID Pegawai : "+ Warna.RESET);
            int idPegawai = Integer.parseInt(input.nextLine());

            System.out.print(Warna.PINK + "Tanggal Mulai (YYYY-MM-DD): "+ Warna.RESET);
            String mulai = input.nextLine();

            System.out.print(Warna.PINK + "Tanggal Selesai (YYYY-MM-DD): "+ Warna.RESET);
            String selesai = input.nextLine();

            System.out.print(Warna.PINK + "Alasan : "+ Warna.RESET);
            String alasan = input.nextLine();

            System.out.print(Warna.PINK + "Jumlah Hari : " + Warna.RESET);
            int jumlah = Integer.parseInt(input.nextLine());

            String sql =

            "insert into project.cuti "
            + "(id_pegawai, tanggal_mulai, tanggal_selesai, alasan, jumlah_hari, status) "
            + "values (?,?,?,?,?,'APPROVED')";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,idPegawai);
            ps.setDate(2, java.sql.Date.valueOf(mulai));
            ps.setDate(3, java.sql.Date.valueOf(selesai));
            ps.setString(4, alasan);
            ps.setInt(5,jumlah);
            ps.executeUpdate();

            System.out.println(Warna.HIJAU + "Cuti Admin Approved" + Warna.RESET);

        } catch(Exception e){

            System.out.println(e);
        }
    }

        // HAPUS CUTI
        public void hapusCuti() {

        try {

            System.out.print(Warna.PINK + "ID Cuti: " + Warna.RESET);
            int idCuti = Integer.parseInt(input.nextLine());

            String sql =
                    "delete from project.cuti where id_cuti = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idCuti);
            ps.executeUpdate();

            System.out.println(Warna.MERAH + "Cuti berhasil dihapus!" + Warna.RESET);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

        // TAMPIL DATA CUTI
        public void tampilDataCuti() {

        try {

            String sql =
            "select c.id_cuti, c.id_pegawai, p.nama_pegawai, " +
            "c.tanggal_mulai, c.tanggal_selesai, c.alasan, c.status " +
            "from project.cuti c " +
            "join project.pegawai p on c.id_pegawai = p.id_pegawai " +
            "order by c.id_cuti desc";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== DATA CUTI =====" + Warna.RESET);

            while(rs.next()) {

                System.out.println(Warna.BIRU +
                    rs.getInt("id_cuti") + " | " +
                    rs.getString("nama_pegawai") + " | " +
                    rs.getDate("tanggal_mulai") + " - " +
                    rs.getDate("tanggal_selesai") + " | " +
                    rs.getString("alasan") + " | " +
                    rs.getString("status") + Warna.RESET
                );
            }

        } catch(Exception e) {
            System.out.println(e);
        }

    }

}
