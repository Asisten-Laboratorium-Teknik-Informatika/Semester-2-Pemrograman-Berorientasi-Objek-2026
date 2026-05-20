import java.sql.*;
import java.util.Scanner;

public class Admin {

    Scanner input =
        new Scanner(System.in);

    // ======================================================
    // MENU ADMIN
    // ======================================================

    public void menu() {

        while (true) {

            System.out.println(UI.RED);

            System.out.println(
                "\n======================================================"
            );

            System.out.println(
                "                 ADMIN DASHBOARD"
            );

            System.out.println(
                "======================================================"
            );

            System.out.println(UI.RESET);

            System.out.println(
                "1. Lihat Total Booking"
            );

            System.out.println(
                "2. Lihat Total Pendapatan"
            );

            System.out.println(
                "3. Reset Semua Booking"
            );

            System.out.println(
                "4. Hapus Semua Akun User"
            );

            System.out.println(
                "5. Lihat Detail Booking"
            );

            System.out.println(
                "6. Lihat Status Studio"
            );

            System.out.println(
                "7. Kembali"
            );

            System.out.print(
                "\nPilih menu : "
            );

            int p =
                input.nextInt();

            // ======================================================
            // TOTAL BOOKING
            // ======================================================

            if (p == 1) {

                totalBooking();
            }

            // ======================================================
            // TOTAL PENDAPATAN
            // ======================================================

            else if (p == 2) {

                totalPendapatan();
            }

            // ======================================================
            // RESET BOOKING
            // ======================================================

            else if (p == 3) {

                resetBooking();
            }

            // ======================================================
            // HAPUS AKUN USER
            // ======================================================

            else if (p == 4) {

                hapusSemuaAkun();
            }

        // ======================================================
        // DETAIL BOOKING
        // ======================================================

        else if (p == 5) {

            detailBooking();
        }

        // ======================================================
        // STATUS STUDIO
        // ======================================================

        else if (p == 6) {

            statusStudio();
        }

            // ======================================================
            // KEMBALI
            // ======================================================

        else if (p == 7) {

                break;
            }

            else {

                UI.error(
                    "Menu tidak tersedia!"
                );
            }
        }
    }

    // ======================================================
    // TOTAL BOOKING
    // ======================================================

    public void totalBooking() {

        try {

            Connection c =
                Koneksi.connect();

            Statement s =
                c.createStatement();

            ResultSet rs =
                s.executeQuery(
                    "SELECT COUNT(*) AS total FROM tiket_bioskop"
                );

            if (rs.next()) {

                System.out.println(
                    UI.GREEN +
                    "\nTotal Booking : "
                    + rs.getInt("total")
                    + UI.RESET
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // ======================================================
    // TOTAL PENDAPATAN
    // ======================================================

    public void totalPendapatan() {

        try {

            Connection c =
                Koneksi.connect();

            Statement s =
                c.createStatement();

            ResultSet rs =
                s.executeQuery(
                    "SELECT " +
                    "(SELECT COALESCE(SUM(harga), 0) FROM tiket_bioskop) AS total_tiket, " +
                    "(SELECT COALESCE(SUM(total_harga), 0) FROM pembelian_snack) AS total_snack"
                );

            if (rs.next()) {

                int totalTiket = rs.getInt("total_tiket");
                int totalSnack = rs.getInt("total_snack");
                int totalSemua = totalTiket + totalSnack;

                System.out.println(UI.CYAN + "\n=== LAPORAN PENDAPATAN ===" + UI.RESET);
                System.out.println("Pendapatan Tiket : Rp " + totalTiket);
                System.out.println("Pendapatan Snack : Rp " + totalSnack);
                System.out.println(
                    UI.GREEN +
                    "\nTotal Pendapatan : Rp "
                    + totalSemua
                    + UI.RESET
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // ======================================================
    // RESET BOOKING
    // ======================================================

    public void resetBooking() {

        try {

            Connection c =
                Koneksi.connect();

            System.out.println(
                UI.RED +
                "\nPERINGATAN!"
                + UI.RESET
            );

            System.out.println(
                "Semua data booking akan dihapus!"
            );

            System.out.print(
                "\nYakin reset booking? (y/n) : "
            );

            input.nextLine();

            String konfirmasi =
                input.nextLine();

            if (
                konfirmasi.equalsIgnoreCase("y")
            ) {

                PreparedStatement ps =
                    c.prepareStatement(
                        "DELETE FROM tiket_bioskop"
                    );

                ps.executeUpdate();

                System.out.print(
                    "\nMereset booking"
                );

                for (
                    int i = 0;
                    i < 6;
                    i++
                ) {

                    Thread.sleep(300);

                    System.out.print(".");
                }

                System.out.println(
                    UI.GREEN +
                    "\n\nSemua booking berhasil direset!"
                    + UI.RESET
                );
            }

            else {

                System.out.println(
                    UI.YELLOW +
                    "\nReset booking dibatalkan."
                    + UI.RESET
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // ======================================================
    // HAPUS SEMUA AKUN USER
    // ======================================================

    public void hapusSemuaAkun() {

        try {

            Connection c =
                Koneksi.connect();

            System.out.println(
                UI.RED +
                "\nPERINGATAN!"
                + UI.RESET
            );

            System.out.println(
                "Semua akun user akan dihapus!"
            );

            System.out.println(
                "Akun admin tetap aman."
            );

            System.out.print(
                "\nYakin hapus semua akun? (y/n) : "
            );

            input.nextLine();

            String konfirmasi =
                input.nextLine();

            // ======================================================
            // KONFIRMASI
            // ======================================================

            if (
                konfirmasi.equalsIgnoreCase("y")
            ) {

                PreparedStatement ps =
                    c.prepareStatement(
                        "DELETE FROM users WHERE role='user'"
                    );

                int total =
                    ps.executeUpdate();

                System.out.print(
                    "\nMenghapus akun"
                );

                for (
                    int i = 0;
                    i < 6;
                    i++
                ) {

                    Thread.sleep(300);

                    System.out.print(".");
                }

                System.out.println(
                    UI.GREEN +
                    "\n\n" + total +
                    " akun user berhasil dihapus!"
                    + UI.RESET
                );
            }

            // ======================================================
            // BATAL
            // ======================================================

            else {

                System.out.println(
                    UI.YELLOW +
                    "\nPenghapusan akun dibatalkan."
                    + UI.RESET
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // ======================================================
    // DETAIL BOOKING
    // ======================================================

    public void detailBooking() {
        try {
            Connection c = Koneksi.connect();
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(
                "SELECT f.judul, f.is_imax, t.studio_id, t.jam_tayang, t.kursi, u.username " +
                "FROM tiket_bioskop t " +
                "JOIN film f ON t.film_id = f.id_film " +
                "JOIN users u ON t.user_id = u.id_user " +
                "ORDER BY f.judul, t.studio_id, t.jam_tayang"
            );

            System.out.println(UI.CYAN + "\n==========================================================================");
            System.out.println("                          DETAIL BOOKING TIKET");
            System.out.println("=================================================================================" + UI.RESET);
            System.out.printf("%-30s %-15s %-12s %-10s %-15s\n", "JUDUL FILM", "STUDIO", "JAM TAYANG", "KURSI", "USERNAME");
            System.out.println("---------------------------------------------------------------------------------");
            
            boolean ada = false;
            while (rs.next()) {
                ada = true;
                int sId = rs.getInt("studio_id");
                String namaStudio = "";
                if (sId >= 1 && sId <= 10) namaStudio = "Regular " + sId;
                else if (sId >= 11 && sId <= 20) namaStudio = "Premiere " + (sId - 10);
                else if (sId >= 21 && sId <= 23) namaStudio = "IMAX " + (sId - 20);
                else namaStudio = "Studio " + sId;

                String judul = rs.getString("judul");
                if (rs.getBoolean("is_imax")) judul += " [IMAX]";

                System.out.printf("%-30.30s %-15s %-12s %-10s %-15s\n", 
                    judul, namaStudio, rs.getString("jam_tayang"), rs.getString("kursi"), rs.getString("username"));
            }
            if (!ada) System.out.println("Belum ada data booking.");
            System.out.println("=================================================================================");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ======================================================
    // STATUS STUDIO
    // ======================================================

    public void statusStudio() {
        try {
            Connection c = Koneksi.connect();
            
            System.out.print("\nMasukkan ID Studio yang ingin dicek (0 untuk lihat semua) : ");
            int idStudio = input.nextInt();
            input.nextLine(); // bersihkan sisa enter/newline
            
            String query = "SELECT t.studio_id, t.kursi, u.username, t.jam_tayang, f.judul, f.is_imax " +
                           "FROM tiket_bioskop t " +
                           "JOIN users u ON t.user_id = u.id_user " +
                           "JOIN film f ON t.film_id = f.id_film ";
                           
            if (idStudio > 0) {
                query += "WHERE t.studio_id = " + idStudio + " ";
            }
            
            query += "ORDER BY t.studio_id ASC, t.jam_tayang ASC, t.kursi ASC";

            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(query);

            System.out.println(UI.CYAN + "\n==========================================================================================");
            System.out.println("                                  STATUS ISI STUDIO");
            System.out.println("==========================================================================================" + UI.RESET);
            System.out.printf("%-15s %-10s %-15s %-12s %-35s\n", "STUDIO", "KURSI", "PEMBELI", "JAM TAYANG", "FILM");
            System.out.println("------------------------------------------------------------------------------------------");
            
            boolean ada = false;
            while (rs.next()) {
                ada = true;
                int sId = rs.getInt("studio_id");
                String namaStudio = "";
                if (sId >= 1 && sId <= 10) namaStudio = "Regular " + sId;
                else if (sId >= 11 && sId <= 20) namaStudio = "Premiere " + (sId - 10);
                else if (sId >= 21 && sId <= 23) namaStudio = "IMAX " + (sId - 20);
                else namaStudio = "Studio " + sId;

                String judul = rs.getString("judul");
                if (rs.getBoolean("is_imax")) judul += " [IMAX]";

                System.out.printf("%-15s %-10s %-15s %-12s %-35.35s\n", 
                    namaStudio, rs.getString("kursi"), rs.getString("username"), rs.getString("jam_tayang"), judul);
            }
            if (!ada) System.out.println("Belum ada kursi yang terisi.");
            System.out.println("==========================================================================================");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}