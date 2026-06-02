import java.sql.*;
import java.util.Scanner;




public class editing {
    public static void edit(Connection conn, Scanner input, String schema, String table) {
        try {
            koneksi.clearScreen();
            View.printTable(conn, schema, table);
            System.out.print("Kolom yang akan diubah: ");
            String kolom1 = input.nextLine();
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs3 = metaData.getColumns(null, schema, table, kolom1);
            if (!rs3.next()) {
                System.out.println("Kolom tidak ditemukan!");
                return;
            }
            int tipeKolom = rs3.getInt("DATA_TYPE");
            System.out.print("Nilai baru\t: ");
            String inputValue = input.nextLine();
            System.out.print("Masukkan Kolom Kriteria\t: ");
            String whereKolom = input.nextLine();
            System.out.print("Nilai Kriteria\t: ");
            String whereValueInput = input.nextLine();
            ResultSet rsWhere = metaData.getColumns(null, schema, table, whereKolom);
            if (!rsWhere.next()) {
                System.out.println("Nilai kriteria tidak ditemukan!");
                return;
            }
            int whereKolomType = rsWhere.getInt("DATA_TYPE");
            Object newValue = menu.parseSqlValue(tipeKolom, inputValue);
            Object whereValue = menu.parseSqlValue(whereKolomType, whereValueInput);

            String query = "UPDATE " + schema + "." + table + " SET " + kolom1 + " = ? WHERE " + whereKolom + " = ?";
            PreparedStatement ps3 = conn.prepareStatement(query);
            menu.setPreparedStatementValue(ps3, 1, tipeKolom, newValue);
            menu.setPreparedStatementValue(ps3, 2, whereKolomType, whereValue);
            ps3.executeUpdate();
            System.out.println("Data berhasil diupdate");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
    
