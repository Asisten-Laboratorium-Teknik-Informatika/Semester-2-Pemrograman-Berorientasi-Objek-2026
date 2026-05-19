package com.penjualan.dao;

import com.penjualan.entity.DetailTransaksi;
import com.penjualan.entity.Barang;
import com.penjualan.entity.Transaksi;
import com.penjualan.utils.Koneksi;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetailTransaksiDAO {
    
    public void insert(DetailTransaksi detail) {
        String sql = "INSERT INTO detail_transaksi (id_transaksi, id_barang, jumlah, harga_satuan, subtotal) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, detail.getTransaksi().getId());
            stmt.setInt(2, detail.getBarang().getId());
            stmt.setInt(3, detail.getJumlah());
            stmt.setDouble(4, detail.getHargaSatuan());
            stmt.setDouble(5, detail.getSubtotal());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) detail.setId(rs.getInt(1));
            System.out.println("✅ Detail transaksi ditambahkan!");
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
    
    public List<DetailTransaksi> getByTransaksiId(int idTransaksi) {
        List<DetailTransaksi> list = new ArrayList<>();
        String sql = "SELECT d.*, b.nama_barang, b.harga FROM detail_transaksi d JOIN barang b ON d.id_barang = b.id WHERE d.id_transaksi=?";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTransaksi);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Transaksi transaksi = new Transaksi();
                transaksi.setId(idTransaksi);
                Barang barang = new Barang(rs.getInt("id_barang"), rs.getString("nama_barang"), rs.getDouble("harga"), 0);
                DetailTransaksi d = new DetailTransaksi(rs.getInt("id"), transaksi, barang, rs.getInt("jumlah"), rs.getDouble("harga_satuan"), rs.getDouble("subtotal"));
                list.add(d);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
        return list;
    }
    
    public void delete(int id) {
        String sql = "DELETE FROM detail_transaksi WHERE id=?";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("✅ Detail transaksi dihapus!");
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
    
    public void deleteByTransaksiId(int idTransaksi) {
        String sql = "DELETE FROM detail_transaksi WHERE id_transaksi=?";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTransaksi);
            stmt.executeUpdate();
            System.out.println("✅ Semua detail transaksi dihapus!");
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
    
    public double getTotalSubtotalByTransaksiId(int idTransaksi) {
        String sql = "SELECT COALESCE(SUM(subtotal), 0) as total FROM detail_transaksi WHERE id_transaksi=?";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTransaksi);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
        return 0;
    }
}