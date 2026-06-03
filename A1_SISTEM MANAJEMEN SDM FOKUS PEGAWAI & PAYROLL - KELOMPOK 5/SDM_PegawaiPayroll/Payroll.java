package SDM_PegawaiPayroll;

public class Payroll {

    private int idPayroll;
    private int idPegawai;
    private String periode;
    private double gajiPokok;
    private double totalTunjangan;
    private double totalPotongan;
    private int totalJamLembur;
    private double totalUpahLembur;
    private double totalGaji;

    public Payroll(int idPayroll, int idPegawai,
            String periode, double gajiPokok,
            double totalTunjangan,
            double totalPotongan,
            int totalJamLembur,
            double totalUpahLembur,
            double totalGaji) {

        this.idPayroll = idPayroll;
        this.idPegawai = idPegawai;
        this.periode = periode;
        this.gajiPokok = gajiPokok;
        this.totalTunjangan = totalTunjangan;
        this.totalPotongan = totalPotongan;
        this.totalJamLembur = totalJamLembur;
        this.totalUpahLembur = totalUpahLembur;
        this.totalGaji = totalGaji;
    }

    public int getIdPayroll() {
        return idPayroll;
    }

    public void setIdPayroll(int idPayroll) {
        this.idPayroll = idPayroll;
    }

    public int getIdPegawai() {
        return idPegawai;
    }

    public void setIdPegawai(int idPegawai) {
        this.idPegawai = idPegawai;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public double getGajiPokok() {
        return gajiPokok;
    }

    public void setGajiPokok(double gajiPokok) {
        this.gajiPokok = gajiPokok;
    }

    public double getTotalTunjangan() {
        return totalTunjangan;
    }

    public void setTotalTunjangan(double totalTunjangan) {
        this.totalTunjangan = totalTunjangan;
    }

    public double getTotalPotongan() {
        return totalPotongan;
    }

    public void setTotalPotongan(double totalPotongan) {
        this.totalPotongan = totalPotongan;
    }

    public int getTotalJamLembur() {
        return totalJamLembur;
    }

    public void setTotalJamLembur(int totalJamLembur) {
        this.totalJamLembur = totalJamLembur;
    }

    public double getTotalUpahLembur() {
        return totalUpahLembur;
    }

    public void setTotalUpahLembur(double totalUpahLembur) {
        this.totalUpahLembur = totalUpahLembur;
    }

    public double getTotalGaji() {
        return totalGaji;
    }

    public void setTotalGaji(double totalGaji) {
        this.totalGaji = totalGaji;
    }
}
