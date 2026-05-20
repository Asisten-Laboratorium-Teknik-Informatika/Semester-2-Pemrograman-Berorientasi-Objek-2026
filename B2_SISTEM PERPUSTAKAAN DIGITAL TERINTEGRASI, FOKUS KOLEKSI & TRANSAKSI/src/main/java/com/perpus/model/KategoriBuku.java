package src.main.java.com.perpus.model;

public class KategoriBuku {
    private int idKategori;
    private String namaKategori;

    public KategoriBuku(int idKategori, String namaKategori) {
        this.idKategori = idKategori;
        this.namaKategori = namaKategori;
    }

    public int getIdKategori() { return idKategori; }
    public String getNamaKategori() { return namaKategori; }
}
