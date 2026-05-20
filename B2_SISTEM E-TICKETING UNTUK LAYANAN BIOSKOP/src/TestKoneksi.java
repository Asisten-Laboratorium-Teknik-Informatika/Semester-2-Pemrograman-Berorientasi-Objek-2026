import java.sql.Connection;

public class TestKoneksi {
    public static void main(String[] args) {
		Connection conn = Koneksi.connect();

		if (conn != null) {
		System.out.println("Berhasil konek ke database!");
		} else {
			System.out.println("Gagal Konek ke database!");
		}
	}
}
