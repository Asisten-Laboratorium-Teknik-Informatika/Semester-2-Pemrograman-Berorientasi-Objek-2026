package model;
import config.DatabaseConfig;
import java.sql.*;

public class Bos extends User {
    public Bos(int id, String nama, String email) { super(id, nama, email); }

    @Override
    public boolean tampilkanMenu() {
        System.out.println("\n--- MENU DIREKTUR (BOS) ---");
        System.out.println("1. Setujui Cuti Pegawai\n2. Buat Pengumuman Baru\n3. Daftar Pegawai\n4. Ubah/Naik Jabatan\n5. Lihat Laporan\n6. Logout");
        String pil = scanner.nextLine();
        if (pil.equals("1")) approveCuti();
        else if (pil.equals("2")) buatPengumuman();
        else if (pil.equals("3")) daftarPegawai();
        else if (pil.equals("4")) naikJabatan();
        else if (pil.equals("5")) lihatLaporan();
        return !pil.equals("6");
    }

    private void approveCuti() {
        System.out.print("Masukkan ID Cuti: ");
        int id = Integer.parseInt(scanner.nextLine());
        try (Connection conn = config.DatabaseConfig.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE kel7.cuti SET status = 'disetujui' WHERE id_cuti = ?");
            ps.setInt(1, id);
            int rows = ps.executeUpdate(); 
            if (rows > 0) System.out.println("Cuti Berhasil Disetujui!");
            else System.out.println("[Gagal] ID Cuti tidak ditemukan!");
        } catch (Exception e) { e.printStackTrace(); }
    }
    private void buatPengumuman() {
        System.out.print("Judul: "); String jdl = scanner.nextLine();
        System.out.print("Isi Pesan: "); String isi = scanner.nextLine();
        try (Connection conn = DatabaseConfig.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO kel7.pengumuman (id_pembuat, judul, isi_pesan) VALUES (?, ?, ?)");
            ps.setInt(1, this.idAkun); ps.setString(2, jdl); ps.setString(3, isi);
            ps.executeUpdate();
            System.out.println("Pengumuman Terkirim!");
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void daftarPegawai() {
    System.out.println("\n--- DAFTAR SELURUH PEGAWAI PERUSAHAAN ---");
    String sql = "SELECT p.id_pegawai,p.nik, p.nama_lengkap, d.nama_departemen, j.nama_jabatan " +
                 "FROM kel7.pegawai p " +
                 "LEFT JOIN kel7.departemen d ON p.id_departemen = d.id_departemen " +
                 "LEFT JOIN kel7.jabatan j ON p.id_jabatan = j.id_jabatan " +
                 "ORDER BY p.id_pegawai ASC";

    try (Connection conn = config.DatabaseConfig.getConnection();
         Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery(sql)) {

        System.out.printf("%-10s | %-15s | %-20s | %-15s | %-15s\n", "ID Pegawai", "Nik Pegawai", "Nama Lengkap", "Departemen", "Jabatan");
        System.out.println("-------------------------------------------------------------------------");
        
        boolean adaData = false;
        while (rs.next()) {
            adaData = true;
            System.out.printf("%-10d | %-15s | %-20s | %-15s | %-15s\n", 
                rs.getInt(1), 
                rs.getString(2), 
                rs.getString(3), 
                rs.getString(4),
                rs.getString(5));
        }
        
        if (!adaData) {
            System.out.println("Belum ada data pegawai terdaftar.");
        }
        System.out.println("-------------------------------------------------------------------------");
    } catch (Exception e) {
        System.out.println("Error memuat data pegawai: " + e.getMessage());
    }
}

    private void lihatLaporan() {
    System.out.println("\n--- LAPORAN KERJA PEGAWAI ---");
    try (Connection conn = config.DatabaseConfig.getConnection()) {
        String sql = "SELECT p.nama_lengkap, l.tanggal, l.deskripsi_pekerjaan FROM kel7.laporan_kerja l " +
                     "JOIN kel7.pegawai p ON l.id_pegawai = p.id_pegawai";
        ResultSet rs = conn.createStatement().executeQuery(sql);
        while(rs.next()) {
            System.out.printf("[%s] %s: %s\n", rs.getDate(2), rs.getString(1), rs.getString(3));
        }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void naikJabatan() {
    System.out.println("\n--- FORM UBAH / NAIK JABATAN PEGAWAI ---");
    System.out.print("Masukkan ID Pegawai yang akan diubah: ");
    int idPeg = Integer.parseInt(scanner.nextLine());

    try (Connection conn = config.DatabaseConfig.getConnection()) {
        String sqlCek = "SELECT nama_lengkap FROM kel7.pegawai WHERE id_pegawai = ?";
        try (PreparedStatement psCek = conn.prepareStatement(sqlCek)) {
            psCek.setInt(1, idPeg);
            try (ResultSet rs = psCek.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("[GAGAL] ID Pegawai " + idPeg + " tidak ditemukan di sistem!");
                    return;
                }
                System.out.println("Pegawai Ditemukan: " + rs.getString("nama_lengkap"));
            }
        }

        System.out.println("\nPilihan Jabatan Baru:");
        System.out.println("1. Staff");
        System.out.println("2. Supervisor");
        System.out.println("3. Manager");
        System.out.print("Pilih ID Jabatan Baru (1-3): ");
        int jabBaru = Integer.parseInt(scanner.nextLine());
        
        String sqlUpdate = "UPDATE kel7.pegawai SET id_jabatan = ? WHERE id_pegawai = ?";
        try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {
            psUpdate.setInt(1, jabBaru);
            psUpdate.setInt(2, idPeg);
            psUpdate.executeUpdate();
            System.out.println("[SUKSES] Jabatan pegawai berhasil diperbarui!");
        }
    } catch (Exception e) {
        System.out.println("Error mengubah jabatan: " + e.getMessage());
        }
    }
}

