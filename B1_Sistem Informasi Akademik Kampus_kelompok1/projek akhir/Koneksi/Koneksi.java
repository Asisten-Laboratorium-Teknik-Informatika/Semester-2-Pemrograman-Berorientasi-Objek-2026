package Koneksi;

import java.sql.Connection;
import java.sql.DriverManager;

public class Koneksi {
    public static Connection connect(){
        try {
            String url = "jdbc:postgresql://localhost:5432/SIAKAD";
            String user = "postgres";
            String password = "postgres";
            
            Connection conn = DriverManager.getConnection(url, user, password);
            conn.createStatement().execute("SET search_path TO b1, public");
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}