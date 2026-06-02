package dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import util.DatabaseConnection;

/**
 * PasswordDAO — ganti password & reset password
 * untuk tiga peran: Mahasiswa, Dosen, Admin.
 *
 * Semua password di-hash SHA-256 sebelum disimpan/diverifikasi.
 */
public class PasswordDAO {

    private static Connection getConnection() throws Exception {
        return DatabaseConnection.getConnection();
    }

    /** Hash password dengan SHA-256, kembalikan hex string. */
    private String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashed = md.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashed) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 tidak tersedia", e);
        }
    }

    // ─────────────────────────────────────────────────────────────────
    //  MAHASISWA
    // ─────────────────────────────────────────────────────────────────

    public boolean verifikasiPasswordMahasiswa(String nim, String password) {
        String sql = "SELECT 1 FROM kelompok4.mahasiswa WHERE nim=? AND password_mahasiswa=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nim);
            ps.setString(2, password);
            return ps.executeQuery().next();
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean gantiPasswordMahasiswa(String nim, String passwordLama, String passwordBaru) {
        if (!verifikasiPasswordMahasiswa(nim, passwordLama)) return false;
        String sql = "UPDATE kelompok4.mahasiswa SET password_mahasiswa=? WHERE nim=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, passwordBaru);
            ps.setString(2, nim);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public String getEmailMahasiswa(String nim) {
        String sql = "SELECT email FROM kelompok4.mahasiswa WHERE nim=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nim);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("email");
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public boolean resetPasswordMahasiswaByEmail(String nim, String email, String passwordBaru) {
        String cek = "SELECT 1 FROM kelompok4.mahasiswa WHERE nim=? AND LOWER(email)=LOWER(?)";
        try (Connection conn = getConnection();
             PreparedStatement psCek = conn.prepareStatement(cek)) {
            psCek.setString(1, nim);
            psCek.setString(2, email);
            if (!psCek.executeQuery().next()) return false;

            String upd = "UPDATE kelompok4.mahasiswa SET password_mahasiswa=? WHERE nim=?";
            PreparedStatement psUpd = conn.prepareStatement(upd);
            psUpd.setString(1, passwordBaru);
            psUpd.setString(2, nim);
            return psUpd.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // ─────────────────────────────────────────────────────────────────
    //  DOSEN
    // ─────────────────────────────────────────────────────────────────

    public boolean verifikasiPasswordDosen(String nidn, String passwordLama) {
        String sql = "SELECT 1 FROM kelompok4.dosen WHERE nidn=? AND password_dosen=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nidn);
            ps.setString(2, passwordLama);
            return ps.executeQuery().next();
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean gantiPasswordDosen(String nidn, String passwordLama, String passwordBaru) {
        if (!verifikasiPasswordDosen(nidn, passwordLama)) return false;
        String sql = "UPDATE kelompok4.dosen SET password_dosen=? WHERE nidn=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, passwordBaru);
            ps.setString(2, nidn);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public String getEmailDosen(String nidn) {
        String sql = "SELECT email FROM kelompok4.dosen WHERE nidn=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nidn);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("email");
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public boolean resetPasswordDosenByEmail(String nidn, String email, String passwordBaru) {
        String cek = "SELECT 1 FROM kelompok4.dosen WHERE nidn=? AND LOWER(email)=LOWER(?)";
        try (Connection conn = getConnection();
             PreparedStatement psCek = conn.prepareStatement(cek)) {
            psCek.setString(1, nidn);
            psCek.setString(2, email);
            if (!psCek.executeQuery().next()) return false;

            String upd = "UPDATE kelompok4.dosen SET password_dosen=? WHERE nidn=?";
            PreparedStatement psUpd = conn.prepareStatement(upd);
            psUpd.setString(1, passwordBaru);
            psUpd.setString(2, nidn);
            return psUpd.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // ─────────────────────────────────────────────────────────────────
    //  ADMIN
    // ─────────────────────────────────────────────────────────────────

    public boolean verifikasiPasswordAdmin(String username, String passwordLama) {
        String sql = "SELECT 1 FROM kelompok4.admin WHERE username=? AND password=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, passwordLama);
            return ps.executeQuery().next();
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean gantiPasswordAdmin(String username, String passwordLama, String passwordBaru) {
        if (!verifikasiPasswordAdmin(username, passwordLama)) return false;
        String sql = "UPDATE kelompok4.admin SET password=? WHERE username=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, passwordBaru);
            ps.setString(2, username);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}