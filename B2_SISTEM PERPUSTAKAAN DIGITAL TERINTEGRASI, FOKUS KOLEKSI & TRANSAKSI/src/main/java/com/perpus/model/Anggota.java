package src.main.java.com.perpus.model;

import java.time.LocalDate;

public class Anggota {
    private int idAnggota;
    private String nomorAnggota;
    private String namaLengkap;
    private String status;
    private LocalDate tanggalExpired;

    public Anggota(int idAnggota, String nomorAnggota, String namaLengkap, String status, LocalDate tanggalExpired) {
        this.idAnggota = idAnggota;
        this.nomorAnggota = nomorAnggota;
        this.namaLengkap = namaLengkap;
        this.status = status;
        this.tanggalExpired = tanggalExpired;
    }

    public int getIdAnggota() { return idAnggota; }
    public String getNomorAnggota() { return nomorAnggota; }
    public String getNamaLengkap() { return namaLengkap; }
    public String getStatus() { return status; }
    public LocalDate getTanggalExpired() { return tanggalExpired; }
}
