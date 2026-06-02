package model;
import config.DatabaseConfig;
import java.sql.*;

public class Bos extends User {
    public Bos(int id, String nama, String email) { super(id, nama, email); }

    @Override
    public boolean tampilkanMenu() {
        System.out.println("\n--- MENU DIREKTUR (BOS) ---");
        System.out.println("1. Setujui Cuti Pegawai\n2. Buat Pengumuman Baru\n3. Daftar Pegawai\n4. Ubah/Naik Jabatan\n5. Lihat Laporan\n6. Berikan Gaji Karyawan/Manager\n7. Logout ");
        String pil = scanner.nextLine();
        if (pil.equals("1")) approveCuti();
        else if (pil.equals("2")) buatPengumuman();
        else if (pil.equals("3")) daftarPegawai();
        else if (pil.equals("4")) naikJabatan();
        else if (pil.equals("5")) lihatLaporan();
        else if (pil.equals("6")) berikanGaji();
        return !pil.equals("7");
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
    System.out.println("\n--- MANAJEMEN PROMOSI JABATAN STRUKTURAL ---");
    System.out.print("Masukkan ID Pegawai yang akan dipromosikan: ");
    int idPegawaiTarget = Integer.parseInt(scanner.nextLine());

    String sqlCek = "SELECT p.nama_lengkap, j.nama_jabatan FROM kel7.pegawai p " +
                    "JOIN kel7.jabatan j ON p.id_jabatan = j.id_jabatan WHERE p.id_pegawai = ?";
    
    Connection conn = null;
    try {
        conn = config.DatabaseConfig.getConnection();
        
        try (PreparedStatement psCek = conn.prepareStatement(sqlCek)) {
            psCek.setInt(1, idPegawaiTarget);
            try (ResultSet rs = psCek.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("Peringatan GAGAL: ID tidak ditemukan di dalam database.");
                    return;
                }
                System.out.println("Pegawai Ditemukan : " + rs.getString("nama_lengkap"));
                System.out.println("Jabatan Saat Ini  : " + rs.getString("nama_jabatan"));
            }
        }

        System.out.println("\nPilih Tingkatan Jabatan Baru:");
        System.out.println("1. Staff");
        System.out.println("2. Supervisor");
        System.out.println("3. Manager");
        System.out.print("Pilih ID Jabatan (1-3): ");
        int idJabatanBaru = Integer.parseInt(scanner.nextLine());

        double gajiPokokBaru = 0;
        String sqlAmbilGaji = "SELECT gaji_dasar FROM kel7.jabatan WHERE id_jabatan = ?";
        try (PreparedStatement psGaji = conn.prepareStatement(sqlAmbilGaji)) {
            psGaji.setInt(1, idJabatanBaru);
            try (ResultSet rsGaji = psGaji.executeQuery()) {
                if (rsGaji.next()) {
                    gajiPokokBaru = rsGaji.getDouble("gaji_dasar");
                }
            }
        }
        conn.setAutoCommit(false); 

        String sqlUpdatePegawai = "UPDATE kel7.pegawai SET id_jabatan = ?, gaji_pokok = ? WHERE id_pegawai = ?";
        try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdatePegawai)) {
            psUpdate.setInt(1, idJabatanBaru);
            psUpdate.setDouble(2, gajiPokokBaru);
            psUpdate.setInt(3, idPegawaiTarget);
            psUpdate.executeUpdate();
        }

        conn.commit(); 
        System.out.println("Sukses: Jabatan dan Gaji Pokok pegawai berhasil diperbarui secara sinkron.");

    } catch (Exception e) {
        if (conn != null) {
            try { 
                conn.rollback();
            } catch (SQLException se) { 
                System.out.println("Error Rollback: " + se.getMessage()); 
            }
        }
        System.out.println("Peringatan GAGAL: Proses modifikasi data dibatalkan karena: " + e.getMessage());
        }
    }

    public void berikanGaji() {
        System.out.println("\n--- PROSES EKSEKUSI PAYROLL BULANAN VIA POSTGRES FUNCTION ---");
        System.out.print("Masukkan ID Pegawai          : ");
        int idPegawaiTarget = Integer.parseInt(scanner.nextLine());
        System.out.print("Masukkan Periode Bulan (1-12): ");
        int bulanPeriode = Integer.parseInt(scanner.nextLine());
        System.out.print("Masukkan Periode Tahun       : ");
        int tahunPeriode = Integer.parseInt(scanner.nextLine());

        String sqlPanggilFunction = "SELECT kel7.sp_proses_payroll(?, ?, ?)";

        try (Connection conn = config.DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlPanggilFunction)) {
            
            ps.setInt(1, idPegawaiTarget);
            ps.setInt(2, bulanPeriode);
            ps.setInt(3, tahunPeriode);
            
            ps.execute();
            System.out.println("Sukses: Seluruh validasi hari kerja, kalkulasi denda, dan lembur berhasil diproses database.");
            System.out.println("Slip payroll bulanan berhasil diterbitkan resmi.");

        } catch (SQLException e) {
            System.out.println("\nPeringatan GAGAL: Eksekusi Payroll Dibatalkan!");
            System.out.println("Alasan: " + e.getMessage());
        }
    }

}

