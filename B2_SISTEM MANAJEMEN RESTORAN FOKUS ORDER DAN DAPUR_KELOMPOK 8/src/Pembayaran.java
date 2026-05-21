import java.sql.*;

public class Pembayaran {
    
    // method untuk memproses pembayaran
    public static void prosesPembayaran(int idPesanan, String metodeBayar, int jumlahBayar) {
        // ambil data pesanan terlebih dahulu
        int totalHarga = 0;
        String namaPelanggan = "";
        int idMeja = 0;
        
        try {
            Statement st = Database.getConn().createStatement();
            
            // ambil total harga dan nama pelanggan
            ResultSet rs = st.executeQuery("SELECT total_harga, nama_pelanggan, meja_id FROM pesanan WHERE id = " + idPesanan);
            if (rs.next()) {
                totalHarga = rs.getInt("total_harga");
                namaPelanggan = rs.getString("nama_pelanggan");
                idMeja = rs.getInt("meja_id");
            } else {
                System.out.println("Pesanan tidak ditemukan");
                rs.close();
                st.close();
                return;
            }
            rs.close();
            
            // cek apakah pesanan sudah dibayar
            ResultSet rsCek = st.executeQuery("SELECT id FROM pembayaran WHERE pesanan_id = " + idPesanan);
            if (rsCek.next()) {
                System.out.println("Pesanan ini sudah dibayar sebelumnya");
                rsCek.close();
                st.close();
                return;
            }
            rsCek.close();
            
            // hitung kembalian
            int kembalian = jumlahBayar - totalHarga;
            
            if (jumlahBayar < totalHarga) {
                System.out.println("Uang kurang! Kurang Rp " + (totalHarga - jumlahBayar));
                st.close();
                return;
            }
            
            // simpan data pembayaran ke database
            String sqlBayar = "INSERT INTO pembayaran (pesanan_id, metode_bayar, jumlah_bayar, jumlah_kembalian, id_kasir) " +
                              "VALUES (" + idPesanan + ", '" + metodeBayar + "', " + jumlahBayar + ", " + kembalian + ", 1)";
            st.executeUpdate(sqlBayar);
            
            // update status pesanan menjadi lunas dan selesai
            st.executeUpdate("UPDATE pesanan SET status_pembayaran = 'lunas', status = 'selesai', waktu_selesai = CURRENT_TIMESTAMP WHERE id = " + idPesanan);
            
            // kosongkan meja
            st.executeUpdate("UPDATE meja SET status = 'kosong' WHERE id = " + idMeja);
            
            // tampilkan struk pembayaran
            System.out.println("\n============================================================");
            System.out.println("                    STRUK PEMBAYARAN");
            System.out.println("============================================================");
            System.out.println("No Invoice    : " + getNoInvoice(idPesanan));
            System.out.println("Nama Pelanggan: " + namaPelanggan);
            System.out.println("Nomor Meja    : " + idMeja);
            System.out.println("------------------------------------------------------------");
            System.out.println("Total Belanja : Rp " + totalHarga);
            System.out.println("Metode Bayar  : " + metodeBayar);
            System.out.println("Jumlah Bayar  : Rp " + jumlahBayar);
            System.out.println("Kembalian     : Rp " + kembalian);
            System.out.println("============================================================");
            System.out.println("Terima kasih " + namaPelanggan + "!");
            System.out.println("Selamat menikmati makanannya");
            System.out.println("============================================================\n");
            
            st.close();
            
        } catch (SQLException e) {
            System.out.println("Error proses pembayaran: " + e.getMessage());
        }
    }
    
    // method untuk mendapatkan no invoice
    private static String getNoInvoice(int idPesanan) {
        String noInvoice = "";
        try {
            Statement st = Database.getConn().createStatement();
            ResultSet rs = st.executeQuery("SELECT no_invoice FROM pesanan WHERE id = " + idPesanan);
            if (rs.next()) {
                noInvoice = rs.getString("no_invoice");
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error ambil no invoice: " + e.getMessage());
        }
        return noInvoice;
    }
    
    // method untuk melihat riwayat pembayaran
    public static void lihatRiwayatPembayaran() {
        String sql = "SELECT p.id, p.no_invoice, p.nama_pelanggan, py.metode_bayar, " +
                     "py.jumlah_bayar, py.waktu_bayar " +
                     "FROM pembayaran py " +
                     "JOIN pesanan p ON py.pesanan_id = p.id " +
                     "ORDER BY py.waktu_bayar DESC LIMIT 10";
        
        try {
            Statement st = Database.getConn().createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            System.out.println("\n============================================================");
            System.out.println("                 RIWAYAT PEMBAYARAN");
            System.out.println("============================================================");
            System.out.println("ID  No Invoice      Pelanggan           Metode   Jumlah       Waktu");
            System.out.println("------------------------------------------------------------");
            
            while (rs.next()) {
                System.out.printf("%-3d %-15s %-18s %-7s Rp%-9d %s\n",
                    rs.getInt("id"),
                    rs.getString("no_invoice"),
                    rs.getString("nama_pelanggan"),
                    rs.getString("metode_bayar"),
                    rs.getInt("jumlah_bayar"),
                    rs.getTimestamp("waktu_bayar").toString().substring(0, 16));
            }
            System.out.println("============================================================\n");
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error lihat riwayat: " + e.getMessage());
        }
    }
    
    // method untuk cek apakah pesanan sudah dibayar
    public static boolean isSudahDibayar(int idPesanan) {
        try {
            Statement st = Database.getConn().createStatement();
            ResultSet rs = st.executeQuery("SELECT id FROM pembayaran WHERE pesanan_id = " + idPesanan);
            boolean sudah = rs.next();
            rs.close();
            st.close();
            return sudah;
        } catch (SQLException e) {
            System.out.println("Error cek pembayaran: " + e.getMessage());
        }
        return false;
    }
    
    // method untuk mendapatkan sisa tagihan (jika DP)
    public static int getSisaTagihan(int idPesanan) {
        int total = 0;
        int sudahDibayar = 0;
        
        try {
            Statement st = Database.getConn().createStatement();
            
            // ambil total harga
            ResultSet rs = st.executeQuery("SELECT total_harga FROM pesanan WHERE id = " + idPesanan);
            if (rs.next()) {
                total = rs.getInt("total_harga");
            }
            rs.close();
            
            // ambil jumlah yang sudah dibayar
            ResultSet rsBayar = st.executeQuery("SELECT jumlah_bayar FROM pembayaran WHERE pesanan_id = " + idPesanan);
            if (rsBayar.next()) {
                sudahDibayar = rsBayar.getInt("jumlah_bayar");
            }
            rsBayar.close();
            st.close();
            
        } catch (SQLException e) {
            System.out.println("Error hitung sisa: " + e.getMessage());
        }
        
        return total - sudahDibayar;
    }
}