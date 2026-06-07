package Entitas;

/**
 * Abstract class Pengguna — dasar dari Mahasiswa dan Dosen.
 *
 * ABSTRACTION  : Pengguna tidak bisa diinstansiasi langsung. Memaksa
 *                subclass mengimplementasikan method abstrak.
 * ENCAPSULATION: Semua field private, hanya bisa diakses lewat getter/setter.
 */
public abstract class Pengguna {

    // ===================== FIELD (private → Encapsulation) =====================
    private String nama;
    private String email;
    private String jenisKelamin;
    private String noHp;

    // ===================== CONSTRUCTOR =====================
    public Pengguna() {}

    public Pengguna(String nama, String email, String jenisKelamin, String noHp) {
        this.nama         = nama;
        this.email        = email;
        this.jenisKelamin = jenisKelamin;
        this.noHp         = noHp;
    }

    // ===================== GETTER & SETTER (Encapsulation) =====================
    public String getNama()         { return nama; }
    public String getEmail()        { return email; }
    public String getJenisKelamin() { return jenisKelamin; }
    public String getNoHp()         { return noHp != null ? noHp : "-"; }

    public void setNama(String nama)                 { this.nama = nama; }
    public void setEmail(String email)               { this.email = email; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }
    public void setNoHp(String noHp)                 { this.noHp = noHp; }

    // ===================== ABSTRACT METHOD (Abstraction + Polymorphism) =====================

    /**
     * Setiap subclass wajib mengimplementasikan cara menampilkan data dirinya sendiri.
     * Ini adalah titik Polymorphism — pemanggilan tampilkanDataDiri() akan
     * mengeksekusi versi milik Mahasiswa atau Dosen sesuai objek sebenarnya.
     */
    public abstract void tampilkanDataDiri();

    /**
     * Setiap subclass wajib mengimplementasikan label identitas uniknya
     * (NIM untuk Mahasiswa, NIDN untuk Dosen).
     */
    public abstract String getIdentitas();

    /**
     * Setiap subclass wajib mengembalikan role-nya ("Mahasiswa" / "Dosen").
     */
    public abstract String getRole();

    // ===================== CONCRETE METHOD (bisa dipakai semua subclass) =====================

    /**
     * Validasi email dasar — dipakai oleh Mahasiswa dan Dosen.
     * Tidak perlu override, tapi bisa jika subclass butuh validasi khusus.
     */
    public boolean isEmailValid(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    /**
     * Representasi string umum — bisa di-override subclass (Polymorphism).
     */
    @Override
    public String toString() {
        return "[" + getRole() + "] " + nama + " (" + getIdentitas() + ")";
    }
}