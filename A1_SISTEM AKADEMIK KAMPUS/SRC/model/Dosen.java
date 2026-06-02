package model;

public class Dosen {
    private String nidn;
    private String namaDosen;
    private String jenisKelamin;
    private String email;
    private String noTelp;
    private String jabatan;
    private int idProdi;
    private String password; // kolom baru untuk login dosen

    public String getNidn() { return nidn; }
    public void setNidn(String nidn) { this.nidn = nidn; }

    public String getNamaDosen() { return namaDosen; }
    public void setNamaDosen(String namaDosen) { this.namaDosen = namaDosen; }

    public String getJenisKelamin() { return jenisKelamin; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNoTelp() { return noTelp; }
    public void setNoTelp(String noTelp) { this.noTelp = noTelp; }

    public String getJabatan() { return jabatan; }
    public void setJabatan(String jabatan) { this.jabatan = jabatan; }

    public int getIdProdi() { return idProdi; }
    public void setIdProdi(int idProdi) { this.idProdi = idProdi; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}