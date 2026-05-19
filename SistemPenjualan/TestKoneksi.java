import com.penjualan.utils.Koneksi;
import java.sql.Connection;

public class TestKoneksi {
    public static void main(String[] args) {
        Connection conn = Koneksi.connect();
        if (conn != null) {
            System.out.println("✅ Koneksi berhasil!");
        } else {
            System.out.println("❌ Koneksi gagal!");
        }
    }
}