package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import koneksi.Koneksi;
import model.Penjualan;

public class PenjualanDAO {
    private Connection conn;

    public PenjualanDAO() {
        this.conn = Koneksi.connect();
    }

    public void insert(Penjualan penjualan) throws SQLException {
        String sql = "INSERT INTO penjualan (id_penjualan, tanggal_penjualan, id_pelanggan, id_karyawan, total_bayar) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, penjualan.getIdPenjualan());
            ps.setTimestamp(2, penjualan.getTanggalPenjualan());
            ps.setString(3, penjualan.getIdPelanggan());
            ps.setString(4, penjualan.getIdKaryawan());
            ps.setDouble(5, penjualan.getTotalBayar());
            ps.executeUpdate();
        }
    }

    public List<Penjualan> getAll() throws SQLException {
        List<Penjualan> list = new ArrayList<>();
        String sql = "SELECT * FROM penjualan";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Penjualan p = new Penjualan(
                    rs.getString("id_penjualan"),
                    rs.getTimestamp("tanggal_penjualan"),
                    rs.getString("id_pelanggan"),
                    rs.getString("id_karyawan"),
                    rs.getDouble("total_bayar")
                );
                list.add(p);
            }
        }
        return list;
    }

    public double getTotalPendapatan() throws SQLException {
        String sql = "SELECT SUM(total_bayar) AS total FROM penjualan";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
        }
        return 0.0;
    }
}