package dao;

import model.Pesanan;
import model.DetailPesanan;
import java.sql.*;
import java.util.List;

public class PesananDAO {
    private Connection connection;

    public PesananDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean buatPesanan(Pesanan pesanan, List<DetailPesanan> listDetail) {
        double totalHargaAsli = 0;
        
        // 1. Hitung total harga keseluruhan
        for (DetailPesanan detail : listDetail) {
            double hargaProduk = 0;
            String queryCekHarga = "SELECT harga FROM produk WHERE id_produk = ?";
            try (PreparedStatement psCek = connection.prepareStatement(queryCekHarga)) {
                psCek.setInt(1, detail.getIdProduk());
                try (ResultSet rsCek = psCek.executeQuery()) {
                    if (rsCek.next()) {
                        hargaProduk = rsCek.getDouble("harga");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Gagal cek harga produk: " + e.getMessage());
                return false;
            }
            totalHargaAsli += (hargaProduk * detail.getJumlah());
        }
        
        String queryPesanan = "INSERT INTO pesanan (id_pelanggan, tanggal_pesanan, total_harga, status_pesanan) VALUES (?, ?, ?, ?)";
        String queryDetail = "INSERT INTO detail_pesanan (id_pesanan, id_produk, jumlah, subtotal) VALUES (?, ?, ?, ?)";
        String queryUpdateStok = "UPDATE produk SET stok = stok - ? WHERE id_produk = ?";
        
        try {
            connection.setAutoCommit(false);
            
            // Input ke tabel pesanan utama
            try (PreparedStatement psPesanan = connection.prepareStatement(queryPesanan, Statement.RETURN_GENERATED_KEYS)) {
                psPesanan.setInt(1, pesanan.getIdPelanggan());
                psPesanan.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                psPesanan.setDouble(3, totalHargaAsli); 
                psPesanan.setString(4, "PENDING");
                
                int affectedRows = psPesanan.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Gagal membuat data pesanan utama.");
                }
                
                int idPesananBaru = 0;
                try (ResultSet generatedKeys = psPesanan.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        idPesananBaru = generatedKeys.getInt(1);
                        pesanan.setIdPesanan(idPesananBaru);
                    }
                }
                
                try (PreparedStatement psDetail = connection.prepareStatement(queryDetail);
                     PreparedStatement psStok = connection.prepareStatement(queryUpdateStok)) {
                    
                    for (DetailPesanan detail : listDetail) {
                        double hargaProdukSatuan = 0;
                        String queryHargaItem = "SELECT harga FROM produk WHERE id_produk = ?";
                        try (PreparedStatement psHrg = connection.prepareStatement(queryHargaItem)) {
                            psHrg.setInt(1, detail.getIdProduk());
                            try (ResultSet rsHrg = psHrg.executeQuery()) {
                                if (rsHrg.next()) {
                                    hargaProdukSatuan = rsHrg.getDouble("harga");
                                }
                            }
                        }
                        
                        double subtotalItem = hargaProdukSatuan * detail.getJumlah();

                        psDetail.setInt(1, idPesananBaru);       
                        psDetail.setInt(2, detail.getIdProduk());     
                        psDetail.setInt(3, detail.getJumlah());       
                        psDetail.setDouble(4, subtotalItem);          
                        psDetail.addBatch();
                        
                        psStok.setInt(1, detail.getJumlah()); 
                        psStok.setInt(2, detail.getIdProduk()); 
                        psStok.addBatch();
                    }
                    
                    psDetail.executeBatch(); 
                    psStok.executeBatch(); 
                }
                
                for (DetailPesanan detail : listDetail) {
                    String queryCekStokSekarang = "SELECT stok, nama_produk FROM produk WHERE id_produk = ?";
                    try (PreparedStatement psCekStok = connection.prepareStatement(queryCekStokSekarang)) {
                        psCekStok.setInt(1, detail.getIdProduk());
                        try (ResultSet rsStok = psCekStok.executeQuery()) {
                            if (rsStok.next()) {
                                int stokSekarang = rsStok.getInt("stok");
                                String namaProduk = rsStok.getString("nama_produk");
                                
                                if (stokSekarang < 10) {
                                    String queryCekRequest = "SELECT COUNT(*) FROM permintaan_stok WHERE id_produk = ? AND status_permintaan = 'PENDING'";
                                    try (PreparedStatement psCekReq = connection.prepareStatement(queryCekRequest)) {
                                        psCekReq.setInt(1, detail.getIdProduk());
                                        try (ResultSet rsReq = psCekReq.executeQuery()) {
                                            if (rsReq.next() && rsReq.getInt(1) == 0) {
                                                String queryInsertMinta = "INSERT INTO permintaan_stok (id_produk, jumlah_minta, status_permintaan) VALUES (?, 50, 'PENDING')";
                                                try (PreparedStatement psMinta = connection.prepareStatement(queryInsertMinta)) {
                                                    psMinta.setInt(1, detail.getIdProduk());
                                                    psMinta.executeUpdate();
                                                    System.out.println("[PERINGATAN] Stok " + namaProduk + " sisa " + stokSekarang + " pcs! Permintaan restock otomatis ke Supplier telah dikirim.");
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
                connection.commit();
                System.out.println("\nSukses! Pesanan berhasil dibuat dengan ID: " + idPesananBaru);
                return true;
            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Transaksi dibatalkan karena eror: " + e.getMessage());
                return false;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.out.println("Gagal transaksi: " + e.getMessage());
            return false;
        }
    }
    
    public boolean konfirmasiPesanan(int idPesanan) {
        String query = "UPDATE pesanan SET status_pesanan = 'DIKONFIRMASI' WHERE id_pesanan = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idPesanan);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public Pesanan getPesananById(int idPesanan) {
        String query = "SELECT id_pesanan, id_pelanggan, total_harga, status_pesanan FROM pesanan WHERE id_pesanan = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idPesanan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Pesanan(
                        rs.getInt("id_pesanan"),
                        rs.getInt("id_pelanggan"),
                        null, 
                        rs.getDouble("total_harga"),
                        rs.getString("status_pesanan")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengambil data pesanan: " + e.getMessage());
        }
        return null;
    }

    public void tampilkanLaporanPenjualan() {
        System.out.println("\n=========================================");
        System.out.println("       LAPORAN PENJUALAN TOKO            ");
        System.out.println("          INFOREN FASHION                ");
        System.out.println("=========================================");
        
        String queryRingkasan = "SELECT COUNT(id_pesanan) AS total_nota, SUM(total_harga) AS total_omzet " +
                                "FROM pesanan WHERE status_pesanan IN ('DIKONFIRMASI', 'SELESAI')";
        
        String queryDetailTerjual = "SELECT p.nama_produk, SUM(dp.jumlah) AS total_terjual, SUM(dp.subtotal) AS total_pendapatan_produk " +
                                    "FROM detail_pesanan dp " +
                                    "JOIN produk p ON dp.id_produk = p.id_produk " +
                                    "JOIN pesanan pe ON dp.id_pesanan = pe.id_pesanan " +
                                    "WHERE pe.status_pesanan IN ('DIKONFIRMASI', 'SELESAI') " +
                                    "GROUP BY p.nama_produk " +
                                    "ORDER BY total_terjual DESC";

        try {
            try (Statement stmt = connection.createStatement();
                 ResultSet rsRingkasan = stmt.executeQuery(queryRingkasan)) {
                if (rsRingkasan.next()) {
                    int totalNota = rsRingkasan.getInt("total_nota");
                    double totalOmzet = rsRingkasan.getDouble("total_omzet");
                    
                    System.out.printf("Total Pesanan Sukses : %d Transaksi\n", totalNota);
                    System.out.printf("Total Omzet Pendapatan: Rp %.2f\n", totalOmzet);
                }
            }
            
            System.out.println("-----------------------------------------");
            System.out.printf("%-20s %-8s %-12s\n", "Nama Produk", "Terjual", "Subtotal");
            System.out.println("-----------------------------------------");
            
            try (Statement stmtDetail = connection.createStatement();
                 ResultSet rsDetail = stmtDetail.executeQuery(queryDetailTerjual)) {
                boolean adaData = false;
                while (rsDetail.next()) {
                    adaData = true;
                    String namaProd = rsDetail.getString("nama_produk");
                    int terjual = rsDetail.getInt("total_terjual");
                    double subtotalProd = rsDetail.getDouble("total_pendapatan_produk");
                    
                    if (namaProd.length() > 20) {
                        namaProd = namaProd.substring(0, 17) + "...";
                    }
                    
                    System.out.printf("%-20s %-8d Rp %-12.2f\n", namaProd, terjual, subtotalProd);
                }
                if (!adaData) {
                    System.out.println("Belum ada produk yang terjual.");
                }
            }
            System.out.println("=========================================\n");
            
        } catch (SQLException e) {
            System.out.println("Gagal menarik data laporan: " + e.getMessage());
        }
    }
    
}