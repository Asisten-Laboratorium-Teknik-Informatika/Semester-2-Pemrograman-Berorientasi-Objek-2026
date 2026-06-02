package dao;
import koneksi.Koneksi;
import model.Produk;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdukDAO {
    private Connection conn;

    public ProdukDAO() {
        this.conn = Koneksi.connect();
    }

    public void insert(Produk produk) throws SQLException {
        String sql = "INSERT INTO produk (id_produk, nama_produk, id_kategori, harga, stok) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, produk.getIdProduk());
            ps.setString(2, produk.getNamaProduk());
            ps.setString(3, produk.getIdKategori());
            ps.setDouble(4, produk.getHarga());
            ps.setInt(5, produk.getStok());
            ps.executeUpdate();
        }
    }

    public List<Produk> getAll() throws SQLException {
        List<Produk> list = new ArrayList<>();
        String sql = "SELECT * FROM produk";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Produk p = new Produk(
                    rs.getString("id_produk"),
                    rs.getString("nama_produk"),
                    rs.getString("id_kategori"),
                    rs.getDouble("harga"),
                    rs.getInt("stok")
                );
                list.add(p);
            }
        }
        return list;
    }

    public void update(Produk produk) throws SQLException {
        String sql = "UPDATE produk SET nama_produk = ?, id_kategori = ?, harga = ?, stok = ? WHERE id_produk = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, produk.getNamaProduk());
            ps.setString(2, produk.getIdKategori());
            ps.setDouble(3, produk.getHarga());
            ps.setInt(4, produk.getStok());
            ps.setString(5, produk.getIdProduk());
            ps.executeUpdate();
        }
    }

    public void delete(String idProduk) throws SQLException {
        String sql = "DELETE FROM produk WHERE id_produk = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idProduk);
            ps.executeUpdate();
        }
    }

    public List<Produk> getStokMenipis() throws SQLException {
        List<Produk> list = new ArrayList<>();
        String sql = "SELECT * FROM produk WHERE stok < 5";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Produk p = new Produk(
                    rs.getString("id_produk"),
                    rs.getString("nama_produk"),
                    rs.getString("id_kategori"),
                    rs.getDouble("harga"),
                    rs.getInt("stok")
                );
                list.add(p);
            }
        }
        return list;
    }

    public void tambahStok(String idProduk, int jumlahMasuk) throws SQLException {
        String sql = "UPDATE produk SET stok = stok + ? WHERE id_produk = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, jumlahMasuk);
            ps.setString(2, idProduk);
            ps.executeUpdate();
        }
    }

    public void tampilkanProdukPremium() throws SQLException {
        String sql = "SELECT id_produk, nama_produk, harga FROM produk " +
                    "WHERE harga > (SELECT AVG(harga) FROM produk)";
                    
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n==================================================");
            System.out.println("    DAFTAR PRODUK PREMIUM (DI ATAS RATA-RATA)   ");
            System.out.println("==================================================");
            
            boolean adaData = false;
            while (rs.next()) {
                adaData = true;
                System.out.printf("- [%s] %-25s : Rp%,.2f\n", 
                    rs.getString("id_produk"), 
                    rs.getString("nama_produk"), 
                    rs.getDouble("harga")
                );
            }
            
            if (!adaData) {
                System.out.println("Belum ada data produk di atas rata-rata.");
            }
            System.out.println("==================================================");
        }
    }
}
