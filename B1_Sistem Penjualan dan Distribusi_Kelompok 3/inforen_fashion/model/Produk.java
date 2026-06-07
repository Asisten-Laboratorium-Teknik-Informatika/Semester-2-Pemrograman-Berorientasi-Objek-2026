package model;

public class Produk {
    private int idProduk;
    private String namaProduk;
    private double harga; 
    private int stok;
    private int idKategori;

    public Produk(int idProduk, String namaProduk, double harga, int stok, int idKategori) {
        this.idProduk = idProduk;
        this.namaProduk = namaProduk;
        this.harga = harga;
        this.stok = stok;
        this.idKategori = idKategori;
    }

    public int getIdProduk() { return idProduk; }
    public String getNamaProduk() { return namaProduk; }
    public double getHarga() { return harga; }
    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }
    public int getIdKategori() { return idKategori; }
}