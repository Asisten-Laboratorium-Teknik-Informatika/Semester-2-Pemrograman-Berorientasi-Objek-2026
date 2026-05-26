package dao;
import koneksi.Koneksi;
import java.sql.*;

public class KurirDAO {
    private Connection conn;

    public KurirDAO() {
        this.conn = Koneksi.connect();
    }

    public void insert(String id, String nama, String jenisKendaraan) throws SQLException {
        String sql = "INSERT INTO kurir (id_kurir, nama_kurir, jenis_kendaraan) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, nama);
            ps.setString(3, jenisKendaraan);
            ps.executeUpdate();
        }
    }

    public void displayAll() throws SQLException {
        String sql = "SELECT * FROM kurir";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.printf("%-12s | %-25s | %-20s\n", "ID Kurir", "Nama Armada", "Kendaraan");
            System.out.println("-------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-12s | %-25s | %-20s\n", 
                    rs.getString("id_kurir"), rs.getString("nama_kurir"), rs.getString("jenis_kendaraan"));
            }
        }
    }

    public void update(String id, String namaBaru, String kendaraanBaru) throws SQLException {
        String sql = "UPDATE kurir SET nama_kurir = ?, jenis_kendaraan = ? WHERE id_kurir = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, namaBaru);
            ps.setString(2, kendaraanBaru);
            ps.setString(3, id);
            if (ps.executeUpdate() > 0) {
                System.out.println(">> Sukses: Data armada kurir berhasil diperbarui!");
            } else {
                System.out.println(">> Gagal: ID Kurir tidak ditemukan.");
            }
        }
    }

    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM kurir WHERE id_kurir = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            if (ps.executeUpdate() > 0) {
                System.out.println(">> Sukses: Agen kurir berhasil dihapus!");
            } else {
                System.out.println(">> Gagal: ID Kurir tidak ditemukan.");
            }
        }
    }
}