package SDM_PegawaiPayroll;

import java.sql.Timestamp;

public class LaporanPayroll {

    private int idLaporan;
    private String periode;
    private Timestamp tanggalCetak;
    private String keterangan;

    public LaporanPayroll(
            int idLaporan,
            String periode,
            Timestamp tanggalCetak,
            String keterangan) {

        this.idLaporan = idLaporan;
        this.periode = periode;
        this.tanggalCetak = tanggalCetak;
        this.keterangan = keterangan;
    }

    public int getIdLaporan() {
        return idLaporan;
    }

    public void setIdLaporan(int idLaporan) {
        this.idLaporan = idLaporan;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public Timestamp getTanggalCetak() {
        return tanggalCetak;
    }

    public void setTanggalCetak(
            Timestamp tanggalCetak) {

        this.tanggalCetak = tanggalCetak;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(
            String keterangan) {

        this.keterangan = keterangan;
    }

}
    
