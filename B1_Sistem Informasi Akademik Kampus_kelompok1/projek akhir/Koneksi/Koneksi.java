import java.sql.Connection;
import java.sql.DriverManager;

public class Koneksi {
    public static Connection connect(){
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/sistem_akademi_kampus";
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
