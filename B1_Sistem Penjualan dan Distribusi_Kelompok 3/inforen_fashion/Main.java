import koneksi.Koneksi;
import model.*;
import dao.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Connection conn = Koneksi.connect();
        if (conn == null) {
            System.out.println("Koneksi database gagal. Program berhenti.");
            return;
        }
        System.out.println("Koneksi PostgreSQL berhasil! Selamat datang di InforEn Fashion.");

        AkunDAO akunDAO = new AkunDAO(conn);
        ProdukDAO produkDAO = new ProdukDAO(conn);
        PesananDAO pesananDAO = new PesananDAO(conn);
        PembayaranDAO pembayaranDAO = new PembayaranDAO(conn);
        PermintaanStokDAO permintaanDAO = new PermintaanStokDAO(conn);

        Scanner scanner = new Scanner(System.in);
        Akun userLogin = null;

        while (userLogin == null) {
            System.out.println("\n=== INFOREN FASHION ===");
            System.out.println("1. Login Akun");
            System.out.println("2. Daftar Akun Baru");
            System.out.println("3. Keluar");
            System.out.print("Pilih opsi: ");
            int pilihanAwal = scanner.nextInt();
            scanner.nextLine(); 

            if (pilihanAwal == 1) {
                System.out.print("Masukkan Username: ");
                String username = scanner.nextLine();
                System.out.print("Masukkan Password: ");
                String password = scanner.nextLine();

                userLogin = akunDAO.login(username, password);

                if (userLogin != null) {
                    System.out.println("\n[SUKSES] Login berhasil! Halo, " + userLogin.getNama() + " (" + userLogin.getRole() + ")");
                } else {
                    System.out.println("[GAGAL] Username atau password salah. Coba lagi.");
                }

            } else if (pilihanAwal == 2) {
                System.out.print("Username baru: ");
                String userBaru = scanner.nextLine();
                System.out.print("Password baru: ");
                String passBaru = scanner.nextLine();
                System.out.print("Nama Lengkap: ");
                String namaBaru = scanner.nextLine();
                System.out.print("Email: ");
                String emailBaru = scanner.nextLine();
                System.out.print("Role (PELANGGAN/PENJUAL/SUPPLIER): ");
                String roleBaru = scanner.nextLine();

                boolean berhasil = akunDAO.daftar(userBaru, passBaru, namaBaru, emailBaru, roleBaru);
                if (berhasil) {
                    System.out.println("[SUKSES] Akun berhasil didaftarkan! Silakan login.");
                } else {
                    System.out.println("[GAGAL] Pendaftaran gagal, cek kembali data anda.");
                }
            } else if (pilihanAwal == 3) {
                System.out.println("Terima kasih, sampai jumpa lagi.");
                return;
            }
        }

        boolean running = true;
        while (running) {
            userLogin.tampilkanMenu();
            System.out.print("Pilih menu: ");
            int pilihanMenu = scanner.nextInt();
            scanner.nextLine(); 

            if (userLogin instanceof Pelanggan) {
                Pelanggan pelanggan = (Pelanggan) userLogin;
                
                switch (pilihanMenu) {
                    case 1:
                        System.out.println("\n--- KATALOG PRODUK ---");
                        List<Produk> produkList = produkDAO.getAllProduk();
                        for (Produk p : produkList) {
                            System.out.println("ID: " + p.getIdProduk() + " | " + p.getNamaProduk() + " | Harga: Rp" + p.getHarga() + " | Stok: " + p.getStok());
                        }
                        break;
                    case 2:
                        List<DetailPesanan> keranjangBeli = new ArrayList<>();
                        String lanjutBelanja = "y";
                        
                        System.out.println("\n--- KERANJANG BELANJA PELANGGAN ---");
                        
                        while (lanjutBelanja.equalsIgnoreCase("y")) {
                            System.out.print("Masukkan ID Produk yang mau dibeli: ");
                            int idProduk = scanner.nextInt();
                            System.out.print("Jumlah beli: ");
                            int jumlahBeli = scanner.nextInt();
                            
                            DetailPesanan detail = new DetailPesanan(0, idProduk, jumlahBeli);
                            keranjangBeli.add(detail);
                            
                            System.out.print("Mau tambah produk lain ke keranjang? (y/n): ");
                            lanjutBelanja = scanner.next();
                        }
                        
                        if (!keranjangBeli.isEmpty()) {
                            Pesanan pesananBaru = new Pesanan(0, userLogin.getIdAkun(), null, 0, "PENDING");
                            
                            boolean suksesBeli = pesananDAO.buatPesanan(pesananBaru, keranjangBeli);
                            
                            if (suksesBeli) {
                                System.out.println("Semua barang di keranjang sukses dipesan!");
                            }
                        } else {
                            System.out.println("Keranjang kosong.");
                    }
                    case 3:
                    System.out.println("\n--- PEMBAYARAN PESANAN PELANGGAN ---");
                    System.out.print("Masukkan ID Pesanan yang mau dibayar: ");
                    int idPesanan = scanner.nextInt();
                    
                    Pesanan notaCek = pesananDAO.getPesananById(idPesanan);
                    
                    if (notaCek == null) {
                        System.out.println("[GAGAL] Nota pesanan ID #" + idPesanan + " tidak ditemukan di database!");
                    } else {
                        if (!notaCek.getStatusPesanan().equalsIgnoreCase("DIKONFIRMASI")) {
                            System.out.println("[DITOLAK] Pesanan ID #" + idPesanan + " belum dikonfirmasi oleh Penjual!");
                            System.out.println("Status saat ini: [" + notaCek.getStatusPesanan() + "]. Silakan hubungi penjual dahulu.");
                        } 
                        else if (notaCek.getStatusPesanan().equalsIgnoreCase("DIBAYAR") || notaCek.getStatusPesanan().equalsIgnoreCase("SELESAI")) {
                            System.out.println("[INFO] Pesanan ID #" + idPesanan + " ini sudah lunas dibayar!");
                        } 
                        else {
                            double totalTagihan = notaCek.getTotalHarga();
                            System.out.printf("Total Tagihan yang harus dibayar: Rp %.2f\n", totalTagihan);
                            
                            System.out.print("Masukkan Nominal Uang Tunai/Limit yang Dibayarkan: Rp ");
                            double uangDibayar = scanner.nextDouble();
                            
                            if (uangDibayar < totalTagihan) {
                                System.out.println("[GAGAL] Uang yang kamu masukkan kurang! Transaksi dibatalkan.");
                            } else {
                                System.out.print("Metode Pembayaran (TUNAI/KREDIT): ");
                                String metode = scanner.next();
                                
                                Pembayaran bayarBaru = new Pembayaran(0, idPesanan, new java.util.Date(), totalTagihan, metode, "DIBAYAR");
                                boolean suksesBayar = pembayaranDAO.prosesPembayaran(bayarBaru);
                                
                                if (suksesBayar) {
                                    
                                    System.out.println("\n[SUKSES] Pesanan #" + idPesanan + " berhasil dibayar.");
                                    
                                    double kembalian = uangDibayar - totalTagihan;
                                    if (kembalian > 0) {
                                        System.out.printf("Uang Kembalian Anda: Rp %.2f\n", kembalian);
                                    } else {
                                        System.out.println("Uang Pas, tidak ada kembalian.");
                                    }
                                    
                                    pembayaranDAO.cetakNota(idPesanan, totalTagihan, metode);
                                }
                            }
                        }
                    }
                    case 4:
                        running = false;
                        System.out.println("Terima kasih telah menggunakan aplikasi kami!");
                        break;
                    default:
                        System.out.println("Pilihan tidak valid.");
                }
            } 
            
            else if (userLogin instanceof Penjual) {
                Penjual penjual = (Penjual) userLogin; 
                
                switch (pilihanMenu) {
                    case 1:
                        System.out.print("Nama Produk Baru: ");
                        String namaP = scanner.nextLine();
                        System.out.print("Harga: ");
                        double hargaP = scanner.nextDouble();
                        System.out.print("Stok Awal: ");
                        int stokP = scanner.nextInt();
                        System.out.print("ID Kategori: ");
                        int katP = scanner.nextInt();

                        penjual.inputProdukBaru(produkDAO, namaP, hargaP, stokP, katP);
                        break;
                    case 2:
                        System.out.print("Masukkan ID Pesanan yang mau dikonfirmasi: ");
                        int idKonfirmasi = scanner.nextInt();
                        boolean suksesK = pesananDAO.konfirmasiPesanan(idKonfirmasi);
                        if (suksesK) {
                            System.out.println("Pesanan #" + idKonfirmasi + " berhasil dikonfirmasi oleh Penjual!");
                        }
                        break;
                    case 3:
                        pesananDAO.tampilkanLaporanPenjualan();
                    case 4:
                        running = false;
                        System.out.println("Terima kasih telah menggunakan aplikasi kami!");
                        break;
                    default:
                        System.out.println("Pilihan tidak valid.");
                }
            } 
            
            else if (userLogin instanceof Supplier) {
                Supplier supplier = (Supplier) userLogin;
                
                switch (pilihanMenu) {
                    case 1:
                        permintaanDAO.tampilkanPermintaanStok();
                    case 2:
                        System.out.print("Masukkan ID Produk yang mau disuplai: ");
                        int idSuplai = scanner.nextInt();
                        System.out.print("Jumlah Pasokan Stok: ");
                        int jmlSuplai = scanner.nextInt();

                        supplier.pasokBarang(produkDAO, idSuplai, jmlSuplai, 5); 
                        break;
                    case 3:
                        running = false;
                        System.out.println("Terima kasih telah menggunakan aplikasi kami!");
                        break;
                    default:
                        System.out.println("Pilihan tidak valid.");
                }
            }
        }
        scanner.close();
    }
}