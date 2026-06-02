
import java.sql.*;
import java.util.Scanner;

public class Workflow extends menu {
    public Workflow(Connection conn, Scanner input){
        super(conn, input);
    }
    

    public static void insertIncoming(){
        Scanner input = new Scanner(System.in);
        Connection conn = koneksi.connect();
        try {
            String print = """
                    SELECT ig.invoice_code,
                        s.name AS supplier,
                        w.name AS warehouse,
                        p.name AS product,
                        igd.quantity,
                        ig.status,
                        ig.incoming_date
                    FROM gudang.incoming_goods ig
                    JOIN gudang.supplier s ON ig.id_supplier = s.id_supplier
                    JOIN gudang.warehouse w ON ig.id_warehouse = w.id_warehouse
                    JOIN gudang.incoming_goods_detail igd ON ig.id_ingoods = igd.id_ingoods
                    JOIN gudang.product p ON igd.id_product = p.id_product;
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
            koneksi.clearScreen();
            View.printTable(conn, "gudang", "incoming_goods");
            

            System.out.println("""
                    
                    ============== Barang Masuk ==============
                    """);

            System.out.print("ID Incoming \t: ");
            int idOut = input.nextInt();
            System.out.print("ID Supplier \t: ");
            int idCust = input.nextInt();
            System.out.print("ID Warehouse \t: ");
            int idWare = input.nextInt();
            System.out.print("ID User \t: ");
            int idUser = input.nextInt();
            System.out.print("ID Barang \t: ");
            int idBarang = input.nextInt();
            System.out.print("Jumlah Barang \t: ");
            int jumlah = input.nextInt();
            input.nextLine();
            System.out.print("Invoice Code \t: ");
            String invCode = input.nextLine();
            System.out.print("Status \t: ");
            String status = input.nextLine();

            String cekStok = """
                    SELECT id_stock, quantity
                    FROM gudang.warehouse_stocks
                    WHERE id_warehouse = ?
                    AND id_product = ?
                    """;

            PreparedStatement psCek = conn.prepareStatement(cekStok);
            psCek.setInt(1, idWare);
            psCek.setInt(2, idBarang);
            ResultSet rs = psCek.executeQuery();

            if (!rs.next()) {
                System.out.println("Barang tidak ditemukan di warehouse asal.");
                conn.rollback();
                return;
            }

            int sourceStockId = rs.getInt("id_stock");
            


            String updateAsal = """
                    UPDATE gudang.warehouse_stocks
                    SET quantity = quantity + ?, updated_at = current_timestamp
                    WHERE id_stock = ?
                    """;

            try (PreparedStatement psAsal = conn.prepareStatement(updateAsal)) {
                psAsal.setInt(1, jumlah);
                psAsal.setInt(2, sourceStockId);
                psAsal.executeUpdate();
            }

            String query = "insert into gudang.incoming_goods " +
            "(id_ingoods, id_supplier, id_warehouse, id_user, invoice_code, status) " +
            "values (?,?,?,?,?,?)";

            PreparedStatement psOut = conn.prepareStatement(query); 
                psOut.setInt(1, idOut);
                psOut.setInt(2, idCust);
                psOut.setInt(3, idWare);
                psOut.setInt(4, idUser);
                psOut.setString(5, invCode);
                psOut.setString(6, status);
                psOut.executeUpdate();

            

            String insertOut = "insert into gudang.incoming_goods_detail " +
                            "(id_ingoods_detail, id_ingoods, id_product, quantity) " +
                            "VALUES (?,?,?,?)";

            PreparedStatement psOutDetail = conn.prepareStatement(insertOut);
            psOutDetail.setInt(1, idOut);
            psOutDetail.setInt(2, idOut);
            psOutDetail.setInt(3, idBarang);
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

    public static void insertOutgoing() {
        Connection conn = koneksi.connect();
        Scanner input = new Scanner(System.in);

        try {
            String print = """
                    SELECT
                        og.invoice_code,
                        c.name AS customer,
                        w.name AS warehouse,
                        p.name AS product,
                        ogd.quantity,
                        og.status,
                        og.outgoing_date
                    FROM gudang.outgoing_goods og
                    JOIN gudang.customer c ON og.id_customer = c.id_customer
                    JOIN gudang.warehouse w ON og.id_warehouse = w.id_warehouse
                    JOIN gudang.outgoing_goods_detail ogd ON og.id_outgoods = ogd.id_outgoods
                    JOIN gudang.product p ON ogd.id_product = p.id_product;
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
            koneksi.clearScreen();
            View.printTable(conn, "gudang", "outgoing_goods");
            

            System.out.println("""
                    
                    ============== Barang Keluar ==============
                    """);

            System.out.print("ID Outgoods \t: ");
            int idOut = input.nextInt();
            System.out.print("ID Customer \t: ");
            int idCust = input.nextInt();
            System.out.print("ID Warehouse \t: ");
            int idWare = input.nextInt();
            System.out.print("ID User \t: ");
            int idUser = input.nextInt();
            System.out.print("ID Barang \t: ");
            int idBarang = input.nextInt();
            System.out.print("Jumlah Barang \t: ");
            int jumlah = input.nextInt();
            input.nextLine();
            System.out.print("Invoice Code \t: ");
            String invCode = input.nextLine();
            System.out.print("Destination \t: ");
            String destination = input.nextLine();
            System.out.print("Status \t: ");
            String status = input.nextLine();

            String cekStok = """
                    SELECT id_stock, quantity
                    FROM gudang.warehouse_stocks
                    WHERE id_warehouse = ?
                    AND id_product = ?
                    """;

            PreparedStatement psCek = conn.prepareStatement(cekStok);
            psCek.setInt(1, idWare);
            psCek.setInt(2, idBarang);
            ResultSet rs = psCek.executeQuery();

            if (!rs.next()) {
                System.out.println("Barang tidak ditemukan di warehouse asal.");
                conn.rollback();
                return;
            }

            int sourceStockId = rs.getInt("id_stock");
            int stokSekarang = rs.getInt("quantity");

            if (stokSekarang < jumlah) {
                System.out.println("Stok tidak mencukupi.");
                conn.rollback();
                return;
            }

            String updateAsal = """
                    UPDATE gudang.warehouse_stocks
                    SET quantity = quantity - ?, updated_at = current_timestamp
                    WHERE id_stock = ?
                    """;

            try (PreparedStatement psAsal = conn.prepareStatement(updateAsal)) {
                psAsal.setInt(1, jumlah);
                psAsal.setInt(2, sourceStockId);
                psAsal.executeUpdate();
            }

            String query = "insert into gudang.outgoing_goods " +
            "(id_outgoods, id_customer, id_warehouse, id_user, invoice_code, destination_address, status) " +
            "values (?,?,?,?,?,?,?)";

            PreparedStatement psOut = conn.prepareStatement(query); 
                psOut.setInt(1, idOut);
                psOut.setInt(2, idCust);
                psOut.setInt(3, idWare);
                psOut.setInt(4, idUser);
                psOut.setString(5, invCode);
                psOut.setString(6, destination);
                psOut.setString(7, status);
                psOut.executeUpdate();

            String insertOut = "insert into gudang.outgoing_goods_detail " +
                            "(id_outgoods_detail, id_outgoods, id_product, quantity) " +
                            "VALUES (?,?,?,?)";

            PreparedStatement psOutDetail = conn.prepareStatement(insertOut);
            psOutDetail.setInt(1, idOut);
            psOutDetail.setInt(2, idOut);
            psOutDetail.setInt(3, idBarang);
            psOutDetail.setInt(4, jumlah);
            psOutDetail.executeUpdate();
                
            

            int idShipment = getNextShipmentId();
            int idShipmentDetail = getNextShipmentDetailId();
            
            String insertShipment = "INSERT INTO gudang.shipment "
                    + "(id_shipment, id_warehouse, destination, shipment_date, status) "
                    + "VALUES (?, ?, ?, current_timestamp, ?)";
            try (PreparedStatement psShipment = conn.prepareStatement(insertShipment)) {
                psShipment.setInt(1, idShipment);
                psShipment.setInt(2, idWare);
                psShipment.setString(3, destination);
                psShipment.setString(4, status);
                psShipment.executeUpdate();
            }

            String insertShipmentDetail = "INSERT INTO gudang.shipment_details "
                    + "(id_shipment_details, id_shipment, id_product, quantity) "
                    + "VALUES (?, ?, ?, ?)";
            try (PreparedStatement psShipmentDetail = conn.prepareStatement(insertShipmentDetail)) {
                psShipmentDetail.setInt(1, idShipmentDetail);
                psShipmentDetail.setInt(2, idShipment);
                psShipmentDetail.setInt(3, idBarang);
                psShipmentDetail.setInt(4, jumlah);
                psShipmentDetail.executeUpdate();
            }

            

            conn.commit();
            System.out.println("""
                    
                    [SUCCESS] Barang Keluar berhasil dibuat.
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

    public static void buatDistribusi() {
        Connection conn = koneksi.connect();
        Scanner input = new Scanner(System.in);
        
        try {
            String print = """
                    SELECT
                        s.id_shipment,
                        w.name AS warehouse,
                        s.destination,
                        p.name AS product,
                        sd.quantity,
                        s.status,
                        s.shipment_date
                    FROM gudang.shipment s
                    JOIN gudang.warehouse w ON s.id_warehouse = w.id_warehouse
                    JOIN gudang.shipment_details sd ON s.id_shipment = sd.id_shipment
                    JOIN gudang.product p ON sd.id_product = p.id_product;
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
            View.printTable(conn, "gudang", "distribution");
            

            System.out.println("""
                    
                    ============== BUAT DISTRIBUSI ==============
                    """);

            System.out.print("No Distribusi : ");
            int noDistribusi = input.nextInt();
            input.nextLine();

            System.out.print("Kode Distribusi :");
            String kodeDistribusi = input.nextLine();

            System.out.print("ID Warehouse Asal : ");
            int warehouseAsal = Integer.parseInt(input.nextLine());

            System.out.print("ID Warehouse Tujuan : ");
            int warehouseTujuan = Integer.parseInt(input.nextLine());

            System.out.print("ID Barang : ");
            int idBarang = Integer.parseInt(input.nextLine());

            System.out.print("Jumlah : ");
            int jumlah = Integer.parseInt(input.nextLine());

            String cekStok = """
                    SELECT id_stock, quantity
                    FROM gudang.warehouse_stocks
                    WHERE id_warehouse = ?
                    AND id_product = ?
                    """;

            PreparedStatement psCek = conn.prepareStatement(cekStok);
            psCek.setInt(1, warehouseAsal);
            psCek.setInt(2, idBarang);
            ResultSet rs = psCek.executeQuery();

            if (!rs.next()) {
                System.out.println("Barang tidak ditemukan di warehouse asal.");
                conn.rollback();
                return;
            }

            int sourceStockId = rs.getInt("id_stock");
            int stokSekarang = rs.getInt("quantity");

            if (stokSekarang < jumlah) {
                System.out.println("Stok tidak mencukupi.");
                conn.rollback();
                return;
            }

            String updateAsal = """
                    UPDATE gudang.warehouse_stocks
                    SET quantity = quantity - ?, updated_at = current_timestamp
                    WHERE id_stock = ?
                    """;

            try (PreparedStatement psAsal = conn.prepareStatement(updateAsal)) {
                psAsal.setInt(1, jumlah);
                psAsal.setInt(2, sourceStockId);
                psAsal.executeUpdate();
            }

            String cekTujuan = """
                    SELECT id_stock, quantity
                    FROM gudang.warehouse_stocks
                    WHERE id_warehouse = ?
                    AND id_product = ?
                    """;

            try (PreparedStatement psTujuan = conn.prepareStatement(cekTujuan)) {
                psTujuan.setInt(1, warehouseTujuan);
                psTujuan.setInt(2, idBarang);
                ResultSet rsTujuan = psTujuan.executeQuery();

                if (rsTujuan.next()) {
                    int destStockId = rsTujuan.getInt("id_stock");
                    String tambahStok = """
                            UPDATE gudang.warehouse_stocks
                            SET quantity = quantity + ?, updated_at = current_timestamp
                            WHERE id_stock = ?
                            """;
                    try (PreparedStatement psTambah = conn.prepareStatement(tambahStok)) {
                        psTambah.setInt(1, jumlah);
                        psTambah.setInt(2, destStockId);
                        psTambah.executeUpdate();
                    }
                } else {
                    int nextId = getNextStockId();
                    String insertStok = """
                            INSERT INTO gudang.warehouse_stocks
                            (id_stock, id_product, id_warehouse, quantity)
                            VALUES (?, ?, ?, ?)
                            """;
                    try (PreparedStatement psInsert = conn.prepareStatement(insertStok)) {
                        psInsert.setInt(1, nextId);
                        psInsert.setInt(2, idBarang);
                        psInsert.setInt(3, warehouseTujuan);
                        psInsert.setInt(4, jumlah);
                        psInsert.executeUpdate();
                    }
                }
            }

            int idShipment = getNextShipmentId();
            int idShipmentDetail = getNextShipmentDetailId();
            String destination = "Gudang " + warehouseTujuan;

            String insertShipment = "INSERT INTO gudang.shipment "
                    + "(id_shipment, id_warehouse, destination, shipment_date, status) "
                    + "VALUES (?, ?, ?, current_timestamp, ?)";
            try (PreparedStatement psShipment = conn.prepareStatement(insertShipment)) {
                psShipment.setInt(1, idShipment);
                psShipment.setInt(2, warehouseAsal);
                psShipment.setString(3, destination);
                psShipment.setString(4, "Distribusi");
                psShipment.executeUpdate();
            }

            String insertShipmentDetail = "INSERT INTO gudang.shipment_details "
                    + "(id_shipment_details, id_shipment, id_product, quantity) "
                    + "VALUES (?, ?, ?, ?)";
            try (PreparedStatement psShipmentDetail = conn.prepareStatement(insertShipmentDetail)) {
                psShipmentDetail.setInt(1, idShipmentDetail);
                psShipmentDetail.setInt(2, idShipment);
                psShipmentDetail.setInt(3, idBarang);
                psShipmentDetail.setInt(4, jumlah);
                psShipmentDetail.executeUpdate();
            }

            String insertDistribution = "Insert into gudang.distribution (id_distribution, distribution_code, id_source_warehouse, id_destination_warehouse, id_product, quantity) values (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement psDistribution = conn.prepareStatement(insertDistribution)){
                psDistribution.setInt(1, noDistribusi);
                psDistribution.setString(2, kodeDistribusi);
                psDistribution.setInt(3, warehouseAsal);
                psDistribution.setInt(4, warehouseTujuan);
                psDistribution.setInt(5, idBarang);
                psDistribution.setInt(6, jumlah);
                psDistribution.executeUpdate();
            }


            conn.commit();
            System.out.println("""
                    
                    [SUCCESS] Distribusi berhasil dibuat.
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
    
    }

    
