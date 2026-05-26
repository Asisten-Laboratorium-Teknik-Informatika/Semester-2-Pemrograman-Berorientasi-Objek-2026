package model;
public class Pelanggan {
    private String idPelanggan;
    private String nama;
    private String email;
    private String telepon;

    public Pelanggan(String idPelanggan, String nama, String email, String telepon) {
        this.idPelanggan = idPelanggan;
        this.nama = nama;
        this.email = email;
        this.telepon = telepon;
    }

    public String getIdPelanggan() { return idPelanggan; }
    public void setIdPelanggan(String idPelanggan) { this.idPelanggan = idPelanggan; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelepon() { return telepon; }
    public void setTelepon(String telepon) { this.telepon = telepon; }
}