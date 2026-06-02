
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class deletes {
    public static void deleting(Connection conn, Scanner input, String schema, String table) {
        try {
            conn.setAutoCommit(false);
            koneksi.clearScreen();
            View.printTable(conn, schema, table);
            System.out.print("Hapus Berdasarkan kolom : ");
            String whereKolom = input.nextLine();
            System.out.print("Nilai Kolomnya : ");
            String idValue = input.nextLine();
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rsWhere = metaData.getColumns(null, schema, table, whereKolom);
            if (!rsWhere.next()) {
                System.out.println("Kolom kriteria tidak ditemukan!");
                return;
            }
            int whereKolomType = rsWhere.getInt("DATA_TYPE");
            Object whereValue = menu.parseSqlValue(whereKolomType, idValue);
            String query = "DELETE FROM " + schema + "." + table + " WHERE " + whereKolom + " = ?";
            PreparedStatement ps4 = conn.prepareStatement(query);
            menu.setPreparedStatementValue(ps4, 1, whereKolomType, whereValue);
            
            int efek = ps4.executeUpdate();
            if (efek > 0) {
                System.out.println("Data Dihapus");
            }else{System.out.println("Data tidak ada");}

            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
    
