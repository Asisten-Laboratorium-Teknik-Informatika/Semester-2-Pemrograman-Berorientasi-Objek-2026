package SDM_PegawaiPayroll;

public class PegawaiTunjangan {

    private String idPegawaiTunjangan;
    private int idPegawai;
    private int idTunjangan;

    public PegawaiTunjangan(
            String idPegawaiTunjangan,
            int idPegawai,
            int idTunjangan) {

        this.idPegawaiTunjangan =
                idPegawaiTunjangan;

        this.idPegawai = idPegawai;

        this.idTunjangan = idTunjangan;
    }

    public String getIdPegawaiTunjangan() {
        return idPegawaiTunjangan;
    }

    public void setIdPegawaiTunjangan(
            String idPegawaiTunjangan) {

        this.idPegawaiTunjangan =
                idPegawaiTunjangan;
    }

    public int getIdPegawai() {
        return idPegawai;
    }

    public void setIdPegawai(
            int idPegawai) {

        this.idPegawai = idPegawai;
    }

    public int getIdTunjangan() {
        return idTunjangan;
    }

    public void setIdTunjangan(
            int idTunjangan) {

        this.idTunjangan = idTunjangan;
    }

}
