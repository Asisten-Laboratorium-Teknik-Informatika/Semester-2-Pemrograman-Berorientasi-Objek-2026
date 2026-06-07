package Entitas;

import Fitur.Tampilan;
import Koneksi.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Class DosenPembimbing — entitas mandiri (bukan turunan Pengguna).
 *
 * LOGIKA BISNIS:
 *   - 1 mahasiswa hanya punya 1 dosen pembimbing SEPANJANG KULIAH
 *   - Admin yang assign/ganti dosen pembimbing
 *   - Dosen pembimbing berwenang menyetujui/menolak KRS mahasiswa bimbingannya
 *
 * ENCAPSULATION: Semua field private, diakses lewat getter/setter.
 */
public class DosenPembimbing {

    // ===================== FIELD =====================
    private String idPembimbing;
    private String nidn;
    private String nim;

    // ===================== CONSTRUCTOR =====================
    public DosenPembimbing() {}

    public DosenPembimbing(String idPembimbing, String nidn, String nim) {
        this.idPembimbing = idPembimbing;
        this.nidn         = nidn;
        this.nim          = nim;
    }

    // ===================== GETTER & SETTER =====================
    public String getIdPembimbing() { return idPembimbing; }
    public String getNidn()         { return nidn; }
    public String getNim()          { return nim; }

    public void setIdPembimbing(String v) { this.idPembimbing = v; }
    public void setNidn(String v)         { this.nidn = v; }
    public void setNim(String v)          { this.nim = v; }

    // ===================== CARI NIDN PEMBIMBING =====================
    public String cariNidnPembimbing(String nim) {
        try (Connection conn = Koneksi.connect()) {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT nidn FROM b1.dosen_pembimbing WHERE nim = ?");
            ps.setString(1, nim);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString("nidn") : null;
        } catch (Exception e) {
            Tampilan.gagal("Gagal mencari dosen pembimbing!");
            return null;
        }
    }

    // ===================== CEK PEMBIMBING =====================
    public boolean cekPembimbing(String nidn, String nim) {
        try (Connection conn = Koneksi.connect()) {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT 1 FROM b1.dosen_pembimbing WHERE nidn = ? AND nim = ?");
            ps.setString(1, nidn);
            ps.setString(2, nim);
            return ps.executeQuery().next();
        } catch (Exception e) {
            Tampilan.gagal("Gagal memeriksa data pembimbing!");
            return false;
        }
    }

    // ===================== LIHAT DOSEN PEMBIMBING (instance) =====================
    /**
     * Dipanggil dari handleAdmin() case 5:
     *   new DosenPembimbing().lihatDosenPembimbing()
     */
    public void lihatDosenPembimbing() {
        try (Connection conn = Koneksi.connect()) {
            String sql =
                "SELECT d.nidn, d.nama, d.email, " +
                "COUNT(dp.nim) AS jumlah_bimbingan " +
                "FROM b1.dosen d " +
                "LEFT JOIN b1.dosen_pembimbing dp ON d.nidn = dp.nidn " +
                "GROUP BY d.nidn, d.nama, d.email " +
                "ORDER BY d.nama";

            ResultSet rs = conn.createStatement().executeQuery(sql);
            List<String[]> rows = new ArrayList<>();
            while (rs.next()) {
                rows.add(new String[]{
                    rs.getString("nidn"),
                    rs.getString("nama"),
                    rs.getString("email"),
                    rs.getString("jumlah_bimbingan")
                });
            }

            System.out.println("\n=== DAFTAR DOSEN PEMBIMBING ===");
            if (rows.isEmpty()) {
                Tampilan.peringatan("Belum ada data dosen pembimbing.");
            } else {
                String[] headers = {"NIDN", "Nama", "Email", "Jumlah Bimbingan"};
                Tampilan.tampilTabel(headers, rows.toArray(new String[0][]));
            }

        } catch (Exception e) {
            Tampilan.gagal("Gagal menampilkan data dosen pembimbing!");
            e.printStackTrace();
        }
    }

    // ===================== LIHAT MAHASISWA BIMBINGAN =====================
    public void lihatMahasiswaBimbingan(String nidn) {
        try (Connection conn = Koneksi.connect()) {
            String sql =
                "SELECT m.nim, m.nama, m.email, p.nama_program_studi, p.jenjang " +
                "FROM b1.dosen_pembimbing dp " +
                "JOIN b1.mahasiswa m     ON dp.nim = m.nim " +
                "JOIN b1.program_studi p ON m.id_program_studi = p.id_program_studi " +
                "WHERE dp.nidn = ? " +
                "ORDER BY m.nama";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nidn);
            ResultSet rs = ps.executeQuery();

            List<String[]> rows = new ArrayList<>();
            while (rs.next()) {
                rows.add(new String[]{
                    rs.getString("nim"),
                    rs.getString("nama"),
                    rs.getString("email"),
                    rs.getString("nama_program_studi"),
                    rs.getString("jenjang")
                });
            }

            System.out.println("\n=== MAHASISWA BIMBINGAN SAYA ===");
            if (rows.isEmpty()) {
                Tampilan.peringatan("Tidak ada mahasiswa bimbingan.");
            } else {
                String[] headers = {"NIM", "Nama", "Email", "Program Studi", "Jenjang"};
                Tampilan.tampilTabel(headers, rows.toArray(new String[0][]));
            }

        } catch (Exception e) {
            Tampilan.gagal("Gagal menampilkan mahasiswa bimbingan!");
            e.printStackTrace();
        }
    }

    // ===================== ASSIGN PEMBIMBING (Admin) =====================
    /**
     * Dipanggil dari handleAdmin() case 3:
     *   DosenPembimbing.assignMahasiswa()
     */
    public static void assignMahasiswa() {
        System.out.println("=== ASSIGN DOSEN PEMBIMBING ===\n");

        // Tampilkan daftar dosen + jumlah bimbingan
        try (Connection conn = Koneksi.connect()) {
            String sqlDosen =
                "SELECT d.nidn, d.nama, COUNT(dp.nim) AS jumlah_bimbingan " +
                "FROM b1.dosen d " +
                "LEFT JOIN b1.dosen_pembimbing dp ON d.nidn = dp.nidn " +
                "GROUP BY d.nidn, d.nama ORDER BY d.nama";

            ResultSet rsDosen = conn.createStatement().executeQuery(sqlDosen);
            List<String[]> rows = new ArrayList<>();
            while (rsDosen.next()) {
                rows.add(new String[]{
                    rsDosen.getString("nidn"),
                    rsDosen.getString("nama"),
                    rsDosen.getString("jumlah_bimbingan")
                });
            }
            if (rows.isEmpty()) { System.out.println("Tidak ada data dosen."); return; }
            Tampilan.tampilTabel(new String[]{"NIDN", "Nama Dosen", "Jumlah Bimbingan"},
                rows.toArray(new String[0][]));

        } catch (Exception e) {
            Tampilan.gagal("Gagal menampilkan daftar dosen: " + e.getMessage()); return;
        }

        System.out.print("\nNIDN Dosen Pembimbing : "); String nidn = Fitur.Dashboard.input.nextLine().trim();
        System.out.print("NIM Mahasiswa         : "); String nim  = Fitur.Dashboard.input.nextLine().trim();

        if (nidn.isEmpty() || nim.isEmpty()) {
            Tampilan.gagal("NIDN dan NIM tidak boleh kosong!"); return;
        }

        try (Connection conn = Koneksi.connect()) {
            // Cek dosen ada
            PreparedStatement cekDosen = conn.prepareStatement(
                "SELECT nama FROM b1.dosen WHERE nidn = ?");
            cekDosen.setString(1, nidn);
            ResultSet rsDosen = cekDosen.executeQuery();
            if (!rsDosen.next()) { Tampilan.gagal("NIDN tidak ditemukan!"); return; }
            String namaDosen = rsDosen.getString("nama");

            // Cek mahasiswa ada
            PreparedStatement cekMhs = conn.prepareStatement(
                "SELECT nama FROM b1.mahasiswa WHERE nim = ?");
            cekMhs.setString(1, nim);
            ResultSet rsMhs = cekMhs.executeQuery();
            if (!rsMhs.next()) { Tampilan.gagal("NIM tidak ditemukan!"); return; }
            String namaMhs = rsMhs.getString("nama");

            // Cek mahasiswa sudah punya pembimbing
            PreparedStatement cekSudah = conn.prepareStatement(
                "SELECT d.nama FROM b1.dosen_pembimbing dp " +
                "JOIN b1.dosen d ON dp.nidn = d.nidn WHERE dp.nim = ?");
            cekSudah.setString(1, nim);
            ResultSet rsSudah = cekSudah.executeQuery();
            if (rsSudah.next()) {
                Tampilan.gagal("Mahasiswa " + namaMhs + " sudah memiliki dosen pembimbing: "
                    + rsSudah.getString("nama") + "!"); return;
            }

            // Generate ID
            ResultSet rsCount = conn.createStatement()
                .executeQuery("SELECT COUNT(*) FROM b1.dosen_pembimbing");
            rsCount.next();
            String idPembimbing = String.format("PB%08d", rsCount.getInt(1) + 1);

            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO b1.dosen_pembimbing (id_pembimbing, nidn, nim) VALUES (?, ?, ?)");
            ps.setString(1, idPembimbing);
            ps.setString(2, nidn);
            ps.setString(3, nim);

            if (ps.executeUpdate() > 0) {
                Tampilan.sukses("\n=== DOSEN PEMBIMBING BERHASIL DIASSIGN ===");
                System.out.println("Dosen     : " + namaDosen + " (" + nidn + ")");
                System.out.println("Mahasiswa : " + namaMhs   + " (" + nim  + ")");
                System.out.println("Berlaku   : Sepanjang masa studi");
            } else {
                Tampilan.gagal("Gagal assign dosen pembimbing.");
            }

        } catch (Exception e) {
            Tampilan.gagal("Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===================== GANTI PEMBIMBING (Admin) =====================
    /**
     * Dipanggil dari handleAdmin() case 4:
     *   DosenPembimbing.gantiPembimbing()
     */
    public static void gantiPembimbing() {
        System.out.println("=== GANTI DOSEN PEMBIMBING ===\n");
        System.out.print("NIM Mahasiswa             : "); String nim      = Fitur.Dashboard.input.nextLine().trim();
        System.out.print("NIDN Dosen Pembimbing Baru: "); String nidnBaru = Fitur.Dashboard.input.nextLine().trim();

        if (nim.isEmpty() || nidnBaru.isEmpty()) {
            Tampilan.gagal("NIM dan NIDN tidak boleh kosong!"); return;
        }

        try (Connection conn = Koneksi.connect()) {
            // Cek mahasiswa sudah punya pembimbing
            PreparedStatement cekAda = conn.prepareStatement(
                "SELECT d.nama AS nama_lama FROM b1.dosen_pembimbing dp " +
                "JOIN b1.dosen d ON dp.nidn = d.nidn WHERE dp.nim = ?");
            cekAda.setString(1, nim);
            ResultSet rsAda = cekAda.executeQuery();
            if (!rsAda.next()) {
                Tampilan.gagal("Mahasiswa ini belum memiliki dosen pembimbing. Gunakan fitur Assign."); return;
            }
            String namaLama = rsAda.getString("nama_lama");

            // Cek dosen baru ada
            PreparedStatement cekDosen = conn.prepareStatement(
                "SELECT nama FROM b1.dosen WHERE nidn = ?");
            cekDosen.setString(1, nidnBaru);
            ResultSet rsDosen = cekDosen.executeQuery();
            if (!rsDosen.next()) { Tampilan.gagal("NIDN dosen baru tidak ditemukan!"); return; }
            String namaBaru = rsDosen.getString("nama");

            PreparedStatement ps = conn.prepareStatement(
                "UPDATE b1.dosen_pembimbing SET nidn = ? WHERE nim = ?");
            ps.setString(1, nidnBaru);
            ps.setString(2, nim);

            if (ps.executeUpdate() > 0) {
                Tampilan.sukses("\n=== DOSEN PEMBIMBING BERHASIL DIGANTI ===");
                System.out.println("NIM Mahasiswa   : " + nim);
                System.out.println("Pembimbing Lama : " + namaLama);
                System.out.println("Pembimbing Baru : " + namaBaru + " (" + nidnBaru + ")");
            } else {
                Tampilan.gagal("Gagal mengganti dosen pembimbing.");
            }

        } catch (Exception e) {
            Tampilan.gagal("Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===================== TUNJUK PEMBIMBING (Admin → Data Master) =====================
    /**
     * Dipanggil dari menuDataMaster() case 9:
     *   DosenPembimbing.tunjukPembimbing()
     * Alias dari assignMahasiswa() — sama logikanya.
     */
    public static void tunjukPembimbing() {
        assignMahasiswa();
    }
}