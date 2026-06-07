package model;

import dao.ProdukDAO;

public class Penjual extends Akun {

    public Penjual(int idAkun, String username, String password, String nama, String email) {
        super(idAkun, username, password, nama, email, "PENJUAL");
    }

    @Override
    public void tampilkanMenu() {
        System.out.println("\n--- MENU PENJUAL ---");
        System.out.println("1. Tambah Produk Baru");
        System.out.println("2. Konfirmasi Transaksi");
        System.out.println("3. Lihat Laporan Penjualan");
        System.out.println("4. Logout");
    }

    public void inputProdukBaru(ProdukDAO produkDAO, String nama, double harga, int stokAwal, int idKategori) {
        Produk produkBaru = new Produk(0, nama, harga, stokAwal, idKategori);
        boolean sukses = produkDAO.tambahProduk(produkBaru);
        if (sukses) {
            System.out.println("Produk baru berhasil dipajang di toko!");
        }
    }
}