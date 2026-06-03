package SDM_PegawaiPayroll;

public class PegawaiPotongan {

    private String idPegawaiPotongan;
    private int idPegawai;
    private int idPotongan;

    public PegawaiPotongan(
            String idPegawaiPotongan,
            int idPegawai,
            int idPotongan) {

        this.idPegawaiPotongan =
                idPegawaiPotongan;

        this.idPegawai = idPegawai;

        this.idPotongan = idPotongan;
    }

    public String getIdPegawaiPotongan() {
        return idPegawaiPotongan;
    }

    public void setIdPegawaiPotongan(
            String idPegawaiPotongan) {

        this.idPegawaiPotongan =
                idPegawaiPotongan;
    }

    public int getIdPegawai() {
        return idPegawai;
    }

    public void setIdPegawai(
            int idPegawai) {

        this.idPegawai = idPegawai;
    }

    public int getIdPotongan() {
        return idPotongan;
    }

    public void setIdPotongan(
            int idPotongan) {

        this.idPotongan = idPotongan;
    }

}
