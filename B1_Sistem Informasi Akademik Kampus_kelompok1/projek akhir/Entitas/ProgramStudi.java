package Entitas;

import Fitur.Tampilan;
import Koneksi.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ProgramStudi {

    private String idProgramStudi;
    private String namaProgramStudi;
    private String fakultas;
    private String jenjang;
    private String akreditasi;

    public ProgramStudi() {}

    public ProgramStudi(String idProgramStudi, String namaProgramStudi,
                        String fakultas, String jenjang, String akreditasi) {
        this.idProgramStudi   = idProgramStudi;
        this.namaProgramStudi = namaProgramStudi;
        this.fakultas         = fakultas;
        this.jenjang          = jenjang;
        this.akreditasi       = akreditasi;
    }

    public String getIdProgramStudi()   { return idProgramStudi; }
    public String getNamaProgramStudi() { return namaProgramStudi; }
    public String getFakultas()         { return fakultas; }
    public String getJenjang()          { return jenjang; }
    public String getAkreditasi()       { return akreditasi; }

    public void setIdProgramStudi(String v)   { this.idProgramStudi = v; }
    public void setNamaProgramStudi(String v) { this.namaProgramStudi = v; }
    public void setFakultas(String v)         { this.fakultas = v; }
    public void setJenjang(String v)          { this.jenjang = v; }
    public void setAkreditasi(String v)       { this.akreditasi = v; }

    public boolean isJenjangValid() {
        return jenjang != null &&
            (jenjang.equals("D3") || jenjang.equals("D4") ||
             jenjang.equals("S1") || jenjang.equals("S2") || jenjang.equals("S3"));
    }

    public boolean isAkreditasiValid() {
        return akreditasi != null &&
            (akreditasi.equalsIgnoreCase("Unggul")      ||
             akreditasi.equalsIgnoreCase("Baik sekali") ||
             akreditasi.equalsIgnoreCase("Baik")        ||
             akreditasi.equalsIgnoreCase("Buruk"));
    }

    // ===================== TAMBAH PROGRAM STUDI =====================
    public static void tambahProgramStudi() {
        System.out.println("=== DATA PROGRAM STUDI ===\n");
        System.out.print("ID Program Studi                               : "); String id   = Fitur.Dashboard.input.nextLine().trim();
        System.out.print("Nama Program Studi                             : "); String nama = Fitur.Dashboard.input.nextLine().trim();
        System.out.print("Nama Fakultas                                  : "); String fak  = Fitur.Dashboard.input.nextLine().trim();
        System.out.print("Jenjang (D3/D4/S1/S2/S3)                      : "); String jen  = Fitur.Dashboard.input.nextLine().trim().toUpperCase();
        System.out.print("Akreditasi (Unggul/Baik sekali/Baik/Buruk)    : "); String akr  = Fitur.Dashboard.input.nextLine().trim();

        if (id.isEmpty() || nama.isEmpty() || fak.isEmpty() || jen.isEmpty() || akr.isEmpty()) {
            Tampilan.gagal("Semua data wajib diisi!"); return;
        }
        if (id.length() > 10)   { Tampilan.gagal("ID Program Studi maksimal 10 karakter!"); return; }
        if (nama.length() > 40) { Tampilan.gagal("Nama Program Studi maksimal 40 karakter!"); return; }
        if (fak.length() > 60)  { Tampilan.gagal("Nama Fakultas maksimal 60 karakter!"); return; }

        ProgramStudi ps = new ProgramStudi(id, nama, fak, jen, akr);
        if (!ps.isJenjangValid())    { Tampilan.gagal("Jenjang tidak valid!"); return; }
        if (!ps.isAkreditasiValid()) { Tampilan.gagal("Akreditasi tidak valid!"); return; }

        try (Connection conn = Koneksi.connect()) {
            if (conn == null) { Tampilan.gagal("Koneksi gagal!"); return; }

            String query =
                "INSERT INTO b1.program_studi(id_program_studi, nama_program_studi, " +
                "fakultas, jenjang, akreditasi) " +
                "VALUES(?, ?, ?, ?::b1.jenjang_enum, ?::akreditasi_enum)";

            PreparedStatement psSql = conn.prepareStatement(query);
            psSql.setString(1, ps.getIdProgramStudi());
            psSql.setString(2, ps.getNamaProgramStudi());
            psSql.setString(3, ps.getFakultas());
            psSql.setString(4, ps.getJenjang());
            psSql.setString(5, ps.getAkreditasi());

            if (psSql.executeUpdate() > 0) {
                Tampilan.sukses("\n=== DATA BERHASIL DISIMPAN ===");
                System.out.println("ID Program Studi : " + ps.getIdProgramStudi());
                System.out.println("Program Studi    : " + ps.getNamaProgramStudi());
            } else {
                Tampilan.gagal("Data gagal disimpan.");
            }

        } catch (Exception e) {
            Tampilan.gagal("Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }
}