package com.penjualan.dao;

import com.penjualan.entity.Supplier;
import com.penjualan.utils.Koneksi;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {
    
    public void insert(Supplier supplier) {
        String sql = "INSERT INTO supplier (nama_supplier, alamat, telepon, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, supplier.getNamaSupplier());
            stmt.setString(2, supplier.getAlamat());
            stmt.setString(3, supplier.getTelepon());
            stmt.setString(4, supplier.getEmail());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) supplier.setId(rs.getInt(1));
            System.out.println("✅ Supplier ditambahkan!");
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
    
    public List<Supplier> getAll() {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT * FROM supplier ORDER BY id";
        try (Connection conn = Koneksi.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Supplier(rs.getInt("id"), rs.getString("nama_supplier"), rs.getString("alamat"), rs.getString("telepon"), rs.getString("email")));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
        return list;
    }
    
    public Supplier findById(int id) {
        String sql = "SELECT * FROM supplier WHERE id=?";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Supplier(rs.getInt("id"), rs.getString("nama_supplier"), rs.getString("alamat"), rs.getString("telepon"), rs.getString("email"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
        return null;
    }
    
    public void update(Supplier supplier) {
        String sql = "UPDATE supplier SET nama_supplier=?, alamat=?, telepon=?, email=? WHERE id=?";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, supplier.getNamaSupplier());
            stmt.setString(2, supplier.getAlamat());
            stmt.setString(3, supplier.getTelepon());
            stmt.setString(4, supplier.getEmail());
            stmt.setInt(5, supplier.getId());
            stmt.executeUpdate();
            System.out.println("✅ Supplier diupdate!");
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
    
    public void delete(int id) {
        String sql = "DELETE FROM supplier WHERE id=?";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("✅ Supplier dihapus!");
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
}