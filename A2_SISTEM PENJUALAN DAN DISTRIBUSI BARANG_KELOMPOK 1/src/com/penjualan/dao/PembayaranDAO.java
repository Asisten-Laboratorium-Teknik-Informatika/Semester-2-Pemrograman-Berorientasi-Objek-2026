package com.penjualan.dao;

import com.penjualan.entity.Pembayaran;
import com.penjualan.entity.Transaksi;
import com.penjualan.utils.Koneksi;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PembayaranDAO {
    
    public void insert(Pembayaran pembayaran) {
        String sql = "INSERT INTO pembayaran (id_transaksi, metode, jumlah_dibayar, kembalian, tgl_pembayaran) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, pembayaran.getTransaksi().getId());
            stmt.setString(2, pembayaran.getMetode());
            stmt.setDouble(3, pembayaran.getJumlahDibayar());
            stmt.setDouble(4, pembayaran.getKembalian());
            stmt.setDate(5, Date.valueOf(pembayaran.getTglPembayaran()));
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) pembayaran.setId(rs.getInt(1));
            System.out.println("✅ Pembayaran ditambahkan!");
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
    
    public List<Pembayaran> getAll() {
        List<Pembayaran> list = new ArrayList<>();
        String sql = "SELECT * FROM pembayaran ORDER BY id DESC";
        try (Connection conn = Koneksi.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Transaksi transaksi = new Transaksi();
                transaksi.setId(rs.getInt("id_transaksi"));
                Pembayaran p = new Pembayaran(rs.getInt("id"), transaksi, rs.getString("metode"), rs.getDouble("jumlah_dibayar"), rs.getDouble("kembalian"), rs.getDate("tgl_pembayaran").toLocalDate());
                list.add(p);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
        return list;
    }
    
    public Pembayaran findByTransaksiId(int idTransaksi) {
        String sql = "SELECT * FROM pembayaran WHERE id_transaksi=?";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTransaksi);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Transaksi transaksi = new Transaksi();
                transaksi.setId(idTransaksi);
                return new Pembayaran(rs.getInt("id"), transaksi, rs.getString("metode"), rs.getDouble("jumlah_dibayar"), rs.getDouble("kembalian"), rs.getDate("tgl_pembayaran").toLocalDate());
            }
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
        return null;
    }
    
    public void update(Pembayaran pembayaran) {
        String sql = "UPDATE pembayaran SET metode=?, jumlah_dibayar=?, kembalian=? WHERE id=?";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pembayaran.getMetode());
            stmt.setDouble(2, pembayaran.getJumlahDibayar());
            stmt.setDouble(3, pembayaran.getKembalian());
            stmt.setInt(4, pembayaran.getId());
            stmt.executeUpdate();
            System.out.println("✅ Pembayaran diupdate!");
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
    
    public void delete(int id) {
        String sql = "DELETE FROM pembayaran WHERE id=?";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("✅ Pembayaran dihapus!");
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
    
    public double getTotalPembayaranByDate(LocalDate tanggal) {
        String sql = "SELECT COALESCE(SUM(jumlah_dibayar), 0) as total FROM pembayaran WHERE tgl_pembayaran=?";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(tanggal));
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