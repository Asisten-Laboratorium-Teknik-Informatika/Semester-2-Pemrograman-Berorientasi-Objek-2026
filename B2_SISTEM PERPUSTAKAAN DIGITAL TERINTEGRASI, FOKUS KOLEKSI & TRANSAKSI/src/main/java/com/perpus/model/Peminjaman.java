package src.main.java.com.perpus.model;

import java.time.LocalDate;

public class Peminjaman {
    private String kodePeminjaman;
    private Anggota anggota;
    private Buku buku;
    private LocalDate tanggalPinjam;
    private LocalDate tanggalJatuhTempo;
    private LocalDate tanggalKembali;
    private String statusPeminjaman; 
    private double denda;

    public Peminjaman(String kodePeminjaman, Anggota anggota, Buku buku) {
        this.kodePeminjaman = kodePeminjaman;
        this.anggota = anggota;
        this.buku = buku;
        this.tanggalPinjam = LocalDate.now();
        // Durasi peminjaman default adalah 7 hari
        this.tanggalJatuhTempo = LocalDate.now().plusDays(7); 
        this.statusPeminjaman = "Dipinjam";
        this.denda = 0.0;
    }

    public String getKodePeminjaman() { return kodePeminjaman; }
    public Anggota getAnggota() { return anggota; }
    public Buku getBuku() { return buku; }
    public LocalDate getTanggalPinjam() { return tanggalPinjam; }
    public LocalDate getTanggalJatuhTempo() { return tanggalJatuhTempo; }
    public LocalDate getTanggalKembali() { return tanggalKembali; }
    public String getStatusPeminjaman() { return statusPeminjaman; }
    public double getDenda() { return denda; }

    public void setTanggalKembali(LocalDate tanggalKembali) { this.tanggalKembali = tanggalKembali; }
    public void setStatusPeminjaman(String statusPeminjaman) { this.statusPeminjaman = statusPeminjaman; }
    public void setDenda(double denda) { this.denda = denda; }
}
