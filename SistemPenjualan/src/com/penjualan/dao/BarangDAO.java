package com.penjualan.dao;

import com.penjualan.entity.Barang;
import com.penjualan.entity.Kategori;
import com.penjualan.utils.Koneksi;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BarangDAO {
    
    public void insert(Barang barang) {
        String sql = "INSERT INTO barang (nama_barang, harga, stok, id_kategori) VALUES (?, ?, ?, ?)";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, barang.getNamaBarang());
            stmt.setDouble(2, barang.getHarga());
            stmt.setInt(3, barang.getStok());
            if (barang.getKategori() != null) {
                stmt.setInt(4, barang.getKategori().getId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) barang.setId(rs.getInt(1));
            System.out.println("✅ Barang ditambahkan!");
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
    
    public List<Barang> getAll() {
        List<Barang> list = new ArrayList<>();
        String sql = "SELECT b.*, k.nama_kategori FROM barang b LEFT JOIN kategori k ON b.id_kategori = k.id ORDER BY b.id";
        try (Connection conn = Koneksi.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Kategori kategori = null;
                if (rs.getObject("id_kategori") != null) {
                    kategori = new Kategori(rs.getInt("id_kategori"), rs.getString("nama_kategori"), null);
                }
                Barang b = new Barang(rs.getInt("id"), rs.getString("nama_barang"), rs.getDouble("harga"), rs.getInt("stok"), kategori);
                list.add(b);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
        return list;
    }
    
    public Barang findById(int id) {
        String sql = "SELECT b.*, k.nama_kategori FROM barang b LEFT JOIN kategori k ON b.id_kategori = k.id WHERE b.id=?";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Kategori kategori = null;
                if (rs.getObject("id_kategori") != null) {
                    kategori = new Kategori(rs.getInt("id_kategori"), rs.getString("nama_kategori"), null);
                }
                return new Barang(rs.getInt("id"), rs.getString("nama_barang"), rs.getDouble("harga"), rs.getInt("stok"), kategori);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
        return null;
    }
    
    public void update(Barang barang) {
        String sql = "UPDATE barang SET nama_barang=?, harga=?, stok=?, id_kategori=? WHERE id=?";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, barang.getNamaBarang());
            stmt.setDouble(2, barang.getHarga());
            stmt.setInt(3, barang.getStok());
            if (barang.getKategori() != null) {
                stmt.setInt(4, barang.getKategori().getId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.setInt(5, barang.getId());
            stmt.executeUpdate();
            System.out.println("✅ Barang diupdate!");
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
    
    public void delete(int id) {
        String sql = "DELETE FROM barang WHERE id=?";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("✅ Barang dihapus!");
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
}