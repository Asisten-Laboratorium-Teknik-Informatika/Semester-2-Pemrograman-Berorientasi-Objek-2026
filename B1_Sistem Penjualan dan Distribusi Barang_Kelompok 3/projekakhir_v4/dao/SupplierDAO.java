package dao;
import koneksi.Koneksi;
import java.sql.*;

public class SupplierDAO {
    private Connection conn;

    public SupplierDAO() {
        this.conn = Koneksi.connect();
    }

    public void insert(String id, String nama, String alamat) throws SQLException {
        String sql = "INSERT INTO supplier (id_supplier, nama_supplier, alamat) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, nama);
            ps.setString(3, alamat);
            ps.executeUpdate();
        }
    }

    public void displayAll() throws SQLException {
        String sql = "SELECT * FROM supplier";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.printf("%-12s | %-25s | %-30s\n", "ID Supplier", "Nama Supplier", "Alamat");
            System.out.println("----------------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-12s | %-25s | %-30s\n", 
                    rs.getString("id_supplier"), rs.getString("nama_supplier"), rs.getString("alamat"));
            }
        }
    }

    public void update(String id, String namaBaru, String alamatBaru) throws SQLException {
        String sql = "UPDATE supplier SET nama_supplier = ?, alamat = ? WHERE id_supplier = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, namaBaru);
            ps.setString(2, alamatBaru);
            ps.setString(3, id);
            if (ps.executeUpdate() > 0) {
                System.out.println(">> Sukses: Data supplier berhasil diperbarui!");
            } else {
                System.out.println(">> Gagal: ID Supplier tidak ditemukan.");
            }
        }
    }

    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM supplier WHERE id_supplier = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            if (ps.executeUpdate() > 0) {
                System.out.println(">> Sukses: Data supplier berhasil dihapus!");
            } else {
                System.out.println(">> Gagal: ID Supplier tidak ditemukan.");
            }
        }
    }
}