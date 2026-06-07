package function;

import java.sql.*;
import java.util.*;

import database.koneksi;
import menu.menu;

public class inserting {

    public static void insert(Connection conn, Scanner input, String schema, String table) {
        
    
    try {
            koneksi.clearScreen();
            View.printTable(conn, schema, table);
            String query = "SELECT * FROM " + schema + "." + table + " LIMIT 0";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int jumlahKolom = meta.getColumnCount();
            List<String> kolomList = new ArrayList<>();
            List<Object> nilaiList = new ArrayList<>();
            List<Integer> tipeKolomList = new ArrayList<>();

            System.out.println("\n=== INPUT ===");
            for (int i = 1; i <= jumlahKolom-1; i++) {
                if (meta.isAutoIncrement(i)) {
                    continue;
                }
                String namaKolom = meta.getColumnName(i);
                int tipeKolom = meta.getColumnType(i);
                System.out.print(namaKolom + " :" );
                String userInput = input.nextLine();
                Object nilai = menu.parseSqlValue(tipeKolom, userInput);
                kolomList.add(namaKolom);
                nilaiList.add(nilai);
                tipeKolomList.add(tipeKolom);
            }

            StringBuilder insertSQL = new StringBuilder();
            insertSQL.append("INSERT INTO " + schema + "." + table + " (");
            for (int i = 0; i < kolomList.size(); i++) {
                insertSQL.append(kolomList.get(i));
                if (i < kolomList.size() - 1) {
                    insertSQL.append(", ");
                }
            }
            insertSQL.append(") VALUES (");
            for (int i = 0; i < nilaiList.size(); i++) {
                insertSQL.append("?");
                if (i < nilaiList.size() - 1) {
                    insertSQL.append(", ");
                }
            }
            insertSQL.append(")");

            PreparedStatement insertPS = conn.prepareStatement(insertSQL.toString());
            for (int i = 0; i < nilaiList.size(); i++) {
                Object nilai = nilaiList.get(i);
                int tipeKolom = tipeKolomList.get(i);
                menu.setPreparedStatementValue(insertPS, i + 1, tipeKolom, nilai);
            }

            int hasil = insertPS.executeUpdate();
            if (hasil > 0) {
                System.out.println("\nData berhasil masuk");
            } else {
                System.out.println("\nData gagal dimasukkan.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}