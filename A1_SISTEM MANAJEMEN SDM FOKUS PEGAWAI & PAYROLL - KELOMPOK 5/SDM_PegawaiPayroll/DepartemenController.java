package SDM_PegawaiPayroll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class DepartemenController {

    Connection conn;
    Scanner input = new Scanner(System.in);

    public DepartemenController() {
        conn = KoneksiDatabase.connect();
    }

    public void menu() {

        int pilih;

        do {

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== MENU DEPARTEMEN =====" + Warna.RESET);
            System.out.println(Warna.BIRU + "1. Tambah Departemen" + Warna.RESET);
            System.out.println(Warna.BIRU + "2. Tampil Departemen" + Warna.RESET);
            System.out.println(Warna.BIRU + "3. Update Departemen" + Warna.RESET);
            System.out.println(Warna.BIRU + "4. Hapus Departemen" + Warna.RESET);
            System.out.println(Warna.MERAH + "0. Kembali" + Warna.RESET);

            System.out.print(Warna.KUNING + "Pilih : " + Warna.RESET);
            pilih = Integer.parseInt(input.nextLine());

            switch(pilih){

                case 1:
                    tambahDepartemen();
                    break;

                case 2:
                    tampilDepartemen();
                    break;

                case 3:
                    updateDepartemen();
                    break;

                case 4:
                    hapusDepartemen();
                    break;

                case 0:
                    System.out.println(Warna.MERAH + "Kembali" + Warna.RESET);
                    break;

                default:
                    System.out.println(Warna.MERAH + "Menu tidak tersedia!" + Warna.RESET);
            }

        } while(pilih != 0);
    }

    // Tambah
    public void tambahDepartemen() {

        try {

            System.out.print(Warna.PINK + "ID Departemen : " + Warna.RESET);
            int idDepartemen = Integer.parseInt(input.nextLine());

            System.out.print(Warna.PINK + "Nama Departemen : " + Warna.RESET);
            String namaDepartemen = input.nextLine();

            String sql =
                    "insert into project.departemen "
                  + "(id_departemen,nama_departemen) "
                  + "values (?,?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,idDepartemen);
            ps.setString(2,namaDepartemen);

            ps.executeUpdate();

            System.out.println(Warna.HIJAU + "Data berhasil ditambahkan!" + Warna.RESET);

        } catch(Exception e){

            System.out.println(e);
        }
    }

    // Tampil
    public void tampilDepartemen() {

        try {

            String sql =
                    "select * from project.departemen";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== DATA DEPARTEMEN =====" + Warna.RESET);

            while(rs.next()) {

                System.out.println(Warna.BIRU +
                        rs.getInt("id_departemen")
                        +" | "
                        +rs.getString("nama_departemen") + Warna.RESET
                );
            }

        } catch(Exception e){

            System.out.println(e);
        }
    }

    // Update
    public void updateDepartemen() {

        try {

            System.out.print(Warna.PINK + "ID Departemen : " + Warna.RESET);
            int idDepartemen = Integer.parseInt(input.nextLine());

            System.out.print(Warna.PINK + "Nama Baru : " + Warna.RESET);
            String namaBaru =input.nextLine();

            String sql =
                  "update project.departemen "
                  + "set nama_departemen = ? "
                  + "where id_departemen = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1,namaBaru);
            ps.setInt(2,idDepartemen);

            ps.executeUpdate();

            System.out.println(Warna.HIJAU + "Update berhasil!" + Warna.RESET);

        } catch(Exception e){

            System.out.println(e);
        }
    }

    // Hapus
    public void hapusDepartemen() {

        try {

            System.out.print(Warna.PINK + "ID Departemen : " + Warna.RESET);

            int idDepartemen = Integer.parseInt(input.nextLine());

            String sql =
                    "delete from project.departemen "
                  + "where id_departemen = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1,idDepartemen);

            ps.executeUpdate();

            System.out.println(Warna.MERAH + "Delete berhasil!" + Warna.RESET);

        } catch(Exception e){

            System.out.println(e);
        }
    }
}