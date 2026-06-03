package SDM_PegawaiPayroll;

public class Tunjangan {

    private int idTunjangan;
    private String namaTunjangan;
    private double jumlah;

    public Tunjangan(
            int idTunjangan,
            String namaTunjangan,
            double jumlah) {

        this.idTunjangan = idTunjangan;
        this.namaTunjangan = namaTunjangan;
        this.jumlah = jumlah;
    }

    public int getIdTunjangan() {
        return idTunjangan;
    }

    public void setIdTunjangan(int idTunjangan) {
        this.idTunjangan = idTunjangan;
    }

    public String getNamaTunjangan() {
        return namaTunjangan;
    }

    public void setNamaTunjangan(String namaTunjangan) {
        this.namaTunjangan = namaTunjangan;
    }

    public double getJumlah() {
        return jumlah;
    }

    public void setJumlah(double jumlah) {
        this.jumlah = jumlah;
    }

}
