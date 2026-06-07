package Entitas;

import Fitur.Tampilan;
import Koneksi.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TahunAkademik {

    private String idTahun;
    private String tahun;
    private String semesterAktif;
    private String status;

    public TahunAkademik() {}

    public String getIdTahun()      { return idTahun; }
    public String getTahun()        { return tahun; }
    public String getSemesterAktif(){ return semesterAktif; }
    public String getStatus()       { return status; }

    public void setIdTahun(String v)       { this.idTahun = v; }
    public void setTahun(String v)         { this.tahun = v; }
    public void setSemesterAktif(String v) { this.semesterAktif = v; }
    public void setStatus(String v)        { this.status = v; }

    // ===================== TAMPILKAN TAHUN AKTIF =====================
    public void tampilkanTahunAktif() {
        try (Connection conn = Koneksi.connect();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT * FROM b1.view_tahun_aktif")) {

            ResultSet rs = ps.executeQuery();
            System.out.println("\n===== TAHUN AKADEMIK AKTIF =====");
            if (rs.next()) {
                System.out.println("ID Tahun : " + rs.getString("id_tahun"));
                System.out.println("Tahun    : " + rs.getString("tahun"));
                System.out.println("Semester : " + rs.getString("semester_aktif"));
                System.out.println("Status   : " + rs.getString("status"));
            } else {
                Tampilan.peringatan("Tidak ada tahun akademik aktif.");
            }
            System.out.println("================================");

        } catch (Exception e) {
            Tampilan.gagal("Gagal menampilkan tahun akademik!");
            e.printStackTrace();
        }
    }

    // ===================== GET ID TAHUN AKTIF =====================
    public String getIdTahunAktif() {
        try (Connection conn = Koneksi.connect();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT id_tahun FROM b1.view_tahun_aktif LIMIT 1")) {

            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString("id_tahun") : null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ===================== GET INFO TAHUN AKTIF =====================
    public String[] getInfoTahunAktif() {
        try (Connection conn = Koneksi.connect();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT tahun, semester_aktif FROM b1.view_tahun_aktif LIMIT 1")) {

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new String[]{ rs.getString("tahun"), rs.getString("semester_aktif") };
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ===================== TAMBAH TAHUN AKADEMIK =====================
    public static void tambahTahunAkademik() {
        System.out.println("=== DATA TAHUN AKADEMIK ===\n");
        System.out.print("ID Tahun                 : "); String idTahun      = Fitur.Dashboard.input.nextLine().trim();
        System.out.print("Tahun (misal 2025/2026)  : "); String tahun        = Fitur.Dashboard.input.nextLine().trim();
        System.out.print("Semester (Ganjil/Genap)  : "); String semester     = Fitur.Dashboard.input.nextLine().trim();
        System.out.print("Status (Aktif/Nonaktif)  : "); String status       = Fitur.Dashboard.input.nextLine().trim();

        if (idTahun.isEmpty() || tahun.isEmpty() || semester.isEmpty() || status.isEmpty()) {
            Tampilan.gagal("Semua data wajib diisi!"); return;
        }

        if (!tahun.matches("\\d{4}/\\d{4}")) {
            Tampilan.gagal("Format tahun harus seperti 2025/2026!"); return;
        }

        semester = semester.substring(0, 1).toUpperCase() + semester.substring(1).toLowerCase();
        status   = status.substring(0, 1).toUpperCase()   + status.substring(1).toLowerCase();

        if (!semester.equals("Ganjil") && !semester.equals("Genap")) {
            Tampilan.gagal("Semester hanya boleh Ganjil atau Genap!"); return;
        }
        if (!status.equals("Aktif") && !status.equals("Nonaktif")) {
            Tampilan.gagal("Status hanya boleh Aktif atau Nonaktif!"); return;
        }

        try (Connection conn = Koneksi.connect()) {
            if (conn == null) { Tampilan.gagal("Koneksi gagal!"); return; }

            PreparedStatement psCekId = conn.prepareStatement(
                "SELECT COUNT(*) FROM b1.tahun_akademik WHERE id_tahun = ?");
            psCekId.setString(1, idTahun);
            ResultSet rsId = psCekId.executeQuery();
            if (rsId.next() && rsId.getInt(1) > 0) {
                Tampilan.gagal("ID Tahun Akademik sudah digunakan!"); return;
            }

            if (status.equals("Aktif")) {
                PreparedStatement psCekAktif = conn.prepareStatement(
                    "SELECT COUNT(*) FROM b1.tahun_akademik WHERE status = 'Aktif'");
                ResultSet rsAktif = psCekAktif.executeQuery();
                if (rsAktif.next() && rsAktif.getInt(1) > 0) {
                    Tampilan.gagal("Masih ada tahun akademik aktif. Nonaktifkan dulu sebelum menambah yang baru.");
                    return;
                }
            }

            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO b1.tahun_akademik(id_tahun, tahun, semester_aktif, status) " +
                "VALUES(?, ?, ?::b1.semester_aktif_enum, ?::status_enum)");
            ps.setString(1, idTahun);
            ps.setString(2, tahun);
            ps.setString(3, semester);
            ps.setString(4, status);

            if (ps.executeUpdate() > 0) {
                Tampilan.sukses("\n=== DATA TAHUN AKADEMIK BERHASIL DISIMPAN ===");
                System.out.println("ID Tahun : " + idTahun);
                System.out.println("Tahun    : " + tahun);
                System.out.println("Semester : " + semester);
                System.out.println("Status   : " + status);
            } else {
                Tampilan.gagal("Data gagal disimpan.");
            }

        } catch (Exception e) {
            Tampilan.gagal("Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }
}