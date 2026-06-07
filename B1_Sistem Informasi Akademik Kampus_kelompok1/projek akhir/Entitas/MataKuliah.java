package Entitas;

import Koneksi.Koneksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class MataKuliah {

    private String idMataKuliah;
    private String idProgramStudi;
    private String namaMataKuliah;
    private short  sks;
    private short  semesterKe;

    public MataKuliah(String idMataKuliah, String idProgramStudi,
                      String namaMataKuliah, short sks, short semesterKe) {
        this.idMataKuliah   = idMataKuliah;
        this.idProgramStudi = idProgramStudi;
        this.namaMataKuliah = namaMataKuliah;
        this.sks            = sks;
        this.semesterKe     = semesterKe;
    }

    public String getIdMataKuliah()   { return idMataKuliah; }
    public String getIdProgramStudi() { return idProgramStudi; }
    public String getNamaMataKuliah() { return namaMataKuliah; }
    public short  getSks()            { return sks; }
    public short  getSemesterKe()     { return semesterKe; }

    public void setIdMataKuliah(String idMataKuliah)     { this.idMataKuliah = idMataKuliah; }
    public void setIdProgramStudi(String idProgramStudi) { this.idProgramStudi = idProgramStudi; }
    public void setNamaMataKuliah(String namaMataKuliah) { this.namaMataKuliah = namaMataKuliah; }
    public void setSks(short sks)                        { this.sks = sks; }
    public void setSemesterKe(short semesterKe)          { this.semesterKe = semesterKe; }

    public boolean isSksValid() {
        return sks == 1 || sks == 2 || sks == 3;
    }

    public boolean isSemesterValid() {
        return semesterKe >= 1 && semesterKe <= 14;
    }

    // ===================== TAMBAH MATA KULIAH =====================
    public static void tambahMataKuliah() {
        Scanner input = new Scanner(System.in);

        System.out.println("=== DATA MATA KULIAH ===\n");
        System.out.print("ID Mata Kuliah: ");   String idMK     = input.nextLine().trim();
        System.out.print("ID Program Studi: "); String idPS     = input.nextLine().trim();
        System.out.print("Nama Mata Kuliah: "); String nama     = input.nextLine().trim();
        System.out.print("Jumlah SKS: ");       String sks      = input.nextLine().trim();
        System.out.print("Semester: ");         String semester = input.nextLine().trim();

        if (idMK.isEmpty() || idPS.isEmpty() || nama.isEmpty()
                || sks.isEmpty() || semester.isEmpty()) {
            System.out.println("Semua data wajib diisi!");
            return;
        }

        short sksVal, semVal;
        try { sksVal = Short.parseShort(sks); }
        catch (NumberFormatException e) { System.out.println("SKS harus berupa angka!"); return; }

        try { semVal = Short.parseShort(semester); }
        catch (NumberFormatException e) { System.out.println("Semester harus berupa angka!"); return; }

        MataKuliah mk = new MataKuliah(idMK, idPS, nama, sksVal, semVal);

        if (!mk.isSksValid()) {
            System.out.println("SKS harus 1, 2, atau 3!"); return;
        }
        if (!mk.isSemesterValid()) {
            System.out.println("Semester harus antara 1 sampai 14!"); return;
        }

        try {
            Connection conn = Koneksi.connect();
            if (conn == null) { System.out.println("Koneksi gagal!"); return; }

            // Cek duplikat ID
            try (PreparedStatement psCek = conn.prepareStatement(
                    "SELECT COUNT(*) FROM b1.mata_kuliah WHERE id_mata_kuliah = ?")) {
                psCek.setString(1, mk.getIdMataKuliah());
                ResultSet rs = psCek.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("ID Mata Kuliah sudah digunakan!"); return;
                }
            }

            String query =
                "INSERT INTO b1.mata_kuliah(id_mata_kuliah, id_program_studi, nama_mata_kuliah, sks, semester_ke) " +
                "VALUES(?, ?, ?, ?::b1.sks_enum, ?)";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, mk.getIdMataKuliah());
            ps.setString(2, mk.getIdProgramStudi());
            ps.setString(3, mk.getNamaMataKuliah());
            ps.setString(4, String.valueOf(mk.getSks()));
            ps.setShort(5, mk.getSemesterKe());

            int baris = ps.executeUpdate();
            if (baris > 0) {
                System.out.println("\n=== DATA MATA KULIAH BERHASIL DISIMPAN ===");
                System.out.println("ID MK    : " + mk.getIdMataKuliah());
                System.out.println("Nama MK  : " + mk.getNamaMataKuliah());
                System.out.println("SKS      : " + mk.getSks());
                System.out.println("Semester : " + mk.getSemesterKe());
            }

        } catch (Exception e) {
            System.out.println("=== Terjadi kesalahan pada data! ===\n" + e.getMessage());
            e.printStackTrace();
        }
    }
}