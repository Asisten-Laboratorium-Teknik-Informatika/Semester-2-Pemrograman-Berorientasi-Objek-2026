package src.main.java.com.perpus.service;

import src.main.java.com.perpus.config.Koneksi;
import src.main.java.com.perpus.model.Anggota;
import src.main.java.com.perpus.model.Buku;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PerpustakaanService {

    // --- 1. MENAMPILKAN KATALOG BUKU ---
    public void tampilkanKatalog() {
        List<Buku> listBuku = new ArrayList<>();
        String query = "SELECT id_buku, isbn, judul, nama_kategori, stok_tersedia FROM v_katalog_tersedia";

        try (Connection conn = Koneksi.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Buku buku = new Buku(
                    rs.getInt("id_buku"),
                    rs.getString("isbn"),
                    rs.getString("judul"),
                    rs.getString("nama_kategori"),
                    rs.getInt("stok_tersedia")
                );
                listBuku.add(buku);
            }
        } catch (SQLException e) {
            System.out.println(" Gagal Terhubung ke DatabaseConfig: " + e.getMessage());
            return;
        }

        System.out.println("\n==========================================================================================================");
        System.out.println("                                      KATALOG BUKU LIVE PERPUSTAKAAN                                      ");
        System.out.println("==========================================================================================================");
        System.out.printf("%-6s %-20s %-42s %-22s %-10s\n", "ID", "ISBN", "JUDUL BUKU", "KATEGORI", "STOK");
        System.out.println("----------------------------------------------------------------------------------------------------------");
        
        for (Buku b : listBuku) {
            String judul = b.getJudul();
            String kategori = b.getNamaKategori();

            if (judul.length() > 38) {
                judul = judul.substring(0, 35) + "...";
            }

            if (kategori.length() > 19) {
                kategori = kategori.substring(0, 16) + "...";
            }

            System.out.printf("%-6d %-20s %-42s %-22s %-10d\n", 
                b.getIdBuku(), 
                b.getIsbn(), 
                judul, 
                kategori, 
                b.getStokTersedia()
            );
        }
        System.out.println("==========================================================================================================");
    }

    // --- 2. MENAMPILKAN DAFTAR ANGGOTA ---
    public void tampilkanDaftarAnggota() {
        List<Anggota> listAnggota = new ArrayList<>();
        String query = "SELECT id_anggota, nomor_anggota, nama_lengkap, tanggal_expired, status_aktif FROM anggota";

        try (Connection conn = Koneksi.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Date expDate = rs.getDate("tanggal_expired");
                boolean aktif = rs.getBoolean("status_aktif");
                String statusText = (aktif && expDate.toLocalDate().isAfter(LocalDate.now())) ? "Aktif" : "Expired";

                Anggota anggota = new Anggota(
                    rs.getInt("id_anggota"),
                    rs.getString("nomor_anggota"),
                    rs.getString("nama_lengkap"),
                    statusText,
                    expDate.toLocalDate()
                );
                listAnggota.add(anggota);
            }
        } catch (SQLException e) {
            System.out.println(" Gagal Terhubung ke DatabaseConfig: " + e.getMessage());
            return;
        }

        System.out.println("\n==================================== DAFTAR ANGGOTA PERPUSTAKAAN ====================================");
        System.out.printf("%-5s %-15s %-25s %-15s %-15s\n", "ID", "NO ANGGOTA", "NAMA LENGKAP", "STATUS", "EXP DATE");
        System.out.println("-----------------------------------------------------------------------------------------------------");
        for (Anggota a : listAnggota) {
            System.out.printf("%-5d %-15s %-25s %-15s %-15s\n", 
                a.getIdAnggota(), a.getNomorAnggota(), a.getNamaLengkap(), a.getStatus(), a.getTanggalExpired());
        }
        System.out.println("=====================================================================================================");
    }

    // --- 3. TRANSAKSI PEMINJAMAN BARU ---
    public void prosesPeminjaman(int idAnggota, int idBuku, int idPetugas) {
        String insertPeminjaman = "INSERT INTO peminjaman (kode_peminjaman, id_anggota, id_petugas, tanggal_jatuh_tempo) " +
                                "VALUES (generate_kode_peminjaman(), ?, ?, CURRENT_DATE + INTERVAL '14 days') RETURNING id_peminjaman";
        String insertDetail = "INSERT INTO detail_peminjaman (id_peminjaman, id_buku, kondisi_awal) VALUES (?, ?, 'Baik')";

        Connection conn = null;
        try {
            conn = Koneksi.getConnection();
            conn.setAutoCommit(false);

            int idPeminjaman = 0;
            try (PreparedStatement pstmt1 = conn.prepareStatement(insertPeminjaman)) {
                pstmt1.setInt(1, idAnggota);
                pstmt1.setInt(2, idPetugas);
                try (ResultSet rs = pstmt1.executeQuery()) {
                    if (rs.next()) idPeminjaman = rs.getInt(1);
                }
            }

            try (PreparedStatement pstmt2 = conn.prepareStatement(insertDetail)) {
                pstmt2.setInt(1, idPeminjaman);
                pstmt2.setInt(2, idBuku);
                pstmt2.executeUpdate();
            }

            conn.commit();
            System.out.println(" SUKSES: Data transaksi peminjaman berhasil disimpan ke PostgreSQL.");

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Jika gagal di tengah jalan, batalkan semua agar database konsisten
                    System.out.println("⚠ TRANSAKSI GAGAL: Terjadi Rollback otomatis.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            // Mengambil pesan kustom dari TRIGGER PostgreSQL (contoh: Stok Buku Habis / Anggota Expired)
            System.out.println("❌ PESAN DB: " + e.getMessage());
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // --- 4. TRANSAKSI PENGEMBALIAN & DENDA ---
    public void prosesPengembalian(String kodePeminjaman) {
        String queryUpdate = "UPDATE peminjaman SET tanggal_kembali = CURRENT_DATE, status_peminjaman = 'Dikembalikan' WHERE kode_peminjaman = ?";
        String queryCekDenda = "SELECT b.judul, p.tanggal_jatuh_tempo, d.jumlah_hari_telat, d.biaya_denda, d.status_bayar " +
                                "FROM peminjaman p " +
                                "JOIN detail_peminjaman dp ON p.id_peminjaman = dp.id_peminjaman " +
                                "JOIN buku b ON dp.id_buku = b.id_buku " +
                                "LEFT JOIN denda d ON p.id_peminjaman = d.id_peminjaman " +
                                "WHERE p.kode_peminjaman = ?";

        try (Connection conn = Koneksi.getConnection()) {
            
            // 1. Jalankan Update Pengembalian
            try (PreparedStatement pstmt = conn.prepareStatement(queryUpdate)) {
                pstmt.setString(1, kodePeminjaman);
                int rowsAffected = pstmt.executeUpdate();
                
                if (rowsAffected == 0) {
                    System.out.println("❌ GAGAL: Kode peminjaman tidak terdaftar atau buku sudah dikembalikan!");
                    return;
                }
            }

            // 2. Baca Kalkulasi Denda Hasil Trigger/Function PostgreSQL
            try (PreparedStatement pstmtCek = conn.prepareStatement(queryCekDenda)) {
                pstmtCek.setString(1, kodePeminjaman);
                try (ResultSet rs = pstmtCek.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("\n================ PENGEMBALIAN BERHASIL ================");
                        System.out.println("Judul Buku          : " + rs.getString("judul"));
                        System.out.println("Tanggal Jatuh Tempo : " + rs.getDate("tanggal_jatuh_tempo"));
                        System.out.println("Tanggal Kembali     : " + LocalDate.now());
                        
                        int hariTelat = rs.getInt("jumlah_hari_telat");
                        if (hariTelat > 0) {
                            System.out.println("Status              : ⚠ TERLAMBAT " + hariTelat + " HARI");
                            System.out.println("Total Denda         : Rp " + rs.getDouble("biaya_denda") + " [" + rs.getString("status_bayar") + "]");
                        } else {
                            System.out.println("Status              : ✓ TEPAT WAKTU (Bebas Denda)");
                        }
                        System.out.println("=======================================================");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Gagal Terhubung ke DatabaseConfig: " + e.getMessage());
        }
    }
}
