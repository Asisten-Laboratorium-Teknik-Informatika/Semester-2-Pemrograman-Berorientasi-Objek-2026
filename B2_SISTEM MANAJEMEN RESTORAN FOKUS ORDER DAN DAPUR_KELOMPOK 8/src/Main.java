import java.sql.*;
import java.util.Scanner;

public class Main {
    private static Scanner input = new Scanner(System.in);
    
    public static void main(String[] args) {
        Database.getConn();
        
        int pilih;
        do {
            System.out.println("\n+==================================================+");
            System.out.println("|                RESTO MANAGER                     |");
            System.out.println("+==================================================+");
            System.out.println("| 1. Buat Pesanan Baru                             |");
            System.out.println("| 2. Lihat Semua Pesanan                           |");
            System.out.println("| 3. Detail Pesanan                                |");
            System.out.println("| 4. Lihat Antrian Dapur                           |");
            System.out.println("| 5. Ambil Pesanan (Dapur)                         |");
            System.out.println("| 6. Selesaikan Pesanan                            |");
            System.out.println("| 7. Update Status Pesanan                         |");
            System.out.println("| 8. Proses Pembayaran                             |");
            System.out.println("| 9. Lihat Riwayat Pembayaran                      |");
            System.out.println("| 10. Laporan Penjualan Hari Ini                   |");
            System.out.println("| 11. Menu Terlaris Hari Ini                       |");
            System.out.println("| 0. Keluar                                        |");
            System.out.println("+==================================================+");
            System.out.print("Pilihan anda: ");
            pilih = input.nextInt();
            input.nextLine();
            
            switch (pilih) {
                case 1:
                    buatPesanan();
                    break;
                case 2:
                    Pesanan.lihatSemua();
                    break;
                case 3:
                    detailPesanan();
                    break;
                case 4:
                    Dapur.lihatAntrian();
                    break;
                case 5:
                    ambilPesanan();
                    break;
                case 6:
                    selesaikanPesanan();
                    break;
                case 7:
                    updateStatus();
                    break;
                case 8:
                    prosesPembayaran();
                    break;
                case 9:
                    Pembayaran.lihatRiwayatPembayaran();
                    break;
                case 10:
                    Laporan.harian();
                    break;
                case 11:
                    Laporan.menuTerlaris();
                    break;
                case 0:
                    System.out.println("\nTerima kasih sudah menggunakan program ini");
                    Database.close();
                    break;
                default:
                    System.out.println("Pilihan tidak tersedia, coba lagi");
            }
        } while (pilih != 0);
    }
    
    private static void buatPesanan() {
        System.out.println("\n--- DAFTAR MEJA KOSONG ---");
        try {
            Statement st = Database.getConn().createStatement();
            ResultSet rs = st.executeQuery("SELECT id, nomor_meja, kapasitas FROM meja WHERE status = 'kosong'");
            while (rs.next()) {
                System.out.println("ID Meja: " + rs.getInt("id") + " | No Meja: " + rs.getInt("nomor_meja") + " | Kapasitas: " + rs.getInt("kapasitas"));
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        System.out.print("\nPilih ID Meja: ");
        int meja = input.nextInt();
        input.nextLine();
        System.out.print("Nama Pelanggan: ");
        String nama = input.nextLine();
        
        int id = Pesanan.buatBaru(meja, nama);
        if (id == -1) {
            System.out.println("Gagal buat pesanan");
            return;
        }
        
        System.out.println("\nSekarang tambahkan item pesanan");
        
        char lanjut;
        do {
            Menu.tampilkanMenuPilihan();
            
            System.out.print("Pilih ID Menu: ");
            int idMenu = input.nextInt();
            System.out.print("Jumlah: ");
            int jumlah = input.nextInt();
            input.nextLine();
            System.out.print("Catatan (opsional): ");
            String catatan = input.nextLine();
            
            if (Menu.cekStok(idMenu, jumlah)) {
                Pesanan.tambahItem(id, idMenu, jumlah, catatan);
                System.out.println("Item berhasil ditambahkan");
            } else {
                System.out.println("Stok tidak mencukupi");
            }
            
            System.out.print("\nTambah lagi? (y/n): ");
            lanjut = input.nextLine().charAt(0);
        } while (lanjut == 'y' || lanjut == 'Y');
        
        System.out.println("\nPesanan selesai. Total: Rp" + Pesanan.getTotal(id));
        System.out.println("Silakan lanjutkan ke pembayaran setelah pesanan selesai");
    }
    
    private static void detailPesanan() {
        System.out.print("ID Pesanan: ");
        int id = input.nextInt();
        Pesanan.lihatDetail(id);
        
        // cek status pembayaran
        try {
            Statement st = Database.getConn().createStatement();
            ResultSet rs = st.executeQuery("SELECT status_pembayaran FROM pesanan WHERE id = " + id);
            if (rs.next()) {
                String statusBayar = rs.getString("status_pembayaran");
                if (statusBayar.equals("lunas")) {
                    System.out.println("Status Pembayaran: SUDAH LUNAS");
                } else {
                    System.out.println("Status Pembayaran: BELUM BAYAR");
                }
            }
            rs.close();
            st.close();
        } catch (SQLException e) {}
    }
    
    private static void ambilPesanan() {
        Dapur.lihatAntrian();
        System.out.print("ID Pesanan yang akan diproses: ");
        int id = input.nextInt();
        Dapur.ambilPesanan(id);
    }
    
    private static void selesaikanPesanan() {
        System.out.print("ID Pesanan yang selesai: ");
        int id = input.nextInt();
        Dapur.selesaikanPesanan(id);
    }
    
    private static void updateStatus() {
        Pesanan.lihatSemua();
        System.out.print("ID Pesanan: ");
        int id = input.nextInt();
        input.nextLine();
        System.out.print("Status baru (diproses/selesai/batal): ");
        String sts = input.nextLine();
        Pesanan.updateStatus(id, sts);
    }
    
    private static void prosesPembayaran() {
        // tampilkan pesanan yang belum dibayar
        System.out.println("\n--- PESANAN YANG BELUM DIBAYAR ---");
        try {
            Statement st = Database.getConn().createStatement();
            ResultSet rs = st.executeQuery("SELECT p.id, p.no_invoice, p.nama_pelanggan, p.total_harga " +
                                           "FROM pesanan p WHERE p.status_pembayaran = 'belum' AND p.status != 'batal'");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + " | Invoice: " + rs.getString("no_invoice") + 
                                   " | Pelanggan: " + rs.getString("nama_pelanggan") + " | Total: Rp" + rs.getInt("total_harga"));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        System.out.print("\nMasukkan ID Pesanan: ");
        int id = input.nextInt();
        
        // cek apakah pesanan sudah dibayar
        if (Pembayaran.isSudahDibayar(id)) {
            System.out.println("Pesanan ini sudah dibayar sebelumnya");
            return;
        }
        
        int total = Pesanan.getTotal(id);
        System.out.println("Total yang harus dibayar: Rp " + total);
        
        System.out.println("\nPilih Metode Pembayaran:");
        System.out.println("1. Tunai");
        System.out.println("2. Qris");
        System.out.println("3. Debit");
        System.out.println("4. Kredit");
        System.out.print("Pilihan metode (1-4): ");
        int pilihMetode = input.nextInt();
        
        String metode = "";
        switch (pilihMetode) {
            case 1:
                metode = "tunai";
                break;
            case 2:
                metode = "qris";
                break;
            case 3:
                metode = "debit";
                break;
            case 4:
                metode = "kredit";
                break;
            default:
                metode = "tunai";
                System.out.println("Pilihan tidak ada, menggunakan tunai");
        }
        
        System.out.print("Jumlah uang: Rp ");
        int bayar = input.nextInt();
        
        Pembayaran.prosesPembayaran(id, metode, bayar);
    }
}