package Entitas;

import Koneksi.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

/**
 * Class Akun — menyimpan kredensial login pengguna.
 *
 * ENCAPSULATION: Semua field private, akses hanya lewat getter/setter.
 */
public class Akun {

    // ===================== FIELD (private → Encapsulation) =====================
    private String username;
    private String passwordAkun;
    private String roleAkun;
    private String nim;
    private String nidn;

    // ===================== CONSTRUCTOR =====================
    public Akun() {}

    public Akun(String username, String passwordAkun, String roleAkun,
                String nim, String nidn) {
        this.username     = username;
        this.passwordAkun = passwordAkun;
        this.roleAkun     = roleAkun;
        this.nim          = nim;
        this.nidn         = nidn;
    }

    // ===================== GETTER & SETTER (Encapsulation) =====================
    public String getUsername()     { return username; }
    public String getPasswordAkun() { return passwordAkun; }
    public String getRoleAkun()     { return roleAkun; }
    public String getNim()          { return nim; }
    public String getNidn()         { return nidn; }

    public void setUsername(String username)         { this.username = username; }
    public void setPasswordAkun(String passwordAkun) { this.passwordAkun = passwordAkun; }
    public void setRoleAkun(String roleAkun)         { this.roleAkun = roleAkun; }
    public void setNim(String nim)                   { this.nim = nim; }
    public void setNidn(String nidn)                 { this.nidn = nidn; }

    // ===================== VALIDASI (terenkapsulasi) =====================
    public boolean isUsernameValid() {
        return username != null && !username.isEmpty() && username.length() <= 50;
    }

    public boolean isPasswordValid() {
        return passwordAkun != null
            && passwordAkun.length() >= 6
            && passwordAkun.length() <= 100;
    }

    public boolean isRoleValid() {
        return "Mahasiswa".equals(roleAkun) || "Dosen".equals(roleAkun);
    }

    // ===================== BUAT AKUN (pengganti main) =====================
    /**
     * Method static untuk membuat akun baru dari konsol.
     * Dipanggil dari Dashboard.menuKelolaAkun() oleh Admin.
     * Semua logika validasi dan query SQL tidak berubah.
     */
    public static void buatAkun() {
        Scanner input = new Scanner(System.in);

        System.out.println("=== DATA AKUN ===\n");
        System.out.print("USERNAME: ");
        String username = input.nextLine().trim();

        System.out.print("PASSWORD: ");
        String password_akun = input.nextLine().trim();

        System.out.print("Role Anda (Mahasiswa/Dosen): ");
        String role_akun = input.nextLine().trim();

        if (role_akun.isEmpty()) {
            System.out.println("Role tidak boleh kosong!");
            return;
        }

        role_akun = role_akun.substring(0, 1).toUpperCase()
                  + role_akun.substring(1).toLowerCase();

        // Buat objek Akun, lalu validasi lewat method-nya (Encapsulation)
        Akun akun = new Akun(username, password_akun, role_akun, null, null);

        if (!akun.isUsernameValid()) {
            System.out.println("Username tidak boleh kosong dan maksimal 50 karakter!");
            return;
        }

        if (!akun.isPasswordValid()) {
            System.out.println("Password minimal 6 dan maksimal 100 karakter!");
            return;
        }

        if (!akun.isRoleValid()) {
            System.out.println("Role tidak valid! Harus Mahasiswa atau Dosen.");
            return;
        }

        if (role_akun.equals("Mahasiswa")) {
            System.out.print("NIM: ");
            String nim = input.nextLine().trim();
            if (nim.isEmpty()) { System.out.println("NIM tidak boleh kosong!"); return; }
            akun.setNim(nim);
        } else {
            System.out.print("NIDN: ");
            String nidn = input.nextLine().trim();
            if (nidn.isEmpty()) { System.out.println("NIDN tidak boleh kosong!"); return; }
            akun.setNidn(nidn);
        }

        try {
            Connection conn = Koneksi.connect();
            if (conn == null) { System.out.println("Koneksi gagal!"); return; }

            // Cek username sudah dipakai
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT COUNT(*) FROM b1.akun WHERE username = ?")) {
                ps.setString(1, akun.getUsername());
                ResultSet rs = ps.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Username sudah digunakan!"); return;
                }
            }

            // Cek Mahasiswa
            if ("Mahasiswa".equals(akun.getRoleAkun())) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT COUNT(*) FROM b1.mahasiswa WHERE nim = ?")) {
                    ps.setString(1, akun.getNim());
                    ResultSet rs = ps.executeQuery();
                    if (rs.next() && rs.getInt(1) == 0) {
                        System.out.println("NIM tidak ditemukan!"); return;
                    }
                }
                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT COUNT(*) FROM b1.akun WHERE nim = ?")) {
                    ps.setString(1, akun.getNim());
                    ResultSet rs = ps.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        System.out.println("Mahasiswa sudah memiliki akun!"); return;
                    }
                }
            }

            // Cek Dosen
            if ("Dosen".equals(akun.getRoleAkun())) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT COUNT(*) FROM b1.dosen WHERE nidn = ?")) {
                    ps.setString(1, akun.getNidn());
                    ResultSet rs = ps.executeQuery();
                    if (rs.next() && rs.getInt(1) == 0) {
                        System.out.println("NIDN tidak ditemukan!"); return;
                    }
                }
                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT COUNT(*) FROM b1.akun WHERE nidn = ?")) {
                    ps.setString(1, akun.getNidn());
                    ResultSet rs = ps.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        System.out.println("Dosen sudah memiliki akun!"); return;
                    }
                }
            }

            // Insert akun
            String query =
                "INSERT INTO b1.akun(username, password_akun, role_akun, nim, nidn) " +
                "VALUES(?, ?, ?::b1.role_akun_enum, ?, ?)";

            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, akun.getUsername());
                ps.setString(2, akun.getPasswordAkun());
                ps.setString(3, akun.getRoleAkun());
                ps.setString(4, akun.getNim());
                ps.setString(5, akun.getNidn());

                int baris = ps.executeUpdate();
                if (baris > 0) {
                    System.out.println("\n=== DATA AKUN BERHASIL DISIMPAN ===");
                    System.out.println("Username : " + akun.getUsername());
                    System.out.println("Role     : " + akun.getRoleAkun());
                    if (akun.getNim()  != null) System.out.println("NIM  : " + akun.getNim());
                    if (akun.getNidn() != null) System.out.println("NIDN : " + akun.getNidn());
                } else {
                    System.out.println("Data akun gagal disimpan.");
                }
            }

        } catch (Exception e) {
            System.out.println("=== Terjadi kesalahan pada data! ===\n" + e.getMessage());
            e.printStackTrace();
        }
    }
}