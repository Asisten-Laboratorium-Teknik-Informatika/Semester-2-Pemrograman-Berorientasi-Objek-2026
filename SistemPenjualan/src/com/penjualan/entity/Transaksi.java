package com.penjualan.entity;

import java.time.LocalDate;

public class Transaksi {
    private int id;
    private Pelanggan pelanggan;
    private User user;
    private LocalDate tglTransaksi;
    private double totalHarga;
    private String status;
    
    public Transaksi() {}
    
    public Transaksi(int id, Pelanggan pelanggan, User user, LocalDate tglTransaksi, double totalHarga, String status) {
        this.id = id;
        this.pelanggan = pelanggan;
        this.user = user;
        this.tglTransaksi = tglTransaksi;
        this.totalHarga = totalHarga;
        this.status = status;
    }
    
    // Getter Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Pelanggan getPelanggan() { return pelanggan; }
    public void setPelanggan(Pelanggan pelanggan) { this.pelanggan = pelanggan; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDate getTglTransaksi() { return tglTransaksi; }
    public void setTglTransaksi(LocalDate tglTransaksi) { this.tglTransaksi = tglTransaksi; }
    public double getTotalHarga() { return totalHarga; }
    public void setTotalHarga(double totalHarga) { this.totalHarga = totalHarga; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}