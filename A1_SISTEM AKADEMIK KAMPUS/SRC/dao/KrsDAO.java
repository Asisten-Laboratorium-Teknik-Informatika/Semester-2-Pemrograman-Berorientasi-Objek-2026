package dao;

import java.sql.*;
import java.util.*;
import model.KRS;
import util.DatabaseConnection;

public class KrsDAO {

    public List<KRS> getKrsAktifByNim(String nim) {
        List<KRS> list = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT k.id_krs, m.nama_mata_kuliah, m.jumlah_sks, " +
                         "d.nama_dosen, kls.nama_kelas, ta.tahun_akademik, " +
                         "ta.semester, k.status_krs, mh.nama_mahasiswa " +
                         "FROM kelompok4.krs k " +
                         "JOIN kelompok4.mahasiswa mh ON k.nim = mh.nim " +
                         "JOIN kelompok4.kelas kls ON k.id_kelas = kls.id_kelas " +
                         "JOIN kelompok4.mata_kuliah m ON kls.kode_mk = m.kode_mk " +
                         "JOIN kelompok4.dosen d ON kls.nidn = d.nidn " +
                         "JOIN kelompok4.tahun_akademik ta ON k.id_ta = ta.id_ta " +
                         "WHERE k.nim = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nim);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                KRS k = new KRS();
                k.setIdKrs(rs.getInt("id_krs"));
                k.setNamaMataKuliah(rs.getString("nama_mata_kuliah"));
                k.setJumlahSks(rs.getInt("jumlah_sks"));
                k.setNamaDosen(rs.getString("nama_dosen"));
                k.setNamaKelas(rs.getString("nama_kelas"));
                k.setTahunAkademik(rs.getString("tahun_akademik"));
                k.setSemesterTA(rs.getString("semester"));
                k.setStatusKrs(rs.getString("status_krs"));
                k.setNamaMahasiswa(rs.getString("nama_mahasiswa"));
                list.add(k);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    /**
     * Mahasiswa mengajukan KRS — status otomatis 'Diajukan', menunggu persetujuan dosen
     */
    public boolean ajukanKrs(String nim, int idKelas) {
        // Cek apakah sudah pernah daftar kelas ini
        String cek = "SELECT 1 FROM kelompok4.krs WHERE nim=? AND id_kelas=?";
        // Ambil id_ta aktif
        String sqlTA = "SELECT id_ta, semester FROM kelompok4.tahun_akademik WHERE is_aktif=TRUE LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement psCek = conn.prepareStatement(cek);
            psCek.setString(1, nim);
            psCek.setInt(2, idKelas);
            ResultSet rsCek = psCek.executeQuery();
            if (rsCek.next()) return false; // sudah terdaftar

            Statement st = conn.createStatement();
            ResultSet rsTA = st.executeQuery(sqlTA);
            if (!rsTA.next()) return false;
            int idTa = rsTA.getInt("id_ta");
            int semesterKe = rsTA.getString("semester").equals("Ganjil") ? 1 : 2;

            String sql = "INSERT INTO kelompok4.krs (nim, id_kelas, id_ta, semester, status_krs) " +
                         "VALUES (?, ?, ?, ?, 'Diajukan')";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nim);
            ps.setInt(2, idKelas);
            ps.setInt(3, idTa);
            ps.setInt(4, semesterKe);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }


    public ResultSet getKelasAktifTersedia(String nim) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT k.id_kelas, mk.kode_mk, mk.nama_mata_kuliah, mk.jumlah_sks, " +
                     "d.nama_dosen, k.nama_kelas, k.kuota, ta.tahun_akademik, ta.semester, " +
                     "(SELECT COUNT(*) FROM kelompok4.krs kr2 WHERE kr2.id_kelas = k.id_kelas) AS peserta " +
                     "FROM kelompok4.kelas k " +
                     "JOIN kelompok4.mata_kuliah mk ON k.kode_mk = mk.kode_mk " +
                     "JOIN kelompok4.dosen d ON k.nidn = d.nidn " +
                     "JOIN kelompok4.tahun_akademik ta ON k.id_ta = ta.id_ta " +
                     "WHERE ta.is_aktif = TRUE " +
                     "AND k.id_kelas NOT IN (" +
                     "  SELECT id_kelas FROM kelompok4.krs WHERE nim=?" +
                     ") " +
                     "ORDER BY mk.nama_mata_kuliah, k.nama_kelas";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nim);
        return ps.executeQuery();
    }

    public boolean updateStatusKrs(int idKrs, String status) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE kelompok4.krs SET status_krs=? WHERE id_krs=?");
            ps.setString(1, status);
            ps.setInt(2, idKrs);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean hapusKrs(int idKrs) {
        String[] sqls = {
            "DELETE FROM kelompok4.presensi WHERE id_krs=?",
            "DELETE FROM kelompok4.nilai WHERE id_krs=?",
            "DELETE FROM kelompok4.krs WHERE id_krs=?"
        };
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            for (String sql : sqls) {
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, idKrs);
                    ps.executeUpdate();
                }
            }
            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (Exception ignored) {}
            DatabaseConnection.closeConnection(conn);
        }
        return false;
    }

    public List<KRS> getAllKrs() {
        List<KRS> list = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT k.id_krs, m.nama_mata_kuliah, m.jumlah_sks, " +
                         "d.nama_dosen, kls.nama_kelas, ta.tahun_akademik, " +
                         "ta.semester, k.status_krs, mh.nama_mahasiswa " +
                         "FROM kelompok4.krs k " +
                         "JOIN kelompok4.mahasiswa mh ON k.nim = mh.nim " +
                         "JOIN kelompok4.kelas kls ON k.id_kelas = kls.id_kelas " +
                         "JOIN kelompok4.mata_kuliah m ON kls.kode_mk = m.kode_mk " +
                         "JOIN kelompok4.dosen d ON kls.nidn = d.nidn " +
                         "JOIN kelompok4.tahun_akademik ta ON k.id_ta = ta.id_ta " +
                         "ORDER BY mh.nama_mahasiswa";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                KRS k = new KRS();
                k.setIdKrs(rs.getInt("id_krs"));
                k.setNamaMataKuliah(rs.getString("nama_mata_kuliah"));
                k.setJumlahSks(rs.getInt("jumlah_sks"));
                k.setNamaDosen(rs.getString("nama_dosen"));
                k.setNamaKelas(rs.getString("nama_kelas"));
                k.setTahunAkademik(rs.getString("tahun_akademik"));
                k.setSemesterTA(rs.getString("semester"));
                k.setStatusKrs(rs.getString("status_krs"));
                k.setNamaMahasiswa(rs.getString("nama_mahasiswa"));
                list.add(k);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}