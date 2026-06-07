package dao;

import model.Produk;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdukDAO {
    private Connection connection;

    public ProdukDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Produk> getAllProduk() {
        List<Produk> listProduk = new ArrayList<>();
        String query = "SELECT * FROM produk WHERE stok > 0";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Produk p = new Produk(
                    rs.getInt("id_produk"),
                    rs.getString("nama_produk"),
                    rs.getDouble("harga"),
                    rs.getInt("stok"),
                    rs.getInt("id_kategori")
                );
                listProduk.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error dalam mengambil produk: " + e.getMessage());
        }
        return listProduk;
    }

    public boolean tambahProduk(Produk produk) {
        String query = "INSERT INTO produk (nama_produk, harga, stok, id_kategori) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, produk.getNamaProduk()); // Baris ini aman pakai setString lek!
            ps.setDouble(2, produk.getHarga());
            ps.setInt(3, produk.getStok());
            ps.setInt(4, produk.getIdKategori());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean updateStok(int idProduk, int stokBaru) {
        String query = "UPDATE produk SET stok = ? WHERE id_produk = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, stokBaru);
            ps.setInt(2, idProduk);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}