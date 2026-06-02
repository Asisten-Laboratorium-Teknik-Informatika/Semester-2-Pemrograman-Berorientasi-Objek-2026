package dao;

import java.sql.*;
import java.util.*;
import model.Nilai;
import util.DatabaseConnection;

public class NilaiDAO {

    public List<Nilai> getNilaiByNim(String nim) {
        List<Nilai> list = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getConnection();

            String sql = "SELECT m.nama_mata_kuliah, m.jumlah_sks, n.*, ta.tahun_akademik, ta.semester " +
                         "FROM kelompok4.nilai n " +
                         "JOIN kelompok4.krs k ON n.id_krs = k.id_krs " +
                         "JOIN kelompok4.kelas kls ON k.id_kelas = kls.id_kelas " +
                         "JOIN kelompok4.mata_kuliah m ON kls.kode_mk = m.kode_mk " +
                         "JOIN kelompok4.tahun_akademik ta ON k.id_ta = ta.id_ta " +
                         "WHERE k.nim=?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nim);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Nilai n = new Nilai();
                n.setNamaMataKuliah(rs.getString("nama_mata_kuliah"));
                n.setJumlahSks(rs.getInt("jumlah_sks"));
                n.setNilaiTugas(rs.getDouble("nilai_tugas"));
                n.setNilaiUts(rs.getDouble("nilai_uts"));
                n.setNilaiUas(rs.getDouble("nilai_uas"));
                n.setNilaiAngka(rs.getDouble("nilai_angka"));
                n.setNilaiHuruf(rs.getString("nilai_huruf"));
                n.setBobotNilai(rs.getDouble("bobot_nilai"));
                n.setIsFinal(rs.getBoolean("is_final"));
                n.setTahunAkademik(rs.getString("tahun_akademik"));
                n.setSemester(rs.getString("semester"));
                list.add(n);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public boolean simpanNilai(Nilai n) {
        try {
            Connection conn = DatabaseConnection.getConnection();

            double angka = n.hitungNilaiAngka();
            String huruf = konversiHuruf(angka);
            double bobot = konversiBobot(huruf);

            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO kelompok4.nilai(id_krs,nilai_tugas,nilai_uts,nilai_uas,nilai_angka,nilai_huruf,bobot_nilai,is_final) " +
                "VALUES (?,?,?,?,?,?,?,?)");

            ps.setInt(1, n.getIdKrs());
            ps.setDouble(2, n.getNilaiTugas());
            ps.setDouble(3, n.getNilaiUts());
            ps.setDouble(4, n.getNilaiUas());
            ps.setDouble(5, angka);
            ps.setString(6, huruf);
            ps.setDouble(7, bobot);
            ps.setBoolean(8, true); // langsung final karena ini input dari dosen, bukan admin

            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    /**
     * Update nilai yang sudah ada berdasarkan id_krs.
     * Dipakai dosen ketika nilai sudah pernah diinput sebelumnya.
     */
    public boolean updateNilai(Nilai n) {
        try {
            Connection conn = DatabaseConnection.getConnection();

            double angka = n.hitungNilaiAngka();
            String huruf = konversiHuruf(angka);
            double bobot = konversiBobot(huruf);

            PreparedStatement ps = conn.prepareStatement(
                "UPDATE kelompok4.nilai SET " +
                "nilai_tugas=?, nilai_uts=?, nilai_uas=?, nilai_angka=?, nilai_huruf=?, bobot_nilai=?, is_final=? " +
                "WHERE id_krs=?");

            ps.setDouble(1, n.getNilaiTugas());
            ps.setDouble(2, n.getNilaiUts());
            ps.setDouble(3, n.getNilaiUas());
            ps.setDouble(4, angka);
            ps.setString(5, huruf);
            ps.setDouble(6, bobot);
            ps.setBoolean(7, true);
            ps.setInt(8, n.getIdKrs());

            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public double hitungIpk(String nim) {
        double total = 0, sks = 0;
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "SELECT m.jumlah_sks, n.bobot_nilai FROM kelompok4.nilai n " +
                "JOIN kelompok4.krs k ON n.id_krs=k.id_krs " +
                "JOIN kelompok4.kelas kls ON k.id_kelas=kls.id_kelas " +
                "JOIN kelompok4.mata_kuliah m ON kls.kode_mk=m.kode_mk " +
                "WHERE k.nim=? AND n.is_final=true");

            ps.setString(1, nim);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                total += rs.getDouble("bobot_nilai") * rs.getInt("jumlah_sks");
                sks += rs.getInt("jumlah_sks");
            }
        } catch (Exception e) { e.printStackTrace(); }
        return sks == 0 ? 0 : total / sks;
    }

    public double hitungIps(String nim) {

    double total = 0;
    double sks = 0;

    try {
        Connection conn = DatabaseConnection.getConnection();

        String sql =
            "SELECT m.jumlah_sks, n.bobot_nilai " +
            "FROM kelompok4.nilai n " +
            "JOIN kelompok4.krs k ON n.id_krs = k.id_krs " +
            "JOIN kelompok4.kelas kls ON k.id_kelas = kls.id_kelas " +
            "JOIN kelompok4.mata_kuliah m ON kls.kode_mk = m.kode_mk " +
            "JOIN kelompok4.tahun_akademik ta ON k.id_ta = ta.id_ta " +
            "WHERE k.nim=? " +
            "AND n.is_final=true " +
            "AND ta.is_aktif=true";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nim);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            total +=
                rs.getDouble("bobot_nilai")
                * rs.getInt("jumlah_sks");

            sks +=
                rs.getInt("jumlah_sks");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return sks == 0 ? 0 : total / sks;
    }

    public int getTotalSksLulus(String nim) {

    int total = 0;

    try {

        Connection conn =
            DatabaseConnection.getConnection();

        String sql =
            "SELECT m.jumlah_sks " +
            "FROM kelompok4.nilai n " +
            "JOIN kelompok4.krs k ON n.id_krs = k.id_krs " +
            "JOIN kelompok4.kelas kls ON k.id_kelas = kls.id_kelas " +
            "JOIN kelompok4.mata_kuliah m ON kls.kode_mk = m.kode_mk " +
            "WHERE k.nim=? " +
            "AND n.is_final=true " +
            "AND n.bobot_nilai > 0";

        PreparedStatement ps =
            conn.prepareStatement(sql);

        ps.setString(1, nim);

        ResultSet rs =
            ps.executeQuery();

        while (rs.next()) {

            total +=
                rs.getInt("jumlah_sks");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return total;
    }

    public ResultSet getRankingNilai() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT mh.nim, mh.nama_mahasiswa, AVG(n.nilai_angka) as rata_rata " +
                     "FROM kelompok4.nilai n " +
                     "JOIN kelompok4.krs k ON n.id_krs=k.id_krs " +
                     "JOIN kelompok4.mahasiswa mh ON k.nim=mh.nim " +
                     "GROUP BY mh.nim, mh.nama_mahasiswa " +
                     "ORDER BY rata_rata DESC";
        return conn.createStatement().executeQuery(sql);
    }

    // helper
    private String konversiHuruf(double nilai) {
        if (nilai >= 85) return "A";
        if (nilai >= 70) return "B";
        if (nilai >= 60) return "C";
        if (nilai >= 50) return "D";
        return "E";
    }

    private double konversiBobot(String huruf) {
        switch (huruf) {
            case "A": return 4;
            case "B": return 3;
            case "C": return 2;
            case "D": return 1;
            default: return 0;
        }
    }

    public List<Nilai> getTranskrip(String nim) {
    List<Nilai> list = new ArrayList<>();

    try {

        Connection conn = DatabaseConnection.getConnection();

        String sql =
            "SELECT m.nama_mata_kuliah, " +
            "m.jumlah_sks, " +
            "n.nilai_huruf, " +
            "n.bobot_nilai " +
            "FROM kelompok4.nilai n " +
            "JOIN kelompok4.krs k ON n.id_krs = k.id_krs " +
            "JOIN kelompok4.kelas kls ON k.id_kelas = kls.id_kelas " +
            "JOIN kelompok4.mata_kuliah m ON kls.kode_mk = m.kode_mk " +
            "WHERE k.nim=? " +
            "AND n.is_final=true " +
            "ORDER BY m.nama_mata_kuliah";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nim);

        ResultSet rs = ps.executeQuery();

        while(rs.next()) {

            Nilai n = new Nilai();

            n.setNamaMataKuliah(
                rs.getString("nama_mata_kuliah"));

            n.setJumlahSks(
                rs.getInt("jumlah_sks"));

            n.setNilaiHuruf(
                rs.getString("nilai_huruf"));

            n.setBobotNilai(
                rs.getDouble("bobot_nilai"));

            list.add(n);
        }

    } catch(Exception e) {
        e.printStackTrace();
    }

    return list;
    }
}