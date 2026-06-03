package SDM_PegawaiPayroll;

import java.sql.Connection;
import java.sql.DriverManager;

public class KoneksiDatabase {

    public static Connection connect() {

        Connection conn = null;

        try {
            Class.forName("org.postgresql.Driver");

            conn = 
            DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/db_sdm",
            "postgres",
            "postgres");

            System.out.println(Warna.HIJAU + "Koneksi Berhasil!" + Warna.RESET);
        } catch (Exception e) {  
            System.out.println(e);
        }

        return conn;
    } 
}
