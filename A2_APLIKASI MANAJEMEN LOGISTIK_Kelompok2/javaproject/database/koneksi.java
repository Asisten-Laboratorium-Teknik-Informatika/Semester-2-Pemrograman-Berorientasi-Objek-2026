package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class koneksi {
    public static Connection connect() {
        try {
            String url = "jdbc:postgresql://localhost:5432/gudang_logistik";
            String user = "postgres";
            String password = "postgres";

            Connection conn = DriverManager.getConnection(url, user, password);
            return conn;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void clearScreen() {
    try {
        new ProcessBuilder("cmd", "/c", "cls")
                .inheritIO()
                .start()
                .waitFor();
    } catch (Exception e) {
        e.printStackTrace();
    }
    }
    public static void pause(Scanner input) {
    System.out.print("\nTekan Enter untuk melanjutkan...");
    input.nextLine();
}
}
