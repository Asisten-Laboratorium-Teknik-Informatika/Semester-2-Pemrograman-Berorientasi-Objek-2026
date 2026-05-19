package com.penjualan.entity;

public class Supplier {
    private int id;
    private String namaSupplier;
    private String alamat;
    private String telepon;
    private String email;
    
    public Supplier() {}
    
    public Supplier(int id, String namaSupplier, String alamat, String telepon, String email) {
        this.id = id;
        this.namaSupplier = namaSupplier;
        this.alamat = alamat;
        this.telepon = telepon;
        this.email = email;
    }
    
    // Getter Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNamaSupplier() { return namaSupplier; }
    public void setNamaSupplier(String namaSupplier) { this.namaSupplier = namaSupplier; }
    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
    public String getTelepon() { return telepon; }
    public void setTelepon(String telepon) { this.telepon = telepon; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}