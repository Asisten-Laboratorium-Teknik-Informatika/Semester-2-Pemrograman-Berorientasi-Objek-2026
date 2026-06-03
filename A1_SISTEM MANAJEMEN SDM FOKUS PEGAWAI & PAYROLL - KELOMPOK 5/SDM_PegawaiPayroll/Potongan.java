package SDM_PegawaiPayroll;

public class Potongan {

    private int idPotongan;
    private String namaPotongan;
    private double jumlah;

    public Potongan(
            int idPotongan,
            String namaPotongan,
            double jumlah) {

        this.idPotongan = idPotongan;
        this.namaPotongan = namaPotongan;
        this.jumlah = jumlah;
    }

    public int getIdPotongan() {
        return idPotongan;
    }

    public void setIdPotongan(int idPotongan) {
        this.idPotongan = idPotongan;
    }

    public String getNamaPotongan() {
        return namaPotongan;
    }

    public void setNamaPotongan(String namaPotongan) {
        this.namaPotongan = namaPotongan;
    }

    public double getJumlah() {
        return jumlah;
    }

    public void setJumlah(double jumlah) {
        this.jumlah = jumlah;
    }

}
