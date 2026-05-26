package dao;
import koneksi.Koneksi;
import java.sql.*;

public class KaryawanDAO {
    private Connection conn;

    public KaryawanDAO() {
        this.conn = Koneksi.connect();
    }

    public void insert(String id, String nama, String jabatan) throws SQLException {
        String sql = "INSERT INTO karyawan (id_karyawan, nama, jabatan) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, nama);
            ps.setString(3, jabatan);
            ps.executeUpdate();
        }
    }

    public void displayAll() throws SQLException {
        String sql = "SELECT * FROM karyawan";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.printf("%-12s | %-25s | %-15s\n", "ID Karyawan", "Nama Karyawan", "Jabatan");
            System.out.println("---------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-12s | %-25s | %-15s\n", 
                    rs.getString("id_karyawan"), rs.getString("nama"), rs.getString("jabatan"));
            }
        }
    }

    public void update(String id, String namaBaru, String jabatanBaru) throws SQLException {
        String sql = "UPDATE karyawan SET nama = ?, jabatan = ? WHERE id_karyawan = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, namaBaru);
            ps.setString(2, jabatanBaru);
            ps.setString(3, id);
            if (ps.executeUpdate() > 0) {
                System.out.println(">> Sukses: Data karyawan berhasil diubah!");
            } else {
                System.out.println(">> Gagal: ID Karyawan tidak ditemukan.");
            }
        }
    }

    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM karyawan WHERE id_karyawan = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            if (ps.executeUpdate() > 0) {
                System.out.println(">> Sukses: Karyawan berhasil dihapus!");
            } else {
                System.out.println(">> Gagal: ID Karyawan tidak ditemukan.");
            }
        }
    }
}