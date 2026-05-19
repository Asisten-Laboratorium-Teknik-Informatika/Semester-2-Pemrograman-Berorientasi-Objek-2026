package com.penjualan.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class Koneksi {

    public static Connection connect() {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/sistem_penjualan";
            String user = "postgres";
            String password = "postgres";
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("? Koneksi ke database sistem_penjualan berhasil!");
            return conn;
        } catch (Exception e) {
            System.err.println("? Koneksi gagal: " + e.getMessage());
            return null;
        }
    }
}
