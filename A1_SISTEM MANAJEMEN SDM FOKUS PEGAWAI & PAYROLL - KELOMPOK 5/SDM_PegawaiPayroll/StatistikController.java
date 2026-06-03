package SDM_PegawaiPayroll;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Scanner;

public class StatistikController {

    Scanner input = new Scanner(System.in);
    Connection conn;

    public StatistikController() {
        conn = KoneksiDatabase.connect();
    }

    public void menu() {

        int pilih;

        do {

            System.out.println(Warna.CYAN + Warna.BOLD + "\n===== STATISTIK & FILTER =====" + Warna.RESET);
            System.out.println(Warna.PINK + Warna.BOLD + "1. Cari Pegawai (LIKE)" + Warna.RESET);
            System.out.println(Warna.PINK + Warna.BOLD + "2. Total Pegawai" + Warna.RESET);
            System.out.println(Warna.PINK + Warna.BOLD + "3. Gaji di atas rata-rata" + Warna.RESET);
            System.out.println(Warna.PINK + Warna.BOLD + "4. Gaji tertinggi" + Warna.RESET);
            System.out.println(Warna.PINK + Warna.BOLD + "5. Gaji terendah" + Warna.RESET);
            System.out.println(Warna.PINK + Warna.BOLD + "6. Sorting gaji DESC" + Warna.RESET);
            System.out.println(Warna.PINK + Warna.BOLD + "7. Pegawai per departemen" + Warna.RESET);
            System.out.println(Warna.PINK + Warna.BOLD + "8. Data pegawai lengkap (JOIN)" + Warna.RESET);
            System.out.println(Warna.PINK + Warna.BOLD + "9. Rata-rata gaji" + Warna.RESET);
            System.out.println(Warna.PINK + Warna.BOLD + "10. Pegawai paling rajin" + Warna.RESET);
            System.out.println(Warna.MERAH + "0. Kembali" + Warna.RESET);

            System.out.print(Warna.KUNING + "Pilih: " + Warna.RESET);
            pilih = input.nextInt();
            input.nextLine();

            switch (pilih) {

                case 1:
                    try {

                        System.out.print(Warna.KUNING + "Nama pegawai: " + Warna.RESET);
                        String nama = input.nextLine();

                        String sql = "select * from project.pegawai where nama_pegawai like ?";
                        var ps = conn.prepareStatement(sql);
                        ps.setString(1, "%" + nama + "%");

                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {
                            System.out.println(Warna.UNGU + rs.getInt("id_pegawai") + " - " +
                                    rs.getString("nama_pegawai")+ Warna.RESET);
                        }

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:
                    try {

                        String sql = "select count(*) as total from project.pegawai";
                        var ps = conn.prepareStatement(sql);
                        ResultSet rs = ps.executeQuery();

                        if (rs.next()) {
                            System.out.println(Warna.CYAN + Warna.BOLD + "Total pegawai: " + rs.getInt("total")+ Warna.RESET);
                        }

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 3:
                    try {
                        
                        String sql = "select * from project.jabatan " +
                                     "where gaji_pokok > (select avg(gaji_pokok) from project.jabatan)";

                        var ps = conn.prepareStatement(sql);
                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {
                            System.out.println(Warna.UNGU + rs.getString("nama_jabatan") +
                                    " - " + String.format("%,.0f", rs.getDouble("gaji_pokok")) + Warna.RESET);
                        }

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 4:
                    try {

                        String sql = "select * from project.jabatan where gaji_pokok = (select max(gaji_pokok) from project.jabatan)";

                        var ps = conn.prepareStatement(sql);
                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {
                            System.out.println(Warna.UNGU + rs.getString("nama_jabatan") +
                                    " - " + String.format("%,.0f", rs.getDouble("gaji_pokok")) + Warna.RESET);
                        }

                    } catch (Exception e) {

                        System.out.println(e.getMessage());
                    }

                    break;

                case 5:
                    try {

                        String sql = "select * from project.jabatan where gaji_pokok = (select min(gaji_pokok) from project.jabatan)";

                        var ps = conn.prepareStatement(sql);
                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {
                            System.out.println(Warna.UNGU + rs.getString("nama_jabatan") +
                                    " - " + String.format("%,.0f", rs.getDouble("gaji_pokok")) + Warna.RESET);
                        }

                    } catch (Exception e) {

                        System.out.println(e.getMessage());
                    }
                    break;

                case 6:
                    try {

                        String sql = "select * from project.jabatan order by gaji_pokok desc";
                        var ps = conn.prepareStatement(sql);
                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {
                            System.out.println(Warna.UNGU + rs.getString("nama_jabatan") +
                                    " - " + String.format("%,.0f", rs.getDouble("gaji_pokok")) + Warna.RESET);
                        }

                    } catch (Exception e) {

                        System.out.println(e.getMessage());
                    }
                    break;

                case 7:
                    try {

                        String sql = 
                            "select d.nama_departemen, count(p.id_pegawai) as total " +
                            "from project.departemen d " +
                            "join project.jabatan j on d.id_departemen = j.id_departemen " +
                            "join project.pegawai p on j.id_jabatan = p.id_jabatan " +
                            "group by d.nama_departemen";

                        var ps = conn.prepareStatement(sql);

                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {
                            System.out.println(Warna.UNGU + rs.getString("nama_departemen") +
                                    " - " + rs.getInt("total") + Warna.RESET);
                        }

                    } catch (Exception e) {

                        System.out.println(e.getMessage());
                    }

                    break;

                case 8:
                    try {

                        String sql = 
                            "select p.nama_pegawai, j.nama_jabatan, d.nama_departemen, j.gaji_pokok " +
                            "from project.pegawai p " +
                            "join project.jabatan j on p.id_jabatan = j.id_jabatan " +
                            "join project.departemen d on j.id_departemen = d.id_departemen";

                        var ps = conn.prepareStatement(sql);
                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {
                            System.out.println(Warna.UNGU + rs.getString("nama_pegawai") +
                                    " | " + rs.getString("nama_jabatan") +
                                    " | " + rs.getString("nama_departemen") + Warna.RESET);
                        }

                    } catch (Exception e) {

                        System.out.println(e.getMessage());
                    }

                    break;

                case 9:
                    try {

                        String sql = "select avg(gaji_pokok) as rata from project.jabatan";
                        var ps = conn.prepareStatement(sql);
                        ResultSet rs = ps.executeQuery();

                        if (rs.next()) {
                            System.out.println(Warna.KUNING + "Rata-rata gaji: " + String.format("%,.0f", rs.getDouble("rata")) + Warna.RESET);
                        }

                    } catch (Exception e) {

                        System.out.println(e.getMessage());
                    }

                    break;

                case 10:
                    try {

                        String sql = 
                            "select p.nama_pegawai, count(a.id_absensi) as total " +
                            "from project.pegawai p " +
                            "join project.absensi a on p.id_pegawai = a.id_pegawai " +
                            "where a.status = 'Hadir' " +
                            "group by p.nama_pegawai " +
                            "order by total desc " +
                            "limit 3";

                        var ps = conn.prepareStatement(sql);
                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {
                            System.out.println(Warna.KUNING + rs.getString("nama_pegawai") +
                                    " - " + rs.getInt("total")+ Warna.RESET);
                        }

                    } catch (Exception e) {

                        System.out.println(e.getMessage());
                    }

                    break;
            }

        } while (pilih != 0);
    }
}