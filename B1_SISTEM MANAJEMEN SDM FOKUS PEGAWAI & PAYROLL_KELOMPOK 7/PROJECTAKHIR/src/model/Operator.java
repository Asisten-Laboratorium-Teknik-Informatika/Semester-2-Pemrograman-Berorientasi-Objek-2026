package model;
import config.DatabaseConfig;
import java.sql.*;

public class Operator extends User {
    public Operator(int id, String nama, String email) { super(id, nama, email); }

    @Override
    public boolean tampilkanMenu() {
        System.out.println("\n--- MANAJEMEN OPERATOR ---");
        System.out.println("1. Daftar Antrean Approval");
        System.out.println("2. Nonaktifkan Akun");
        System.out.println("3. Hapus Permanen");
        System.out.println("4. Logout");
        System.out.print("Pilih: ");
        String pil = scanner.nextLine();

        switch(pil) {
            case "1" -> listPendingApproval();
            case "2" -> softDeleteAkun();
            case "3" -> hardDeleteAkun();
            case "4" -> { return false; }
        }
        return true;
    }

    public void listPendingApproval() {
    System.out.println("\n===========================================");
    System.out.println("      ANTREAN PERSETUJUAN AKUN BARU        ");
    System.out.println("===========================================");
    
    String sql = "SELECT id_akun, nama, email FROM kel7.user_akun WHERE is_approved = FALSE";

    try (Connection conn = DatabaseConfig.getConnection();
         Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery(sql)) {

        System.out.printf("%-10s | %-20s | %-25s\n", "ID Akun", "Nama", "Email");
        System.out.println("------------------------------------------------------------");
        
        boolean adaAntrean = false;
        while (rs.next()) {
            adaAntrean = true;
            System.out.printf("%-10d | %-20s | %-25s\n", rs.getInt(1), rs.getString(2), rs.getString(3));
        }

        if (!adaAntrean) {
            System.out.println("               TIDAK ADA ANTREAN BARU                       ");
            System.out.println("------------------------------------------------------------");
            return;
        }
        System.out.println("------------------------------------------------------------");

        System.out.print("\nMasukkan ID Akun yang ingin Anda APPROVE: ");
        int idApprove = Integer.parseInt(scanner.nextLine());

        String sqlApp = "UPDATE kel7.user_akun SET is_approved = TRUE, is_active = TRUE WHERE id_akun = ?";
        try (PreparedStatement psApp = conn.prepareStatement(sqlApp)) {
            psApp.setInt(1, idApprove);
            int baris = psApp.executeUpdate();
            if (baris > 0) {
                System.out.println("[SUKSES] Akun telah aktif! Pegawai sekarang sudah bisa login.");
            } else {
                System.out.println("[GAGAL] ID Akun salah / tidak ada di dalam antrean.");
            }
        }
    } catch (Exception e) {
        System.out.println("Error sistem approval: " + e.getMessage());
    }
}

    private void softDeleteAkun() {
    System.out.print("Masukkan ID Akun yang akan dinonaktifkan: ");
    int id = Integer.parseInt(scanner.nextLine());

    try (Connection conn = config.DatabaseConfig.getConnection()) {
        String sql = "UPDATE kel7.user_akun SET is_active = FALSE, is_approved = FALSE WHERE id_akun = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        
        if (ps.executeUpdate() > 0) {
            System.out.println("[BERHASIL] Akun dinonaktifkan. Pegawai tidak bisa login lagi.");
        } else {
            System.out.println("[GAGAL] ID Akun tidak ditemukan.");
        }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void hardDeleteAkun() {
    System.out.print("ID Akun yang akan DIHAPUS PERMANEN: ");
    int id = Integer.parseInt(scanner.nextLine());
    System.out.print("PERINGATAN: Semua riwayat gaji & laporan akan hilang! Lanjutkan? (y/n): ");
    if (!scanner.nextLine().equalsIgnoreCase("y")) return;

    String sqlCall = "CALL kel7.sp_hard_delete_akun(?)";

    try (Connection conn = config.DatabaseConfig.getConnection();
         PreparedStatement ps = conn.prepareStatement(sqlCall)) {
         
        ps.setInt(1, id);
        ps.execute(); 
        
        System.out.println("[BERHASIL] Data telah dihapus sepenuhnya dari sistem (Aman dengan Transaksi di Postgres).");
    } catch (SQLException e) {
        System.out.println("[GAGAL] " + e.getMessage());
    } catch (Exception e) { 
        e.printStackTrace(); 
    }
    }
}