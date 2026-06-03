package SDM_PegawaiPayroll;

import java.sql.Date;

public class KalenderLibur {

    private int idLibur;
    private Date tanggalLibur;
    private String namaLibur;
    private String jenisLibur;

    public KalenderLibur(
            int idLibur,
            Date tanggalLibur,
            String namaLibur,
            String jenisLibur) {

        this.idLibur = idLibur;
        this.tanggalLibur = tanggalLibur;
        this.namaLibur = namaLibur;
        this.jenisLibur = jenisLibur;
    }

    public int getIdLibur() {
        return idLibur;
    }

    public void setIdLibur(int idLibur) {
        this.idLibur = idLibur;
    }

    public Date getTanggalLibur() {
        return tanggalLibur;
    }

    public void setTanggalLibur(Date tanggalLibur) {
        this.tanggalLibur = tanggalLibur;
    }

    public String getNamaLibur() {
        return namaLibur;
    }

    public void setNamaLibur(String namaLibur) {
        this.namaLibur = namaLibur;
    }

    public String getJenisLibur() {
        return jenisLibur;
    }

    public void setJenisLibur(String jenisLibur) {
        this.jenisLibur = jenisLibur;
    }

}
