import java.sql.Connection;
import java.sql.DriverManager;

public class Koneksi {
    public static Connection connect() {
		try {
			// Memastikan Driver PostgreSQL dimuat (berguna untuk kompatibilitas)
			Class.forName("org.postgresql.Driver");

			String url = "jdbc:postgresql://localhost:5432/bioskop_mtix";
			String user = "postgres";
			String password = "postgres";

			Connection conn = DriverManager.getConnection(url, user, password);
			return conn;
		} catch (Exception e) {
			System.err.println("Koneksi Database Gagal: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}