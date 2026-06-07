
import java.sql.*;
import java.util.Scanner;

import database.koneksi;
import function.testing;
import menu.menu;

public class Main {
    public static void main(String[] args) {
        koneksi.clearScreen();
        Scanner input = new Scanner(System.in);
        Connection conn = koneksi.connect();
        testing.testKoneksi();
        while (true) {
        try {
            System.out.println("\n========== LOGIN ==========");
            System.out.print("Username : ");
            String username = input.nextLine();
            System.out.print("Password : ");
            String password = input.nextLine();
            System.out.println("=============================");
            String query = """
                SELECT *
                FROM gudang.users
                WHERE username = ?
                AND password = ?
            """;
            PreparedStatement ps =conn.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            // Jika data ditemukan
            if (rs.next()) {
                System.out.println("\nLogin berhasil!");
                int role = rs.getInt("id_roles");
                String nama = rs.getString("full_name");
                String gmail = rs.getString("email");
                switch (role) {
                    case 1:
                        System.out.println("Selamat datang, " + nama + "!\n");
                        System.out.println("Hubungi Melalui: " + gmail + "\n");
                        while (true) {
                            System.out.println("====================================================\r\n" + //
                                            "                     MENU ADMIN\r\n" + //
                                            "====================================================\r\n" + //
                                            "1. Data Barang\r\n" + //
                                            "2. Data Supplier\r\n" + //
                                            "3. Data Customer\r\n" + //
                                            "4. Data Warehouse\r\n" + //
                                            "5. Manajemen Stok\r\n" +//
                                            "6. Barang Masuk\r\n" + //
                                            "7. Barang Keluar\r\n" + //
                                            "8. Distribusi Antar Warehouse\r\n" + //
                                            "9. Riwayat Transaksi\r\n" + //
                                            "10. Manajemen User\r\n" + //
                                            "11. Logout\r\n" + //
                                            "0. Exit\r\n" + //
                                            "====================================================\r\n" + //
                                            "");
                            System.out.print("\nPilih menu: ");
                            int pilih = Integer.parseInt(input.nextLine());
                            switch (pilih) {
                            case 1:
                                new menu(conn, input).CRUDproduct("gudang", "product");
                                break;
                            case 2:
                                new menu(conn, input).showCrudMenu("Data Supplier", "gudang", "supplier");
                                break;
                            case 3:
                                new menu(conn, input).showCrudMenu("Data Customer", "gudang", "customer");
                                break;
                            case 4:
                                new menu(conn, input).showCrudMenu("Data Warehouse", "gudang", "warehouse");
                                break;
                            case 5:
                                new menu(conn, input).showStockMenu();
                                break;
                            case 6:
                                new menu(conn, input).barangIn();
                                break;
                            case 7:
                                new menu(conn, input).barangOut();
                                break;
                            case 8:
                                new menu(conn, input).distribusi();
                                break;
                            case 9:
                                new menu(conn, input).riwayat();
                                break;
                            case 10:
                                new menu(conn, input).user("gudang", "users");
                                break;
                            case 11:
                                role = 0;
                                break;
                            default:
                                System.out.println("Pilihan tidak valid.");
                            case 0:
                                koneksi.clearScreen();
                                System.out.println("Terima kasih telah menggunakan aplikasi ini. Sampai jumpa!");
                                koneksi.pause(input);
                                return;
                            }
                            if(role==0) break;
                        }
                    koneksi.clearScreen();
                    break;
                    
                    case 3:
                        System.out.println("Selamat datang, " + nama + "!\n");
                        System.out.println("Hubungi Melalui: " + gmail + "\n");
                        while (true) {
                            System.out.println("====================================================\r\n" + //
                                            "             MENU LOGISTIC STAFF\r\n" + //
                                            "====================================================\r\n" + //
                                            "1. Barang Keluar\r\n" + //
                                            "2. Distribusi Antar Warehouse\r\n" + //
                                            "3. Riwayat Pengiriman\r\n" + //
                                            "4. Data Customer\r\n" + //
                                            "5. Logout\r\n" + //
                                            "0. Exit\r\n" + //
                                            "====================================================\r\n" + //
                                            "");
                            
                            System.out.print("Pilih menu: ");
                            int pilih = Integer.parseInt(input.nextLine());
                            switch (pilih) {
                            case 1:
                                new menu(conn, input).barangOut();
                                break;
                            case 2:
                                new menu(conn, input).distribusi();
                                break;
                            case 3:
                                new menu(conn, input).riwayat();
                                break;
                            case 4:
                                new menu(conn, input).showCrudMenu("Data Customer", "gudang", "customer");
                                break;
                            case 5:
                                role = 0;
                                break;
                            case 0:
                                koneksi.clearScreen();
                                System.out.println("Terima kasih telah menggunakan aplikasi ini. Sampai jumpa!");
                                koneksi.pause(input);
                                return;
                            default:
                                System.out.println("Pilihan tidak valid.");
                            } 
                            if(role==0) break;
                        }
                    koneksi.clearScreen();
                    break;
                        
                    case 2:
                        System.out.println("Selamat datang, " + nama + "!\n");
                        System.out.println("Hubungi Melalui: " + gmail + "\n");
                        while (true) {
                            System.out.println("====================================================\r\n" + //
                                                "            MENU WAREHOUSE STAFF\r\n" + //
                                                "====================================================\r\n" + //
                                                "1. Data Barang\r\n" + //
                                                "2. Manajemen Stok\r\n" + //
                                                "3. Barang Masuk\r\n" + //
                                                "4. Supplier\r\n" + //
                                                "5. Riwayat Barang\r\n" + //
                                                "6. Logout\r\n" + //
                                                "0. Exit\r\n" + //
                                                "====================================================\r\n" + //
                                                "");
                            System.out.print("Pilih menu: ");
                            int pilih = Integer.parseInt(input.nextLine());
                            switch (pilih) {
                                case 1:
                                new menu(conn, input).showCrudMenu("Data Barang", "gudang", "product");
                                break;
                                case 2:
                                new menu(conn, input).showStockMenu();
                                break;
                                case 3:
                                new menu(conn, input).barangIn();
                                break;
                                case 4:
                                    new menu(conn, input).showCrudMenu("Data Supplier", "gudang", "supplier");
                                    break;
                                case 5:
                                    new menu(conn, input).riwayat();
                                    break;
                                case 6:
                                    role = 0;
                                    break;
                                case 0:
                                    koneksi.clearScreen();
                                    System.out.println("Terima kasih telah menggunakan aplikasi ini. Sampai jumpa!");
                                    koneksi.pause(input);
                                    return;
                                default:
                                    System.out.println("Pilihan tidak valid.");
                            }
                            if(role==0) break;
                        }
                    koneksi.clearScreen();
                    break;

                    default:
                        System.out.println("Peran tidak dikenal.");
                        break;
                }
            } else {
                System.out.println("\nUsername atau password salah.");
            }
        } catch (Exception e) {e.printStackTrace();}
    }
}
}



