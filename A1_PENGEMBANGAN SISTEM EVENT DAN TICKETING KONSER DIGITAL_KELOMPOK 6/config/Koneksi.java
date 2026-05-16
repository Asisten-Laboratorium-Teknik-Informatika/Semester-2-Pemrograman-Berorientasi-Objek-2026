package config;

import java.sql.Connection;
import java.sql.DriverManager;

public class Koneksi {

    private static final String URL =
            "jdbc:postgresql://localhost:5432/project";

    private static final String USER =
            "postgres";

    private static final String PASSWORD =
            "postgres";

    public static Connection getKoneksi() {

        Connection conn = null;

        try {

            Class.forName(
                    "org.postgresql.Driver"
            );

            conn = DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
            );

        } catch (Exception e) {

            System.out.println(
                    "Koneksi gagal!"
            );

            e.printStackTrace();
        }

        return conn;
    }
}