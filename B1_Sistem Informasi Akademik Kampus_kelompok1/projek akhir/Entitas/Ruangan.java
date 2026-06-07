package Entitas;

import Fitur.Tampilan;
import Koneksi.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Class Ruangan.
 *
 * ENCAPSULATION: Semua field private dengan getter/setter.
 */
public class Ruangan {

    // ===================== FIELD =====================
    private String idRuangan;
    private String namaRuangan;
    private String gedung;
    private short  kapasitas;

    // ===================== CONSTRUCTOR =====================
    public Ruangan() {}

    public Ruangan(String idRuangan, String namaRuangan, String gedung, short kapasitas) {
        this.idRuangan   = idRuangan;
        this.namaRuangan = namaRuangan;
        this.gedung      = gedung;
        this.kapasitas   = kapasitas;
    }

    // ===================== GETTER & SETTER =====================
    public String getIdRuangan()   { return idRuangan; }
    public String getNamaRuangan() { return namaRuangan; }
    public String getGedung()      { return gedung; }
    public short  getKapasitas()   { return kapasitas; }

    public void setIdRuangan(String v)   { this.idRuangan = v; }
    public void setNamaRuangan(String v) { this.namaRuangan = v; }
    public void setGedung(String v)      { this.gedung = v; }
    public void setKapasitas(short v)    { this.kapasitas = v; }

    // ===================== VALIDASI =====================
    public boolean isKapasitasValid() {
        return kapasitas > 0 && kapasitas <= 150;
    }

    // ===================== TAMBAH RUANGAN =====================
    /**
     * Dipanggil dari Dashboard (Admin → Data Master).
     * Menggantikan main() lama.
     */
    public static void tambahRuangan() {
        System.out.println("=== DATA RUANGAN ===\n");
        System.out.print("ID Ruangan : "); String idR  = Fitur.Dashboard.input.nextLine().trim();
        System.out.print("Nama Ruang : "); String nama = Fitur.Dashboard.input.nextLine().trim();
        System.out.print("Gedung     : "); String ged  = Fitur.Dashboard.input.nextLine().trim();
        System.out.print("Kapasitas  : "); String kap  = Fitur.Dashboard.input.nextLine().trim();

        if (idR.isEmpty() || nama.isEmpty() || ged.isEmpty() || kap.isEmpty()) {
            Tampilan.gagal("Semua data wajib diisi!"); return;
        }

        short kapVal;
        try { kapVal = Short.parseShort(kap); }
        catch (NumberFormatException e) { Tampilan.gagal("Kapasitas harus berupa angka!"); return; }

        Ruangan ruangan = new Ruangan(idR, nama, ged, kapVal);
        if (!ruangan.isKapasitasValid()) {
            Tampilan.gagal("Kapasitas harus antara 1 sampai 150!"); return;
        }

        try (Connection conn = Koneksi.connect()) {
            if (conn == null) { Tampilan.gagal("Koneksi gagal!"); return; }

            // Cek ID duplikat
            PreparedStatement psCek = conn.prepareStatement(
                "SELECT COUNT(*) FROM b1.ruangan WHERE id_ruangan = ?");
            psCek.setString(1, ruangan.getIdRuangan());
            ResultSet rs = psCek.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                Tampilan.gagal("ID Ruangan sudah digunakan!"); return;
            }

            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO b1.ruangan(id_ruangan, nama_ruangan, gedung, kapasitas) " +
                "VALUES(?, ?, ?, ?)");
            ps.setString(1, ruangan.getIdRuangan());
            ps.setString(2, ruangan.getNamaRuangan());
            ps.setString(3, ruangan.getGedung());
            ps.setShort(4,  ruangan.getKapasitas());

            if (ps.executeUpdate() > 0) {
                Tampilan.sukses("\n=== DATA RUANGAN BERHASIL DISIMPAN ===");
                System.out.println("ID Ruangan   : " + ruangan.getIdRuangan());
                System.out.println("Nama Ruangan : " + ruangan.getNamaRuangan());
                System.out.println("Gedung       : " + ruangan.getGedung());
                System.out.println("Kapasitas    : " + ruangan.getKapasitas());
            } else {
                Tampilan.gagal("Data gagal disimpan.");
            }

        } catch (Exception e) {
            Tampilan.gagal("Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }
}