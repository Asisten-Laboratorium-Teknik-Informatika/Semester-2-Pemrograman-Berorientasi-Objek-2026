package model;

import config.DatabaseConfig;
import java.sql.*;
import java.time.LocalTime;
import java.time.Duration;
import java.util.Scanner;

public class Pegawai extends User {
    public Pegawai(int id, String nama, String email) { super(id, nama, email); }

    @Override
    public boolean tampilkanMenu() {
        int idDept = 0;

        String sqlCekDept = "SELECT id_departemen FROM kel7.pegawai WHERE id_akun = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlCekDept)) {
            ps.setInt(1, this.idAkun);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    idDept = rs.getInt("id_departemen");
                }
            }
        } catch (SQLException e) {
            System.out.println("Gagal memuat departemen: " + e.getMessage());
        }
        
        if (idDept == 3) { // DEPARTEMEN HRD
            System.out.println("\n--- MENU PEGAWAI HRD ---");
            System.out.println("1. Absen Masuk & Keluar");
            System.out.println("2. Ajukan Cuti");
            System.out.println("3. Lihat Pengumuman");
            System.out.println("4. Slip Gaji (Pajak 5%)");
            System.out.println("5. Laporan kerja");
            System.out.println("6. Statistik Pegawai Per Departemen");
            System.out.println("7. Pantau Pengajuan Cuti Karyawan");
            System.out.println("8. Logout");
        } else if (idDept == 2) {
            System.out.println("\n--- MENU PEGAWAI IT ---");
            System.out.println("1. Absen Masuk & Keluar");
            System.out.println("2. Ajukan Cuti");
            System.out.println("3. Lihat Pengumuman");
            System.out.println("4. Slip Gaji (Pajak 5%)");
            System.out.println("5. Laporan kerja");
            System.out.println("6. Lihat Log Laporan Kerja Semua Karyawan");
            System.out.println("7. Logout");
        } else { 
            System.out.println("6. Logout");
        }
        
        System.out.print("Pilih opsi: ");
        String p = scanner.nextLine();
        
        if (idDept == 3) { 
            switch(p) {
                case "1" -> lakukanAbsen();
                case "2" -> ajukanCuti();
                case "3" -> lihatPengumuman();
                case "4" -> lihatSlipGaji();
                case "5" -> buatLaporan();
                case "6" -> tampilkanPegawaiPerDepartemen(); 
                case "7" -> lihatDaftarCutiKaryawan();       
                case "8" -> { return false; }
                default  -> System.out.println("Pilihan tidak valid.");
            }
        } else if (idDept == 2) { 
            switch(p) {
                case "1" -> lakukanAbsen();
                case "2" -> ajukanCuti();
                case "3" -> lihatPengumuman();
                case "4" -> lihatSlipGaji();
                case "5" -> buatLaporan();
                case "6" -> lihatLaporan();
                case "7" -> { return false; }
                default  -> System.out.println("Pilihan tidak valid.");
            }
        } else { 
            switch(p) {
                case "1" -> lakukanAbsen();
                case "2" -> ajukanCuti();
                case "3" -> lihatPengumuman();
                case "4" -> lihatSlipGaji();
                case "5" -> buatLaporan();
                case "6" -> { return false; }
                default  -> System.out.println("Pilihan tidak valid.");
            }
        }
        return true;
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

    private void tampilkanPegawaiPerDepartemen() {
        System.out.println("\n--- STATISTIK DISTRIBUSI PEGAWAI KANTOR ---");
        String sql = "SELECT id_departemen, COUNT(*) as total FROM kel7.pegawai GROUP BY id_departemen ORDER BY id_departemen";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            System.out.printf("%-25s | %-15s\n", "Nama Departemen", "Total Staff");
            System.out.println("---------------------------------------------------");
            while (rs.next()) {
                int idDept = rs.getInt("id_departemen");
                int total = rs.getInt("total");
                String namaDept = switch (idDept) {
                    case 1 -> "Keuangan";
                    case 2 -> "IT (Information Tech)";
                    case 3 -> "HR (Human Resource)";
                    default -> "Departemen Lain";
                };
                System.out.printf("%-25s | %-15d pegawai\n", namaDept, total);
            }
            System.out.println("---------------------------------------------------");
        } catch (SQLException e) { System.out.println("Error statistik: " + e.getMessage()); }
    }

    private void lihatDaftarCutiKaryawan() {
        System.out.println("\n--- REKAP PENGAJUAN CUTI SELURUH KARYAWAN ---");
        String sql = "SELECT c.id_cuti, p.nama_lengkap, c.jenis_cuti, c.tgl_mulai " +
                     "FROM kel7.cuti c JOIN kel7.pegawai p ON c.id_pegawai = p.id_pegawai ORDER BY c.tgl_mulai DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            boolean adaCuti = false;
            while (rs.next()) {
                adaCuti = true;
                System.out.printf("ID Rekap: %d | Karyawan: %-20s | Alasan: %-12s | Tanggal: %s\n",
                        rs.getInt("id_cuti"), rs.getString("nama_lengkap"), rs.getString("jenis_cuti"), rs.getDate("tgl_mulai"));
            }
            if (!adaCuti) System.out.println("Tidak ada rekam pengajuan cuti.");
        } catch (SQLException e) { System.out.println("Error data cuti: " + e.getMessage()); }
    }

    private void lihatLaporan() {
    System.out.println("\n--- LAPORAN KERJA PEGAWAI ---");
    try (Connection conn = config.DatabaseConfig.getConnection()) {
        String sql = "SELECT p.nama_lengkap, l.tanggal, l.deskripsi_pekerjaan FROM kel7.laporan_kerja l " +
                     "JOIN kel7.pegawai p ON l.id_pegawai = p.id_pegawai";
        ResultSet rs = conn.createStatement().executeQuery(sql);
        while(rs.next()) {
            System.out.printf("[%s] %s: %s\n", rs.getDate(2), rs.getString(1), rs.getString(3));
        }
        } catch (Exception e) { e.printStackTrace(); }
    }
}