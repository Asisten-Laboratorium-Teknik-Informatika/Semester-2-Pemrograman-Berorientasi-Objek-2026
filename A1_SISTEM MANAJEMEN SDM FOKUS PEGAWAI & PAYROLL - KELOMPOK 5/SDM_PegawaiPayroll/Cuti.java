package SDM_PegawaiPayroll;

import java.sql.Date;

public class Cuti {

    private int idCuti;
    private int idPegawai;
    private String jenisCuti;
    private Date tanggalMulai;
    private Date tanggalSelesai;
    private int jumlahHari;
    private String alasan;
    private String status;

    public Cuti(
            int idCuti,
            int idPegawai,
            String jenisCuti,
            Date tanggalMulai,
            Date tanggalSelesai,
            int jumlahHari,
            String alasan,
            String status) {

        this.idCuti = idCuti;
        this.idPegawai = idPegawai;
        this.jenisCuti = jenisCuti;
        this.tanggalMulai = tanggalMulai;
        this.tanggalSelesai = tanggalSelesai;
        this.jumlahHari = jumlahHari;
        this.alasan = alasan;
        this.status = status;
    }

    public int getIdCuti() {
        return idCuti;
    }

    public void setIdCuti(int idCuti) {
        this.idCuti = idCuti;
    }

    public int getIdPegawai() {
        return idPegawai;
    }

    public void setIdPegawai(int idPegawai) {
        this.idPegawai = idPegawai;
    }

    public String getJenisCuti() {
        return jenisCuti;
    }

    public void setJenisCuti(String jenisCuti) {
        this.jenisCuti = jenisCuti;
    }

    public Date getTanggalMulai() {
        return tanggalMulai;
    }

    public void setTanggalMulai(Date tanggalMulai) {
        this.tanggalMulai = tanggalMulai;
    }

    public Date getTanggalSelesai() {
        return tanggalSelesai;
    }

    public void setTanggalSelesai(Date tanggalSelesai) {
        this.tanggalSelesai = tanggalSelesai;
    }

    public int getJumlahHari() {
        return jumlahHari;
    }

    public void setJumlahHari(int jumlahHari) {
        this.jumlahHari = jumlahHari;
    }

    public String getAlasan() {
        return alasan;
    }

    public void setAlasan(String alasan) {
        this.alasan = alasan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
