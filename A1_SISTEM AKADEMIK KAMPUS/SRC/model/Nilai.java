package model;

public class Nilai {
    private int idKrs;
    private double nilaiTugas;
    private double nilaiUts;
    private double nilaiUas;

    private double nilaiAngka;
    private String nilaiHuruf;
    private double bobotNilai;

    private boolean isFinal;

    private String namaMataKuliah;
    private int jumlahSks;
    private String tahunAkademik;
    private String semester;

    // ===== GETTER & SETTER =====

    public int getIdKrs() { return idKrs; }
    public void setIdKrs(int i) { this.idKrs = i; }

    public double getNilaiTugas() { return nilaiTugas; }
    public void setNilaiTugas(double v) { this.nilaiTugas = v; }

    public double getNilaiUts() { return nilaiUts; }
    public void setNilaiUts(double v) { this.nilaiUts = v; }

    public double getNilaiUas() { return nilaiUas; }
    public void setNilaiUas(double v) { this.nilaiUas = v; }

    public boolean isIsFinal() { return isFinal; }
    public void setIsFinal(boolean f) { this.isFinal = f; }

    public String getNamaMataKuliah() { return namaMataKuliah; }
    public void setNamaMataKuliah(String n) { this.namaMataKuliah = n; }

    public int getJumlahSks() { return jumlahSks; }
    public void setJumlahSks(int s) { this.jumlahSks = s; }

    public String getTahunAkademik() { return tahunAkademik; }
    public void setTahunAkademik(String t) { this.tahunAkademik = t; }

    public String getSemester() { return semester; }
    public void setSemester(String s) { this.semester = s; }

    // ===== TAMBAHAN (INI YANG TADI KURANG) =====

    public double getNilaiAngka() {
        return nilaiAngka;
    }

    public void setNilaiAngka(double nilaiAngka) {
        this.nilaiAngka = nilaiAngka;
    }

    public String getNilaiHuruf() {
        return nilaiHuruf;
    }

    public void setNilaiHuruf(String nilaiHuruf) {
        this.nilaiHuruf = nilaiHuruf;
    }

    public double getBobotNilai() {
        return bobotNilai;
    }

    public void setBobotNilai(double bobotNilai) {
        this.bobotNilai = bobotNilai;
    }

    // ===== METHOD HITUNG =====

    public double hitungNilaiAngka() {
        return (nilaiTugas * 0.3) + (nilaiUts * 0.3) + (nilaiUas * 0.4);
    }
}