package model;
import config.DatabaseConfig;
import java.sql.*;
import java.time.LocalTime;
import java.time.Duration;

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
            System.out.println("1. Absen Masuk & Keluar");
            System.out.println("2. BERIKAN GAJI (Penerbitan & Approval Otomatis)");
            System.out.println("3. Kelola Komponen Gaji");
            System.out.println("4. Lihat Slip Gaji");
            System.out.println("5. Lihat Seluruh Riwayat Penggajian");
            System.out.println("6. Lihat Pengumuman Bos");
            System.out.println("7. Ajukan Cuti");
            System.out.println("8. Logout");
        } else { 
            System.out.println("1. Absen Masuk & Keluar");
            System.out.println("2. Lihat Riwayat Gaji Seluruh Karyawan");
            System.out.println("3. Update Komponen Gaji");
            System.out.println("4. Lihat Slip Gaji");
            System.out.println("5. Lihat Pengumuman Bos");
            System.out.println("6. Ajukan Cuti");
            System.out.println("7. Logout");
        }
        
        System.out.print("Pilih: ");
        String pil = scanner.nextLine();
        
        if (this.idJabatan == 3) {
            switch (pil) {
                case "1" -> lakukanAbsen();
                case "2" -> berikanGaji();
                case "3" -> tambahKomponen();
                case "4" -> lihatSlipGaji();
                case "5" -> lihatSemuaSlip();
                case "6" -> lihatPengumuman();
                case "7" -> ajukanCuti();
                case "8" -> { return false; }
            }
        } else {
            switch (pil) {
                case "1" -> lakukanAbsen();
                case "2" -> lihatSemuaSlip();
                case "3" -> tambahKomponen();
                case "4" -> lihatSlipGaji();
                case "5" -> lihatPengumuman();
                case "6" -> ajukanCuti();
                case "7" -> { return false; }
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

    public void lihatSemuaSlip() {
    System.out.println("\n--- MONITORING SLIP GAJI SELURUH KARYAWAN ---");
    System.out.print("Masukkan Periode Bulan (1-12): ");
    int bulanCari = Integer.parseInt(scanner.nextLine());
    System.out.print("Masukkan Periode Tahun       : ");
    int tahunCari = Integer.parseInt(scanner.nextLine());

    String sqlAmbilView = "SELECT * FROM kel7.v_slip_gaji WHERE bulan = ? AND tahun = ? ORDER BY nama_lengkap ASC";
    String sqlKomponen = "SELECT nama_komponen, nominal FROM kel7.komponen_gaji";

    try (Connection conn = config.DatabaseConfig.getConnection()) {
        double tunjMakanMaster = 0;
        double tunjTransportMaster = 0;
        double asuransiMaster = 0;
        
        try (PreparedStatement psK = conn.prepareStatement(sqlKomponen);
             ResultSet rsK = psK.executeQuery()) {
            while (rsK.next()) {
                String nama = rsK.getString("nama_komponen");
                double nom = rsK.getDouble("nominal");
                if (nama.equalsIgnoreCase("Tunjangan Makan")) tunjMakanMaster = nom;
                else if (nama.equalsIgnoreCase("Tunjangan Transport")) tunjTransportMaster = nom;
                else if (nama.equalsIgnoreCase("Asuransi Kesehatan")) asuransiMaster = nom;
            }
        }

        try (PreparedStatement ps = conn.prepareStatement(sqlAmbilView)) {
            ps.setInt(1, bulanCari);
            ps.setInt(2, tahunCari);

            try (ResultSet rs = ps.executeQuery()) {
                boolean adaData = false;

                while (rs.next()) {
                    adaData = true;
                    
                    double gajiPokok = rs.getDouble("gaji_pokok");
                    double totalTunjangan = rs.getDouble("total_tunjangan");
                    double potonganLain = rs.getDouble("potongan_lain");
                    double pajakPph21 = rs.getDouble("pajak_pph21");
                    double gajiNetto = rs.getDouble("gaji_netto");
                    String status = rs.getString("status");
                    String namaLengkap = rs.getString("nama_lengkap");

                    double uangLembur = totalTunjangan - (tunjMakanMaster + tunjTransportMaster);
                    if (uangLembur < 0) uangLembur = 0;

                    double dendaTerlambat = potonganLain - asuransiMaster;
                    if (dendaTerlambat < 0) dendaTerlambat = 0;
                    
                    double totalPotongan = pajakPph21 + potonganLain;

                    System.out.println("\n=======================================================");
                    System.out.println("               SLIP GAJI RESMI PERUSAHAAN              ");
                    System.out.println("=======================================================");
                    System.out.printf("Nama Karyawan        : %-30s\n", namaLengkap);
                    System.out.printf("Periode Penggajian   : Bulan %d / Tahun %d\n", bulanCari, tahunCari);
                    System.out.printf("Status Verifikasi    : [%s]\n", status);
                    System.out.println("=======================================================");
                    System.out.printf("  Gaji Pokok Struktural          : Rp %,15.2f\n", gajiPokok);
                    System.out.println("-------------------------------------------------------");
                    System.out.println("  RINCIAN TUNJANGAN (PENDAPATAN):");
                    System.out.printf("  [+] Tunjangan Makan            : Rp %,15.2f\n", tunjMakanMaster);
                    System.out.printf("  [+] Tunjangan Transport        : Rp %,15.2f\n", tunjTransportMaster);
                    System.out.printf("  [+] Tunjangan Lembur (Absensi) : Rp %,15.2f\n", uangLembur);
                    System.out.printf("  TOTAL ALL TUNJANGAN            : Rp %,15.2f\n", totalTunjangan);
                    System.out.println("-------------------------------------------------------");
                    System.out.println("  RINCIAN POTONGAN (PENGURANG):");
                    System.out.printf("  [-] Asuransi Kesehatan         : Rp %,15.2f\n", asuransiMaster);
                    System.out.printf("  [-] Denda Terlambat (Absensi)  : Rp %,15.2f\n", dendaTerlambat);
                    System.out.printf("  [-] Potongan Pajak PPh21 (5%%)  : Rp %,15.2f\n", pajakPph21);
                    System.out.printf("  TOTAL ALL POTONGAN             : Rp %,15.2f\n", totalPotongan);
                    System.out.println("-------------------------------------------------------");
                    System.out.printf(" TOTAL GAJI BERSIH DITERIMA(NETTO): Rp %,15.2f\n", gajiNetto);
                    System.out.println("=======================================================");
                }

                if (!adaData) {
                    System.out.println("\nInformasi: Belum ada data slip gaji yang diproses untuk periode tersebut.");
                }
            }
        }
    } catch (SQLException e) {
        System.out.println("Peringatan GAGAL: Terjadi kesalahan sistem Keuangan: " + e.getMessage());
    }
}
    
   public void berikanGaji() {
        System.out.println("\n--- PROSES EKSEKUSI PAYROLL BULANAN VIA POSTGRES FUNCTION ---");
        System.out.print("Masukkan ID Pegawai          : ");
        int idPegawaiTarget = Integer.parseInt(scanner.nextLine());
        System.out.print("Masukkan Periode Bulan (1-12): ");
        int bulanPeriode = Integer.parseInt(scanner.nextLine());
        System.out.print("Masukkan Periode Tahun       : ");
        int tahunPeriode = Integer.parseInt(scanner.nextLine());

        String sqlPanggilFunction = "SELECT kel7.sp_proses_payroll(?, ?, ?)";

        try (Connection conn = config.DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlPanggilFunction)) {
            
            ps.setInt(1, idPegawaiTarget);
            ps.setInt(2, bulanPeriode);
            ps.setInt(3, tahunPeriode);
            
            ps.execute();
            System.out.println("Sukses: Seluruh validasi hari kerja, kalkulasi denda, dan lembur berhasil diproses database.");
            System.out.println("Slip payroll bulanan berhasil diterbitkan resmi.");

        } catch (SQLException e) {
            System.out.println("\nPeringatan GAGAL: Eksekusi Payroll Dibatalkan!");
            System.out.println("Alasan: " + e.getMessage());
        }
    }

    private void lihatPengumuman() {
        try (Connection conn = DatabaseConfig.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT judul, isi_pesan, tanggal_post FROM kel7.pengumuman ORDER BY tanggal_post DESC");
            System.out.println("\n--- INFORMASI KANTOR ---");
            while(rs.next()) System.out.println("[" + rs.getDate(3) + "] " + rs.getString(1) + ": " + rs.getString(2));
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void lihatSlipGaji() {
        System.out.println("\n--- PENCARIAN SLIP GAJI BULANAN ---");
        System.out.print("Masukkan Periode Bulan (1-12): ");
        int bulanCari = Integer.parseInt(scanner.nextLine());
        System.out.print("Masukkan Periode Tahun       : ");
        int tahunCari = Integer.parseInt(scanner.nextLine());

        String sqlAmbilView = "SELECT * FROM kel7.v_slip_gaji WHERE id_akun = ? AND bulan = ? AND tahun = ?";
        String sqlKomponen = "SELECT nama_komponen, nominal FROM kel7.komponen_gaji";

        try (Connection conn = config.DatabaseConfig.getConnection()) {
            double tunjMakanMaster = 0;
            double tunjTransportMaster = 0;
            double asuransiMaster = 0;
            
            try (PreparedStatement psK = conn.prepareStatement(sqlKomponen);
                 ResultSet rsK = psK.executeQuery()) {
                while (rsK.next()) {
                    String nama = rsK.getString("nama_komponen");
                    double nom = rsK.getDouble("nominal");
                    if (nama.equalsIgnoreCase("Tunjangan Makan")) tunjMakanMaster = nom;
                    else if (nama.equalsIgnoreCase("Tunjangan Transport")) tunjTransportMaster = nom;
                    else if (nama.equalsIgnoreCase("Asuransi Kesehatan")) asuransiMaster = nom;
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlAmbilView)) {
                ps.setInt(1, this.idAkun); 
                ps.setInt(2, bulanCari);
                ps.setInt(3, tahunCari);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        double gajiPokok = rs.getDouble("gaji_pokok");
                        double totalTunjangan = rs.getDouble("total_tunjangan");
                        double potonganLain = rs.getDouble("potongan_lain");
                        double pajakPph21 = rs.getDouble("pajak_pph21");
                        double gajiNetto = rs.getDouble("gaji_netto");
                        String status = rs.getString("status");
                        String namaLengkap = rs.getString("nama_lengkap");

                        double uangLembur = totalTunjangan - (tunjMakanMaster + tunjTransportMaster);
                        if (uangLembur < 0) uangLembur = 0;

                        double dendaTerlambat = potonganLain - asuransiMaster;
                        if (dendaTerlambat < 0) dendaTerlambat = 0;
                        
                        double totalPotongan = pajakPph21 + potonganLain;

                        System.out.println("\n=======================================================");
                        System.out.println("               SLIP GAJI RESMI PERUSAHAAN              ");
                        System.out.println("=======================================================");
                        System.out.printf("Nama Karyawan        : %-30s\n", namaLengkap);
                        System.out.printf("Periode Penggajian   : Bulan %d / Tahun %d\n", bulanCari, tahunCari);
                        System.out.printf("Status Verifikasi    : [%s]\n", status);
                        System.out.println("=======================================================");
                        System.out.printf("  Gaji Pokok Struktural          : Rp %,15.2f\n", gajiPokok);
                        System.out.println("-------------------------------------------------------");
                        System.out.println("  RINCIAN TUNJANGAN (PENDAPATAN):");
                        System.out.printf("  [+] Tunjangan Makan            : Rp %,15.2f\n", tunjMakanMaster);
                        System.out.printf("  [+] Tunjangan Transport        : Rp %,15.2f\n", tunjTransportMaster);
                        System.out.printf("  [+] Tunjangan Lembur (Absensi) : Rp %,15.2f\n", uangLembur);
                        System.out.printf("  TOTAL ALL TUNJANGAN            : Rp %,15.2f\n", totalTunjangan);
                        System.out.println("-------------------------------------------------------");
                        System.out.println("  RINCIAN POTONGAN (PENGURANG):");
                        System.out.printf("  [-] Asuransi Kesehatan         : Rp %,15.2f\n", asuransiMaster);
                        System.out.printf("  [-] Denda Terlambat (Absensi)  : Rp %,15.2f\n", dendaTerlambat);
                        System.out.printf("  [-] Potongan Pajak PPh21 (5%%)  : Rp %,15.2f\n", pajakPph21);
                        System.out.printf("  TOTAL ALL POTONGAN             : Rp %,15.2f\n", totalPotongan);
                        System.out.println("-------------------------------------------------------");
                        System.out.printf(" TOTAL GAJI BERSIH DITERIMA(NETTO): Rp %,15.2f\n", gajiNetto);
                        System.out.println("=======================================================");
                    } else {
                        System.out.println("Informasi: Slip gaji periode tersebut belum diproses oleh divisi Keuangan.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Peringatan GAGAL: Terjadi kesalahan sistem: " + e.getMessage());
        }
    }

    private void lakukanAbsen() {
        LocalTime waktuSekarang = LocalTime.now();
        java.sql.Date tanggalHariIni = new java.sql.Date(System.currentTimeMillis());
        
        LocalTime batasMasuk = LocalTime.of(8, 0);       
        LocalTime jamPulangStandar = LocalTime.of(17, 0); 

        try (Connection conn = DatabaseConfig.getConnection()) {
            int idPegawaiReal = 0;
            String sqlCariID = "SELECT id_pegawai FROM kel7.pegawai WHERE id_akun = ?";
            try (PreparedStatement psCari = conn.prepareStatement(sqlCariID)) {
                psCari.setInt(1, this.idAkun);
                try (ResultSet rsCari = psCari.executeQuery()) {
                    if (rsCari.next()) {
                        idPegawaiReal = rsCari.getInt("id_pegawai");
                    } else {
                        System.out.println("[ERROR] ID Pegawai tidak ditemukan untuk akun ini.");
                        return;
                    }
                }
            }

            boolean sudahAbsenMasukHariIni = false;
            String sqlCekAbsen = "SELECT id_absensi FROM kel7.absensi WHERE id_pegawai = ? AND tanggal = ?";
            try (PreparedStatement psCek = conn.prepareStatement(sqlCekAbsen)) {
                psCek.setInt(1, idPegawaiReal);
                psCek.setDate(2, tanggalHariIni);
                try (ResultSet rsCek = psCek.executeQuery()) {
                    if (rsCek.next()) {
                        sudahAbsenMasukHariIni = true;
                    }
                }
            }

            if (!sudahAbsenMasukHariIni && waktuSekarang.isBefore(LocalTime.of(12, 0))) {
                System.out.println("\n--- MEMPROSES ABSEN MASUK ---");
                String statusKehadiran = "Tepat Waktu";
                double dendaTerlambat = 0;

                if (waktuSekarang.isAfter(batasMasuk)) {
                    statusKehadiran = "Terlambat";
                    dendaTerlambat = 100000; 
                    System.out.println("Peringatan: Anda TERLAMBAT! Denda Rp 100.000 otomatis dicatat.");
                } else {
                    System.out.println("Terima kasih, Anda absen masuk tepat waktu.");
                }

                String sqlMasuk = "INSERT INTO kel7.absensi (id_pegawai, tanggal, jam_masuk, status_kehadiran, denda_terlambat, keterangan) VALUES (?, ?, ?, ?, ?, 'hadir')";
                try (PreparedStatement psMasuk = conn.prepareStatement(sqlMasuk)) {
                    psMasuk.setInt(1, idPegawaiReal);
                    psMasuk.setDate(2, tanggalHariIni);
                    psMasuk.setTime(3, java.sql.Time.valueOf(waktuSekarang));
                    psMasuk.setString(4, statusKehadiran);
                    psMasuk.setDouble(5, dendaTerlambat);
                    psMasuk.executeUpdate();
                    System.out.println("[SUKSES] Absen masuk berhasil disimpan pada pukul " + waktuSekarang);
                }

            } else if (sudahAbsenMasukHariIni) {
                System.out.println("\n--- MEMPROSES ABSEN KELUAR ---");
                double uangLembur = 0;
                long jamLembur = 0;

                if (waktuSekarang.isAfter(jamPulangStandar)) {
                    Duration selisih = Duration.between(jamPulangStandar, waktuSekarang);
                    jamLembur = selisih.toHours(); 

                    if (jamLembur > 0) {
                        uangLembur = jamLembur * 25000; 
                        System.out.printf("Anda kerja lembur selama %d jam. Bonus Pendapatan: Rp %,.2f\n", jamLembur, uangLembur);
                    }
                } else {
                    System.out.println("Informasi: Anda pulang sebelum/tepat waktu standar (17:00). Lembur tidak dihitung.");
                }

                String sqlKeluar = "UPDATE kel7.absensi SET jam_keluar = ?, jam_lembur = ?, uang_lembur = ? WHERE id_pegawai = ? AND tanggal = ?";
                try (PreparedStatement psKeluar = conn.prepareStatement(sqlKeluar)) {
                    psKeluar.setTime(1, java.sql.Time.valueOf(waktuSekarang));
                    psKeluar.setLong(2, jamLembur);
                    psKeluar.setDouble(3, uangLembur);
                    psKeluar.setInt(4, idPegawaiReal);
                    psKeluar.setDate(5, tanggalHariIni);
                    psKeluar.executeUpdate();
                    System.out.println("[SUKSES] Absen keluar berhasil disimpan pada pukul " + waktuSekarang + ". Selamat beristirahat!");
                }
            } else {
                System.out.println("Peringatan: Anda tidak bisa melakukan absen keluar karena belum melakukan absen masuk pagi ini!");
            }

        } catch (Exception e) {
            System.out.println("Gagal memproses absensi: " + e.getMessage());
        }
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

}