import java.sql.*;

public class Laporan {
    
    public static void harian() {
        String sql = "SELECT COUNT(*) as total_pesanan, COALESCE(SUM(total_harga),0) as pendapatan " +
                     "FROM pesanan WHERE DATE(waktu_order) = CURRENT_DATE AND status != 'batal'";
        try {
            Statement st = Database.getConn().createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                System.out.println("\n============================================================");
                System.out.println("              LAPORAN PENJUALAN HARI INI");
                System.out.println("============================================================");
                System.out.println("Tanggal       : " + java.time.LocalDate.now());
                System.out.println("Total Pesanan : " + rs.getInt("total_pesanan"));
                System.out.println("Pendapatan    : Rp" + rs.getInt("pendapatan"));
                System.out.println("============================================================\n");
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error laporan harian: " + e.getMessage());
        }
    }
    
    public static void menuTerlaris() {
        String sql = "SELECT m.nama, SUM(dp.jumlah) as terjual " +
                     "FROM detail_pesanan dp JOIN menu m ON dp.menu_id = m.id " +
                     "JOIN pesanan p ON dp.pesanan_id = p.id " +
                     "WHERE DATE(p.waktu_order) = CURRENT_DATE " +
                     "GROUP BY m.id, m.nama ORDER BY terjual DESC LIMIT 5";
        try {
            Statement st = Database.getConn().createStatement();
            ResultSet rs = st.executeQuery(sql);
            System.out.println("============================================================");
            System.out.println("                TOP 5 MENU TERLARIS");
            System.out.println("============================================================");
            int no = 1;
            while (rs.next()) {
                System.out.println(no++ + ". " + rs.getString("nama") + " - " + rs.getInt("terjual") + " terjual");
            }
            System.out.println("============================================================\n");
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error menu terlaris: " + e.getMessage());
        }
    }
}