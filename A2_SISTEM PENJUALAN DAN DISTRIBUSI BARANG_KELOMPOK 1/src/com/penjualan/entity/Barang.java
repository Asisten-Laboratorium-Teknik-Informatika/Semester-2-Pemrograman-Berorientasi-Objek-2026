package com.penjualan.entity;

public class Barang {
    private int id;
    private String namaBarang;
    private double harga;
    private int stok;
    private Kategori kategori;
    
    public Barang() {}
    
    public Barang(int id, String namaBarang, double harga, int stok) {
        this.id = id;
        this.namaBarang = namaBarang;
        this.harga = harga;
        this.stok = stok;
    }
    
    public Barang(int id, String namaBarang, double harga, int stok, Kategori kategori) {
        this(id, namaBarang, harga, stok);
        this.kategori = kategori;
    }
    
    // Getter Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNamaBarang() { return namaBarang; }
    public void setNamaBarang(String namaBarang) { this.namaBarang = namaBarang; }
    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }
    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }
    public Kategori getKategori() { return kategori; }
    public void setKategori(Kategori kategori) { this.kategori = kategori; }
}