package dao;
import koneksi.Koneksi;
import model.Pengiriman;
import java.sql.*;

public class PengirimanDAO {
    private Connection conn;

    public PengirimanDAO() {
        this.conn = Koneksi.connect();
    }

    public void insert(Pengiriman pengiriman) throws SQLException {
        String sql = "INSERT INTO pengiriman (id_pengiriman, id_penjualan, id_kurir, tanggal_kirim, status_pengiriman) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pengiriman.getIdPengiriman());
            ps.setString(2, pengiriman.getIdPenjualan());
            ps.setString(3, pengiriman.getIdKurir());
            ps.setDate(4, pengiriman.getTanggalKirim());
            ps.setString(5, pengiriman.getStatusPengiriman());
            ps.executeUpdate();
        }
    }

    public void updateStatus(String idPengiriman, String statusBaru) throws SQLException {
        String sql = "UPDATE pengiriman SET status_pengiriman = ? WHERE id_pengiriman = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statusBaru);
            ps.setString(2, idPengiriman);
            ps.executeUpdate();
        }
    } 
    
    public void tampilkanLaporanLogistikLengkap() throws SQLException {
        String sql = "SELECT pg.id_pengiriman, p.id_penjualan, pl.nama AS nama_pelanggan, " +
                    "k.nama_kurir, pg.tanggal_kirim, pg.status_pengiriman " +
                    "FROM pengiriman pg " +
                    "JOIN penjualan p ON pg.id_penjualan = p.id_penjualan " +
                    "JOIN pelanggan pl ON p.id_pelanggan = pl.id_pelanggan " +
                    "JOIN kurir k ON pg.id_kurir = k.id_kurir";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n=========================================================================================");
            System.out.println("                           LAPORAN OPERASIONAL LOGISTIK & DISTRIBUSI                     ");
            System.out.println("=========================================================================================");
            System.out.printf("%-10s | %-10s | %-20s | %-15s | %-12s | %-10s\n", 
                "ID KIRIM", "ID PENJ", "NAMA PELANGGAN", "KURIR VENDOR", "TGL KIRIM", "STATUS");
            System.out.println("-----------------------------------------------------------------------------------------");
            
            boolean adaData = false;
            while (rs.next()) {
                adaData = true;
                System.out.printf("%-10s | %-10s | %-20s | %-15s | %-12s | %-10s\n",
                    rs.getString("id_pengiriman"),
                    rs.getString("id_penjualan"),
                    rs.getString("nama_pelanggan"),
                    rs.getString("nama_kurir"),
                    rs.getDate("tanggal_kirim").toString(),
                    rs.getString("status_pengiriman")
                );
            }
            
            if (!adaData) {
                System.out.println("Belum ada pengiriman barang yang tercatat.");
            }
            System.out.println("=========================================================================================");
        }
    }
}
