package com.penjualan.entity;

public class Pelanggan {
    private int id;
    private String nama;
    private String alamat;
    private String telepon;
    private String email;
    private int poin;
    
    public Pelanggan() {}
    
    public Pelanggan(int id, String nama, String alamat, String telepon, String email, int poin) {
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.telepon = telepon;
        this.email = email;
        this.poin = poin;
    }
    
    // Getter Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
    public String getTelepon() { return telepon; }
    public void setTelepon(String telepon) { this.telepon = telepon; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getPoin() { return poin; }
    public void setPoin(int poin) { this.poin = poin; }
}