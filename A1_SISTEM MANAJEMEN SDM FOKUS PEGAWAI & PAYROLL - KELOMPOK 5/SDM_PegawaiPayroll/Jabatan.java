package SDM_PegawaiPayroll;

public class Jabatan {

    private int idJabatan;
    private int idDepartemen;
    private String namaJabatan;
    private double gajiPokok;

    public Jabatan(int idJabatan, int idDepartemen, String namaJabatan,  double gajiPokok) {
        this.idJabatan = idJabatan;
        this.idDepartemen = idDepartemen;
        this.namaJabatan =  namaJabatan;
        this.gajiPokok = gajiPokok;
    }

    public int getIdJabatan() {
        return idJabatan;
    }

    public void setIdJabatan(int idJabatan) {
        this.idJabatan = idJabatan;
    }

    public int getIdDepartemen() {
        return idDepartemen;
    }

    public void setIdDepartemen(int idDepartemen) {
        this.idDepartemen = idDepartemen;
    }

    public String getNamaJabatan() {
        return namaJabatan;
    }

    public void setNamaJabatan(String namaJabatan) {
        this.namaJabatan = namaJabatan;
    }

    public double getGajiPokok() {
        return gajiPokok;
    }

    public void setGajiPokok(double gajiPokok) {
        this.gajiPokok = gajiPokok;
    }

}
