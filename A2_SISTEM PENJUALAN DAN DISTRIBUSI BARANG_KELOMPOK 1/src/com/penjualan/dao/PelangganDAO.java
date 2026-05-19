package com.penjualan.dao;

import com.penjualan.entity.Pelanggan;
import com.penjualan.utils.Koneksi;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PelangganDAO {
    
    public void insert(Pelanggan pelanggan) {
        String sql = "INSERT INTO pelanggan (nama, alamat, telepon, email, poin) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, pelanggan.getNama());
            stmt.setString(2, pelanggan.getAlamat());
            stmt.setString(3, pelanggan.getTelepon());
            stmt.setString(4, pelanggan.getEmail());
            stmt.setInt(5, pelanggan.getPoin());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) pelanggan.setId(rs.getInt(1));
            System.out.println("✅ Pelanggan ditambahkan!");
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
    
    public List<Pelanggan> getAll() {
        List<Pelanggan> list = new ArrayList<>();
        String sql = "SELECT * FROM pelanggan ORDER BY id";
        try (Connection conn = Koneksi.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Pelanggan(rs.getInt("id"), rs.getString("nama"), rs.getString("alamat"), rs.getString("telepon"), rs.getString("email"), rs.getInt("poin")));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
        return list;
    }
    
    public Pelanggan findById(int id) {
        String sql = "SELECT * FROM pelanggan WHERE id=?";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Pelanggan(rs.getInt("id"), rs.getString("nama"), rs.getString("alamat"), rs.getString("telepon"), rs.getString("email"), rs.getInt("poin"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
        return null;
    }
    
    public void update(Pelanggan pelanggan) {
        String sql = "UPDATE pelanggan SET nama=?, alamat=?, telepon=?, email=?, poin=? WHERE id=?";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pelanggan.getNama());
            stmt.setString(2, pelanggan.getAlamat());
            stmt.setString(3, pelanggan.getTelepon());
            stmt.setString(4, pelanggan.getEmail());
            stmt.setInt(5, pelanggan.getPoin());
            stmt.setInt(6, pelanggan.getId());
            stmt.executeUpdate();
            System.out.println("✅ Pelanggan diupdate!");
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
    
    public void delete(int id) {
        String sql = "DELETE FROM pelanggan WHERE id=?";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("✅ Pelanggan dihapus!");
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
}