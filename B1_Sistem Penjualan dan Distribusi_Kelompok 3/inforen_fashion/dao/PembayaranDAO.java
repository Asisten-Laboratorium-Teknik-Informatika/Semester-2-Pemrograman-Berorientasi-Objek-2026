package dao;

import model.*;
import java.sql.*;

public class PembayaranDAO {
    private Connection connection;

    public PembayaranDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean prosesPembayaran(Pembayaran pembayaran) {
        String queryPembayaran = "INSERT INTO pembayaran (id_pesanan, jumlah_bayar, metode_pembayaran, status_pembayaran) VALUES (?, ?, ?, ?)";
        String queryUpdatePesanan = "UPDATE pesanan SET status_pesanan = 'DIBAYAR' WHERE id_pesanan = ?";

        try {
            connection.setAutoCommit(false);

            PreparedStatement psBayar = connection.prepareStatement(queryPembayaran);
            psBayar.setInt(1, pembayaran.getIdPesanan());
            psBayar.setDouble(2, pembayaran.getJumlahBayar());
            psBayar.setString(3, pembayaran.getMetodePembayaran());
            psBayar.setString(4, pembayaran.getStatusPembayaran());
            psBayar.executeUpdate();

            if (pembayaran.getStatusPembayaran().equalsIgnoreCase("SUKSES")) {
                PreparedStatement psPesanan = connection.prepareStatement(queryUpdatePesanan);
                psPesanan.setInt(1, pembayaran.getIdPesanan());
                psPesanan.executeUpdate();
            }

            connection.commit();
            return true;

        } catch (SQLException e) {
            try {
                System.out.println("Pembayaran gagal, database di-rollback! Error: " + e.getMessage());
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void cetakNota(int idPesanan, double totalBayar, String metode) {
        System.out.println("\n=========================================");
        System.out.println("            INFOREN FASHION              ");
        System.out.println("        Medan, North Sumatra, ID         ");
        System.out.println("=========================================");
        System.out.println("ID Pesanan : #" + idPesanan);
        System.out.println("Tanggal    : " + new java.util.Date());
        System.out.println("Metode     : " + metode.toUpperCase());
        System.out.println("-----------------------------------------");
        System.out.printf("%-18s %-4s %-12s\n", "Nama Produk", "Qty", "Subtotal");
        System.out.println("-----------------------------------------");

        String query = "SELECT p.nama_produk, dp.jumlah, dp.subtotal " +
                       "FROM detail_pesanan dp " +
                       "JOIN produk p ON dp.id_produk = p.id_produk " +
                       "WHERE dp.id_pesanan = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idPesanan);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String namaProduk = rs.getString("nama_produk");
                    int qty = rs.getInt("jumlah");
                    double subtotal = rs.getDouble("subtotal");
                    
                    if (namaProduk.length() > 18) {
                        namaProduk = namaProduk.substring(0, 15) + "...";
                    }
                    
                    System.out.printf("%-18s %-4d Rp %-12.2f\n", namaProduk, qty, subtotal);
                }
            }
            System.out.println("-----------------------------------------");
            System.out.printf("%-23s : Rp %-12.2f\n", "TOTAL BAYAR", totalBayar);
            System.out.println("=========================================");
            System.out.println("Terima kasih sudah berbelanja di InfoRen Fasihon!");
            System.out.println("=========================================\n");
            
        } catch (SQLException e) {
            System.out.println("Gagal mencetak nota dari database: " + e.getMessage());
        }
    }
}