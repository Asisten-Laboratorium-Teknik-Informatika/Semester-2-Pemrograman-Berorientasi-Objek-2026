package model;

public class Jadwal {
    private String namaMataKuliah;
    private String namaDosen;
    private String namaRuangan;
    private String hari;
    private java.sql.Time jamMulai;
    private java.sql.Time jamSelesai;
    private String tahunAkademik;
    private String semester;

    // TAMBAHAN UNTUK UJIAN
    private String jenisUjian;
    private java.sql.Date tanggal;
    private String pengawas;

    public String getNamaMataKuliah() { return namaMataKuliah; }
    public void setNamaMataKuliah(String n) { this.namaMataKuliah = n; }

    public String getNamaDosen() { return namaDosen; }
    public void setNamaDosen(String d) { this.namaDosen = d; }

    public String getNamaRuangan() { return namaRuangan; }
    public void setNamaRuangan(String r) { this.namaRuangan = r; }

    public String getHari() { return hari; }
    public void setHari(String h) { this.hari = h; }

    public java.sql.Time getJamMulai() { return jamMulai; }
    public void setJamMulai(java.sql.Time t) { this.jamMulai = t; }

    public java.sql.Time getJamSelesai() { return jamSelesai; }
    public void setJamSelesai(java.sql.Time t) { this.jamSelesai = t; }

    public String getTahunAkademik() { return tahunAkademik; }
    public void setTahunAkademik(String t) { this.tahunAkademik = t; }

    public String getSemester() { return semester; }
    public void setSemester(String s) { this.semester = s; }

    // ===== TAMBAHAN =====
    public String getJenisUjian() { return jenisUjian; }
    public void setJenisUjian(String jenisUjian) { this.jenisUjian = jenisUjian; }

    public java.sql.Date getTanggal() { return tanggal; }
    public void setTanggal(java.sql.Date tanggal) { this.tanggal = tanggal; }

    public String getPengawas() { return pengawas; }
    public void setPengawas(String pengawas) { this.pengawas = pengawas; }
}