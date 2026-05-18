package model;
import config.DatabaseConfig;
import java.sql.*;

public class Keuangan extends User {
   private int idJabatan;

    public Keuangan(int id, String nama, String email, int idJabatan) {
        super(id, nama, email);
        this.idJabatan = idJabatan;
    }

    @Override
    public boolean tampilkanMenu() {
        System.out.println("\n--- MENU DEPARTEMEN KEUANGAN ---");
        
        if (this.idJabatan == 3) { 
            System.out.println("1. BERIKAN GAJI (Penerbitan & Approval Otomatis)");
            System.out.println("2. Kelola Komponen Gaji");
            System.out.println("3. Lihat Seluruh Riwayat Penggajian");
            System.out.println("4. Lihat Pengumuman Bos");
            System.out.println("5. Logout");
        } else { 
            System.out.println("1. Lihat Riwayat Gaji Seluruh Karyawan");
            System.out.println("2. Update Komponen Gaji");
            System.out.println("3. Lihat Pengumuman Bos");
            System.out.println("4. Logout");
        }
        
        System.out.print("Pilih: ");
        String pil = scanner.nextLine();
        
        if (this.idJabatan == 3) {
            switch (pil) {
                case "1" -> berikanGaji();
                case "2" -> tambahKomponen();
                case "3" -> lihatSemuaSlip();
                case "4" -> lihatPengumuman();
                case "5" -> { return false; }
            }
        } else {
            switch (pil) {
                case "1" -> lihatSemuaSlip();
                case "2" -> tambahKomponen();
                case "3" -> lihatPengumuman();
                case "4" -> { return false; }
            }
        }
        return true;
    }
    private void tambahKomponen() {
        System.out.print("Nama Komponen: "); String nm = scanner.nextLine();
        System.out.print("Jenis (tunjangan/potongan): "); String jns = scanner.nextLine();
        System.out.print("Nominal: "); double nom = Double.parseDouble(scanner.nextLine());
        try (Connection conn = DatabaseConfig.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO kel7.komponen_gaji (nama_komponen, jenis, nominal) VALUES (?, ?, ?)");
            ps.setString(1, nm); ps.setString(2, jns); ps.setDouble(3, nom);
            ps.executeUpdate();
            System.out.println("Komponen Tersimpan!");
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void lihatSemuaSlip() {
    System.out.println("\n--- REKAP GAJI SELURUH KARYAWAN ---");
    try (Connection conn = config.DatabaseConfig.getConnection()) {
        String sql = "SELECT nama_lengkap, bulan, tahun, gaji_netto, status FROM kel7.v_slip_gaji";
        ResultSet rs = conn.createStatement().executeQuery(sql);
        
        System.out.printf("%-20s | %-10s | %-15s | %-10s\n", "Nama", "Periode", "Gaji Netto", "Status");
        System.out.println("----------------------------------------------------------------------");
        while(rs.next()) {
            System.out.printf("%-20s | %d/%d  | Rp %-13.0f | %-10s\n", 
                rs.getString(1), rs.getInt(2), rs.getInt(3), rs.getDouble(4), rs.getString(5));
        }
    } catch (Exception e) { e.printStackTrace(); }
}
    
   public void berikanGaji() {
    System.out.println("\n--- PENERBITAN GAJI & POTONGAN PAJAK PPh21 ---");
    System.out.print("ID Pegawai: "); int idPeg = Integer.parseInt(scanner.nextLine());
    System.out.print("Bulan (1-12): "); int bln = Integer.parseInt(scanner.nextLine());
    System.out.print("Tahun: "); int thn = Integer.parseInt(scanner.nextLine());

    try (Connection conn = config.DatabaseConfig.getConnection()) {
        PreparedStatement psG = conn.prepareStatement("SELECT gaji_pokok FROM kel7.pegawai WHERE id_pegawai = ?");
        psG.setInt(1, idPeg);
        ResultSet rs = psG.executeQuery();
        
        if (rs.next()) {
            double gapok = rs.getDouble("gaji_pokok");
            
            double totalTunjangan = 0;
            double totalPotonganLain = 0;
            ResultSet rsK = conn.createStatement().executeQuery("SELECT jenis, SUM(nominal) FROM kel7.komponen_gaji GROUP BY jenis");
            while(rsK.next()){
                if(rsK.getString(1).equalsIgnoreCase("tunjangan")) totalTunjangan = rsK.getDouble(2);
                else totalPotonganLain = rsK.getDouble(2);
            }

            double bruto = gapok + totalTunjangan;
            double pajak = bruto * 0.05; 
            double totalBersih = bruto - totalPotonganLain - pajak;

            String sql = "INSERT INTO kel7.payroll (id_pegawai, bulan, tahun, gaji_pokok, total_tunjangan, total_potongan, total_gaji, status) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, 'disetujui')";
            
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idPeg);
            ps.setInt(2, bln);
            ps.setInt(3, thn);
            ps.setDouble(4, gapok);
            ps.setDouble(5, totalTunjangan);
            ps.setDouble(6, totalPotonganLain + pajak);
            ps.setDouble(7, totalBersih);
            
            if (ps.executeUpdate() > 0) {
                System.out.println("-------------------------------------------");
                System.out.println("Gaji Bruto      : Rp " + bruto);
                System.out.println("Potongan Pajak  : Rp " + pajak);
                System.out.println("Diterima Pegawai: Rp " + totalBersih);
                System.out.println("-------------------------------------------");
                System.out.println("[SUKSES] Gaji disetujui otomatis oleh Manager Keuangan.");
            }
        } else {
            System.out.println("[ERROR] ID Pegawai tidak ditemukan.");
        }
    } catch (Exception e) {
        System.out.println("Kesalahan Sistem: " + e.getMessage());
        }
    }

    private void lihatPengumuman() {
        try (Connection conn = DatabaseConfig.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT judul, isi_pesan, tanggal_post FROM kel7.pengumuman ORDER BY tanggal_post DESC");
            System.out.println("\n--- INFORMASI KANTOR ---");
            while(rs.next()) System.out.println("[" + rs.getDate(3) + "] " + rs.getString(1) + ": " + rs.getString(2));
        } catch (Exception e) { e.printStackTrace(); }
    }
}