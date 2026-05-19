package com.penjualan.entity;

public class DetailTransaksi {
    private int id;
    private Transaksi transaksi;
    private Barang barang;
    private int jumlah;
    private double hargaSatuan;
    private double subtotal;
    
    public DetailTransaksi() {}
    
    public DetailTransaksi(int id, Transaksi transaksi, Barang barang, int jumlah, double hargaSatuan, double subtotal) {
        this.id = id;
        this.transaksi = transaksi;
        this.barang = barang;
        this.jumlah = jumlah;
        this.hargaSatuan = hargaSatuan;
        this.subtotal = subtotal;
    }
    
    // Getter Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Transaksi getTransaksi() { return transaksi; }
    public void setTransaksi(Transaksi transaksi) { this.transaksi = transaksi; }
    public Barang getBarang() { return barang; }
    public void setBarang(Barang barang) { this.barang = barang; }
    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }
    public double getHargaSatuan() { return hargaSatuan; }
    public void setHargaSatuan(double hargaSatuan) { this.hargaSatuan = hargaSatuan; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}