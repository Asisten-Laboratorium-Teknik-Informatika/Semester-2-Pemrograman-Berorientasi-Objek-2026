package dao;

import java.sql.*;
import java.util.*;
import model.Dosen;
import model.Jadwal;
import model.KRS;
import util.DatabaseConnection;

public class DosenDAO {

    public Dosen loginDosen(String nidn, String password) {
        // Password dosen disimpan di kolom 'password_dosen' yang kita tambahkan
        String sql = "SELECT * FROM kelompok4.dosen WHERE nidn=? AND password_dosen=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nidn);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Dosen d = new Dosen();
                d.setNidn(rs.getString("nidn"));
                d.setNamaDosen(rs.getString("nama_dosen"));
                d.setEmail(rs.getString("email"));
                d.setJabatan(rs.getString("jabatan"));
                d.setIdProdi(rs.getInt("id_prodi"));
                return d;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

   
    public List<KRS> getKrsPendingByNidn(String nidn) {
        List<KRS> list = new ArrayList<>();
        String sql = "SELECT k.id_krs, mh.nama_mahasiswa, mk.nama_mata_kuliah, mk.jumlah_sks, " +
                     "d.nama_dosen, kls.nama_kelas, ta.tahun_akademik, ta.semester, k.status_krs " +
                     "FROM kelompok4.krs k " +
                     "JOIN kelompok4.mahasiswa mh ON k.nim = mh.nim " +
                     "JOIN kelompok4.kelas kls ON k.id_kelas = kls.id_kelas " +
                     "JOIN kelompok4.mata_kuliah mk ON kls.kode_mk = mk.kode_mk " +
                     "JOIN kelompok4.dosen d ON kls.nidn = d.nidn " +
                     "JOIN kelompok4.tahun_akademik ta ON k.id_ta = ta.id_ta " +
                     "WHERE kls.nidn = ? AND k.status_krs = 'Diajukan' " +
                     "ORDER BY mh.nama_mahasiswa";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nidn);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                KRS k = new KRS();
                k.setIdKrs(rs.getInt("id_krs"));
                k.setNamaMahasiswa(rs.getString("nama_mahasiswa"));
                k.setNamaMataKuliah(rs.getString("nama_mata_kuliah"));
                k.setJumlahSks(rs.getInt("jumlah_sks"));
                k.setNamaDosen(rs.getString("nama_dosen"));
                k.setNamaKelas(rs.getString("nama_kelas"));
                k.setTahunAkademik(rs.getString("tahun_akademik"));
                k.setSemesterTA(rs.getString("semester"));
                k.setStatusKrs(rs.getString("status_krs"));
                list.add(k);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    /**
     * Ambil semua KRS (Diajukan + Disetujui + Ditolak) untuk kelas dosen ini
     */
    public List<KRS> getAllKrsByNidn(String nidn) {
        List<KRS> list = new ArrayList<>();
        String sql = "SELECT k.id_krs, mh.nama_mahasiswa, mk.nama_mata_kuliah, mk.jumlah_sks, " +
                     "d.nama_dosen, kls.nama_kelas, ta.tahun_akademik, ta.semester, k.status_krs " +
                     "FROM kelompok4.krs k " +
                     "JOIN kelompok4.mahasiswa mh ON k.nim = mh.nim " +
                     "JOIN kelompok4.kelas kls ON k.id_kelas = kls.id_kelas " +
                     "JOIN kelompok4.mata_kuliah mk ON kls.kode_mk = mk.kode_mk " +
                     "JOIN kelompok4.dosen d ON kls.nidn = d.nidn " +
                     "JOIN kelompok4.tahun_akademik ta ON k.id_ta = ta.id_ta " +
                     "WHERE kls.nidn = ? " +
                     "ORDER BY k.status_krs, mh.nama_mahasiswa";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nidn);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                KRS k = new KRS();
                k.setIdKrs(rs.getInt("id_krs"));
                k.setNamaMahasiswa(rs.getString("nama_mahasiswa"));
                k.setNamaMataKuliah(rs.getString("nama_mata_kuliah"));
                k.setJumlahSks(rs.getInt("jumlah_sks"));
                k.setNamaDosen(rs.getString("nama_dosen"));
                k.setNamaKelas(rs.getString("nama_kelas"));
                k.setTahunAkademik(rs.getString("tahun_akademik"));
                k.setSemesterTA(rs.getString("semester"));
                k.setStatusKrs(rs.getString("status_krs"));
                list.add(k);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    /**
     * Setujui atau tolak KRS
     */
    public boolean updateStatusKrs(int idKrs, String status) {
        String sql = "UPDATE kelompok4.krs SET status_krs=? WHERE id_krs=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, idKrs);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    /**
     * Tambah kelas baru oleh dosen
     * Dosen memilih mata kuliah, tahun akademik aktif, nama kelas, dan kuota
     */
    public boolean tambahKelas(String kodeMk, String nidn, String namaKelas, int kuota) {
        // Ambil id_ta yang aktif
        String sqlTA = "SELECT id_ta FROM kelompok4.tahun_akademik WHERE is_aktif = TRUE LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sqlTA)) {
            if (!rs.next()) return false;
            int idTa = rs.getInt("id_ta");

            String sql = "INSERT INTO kelompok4.kelas (kode_mk, nidn, id_ta, nama_kelas, kuota) " +
                         "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, kodeMk);
            ps.setString(2, nidn);
            ps.setInt(3, idTa);
            ps.setString(4, namaKelas);
            ps.setInt(5, kuota);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    /**
     * Tambah kelas + langsung set jadwalnya sekaligus
     * Mengembalikan id_kelas yang baru dibuat, atau -1 jika gagal
     */
    public int tambahKelasReturnId(String kodeMk, String nidn, String namaKelas, int kuota) {
        String sqlTA = "SELECT id_ta FROM kelompok4.tahun_akademik WHERE is_aktif = TRUE LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rsTA = st.executeQuery(sqlTA)) {
            if (!rsTA.next()) return -1;
            int idTa = rsTA.getInt("id_ta");

            String sql = "INSERT INTO kelompok4.kelas (kode_mk, nidn, id_ta, nama_kelas, kuota) " +
                         "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, kodeMk);
            ps.setString(2, nidn);
            ps.setInt(3, idTa);
            ps.setString(4, namaKelas);
            ps.setInt(5, kuota);
            ps.executeUpdate();

            ResultSet rsKey = ps.getGeneratedKeys();
            if (rsKey.next()) return rsKey.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    /**
     * Tambah jadwal untuk kelas yang sudah ada
     */
    public boolean tambahJadwal(int idKelas, int idRuangan, String hari,
                                 String jamMulai, String jamSelesai) {
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
     * Ambil jadwal kuliah milik dosen ini (semester aktif)
     */
    public List<Jadwal> getJadwalByNidn(String nidn) {
        List<Jadwal> list = new ArrayList<>();
        String sql = "SELECT mk.nama_mata_kuliah, d.nama_dosen, r.nama_ruangan, " +
                     "j.hari, j.jam_mulai, j.jam_selesai, ta.tahun_akademik, ta.semester, " +
                     "k.nama_kelas " +
                     "FROM kelompok4.jadwal j " +
                     "JOIN kelompok4.kelas k ON j.id_kelas = k.id_kelas " +
                     "JOIN kelompok4.mata_kuliah mk ON k.kode_mk = mk.kode_mk " +
                     "JOIN kelompok4.dosen d ON k.nidn = d.nidn " +
                     "JOIN kelompok4.ruangan r ON j.id_ruangan = r.id_ruangan " +
                     "JOIN kelompok4.tahun_akademik ta ON k.id_ta = ta.id_ta " +
                     "WHERE k.nidn = ? AND ta.is_aktif = TRUE " +
                     "ORDER BY j.hari, j.jam_mulai";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nidn);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Jadwal j = new Jadwal();
                j.setNamaMataKuliah(rs.getString("nama_mata_kuliah"));
                j.setNamaDosen(rs.getString("nama_dosen"));
                j.setNamaRuangan(rs.getString("nama_ruangan"));
                j.setHari(rs.getString("hari"));
                j.setJamMulai(rs.getTime("jam_mulai"));
                j.setJamSelesai(rs.getTime("jam_selesai"));
                j.setTahunAkademik(rs.getString("tahun_akademik"));
                j.setSemester(rs.getString("semester"));
                list.add(j);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    /**
     * Ambil semua mata kuliah (untuk dropdown pilih MK saat tambah kelas)
     */
    public ResultSet getAllMataKuliah() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        return conn.createStatement().executeQuery(
            "SELECT kode_mk, nama_mata_kuliah, jumlah_sks FROM kelompok4.mata_kuliah ORDER BY nama_mata_kuliah"
        );
    }

    /**
     * Ambil semua ruangan (untuk dropdown pilih ruangan saat tambah jadwal)
     */
    public ResultSet getRuangan() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        return conn.createStatement().executeQuery(
            "SELECT id_ruangan, nama_ruangan FROM kelompok4.ruangan ORDER BY nama_ruangan"
        );
    }

    /**
     * Ambil daftar kelas+jadwal milik dosen (semester aktif),
     * dipakai untuk dropdown input presensi di panel dosen.
     */
    public ResultSet getJadwalKelasDetail(String nidn) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT j.id_jadwal, k.id_kelas, " +
                     "mk.nama_mata_kuliah, k.nama_kelas, " +
                     "j.hari, j.jam_mulai, ta.tahun_akademik " +
                     "FROM kelompok4.jadwal j " +
                     "JOIN kelompok4.kelas k      ON j.id_kelas   = k.id_kelas " +
                     "JOIN kelompok4.mata_kuliah mk ON k.kode_mk  = mk.kode_mk " +
                     "JOIN kelompok4.tahun_akademik ta ON k.id_ta = ta.id_ta " +
                     "WHERE k.nidn = ? AND ta.is_aktif = TRUE " +
                     "ORDER BY j.hari, j.jam_mulai";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nidn);
        return ps.executeQuery();
    }

    // ─── TAMBAHAN UNTUK ADMIN ────────────────────────────────────────────

    /** Ambil semua dosen — dipakai di panel admin */
    public List<Dosen> getAllDosen() {
        List<Dosen> list = new ArrayList<>();
        String sql = "SELECT * FROM kelompok4.dosen ORDER BY nama_dosen";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Dosen d = new Dosen();
                d.setNidn(rs.getString("nidn"));
                d.setNamaDosen(rs.getString("nama_dosen"));
                d.setJenisKelamin(rs.getString("jenis_kelamin"));
                d.setEmail(rs.getString("email"));
                d.setNoTelp(rs.getString("no_telp"));
                d.setJabatan(rs.getString("jabatan"));
                d.setIdProdi(rs.getInt("id_prodi"));
                list.add(d);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    /** Tambah dosen baru — password default = NIDN */
    public boolean tambahDosen(Dosen d) {
        String sql = "INSERT INTO kelompok4.dosen " +
                     "(nidn, nama_dosen, jenis_kelamin, email, no_telp, jabatan, id_prodi, password_dosen) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getNidn());
            ps.setString(2, d.getNamaDosen());
            ps.setString(3, d.getJenisKelamin());
            ps.setString(4, d.getEmail());
            ps.setString(5, d.getNoTelp());
            ps.setString(6, d.getJabatan());
            ps.setInt(7, d.getIdProdi() == 0 ? 1 : d.getIdProdi());
            ps.setString(8, d.getNidn()); // password default = NIDN
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    /** Update data dosen */
    public boolean updateDosen(Dosen d) {
        String sql = "UPDATE kelompok4.dosen SET " +
                     "nama_dosen=?, jenis_kelamin=?, email=?, no_telp=?, jabatan=?, id_prodi=? " +
                     "WHERE nidn=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getNamaDosen());
            ps.setString(2, d.getJenisKelamin());
            ps.setString(3, d.getEmail());
            ps.setString(4, d.getNoTelp());
            ps.setString(5, d.getJabatan());
            ps.setInt(6, d.getIdProdi());
            ps.setString(7, d.getNidn());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    /** Hapus dosen — hanya bisa jika tidak punya kelas aktif */
    public boolean hapusDosen(String nidn) {
        String sql = "DELETE FROM kelompok4.dosen WHERE nidn=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nidn);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    /** Reset password dosen ke NIDN (oleh admin) */
    public boolean resetPasswordDosen(String nidn) {
        String sql = "UPDATE kelompok4.dosen SET password_dosen=? WHERE nidn=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nidn); // reset ke NIDN
            ps.setString(2, nidn);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}