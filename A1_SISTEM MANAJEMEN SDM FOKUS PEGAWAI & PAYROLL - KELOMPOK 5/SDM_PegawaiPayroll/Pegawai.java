package SDM_PegawaiPayroll;

public class Pegawai {

    private int idPegawai;
    private int idJabatan;
    private String namaPegawai;
    private String alamat;
    private String noHp;
    private String email;
    private String tanggalMasuk;

    public Pegawai(
            int idPegawai,
            int idJabatan,
            String namaPegawai,
            String alamat,
            String noHp,
            String email,
            String tanggalMasuk) {

        this.idPegawai = idPegawai;
        this.idJabatan = idJabatan;
        this.namaPegawai = namaPegawai;
        this.alamat = alamat;
        this.noHp = noHp;
        this.email = email;
        this.tanggalMasuk = tanggalMasuk;
    }

    public int getIdPegawai() {
        return idPegawai;
    }

    public void setIdPegawai(int idPegawai) {
        this.idPegawai = idPegawai;
    }

    public int getIdJabatan() {
        return idJabatan;
    }

    public void setIdJabatan(int idJabatan) {
        this.idJabatan = idJabatan;
    }

    public String getNamaPegawai() {
        return namaPegawai;
    }

    public void setNamaPegawai(String namaPegawai) {
        this.namaPegawai = namaPegawai;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTanggalMasuk() {
        return tanggalMasuk;
    }

    public void setTanggalMasuk(
            String tanggalMasuk) {

        this.tanggalMasuk =
                tanggalMasuk;
    }

}
