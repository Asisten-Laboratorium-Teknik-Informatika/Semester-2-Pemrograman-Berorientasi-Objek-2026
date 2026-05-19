package com.penjualan.dao;

import com.penjualan.entity.Pengiriman;
import com.penjualan.entity.Transaksi;
import com.penjualan.utils.Koneksi;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PengirimanDAO {
    
    public void insert(Pengiriman pengiriman) {
        String sql = "INSERT INTO pengiriman (id_transaksi, kurir, no_resi, tgl_kirim, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, pengiriman.getTransaksi().getId());
            stmt.setString(2, pengiriman.getKurir());
            stmt.setString(3, pengiriman.getNoResi());
            stmt.setDate(4, Date.valueOf(pengiriman.getTglKirim()));
            stmt.setString(5, pengiriman.getStatus());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                pengiriman.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            System.err.println("Error insert pengiriman: " + e.getMessage());
        }
    }
    
    public List<Pengiriman> getAll() {
        List<Pengiriman> list = new ArrayList<>();
        String sql = "SELECT * FROM pengiriman ORDER BY id DESC";
        try (Connection conn = Koneksi.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Transaksi transaksi = new Transaksi();
                transaksi.setId(rs.getInt("id_transaksi"));
                Pengiriman p = new Pengiriman();
                p.setId(rs.getInt("id"));
                p.setTransaksi(transaksi);
                p.setKurir(rs.getString("kurir"));
                p.setNoResi(rs.getString("no_resi"));
                p.setTglKirim(rs.getDate("tgl_kirim").toLocalDate());
                if (rs.getDate("tgl_terima") != null) {
                    p.setTglTerima(rs.getDate("tgl_terima").toLocalDate());
                }
                p.setStatus(rs.getString("status"));
                list.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error get all pengiriman: " + e.getMessage());
        }
        return list;
    }
    
    public Pengiriman findById(int id) {
        String sql = "SELECT * FROM pengiriman WHERE id=?";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Transaksi transaksi = new Transaksi();
                transaksi.setId(rs.getInt("id_transaksi"));
                Pengiriman p = new Pengiriman();
                p.setId(rs.getInt("id"));
                p.setTransaksi(transaksi);
                p.setKurir(rs.getString("kurir"));
                p.setNoResi(rs.getString("no_resi"));
                p.setTglKirim(rs.getDate("tgl_kirim").toLocalDate());
                if (rs.getDate("tgl_terima") != null) {
                    p.setTglTerima(rs.getDate("tgl_terima").toLocalDate());
                }
                p.setStatus(rs.getString("status"));
                return p;
            }
        } catch (SQLException e) {
            System.err.println("Error find pengiriman by id: " + e.getMessage());
        }
        return null;
    }
    
    public Pengiriman findByTransaksiId(int idTransaksi) {
        String sql = "SELECT * FROM pengiriman WHERE id_transaksi=?";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTransaksi);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Transaksi transaksi = new Transaksi();
                transaksi.setId(idTransaksi);
                Pengiriman p = new Pengiriman();
                p.setId(rs.getInt("id"));
                p.setTransaksi(transaksi);
                p.setKurir(rs.getString("kurir"));
                p.setNoResi(rs.getString("no_resi"));
                p.setTglKirim(rs.getDate("tgl_kirim").toLocalDate());
                if (rs.getDate("tgl_terima") != null) {
                    p.setTglTerima(rs.getDate("tgl_terima").toLocalDate());
                }
                p.setStatus(rs.getString("status"));
                return p;
            }
        } catch (SQLException e) {
            System.err.println("Error find pengiriman by transaksi: " + e.getMessage());
        }
        return null;
    }
    
    public void update(Pengiriman pengiriman) {
        String sql = "UPDATE pengiriman SET kurir=?, no_resi=?, tgl_kirim=?, tgl_terima=?, status=?, updated_at=CURRENT_TIMESTAMP WHERE id=?";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pengiriman.getKurir());
            stmt.setString(2, pengiriman.getNoResi());
            stmt.setDate(3, Date.valueOf(pengiriman.getTglKirim()));
            if (pengiriman.getTglTerima() != null) {
                stmt.setDate(4, Date.valueOf(pengiriman.getTglTerima()));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            stmt.setString(5, pengiriman.getStatus());
            stmt.setInt(6, pengiriman.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error update pengiriman: " + e.getMessage());
        }
    }
    
    public void konfirmasiTerima(int id) {
        String sql = "UPDATE pengiriman SET tgl_terima=CURRENT_DATE, status='DITERIMA', updated_at=CURRENT_TIMESTAMP WHERE id=?";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error konfirmasi terima: " + e.getMessage());
        }
    }
    
    public void delete(int id) {
        String sql = "DELETE FROM pengiriman WHERE id=?";
        try (Connection conn = Koneksi.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error delete pengiriman: " + e.getMessage());
        }
    }
}
