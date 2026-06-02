package dao;

import java.sql.*;
import java.util.*;
import model.Jadwal;
import util.DatabaseConnection;

public class JadwalDAO {

    // Ambil semua jadwal aktif
    public List<Jadwal> getAllJadwalAktif() {
        List<Jadwal> list = new ArrayList<>();
        String sql = """
            SELECT mk.nama_mata_kuliah, d.nama_dosen, r.nama_ruangan,
                   j.hari, j.jam_mulai, j.jam_selesai,
                   ta.tahun_akademik, ta.semester
            FROM kelompok4.jadwal j
            JOIN kelompok4.kelas k ON j.id_kelas = k.id_kelas
            JOIN kelompok4.mata_kuliah mk ON k.kode_mk = mk.kode_mk
            JOIN kelompok4.dosen d ON k.nidn = d.nidn
            JOIN kelompok4.ruangan r ON j.id_ruangan = r.id_ruangan
            JOIN kelompok4.tahun_akademik ta ON k.id_ta = ta.id_ta
            WHERE ta.is_aktif = TRUE
            ORDER BY j.hari, j.jam_mulai
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) { list.add(mapJadwal(rs, true)); }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // Filter berdasarkan hari
    public List<Jadwal> getJadwalByHari(String hari) {
        List<Jadwal> list = new ArrayList<>();
        String sql = """
            SELECT mk.nama_mata_kuliah, d.nama_dosen, r.nama_ruangan,
                   j.hari, j.jam_mulai, j.jam_selesai,
                   ta.tahun_akademik, ta.semester
            FROM kelompok4.jadwal j
            JOIN kelompok4.kelas k ON j.id_kelas = k.id_kelas
            JOIN kelompok4.mata_kuliah mk ON k.kode_mk = mk.kode_mk
            JOIN kelompok4.dosen d ON k.nidn = d.nidn
            JOIN kelompok4.ruangan r ON j.id_ruangan = r.id_ruangan
            JOIN kelompok4.tahun_akademik ta ON k.id_ta = ta.id_ta
            WHERE j.hari = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hari);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) { list.add(mapJadwal(rs, true)); }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // Jadwal berdasarkan NIM mahasiswa
    public List<Jadwal> getJadwalByNim(String nim) {
        List<Jadwal> list = new ArrayList<>();
        String sql = """
            SELECT mk.nama_mata_kuliah, d.nama_dosen, r.nama_ruangan,
                   j.hari, j.jam_mulai, j.jam_selesai,
                   ta.tahun_akademik, ta.semester
            FROM kelompok4.krs kr
            JOIN kelompok4.kelas kls ON kr.id_kelas = kls.id_kelas
            JOIN kelompok4.jadwal j ON j.id_kelas = kls.id_kelas
            JOIN kelompok4.mata_kuliah mk ON kls.kode_mk = mk.kode_mk
            JOIN kelompok4.dosen d ON kls.nidn = d.nidn
            JOIN kelompok4.ruangan r ON j.id_ruangan = r.id_ruangan
            JOIN kelompok4.tahun_akademik ta ON kls.id_ta = ta.id_ta
            WHERE kr.nim = ? AND ta.is_aktif = TRUE
            ORDER BY j.hari, j.jam_mulai
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nim);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) { list.add(mapJadwal(rs, true)); }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // Jadwal Ujian
    public List<Jadwal> getJadwalUjian(String jenis) {
        List<Jadwal> list = new ArrayList<>();
        String sql = """
            SELECT mk.nama_mata_kuliah, d.nama_dosen,
                   ju.jenis_ujian, ju.tanggal,
                   ju.jam_mulai, ju.jam_selesai,
                   r.nama_ruangan, ju.pengawas
            FROM kelompok4.jadwal_ujian ju
            JOIN kelompok4.kelas k ON ju.id_kelas = k.id_kelas
            JOIN kelompok4.mata_kuliah mk ON k.kode_mk = mk.kode_mk
            JOIN kelompok4.dosen d ON k.nidn = d.nidn
            JOIN kelompok4.ruangan r ON ju.id_ruangan = r.id_ruangan
            WHERE ju.jenis_ujian = ?
            ORDER BY ju.tanggal, ju.jam_mulai
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, jenis);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Jadwal j = new Jadwal();
                j.setNamaMataKuliah(rs.getString("nama_mata_kuliah"));
                j.setNamaDosen(rs.getString("nama_dosen"));
                j.setJenisUjian(rs.getString("jenis_ujian"));
                j.setTanggal(rs.getDate("tanggal"));
                j.setJamMulai(rs.getTime("jam_mulai"));
                j.setJamSelesai(rs.getTime("jam_selesai"));
                j.setNamaRuangan(rs.getString("nama_ruangan"));
                j.setPengawas(rs.getString("pengawas"));
                list.add(j);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    
    public boolean tambahJadwal(int idKelas, int idRuangan,
                                 String hari, String jamMulai, String jamSelesai) {
        String sql = "INSERT INTO kelompok4.jadwal (id_kelas, id_ruangan, hari, jam_mulai, jam_selesai) " +
                     "VALUES (?, ?, ?, ?::time, ?::time)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idKelas);
            ps.setInt(2, idRuangan);
            ps.setString(3, hari);
            ps.setString(4, jamMulai);
            ps.setString(5, jamSelesai);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    /**
     * Hapus jadwal berdasarkan id_jadwal
     */
    public boolean hapusJadwal(int idJadwal) {
        String sql = "DELETE FROM kelompok4.jadwal WHERE id_jadwal=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idJadwal);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // Ambil daftar kelas aktif untuk dropdown form tambah jadwal
    public ResultSet getKelasAktif() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        return conn.createStatement().executeQuery(
            "SELECT k.id_kelas, mk.nama_mata_kuliah, k.nama_kelas, d.nama_dosen " +
            "FROM kelompok4.kelas k " +
            "JOIN kelompok4.mata_kuliah mk ON k.kode_mk = mk.kode_mk " +
            "JOIN kelompok4.dosen d ON k.nidn = d.nidn " +
            "JOIN kelompok4.tahun_akademik ta ON k.id_ta = ta.id_ta " +
            "WHERE ta.is_aktif = TRUE ORDER BY mk.nama_mata_kuliah"
        );
    }

    // Ambil daftar ruangan untuk dropdown
    public ResultSet getRuangan() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        return conn.createStatement().executeQuery(
            "SELECT id_ruangan, nama_ruangan FROM kelompok4.ruangan ORDER BY nama_ruangan"
        );
    }

    private Jadwal mapJadwal(ResultSet rs, boolean withTA) throws SQLException {
        Jadwal j = new Jadwal();
        j.setNamaMataKuliah(rs.getString("nama_mata_kuliah"));
        j.setNamaDosen(rs.getString("nama_dosen"));
        j.setNamaRuangan(rs.getString("nama_ruangan"));
        j.setHari(rs.getString("hari"));
        j.setJamMulai(rs.getTime("jam_mulai"));
        j.setJamSelesai(rs.getTime("jam_selesai"));
        if (withTA) {
            try {
                j.setTahunAkademik(rs.getString("tahun_akademik"));
                j.setSemester(rs.getString("semester"));
            } catch (Exception ignored) {}
        }
        return j;
    }
}
