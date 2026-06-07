package Entitas;

public abstract class Pengguna {

    private String nama;
    private String email;
    private String jenisKelamin;
    private String noHp;

    public Pengguna() {}

    public Pengguna(String nama, String email, String jenisKelamin, String noHp) {
        this.nama         = nama;
        this.email        = email;
        this.jenisKelamin = jenisKelamin;
        this.noHp         = noHp;
    }

    public String getNama()         { return nama; }
    public String getEmail()        { return email; }
    public String getJenisKelamin() { return jenisKelamin; }
    public String getNoHp()         { return noHp != null ? noHp : "-"; }

    public void setNama(String nama)                 { this.nama = nama; }
    public void setEmail(String email)               { this.email = email; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }
    public void setNoHp(String noHp)                 { this.noHp = noHp; }

    public abstract void tampilkanDataDiri();

    public abstract String getIdentitas();

    public abstract String getRole();

    public boolean isEmailValid(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    @Override
    public String toString() {
        return "[" + getRole() + "] " + nama + " (" + getIdentitas() + ")";
    }
}