package Entitas;

import Fitur.Tampilan;
import Koneksi.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Mahasiswa — mewarisi Pengguna.
 *
 * INHERITANCE  : extends Pengguna → mewarisi field nama, email, jenisKelamin, noHp.
 * ENCAPSULATION: Field spesifik (nim, idProgramStudi, tanggalLahir) private.
 * POLYMORPHISM : Override tampilkanDataDiri() dan getIdentitas() dari Pengguna.
 */
public class Mahasiswa extends Pengguna {

    // ===================== FIELD SPESIFIK (private → Encapsulation) =====================
    private String nim;
    private String idProgramStudi;
    private java.sql.Date tanggalLahir;

    // ===================== CONSTRUCTOR =====================
    public Mahasiswa() { super("", "", "", ""); }

    public Mahasiswa(String nim, String idProgramStudi,
                     String nama, String email,
                     String jenisKelamin, String noHp,
                     java.sql.Date tanggalLahir) {
        super(nama, email, jenisKelamin, noHp);
        this.nim            = nim;
        this.idProgramStudi = idProgramStudi;
        this.tanggalLahir   = tanggalLahir;
    }

    // ===================== GETTER & SETTER (Encapsulation) =====================
    public String getNim()                 { return nim; }
    public String getIdProgramStudi()      { return idProgramStudi; }
    public java.sql.Date getTanggalLahir() { return tanggalLahir; }

    public void setNim(String nim)                          { this.nim = nim; }
    public void setIdProgramStudi(String idProgramStudi)    { this.idProgramStudi = idProgramStudi; }
    public void setTanggalLahir(java.sql.Date tanggalLahir) { this.tanggalLahir = tanggalLahir; }

    // ===================== OVERRIDE (Polymorphism) =====================

    @Override
    public String getIdentitas() { return nim; }

    @Override
    public String getRole() { return "Mahasiswa"; }

    /**
     * Override tanpa parameter — pakai nim yang tersimpan di objek.
     */
    @Override
    public void tampilkanDataDiri() {
        if (this.nim != null) {
            tampilkanDataDiri(this.nim);
        } else {
            Tampilan.gagal("NIM belum diset pada objek Mahasiswa ini.");
        }
    }

    // ===================== TAMPILKAN DATA DIRI (dengan parameter) =====================
    public void tampilkanDataDiri(String nim) {
        try (Connection conn = Koneksi.connect()) {
            String sql =
                "SELECT m.nim, m.nama, m.email, m.jenis_kelamin, m.no_hp, m.tanggal_lahir, " +
                "p.nama_program_studi, p.jenjang, p.fakultas " +
                "FROM b1.mahasiswa m " +
                "JOIN b1.program_studi p ON m.id_program_studi = p.id_program_studi " +
                "WHERE m.nim = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nim);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] headers = {"Identitas", "Data"};
                String[][] rows = {
                    {"NIM",           rs.getString("nim")},
                    {"Nama",          rs.getString("nama")},
                    {"E-Mail",        rs.getString("email")},
                    {"Jenis Kelamin", rs.getString("jenis_kelamin")},
                    {"No. HP",        rs.getString("no_hp") != null ? rs.getString("no_hp") : "-"},
                    {"Tanggal Lahir", rs.getString("tanggal_lahir")},
                    {"Program Studi", rs.getString("nama_program_studi")},
                    {"Jenjang",       rs.getString("jenjang")},
                    {"Fakultas",      rs.getString("fakultas")}
                };
                System.out.println("\n=== DATA DIRI MAHASISWA ===");
                Tampilan.tampilTabel(headers, rows);
            } else {
                Tampilan.gagal("Data mahasiswa tidak ditemukan.");
            }

        } catch (Exception e) {
            Tampilan.gagal("Gagal menampilkan data diri!");
            e.printStackTrace();
        }
    }

    // ===================== ALIAS — dipanggil Dashboard =====================
    /**
     * lihatDataDiri() adalah alias dari tampilkanDataDiri(nim).
     * Dashboard memanggil: new Mahasiswa().lihatDataDiri(Session.getNim())
     */
    public void lihatDataDiri(String nim) {
        tampilkanDataDiri(nim);
    }

    // ===================== DAFTARKAN MAHASISWA BARU =====================
    public static void daftarMahasiswa() {
        System.out.println("=== DATA MAHASISWA ===\n");
        System.out.print("ID Program Studi                           : "); String id_program_studi = Fitur.Dashboard.input.nextLine().trim();
        System.out.print("NIM                                        : "); String nim              = Fitur.Dashboard.input.nextLine().trim();
        System.out.print("Nama                                       : "); String nama             = Fitur.Dashboard.input.nextLine().trim();
        System.out.print("Akun E-Mail                                : "); String email            = Fitur.Dashboard.input.nextLine().trim();
        System.out.print("Jenis Kelamin (Laki-Laki/Perempuan)        : "); String jenis_kelamin    = Fitur.Dashboard.input.nextLine().trim();
        System.out.print("Nomor Handphone (kosongkan jika tidak ada) : "); String no_hp            = Fitur.Dashboard.input.nextLine().trim();
        System.out.print("Tanggal Lahir (YYYY-MM-DD)                 : "); String tanggal_lahir    = Fitur.Dashboard.input.nextLine().trim();

        if (id_program_studi.isEmpty() || nim.isEmpty() || nama.isEmpty()
                || email.isEmpty() || jenis_kelamin.isEmpty() || tanggal_lahir.isEmpty()) {
            Tampilan.gagal("Semua data wajib diisi, kecuali nomor handphone.");
            return;
        }

        if (!nim.matches("\\d+")) {
            Tampilan.gagal("NIM hanya boleh berisi angka!"); return;
        }

        Mahasiswa mhs = new Mahasiswa();
        if (!mhs.isEmailValid(email)) {
            Tampilan.gagal("Format email tidak valid!"); return;
        }

        if (!jenis_kelamin.equalsIgnoreCase("Laki-Laki")
                && !jenis_kelamin.equalsIgnoreCase("Perempuan")) {
            Tampilan.gagal("Jenis kelamin harus 'Laki-Laki' atau 'Perempuan'!"); return;
        }
        // Normalisasi sesuai enum DB
        jenis_kelamin = jenis_kelamin.equalsIgnoreCase("Laki-Laki") ? "Laki-Laki" : "Perempuan";

        if (!no_hp.isEmpty() && !no_hp.matches("\\d+")) {
            Tampilan.gagal("Nomor handphone hanya boleh berisi angka!"); return;
        }

        java.sql.Date tanggalLahir;
        try {
            tanggalLahir = java.sql.Date.valueOf(tanggal_lahir);
            if (tanggalLahir.after(new java.sql.Date(System.currentTimeMillis()))) {
                Tampilan.gagal("Tanggal lahir tidak boleh melebihi hari ini!"); return;
            }
        } catch (IllegalArgumentException e) {
            Tampilan.gagal("Format tanggal harus YYYY-MM-DD!"); return;
        }

        try (Connection conn = Koneksi.connect()) {
            if (conn == null) { Tampilan.gagal("Koneksi gagal!"); return; }

            String query =
                "INSERT INTO b1.mahasiswa(id_program_studi, nim, nama, email, " +
                "jenis_kelamin, no_hp, tanggal_lahir) " +
                "VALUES(?, ?, ?, ?, ?::b1.jenis_kelamin_mahasiswa_enum, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, id_program_studi);
            ps.setString(2, nim);
            ps.setString(3, nama);
            ps.setString(4, email);
            ps.setString(5, jenis_kelamin);
            if (no_hp.isEmpty()) ps.setNull(6, java.sql.Types.VARCHAR);
            else                 ps.setString(6, no_hp);
            ps.setDate(7, tanggalLahir);

            if (ps.executeUpdate() > 0) {
                Tampilan.sukses("\n=== DATA MAHASISWA BERHASIL DISIMPAN ===");
                System.out.println("NIM  : " + nim);
                System.out.println("Nama : " + nama);
            } else {
                Tampilan.gagal("Data mahasiswa gagal disimpan.");
            }

        } catch (Exception e) {
            Tampilan.gagal("Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===================== LIHAT SEMUA MAHASISWA =====================
    public void lihatMahasiswa() {
        try (Connection conn = Koneksi.connect()) {
            String sql = "SELECT nim, nama, email FROM b1.mahasiswa ORDER BY nim";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            List<String[]> rows = new ArrayList<>();
            while (rs.next()) {
                rows.add(new String[]{
                    rs.getString("nim"),
                    rs.getString("nama"),
                    rs.getString("email")
                });
            }

            System.out.println("\n=== DATA MAHASISWA ===");
            if (rows.isEmpty()) {
                Tampilan.peringatan("Tidak ada data mahasiswa.");
            } else {
                String[] headers = {"NIM", "Nama", "Email"};
                Tampilan.tampilTabel(headers, rows.toArray(new String[0][]));
            }

        } catch (Exception e) {
            Tampilan.gagal("Gagal menampilkan data mahasiswa!");
            e.printStackTrace();
        }
    }
}