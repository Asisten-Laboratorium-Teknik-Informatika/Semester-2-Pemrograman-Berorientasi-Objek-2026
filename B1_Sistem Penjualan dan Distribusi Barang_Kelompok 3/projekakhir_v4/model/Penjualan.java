package model;
import java.sql.Timestamp;

public class Penjualan {
    private String idPenjualan;
    private Timestamp tanggalPenjualan;
    private String idPelanggan;
    private String idKaryawan;
    private double totalBayar;

    public Penjualan(String idPenjualan, Timestamp tanggalPenjualan, String idPelanggan, String idKaryawan, double totalBayar) {
        this.idPenjualan = idPenjualan;
        this.tanggalPenjualan = tanggalPenjualan;
        this.idPelanggan = idPelanggan;
        this.idKaryawan = idKaryawan;
        this.totalBayar = totalBayar;
    }

    public String getIdPenjualan() { return idPenjualan; }
    public void setIdPenjualan(String idPenjualan) { this.idPenjualan = idPenjualan; }

    public Timestamp getTanggalPenjualan() { return tanggalPenjualan; }
    public void setTanggalPenjualan(Timestamp tanggalPenjualan) { this.tanggalPenjualan = tanggalPenjualan; }

    public String getIdPelanggan() { return idPelanggan; }
    public void setIdPelanggan(String idPelanggan) { this.idPelanggan = idPelanggan; }

    public String getIdKaryawan() { return idKaryawan; }
    public void setIdKaryawan(String idKaryawan) { this.idKaryawan = idKaryawan; }

    public double getTotalBayar() { return totalBayar; }
    public void setTotalBayar(double totalBayar) { this.totalBayar = totalBayar; }
}