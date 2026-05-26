package dao;
import koneksi.Koneksi;
import model.Pelanggan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PelangganDAO {
    private Connection conn;

    public PelangganDAO() {
        this.conn = Koneksi.connect();
    }

    public void insert(Pelanggan pelanggan) throws SQLException {
        String sql = "INSERT INTO pelanggan (id_pelanggan, nama, email, telepon) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pelanggan.getIdPelanggan());
            ps.setString(2, pelanggan.getNama());
            ps.setString(3, pelanggan.getEmail());
            ps.setString(4, pelanggan.getTelepon());
            ps.executeUpdate();
        }
    }

    public List<Pelanggan> getAll() throws SQLException {
        List<Pelanggan> list = new ArrayList<>();
        String sql = "SELECT * FROM pelanggan";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Pelanggan p = new Pelanggan(
                    rs.getString("id_pelanggan"),
                    rs.getString("nama"),
                    rs.getString("email"),
                    rs.getString("telepon")
                );
                list.add(p);
            }
        }
        return list;
    }

    public void update(String id, String namaBaru, String emailBaru, String telpBaru) throws SQLException {
        String sql = "UPDATE pelanggan SET nama = ?, email = ?, telepon = ? WHERE id_pelanggan = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, namaBaru);
            ps.setString(2, emailBaru);
            ps.setString(3, telpBaru);
            ps.setString(4, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println(">> Peringatan: ID Pelanggan tidak ditemukan, data gagal diubah.");
            } else {
                System.out.println(">> Sukses: Data pelanggan berhasil diperbarui!");
            }
        }
    }

    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM pelanggan WHERE id_pelanggan = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println(">> Peringatan: ID Pelanggan tidak ditemukan, gagal dihapus.");
            } else {
                System.out.println(">> Sukses: Data pelanggan berhasil dihapus dari database!");
            }
        }
    }
}