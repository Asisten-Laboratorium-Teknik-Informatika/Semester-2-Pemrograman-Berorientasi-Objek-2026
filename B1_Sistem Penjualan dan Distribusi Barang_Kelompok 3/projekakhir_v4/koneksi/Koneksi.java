package koneksi;
import java.sql.Connection;
import java.sql.DriverManager;

public class Koneksi {
    public static Connection connect() {
        try {
            String url = "jdbc:postgresql://localhost:5432/projekakhir_v4";
            String user = "postgres";
            String password = "postgres";

            Connection conn = DriverManager.getConnection(url, user, password);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
