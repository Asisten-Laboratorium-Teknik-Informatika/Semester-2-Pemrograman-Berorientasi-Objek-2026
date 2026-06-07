package dao;

import java.sql.*;

public class PermintaanStokDAO {
    private Connection connection;

    public PermintaanStokDAO(Connection connection) {
        this.connection = connection;
    }

    public void tampilkanPermintaanStok() {
        System.out.println("\n--- DAFTAR PERMINTAAN STOK DARI TOKO ---");
        System.out.printf("%-5s %-20s %-12s %-12s\n", "ID", "Nama Produk", "Jumlah Minta", "Status");
        System.out.println("---------------------------------------------------------");

        String query = "SELECT ps.id_permintaan, p.nama_produk, ps.jumlah_minta, ps.status_permintaan " +
                       "FROM permintaan_stok ps " +
                       "JOIN produk p ON ps.id_produk = p.id_produk " +
                       "WHERE ps.status_permintaan = 'PENDING'";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            boolean adaData = false;
            while (rs.next()) {
                adaData = true;
                int id = rs.getInt("id_permintaan");
                String namaProd = rs.getString("nama_produk");
                int jumlah = rs.getInt("jumlah_minta");
                String status = rs.getString("status_permintaan");

                if (namaProd.length() > 20) {
                    namaProd = namaProd.substring(0, 17) + "...";
                }

                System.out.printf("%-5d %-20s %-12d [%-10s]\n", id, namaProd, jumlah, status);
            }

            if (!adaData) {
                System.out.println("Aman! Belum ada permintaan stok baru dari toko.");
            }
            System.out.println("---------------------------------------------------------\n");

        } catch (SQLException e) {
            System.out.println("Gagal mengambil data permintaan stok: " + e.getMessage());
        }
    }
}