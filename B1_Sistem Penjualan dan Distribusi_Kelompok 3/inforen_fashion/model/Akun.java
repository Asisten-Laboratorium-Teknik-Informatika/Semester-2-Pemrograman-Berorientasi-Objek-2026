package model;

public abstract class Akun {
    protected int idAkun;
    protected String username;
    protected String password;
    protected String nama;
    protected String email;
    protected String role;

    public Akun(int idAkun, String username, String password, String nama, String email, String role) {
        this.idAkun = idAkun;
        this.username = username;
        this.password = password;
        this.nama = nama;
        this.email = email;
        this.role = role;
    }

    public int getIdAkun() { return idAkun; }
    public String getUsername() { return username; }
    public String getNama() { return nama; }
    public String getRole() { return role; }

    public abstract void tampilkanMenu();
}
