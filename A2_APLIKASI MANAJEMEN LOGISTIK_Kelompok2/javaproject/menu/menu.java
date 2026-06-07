package menu;

import java.sql.*;
import java.util.*;

import database.koneksi;
import function.*;
import modell.*;

public class menu {
    private final Connection conn;
    private final Scanner input;

    public menu(Connection conn, Scanner input) {
        this.conn = conn;
        this.input = input;
    }

    public List<product> getAll() {

        List<product> list = new ArrayList<>();

        String sql =
                "SELECT * FROM gudang.product ORDER BY id_product";

        try (
                Connection conn = koneksi.connect();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                product p = new product()
                    .idProduct(rs.getInt("id_product"))
                    .idCategories(rs.getInt("id_categories"))
                    .idSupplier(rs.getInt("id_supplier"))
                    .name(rs.getString("name"))
                    .price(rs.getDouble("price"))
                    .unit(rs.getString("unit"));

                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void showCrudMenu(String title, String schema, String table) {
        while (true) {
            koneksi.clearScreen();
            System.out.println("\n========== " + title + " ==========");
            System.out.println("1. Lihat Semua");
            System.out.println("2. Cari");
            System.out.println("3. Tambah");
            System.out.println("4. Edit");
            System.out.println("5. Hapus");
            System.out.println("0. Kembali");
            System.out.println("===================================");
            System.out.print("Pilih menu: ");

            int pilihan;
            try {
                pilihan = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid. Silakan masukkan angka.");
                continue;
            }

            switch (pilihan) {
                case 1:
                    try {
                        View.printTable(conn, schema, table);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    koneksi.clearScreen();
                    searchByName(schema, table);
                    break;
                case 3:
                    koneksi.clearScreen();
                    insertRow(schema, table);
                    break;
                case 4:
                    koneksi.clearScreen();
                    updateRow(schema, table);
                    break;
                case 5:
                    koneksi.clearScreen();
                    deleteRow(schema, table);
                    break;
                case 0:
                    koneksi.clearScreen();
                    return;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
            koneksi.pause(input);
            break;
        }
    }

    public void showCrudUser(String title, String schema, String table) {
        while (true) {
            koneksi.clearScreen();
            System.out.println("\n===== " + title + " =====");
            System.out.println("1. Lihat Semua");
            System.out.println("2. Cari");
            System.out.println("3. Tambah");
            System.out.println("4. Edit");
            System.out.println("5. Hapus");
            System.out.println("0. Kembali");
            System.out.print("Pilih menu: ");

            int pilihan;
            try {
                pilihan = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid. Silakan masukkan angka.");
                continue;
            }

            switch (pilihan) {
                case 1:
                    try {
                        View.printTable(conn, schema, table);
                        koneksi.pause(input);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    searchUserName(schema, table);
                    koneksi.pause(input);
                    break;
                case 3:
                    insertRow(schema, table);
                    koneksi.pause(input);
                    break;
                case 4:
                    updateRow(schema, table);
                    koneksi.pause(input);
                    break;
                case 5:
                    deleteRow(schema, table);
                    koneksi.pause(input);
                    break;
                case 0:
                    koneksi.clearScreen();
                    return;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    public void CRUDproduct(String schema, String table) {
        while (true) {
            koneksi.clearScreen();
            System.out.println("\n===== Data Barang =====");
            System.out.println("1. Lihat Semua");
            System.out.println("2. Cari");
            System.out.println("3. Tambah");
            System.out.println("4. Edit");
            System.out.println("5. Hapus");
            System.out.println("0. Kembali");
            System.out.print("Pilih menu: ");

            int pilihan;
            try {
                pilihan = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid. Silakan masukkan angka.");
                continue;
            }

            switch (pilihan) {
                case 1:
                    try {
                        View.printTable(conn, schema, table);
                        koneksi.pause(input);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    searchByName(schema, table);
                    koneksi.pause(input);
                    break;
                case 3:
                    insertProduct(schema, table);
                    koneksi.pause(input);
                    break;
                case 4:        
                    updateRow(schema, table);
                    koneksi.pause(input);
                    break;
                case 5:
                    deleteProduct(schema, table);
                    koneksi.pause(input);
                    break;
                case 0:
                    koneksi.clearScreen();
                    return;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private void deleteProduct(String schema, String table) {
        Scanner input = new Scanner(System.in);
        Connection conn = koneksi.connect();

        try {
            conn.setAutoCommit(false);
            koneksi.clearScreen();
            View.printTable(conn, schema, table);
            System.out.print("Hapus Berdasarkan kolom : ");
            String whereKolom = input.nextLine();
            System.out.print("Nilai Kolomnya : ");
            String idValue = input.nextLine();
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rsWhere = metaData.getColumns(null, schema, "warehouse_stocks", whereKolom);
            if (!rsWhere.next()) {
                System.out.println("Kolom kriteria tidak ditemukan!");
                return;
            }
            int whereKolomType = rsWhere.getInt("DATA_TYPE");
            Object whereValue = menu.parseSqlValue(whereKolomType, idValue);
            String query = "DELETE FROM " + schema + "." + "warehouse_stocks" + " WHERE " + whereKolom + " = ?";
            PreparedStatement ps4 = conn.prepareStatement(query);
            menu.setPreparedStatementValue(ps4, 1, whereKolomType, whereValue);
            
            int efek = ps4.executeUpdate();
            if (efek > 0) {
                System.out.println("Data Dihapus");
                conn.setAutoCommit(true);
            }else{System.out.println("Data tidak ada");
                conn.rollback();
            }
            
            

            conn.setAutoCommit(false);
            ResultSet rsWhere1 = metaData.getColumns(null, schema, table, whereKolom);
            if (!rsWhere1.next()) {
                System.out.println("Kolom kriteria tidak ditemukan!");
                return;
            }
            String query1 = "DELETE FROM " + schema + "." + table + " WHERE " + whereKolom + " = ?";
            PreparedStatement ps5 = conn.prepareStatement(query1);
            menu.setPreparedStatementValue(ps5, 1, whereKolomType, whereValue);
            int efek1 = ps5.executeUpdate();
            if (efek1 > 0) {
                System.out.println("Data Dihapus");
                conn.setAutoCommit(true);
            }else{System.out.println("Data tidak ada");
                conn.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertProduct(String schema, String table) {
        Scanner input = new Scanner(System.in);
        Connection conn = koneksi.connect();
        try {
            String print = """
                    SELECT
                        p.id_product, p.name AS product_name, c.name AS category_name, s.name AS supplier_name, p.price, p.unit
                    FROM gudang.product p
                    JOIN gudang.categories c ON p.id_categories = c.id_categories
                    JOIN gudang.supplier s ON p.id_supplier = s.id_supplier;

                    """;
            PreparedStatement ps = conn.prepareStatement(print);
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();
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

            } catch (SQLException e) {
                e.printStackTrace();
            }

        try {
            conn.setAutoCommit(false);
            DatabaseMetaData metaData = conn.getMetaData();
            
            View.printTable(conn, "gudang", "product");
            View.printTable(conn, "gudang", "warehouse_stocks");
            

            System.out.println("""
                    
                    ============== Data Barang  ==============
                    """);

            System.out.print("ID Product \t: ");
            int idPro = input.nextInt();
            System.out.print("ID Supplier \t: ");
            int idSupp = input.nextInt();
            System.out.print("ID Categories \t: ");
            int idCate = input.nextInt();
            input.nextLine();
            System.out.print("Nama Barang \t: ");
            String name = input.nextLine();
            System.out.println("Unit Barang \t: ");
            String unit = input.nextLine();
            System.out.print("Harga Barang \t: ");
            int harga = input.nextInt();
            System.out.print("Jumlah Barang \t: ");
            int jumlah = input.nextInt();
            
            String hargaStr = String.valueOf(harga);
            ResultSet rs3 = metaData.getColumns(null, schema, table, "price");
            if (!rs3.next()) {
                System.out.println("Kolom tidak ditemukan!");
                return;
            }
            int tipeKolom = rs3.getInt("DATA_TYPE");
            ResultSet rsWhere = metaData.getColumns(null, schema, table, "price");
            if (!rsWhere.next()) {
                System.out.println("Kolom kriteria tidak ditemukan!");
                return;
            }
            int whereKolomType = rsWhere.getInt("DATA_TYPE");
            String whereValueInput = input.nextLine();
            Object newValue = menu.parseSqlValue(tipeKolom, hargaStr);
            Object whereValue = menu.parseSqlValue(whereKolomType, whereValueInput);
            
            System.out.print("ID Stock \t: ");
            int idStock = input.nextInt();
            System.out.print("ID Warehouse \t: ");
            int idWare = input.nextInt();
            input.nextLine();
            

            String query = "insert into gudang.product " +
            "(id_product, id_categories, id_supplier, name, price, unit) " +
            "values (?,?,?,?,?,?)";

            PreparedStatement psOut = conn.prepareStatement(query); 
                psOut.setInt(1, idPro);
                psOut.setInt(2, idCate);
                psOut.setInt(3, idSupp);
                psOut.setString(4, name);
                setPreparedStatementValue(psOut, 5, tipeKolom, newValue);
                psOut.setString(6, unit);
                psOut.executeUpdate();

            

            String insertOut = "insert into gudang.warehouse_stocks " +
                            "(id_stock, id_product, id_warehouse, quantity) " +
                            "VALUES (?,?,?,?)";

            PreparedStatement psOutDetail = conn.prepareStatement(insertOut);
            psOutDetail.setInt(1, idStock);
            psOutDetail.setInt(2, idPro);
            psOutDetail.setInt(3, idWare);
            psOutDetail.setInt(4, jumlah);
            psOutDetail.executeUpdate();
            

            conn.commit();
            System.out.println("""
                    
                    [SUCCESS] Barang masuk berhasil dibuat.
                    """);
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ignored) {
            }
        }
    }

    public void showStockMenu() {
        while (true) {
            koneksi.clearScreen();
            System.out.println("\n===== Manajemen Stok =====");
            System.out.println("1. Lihat Stok");
            System.out.println("2. Stok Banyak");
            System.out.println("3. Stok Sedikit");
            System.out.println("0. Kembali");
            System.out.println("============================");
            System.out.print("\nPilih menu: ");
            int pilihan = Integer.parseInt(input.nextLine());

            switch (pilihan) {
                case 1:
                    viewStock();
                    koneksi.pause(input);
                    break;
                case 2:
                    max();
                    koneksi.pause(input);
                    break;
                case 3:
                    min();
                    koneksi.pause(input);
                    break;
                case 0:
                    koneksi.clearScreen();
                    return;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private void min(){
        String query = """
                select * from gudang.warehouse_stocks where quantity < 15
                """;

        try (PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()){
            printResultSet2(rs);
        } catch (Exception e) {
        }
    }

    private void max() {
        String query = """
                select * from gudang.warehouse_stocks where quantity > 50
                """;

        try (PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()){
            printResultSet2(rs);
        } catch (Exception e) {
        }
    }

    private void viewStock() {
        String query =
            "SELECT " + //
            "p.id_product, p.name AS product_name, c.name AS category_name, s.name AS supplier_name, p.price, p.unit " + //
            "FROM gudang.product p " + //
            "JOIN gudang.categories c ON p.id_categories = c.id_categories " + //
            "JOIN gudang.supplier s ON p.id_supplier = s.id_supplier;";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            printResultSet2(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected static int getNextStockId() throws SQLException {
        Connection conn = koneksi.connect();
        String query = "SELECT COALESCE(MAX(id_stock), 0) + 1 AS next_id FROM gudang.warehouse_stocks";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt("next_id");
            }
        }
        return 1;
    }

    protected static int getNextShipmentId() throws SQLException {
        Connection conn = koneksi.connect();
        String query = "SELECT COALESCE(MAX(id_shipment), 0) + 1 AS next_id FROM gudang.shipment";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt("next_id");
            }
        }
        return 1;
    }

    protected int getNextOutDetail() throws SQLException {
        String query = "SELECT COALESCE(MAX(id_outgoods_detail), 0) + 1 AS next_id FROM gudang.outgoing_goods_detail";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt("next_id");
            }
        }
        return 1;
    }

    protected static int getNextShipmentDetailId() throws SQLException {
        Connection conn = koneksi.connect();
        String query = "SELECT COALESCE(MAX(id_shipment_details), 0) + 1 AS next_id FROM gudang.shipment_details";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt("next_id");
            }
        }
        return 1;
    }

    private void searchByName(String schema, String table) {
        try {
            koneksi.clearScreen();
            System.out.print("Masukkan Nama: ");
            String nama = input.nextLine();
            String query = "SELECT * FROM " + schema + "." + table + " WHERE name ILIKE ? ";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, "%" + nama + "%");
            ResultSet rs = ps.executeQuery();
            printResultSet2(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    private void searchUserName(String schema, String table) {
        try {
            koneksi.clearScreen();
            System.out.print("Masukkan Nama: ");
            String nama = input.nextLine();
            String query = "SELECT * FROM " + schema + "." + table + " WHERE username ILIKE ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, "%" + nama + "%");
            ResultSet rs = ps.executeQuery();
            printResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertRow(String schema, String table) {
        inserting.insert(conn, input,  schema,  table);
    }

    private void updateRow(String schema, String table) {
        editing.edit(conn, input, schema, table);
    }

    private void deleteRow(String schema, String table) {
        deletes.deleting(conn, input, schema, table);
    }

    public static void setPreparedStatementValue(PreparedStatement ps, int index, int tipeKolom, Object nilai) throws SQLException {
        if (nilai == null) {
            ps.setNull(index, tipeKolom);
            return;
        }
        switch (tipeKolom) {
            case Types.INTEGER:
            case Types.SMALLINT:
            case Types.BIGINT:
                ps.setInt(index, (Integer) nilai);
                break;
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
                ps.setDouble(index, (Double) nilai);
                break;
            case Types.NUMERIC:
            case Types.DECIMAL:
                ps.setBigDecimal(index, (java.math.BigDecimal) nilai);
                break;
            case Types.BOOLEAN:
            case Types.BIT:
                ps.setBoolean(index, (Boolean) nilai);
                break;
            case Types.DATE:
                ps.setDate(index, (java.sql.Date) nilai);
                break;
            case Types.TIMESTAMP:
            case Types.TIMESTAMP_WITH_TIMEZONE:
                ps.setTimestamp(index, (java.sql.Timestamp) nilai);
                break;
            default:
                ps.setString(index, nilai.toString());
                break;
        }
    }

    public static Object parseSqlValue(int tipeKolom, String userInput) {
        String value = userInput == null ? "" : userInput.trim();
        switch (tipeKolom) {
            case Types.INTEGER:
            case Types.SMALLINT:
            case Types.BIGINT:
                return value.isEmpty() ? 0 : Integer.parseInt(value);
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
                return value.isEmpty() ? 0.0 : Double.parseDouble(value);
            case Types.NUMERIC:
            case Types.DECIMAL:
                return value.isEmpty() ? java.math.BigDecimal.ZERO : new java.math.BigDecimal(value);
            case Types.BOOLEAN:
            case Types.BIT:
                return value.isEmpty() ? Boolean.FALSE : Boolean.parseBoolean(value);
            case Types.DATE:
                return value.isEmpty() ? java.sql.Date.valueOf(java.time.LocalDate.now()) : java.sql.Date.valueOf(value);
            case Types.TIMESTAMP:
            case Types.TIMESTAMP_WITH_TIMEZONE:
                return value.isEmpty() ? java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()) : java.sql.Timestamp.valueOf(value);
            default:
                return value;
        }
    }

    private void printResultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();

        if (!rs.next()) {
            System.out.println("Data tidak ditemukan!");
            return;
        }

        for (int i = 1; i <= columnCount; i++) {
            System.out.printf("%-20s", "---------------------");
        }
        System.out.println();
        for (int i = 1; i <= columnCount; i++) {
            System.out.printf("%-20s", "| " + meta.getColumnName(i));
        }
        System.out.println("   |");
        for (int i = 1; i <= columnCount; i++) {
            System.out.printf("%-20s", "---------------------");
        }
        System.out.println();
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                System.out.printf("%-20s", "| " + rs.getString(i));
            }
            System.out.println("     |");
        }
    }

    private void printResultSet2(ResultSet rs) throws SQLException {
    ResultSetMetaData meta = rs.getMetaData();
    int columnCount = meta.getColumnCount();

    // cek apakah ada data
    if (!rs.next()) {
        System.out.println("Data tidak ditemukan!");
        return;
    }

    // garis atas
    for (int i = 1; i <= columnCount; i++) {
        System.out.printf("%-20s", "---------------------");
    }
    System.out.println();

    // nama kolom
    for (int i = 1; i <= columnCount; i++) {
        System.out.printf("%-20s", "| " + meta.getColumnName(i));
    }
    System.out.println("     |");

    // garis bawah header
    for (int i = 1; i <= columnCount; i++) {
        System.out.printf("%-20s", "---------------------");
    }
    System.out.println();

    // print data pertama
    for (int i = 1; i <= columnCount; i++) {
        System.out.printf("%-20s", "| " + rs.getString(i));
    }
    System.out.println("     |");

    // print data berikutnya
    while (rs.next()) {
        for (int i = 1; i <= columnCount; i++) {
            System.out.printf("%-20s", "| " + rs.getString(i));
        }
        System.out.println("     |");
    }

    for (int i = 1; i <= columnCount; i++) {
        System.out.printf("%-20s", "---------------------");
    }
    System.out.println();
}

    public void riwayat(){
        int pilih;

        while (true) {
            koneksi.clearScreen();
            System.out.println("""
                    
                    ============== RIWAYAT TRANSAKSI ================
                    1. Laporan Riwayat Barang Keluar
                    2. Laporan Riwayat Barang Masuk
                    3. Laporan Riwayat Distribusi Warehouse
                    0. Kembali
                    =================================================
                    """);

            System.out.print("Pilih Menu : ");
            pilih = Integer.parseInt(input.nextLine());

            switch (pilih) {

                case 1:
                    riwayatBarangKeluar();
                    koneksi.pause(input);
                    break;

                case 2:
                    riwayatBarangMasuk();
                    koneksi.pause(input);
                    break;

                case 3:
                    riwayatDistribusi();
                    koneksi.pause(input);
                    break;

                case 0:
                    koneksi.clearScreen();
                    return;

                default:
                    System.out.println("Menu tidak tersedia");
                    break;
            }
        }
    }

    public void riwayatBarangMasuk(){
        try {
            View.printTable(conn, "gudang", "incoming_goods");
            koneksi.pause(input);
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public void riwayatBarangKeluar() {

    try {
            View.printTable(conn, "gudang", "outgoing_goods");
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public void riwayatDistribusi() {

    try {
            View.printTable(conn, "gudang", "shipment");
            View.printTable(conn, "gudang", "distribution");
            koneksi.pause(input);
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public void user(String schema, String table){
        int pilih;
        do {
            koneksi.clearScreen();
        System.out.println("""
                
                ============= MANAJEMEN USER ==============
                1. Lihat User
                2. Tambah User
                3. Edit User
                4. Hapus User
                0. Kembali
                ===========================================
                """);

        System.out.print("Pilih Menu : ");
        pilih = Integer.parseInt(input.nextLine());

        switch (pilih) {

            case 1:
                try {
                    View.printTable(conn, "gudang", "users");
                    koneksi.pause(input);
                } catch (Exception e) {
                }
                break;

            case 2:
                insertRow(schema, table);
                koneksi.pause(input);
                break;

            case 3:
                updateRow(schema, table);
                koneksi.pause(input);
                break;

            case 4:
                deleteRow(schema, table);
                koneksi.pause(input);
                break;

            case 0:
                koneksi.clearScreen();
                return;

            default:
                System.out.println("Menu tidak tersedia");
        }

    } while (pilih != 0);
    }

    public void distribusi(){

    int pilih;

    do {
        koneksi.clearScreen();
        System.out.println("""
                
                ======== DISTRIBUSI ANTAR WAREHOUSE ========

                1. Buat Distribusi
                0. Kembali

                ============================================
                """);

        System.out.print("Pilih Menu : ");
        pilih = Integer.parseInt(input.nextLine());

        switch (pilih) {

            case 1:
                Workflow.buatDistribusi();
                koneksi.pause(input);
                break;

            case 0:
                koneksi.clearScreen();
                break;

            default:
                System.out.println("Menu tidak tersedia");
        }

    } while (pilih != 0);
    }

    

    public void barangOut(){
        while (true) {
            koneksi.clearScreen();
            System.out.println("============== BARANG KELUAR ==============\r\n" + //
                        "1. Input Barang Keluar\r\n" + //
                        "2. Detail Barang Keluar\r\n" + //
                        "0. Kembali\r\n" + //
                        "===========================================\r\n" + //
                        "");

            System.out.print("Pilih Menu: ");
            int pilih = input.nextInt();
            input.nextLine();

            switch (pilih) {
                case 1:
                    Workflow.insertOutgoing();
                    koneksi.pause(input);
                    break;
                
                case 2:
                    try {
                        showLastOutGoods();
                        koneksi.pause(input);
                    } 
                    catch (Exception e) {
                        }
                    break;

                case 0:
                    koneksi.clearScreen();
                    return;

                default:
                    System.out.println("Pilihan Tidak Valid!");;
            }
        }
    }

    public void barangIn(){
        while (true) {
            koneksi.clearScreen();
            System.out.println("\n============== BARANG MASUK ==============\r\n" + //
                        "1. Input Barang Masuk\r\n" + //
                        "2. Detail Barang Masuk\r\n" + //
                        "0. Kembali\r\n" + //
                        "===========================================\r\n" + //
                        "");

            System.out.print("Pilih Menu: ");
            int pilih = input.nextInt();
            input.nextLine();

            switch (pilih) {
                case 1:
                    Workflow.insertIncoming();
                    koneksi.pause(input);
                    break;
                
                case 2:
                    try {
                        showLastInGoods();
                        koneksi.pause(input);
                    } 
                    catch (Exception e) {
                        }
                    break;

                case 0:
                    koneksi.clearScreen();
                    return;

                default:
                    System.out.println("Pilihan Tidak Valid!");;
            }
        }
    }

    private void showLastInGoods(){

        String query = """
        SELECT *
        FROM gudang.incoming_goods
        ORDER BY id_ingoods DESC
        LIMIT 1
        """;

    try (PreparedStatement ps = conn.prepareStatement(query);
         ResultSet rs = ps.executeQuery()) {

        printResultSet2(rs);

    } catch (SQLException e) {
        e.printStackTrace();
    }
    }

    private void showLastOutGoods(){

        String query = """
        SELECT *
        FROM gudang.outgoing_goods
        ORDER BY id_outgoods DESC
        LIMIT 1
        """;

    try (PreparedStatement ps = conn.prepareStatement(query);
         ResultSet rs = ps.executeQuery()) {

        printResultSet2(rs);

    } catch (SQLException e) {
        e.printStackTrace();
    }
    }

    
        
    
}
    




