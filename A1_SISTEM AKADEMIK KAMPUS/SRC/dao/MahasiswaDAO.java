package dao;

import java.sql.*;
import java.util.*;
import model.Mahasiswa;
import util.DatabaseConnection;

public class MahasiswaDAO {

    public List<Mahasiswa> getAllMahasiswa() {
        List<Mahasiswa> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM kelompok4.mahasiswa")) {
            while (rs.next()) {
                list.add(mapMahasiswa(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Mahasiswa loginMahasiswa(String nim, String password) {
        String sql = "SELECT * FROM kelompok4.mahasiswa WHERE nim=? AND password_mahasiswa=?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nim);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapMahasiswa(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String registrasiMahasiswaBaru(Mahasiswa m) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Generate NIM: angkatan + angka urut
            String sqlMaxNim = "SELECT MAX(nim) FROM kelompok4.mahasiswa WHERE nim LIKE ?";
            PreparedStatement psMax = conn.prepareStatement(sqlMaxNim);
            String prefix = String.valueOf(m.getAngkatan());
            psMax.setString(1, prefix + "%");
            ResultSet rsMax = psMax.executeQuery();
            String nimBaru;
            if (rsMax.next() && rsMax.getString(1) != null) {
                long maxNim = Long.parseLong(rsMax.getString(1));
                nimBaru = String.valueOf(maxNim + 1);
            } else {
                nimBaru = prefix + "001001";
            }

            String sql = "INSERT INTO kelompok4.mahasiswa " +
                    "(nim, nama_mahasiswa, jenis_kelamin, tempat_lahir, tanggal_lahir, " +
                    "alamat, no_telp, email, angkatan, id_prodi, status, password_mahasiswa) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'Aktif', ?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nimBaru);
            ps.setString(2, m.getNamaMahasiswa());
            ps.setString(3, m.getJenisKelamin());
            ps.setString(4, m.getTempatLahir());
            ps.setDate(5, m.getTanggalLahir() != null ? m.getTanggalLahir()
                    : java.sql.Date.valueOf("2000-01-01"));
            ps.setString(6, m.getAlamat());
            ps.setString(7, m.getNoTelp());
            ps.setString(8, m.getEmail());
            ps.setInt(9, m.getAngkatan());
            ps.setInt(10, m.getIdProdi() == 0 ? 1 : m.getIdProdi());
            // Gunakan getPasswordMahasiswa() — satu-satunya field password
            ps.setString(11, (m.getPasswordMahasiswa() != null && !m.getPasswordMahasiswa().isEmpty())
                    ? m.getPasswordMahasiswa() : nimBaru);

            if (ps.executeUpdate() > 0)
                return nimBaru;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean tambahMahasiswa(Mahasiswa m) {
        String sql = "INSERT INTO kelompok4.mahasiswa " +
                "(nim, nama_mahasiswa, jenis_kelamin, tempat_lahir, tanggal_lahir, alamat, no_telp, email, angkatan, id_prodi, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getNim());
            ps.setString(2, m.getNamaMahasiswa());
            ps.setString(3, m.getJenisKelamin());
            ps.setString(4, m.getTempatLahir());
            ps.setDate(5, m.getTanggalLahir() != null ? m.getTanggalLahir()
                    : java.sql.Date.valueOf("2000-01-01"));
            ps.setString(6, m.getAlamat());
            ps.setString(7, m.getNoTelp());
            ps.setString(8, m.getEmail());
            ps.setInt(9, m.getAngkatan() == 0 ? 2024 : m.getAngkatan());
            ps.setInt(10, m.getIdProdi() == 0 ? 1 : m.getIdProdi());
            ps.setString(11, m.getStatus() == null ? "Aktif" : m.getStatus());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateMahasiswa(Mahasiswa m) {
        String sql = "UPDATE kelompok4.mahasiswa SET " +
                "nama_mahasiswa=?, jenis_kelamin=?, tempat_lahir=?, tanggal_lahir=?, alamat=?, no_telp=?, email=?, angkatan=?, id_prodi=?, status=? " +
                "WHERE nim=?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getNamaMahasiswa());
            ps.setString(2, m.getJenisKelamin());
            ps.setString(3, m.getTempatLahir());
            ps.setDate(4, m.getTanggalLahir());
            ps.setString(5, m.getAlamat());
            ps.setString(6, m.getNoTelp());
            ps.setString(7, m.getEmail());
            ps.setInt(8, m.getAngkatan());
            ps.setInt(9, m.getIdProdi());
            ps.setString(10, m.getStatus());
            ps.setString(11, m.getNim());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Update HANYA kolom status — aman dipakai admin tanpa perlu data lengkap.
     */
    public boolean updateStatusMahasiswa(String nim, String status) {
        String sql = "UPDATE kelompok4.mahasiswa SET status=? WHERE nim=?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, nim);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean hapusMahasiswa(String nim) {
        // Urutan hapus: presensi → nilai → krs → pembayaran → mahasiswa
        // (status_akademik, penerima_beasiswa, anggota_organisasi sudah ON DELETE CASCADE)
        String[] sqls = {
            // 1. Presensi (via id_krs yang dimiliki mahasiswa ini)
            "DELETE FROM kelompok4.presensi WHERE id_krs IN " +
            "  (SELECT id_krs FROM kelompok4.krs WHERE nim=?)",
            // 2. Nilai (via id_krs)
            "DELETE FROM kelompok4.nilai WHERE id_krs IN " +
            "  (SELECT id_krs FROM kelompok4.krs WHERE nim=?)",
            // 3. KRS
            "DELETE FROM kelompok4.krs WHERE nim=?",
            // 4. Pembayaran (ON DELETE RESTRICT, harus manual)
            "DELETE FROM kelompok4.pembayaran WHERE nim=?",
            // 5. Mahasiswa
            "DELETE FROM kelompok4.mahasiswa WHERE nim=?"
        };
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            for (String sql : sqls) {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, nim);
                ps.executeUpdate();
                ps.close();
            }
            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (Exception ex) { ex.printStackTrace(); }
        }
        return false;
    }

    public List<Mahasiswa> cariMahasiswaByNama(String keyword) {
        List<Mahasiswa> list = new ArrayList<>();
        String sql = "SELECT * FROM kelompok4.mahasiswa WHERE nama_mahasiswa ILIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapMahasiswa(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ResultSet getAllProdi() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        return conn.createStatement().executeQuery(
                "SELECT p.id_prodi, p.nama_prodi, f.nama_fakultas " +
                        "FROM kelompok4.program_studi p " +
                        "JOIN kelompok4.fakultas f ON p.id_fakultas = f.id_fakultas " +
                        "ORDER BY f.nama_fakultas, p.nama_prodi");
    }

    private Mahasiswa mapMahasiswa(ResultSet rs) throws SQLException {
        Mahasiswa m = new Mahasiswa();
        m.setNim(rs.getString("nim"));
        m.setNamaMahasiswa(rs.getString("nama_mahasiswa"));
        m.setJenisKelamin(rs.getString("jenis_kelamin"));
        m.setTempatLahir(rs.getString("tempat_lahir"));
        m.setTanggalLahir(rs.getDate("tanggal_lahir"));
        m.setAlamat(rs.getString("alamat"));
        m.setNoTelp(rs.getString("no_telp"));
        m.setEmail(rs.getString("email"));
        m.setAngkatan(rs.getInt("angkatan"));
        m.setIdProdi(rs.getInt("id_prodi"));
        m.setStatus(rs.getString("status"));
        return m;
    }
}