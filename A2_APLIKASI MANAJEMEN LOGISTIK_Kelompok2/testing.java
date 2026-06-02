
import java.sql.Connection;

public class testing{
    public static void testKoneksi() {
        Connection conn = koneksi.connect();
        if(conn != null){
            System.out.println("BERHASIL TERHUBUNG KE DATABASE!");
        } else {
            System.out.println("GAGAL TERHUBUNG KE DATABASE!");
        }
    }
}