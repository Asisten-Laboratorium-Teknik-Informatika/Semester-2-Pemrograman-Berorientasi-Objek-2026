package com.penjualan;

import com.penjualan.ui.MenuUtama;
import com.penjualan.utils.Koneksi;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                     SISTEM PENJUALAN                         ║");
        System.out.println("║                         &                                    ║");
        System.out.println("║                     DISTRIBUSI                               ║");
        System.out.println("║                  Java OOP + PostgreSQL                       ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        
        // Test koneksi database
        Connection conn = Koneksi.connect();
        
        if (conn != null) {
            System.out.println("\n✅ Database siap digunakan!\n");
            MenuUtama.mainMenu();
        } else {
            System.out.println("\n❌ Gagal koneksi database. Program berhenti.\n");
        }
        
        // Tutup koneksi
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("✅ Koneksi database ditutup.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}