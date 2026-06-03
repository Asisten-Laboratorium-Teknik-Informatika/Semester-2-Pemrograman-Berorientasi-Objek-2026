package SDM_PegawaiPayroll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class JabatanController {

    Connection conn;
    Scanner input = new Scanner(System.in);

    public JabatanController() {

        conn = KoneksiDatabase.connect();

    }

    public void menu() {

        int pilih;

        do {

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== MENU JABATAN =====" + Warna.RESET);
            System.out.println(Warna.BIRU + "1. Tambah Jabatan" + Warna.RESET);
            System.out.println(Warna.BIRU + "2. Tampil Jabatan" + Warna.RESET);
            System.out.println(Warna.BIRU + "3. Update Jabatan" + Warna.RESET);
            System.out.println(Warna.BIRU + "4. Hapus Jabatan" + Warna.RESET);
            System.out.println(Warna.MERAH + "0. Kembali" + Warna.RESET);

            System.out.print(Warna.KUNING + "Pilih : " + Warna.RESET);

            pilih = Integer.parseInt(input.nextLine());

            switch(pilih){

                case 1:

                    tambahJabatan();

                    break;

                case 2:

                    tampilJabatan();

                    break;

                case 3:

                    updateJabatan();

                    break;

                case 4:

                    hapusJabatan();

                    break;

            }

        }

        while(pilih != 0);

    }

    // TAMBAH
    public void tambahJabatan() {

        try {

            System.out.print(Warna.PINK + "ID Jabatan : " + Warna.RESET);
            int idJabatan = Integer.parseInt(input.nextLine());

            System.out.print(Warna.PINK + "ID Departemen : " + Warna.RESET);
            int idDepartemen = Integer.parseInt(input.nextLine());

            System.out.print(Warna.PINK + "Nama Jabatan : " + Warna.RESET);
            String namaJabatan = input.nextLine();

            System.out.print(Warna.PINK + "Gaji Pokok : " + Warna.RESET);
            double gajiPokok = Double.parseDouble(input.nextLine());

            String sql =
            "insert into project.jabatan "
          + "(id_jabatan,id_departemen,"
          + "nama_jabatan,gaji_pokok) "
          + "values(?,?,?,?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,idJabatan);
            ps.setInt(2,idDepartemen);
            ps.setString(3,namaJabatan);
            ps.setDouble(4,gajiPokok);

            ps.executeUpdate();

            System.out.println(Warna.HIJAU + "Data berhasil ditambah!" + Warna.RESET);

        } catch(Exception e){

            System.out.println(e);

        }

    }

    // TAMPIL
    public void tampilJabatan() {

        try {

            String sql =
            "select * from project.jabatan";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== DATA JABATAN =====" + Warna.RESET);

            while(rs.next()) {

                System.out.println(Warna.BIRU +
                        rs.getInt("id_jabatan")
                        +" | "
                        +rs.getInt("id_departemen")
                        +" | "
                        +rs.getString("nama_jabatan")
                        +" | "
                        +rs.getDouble("gaji_pokok") + Warna.RESET
                );

            }

        } catch(Exception e){

            System.out.println(e);

        }

    }

    // UPDATE
    public void updateJabatan() {

        try {

            System.out.print(Warna.PINK + "ID Jabatan : " + Warna.RESET);
            int idJabatan = Integer.parseInt(input.nextLine());

            System.out.print(Warna.PINK + "Nama Jabatan Baru : " + Warna.RESET);
            String namaBaru = input.nextLine();

            System.out.print(Warna.PINK + "Gaji Pokok Baru : " + Warna.RESET);
            double gajiBaru = Double.parseDouble(input.nextLine());

            String sql =
            "update project.jabatan "
          + "set nama_jabatan = ?, "
          + "gaji_pokok = ? "
          + "where id_jabatan = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1,namaBaru);
            ps.setDouble(2,gajiBaru);
            ps.setInt(3,idJabatan);
            ps.executeUpdate();

            System.out.println(Warna.HIJAU + "Update berhasil!" + Warna.RESET);

        }

        catch(Exception e){

            System.out.println(e);

        }

    }

    // DELETE
    public void hapusJabatan() {

        try {

            System.out.print(Warna.PINK + "ID Jabatan : " + Warna.RESET);

            int idJabatan = Integer.parseInt(input.nextLine());

            String sql =
            "delete from project.jabatan "
          + "where id_jabatan = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,idJabatan);
            ps.executeUpdate();

            System.out.println(Warna.MERAH + "Delete berhasil!" + Warna.RESET);

        } catch(Exception e){

            System.out.println(e);

        }

    }

}
