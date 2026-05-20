package src.main.java.com.perpus.model;

public class Buku {
    private int idBuku;
    private String isbn;
    private String judul;
    private String namaKategori;
    private int stokTersedia;

    public Buku(int idBuku, String isbn, String judul, String namaKategori, int stokTersedia) {
        this.idBuku = idBuku;
        this.isbn = isbn;
        this.judul = judul;
        this.namaKategori = namaKategori;
        this.stokTersedia = stokTersedia;
    }

    public int getIdBuku() { return idBuku; }
    public String getIsbn() { return isbn; }
    public String getJudul() { return judul; }
    public String getNamaKategori() { return namaKategori; }
    public int getStokTersedia() { return stokTersedia; }
}
