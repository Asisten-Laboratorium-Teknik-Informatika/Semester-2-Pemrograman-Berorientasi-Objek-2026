package model;

import dao.ProdukDAO;

public class Supplier extends Akun {

    public Supplier(int idAkun, String username, String password, String nama, String email) {
        super(idAkun, username, password, nama, email, "SUPPLIER");
    }

    @Override
    public void tampilkanMenu() {
        System.out.println("\n--- MENU SUPPLIER ---");
        System.out.println("1. Lihat Permintaan Stok");
        System.out.println("2. Kirim Pasokan Produk (Restock)");
        System.out.println("3. Logout");
    }

    public void pasokBarang(ProdukDAO produkDAO, int idProduk, int jumlahPasokan, int stokSekarang) {
        int stokBaru = stokSekarang + jumlahPasokan;
        boolean sukses = produkDAO.updateStok(idProduk, stokBaru);
        if (sukses) {
            System.out.println("Stok produk ID " + idProduk + " berhasil ditambah sebanyak " + jumlahPasokan);
        }
    }
}