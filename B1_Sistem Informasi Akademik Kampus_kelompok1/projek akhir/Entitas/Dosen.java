package Entitas;

import Fitur.Tampilan;
import Koneksi.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class Dosen — mewarisi Pengguna.
 *
 * INHERITANCE  : extends Pengguna → mewarisi field nama, email, jenisKelamin, noHp.
 * ENCAPSULATION: Field spesifik Dosen (nidn, idProgramStudi) private.
 * POLYMORPHISM : Override tampilkanDataDiri() dan getIdentitas() dari Pengguna.
 */
public class Dosen extends Pengguna {

    // ===================== FIELD SPESIFIK (private → Encapsulation) =====================
    private String nidn;
    private String idProgramStudi;

    // ===================== CONSTRUCTOR =====================
    public Dosen() {}

    public Dosen(String nidn, String idProgramStudi,
                 String nama, String email,
                 String jenisKelamin, String noHp) {
        super(nama, email, jenisKelamin, noHp);
        this.nidn           = nidn;
        this.idProgramStudi = idProgramStudi;
    }

    // ===================== GETTER & SETTER (Encapsulation) =====================
    public String getNidn()           { return nidn; }
    public String getIdProgramStudi() { return idProgramStudi; }

    public void setNidn(String nidn)                      { this.nidn = nidn; }
    public void setIdProgramStudi(String idProgramStudi)  { this.idProgramStudi = idProgramStudi; }

    // ===================== OVERRIDE (Polymorphism) =====================

    /**
     * Override wajib dari Pengguna.
     * Memanggil lihatDataDiri(nidn) agar data diambil dari DB.
     */
    @Override
    public void tampilkanDataDiri() {
        lihatDataDiri(nidn);
    }

    @Override
    public String getIdentitas() { return nidn; }

    @Override
    public String getRole() { return "Dosen"; }

    // ===================== DAFTARKAN DOSEN BARU (logika lama, tidak berubah) =====================
    // ✅ Diganti dari main() → daftarDosen() agar konsisten dengan class lain
    public static void daftarDosen() {
        Scanner input = new Scanner(System.in);

        System.out.println("=== DATA DOSEN ===\n");
        System.out.print("ID Program Studi                           : "); String id_program_studi = input.nextLine().trim();
        System.out.print("NIDN                                       : "); String nidn             = input.nextLine().trim();
        System.out.print("Nama                                       : "); String nama             = input.nextLine().trim();
        System.out.print("Akun E-Mail                                : "); String email            = input.nextLine().trim();
        System.out.print("Jenis Kelamin (Laki-Laki/Perempuan)        : "); String jenis_kelamin    = input.nextLine().trim();
        System.out.print("Nomor Handphone (kosongkan jika tidak ada) : "); String no_hp            = input.nextLine().trim();

        Dosen dosen = new Dosen();
        dosen.setNidn(nidn);
        dosen.setIdProgramStudi(id_program_studi);
        dosen.setNama(nama);
        dosen.setEmail(email);
        dosen.setNoHp(no_hp.isEmpty() ? null : no_hp);

        if (id_program_studi.isEmpty() || nidn.isEmpty() || nama.isEmpty() || email.isEmpty() || jenis_kelamin.isEmpty()) {
            System.out.println("Semua data wajib diisi, kecuali nomor handphone.");
            return;
        }

        if (!dosen.isEmailValid(email)) {
            System.out.println("Format email tidak valid!");
            return;
        }

        if (!jenis_kelamin.equalsIgnoreCase("Laki-Laki")
                && !jenis_kelamin.equalsIgnoreCase("Perempuan")) {
            System.out.println("Jenis kelamin harus 'Laki-Laki' atau 'Perempuan'!");
            return;
        }
        // ✅ Normalisasi agar sesuai dengan enum DB: 'Laki-Laki' atau 'Perempuan'
        jenis_kelamin = jenis_kelamin.equalsIgnoreCase("Laki-Laki") ? "Laki-Laki" : "Perempuan";
        dosen.setJenisKelamin(jenis_kelamin);

        if (!no_hp.isEmpty() && !no_hp.matches("\\d+")) {
            System.out.println("Nomor handphone hanya boleh berisi angka!");
            return;
        }

        try {
            Connection conn = Koneksi.connect();
            if (conn == null) { System.out.println("Koneksi gagal!"); return; }

            // ✅ Enum cast benar: ::jenis_kelamin_dosen_enum (sesuai SQL schema)
            String query =
                "INSERT INTO b1.dosen(id_program_studi, nidn, nama, email, jenis_kelamin, no_hp) " +
                "VALUES(?, ?, ?, ?, ?::b1.jenis_kelamin_dosen_enum, ?)";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, dosen.getIdProgramStudi());
            ps.setString(2, dosen.getNidn());
            ps.setString(3, dosen.getNama());
            ps.setString(4, dosen.getEmail());
            ps.setString(5, dosen.getJenisKelamin());  // ✅ nilai sudah ternormalisasi

            if (no_hp.isEmpty()) {
                ps.setNull(6, java.sql.Types.VARCHAR);
            } else {
                ps.setString(6, dosen.getNoHp());
            }

            if (ps.executeUpdate() > 0) {
                System.out.println("\n=== DATA DOSEN BERHASIL DISIMPAN ===");
                System.out.println(dosen.toString());
            } else {
                System.out.println("Data dosen gagal disimpan.");
            }

        } catch (Exception e) {
            System.out.println("=== Terjadi kesalahan pada data! ===\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===================== LIHAT SEMUA DOSEN =====================
    public void lihatDosen() {
        try (Connection conn = Koneksi.connect()) {
            String sql = "SELECT nidn, nama, email FROM b1.dosen ORDER BY nidn";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            List<String[]> rows = new ArrayList<>();
            while (rs.next()) {
                rows.add(new String[]{
                    rs.getString("nidn"),
                    rs.getString("nama"),
                    rs.getString("email")
                });
            }

            System.out.println("\n=== DATA DOSEN ===");
            if (rows.isEmpty()) {
                Tampilan.peringatan("Tidak ada data dosen.");
            } else {
                String[] headers = {"NIDN", "Nama", "Email"};
                Tampilan.tampilTabel(headers, rows.toArray(new String[0][]));
            }

        } catch (Exception e) {
            Tampilan.gagal("Gagal menampilkan data dosen!");
            e.printStackTrace();
        }
    }

    // ===================== LIHAT DATA DIRI DOSEN (dipanggil tampilkanDataDiri) =====================
    public void lihatDataDiri(String nidn) {
        try (Connection conn = Koneksi.connect()) {
            String sql =
                "SELECT d.nidn, d.nama, d.email, d.jenis_kelamin, d.no_hp, " +
                "p.nama_program_studi, p.jenjang, p.fakultas " +
                "FROM b1.dosen d " +
                "JOIN b1.program_studi p ON d.id_program_studi = p.id_program_studi " +
                "WHERE d.nidn = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nidn);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String[] headers = {"Identitas", "Data"};
                String[][] rows = {
                    {"NIDN",          rs.getString("nidn")},
                    {"Nama",          rs.getString("nama")},
                    {"E-Mail",        rs.getString("email")},
                    {"Jenis Kelamin", rs.getString("jenis_kelamin")},
                    {"No. HP",        rs.getString("no_hp") != null ? rs.getString("no_hp") : "-"},
                    {"Program Studi", rs.getString("nama_program_studi")},
                    {"Jenjang",       rs.getString("jenjang")},
                    {"Fakultas",      rs.getString("fakultas")}
                };
                System.out.println("\n=== DATA DIRI DOSEN ===");
                Tampilan.tampilTabel(headers, rows);
            } else {
                Tampilan.gagal("Data dosen tidak ditemukan.");
            }

        } catch (Exception e) {
            Tampilan.gagal("Gagal menampilkan data diri!");
            e.printStackTrace();
        }
    }
}