package model;

public class DetailPesanan {
    private int idDetail;
    private int idPesanan; 
    private int idProduk;  
    private int jumlah;
    private double subtotal;

    public DetailPesanan(int idDetail, int idPesanan, int idProduk, int jumlah, double subtotal) {
        this.idDetail = idDetail;
        this.idPesanan = idPesanan;
        this.idProduk = idProduk;
        this.jumlah = jumlah;
        this.subtotal = subtotal;
    }

    public int getIdDetail() { return idDetail; }
    public int getIdPesanan() { return idPesanan; }
    public int getIdProduk() { return idProduk; }
    public int getJumlah() { return jumlah; }
    public double getSubtotal() { return subtotal; }

    public DetailPesanan(int idDetail, int idProduk, int jumlah) {
        this.idDetail = idDetail;
        this.idProduk = idProduk;
        this.jumlah = jumlah;
    }
}