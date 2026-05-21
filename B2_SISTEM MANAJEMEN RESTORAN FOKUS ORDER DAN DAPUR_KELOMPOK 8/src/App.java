import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {

        String url = "jdbc:postgresql://localhost:5432/dbku";
        String user = "postgres";
        String password = "postgres";

        try {
            Class.forName("org.postgresql.Driver"); 

            Connection conn = DriverManager.getConnection(url, user, password);

            if (conn != null) {
                System.out.println("Connected to the database!");
            }

        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());

        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found: " + e.getMessage());
        }
    }
}
