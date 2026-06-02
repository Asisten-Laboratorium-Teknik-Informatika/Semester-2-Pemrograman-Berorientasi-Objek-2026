package model;

public class Presensi {
    private int idKrs;
    private int idJadwal;
    private java.sql.Date tanggal;
    private String status;
    private String keterangan;

    public int getIdKrs() { return idKrs; }
    public void setIdKrs(int i) { this.idKrs = i; }

    public int getIdJadwal() { return idJadwal; }
    public void setIdJadwal(int i) { this.idJadwal = i; }

    public java.sql.Date getTanggal() { return tanggal; }
    public void setTanggal(java.sql.Date d) { this.tanggal = d; }

    public String getStatus() { return status; }
    public void setStatus(String s) { this.status = s; }

    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String k) { this.keterangan = k; }
}