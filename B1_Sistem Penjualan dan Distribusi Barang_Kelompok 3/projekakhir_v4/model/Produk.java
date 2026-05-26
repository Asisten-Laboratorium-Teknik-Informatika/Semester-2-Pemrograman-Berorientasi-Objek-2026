package model;
public class Produk {
    private String idProduk;
    private String namaProduk;
    private String idKategori;
    private double harga;
    private int stok;

    public Produk(String idProduk, String namaProduk, String idKategori, double harga, int stok) {
        this.idProduk = idProduk;
        this.namaProduk = namaProduk;
        this.idKategori = idKategori;
        this.harga = harga;
        this.stok = stok;
    }

    public String getIdProduk() { return idProduk; }
    public void setIdProduk(String idProduk) { this.idProduk = idProduk; }

    public String getNamaProduk() { return namaProduk; }
    public void setNamaProduk(String namaProduk) { this.namaProduk = namaProduk; }

    public String getIdKategori() { return idKategori; }
    public void setIdKategori(String idKategori) { this.idKategori = idKategori; }

    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }

    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }
}