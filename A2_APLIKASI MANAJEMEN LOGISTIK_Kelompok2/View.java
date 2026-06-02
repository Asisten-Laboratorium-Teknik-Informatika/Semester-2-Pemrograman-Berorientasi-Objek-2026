
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class View {
    public static void viewing() {
        koneksi.clearScreen();
        Scanner scanning = new Scanner(System.in);
        String schema = "gudang";
        Connection conn = koneksi.connect();
        
        

        try {
            Set<String> forbidden = new HashSet<>();
            forbidden.add("incoming_goods");
            forbidden.add("incoming_goods_detail");
            forbidden.add("outgoing_goods");
            forbidden.add("outgoing_goods_detail");
            
            ArrayList<String> tables = getTables(conn, schema);
            // munculin tabel
            System.out.println("\n=== LIST ===");
            for (int i = 0; i < tables.size(); i++) {
                System.out.println((i + 1) + ". " + tables.get(i));
            }
            // milih tabel
            System.out.print("\nKetik nomor atau nama tabel : ");
            String input = scanning.nextLine();
            String selectedTable = null;
            try {
                int index = Integer.parseInt(input);
                if (index >= 1 && index <= tables.size()) {selectedTable = tables.get(index - 1);}
            } catch (NumberFormatException e) {
                if (tables.contains(input)) {selectedTable = input;}
                
            }
            if (selectedTable == null || forbidden.contains(selectedTable)) {
                System.out.println("Pilihan tidak valid atau tidak diizinkan.");
                scanning.close();
                return;
            }
            printTable(conn, schema, selectedTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ambil tabel di schema
    public static ArrayList<String> getTables(Connection conn, String schema) throws SQLException {
        ArrayList<String> tables = new ArrayList<>();
        String query = """
            SELECT table_name
            FROM information_schema.tables
            WHERE table_schema = ?
            AND table_type = 'BASE TABLE'
            ORDER BY table_name
        """;
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, schema);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            tables.add(rs.getString("table_name"));
        }
        return tables;
    }

    // output
    public static void printTable(Connection conn, String schema, String table) throws SQLException {
        String query = "SELECT * FROM " + schema + "." + table;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();
        System.out.println("\n=== " + table + " ===");
        System.out.println();
        for (int i = 1; i <= columnCount; i++) { // separator
            System.out.printf("%-20s", "---------------------");
        }
        System.out.println();
        for (int i = 1; i <= columnCount; i++) { // kolom
            System.out.printf("%-20s", "| " + meta.getColumnName(i));
        }
        System.out.println("   |");
        for (int i = 1; i <= columnCount; i++) { // separator
            System.out.printf("%-20s", "---------------------");
        }
        System.out.println();
        while (rs.next()) { // isi 
            for (int i = 1; i <= columnCount; i++) {
                System.out.printf("%-20s", "| " + rs.getString(i));
            }
            System.out.println("   |");
        }
        for (int i = 1; i <= columnCount; i++) { // separator
            System.out.printf("%-20s", "---------------------");
        }
        System.out.println();
        System.out.println();
    }
    
}
