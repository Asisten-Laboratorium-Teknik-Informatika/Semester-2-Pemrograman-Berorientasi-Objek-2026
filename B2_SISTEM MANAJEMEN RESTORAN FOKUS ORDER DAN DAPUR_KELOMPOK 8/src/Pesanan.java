import java.sql.*;

public class Pesanan {
    
    private static String buatInvoice() {
        return "INV" + System.currentTimeMillis();
    }
    
    public static int buatBaru(int idMeja, String namaPelanggan) {
        String noInv = buatInvoice();
        String sql = "INSERT INTO pesanan (no_invoice, meja_id, nama_pelanggan, id_kasir, status) " +
                     "VALUES ('" + noInv + "', " + idMeja + ", '" + namaPelanggan + "', 1, 'baru') RETURNING id";
        try {
            Statement st = Database.getConn().createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                int id = rs.getInt("id");
                st.executeUpdate("UPDATE meja SET status = 'terisi' WHERE id = " + idMeja);
                System.out.println("\nPesanan berhasil dibuat");
                System.out.println("ID Pesanan: " + id);
                System.out.println("No Invoice: " + noInv);
                rs.close();
                st.close();
                return id;
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error buat pesanan: " + e.getMessage());
        }
        return -1;
    }
    
    public static void tambahItem(int idPesanan, int idMenu, int jumlah, String catatan) {
        int harga = Menu.getHarga(idMenu);
        int subtotal = harga * jumlah;
        String sql = "INSERT INTO detail_pesanan (pesanan_id, menu_id, jumlah, harga_satuan, subtotal, catatan_item) " +
                     "VALUES (" + idPesanan + ", " + idMenu + ", " + jumlah + ", " + harga + ", " + subtotal + ", '" + catatan + "')";
        try {
            Statement st = Database.getConn().createStatement();
            st.executeUpdate(sql);
            st.executeUpdate("UPDATE pesanan SET total_harga = (SELECT SUM(subtotal) FROM detail_pesanan WHERE pesanan_id = " + idPesanan + ") WHERE id = " + idPesanan);
            Menu.kurangiStok(idMenu, jumlah);
            System.out.println("  " + Menu.getNama(idMenu) + " x" + jumlah + " = Rp" + subtotal);
            st.close();
        } catch (SQLException e) {
            System.out.println("Error tambah item: " + e.getMessage());
        }
    }
    
    public static void lihatSemua() {
        String sql = "SELECT p.id, p.no_invoice, p.nama_pelanggan, m.nomor_meja, p.total_harga, p.status " +
                     "FROM pesanan p JOIN meja m ON p.meja_id = m.id " +
                     "WHERE p.status NOT IN ('selesai','batal') ORDER BY p.id DESC";
        try {
            Statement st = Database.getConn().createStatement();
            ResultSet rs = st.executeQuery(sql);
            System.out.println("\n============================================================");
            System.out.println("                   PESANAN AKTIF");
            System.out.println("============================================================");
            System.out.println("ID  Invoice          Pelanggan             Meja  Total      Status");
            while (rs.next()) {
                System.out.printf("%-3d %-15s %-20s %-4d Rp%-7d %s\n",
                    rs.getInt("id"),
                    rs.getString("no_invoice"),
                    rs.getString("nama_pelanggan"),
                    rs.getInt("nomor_meja"),
                    rs.getInt("total_harga"),
                    rs.getString("status"));
            }
            System.out.println("============================================================\n");
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error lihat pesanan: " + e.getMessage());
        }
    }
    
    public static void lihatDetail(int idPesanan) {
        String sql = "SELECT p.*, m.nomor_meja FROM pesanan p JOIN meja m ON p.meja_id = m.id WHERE p.id = " + idPesanan;
        try {
            Statement st = Database.getConn().createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                System.out.println("\n============================================================");
                System.out.println("                   DETAIL PESANAN");
                System.out.println("============================================================");
                System.out.println("No Invoice    : " + rs.getString("no_invoice"));
                System.out.println("Nama Pelanggan: " + rs.getString("nama_pelanggan"));
                System.out.println("Nomor Meja    : " + rs.getInt("nomor_meja"));
                System.out.println("Total Harga   : Rp" + rs.getInt("total_harga"));
                System.out.println("Status        : " + rs.getString("status"));
                System.out.println("\nItem Pesanan:");
                System.out.println("------------------------------------------------------------");
                
                ResultSet rs2 = st.executeQuery("SELECT m.nama, dp.jumlah, dp.subtotal, dp.catatan_item FROM detail_pesanan dp JOIN menu m ON dp.menu_id = m.id WHERE dp.pesanan_id = " + idPesanan);
                while (rs2.next()) {
                    System.out.println("  - " + rs2.getString("nama") + " x" + rs2.getInt("jumlah") + " = Rp" + rs2.getInt("subtotal"));
                    if (rs2.getString("catatan_item") != null && !rs2.getString("catatan_item").isEmpty()) {
                        System.out.println("    Catatan: " + rs2.getString("catatan_item"));
                    }
                }
                rs2.close();
                System.out.println("============================================================\n");
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error detail pesanan: " + e.getMessage());
        }
    }
    
    public static void updateStatus(int idPesanan, String statusBaru) {
        String sql = "UPDATE pesanan SET status = '" + statusBaru + "' WHERE id = " + idPesanan;
        try {
            Statement st = Database.getConn().createStatement();
            st.executeUpdate(sql);
            if (statusBaru.equals("selesai") || statusBaru.equals("batal")) {
                st.executeUpdate("UPDATE meja SET status = 'kosong' WHERE id = (SELECT meja_id FROM pesanan WHERE id = " + idPesanan + ")");
            }
            System.out.println("Status pesanan " + idPesanan + " diubah menjadi " + statusBaru);
            st.close();
        } catch (SQLException e) {
            System.out.println("Error update status: " + e.getMessage());
        }
    }
    
    public static int getTotal(int idPesanan) {
        String sql = "SELECT total_harga FROM pesanan WHERE id = " + idPesanan;
        try {
            Statement st = Database.getConn().createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                int total = rs.getInt("total_harga");
                rs.close();
                st.close();
                return total;
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error ambil total: " + e.getMessage());
        }
        return 0;
    }
    
    public static String getNamaPelanggan(int idPesanan) {
        String sql = "SELECT nama_pelanggan FROM pesanan WHERE id = " + idPesanan;
        try {
            Statement st = Database.getConn().createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                String nama = rs.getString("nama_pelanggan");
                rs.close();
                st.close();
                return nama;
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error ambil nama: " + e.getMessage());
        }
        return "-";
    }
    
    public static int getIdMeja(int idPesanan) {
        String sql = "SELECT meja_id FROM pesanan WHERE id = " + idPesanan;
        try {
            Statement st = Database.getConn().createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                int meja = rs.getInt("meja_id");
                rs.close();
                st.close();
                return meja;
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error ambil meja: " + e.getMessage());
        }
        return 0;
    }
}