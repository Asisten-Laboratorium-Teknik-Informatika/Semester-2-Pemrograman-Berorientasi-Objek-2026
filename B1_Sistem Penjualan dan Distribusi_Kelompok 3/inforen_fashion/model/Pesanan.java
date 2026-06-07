package model;

import java.util.Date;

public class Pesanan {
    private int idPesanan;
    private int idPelanggan;
    private Date tanggalPesanan;
    private double totalHarga;
    private String statusPesanan; 

    public Pesanan(int idPesanan, int idPelanggan, Date tanggalPesanan, double totalHarga, String statusPesanan) {
        this.idPesanan = idPesanan;
        this.idPelanggan = idPelanggan;
        this.tanggalPesanan = tanggalPesanan;
        this.totalHarga = totalHarga;
        this.statusPesanan = statusPesanan;
    }

    // Tambahkan ini di file model/Pesanan.java lek
    public void setIdPesanan(int idPesanan) {
        this.idPesanan = idPesanan;
    }

    public int getIdPesanan() { return idPesanan; }
    public int getIdPelanggan() { return idPelanggan; }
    public Date getTanggalPesanan() { return tanggalPesanan; }
    public double getTotalHarga() { return totalHarga; }
    public String getStatusPesanan() { return statusPesanan; }
    public void setStatusPesanan(String statusPesanan) { this.statusPesanan = statusPesanan; }
}