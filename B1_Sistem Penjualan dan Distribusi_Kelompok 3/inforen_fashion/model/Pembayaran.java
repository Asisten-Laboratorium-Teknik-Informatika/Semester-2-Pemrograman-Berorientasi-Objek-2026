package model;

import java.util.Date;

public class Pembayaran {
    private int idPembayaran;
    private int idPesanan;       
    private Date tanggalBayar;
    private double jumlahBayar;
    private String metodePembayaran; 
    private String statusPembayaran; 

    public Pembayaran(int idPembayaran, int idPesanan, Date tanggalBayar, double jumlahBayar, String metodePembayaran, String statusPembayaran) {
        this.idPembayaran = idPembayaran;
        this.idPesanan = idPesanan;
        this.tanggalBayar = tanggalBayar;
        this.jumlahBayar = jumlahBayar;
        this.metodePembayaran = metodePembayaran;
        this.statusPembayaran = statusPembayaran;
    }

    public int getIdPembayaran() { return idPembayaran; }
    public int getIdPesanan() { return idPesanan; }
    public Date getTanggalBayar() { return tanggalBayar; }
    public double getJumlahBayar() { return jumlahBayar; }
    public String getMetodePembayaran() { return metodePembayaran; }
    public String getStatusPembayaran() { return statusPembayaran; }
    public void setStatusPembayaran(String statusPembayaran) { this.statusPembayaran = statusPembayaran; }
}