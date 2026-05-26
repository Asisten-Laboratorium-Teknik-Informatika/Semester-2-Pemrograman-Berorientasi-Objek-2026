package dao;
import koneksi.Koneksi;
import java.sql.*;

public class PasokanDAO {
    private Connection conn;

    public PasokanDAO() {
        this.conn = Koneksi.connect();
    }

    public void insertPasokan(String idPasokan, String idProduk, String idSupplier, int jumlah) throws SQLException {
        String sql = "INSERT INTO pasokan (id_pasokan, id_produk, id_supplier, tanggal_pasok, jumlah_masuk) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idPasokan);
            ps.setString(2, idProduk);
            ps.setString(3, idSupplier);
            ps.setDate(4, new Date(System.currentTimeMillis()));
            ps.setInt(5, jumlah);
            ps.executeUpdate();
        }
    }
}