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
}