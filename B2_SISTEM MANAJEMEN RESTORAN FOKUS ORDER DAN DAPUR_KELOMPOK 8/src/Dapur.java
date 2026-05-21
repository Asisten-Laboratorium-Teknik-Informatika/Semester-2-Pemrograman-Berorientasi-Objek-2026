import java.sql.*;

public class Dapur {
    
    public static void lihatAntrian() {
        String sql = "SELECT p.id, p.no_invoice, p.nama_pelanggan, p.waktu_order, " +
                     "STRING_AGG(CONCAT(m.nama, ' (', dp.jumlah, ')'), ', ') as items " +
                     "FROM pesanan p " +
                     "JOIN detail_pesanan dp ON p.id = dp.pesanan_id " +
                     "JOIN menu m ON dp.menu_id = m.id " +
                     "WHERE p.status IN ('baru', 'diproses') " +
                     "GROUP BY p.id, p.no_invoice, p.nama_pelanggan, p.waktu_order " +
                     "ORDER BY p.waktu_order";
        try {
            Statement st = Database.getConn().createStatement();
            ResultSet rs = st.executeQuery(sql);
            System.out.println("\n============================================================");
            System.out.println("                   ANTRIAN DAPUR");
            System.out.println("============================================================");
            if (!rs.isBeforeFirst()) {
                System.out.println("                  (Tidak ada antrian)");
            }
            while (rs.next()) {
                System.out.println("ID Pesanan : " + rs.getInt("id"));
                System.out.println("No Invoice : " + rs.getString("no_invoice"));
                System.out.println("Pelanggan  : " + rs.getString("nama_pelanggan"));
                System.out.println("Pesanan    : " + rs.getString("items"));
                System.out.println("Waktu Order: " + rs.getTimestamp("waktu_order"));
                System.out.println("------------------------------------------------------------");
            }
            System.out.println("============================================================\n");
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error lihat antrian: " + e.getMessage());
        }
    }
    
    public static void ambilPesanan(int idPesanan) {
        String sql = "UPDATE pesanan SET status = 'diproses' WHERE id = " + idPesanan + " AND status = 'baru'";
        try {
            Statement st = Database.getConn().createStatement();
            int row = st.executeUpdate(sql);
            if (row > 0) {
                System.out.println("Pesanan " + idPesanan + " sedang diproses");
            } else {
                System.out.println("Pesanan tidak ditemukan atau sudah diproses");
            }
            st.close();
        } catch (SQLException e) {
            System.out.println("Error ambil pesanan: " + e.getMessage());
        }
    }
    
    public static void selesaikanPesanan(int idPesanan) {
        String sql = "UPDATE pesanan SET status = 'siap' WHERE id = " + idPesanan + " AND status = 'diproses'";
        try {
            Statement st = Database.getConn().createStatement();
            int row = st.executeUpdate(sql);
            if (row > 0) {
                System.out.println("Pesanan " + idPesanan + " selesai, siap diantar");
            } else {
                System.out.println("Pesanan tidak ditemukan atau belum diproses");
            }
            st.close();
        } catch (SQLException e) {
            System.out.println("Error selesaikan pesanan: " + e.getMessage());
        }
    }
}