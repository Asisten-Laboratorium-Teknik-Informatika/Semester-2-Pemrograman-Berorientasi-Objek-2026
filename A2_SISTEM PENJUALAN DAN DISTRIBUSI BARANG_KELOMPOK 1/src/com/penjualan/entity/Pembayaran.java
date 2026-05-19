package com.penjualan.entity;

import java.time.LocalDate;

public class Pembayaran {
    private int id;
    private Transaksi transaksi;
    private String metode;
    private double jumlahDibayar;
    private double kembalian;
    private LocalDate tglPembayaran;
    
    public Pembayaran() {}
    
    public Pembayaran(int id, Transaksi transaksi, String metode, double jumlahDibayar, double kembalian, LocalDate tglPembayaran) {
        this.id = id;
        this.transaksi = transaksi;
        this.metode = metode;
        this.jumlahDibayar = jumlahDibayar;
        this.kembalian = kembalian;
        this.tglPembayaran = tglPembayaran;
    }
    
    // Getter Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Transaksi getTransaksi() { return transaksi; }
    public void setTransaksi(Transaksi transaksi) { this.transaksi = transaksi; }
    public String getMetode() { return metode; }
    public void setMetode(String metode) { this.metode = metode; }
    public double getJumlahDibayar() { return jumlahDibayar; }
    public void setJumlahDibayar(double jumlahDibayar) { this.jumlahDibayar = jumlahDibayar; }
    public double getKembalian() { return kembalian; }
    public void setKembalian(double kembalian) { this.kembalian = kembalian; }
    public LocalDate getTglPembayaran() { return tglPembayaran; }
    public void setTglPembayaran(LocalDate tglPembayaran) { this.tglPembayaran = tglPembayaran; }
}