package model;

import dao.ProdukDAO;
import dao.PembayaranDAO;

public class Pelanggan extends Akun {
    
    public Pelanggan(int idAkun, String username, String password, String nama, String email) {
        super(idAkun, username, password, nama, email, "PELANGGAN");
    }

    @Override
    public void tampilkanMenu() {
        System.out.println("\n--- MENU PELANGGAN ---");
        System.out.println("1. Lihat Katalog Produk");
        System.out.println("2. Beli Produk");
        System.out.println("3. Bayar Pesanan");
        System.out.println("4. Logout");
    }

    public void beliProduk(ProdukDAO produkDAO, int idProdukPilihan, int jumlahBeli, int stokSekarang) {
        if (stokSekarang >= jumlahBeli) {
            int sisaStok = stokSekarang - jumlahBeli;
            produkDAO.updateStok(idProdukPilihan, sisaStok);
        } else {
            System.out.println("Stok tidak mencukupi!");
        }
    }

    public void bayarPesanan(PembayaranDAO pembayaranDAO, int idPesanan, double totalTagihan, String metode) {
        Pembayaran bayarBaru = new Pembayaran(0, idPesanan, new java.util.Date(), totalTagihan, metode, "SUKSES");
        
        boolean sukses = pembayaranDAO.prosesPembayaran(bayarBaru);
        if (sukses) {
            System.out.println("Pesanan #" + idPesanan + " berhasil dibayar.");
            
            pembayaranDAO.cetakNota(idPesanan, totalTagihan, metode);
        } else {
            System.out.println("Pembayaran gagal diproses.");
        }
    }
}