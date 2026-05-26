import dao.*;
import model.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    
    private static ProdukDAO produkDAO = new ProdukDAO();
    private static PenjualanDAO penjualanDAO = new PenjualanDAO();
    private static PengirimanDAO pengirimanDAO = new PengirimanDAO();
    private static PasokanDAO pasokanDAO = new PasokanDAO();
    private static PelangganDAO pelangganDAO = new PelangganDAO();
    private static KaryawanDAO karyawanDAO = new KaryawanDAO();
    private static KurirDAO kurirDAO = new KurirDAO();
    private static SupplierDAO supplierDAO = new SupplierDAO();

    public static void main(String[] args) {
        int pilihanUtama = 0;

        do {
            System.out.println("\n==============================================");
            System.out.println("   SISTEM ERP TOKO FASHION (FULL MANAGEMENT)   ");
            System.out.println("==============================================");
            System.out.println("1. MANAJEMEN DATA MASTER");
            System.out.println("2. OPERASIONAL PENJUALAN");
            System.out.println("3. DISTRIBUSI LOGISTIK");
            System.out.println("4. RANTAI PASOK / STOK");
            System.out.println("5. LAPORAN PENJUALAN");
            System.out.println("6. Keluar Aplikasi");
            System.out.print("Pilih Kategori Menu (1-6): ");

            if (scanner.hasNextInt()) {
                pilihanUtama = scanner.nextInt(); scanner.nextLine(); 
            } else {
                System.out.println("Pilih antara 1-6.");
                scanner.nextLine(); continue;
            }

            try {
                switch (pilihanUtama) {
                    case 1: menuDataMaster(); break;
                    case 2: menuPenjualan(); break;
                    case 3: menuDistribusi(); break;
                    case 4: menuStok(); break;
                    case 5: menuLaporan(); break;
                    case 6: System.out.println("Aplikasi ditutup. Terima Kasih!"); break;
                    default: System.out.println("Menu tidak valid!");
                }
            } catch (SQLException e) {
                System.err.println("\n[ERROR DATABASE]: " + e.getMessage());
            }

        } while (pilihanUtama != 6);
    }

    private static void menuDataMaster() throws SQLException {
        System.out.println("\n--- KATEGORI MANAJEMEN DATA MASTER ---");
        System.out.println("1. Master Data Pelanggan (Member)");
        System.out.println("2. Master Data Karyawan (Staff)");
        System.out.println("3. Master Data Kurir (Ekspedisi)");
        System.out.println("4. Master Data Supplier (Mitra Pasok)");
        System.out.print("Pilih Kategori (1-4): ");
        int ops = scanner.nextInt(); scanner.nextLine();

        System.out.println("\n--- PILIHAN OPERASI CRUD ---");
        System.out.println("[1] Tambah Data Baru");
        System.out.println("[2] Tampilkan Semua");
        System.out.println("[3] Ubah Data");
        System.out.println("[4] Hapus Data");
        System.out.print("Pilih Aksi (1-4): ");
        int aksi = scanner.nextInt(); scanner.nextLine();

        switch (ops) {
            case 1:
                if (aksi == 1) {
                    System.out.print("ID Pelanggan          : "); String id = scanner.nextLine();
                    System.out.print("Nama Pelanggan        : "); String nama = scanner.nextLine();
                    System.out.print("Email                 : "); String email = scanner.nextLine();
                    System.out.print("Nomor Telepon         : "); String telp = scanner.nextLine();
                    pelangganDAO.insert(new Pelanggan(id, nama, email, telp));
                    System.out.println(">> Pelanggan Berhasil Didaftarkan!");
                } else if (aksi == 2) {
                    tampilkanPelanggan();
                } else if (aksi == 3) {
                    System.out.print("Masukkan ID Pelanggan yang akan diubah: "); String idEdit = scanner.nextLine();
                    System.out.print("Nama Baru  : "); String namaBaru = scanner.nextLine();
                    System.out.print("Email Baru : "); String emailBaru = scanner.nextLine();
                    System.out.print("No.HP Baru : "); String telpBaru = scanner.nextLine();
                    pelangganDAO.update(idEdit, namaBaru, emailBaru, telpBaru);
                } else if (aksi == 4) {
                    System.out.print("Masukkan ID Pelanggan yang akan dihapus: "); String idHapus = scanner.nextLine();
                    pelangganDAO.delete(idHapus);
                }
                break;

            case 2:
                if (aksi == 1) {
                    System.out.print("ID Karyawan          : "); String id = scanner.nextLine();
                    System.out.print("Nama Karyawan        : "); String nama = scanner.nextLine();
                    System.out.print("Jabatan              : "); String jab = scanner.nextLine();
                    karyawanDAO.insert(id, nama, jab);
                    System.out.println(">> Staf Karyawan Berhasil Ditambahkan!");
                } else if (aksi == 2) {
                    karyawanDAO.displayAll();
                } else if (aksi == 3) {
                    System.out.print("Masukkan ID Karyawan yang akan diubah: "); String idEdit = scanner.nextLine();
                    System.out.print("Nama Staf Baru : "); String namaBaru = scanner.nextLine();
                    System.out.print("Jabatan Baru   : "); String jabBaru = scanner.nextLine();
                    karyawanDAO.update(idEdit, namaBaru, jabBaru);
                } else if (aksi == 4) {
                    System.out.print("Masukkan ID Karyawan yang akan dihapus: "); String idHapus = scanner.nextLine();
                    karyawanDAO.delete(idHapus);
                }
                break;

            case 3: 
                if (aksi == 1) {
                    System.out.print("ID Kurir          : "); String id = scanner.nextLine();
                    System.out.print("Nama Perusahaan   : "); String nama = scanner.nextLine();
                    System.out.print("Jenis Kendaraan   : "); String kend = scanner.nextLine();
                    kurirDAO.insert(id, nama, kend);
                    System.out.println(">> Agen Kurir Logistik Berhasil Disimpan!");
                } else if (aksi == 2) {
                    kurirDAO.displayAll();
                } else if (aksi == 3) {
                    System.out.print("Masukkan ID Kurir yang akan diubah: "); String idEdit = scanner.nextLine();
                    System.out.print("Nama Vendor Baru    : "); String namaBaru = scanner.nextLine();
                    System.out.print("Jenis Kendaraan Baru: "); String kendBaru = scanner.nextLine();
                    kurirDAO.update(idEdit, namaBaru, kendBaru);
                } else if (aksi == 4) {
                    System.out.print("Masukkan ID Kurir yang akan dihapus: "); String idHapus = scanner.nextLine();
                    kurirDAO.delete(idHapus);
                }
                break;

            case 4:
                if (aksi == 1) {
                    System.out.print("ID Supplier          : "); String id = scanner.nextLine();
                    System.out.print("Nama Supplier        : "); String nama = scanner.nextLine();
                    System.out.print("Alamat Kantor        : "); String alm = scanner.nextLine();
                    supplierDAO.insert(id, nama, alm);
                    System.out.println(">> Supplier Pakaian Berhasil Disimpan!");
                } else if (aksi == 2) {
                    supplierDAO.displayAll();
                } else if (aksi == 3) {
                    System.out.print("Masukkan ID Supplier yang akan diubah: "); String idEdit = scanner.nextLine();
                    System.out.print("Nama PT/Supplier Baru: "); String namaBaru = scanner.nextLine();
                    System.out.print("Alamat Kantor Baru   : "); String almBaru = scanner.nextLine();
                    supplierDAO.update(idEdit, namaBaru, almBaru);
                } else if (aksi == 4) {
                    System.out.print("Masukkan ID Supplier yang akan dihapus: "); String idHapus = scanner.nextLine();
                    supplierDAO.delete(idHapus);
                }
                break;

            default:
                System.out.println("Kategori tidak valid!");
        }
    }

    private static void menuPenjualan() throws SQLException {
        System.out.println("\n--- OPERASIONAL PENJUALAN KASIR ---");
        System.out.println("1. Buat Nota Penjualan Baru");
        System.out.println("2. Tampilkan Katalog Produk Fashion");
        System.out.print("Pilih Opsi: ");
        int ops = scanner.nextInt(); scanner.nextLine();

        if (ops == 1) {
            System.out.print("ID Penjualan                 : "); String idPenjualan = scanner.nextLine();
            System.out.print("ID Pelanggan                 : "); String idPelanggan = scanner.nextLine();
            System.out.print("ID Karyawan                  : "); String idKaryawan = scanner.nextLine();
            System.out.print("Total Nominal Belanja (Rp)   : "); double total = scanner.nextDouble(); scanner.nextLine();

            Penjualan p = new Penjualan(idPenjualan, new Timestamp(System.currentTimeMillis()), idPelanggan, idKaryawan, total);
            penjualanDAO.insert(p);
            System.out.println(">> Sukses: Transaksi Penjualan Berhasil Dicatat!");
        } else if (ops == 2) {
            tampilkanProduk();
        }
    }

    private static void menuDistribusi() throws SQLException {
        System.out.println("\n--- DISTRIBUSI & LOGISTIK PENGIRIMAN ---");
        System.out.println("1. Buat Resi Pengiriman Baru");
        System.out.println("2. Update Status Perjalanan Kurir");
        System.out.print("Pilih Opsi: ");
        int ops = scanner.nextInt(); scanner.nextLine();

        if (ops == 1) {
            System.out.print("ID Pengiriman           : "); String idKirim = scanner.nextLine();
            System.out.print("ID Penjualan            : "); String idPenj = scanner.nextLine();
            System.out.print("ID Kurir                : "); String idKurir = scanner.nextLine();
            System.out.print("Status Pengiriman Awal  : "); String status = scanner.nextLine();

            Pengiriman pg = new Pengiriman(idKirim, idPenj, idKurir, new Date(System.currentTimeMillis()), status);
            pengirimanDAO.insert(pg);
            System.out.println(">> Sukses: Resi Pengiriman dan Surat Jalan Berhasil Dibuat!");
        } else if (ops == 2) {
            System.out.print("Masukkan ID Pengiriman: "); String idKirim = scanner.nextLine();
            System.out.print("Status Perubahan (Diproses/Dikirim/Selesai): "); String statusB = scanner.nextLine();
            pengirimanDAO.updateStatus(idKirim, statusB);
            System.out.println(">> Sukses: Logistik Paket Diperbarui!");
        }
    }

    private static void menuStok() throws SQLException {
        System.out.println("\n--- MANAJEMEN STOK & BARANG MASUK ---");
        System.out.println("1. Tambah Katalog Baju/Produk Baru");
        System.out.println("2. Input Pasokan Masuk Dari Supplier (Restock)");
        System.out.print("Pilih Opsi: ");
        int ops = scanner.nextInt(); scanner.nextLine();

        if (ops == 1) {
            System.out.print("ID Produk Baru          : "); String id = scanner.nextLine();
            System.out.print("Nama Pakaian            : "); String nama = scanner.nextLine();
            System.out.print("ID Kategori             : "); String kat = scanner.nextLine();
            System.out.print("Harga Satuan            : "); double harga = scanner.nextDouble();
            System.out.print("Stok Awal Gudang        : "); int stok = scanner.nextInt(); scanner.nextLine();

            produkDAO.insert(new Produk(id, nama, kat, harga, stok));
            System.out.println(">> Sukses: Katalog Baru Siap Dijual!");
        } else if (ops == 2) {
            System.out.print("ID Pasokan                  : "); String idPasok = scanner.nextLine();
            System.out.print("ID Produk                   : "); String idProd = scanner.nextLine();
            System.out.print("ID Supplier                 : "); String idSup = scanner.nextLine();
            System.out.print("Jumlah Kuantitas Masuk      : "); int jml = scanner.nextInt(); scanner.nextLine();

            pasokanDAO.insertPasokan(idPasok, idProd, idSup, jml);
            produkDAO.tambahStok(idProd, jml); 
            System.out.println(">> Sukses: Stok Gudang Otomatis Bertambah!");
        }
    }

    private static void menuLaporan() throws SQLException {
        System.out.println("\n--- LAPORAN MONITORING EXECUTIVE ---");
        System.out.println("1. Akumulasi Total Omzet Penjualan");
        System.out.println("2. Cek Alarm Produk Limit (Stok < 5 Pcs)");
        System.out.print("Pilih Opsi: ");
        int ops = scanner.nextInt(); scanner.nextLine();

        if (ops == 1) {
            System.out.printf("\n====================================\n");
            System.out.printf(" TOTAL OMZET MASUK : Rp%,.2f\n", penjualanDAO.getTotalPendapatan());
            System.out.printf("====================================\n");
        } else if (ops == 2) {
            List<Produk> menipis = produkDAO.getStokMenipis();
            System.out.println("\n--- Peringatan Stok Limit Toko ---");
            if (menipis.isEmpty()) {
                System.out.println("Stok masih tersedia.");
            } else {
                for (Produk p : menipis) {
                    System.out.println("⚠ ID [" + p.getIdProduk() + "] " + p.getNamaProduk() + " Sisa " + p.getStok() + " Pcs!");
                }
            }
        }
    }

    private static void tampilkanProduk() throws SQLException {
        List<Produk> semuaProduk = produkDAO.getAll();
        System.out.printf("\n%-10s | %-25s | %-12s | %-6s\n", "ID", "Nama Produk", "Harga", "Stok");
        System.out.println("---------------------------------------------------------------");
        for (Produk p : semuaProduk) {
            System.out.printf("%-10s | %-25s | Rp%-10.2f | %-6d\n", p.getIdProduk(), p.getNamaProduk(), p.getHarga(), p.getStok());
        }
    }

    private static void tampilkanPelanggan() throws SQLException {
        List<Pelanggan> semuaPelanggan = pelangganDAO.getAll();
        System.out.printf("\n%-12s | %-20s | %-20s | %-15s\n", "ID Pelanggan", "Nama", "Email", "Telepon");
        System.out.println("-----------------------------------------------------------------------------");
        for (Pelanggan pel : semuaPelanggan) {
            System.out.printf("%-12s | %-20s | %-20s | %-15s\n", pel.getIdPelanggan(), pel.getNama(), pel.getEmail(), pel.getTelepon());
        }
    }
}