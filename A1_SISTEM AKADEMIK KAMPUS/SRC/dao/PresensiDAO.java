package dao;

import java.sql.*;
import model.Presensi;
import util.DatabaseConnection;

public class PresensiDAO {

    // FIX: nama view yang benar adalah v_rekap_presensi, bukan view_rekap_presensi
    public ResultSet getRekapPresensi(String nim) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(
            "SELECT * FROM kelompok4.v_rekap_presensi WHERE nim=?"
        );
        ps.setString(1, nim);
        return ps.executeQuery();
    }

    // FIX: sama, ganti view_rekap_presensi → v_rekap_presensi
    public ResultSet getMahasiswaBawah75Persen() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        return conn.createStatement().executeQuery(
            "SELECT * FROM kelompok4.v_rekap_presensi WHERE persen_kehadiran < 75"
        );
    }

    public ResultSet getMahasiswaByKelas(int idKelas) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT krs.id_krs, m.nim, m.nama_mahasiswa " +
                     "FROM kelompok4.krs " +
                     "JOIN kelompok4.mahasiswa m ON krs.nim = m.nim " +
                     "WHERE krs.id_kelas = ? AND krs.status_krs = 'Disetujui' " +
                     "ORDER BY m.nama_mahasiswa";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, idKelas);
        return ps.executeQuery();
    }

    public boolean tambahPresensi(Presensi p) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO kelompok4.presensi(id_krs, id_jadwal, tanggal, status, keterangan) " +
                "VALUES (?, ?, ?, ?, ?)"
            );
            ps.setInt(1, p.getIdKrs());
            ps.setInt(2, p.getIdJadwal());
            ps.setDate(3, p.getTanggal());
            ps.setString(4, p.getStatus());
            ps.setString(5, p.getKeterangan());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}