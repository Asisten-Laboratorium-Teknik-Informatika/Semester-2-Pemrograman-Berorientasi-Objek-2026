package com.penjualan.entity;

import java.time.LocalDate;

public class Pengiriman {
    private int id;
    private Transaksi transaksi;
    private String kurir;
    private String noResi;
    private LocalDate tglKirim;
    private LocalDate tglTerima;
    private String status;
    
    public Pengiriman() {}
    
    public Pengiriman(int id, Transaksi transaksi, String kurir, String noResi, LocalDate tglKirim, LocalDate tglTerima, String status) {
        this.id = id;
        this.transaksi = transaksi;
        this.kurir = kurir;
        this.noResi = noResi;
        this.tglKirim = tglKirim;
        this.tglTerima = tglTerima;
        this.status = status;
    }
    
    // Getter Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Transaksi getTransaksi() { return transaksi; }
    public void setTransaksi(Transaksi transaksi) { this.transaksi = transaksi; }
    public String getKurir() { return kurir; }
    public void setKurir(String kurir) { this.kurir = kurir; }
    public String getNoResi() { return noResi; }
    public void setNoResi(String noResi) { this.noResi = noResi; }
    public LocalDate getTglKirim() { return tglKirim; }
    public void setTglKirim(LocalDate tglKirim) { this.tglKirim = tglKirim; }
    public LocalDate getTglTerima() { return tglTerima; }
    public void setTglTerima(LocalDate tglTerima) { this.tglTerima = tglTerima; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}