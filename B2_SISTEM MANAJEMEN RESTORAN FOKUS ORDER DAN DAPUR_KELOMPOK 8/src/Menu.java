import java.sql.*;

public class Menu {
    
    public static void lihatSemua() {
        String sql = "SELECT m.id, m.nama, m.harga, k.nama as kategori, m.stok " +
                     "FROM menu m LEFT JOIN kategori_menu k ON m.kategori_id = k.id " +
                     "WHERE m.tersedia = true ORDER BY k.id, m.id";
        try {
            Statement st = Database.getConn().createStatement();
            ResultSet rs = st.executeQuery(sql);
            System.out.println("\n============================================================");
            System.out.println("                   DAFTAR MENU");
            System.out.println("============================================================");
            System.out.println("ID  Nama                       Kategori        Harga    Stok");
            System.out.println("------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-3d %-25s %-12s Rp%-6d %d\n",
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getString("kategori"),
                    rs.getInt("harga"),
                    rs.getInt("stok"));
            }
            System.out.println("============================================================\n");
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error tampil menu: " + e.getMessage());
        }
    }
    
    // FITUR BARU: cari menu berdasarkan kategori
    public static void cariBerdasarkanKategori() {
        try {
            // Tampilkan daftar kategori dulu
            Statement st = Database.getConn().createStatement();
            ResultSet rsKategori = st.executeQuery("SELECT id, nama FROM kategori_menu ORDER BY id");
            
            System.out.println("\n============================================================");
            System.out.println("                   DAFTAR KATEGORI");
            System.out.println("============================================================");
            System.out.println("ID  Nama Kategori");
            System.out.println("------------------------------------------------------------");
            while (rsKategori.next()) {
                System.out.printf("%-3d %-25s\n", rsKategori.getInt("id"), rsKategori.getString("nama"));
            }
            System.out.println("============================================================");
            rsKategori.close();
            
            System.out.print("\nPilih ID Kategori: ");
            java.util.Scanner sc = new java.util.Scanner(System.in);
            int idKategori = sc.nextInt();
            
            String sql = "SELECT m.id, m.nama, m.harga, k.nama as kategori, m.stok " +
                         "FROM menu m LEFT JOIN kategori_menu k ON m.kategori_id = k.id " +
                         "WHERE m.tersedia = true AND m.kategori_id = " + idKategori + " ORDER BY m.id";
            
            ResultSet rs = st.executeQuery(sql);
            System.out.println("\n============================================================");
            System.out.println("                   MENU BERDASARKAN KATEGORI");
            System.out.println("============================================================");
            System.out.println("ID  Nama                       Kategori        Harga    Stok");
            System.out.println("------------------------------------------------------------");
            
            boolean ada = false;
            while (rs.next()) {
                ada = true;
                System.out.printf("%-3d %-25s %-12s Rp%-6d %d\n",
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getString("kategori"),
                    rs.getInt("harga"),
                    rs.getInt("stok"));
            }
            
            if (!ada) {
                System.out.println("  Tidak ada menu dalam kategori ini");
            }
            System.out.println("============================================================\n");
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error cari kategori: " + e.getMessage());
        }
    }
    
    public static int getHarga(int idMenu) {
        String sql = "SELECT harga FROM menu WHERE id = " + idMenu;
        try {
            Statement st = Database.getConn().createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                int h = rs.getInt("harga");
                rs.close();
                st.close();
                return h;
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error ambil harga: " + e.getMessage());
        }
        return 0;
    }
    
    public static String getNama(int idMenu) {
        String sql = "SELECT nama FROM menu WHERE id = " + idMenu;
        try {
            Statement st = Database.getConn().createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                String n = rs.getString("nama");
                rs.close();
                st.close();
                return n;
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error ambil nama: " + e.getMessage());
        }
        return "-";
    }
    
    public static boolean cekStok(int idMenu, int jumlah) {
        String sql = "SELECT stok FROM menu WHERE id = " + idMenu;
        try {
            Statement st = Database.getConn().createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                int stok = rs.getInt("stok");
                rs.close();
                st.close();
                return stok >= jumlah;
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error cek stok: " + e.getMessage());
        }
        return false;
    }
    
    public static void kurangiStok(int idMenu, int jumlah) {
        String sql = "UPDATE menu SET stok = stok - " + jumlah + " WHERE id = " + idMenu;
        try {
            Statement st = Database.getConn().createStatement();
            st.executeUpdate(sql);
            st.close();
        } catch (SQLException e) {
            System.out.println("Error update stok: " + e.getMessage());
        }
    }
    
    // Untuk tampilan menu saat memesan (opsional: bisa pilih mau liat semua atau cari kategori)
    public static void tampilkanMenuPilihan() {
        System.out.println("\n------------------------------------------------------------");
        System.out.println("1. Lihat semua menu");
        System.out.println("2. Cari menu berdasarkan kategori");
        System.out.print("Pilihan: ");
        
        java.util.Scanner sc = new java.util.Scanner(System.in);
        int pilih = sc.nextInt();
        
        if (pilih == 1) {
            lihatSemua();
        } else if (pilih == 2) {
            cariBerdasarkanKategori();
        } else {
            System.out.println("Pilihan tidak ada, menampilkan semua menu");
            lihatSemua();
        }
    }
}