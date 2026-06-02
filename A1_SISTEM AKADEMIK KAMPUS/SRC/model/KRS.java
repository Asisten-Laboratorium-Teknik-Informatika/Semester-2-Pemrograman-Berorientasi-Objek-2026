package model;

public class KRS {
    private int idKrs;
    private String namaMahasiswa;
    private String namaMataKuliah;
    private int jumlahSks;
    private String namaDosen;
    private String namaKelas;
    private String tahunAkademik;
    private String semesterTA;
    private String statusKrs;

    public int getIdKrs() { return idKrs; }
    public void setIdKrs(int i) { this.idKrs = i; }

    public String getNamaMahasiswa() { return namaMahasiswa; }
    public void setNamaMahasiswa(String n) { this.namaMahasiswa = n; }

    public String getNamaMataKuliah() { return namaMataKuliah; }
    public void setNamaMataKuliah(String n) { this.namaMataKuliah = n; }

    public int getJumlahSks() { return jumlahSks; }
    public void setJumlahSks(int s) { this.jumlahSks = s; }

    public String getNamaDosen() { return namaDosen; }
    public void setNamaDosen(String d) { this.namaDosen = d; }

    public String getNamaKelas() { return namaKelas; }
    public void setNamaKelas(String k) { this.namaKelas = k; }

    public String getTahunAkademik() { return tahunAkademik; }
    public void setTahunAkademik(String t) { this.tahunAkademik = t; }

    public String getSemesterTA() { return semesterTA; }
    public void setSemesterTA(String s) { this.semesterTA = s; }

    public String getStatusKrs() { return statusKrs; }
    public void setStatusKrs(String s) { this.statusKrs = s; }
}