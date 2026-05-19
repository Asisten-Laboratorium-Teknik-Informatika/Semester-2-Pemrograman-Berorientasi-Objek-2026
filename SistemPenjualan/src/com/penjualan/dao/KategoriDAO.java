package com.penjualan.dao;

import com.penjualan.entity.Kategori;
import com.penjualan.utils.Koneksi;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KategoriDAO {
    
    public void insert(Kategori kategori) {
        String sql = "INSERT INTO kategori (nama_kategori, deskripsi) VALUES (?, ?)";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, kategori.getNamaKategori());
            stmt.setString(2, kategori.getDeskripsi());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) kategori.setId(rs.getInt(1));
            System.out.println("✅ Kategori ditambahkan!");
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
    
    public List<Kategori> getAll() {
        List<Kategori> list = new ArrayList<>();
        String sql = "SELECT * FROM kategori ORDER BY id";
        try (Connection conn = Koneksi.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Kategori(rs.getInt("id"), rs.getString("nama_kategori"), rs.getString("deskripsi")));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
        return list;
    }
    
    public Kategori findById(int id) {
        String sql = "SELECT * FROM kategori WHERE id=?";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Kategori(rs.getInt("id"), rs.getString("nama_kategori"), rs.getString("deskripsi"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
        return null;
    }
    
    public void update(Kategori kategori) {
        String sql = "UPDATE kategori SET nama_kategori=?, deskripsi=? WHERE id=?";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kategori.getNamaKategori());
            stmt.setString(2, kategori.getDeskripsi());
            stmt.setInt(3, kategori.getId());
            stmt.executeUpdate();
            System.out.println("✅ Kategori diupdate!");
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
    
    public void delete(int id) {
        String sql = "DELETE FROM kategori WHERE id=?";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("✅ Kategori dihapus!");
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
}