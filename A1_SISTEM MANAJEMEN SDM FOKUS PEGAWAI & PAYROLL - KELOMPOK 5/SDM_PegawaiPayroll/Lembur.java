package SDM_PegawaiPayroll;

public class Lembur {

    private int idLembur;
    private int idPegawai;
    private String tanggalLembur;
    private int jamLembur;
    private String jenisHari;
    private double upahPerJam;
    private double upahLembur;

    public Lembur(
            int idLembur,
            int idPegawai,
            String tanggalLembur,
            int jamLembur,
            String jenisHari,
            double upahPerJam,
            double upahLembur) {

        this.idLembur = idLembur;
        this.idPegawai = idPegawai;
        this.tanggalLembur = tanggalLembur;
        this.jamLembur = jamLembur;
        this.jenisHari = jenisHari;
        this.upahPerJam = upahPerJam;
        this.upahLembur = upahLembur;
    }

    public int getIdLembur() {
        return idLembur;
    }

    public void setIdLembur(int idLembur) {
        this.idLembur = idLembur;
    }

    public int getIdPegawai() {
        return idPegawai;
    }

    public void setIdPegawai(int idPegawai) {
        this.idPegawai = idPegawai;
    }

    public String getTanggalLembur() {
        return tanggalLembur;
    }

    public void setTanggalLembur(String tanggalLembur) {
        this.tanggalLembur = tanggalLembur;
    }

    public int getJamLembur() {
        return jamLembur;
    }

    public void setJamLembur(int jamLembur) {
        this.jamLembur = jamLembur;
    }

    public String getJenisHari() {
        return jenisHari;
    }

    public void setJenisHari(String jenisHari) {
        this.jenisHari = jenisHari;
    }

    public double getUpahPerJam() {
        return upahPerJam;
    }

    public void setUpahPerJam(double upahPerJam) {
        this.upahPerJam = upahPerJam;
    }

    public double getUpahLembur() {
        return upahLembur;
    }

    public void setUpahLembur(double upahLembur) {
        this.upahLembur = upahLembur;
    }

}
