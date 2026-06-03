package SDM_PegawaiPayroll;

import java.sql.Date;

public class Absensi {

    private int idAbsensi;
    private int idPegawai;
    private Date tanggal;
    private String status;

    public Absensi(int idAbsensi,
                   int idPegawai,
                   Date tanggal,
                   String status) {

        this.idAbsensi = idAbsensi;
        this.idPegawai = idPegawai;
        this.tanggal = tanggal;
        this.status = status;
    }

    public int getIdAbsensi() {
        return idAbsensi;
    }

    public void setIdAbsensi(int idAbsensi) {
        this.idAbsensi = idAbsensi;
    }

    public int getIdPegawai() {
        return idPegawai;
    }

    public void setIdPegawai(int idPegawai) {
        this.idPegawai = idPegawai;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
