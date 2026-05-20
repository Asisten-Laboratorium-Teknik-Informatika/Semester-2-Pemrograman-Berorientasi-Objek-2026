package src.main.java.com.perpus;

import src.main.java.com.perpus.service.PerpustakaanService;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        PerpustakaanService perpus = new PerpustakaanService();
        Scanner input = new Scanner(System.in);
        int pilihan = 0;
        
        do {
            System.out.println("\n==============================================");
            System.out.println("         SISTEM INFORMASI PERPUSTAKAAN   ");
            System.out.println("==============================================");
            System.out.println("1. Lihat Daftar Buku");
            System.out.println("2. Lihat Daftar Anggota");
            System.out.println("3. Transaksi Peminjaman Baru");
            System.out.println("4. Transaksi Pengembalian Buku");
            System.out.println("5. Keluar Aplikasi");
            System.out.print("Pilih menu (1-5): ");
            
            try {
                pilihan = input.nextInt();
                input.nextLine();
                
                switch (pilihan) {
                    case 1:
                        perpus.tampilkanKatalog();
                        break;
                    case 2:
                        perpus.tampilkanDaftarAnggota();
                    case 3:
                        System.out.println("\n--- FORM TRANSAKSI PEMINJAMAN ---");
                        System.out.print("Masukkan ID Anggota : "); int idAng = input.nextInt();
                        System.out.print("Masukkan ID Buku    : "); int idBuk = input.nextInt();
                        System.out.print("Masukkan ID Petugas : "); int idPet = input.nextInt();

                        perpus.prosesPeminjaman(idAng, idBuk, idPet);
                        break;
                    case 4:
                        System.out.println("\n--- FORM TRANSAKSI PENGEMBALIAN ---");
                        System.out.print("Masukkan Kode Peminjaman (cth: PJM-2026-001): ");
                        String kodeKembali = input.nextLine();
                        
                        perpus.prosesPengembalian(kodeKembali);
                        break;
                    case 5:
                        System.out.println("Aplikasi ditutup.");
                        break;
                    default:
                        System.out.println(" Opsi tidak valid.");
                }
            } catch (Exception e) {
                System.out.println(" Terjadi kesalahan pada input data.");
                input.nextLine();
            }
        } while (pilihan != 5);
        
        input.close();
    }
}
