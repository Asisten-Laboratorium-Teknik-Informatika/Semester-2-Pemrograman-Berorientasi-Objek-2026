package com.penjualan.ui;

import com.penjualan.dao.*;
import com.penjualan.entity.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuUtama {
    private static Scanner scanner = new Scanner(System.in);
    
    private static BarangDAO barangDAO = new BarangDAO();
    private static SupplierDAO supplierDAO = new SupplierDAO();
    private static PelangganDAO pelangganDAO = new PelangganDAO();
    private static KategoriDAO kategoriDAO = new KategoriDAO();
    private static UserDAO userDAO = new UserDAO();
    private static TransaksiDAO transaksiDAO = new TransaksiDAO();
    private static DetailTransaksiDAO detailTransaksiDAO = new DetailTransaksiDAO();
    private static PembayaranDAO pembayaranDAO = new PembayaranDAO();
    private static PengirimanDAO pengirimanDAO = new PengirimanDAO();
    
    public static void mainMenu() {
        int pilihan;
        do {
            System.out.println("\n????????????????????????????????????????????????????????????????????");
            System.out.println("?                          MENU UTAMA                              ?");
            System.out.println("????????????????????????????????????????????????????????????????????");
            System.out.println("?  1. Manajemen Barang                                             ?");
            System.out.println("?  2. Manajemen Supplier                                           ?");
            System.out.println("?  3. Manajemen Pelanggan                                          ?");
            System.out.println("?  4. Manajemen Kategori                                           ?");
            System.out.println("?  5. Manajemen User                                               ?");
            System.out.println("?  6. Transaksi Penjualan                                          ?");
            System.out.println("?  7. Pembayaran                                                   ?");
            System.out.println("?  8. Pengiriman                                                   ?");
            System.out.println("?  9. Laporan                                                      ?");
            System.out.println("?  0. Keluar                                                       ?");
            System.out.println("????????????????????????????????????????????????????????????????????");
            System.out.print("Pilih menu: ");
            pilihan = scanner.nextInt();
            scanner.nextLine();
            
            switch (pilihan) {
                case 1: menuBarang(); break;
                case 2: menuSupplier(); break;
                case 3: menuPelanggan(); break;
                case 4: menuKategori(); break;
                case 5: menuUser(); break;
                case 6: menuTransaksi(); break;
                case 7: menuPembayaran(); break;
                case 8: menuPengiriman(); break;
                case 9: menuLaporan(); break;
                case 0: System.out.println("\n?? Terima kasih telah menggunakan sistem!"); break;
                default: System.out.println("\n? Pilihan tidak valid!");
            }
        } while (pilihan != 0);
    }
    
    // ==================== MENU BARANG ====================
    private static void menuBarang() {
        int pilihan;
        do {
            System.out.println("\n????????????????????????????????????????????????????????????????????");
            System.out.println("?                         MANAJEMEN BARANG                          ?");
            System.out.println("????????????????????????????????????????????????????????????????????");
            System.out.println("?  1. Lihat Barang                                                  ?");
            System.out.println("?  2. Tambah Barang                                                 ?");
            System.out.println("?  3. Edit Barang                                                   ?");
            System.out.println("?  4. Hapus Barang                                                  ?");
            System.out.println("?  5. Cari Barang                                                   ?");
            System.out.println("?  0. Kembali                                                       ?");
            System.out.println("????????????????????????????????????????????????????????????????????");
            System.out.print("Pilih: ");
            pilihan = scanner.nextInt();
            scanner.nextLine();
            
            switch (pilihan) {
                case 1: lihatBarang(); break;
                case 2: tambahBarang(); break;
                case 3: editBarang(); break;
                case 4: hapusBarang(); break;
                case 5: cariBarang(); break;
            }
        } while (pilihan != 0);
    }
    
    private static void lihatBarang() {
        List<Barang> list = barangDAO.getAll();
        if (list.isEmpty()) {
            System.out.println("\n?? Belum ada data barang.");
        } else {
            System.out.println("\n?? DAFTAR BARANG");
            System.out.println("================================================================================");
            System.out.printf("%-5s %-25s %-15s %-10s\n", "ID", "Nama Barang", "Harga", "Stok");
            System.out.println("================================================================================");
            for (Barang b : list) {
                System.out.printf("%-5d %-25s Rp%-13.0f %-10d\n", b.getId(), b.getNamaBarang(), b.getHarga(), b.getStok());
            }
            System.out.println("================================================================================");
        }
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    private static void tambahBarang() {
        System.out.println("\n? TAMBAH BARANG");
        System.out.print("ID Barang: ");
        int id = scanner.nextInt(); scanner.nextLine();
        System.out.print("Nama Barang: ");
        String nama = scanner.nextLine();
        System.out.print("Harga: Rp ");
        double harga = scanner.nextDouble();
        System.out.print("Stok: ");
        int stok = scanner.nextInt(); scanner.nextLine();
        
        Barang b = new Barang(id, nama, harga, stok);
        barangDAO.insert(b);
        System.out.println("? Barang berhasil ditambahkan!");
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    private static void editBarang() {
        System.out.print("\nID Barang: ");
        int id = scanner.nextInt(); scanner.nextLine();
        Barang b = barangDAO.findById(id);
        if (b == null) {
            System.out.println("? Barang tidak ditemukan!");
        } else {
            System.out.print("Nama baru (" + b.getNamaBarang() + "): ");
            String nama = scanner.nextLine();
            if (!nama.isEmpty()) b.setNamaBarang(nama);
            System.out.print("Harga baru (0 jika tidak): Rp ");
            double harga = scanner.nextDouble();
            if (harga > 0) b.setHarga(harga);
            System.out.print("Stok baru (-1 jika tidak): ");
            int stok = scanner.nextInt();
            if (stok >= 0) b.setStok(stok);
            scanner.nextLine();
            barangDAO.update(b);
            System.out.println("? Barang berhasil diupdate!");
        }
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    private static void hapusBarang() {
        System.out.print("\nID Barang: ");
        int id = scanner.nextInt(); scanner.nextLine();
        System.out.print("Yakin? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            barangDAO.delete(id);
            System.out.println("? Barang berhasil dihapus!");
        }
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    private static void cariBarang() {
        System.out.print("\nID Barang: ");
        int id = scanner.nextInt(); scanner.nextLine();
        Barang b = barangDAO.findById(id);
        if (b != null) {
            System.out.printf("\n? %s | Harga: Rp%.0f | Stok: %d\n", b.getNamaBarang(), b.getHarga(), b.getStok());
        } else {
            System.out.println("? Barang tidak ditemukan!");
        }
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    // ==================== MENU SUPPLIER ====================
    private static void menuSupplier() {
        int pilihan;
        do {
            System.out.println("\n????????????????????????????????????????????????????????????????????");
            System.out.println("?                      MANAJEMEN SUPPLIER                          ?");
            System.out.println("????????????????????????????????????????????????????????????????????");
            System.out.println("?  1. Lihat Supplier                                                ?");
            System.out.println("?  2. Tambah Supplier                                               ?");
            System.out.println("?  3. Edit Supplier                                                 ?");
            System.out.println("?  4. Hapus Supplier                                                ?");
            System.out.println("?  0. Kembali                                                       ?");
            System.out.println("????????????????????????????????????????????????????????????????????");
            System.out.print("Pilih: ");
            pilihan = scanner.nextInt();
            scanner.nextLine();
            
            switch (pilihan) {
                case 1: lihatSupplier(); break;
                case 2: tambahSupplier(); break;
                case 3: editSupplier(); break;
                case 4: hapusSupplier(); break;
            }
        } while (pilihan != 0);
    }
    
    private static void lihatSupplier() {
        List<Supplier> list = supplierDAO.getAll();
        if (list.isEmpty()) {
            System.out.println("\n?? Belum ada data supplier.");
        } else {
            System.out.println("\n?? DAFTAR SUPPLIER");
            System.out.println("================================================================================");
            System.out.printf("%-5s %-25s %-15s %-30s\n", "ID", "Nama Supplier", "Telepon", "Alamat");
            System.out.println("================================================================================");
            for (Supplier s : list) {
                System.out.printf("%-5d %-25s %-15s %-30s\n", s.getId(), s.getNamaSupplier(), s.getTelepon(), s.getAlamat());
            }
            System.out.println("================================================================================");
        }
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    private static void tambahSupplier() {
        System.out.println("\n? TAMBAH SUPPLIER");
        System.out.print("ID Supplier: ");
        int id = scanner.nextInt(); scanner.nextLine();
        System.out.print("Nama Supplier: ");
        String nama = scanner.nextLine();
        System.out.print("Alamat: ");
        String alamat = scanner.nextLine();
        System.out.print("Telepon: ");
        String telepon = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        Supplier s = new Supplier(id, nama, alamat, telepon, email);
        supplierDAO.insert(s);
        System.out.println("? Supplier berhasil ditambahkan!");
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    private static void editSupplier() {
        System.out.print("\nID Supplier: ");
        int id = scanner.nextInt(); scanner.nextLine();
        Supplier s = supplierDAO.findById(id);
        if (s == null) {
            System.out.println("? Supplier tidak ditemukan!");
        } else {
            System.out.print("Nama baru (" + s.getNamaSupplier() + "): ");
            String nama = scanner.nextLine();
            if (!nama.isEmpty()) s.setNamaSupplier(nama);
            System.out.print("Alamat baru (" + s.getAlamat() + "): ");
            String alamat = scanner.nextLine();
            if (!alamat.isEmpty()) s.setAlamat(alamat);
            System.out.print("Telepon baru (" + s.getTelepon() + "): ");
            String telepon = scanner.nextLine();
            if (!telepon.isEmpty()) s.setTelepon(telepon);
            System.out.print("Email baru (" + s.getEmail() + "): ");
            String email = scanner.nextLine();
            if (!email.isEmpty()) s.setEmail(email);
            supplierDAO.update(s);
            System.out.println("? Supplier berhasil diupdate!");
        }
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    private static void hapusSupplier() {
        System.out.print("\nID Supplier: ");
        int id = scanner.nextInt(); scanner.nextLine();
        System.out.print("Yakin? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            supplierDAO.delete(id);
            System.out.println("? Supplier berhasil dihapus!");
        }
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    // ==================== MENU PELANGGAN ====================
    private static void menuPelanggan() {
        int pilihan;
        do {
            System.out.println("\n????????????????????????????????????????????????????????????????????");
            System.out.println("?                      MANAJEMEN PELANGGAN                         ?");
            System.out.println("????????????????????????????????????????????????????????????????????");
            System.out.println("?  1. Lihat Pelanggan                                                ?");
            System.out.println("?  2. Tambah Pelanggan                                               ?");
            System.out.println("?  3. Edit Pelanggan                                                 ?");
            System.out.println("?  4. Hapus Pelanggan                                                ?");
            System.out.println("?  0. Kembali                                                       ?");
            System.out.println("????????????????????????????????????????????????????????????????????");
            System.out.print("Pilih: ");
            pilihan = scanner.nextInt();
            scanner.nextLine();
            
            switch (pilihan) {
                case 1: lihatPelanggan(); break;
                case 2: tambahPelanggan(); break;
                case 3: editPelanggan(); break;
                case 4: hapusPelanggan(); break;
            }
        } while (pilihan != 0);
    }
    
    private static void lihatPelanggan() {
        List<Pelanggan> list = pelangganDAO.getAll();
        if (list.isEmpty()) {
            System.out.println("\n?? Belum ada data pelanggan.");
        } else {
            System.out.println("\n?? DAFTAR PELANGGAN");
            System.out.println("================================================================================");
            System.out.printf("%-5s %-20s %-20s %-15s\n", "ID", "Nama", "Alamat", "Telepon");
            System.out.println("================================================================================");
            for (Pelanggan p : list) {
                System.out.printf("%-5d %-20s %-20s %-15s\n", p.getId(), p.getNama(), p.getAlamat(), p.getTelepon());
            }
            System.out.println("================================================================================");
        }
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    private static void tambahPelanggan() {
        System.out.println("\n? TAMBAH PELANGGAN");
        System.out.print("ID Pelanggan: ");
        int id = scanner.nextInt(); scanner.nextLine();
        System.out.print("Nama: ");
        String nama = scanner.nextLine();
        System.out.print("Alamat: ");
        String alamat = scanner.nextLine();
        System.out.print("Telepon: ");
        String telepon = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        Pelanggan p = new Pelanggan(id, nama, alamat, telepon, email, 0);
        pelangganDAO.insert(p);
        System.out.println("? Pelanggan berhasil ditambahkan!");
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    private static void editPelanggan() {
        System.out.print("\nID Pelanggan: ");
        int id = scanner.nextInt(); scanner.nextLine();
        Pelanggan p = pelangganDAO.findById(id);
        if (p == null) {
            System.out.println("? Pelanggan tidak ditemukan!");
        } else {
            System.out.print("Nama baru (" + p.getNama() + "): ");
            String nama = scanner.nextLine();
            if (!nama.isEmpty()) p.setNama(nama);
            System.out.print("Alamat baru (" + p.getAlamat() + "): ");
            String alamat = scanner.nextLine();
            if (!alamat.isEmpty()) p.setAlamat(alamat);
            System.out.print("Telepon baru (" + p.getTelepon() + "): ");
            String telepon = scanner.nextLine();
            if (!telepon.isEmpty()) p.setTelepon(telepon);
            System.out.print("Email baru (" + p.getEmail() + "): ");
            String email = scanner.nextLine();
            if (!email.isEmpty()) p.setEmail(email);
            pelangganDAO.update(p);
            System.out.println("? Pelanggan berhasil diupdate!");
        }
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    private static void hapusPelanggan() {
        System.out.print("\nID Pelanggan: ");
        int id = scanner.nextInt(); scanner.nextLine();
        System.out.print("Yakin? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            pelangganDAO.delete(id);
            System.out.println("? Pelanggan berhasil dihapus!");
        }
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    // ==================== MENU KATEGORI ====================
    private static void menuKategori() {
        int pilihan;
        do {
            System.out.println("\n????????????????????????????????????????????????????????????????????");
            System.out.println("?                        MANAJEMEN KATEGORI                        ?");
            System.out.println("????????????????????????????????????????????????????????????????????");
            System.out.println("?  1. Lihat Kategori                                                ?");
            System.out.println("?  2. Tambah Kategori                                               ?");
            System.out.println("?  3. Edit Kategori                                                 ?");
            System.out.println("?  4. Hapus Kategori                                                ?");
            System.out.println("?  0. Kembali                                                       ?");
            System.out.println("????????????????????????????????????????????????????????????????????");
            System.out.print("Pilih: ");
            pilihan = scanner.nextInt();
            scanner.nextLine();
            
            switch (pilihan) {
                case 1: lihatKategori(); break;
                case 2: tambahKategori(); break;
                case 3: editKategori(); break;
                case 4: hapusKategori(); break;
            }
        } while (pilihan != 0);
    }
    
    private static void lihatKategori() {
        List<Kategori> list = kategoriDAO.getAll();
        if (list.isEmpty()) {
            System.out.println("\n?? Belum ada data kategori.");
        } else {
            System.out.println("\n?? DAFTAR KATEGORI");
            System.out.println("================================================================================");
            System.out.printf("%-5s %-20s %-30s\n", "ID", "Nama Kategori", "Deskripsi");
            System.out.println("================================================================================");
            for (Kategori k : list) {
                System.out.printf("%-5d %-20s %-30s\n", k.getId(), k.getNamaKategori(), k.getDeskripsi());
            }
            System.out.println("================================================================================");
        }
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    private static void tambahKategori() {
        System.out.println("\n? TAMBAH KATEGORI");
        System.out.print("ID Kategori: ");
        int id = scanner.nextInt(); scanner.nextLine();
        System.out.print("Nama Kategori: ");
        String nama = scanner.nextLine();
        System.out.print("Deskripsi: ");
        String deskripsi = scanner.nextLine();
        
        Kategori k = new Kategori(id, nama, deskripsi);
        kategoriDAO.insert(k);
        System.out.println("? Kategori berhasil ditambahkan!");
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    private static void editKategori() {
        System.out.print("\nID Kategori: ");
        int id = scanner.nextInt(); scanner.nextLine();
        Kategori k = kategoriDAO.findById(id);
        if (k == null) {
            System.out.println("? Kategori tidak ditemukan!");
        } else {
            System.out.print("Nama baru (" + k.getNamaKategori() + "): ");
            String nama = scanner.nextLine();
            if (!nama.isEmpty()) k.setNamaKategori(nama);
            System.out.print("Deskripsi baru (" + k.getDeskripsi() + "): ");
            String deskripsi = scanner.nextLine();
            if (!deskripsi.isEmpty()) k.setDeskripsi(deskripsi);
            kategoriDAO.update(k);
            System.out.println("? Kategori berhasil diupdate!");
        }
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    private static void hapusKategori() {
        System.out.print("\nID Kategori: ");
        int id = scanner.nextInt(); scanner.nextLine();
        System.out.print("Yakin? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            kategoriDAO.delete(id);
            System.out.println("? Kategori berhasil dihapus!");
        }
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    // ==================== MENU USER ====================
    private static void menuUser() {
        int pilihan;
        do {
            System.out.println("\n????????????????????????????????????????????????????????????????????");
            System.out.println("?                         MANAJEMEN USER                           ?");
            System.out.println("????????????????????????????????????????????????????????????????????");
            System.out.println("?  1. Lihat User                                                    ?");
            System.out.println("?  2. Tambah User                                                   ?");
            System.out.println("?  3. Edit User                                                     ?");
            System.out.println("?  4. Hapus User                                                    ?");
            System.out.println("?  0. Kembali                                                       ?");
            System.out.println("????????????????????????????????????????????????????????????????????");
            System.out.print("Pilih: ");
            pilihan = scanner.nextInt();
            scanner.nextLine();
            
            switch (pilihan) {
                case 1: lihatUser(); break;
                case 2: tambahUser(); break;
                case 3: editUser(); break;
                case 4: hapusUser(); break;
            }
        } while (pilihan != 0);
    }
    
    private static void lihatUser() {
        List<User> list = userDAO.getAll();
        if (list.isEmpty()) {
            System.out.println("\n?? Belum ada data user.");
        } else {
            System.out.println("\n?? DAFTAR USER");
            System.out.println("================================================================================");
            System.out.printf("%-5s %-15s %-25s %-10s\n", "ID", "Username", "Email", "Role");
            System.out.println("================================================================================");
            for (User u : list) {
                System.out.printf("%-5d %-15s %-25s %-10s\n", u.getId(), u.getUsername(), u.getEmail(), u.getRole());
            }
            System.out.println("================================================================================");
        }
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    private static void tambahUser() {
        System.out.println("\n? TAMBAH USER");
        System.out.print("ID User: ");
        int id = scanner.nextInt(); scanner.nextLine();
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Role (admin/kasir/manager): ");
        String role = scanner.nextLine();
        
        User u = new User(id, username, password, email, role);
        userDAO.insert(u);
        System.out.println("? User berhasil ditambahkan!");
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    private static void editUser() {
        System.out.print("\nID User: ");
        int id = scanner.nextInt(); scanner.nextLine();
        User u = userDAO.findById(id);
        if (u == null) {
            System.out.println("? User tidak ditemukan!");
        } else {
            System.out.print("Username baru (" + u.getUsername() + "): ");
            String username = scanner.nextLine();
            if (!username.isEmpty()) u.setUsername(username);
            System.out.print("Password baru (kosong jika tidak): ");
            String password = scanner.nextLine();
            if (!password.isEmpty()) u.setPassword(password);
            System.out.print("Email baru (" + u.getEmail() + "): ");
            String email = scanner.nextLine();
            if (!email.isEmpty()) u.setEmail(email);
            System.out.print("Role baru (" + u.getRole() + "): ");
            String role = scanner.nextLine();
            if (!role.isEmpty()) u.setRole(role);
            userDAO.update(u);
            System.out.println("? User berhasil diupdate!");
        }
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    private static void hapusUser() {
        System.out.print("\nID User: ");
        int id = scanner.nextInt(); scanner.nextLine();
        System.out.print("Yakin? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            userDAO.delete(id);
            System.out.println("? User berhasil dihapus!");
        }
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    // ==================== MENU TRANSAKSI ====================
    private static List<DetailTransaksi> tempDetailList = new ArrayList<>();
    private static int currentTransaksiId = 0;
    
    private static void menuTransaksi() {
        int pilihan;
        do {
            System.out.println("\n????????????????????????????????????????????????????????????????????");
            System.out.println("?                        TRANSAKSI PENJUALAN                        ?");
            System.out.println("????????????????????????????????????????????????????????????????????");
            System.out.println("?  1. Transaksi Baru                                                 ?");
            System.out.println("?  2. Lihat Riwayat Transaksi                                        ?");
            System.out.println("?  3. Lihat Detail Transaksi                                         ?");
            System.out.println("?  4. Update Status Transaksi                                        ?");
            System.out.println("?  5. Hapus Transaksi                                                ?");
            System.out.println("?  0. Kembali                                                       ?");
            System.out.println("????????????????????????????????????????????????????????????????????");
            System.out.print("Pilih: ");
            pilihan = scanner.nextInt();
            scanner.nextLine();
            
            switch (pilihan) {
                case 1: transaksiBaru(); break;
                case 2: lihatRiwayatTransaksi(); break;
                case 3: lihatDetailTransaksi(); break;
                case 4: updateStatusTransaksi(); break;
                case 5: hapusTransaksi(); break;
            }
        } while (pilihan != 0);
    }
    
    private static void transaksiBaru() {
        System.out.println("\n?? TRANSAKSI BARU");
        
        // Pilih pelanggan
        System.out.print("ID Pelanggan (0 untuk tamu): ");
        int idPelanggan = scanner.nextInt(); scanner.nextLine();
        Pelanggan pelanggan = (idPelanggan > 0) ? pelangganDAO.findById(idPelanggan) : null;
        
        // Pilih user/kasir
        System.out.print("ID User (kasir): ");
        int idUser = scanner.nextInt(); scanner.nextLine();
        User user = userDAO.findById(idUser);
        if (user == null) {
            System.out.println("? User tidak ditemukan!");
            return;
        }
        
        // Buat transaksi
        Transaksi transaksi = new Transaksi();
        transaksi.setPelanggan(pelanggan);
        transaksi.setUser(user);
        transaksi.setTglTransaksi(LocalDate.now());
        transaksi.setStatus("proses");
        transaksi.setTotalHarga(0);
        transaksiDAO.insert(transaksi);
        
        currentTransaksiId = transaksi.getId();
        tempDetailList.clear();
        
        double totalHarga = 0;
        boolean tambahLagi = true;
        
        while (tambahLagi) {
            System.out.println("\n?? TAMBAH ITEM");
            lihatBarang();
            System.out.print("ID Barang: ");
            int idBarang = scanner.nextInt();
            System.out.print("Jumlah: ");
            int jumlah = scanner.nextInt(); scanner.nextLine();
            
            Barang barang = barangDAO.findById(idBarang);
            if (barang != null && barang.getStok() >= jumlah) {
                double subtotal = barang.getHarga() * jumlah;
                
                DetailTransaksi detail = new DetailTransaksi();
                detail.setTransaksi(transaksi);
                detail.setBarang(barang);
                detail.setJumlah(jumlah);
                detail.setHargaSatuan(barang.getHarga());
                detail.setSubtotal(subtotal);
                detailTransaksiDAO.insert(detail);
                tempDetailList.add(detail);
                
                // Update stok barang
                barang.setStok(barang.getStok() - jumlah);
                barangDAO.update(barang);
                
                totalHarga += subtotal;
                System.out.println("? " + barang.getNamaBarang() + " x" + jumlah + " = Rp" + subtotal);
            } else {
                System.out.println("? Barang tidak tersedia atau stok kurang!");
            }
            
            System.out.print("Tambah lagi? (y/n): ");
            tambahLagi = scanner.nextLine().equalsIgnoreCase("y");
        }
        
        // Update total harga transaksi
        transaksi.setTotalHarga(totalHarga);
        transaksiDAO.update(transaksi);
        System.out.println("\n? Total Transaksi: Rp" + totalHarga);
        System.out.println("? Transaksi berhasil disimpan! ID Transaksi: " + transaksi.getId());
        
        System.out.print("\nLanjut ke pembayaran? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            prosesPembayaran(transaksi);
        }
        
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    private static void lihatRiwayatTransaksi() {
        List<Transaksi> list = transaksiDAO.getAll();
        if (list.isEmpty()) {
            System.out.println("\n?? Belum ada data transaksi.");
        } else {
            System.out.println("\n?? RIWAYAT TRANSAKSI");
            System.out.println("====================================================================================================");
            System.out.printf("%-5s %-12s %-20s %-15s %-10s\n", "ID", "Tanggal", "Pelanggan", "Total", "Status");
            System.out.println("====================================================================================================");
            for (Transaksi t : list) {
                String namaPelanggan = t.getPelanggan() != null ? t.getPelanggan().getNama() : "Tamu";
                System.out.printf("%-5d %-12s %-20s Rp%-13.0f %-10s\n", t.getId(), t.getTglTransaksi(), namaPelanggan, t.getTotalHarga(), t.getStatus());
            }
            System.out.println("====================================================================================================");
        }
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    private static void lihatDetailTransaksi() {
        System.out.print("\nID Transaksi: ");
        int id = scanner.nextInt(); scanner.nextLine();
        List<DetailTransaksi> list = detailTransaksiDAO.getByTransaksiId(id);
        if (list.isEmpty()) {
            System.out.println("? Tidak ada detail untuk transaksi ID " + id);
        } else {
            System.out.println("\n?? DETAIL TRANSAKSI ID: " + id);
            System.out.println("================================================================================");
            System.out.printf("%-5s %-5s %-20s %-10s %-15s %-15s\n", "ID", "ID Barang", "Nama Barang", "Jumlah", "Harga Satuan", "Subtotal");
            System.out.println("================================================================================");
            for (DetailTransaksi d : list) {
                System.out.printf("%-5d %-5d %-20s %-10d Rp%-13.0f Rp%-13.0f\n", 
                    d.getId(), d.getBarang().getId(), d.getBarang().getNamaBarang(), 
                    d.getJumlah(), d.getHargaSatuan(), d.getSubtotal());
            }
            System.out.println("================================================================================");
        }
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    private static void updateStatusTransaksi() {
        System.out.print("\nID Transaksi: ");
        int id = scanner.nextInt(); scanner.nextLine();
        System.out.print("Status baru (proses/selesai/batal): ");
        String status = scanner.nextLine();
        transaksiDAO.updateStatus(id, status);
        System.out.println("? Status transaksi berhasil diupdate!");
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    private static void hapusTransaksi() {
        System.out.print("\nID Transaksi: ");
        int id = scanner.nextInt(); scanner.nextLine();
        System.out.print("Yakin? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            transaksiDAO.delete(id);
            System.out.println("? Transaksi berhasil dihapus!");
        }
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    // ==================== MENU PEMBAYARAN ====================
    private static void menuPembayaran() {
        int pilihan;
        do {
            System.out.println("\n????????????????????????????????????????????????????????????????????");
            System.out.println("?                           PEMBAYARAN                              ?");
            System.out.println("????????????????????????????????????????????????????????????????????");
            System.out.println("?  1. Lihat Pembayaran                                               ?");
            System.out.println("?  2. Proses Pembayaran                                              ?");
            System.out.println("?  3. Hapus Pembayaran                                               ?");
            System.out.println("?  0. Kembali                                                       ?");
            System.out.println("????????????????????????????????????????????????????????????????????");
            System.out.print("Pilih: ");
            pilihan = scanner.nextInt();
            scanner.nextLine();
            
            switch (pilihan) {
                case 1: lihatPembayaran(); break;
                case 2: prosesPembayaran(null); break;
                case 3: hapusPembayaran(); break;
            }
        } while (pilihan != 0);
    }
    
    private static void lihatPembayaran() {
        List<Pembayaran> list = pembayaranDAO.getAll();
        if (list.isEmpty()) {
            System.out.println("\n?? Belum ada data pembayaran.");
        } else {
            System.out.println("\n?? DAFTAR PEMBAYARAN");
            System.out.println("========================================================================================");
            System.out.printf("%-5s %-10s %-10s %-15s %-15s\n", "ID", "ID Transaksi", "Metode", "Jumlah Dibayar", "Tanggal");
            System.out.println("========================================================================================");
            for (Pembayaran p : list) {
                System.out.printf("%-5d %-10d %-10s Rp%-13.0f %-12s\n", 
                    p.getId(), p.getTransaksi().getId(), p.getMetode(), p.getJumlahDibayar(), p.getTglPembayaran());
            }
            System.out.println("========================================================================================");
        }
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    private static void prosesPembayaran(Transaksi transaksi) {
        if (transaksi == null) {
            System.out.print("\nID Transaksi: ");
            int id = scanner.nextInt(); scanner.nextLine();
            transaksi = transaksiDAO.findById(id);
        }
        
        if (transaksi == null) {
            System.out.println("? Transaksi tidak ditemukan!");
        } else {
            System.out.println("Total Tagihan: Rp" + transaksi.getTotalHarga());
            System.out.print("Metode Pembayaran (Tunai/Transfer/QRIS/Kartu Kredit): ");
            String metode = scanner.nextLine();
            System.out.print("Jumlah Dibayar: Rp ");
            double dibayar = scanner.nextDouble(); scanner.nextLine();
            
            double kembalian = dibayar - transaksi.getTotalHarga();
            if (kembalian < 0) kembalian = 0;
            
            Pembayaran pembayaran = new Pembayaran();
            pembayaran.setTransaksi(transaksi);
            pembayaran.setMetode(metode);
            pembayaran.setJumlahDibayar(dibayar);
            pembayaran.setKembalian(kembalian);
            pembayaran.setTglPembayaran(LocalDate.now());
            pembayaranDAO.insert(pembayaran);
            
            if (dibayar >= transaksi.getTotalHarga()) {
                transaksiDAO.updateStatus(transaksi.getId(), "selesai");
                System.out.println("? Pembayaran LUNAS! Kembalian: Rp" + kembalian);
            } else {
                System.out.println("?? Pembayaran belum lunas. Sisa: Rp" + (transaksi.getTotalHarga() - dibayar));
            }
        }
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    private static void hapusPembayaran() {
        System.out.print("\nID Pembayaran: ");
        int id = scanner.nextInt(); scanner.nextLine();
        System.out.print("Yakin? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            pembayaranDAO.delete(id);
            System.out.println("? Pembayaran berhasil dihapus!");
        }
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    // ==================== MENU PENGIRIMAN ====================
    private static void menuPengiriman() {
        int pilihan;
        do {
            System.out.println("\n????????????????????????????????????????????????????????????????????");
            System.out.println("?                            PENGIRIMAN                             ?");
            System.out.println("????????????????????????????????????????????????????????????????????");
            System.out.println("?  1. Lihat Pengiriman                                               ?");
            System.out.println("?  2. Buat Pengiriman                                                ?");
            System.out.println("?  3. Konfirmasi Terima                                              ?");
            System.out.println("?  4. Hapus Pengiriman                                               ?");
            System.out.println("?  0. Kembali                                                       ?");
            System.out.println("????????????????????????????????????????????????????????????????????");
            System.out.print("Pilih: ");
            pilihan = scanner.nextInt();
            scanner.nextLine();
            
            switch (pilihan) {
                case 1: lihatPengiriman(); break;
                case 2: buatPengiriman(); break;
                case 3: konfirmasiTerima(); break;
                case 4: hapusPengiriman(); break;
            }
        } while (pilihan != 0);
    }
    
    private static void lihatPengiriman() {
        List<Pengiriman> list = pengirimanDAO.getAll();
        if (list.isEmpty()) {
            System.out.println("\n?? Belum ada data pengiriman.");
        } else {
            System.out.println("\n?? DAFTAR PENGIRIMAN");
            System.out.println("================================================================================================");
            System.out.printf("%-5s %-10s %-10s %-15s %-12s %-12s %-10s\n", "ID", "ID Transaksi", "Kurir", "No Resi", "Tgl Kirim", "Tgl Terima", "Status");
            System.out.println("================================================================================================");
            for (Pengiriman p : list) {
                String tglTerima = p.getTglTerima() != null ? p.getTglTerima().toString() : "-";
                System.out.printf("%-5d %-10d %-10s %-15s %-12s %-12s %-10s\n", 
                    p.getId(), p.getTransaksi().getId(), p.getKurir(), p.getNoResi(), 
                    p.getTglKirim(), tglTerima, p.getStatus());
            }
            System.out.println("================================================================================================");
        }
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    private static void buatPengiriman() {
        System.out.print("\nID Transaksi: ");
        int id = scanner.nextInt(); scanner.nextLine();
        Transaksi transaksi = transaksiDAO.findById(id);
        if (transaksi == null) {
            System.out.println("? Transaksi tidak ditemukan!");
        } else {
            System.out.print("Kurir (JNE/J&T/SiCepat/Pos Indonesia): ");
            String kurir = scanner.nextLine();
            System.out.print("No Resi: ");
            String noResi = scanner.nextLine();
            
            Pengiriman pengiriman = new Pengiriman();
            pengiriman.setTransaksi(transaksi);
            pengiriman.setKurir(kurir);
            pengiriman.setNoResi(noResi);
            pengiriman.setTglKirim(LocalDate.now());
            pengiriman.setStatus("DIKIRIM");
            pengirimanDAO.insert(pengiriman);
            
            transaksiDAO.updateStatus(id, "dikirim");
            System.out.println("? Pengiriman berhasil dibuat!");
        }
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    private static void konfirmasiTerima() {
        System.out.print("\nID Pengiriman: ");
        int id = scanner.nextInt(); scanner.nextLine();
        Pengiriman pengiriman = pengirimanDAO.findById(id);
        if (pengiriman == null) {
            System.out.println("? Pengiriman tidak ditemukan!");
        } else {
            pengiriman.setTglTerima(LocalDate.now());
            pengiriman.setStatus("DITERIMA");
            pengirimanDAO.update(pengiriman);
            transaksiDAO.updateStatus(pengiriman.getTransaksi().getId(), "selesai");
            System.out.println("? Barang telah diterima!");
        }
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    private static void hapusPengiriman() {
        System.out.print("\nID Pengiriman: ");
        int id = scanner.nextInt(); scanner.nextLine();
        System.out.print("Yakin? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            pengirimanDAO.delete(id);
            System.out.println("? Pengiriman berhasil dihapus!");
        }
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    // ==================== MENU LAPORAN ====================
    private static void menuLaporan() {
        int pilihan;
        do {
            System.out.println("\n????????????????????????????????????????????????????????????????????");
            System.out.println("?                             LAPORAN                               ?");
            System.out.println("????????????????????????????????????????????????????????????????????");
            System.out.println("?  1. Laporan Stok Barang                                            ?");
            System.out.println("?  2. Laporan Transaksi                                              ?");
            System.out.println("?  3. Laporan Omzet                                                  ?");
            System.out.println("?  0. Kembali                                                       ?");
            System.out.println("????????????????????????????????????????????????????????????????????");
            System.out.print("Pilih: ");
            pilihan = scanner.nextInt();
            scanner.nextLine();
            
            switch (pilihan) {
                case 1: laporanStok(); break;
                case 2: laporanTransaksi(); break;
                case 3: laporanOmzet(); break;
            }
        } while (pilihan != 0);
    }
    
    private static void laporanStok() {
        List<Barang> list = barangDAO.getAll();
        System.out.println("\n?? LAPORAN STOK BARANG");
        System.out.println("================================================================================");
        System.out.printf("%-5s %-25s %-15s %-10s\n", "ID", "Nama Barang", "Harga", "Stok");
        System.out.println("================================================================================");
        for (Barang b : list) {
            System.out.printf("%-5d %-25s Rp%-13.0f %-10d\n", b.getId(), b.getNamaBarang(), b.getHarga(), b.getStok());
        }
        System.out.println("================================================================================");
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    private static void laporanTransaksi() {
        List<Transaksi> list = transaksiDAO.getAll();
        if (list.isEmpty()) {
            System.out.println("\n?? Belum ada data transaksi.");
        } else {
            System.out.println("\n?? LAPORAN TRANSAKSI");
            System.out.println("====================================================================================================");
            System.out.printf("%-5s %-12s %-20s %-15s %-10s\n", "ID", "Tanggal", "Pelanggan", "Total", "Status");
            System.out.println("====================================================================================================");
            for (Transaksi t : list) {
                String namaPelanggan = t.getPelanggan() != null ? t.getPelanggan().getNama() : "Tamu";
                System.out.printf("%-5d %-12s %-20s Rp%-13.0f %-10s\n", t.getId(), t.getTglTransaksi(), namaPelanggan, t.getTotalHarga(), t.getStatus());
            }
            System.out.println("====================================================================================================");
        }
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
    
    private static void laporanOmzet() {
        List<Transaksi> list = transaksiDAO.getAll();
        double total = 0;
        for (Transaksi t : list) {
            if ("selesai".equals(t.getStatus())) {
                total += t.getTotalHarga();
            }
        }
        System.out.println("\n?? LAPORAN OMZET");
        System.out.println("================================================================");
        System.out.println("Total Omzet: Rp" + total);
        System.out.println("================================================================");
        System.out.print("\nTekan Enter...");
        scanner.nextLine();
    }
}
