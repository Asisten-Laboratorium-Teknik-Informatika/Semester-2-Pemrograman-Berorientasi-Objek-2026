package model;

import java.sql.Date;

public class Mahasiswa {

    private String nim;
    private String namaMahasiswa;
    private String jenisKelamin;
    private String tempatLahir;
    private Date tanggalLahir;
    private String alamat;
    private String noTelp;
    private String email;
    private int angkatan;
    private int idProdi;
    private String status;
    private String passwordMahasiswa; // satu-satunya field password

    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }

    public String getNamaMahasiswa() { return namaMahasiswa; }
    public void setNamaMahasiswa(String namaMahasiswa) { this.namaMahasiswa = namaMahasiswa; }

    public String getJenisKelamin() { return jenisKelamin; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }

    public String getTempatLahir() { return tempatLahir; }
    public void setTempatLahir(String tempatLahir) { this.tempatLahir = tempatLahir; }

    public Date getTanggalLahir() { return tanggalLahir; }
    public void setTanggalLahir(Date tanggalLahir) { this.tanggalLahir = tanggalLahir; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public String getNoTelp() { return noTelp; }
    public void setNoTelp(String noTelp) { this.noTelp = noTelp; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getAngkatan() { return angkatan; }
    public void setAngkatan(int angkatan) { this.angkatan = angkatan; }

    public int getIdProdi() { return idProdi; }
    public void setIdProdi(int idProdi) { this.idProdi = idProdi; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPasswordMahasiswa() { return passwordMahasiswa; }
    public void setPasswordMahasiswa(String passwordMahasiswa) { this.passwordMahasiswa = passwordMahasiswa; }
}