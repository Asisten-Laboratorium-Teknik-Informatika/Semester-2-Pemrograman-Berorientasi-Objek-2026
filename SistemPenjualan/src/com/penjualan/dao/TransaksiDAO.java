package com.penjualan.dao;

import com.penjualan.entity.Transaksi;
import com.penjualan.entity.Pelanggan;
import com.penjualan.entity.User;
import com.penjualan.utils.Koneksi;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransaksiDAO {
    
    public void insert(Transaksi transaksi) {
        String sql = "INSERT INTO transaksi (id_pelanggan, id_user, tgl_transaksi, total_harga, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (transaksi.getPelanggan() != null) {
                stmt.setInt(1, transaksi.getPelanggan().getId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setInt(2, transaksi.getUser().getId());
            stmt.setDate(3, Date.valueOf(transaksi.getTglTransaksi()));
            stmt.setDouble(4, transaksi.getTotalHarga());
            stmt.setString(5, transaksi.getStatus());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                transaksi.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            System.err.println("Error insert transaksi: " + e.getMessage());
        }
    }
    
    public List<Transaksi> getAll() {
        List<Transaksi> list = new ArrayList<>();
        String sql = "SELECT t.*, p.nama as nama_pelanggan, u.username FROM transaksi t LEFT JOIN pelanggan p ON t.id_pelanggan = p.id LEFT JOIN \"user\" u ON t.id_user = u.id ORDER BY t.id DESC";
        try (Connection conn = Koneksi.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Pelanggan pelanggan = null;
                if (rs.getObject("id_pelanggan") != null) {
                    pelanggan = new Pelanggan();
                    pelanggan.setId(rs.getInt("id_pelanggan"));
                    pelanggan.setNama(rs.getString("nama_pelanggan"));
                }
                User user = new User();
                user.setId(rs.getInt("id_user"));
                user.setUsername(rs.getString("username"));
                Transaksi t = new Transaksi();
                t.setId(rs.getInt("id"));
                t.setPelanggan(pelanggan);
                t.setUser(user);
                t.setTglTransaksi(rs.getDate("tgl_transaksi").toLocalDate());
                t.setTotalHarga(rs.getDouble("total_harga"));
                t.setStatus(rs.getString("status"));
                list.add(t);
            }
        } catch (SQLException e) {
            System.err.println("Error get all transaksi: " + e.getMessage());
        }
        return list;
    }
    
    public Transaksi findById(int id) {
        String sql = "SELECT t.*, p.nama as nama_pelanggan, u.username FROM transaksi t LEFT JOIN pelanggan p ON t.id_pelanggan = p.id LEFT JOIN \"user\" u ON t.id_user = u.id WHERE t.id=?";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Pelanggan pelanggan = null;
                if (rs.getObject("id_pelanggan") != null) {
                    pelanggan = new Pelanggan();
                    pelanggan.setId(rs.getInt("id_pelanggan"));
                    pelanggan.setNama(rs.getString("nama_pelanggan"));
                }
                User user = new User();
                user.setId(rs.getInt("id_user"));
                user.setUsername(rs.getString("username"));
                Transaksi t = new Transaksi();
                t.setId(rs.getInt("id"));
                t.setPelanggan(pelanggan);
                t.setUser(user);
                t.setTglTransaksi(rs.getDate("tgl_transaksi").toLocalDate());
                t.setTotalHarga(rs.getDouble("total_harga"));
                t.setStatus(rs.getString("status"));
                return t;
            }
        } catch (SQLException e) {
            System.err.println("Error find transaksi: " + e.getMessage());
        }
        return null;
    }
    
    public void update(Transaksi transaksi) {
        String sql = "UPDATE transaksi SET id_pelanggan=?, id_user=?, tgl_transaksi=?, total_harga=?, status=? WHERE id=?";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (transaksi.getPelanggan() != null) {
                stmt.setInt(1, transaksi.getPelanggan().getId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setInt(2, transaksi.getUser().getId());
            stmt.setDate(3, Date.valueOf(transaksi.getTglTransaksi()));
            stmt.setDouble(4, transaksi.getTotalHarga());
            stmt.setString(5, transaksi.getStatus());
            stmt.setInt(6, transaksi.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error update transaksi: " + e.getMessage());
        }
    }
    
    public void updateStatus(int id, String status) {
        String sql = "UPDATE transaksi SET status=? WHERE id=?";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error update status: " + e.getMessage());
        }
    }
    
    public void delete(int id) {
        String sql = "DELETE FROM transaksi WHERE id=?";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error delete transaksi: " + e.getMessage());
        }
    }
}
