package model;
import config.DatabaseConfig;
import java.sql.*;
import java.util.Scanner;

public class Pegawai extends User {
    public Pegawai(int id, String nama, String email) { super(id, nama, email); }

    @Override
    public boolean tampilkanMenu() {
        System.out.println("\n--- MENU PEGAWAI ---");
        System.out.println("1. Absen Masuk\n2. Ajukan Cuti\n3. Lihat Pengumuman\n4. Slip Gaji (Pajak 5%)\n5. Laporan kerja\n6. Logout");
        String p = scanner.nextLine();
        switch(p) {
            case "1" -> lakukanAbsen();
            case "2" -> ajukanCuti();
            case "3" -> lihatPengumuman();
            case "4" -> lihatSlip();
            case "5" -> buatLaporan();
            case "6" -> { return false; }
        }
        return true;
    }

    private void lakukanAbsen() {
        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "INSERT INTO kel7.absensi (id_pegawai, keterangan) VALUES ((SELECT id_pegawai FROM kel7.pegawai WHERE id_akun = ?), 'hadir')";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, this.idAkun); ps.executeUpdate();
            System.out.println("Absen Berhasil!");
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void ajukanCuti() {
        System.out.print("Jenis Cuti: "); String jns = scanner.nextLine();
        System.out.print("Tanggal Mulai (YYYY-MM-DD): "); String tgl = scanner.nextLine();
        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "INSERT INTO kel7.cuti (id_pegawai, jenis_cuti, tgl_mulai) VALUES ((SELECT id_pegawai FROM kel7.pegawai WHERE id_akun = ?), ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, this.idAkun); ps.setString(2, jns); ps.setDate(3, Date.valueOf(tgl));
            ps.executeUpdate();
            System.out.println("Cuti Diajukan!");
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void lihatPengumuman() {
        try (Connection conn = DatabaseConfig.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT judul, isi_pesan, tanggal_post FROM kel7.pengumuman ORDER BY tanggal_post DESC");
            System.out.println("\n--- INFORMASI KANTOR ---");
            while(rs.next()) System.out.println("[" + rs.getDate(3) + "] " + rs.getString(1) + ": " + rs.getString(2));
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void lihatSlip() {
    System.out.println("\n===========================================");
    System.out.println("          PENCARIAN SLIP GAJI              ");
    System.out.println("===========================================");
    System.out.print("M4asukkan Bulan (1-12): ");
    int bln = Integer.parseInt(scanner.nextLine());
    System.out.print("Masukkan Tahun       : ");
    int thn = Integer.parseInt(scanner.nextLine());

    String sqlGaji = "SELECT * FROM kel7.v_slip_gaji WHERE id_akun = ? AND bulan = ? AND tahun = ?";

    try (Connection conn = config.DatabaseConfig.getConnection();
         PreparedStatement psGaji = conn.prepareStatement(sqlGaji)) {
        
        psGaji.setInt(1, this.idAkun);
        psGaji.setInt(2, bln);
        psGaji.setInt(3, thn);
        
        try (ResultSet rs = psGaji.executeQuery()) {
            if (rs.next()) {
                double gapok = rs.getDouble("gaji_pokok");
                double totalTunjangan = rs.getDouble("total_tunjangan");
                double bruto = gapok + totalTunjangan;
                double pajak = rs.getDouble("pajak_pph21");
                double gajiNetto = rs.getDouble("gaji_netto");

                System.out.println("\n===========================================");
                System.out.println("           SLIP GAJI RESMI KARYAWAN        ");
                System.out.println("===========================================");
                System.out.println("Nama Karyawan : " + rs.getString("nama_lengkap"));
                System.out.println("Periode       : " + bln + " - " + thn);
                System.out.println("Status        : " + rs.getString("status").toUpperCase());
                System.out.println("-------------------------------------------");
                System.out.println("PENGHASILAN (+)");
                System.out.printf("  %-18s : Rp %,12.0f\n", "Gaji Pokok", gapok);
                
                String sqlKomp = "SELECT nama_komponen, nominal FROM kel7.komponen_gaji WHERE jenis = ?";
                try (PreparedStatement psKomp = conn.prepareStatement(sqlKomp)) {
                    psKomp.setString(1, "tunjangan");
                    try (ResultSet rsTunj = psKomp.executeQuery()) {
                        while (rsTunj.next()) {
                            System.out.printf("  %-18s : Rp %,12.0f\n", rsTunj.getString("nama_komponen"), rsTunj.getDouble("nominal"));
                        }
                    }
                    
                    System.out.println("  ---------------------------------------");
                    System.out.printf("  %-18s : Rp %,12.0f\n", "TOTAL BRUTO", bruto);
                    
                    System.out.println("\nPOTONGAN (-)");
                    psKomp.setString(1, "potongan");
                    try (ResultSet rsPot = psKomp.executeQuery()) {
                        while (rsPot.next()) {
                            if (rsPot.getString("nama_komponen").equalsIgnoreCase("Pajak PPh21")) {
                                continue; 
                            }
                            System.out.printf("  %-18s : Rp %,12.0f\n", rsPot.getString("nama_komponen"), rsPot.getDouble("nominal"));
                        }
                    }
                }
                
                System.out.printf("  %-18s : Rp %,12.0f\n", "Pajak PPh21 (5%)", pajak);
                System.out.println("-------------------------------------------");
                System.out.printf("GAJI BERSIH (NET)    : Rp %,12.0f\n", gajiNetto);
                System.out.println("===========================================\n");
                
            } else {
                System.out.println("\n[INFO] Slip gaji untuk periode " + bln + "-" + thn + " tidak ditemukan.");
                System.out.println("Pastikan Manager Keuangan telah memproses penggajian bulan ini.");
            }
        }
    } catch (Exception e) {
        System.out.println("[ERROR] Gagal memuat data slip gaji: " + e.getMessage());
        e.printStackTrace();
    }
}

public static void registrasiMandiri() {
    try (Scanner sc = new Scanner(System.in)) {
        System.out.println("\n--- PENDAFTARAN PEGAWAI BARU ---");
        System.out.print("Nama Lengkap : "); String nama = sc.nextLine();
        System.out.print("Email        : "); String email = sc.nextLine();
        System.out.print("Password     : "); String pass = sc.nextLine();
        System.out.print("Masukkan NIK    : "); String nik = sc.nextLine();
        
        System.out.println("Pilih Departemen (1: Keuangan, 2: IT, 3: HR): ");
        int idDept = Integer.parseInt(sc.nextLine());

        try (Connection conn = config.DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);

            String sqlAkun = "INSERT INTO kel7.user_akun (nama, email, password, role, is_approved) VALUES (?, ?, ?, 'pegawai', FALSE) RETURNING id_akun";
            PreparedStatement psAkun = conn.prepareStatement(sqlAkun);
            psAkun.setString(1, nama);
            psAkun.setString(2, email);
            psAkun.setString(3, pass);
            ResultSet rs = psAkun.executeQuery();

            if (rs.next()) {
                int idBaru = rs.getInt(1);

                String sqlPegawai = "INSERT INTO kel7.pegawai (id_akun, nama_lengkap, id_departemen, id_jabatan, gaji_pokok, nik) " +
                                     "VALUES (?, ?, ?, 1, (SELECT gaji_dasar FROM kel7.jabatan WHERE id_jabatan = 1), ?)";
                PreparedStatement psPeg = conn.prepareStatement(sqlPegawai);
                psPeg.setInt(1, idBaru);
                psPeg.setString(2, nama);
                psPeg.setInt(3, idDept);
                psPeg.setString(4, nik);
                psPeg.executeUpdate();

                conn.commit();
                System.out.println("\n[SUKSES] Akun terdaftar sebagai Junior Staff. Menunggu approval Operator.");
            }
        } catch (Exception e) {
            System.out.println("Gagal Registrasi: " + e.getMessage());
            }
         }
    }

        public void buatLaporan() {
        System.out.print("Deskripsi Pekerjaan Hari Ini: ");
        String desc = scanner.nextLine();
        try (Connection conn = config.DatabaseConfig.getConnection()) {
            String sql = "INSERT INTO kel7.laporan_kerja (id_pegawai, deskripsi_pekerjaan) " +
                        "VALUES ((SELECT id_pegawai FROM kel7.pegawai WHERE id_akun = ?), ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, this.idAkun);
            ps.setString(2, desc);
            ps.executeUpdate();
            System.out.println("Laporan kerja berhasil dikirim!");
        } catch (Exception e) { e.printStackTrace(); }
        }
}