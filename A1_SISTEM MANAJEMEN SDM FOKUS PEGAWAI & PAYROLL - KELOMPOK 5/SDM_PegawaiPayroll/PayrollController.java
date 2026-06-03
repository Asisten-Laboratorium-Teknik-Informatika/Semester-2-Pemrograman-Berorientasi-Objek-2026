package SDM_PegawaiPayroll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class PayrollController {

    Connection conn;
    Scanner input = new Scanner(System.in);

    public PayrollController(){

        conn = KoneksiDatabase.connect();

    }

    // MENU ADMIN
    public void menu(){

        int pilih;

        do{

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== MENU PAYROLL =====" + Warna.RESET);
            System.out.println(Warna.BIRU + "1. Tambah Payroll" + Warna.RESET);
            System.out.println(Warna.BIRU + "2. Tampil Payroll" + Warna.RESET);
            System.out.println(Warna.BIRU + "3. Update Payroll" + Warna.RESET);
            System.out.println(Warna.BIRU + "4. Hapus Payroll" + Warna.RESET);
            System.out.println(Warna.BIRU + "5. Cetak Slip Gaji" + Warna.RESET);
            System.out.println(Warna.MERAH + "0. Kembali" + Warna.RESET);
            System.out.print(Warna.KUNING + "Pilih : " + Warna.RESET);

            pilih = Integer.parseInt(input.nextLine());

            switch(pilih){

                case 1:

                    tambahPayroll();
                    break;

                case 2:

                    tampilPayroll();
                    break;

                case 3:

                    updatePayroll();
                    break;

                case 4:

                    hapusPayroll();
                    break;

                case 5:

                    cetakSlipAdmin();
                    break;

                case 0:

                    System.out.println(Warna.MERAH + "Kembali..." + Warna.RESET);
                    break;

                default:

                    System.out.println(Warna.MERAH + "Menu tidak tersedia!" + Warna.RESET);
            }
        }

        while(pilih!=0);
    }

    // TAMBAH
    public void tambahPayroll(){

        try{

            System.out.print(Warna.PINK + "ID Pegawai : " + Warna.RESET);
            int idPegawai = Integer.parseInt(input.nextLine());

            // Periode
            String periode = java.time.YearMonth.now().toString();

            // GAJI POKOK
            String sqlGaji =

            "select j.gaji_pokok "
            + "from project.pegawai p "
            + "join project.jabatan j "
            + "on p.id_jabatan=j.id_jabatan "
            + "where p.id_pegawai=?";

            PreparedStatement psG = conn.prepareStatement(sqlGaji);

            psG.setInt(1,idPegawai);

            ResultSet rsG = psG.executeQuery();

            double gajiPokok = 0;

            if(rsG.next()){

                gajiPokok = rsG.getDouble("gaji_pokok");

            }

            // TOTAL TUNJANGAN
            String sqlT =

            "select coalesce(sum(t.jumlah),0) as total "
            + "from project."
            + "pegawai_tunjangan pt "
            + "JOIN project."
            + "tunjangan t "
            + "ON pt.id_tunjangan="
            + "t.id_tunjangan "
            + "WHERE pt.id_pegawai=?";

            PreparedStatement psT = conn.prepareStatement(sqlT);

            psT.setInt(1,idPegawai);

            ResultSet rsT = psT.executeQuery();

            double tunjangan = 0;

            if(rsT.next()){

                tunjangan = rsT.getDouble("total");

            }

            // TOTAL POTONGAN
            String sqlP =

            "select coalesce(sum(p.jumlah), 0) as total "
            + "from project."
            + "pegawai_potongan pp "
            + "JOIN project."
            + "potongan p "
            + "ON pp.id_potongan="
            + "p.id_potongan "
            + "WHERE pp.id_pegawai=?";

            PreparedStatement psP = conn.prepareStatement(sqlP);

            psP.setInt(1,idPegawai);

            ResultSet rsP = psP.executeQuery();

            double potongan = 0;

            if(rsP.next()){

                potongan = rsP.getDouble("total");
            
            }

             // TOTAL LEMBUR
            String sqlL =

            "select coalesce(sum(jam_lembur),0) as total_jam, "
            + "coalesce(sum(upah_lembur),0) as total_upah "
            + "from project.lembur "
            + "where id_pegawai = ?";

            PreparedStatement psL = conn.prepareStatement(sqlL);

            psL.setInt(1,idPegawai);

            ResultSet rsL = psL.executeQuery();

            int totalJamLembur = 0;
            double lembur = 0;

            if(rsL.next()){

                totalJamLembur = rsL.getInt("total_jam");
                lembur = rsL.getDouble("total_upah");
            }

            // HITUNG JUMLAH ALPHA

            String sqlAlpha =

            "select count(*) as total_alpha "
            + "from project.absensi "
            + "where id_pegawai=? "
            + "and status='Alpha'";

            PreparedStatement psAlpha =conn.prepareStatement(sqlAlpha);

            psAlpha.setInt(1,idPegawai);

            ResultSet rsAlpha = psAlpha.executeQuery();

            int totalAlpha = 0;

            if(rsAlpha.next()){

                totalAlpha = rsAlpha.getInt("total_alpha");

            }

            // AMBIL NOMINAL POTONGAN ALPHA

            String sqlPotongan =

            "select jumlah "
            + "from project.potongan "
            + "where nama_potongan = 'Potongan Absensi'";

            PreparedStatement psPotongan = conn.prepareStatement(sqlPotongan);

            ResultSet rsPotongan = psPotongan.executeQuery();

            double nominalAlpha = 0;

            if(rsPotongan.next()){

                nominalAlpha = rsPotongan.getDouble("jumlah");
            }

            double potonganAlpha = totalAlpha * nominalAlpha;

            System.out.println(Warna.MERAH + "Potongan Alpha : Rp" + potonganAlpha + Warna.RESET);

            double totalGaji = gajiPokok + tunjangan + lembur - (potongan + potonganAlpha);

            String sql =

            "insert into project.payroll "
            + "(id_pegawai,"
            +"periode,"
            + "gaji_pokok,"
            + "total_tunjangan,"
            + "total_potongan,"
            + "total_jam_lembur,"
            + "total_upah_lembur,"
            + "total_gaji)"
            + " values(?,?,?,?,?,?,?,?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,idPegawai);
            ps.setString(2,periode);
            ps.setDouble(3,gajiPokok);
            ps.setDouble(4,tunjangan);
            ps.setDouble(5,potongan + potonganAlpha);
            ps.setInt(6,totalJamLembur);
            ps.setDouble(7,lembur);
            ps.setDouble(8,totalGaji);
            ps.executeUpdate();

            System.out.println(Warna.HIJAU + "Payroll berhasil ditambah!" + Warna.RESET);

        } catch(Exception e){

            System.out.println(e);
        }
    }

    // TAMPIL
    public void tampilPayroll(){

        try{

            String sql =

            "select * "
            + "from project.payroll "
            + "order by id_payroll";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== DATA PAYROLL =====" + Warna.RESET);


            while(rs.next()){

                System.out.println(Warna.BIRU + rs.getInt("id_payroll")
                + " | "
                + rs.getInt("id_pegawai")
                + " | "
                + rs.getString("periode")
                + " | Rp."
                + String.format("%,.0f", rs.getDouble("gaji_pokok"))
                + " | Rp." 
                + String.format("%,.0f", rs.getDouble("total_tunjangan")) 
                + " | Rp." +
                String.format("%,.0f", rs.getDouble("total_potongan")) 
                + " | " +
                rs.getInt("total_jam_lembur") 
                + " jam | Rp." +
                String.format("%,.0f", rs.getDouble("total_upah_lembur")) 
                + " | Rp." +
                String.format("%,.0f", rs.getDouble("total_gaji")) 
                + Warna.RESET
                );

            }

        } catch(Exception e){

            System.out.println(e);
        }
    }

    // UPDATE
    public void updatePayroll(){

        try{

            System.out.print(Warna.PINK + "ID Payroll : " + Warna.RESET);
            int idPayroll = Integer.parseInt(input.nextLine());

            System.out.print(Warna.PINK + "Total Gaji Baru : " + Warna.RESET);
            double totalGaji = Double.parseDouble(input.nextLine());

            String sql =

            "update project.payroll "
            + "set total_gaji = ? "
            + "where id_payroll = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setDouble(1,totalGaji);
            ps.setInt(2,idPayroll);
            ps.executeUpdate();

            System.out.println(Warna.HIJAU + "Payroll berhasil diupdate!" + Warna.RESET);

        } catch(Exception e){

            System.out.println(e);
        }
    }

    // HAPUS
    public void hapusPayroll(){

        try{

            System.out.print(Warna.PINK + "ID Pegawai : " + Warna.RESET);
            int idPegawai = Integer.parseInt(input.nextLine());

            String sql =

            "delete from project.payroll "
            + "where id_pegawai = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,idPegawai);
            ps.executeUpdate();

            System.out.println(Warna.MERAH + "Payroll berhasil dihapus!" + Warna.RESET);

        } catch(Exception e){

            System.out.println(e);
        }
    }

    // SLIP ADMIN
    public void cetakSlipAdmin(){

        System.out.print(Warna.PINK + "ID Pegawai : " + Warna.RESET);
        int idPegawai =Integer.parseInt(input.nextLine());

        lihatSlipGaji(idPegawai);
    }

    // SLIP PEGAWAI
    public void lihatSlipGaji(int idPegawai){

        try{

            String sql =

            "select * "
            + "from project.view_slip_gaji "
            + "where id_pegawai=? "
            + "order by id_payroll desc "
            + "limit 1";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,idPegawai);

            ResultSet rs = ps.executeQuery();
            

            if(rs.next()){

                System.out.println(Warna.NEON_ORANGE + Warna.BOLD + "\n================== SLIP GAJI ==================" + Warna.RESET);
                System.out.println(Warna.GOLD + "Nama : "+ rs.getString("nama_pegawai")+ Warna.RESET);
                System.out.println(Warna.GOLD + "Departemen : "+ rs.getString("nama_departemen") + Warna.RESET);
                System.out.println(Warna.GOLD + "Jabatan : "+ rs.getString("nama_jabatan") + Warna.RESET);
                System.out.println(Warna.GOLD + "Periode : "+ rs.getString("periode") + Warna.RESET);
                System.out.println(Warna.GOLD + "Gaji Pokok : Rp." + String.format("%,.0f", rs.getDouble("gaji_pokok")) + Warna.RESET);
                System.out.println(Warna.HIJAU + "TOTAL GAJI : Rp." + String.format("%,.0f", rs.getDouble("total_gaji")) + Warna.RESET);
            }

            // TUNJANGAN DIPILIH
            System.out.println(Warna.NEON_ORANGE + Warna.BOLD +"\n================== TUNJANGAN ==================" + Warna.RESET);

            String sqlT =

            "select t.nama_tunjangan,"
            + "t.jumlah "
            + "from project.pegawai_tunjangan pt "
            + "join project.tunjangan t "
            + "on pt.id_tunjangan="
            + "t.id_tunjangan "
            + "where pt.id_pegawai=?";

            PreparedStatement psT = conn.prepareStatement(sqlT);

            psT.setInt(1,idPegawai);

            ResultSet rsT = psT.executeQuery();

            double totalTunjangan = 0;

            while(rsT.next()){

                System.out.println(Warna.GOLD +
                rsT.getString("nama_tunjangan")
                + " : Rp."
                + String.format("%,.0f", rsT.getDouble("jumlah"))
                + Warna.RESET
                );

                totalTunjangan +=rsT.getDouble("jumlah");
            }

            // RINCIAN LEMBUR

            System.out.println(Warna.NEON_ORANGE + Warna.BOLD + "\n=============== RINCIAN LEMBUR ===============" + Warna.RESET);

            String sqlL =

            "select tanggal_lembur,"
            + "jam_lembur,"
            + "upah_lembur "
            + "from project.lembur "
            + "where id_pegawai = ?";

            PreparedStatement psL =
                    conn.prepareStatement(sqlL);

            psL.setInt(1, idPegawai);

            ResultSet rsL = psL.executeQuery();

            double totalLembur = 0;

            while(rsL.next()){

                System.out.println(Warna.GOLD + rsL.getDate("tanggal_lembur")
                + " | "
                + rsL.getInt("jam_lembur")
                + " jam | Rp."
                + String.format("%,.0f", rsL.getDouble("upah_lembur")) + Warna.RESET
                );

                totalLembur += rsL.getDouble("upah_lembur");
            }

             // POTONGAN
            System.out.println(Warna.NEON_ORANGE + Warna.BOLD + "\n================== POTONGAN ==================" + Warna.RESET);

            String sqlP =

            "select p.nama_potongan,"
            + "p.jumlah "
            + "FROM project.pegawai_potongan pp "
            + "JOIN project.potongan p "
            + "ON pp.id_potongan="
            + "p.id_potongan "
            + "where pp.id_pegawai=?";

            PreparedStatement psP = conn.prepareStatement(sqlP);

            psP.setInt(1,idPegawai);

            ResultSet rsP = psP.executeQuery();

            double totalPotongan = 0;

            while(rsP.next()){

                System.out.println(Warna.GOLD + 
                rsP.getString("nama_potongan")
                + " : Rp."
                + String.format("%,.0f", rsP.getDouble("jumlah")) + Warna.RESET
                );

                totalPotongan += rsP.getDouble("jumlah");
            }

            // HITUNG JUMLAH ALPHA
            String sqlAlpha =

            "select count(*) as total_alpha "
            + "from project.absensi "
            + "where id_pegawai=? "
            + "and status='Alpha'";

            PreparedStatement psAlpha =conn.prepareStatement(sqlAlpha);

            psAlpha.setInt(1,idPegawai);

            ResultSet rsAlpha = psAlpha.executeQuery();

            int totalAlpha = 0;

            if(rsAlpha.next()){

                totalAlpha = rsAlpha.getInt("total_alpha");

            }

            // AMBIL NOMINAL POTONGAN ALPHA
            String sqlPotongan =

            "select jumlah "
            + "from project.potongan "
            + "where nama_potongan='Potongan Absensi'";

            PreparedStatement psPotongan =conn.prepareStatement(sqlPotongan);

            ResultSet rsPotongan = psPotongan.executeQuery();

            double nominalAlpha = 0;

            if(rsPotongan.next()){

                nominalAlpha = rsPotongan.getDouble("jumlah");

            }

            double potonganAlpha = totalAlpha * nominalAlpha;

            System.out.println(Warna.NEON_ORANGE + Warna.BOLD + "\n==============================================" + Warna.RESET);
            System.out.println(Warna.GOLD + "TOTAL TUNJANGAN : Rp." + String.format("%,.0f", totalTunjangan)+ Warna.RESET);
            System.out.println(Warna.GOLD + "TOTAL LEMBUR : Rp." + String.format("%,.0f", totalLembur)+ Warna.RESET);
            System.out.println(Warna.GOLD + "TOTAL POTONGAN : Rp." + String.format("%,.0f", totalPotongan)+ Warna.RESET);
            System.out.println(Warna.GOLD + "Potongan Alpha : Rp." + String.format("%,.0f", potonganAlpha)+ Warna.RESET);
            System.out.println(Warna.HIJAU + "TOTAL GAJI : Rp." + String.format("%,.0f", rs.getDouble("total_gaji")) + Warna.RESET);

        } catch(Exception e){

            System.out.println(e);

        }
    }
}
