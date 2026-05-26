package model;
import java.sql.Date;

public class Pengiriman {
    private String idPengiriman;
    private String idPenjualan;
    private String idKurir;
    private Date tanggalKirim;
    private String statusPengiriman;

    public Pengiriman(String idPengiriman, String idPenjualan, String idKurir, Date tanggalKirim, String statusPengiriman) {
        this.idPengiriman = idPengiriman;
        this.idPenjualan = idPenjualan;
        this.idKurir = idKurir;
        this.tanggalKirim = tanggalKirim;
        this.statusPengiriman = statusPengiriman;
    }

    public String getIdPengiriman() { return idPengiriman; }
    public void setIdPengiriman(String idPengiriman) { this.idPengiriman = idPengiriman; }

    public String getIdPenjualan() { return idPenjualan; }
    public void setIdPenjualan(String idPenjualan) { this.idPenjualan = idPenjualan; }

    public String getIdKurir() { return idKurir; }
    public void setIdKurir(String idKurir) { this.idKurir = idKurir; }

    public Date getTanggalKirim() { return tanggalKirim; }
    public void setTanggalKirim(Date tanggalKirim) { this.tanggalKirim = tanggalKirim; }

    public String getStatusPengiriman() { return statusPengiriman; }
    public void setStatusPengiriman(String statusPengiriman) { this.statusPengiriman = statusPengiriman; }
}