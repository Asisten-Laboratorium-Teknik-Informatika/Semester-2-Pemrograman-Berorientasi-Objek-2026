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

    public void cetakNotaTransaksi(String idPenjualan) throws SQLException {
        String sqlHeader = "SELECT p.id_penjualan, p.tanggal_penjualan, p.total_bayar, " +
                        "pl.nama AS nama_pelanggan, pl.id_pelanggan, k.nama AS nama_kasir, k.id_karyawan " +
                        "FROM penjualan p " +
                        "LEFT JOIN pelanggan pl ON p.id_pelanggan = pl.id_pelanggan " +
                        "LEFT JOIN karyawan k ON p.id_karyawan = k.id_karyawan " +
                        "WHERE p.id_penjualan = ?";

        String sqlItems = "SELECT dp.jumlah, dp.subtotal, pr.nama_produk, pr.harga " +
                        "FROM detail_penjualan dp " +
                        "JOIN produk pr ON dp.id_produk = pr.id_produk " +
                        "WHERE dp.id_penjualan = ?";

        try (PreparedStatement psHeader = conn.prepareStatement(sqlHeader)) {
            psHeader.setString(1, idPenjualan);
            try (ResultSet rsH = psHeader.executeQuery()) {
                if (rsH.next()) {
                    System.out.println("\n==================================================");
                    System.out.println("                  InforEn Fashion                 ");
                    System.out.println("==================================================");
                    System.out.printf("ID Nota   : %s\n", rsH.getString("id_penjualan"));
                    System.out.printf("Tanggal   : %s\n", rsH.getTimestamp("tanggal_penjualan").toString());
                    System.out.printf("Pelanggan : %s (%s)\n", rsH.getString("nama_pelanggan"), rsH.getString("id_pelanggan"));
                    System.out.printf("Kasir     : %s (%s)\n", rsH.getString("nama_kasir"), rsH.getString("id_karyawan"));
                    System.out.println("--------------------------------------------------");
                    System.out.printf("%-20s %-5s %-10s %-12s\n", "Produk", "Qty", "Harga", "Subtotal");
                    System.out.println("--------------------------------------------------");

                    try (PreparedStatement psItems = conn.prepareStatement(sqlItems)) {
                        psItems.setString(1, idPenjualan);
                        try (ResultSet rsI = psItems.executeQuery()) {
                            while (rsI.next()) {
                                System.out.printf("%-20s %-5d Rp%-9,.0f Rp%,.0f\n", 
                                    rsI.getString("nama_produk"),
                                    rsI.getInt("jumlah"),
                                    rsI.getDouble("harga"),
                                    rsI.getDouble("subtotal")
                                );
                            }
                        }
                    }
                    
                    System.out.println("--------------------------------------------------");
                    System.out.printf("TOTAL BELANJA                       : Rp%,.2f\n", rsH.getDouble("total_bayar"));
                    System.out.println("==================================================");
                    System.out.println("         -- Terima Kasih Telah Berbelanja --\n");
                    
                } else {
                    System.out.println(">> Error: ID Transaksi " + idPenjualan + " tidak ditemukan di database.");
                }
            }
        }
    }
}
