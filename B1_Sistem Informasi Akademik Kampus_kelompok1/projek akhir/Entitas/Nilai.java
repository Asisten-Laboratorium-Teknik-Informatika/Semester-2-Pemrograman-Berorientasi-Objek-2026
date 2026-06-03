package Entitas;

import Fitur.Tampilan;
import Koneksi.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Nilai {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("=== DATA NILAI ===\n");
        System.out.print("ID Nilai: ");      String id_nilai = input.nextLine();
        System.out.print("ID Detail KRS: "); String id_detail_krs = input.nextLine();
        System.out.print("Nilai TUGAS: ");   String nilai_tugas = input.nextLine();
        System.out.print("Nilai UTS: ");     String nilai_uts = input.nextLine();
        System.out.print("Nilai UAS: ");     String nilai_uas = input.nextLine();

        try {
            Connection conn = Koneksi.connect();

            if (conn == null) {
                System.out.println("Koneksi gagal!");
                return;
            }

            String query =
            "INSERT INTO b1.nilai(id_nilai, id_detail_krs, nilai_tugas, nilai_uts, nilai_uas) " +
            "VALUES(?, ?, ?::numeric, ?::numeric, ?::numeric)";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, id_nilai);    ps.setString(2, id_detail_krs);
            ps.setString(3, nilai_tugas); ps.setString(4, nilai_uts);
            ps.setString(5, nilai_uas);
            
            if (ps.executeUpdate() > 0)
                System.out.println("===> Data berhasil ditambahkan! Silakan cek database.");

        } catch (Exception e) {
            System.out.println("=== Terjadi kesalahan pada data! ===\n: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===================== LIHAT SEMUA NILAI =====================
    public void lihatNilai() {
        try (Connection conn = Koneksi.connect()) {
            String sql = "SELECT id_nilai, nilai_akhir, predikat FROM b1.nilai";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            java.util.List<String[]> rows = new java.util.ArrayList<>();
            while (rs.next()) {
                rows.add(new String[]{
                    rs.getString("id_nilai"),
                    rs.getString("nilai_akhir"),
                    rs.getString("predikat")
                });
            }

            String[] headers = {"ID Nilai", "Nilai Akhir", "Predikat"};
            Tampilan.tampilTabel(headers, rows.toArray(new String[0][]));

        } catch (Exception e) {
            Tampilan.gagal("Gagal menampilkan data nilai!");
            e.printStackTrace();
        }
    }

    // ===================== LIHAT NILAI MAHASISWA =====================
    public void lihatNilaiMahasiswa(String nim) {
        String sql =
            "SELECT * FROM b1.view_khs " +
            "WHERE nim = ?";

        try (Connection conn = Koneksi.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nim);
            ResultSet rs = ps.executeQuery();

            java.util.List<String[]> rows = new java.util.ArrayList<>();
            while (rs.next()) {
                rows.add(new String[]{
                    rs.getString("nama_mata_kuliah"),
                    rs.getString("sks"),                    
                    rs.getString("nilai_tugas"),
                    rs.getString("nilai_uts"),
                    rs.getString("nilai_uas"),
                    rs.getString("nilai_akhir"),
                    rs.getString("predikat")
                });
            }

            System.out.println("\n=== NILAI SAYA ===");
            if (rows.isEmpty()) {
                Tampilan.peringatan("\nBelum ada nilai.");
            } else {
                String[] headers = {"Mata Kuliah", "Tugas", "UTS", "UAS", "Akhir", "Predikat"};
                Tampilan.tampilTabel(headers, rows.toArray(new String[0][]));
            }

        } catch (Exception e) {
            Tampilan.gagal("Gagal menampilkan nilai!");
            e.printStackTrace();
        }
    }

    // ===================== INPUT NILAI (untuk Dosen) =====================
    public void inputNilai(String nidn) {
        Scanner input = new Scanner(System.in);

        try (Connection conn = Koneksi.connect()) {
            String sqlKRS =
                "SELECT dk.id_detail_krs, m.nim, m.nama, mk.nama_mata_kuliah " +
                "FROM b1.detail_krs dk " +
                "JOIN b1.krs k           ON dk.id_krs = k.id_krs " +
                "JOIN b1.mahasiswa m     ON k.nim = m.nim " +
                "JOIN b1.kelas_kuliah kk ON dk.id_kelas_kuliah = kk.id_kelas_kuliah " +
                "JOIN b1.mata_kuliah mk  ON kk.id_mata_kuliah = mk.id_mata_kuliah " +
                "WHERE kk.nidn = ? AND k.status_persetujuan = 'Disetujui' " +
                "AND dk.id_detail_krs NOT IN (SELECT id_detail_krs FROM b1.nilai)";

            PreparedStatement ps1 = conn.prepareStatement(sqlKRS);
            ps1.setString(1, nidn);
            ResultSet rs = ps1.executeQuery();

            java.util.List<String[]> rows = new java.util.ArrayList<>();
            while (rs.next()) {
                rows.add(new String[]{
                    rs.getString("id_detail_krs"),
                    rs.getString("nim"),
                    rs.getString("nama_mata_kuliah")
                });
            }

            System.out.println("\n=== MAHASISWA BELUM ADA NILAI ===");
            if (rows.isEmpty()) {
                Tampilan.peringatan("\nSemua mahasiswa sudah memiliki nilai.");
                return;
            }

            String[] headers = {"ID Detail KRS", "NIM", "Mata Kuliah"};
            Tampilan.tampilTabel(headers, rows.toArray(new String[0][]));

            System.out.print("Masukkan ID Detail KRS: "); String idDetailKRS = input.nextLine().trim();
            System.out.print("Nilai Tugas (0-100): ");    String nilaiTugas = input.nextLine().trim();
            System.out.print("Nilai UTS (0-100): ");      String nilaiUTS = input.nextLine().trim();
            System.out.print("Nilai UAS (0-100): ");      String nilaiUAS = input.nextLine().trim();

            String idNilai = String.format("NL%08d", System.currentTimeMillis() % 100_000_000L);
            String sqlInsert =
                "INSERT INTO b1.nilai(id_nilai, id_detail_krs, nilai_tugas, nilai_uts, nilai_uas) " +
                "VALUES(?, ?, ?::numeric, ?::numeric, ?::numeric)";

            PreparedStatement ps2 = conn.prepareStatement(sqlInsert);
            ps2.setString(1, idNilai); ps2.setString(2, idDetailKRS);
            ps2.setString(3, nilaiTugas); ps2.setString(4, nilaiUTS);
            ps2.setString(5, nilaiUAS);
            ps2.executeUpdate();

            Tampilan.sukses("\nNilai berhasil disimpan! Nilai akhir dihitung otomatis dan bisa dilihat di menu KHS.");

        } catch (Exception e) {
            Tampilan.gagal("Gagal input nilai!");
            e.printStackTrace();
        }
    }

    // ===================== KHS MAHASISWA =====================
    public void lihatKHSMahasiswa(String nim, String idTahun) {
        String sql =
            "SELECT mk.nama_mata_kuliah, mk.sks, " +
            "n.nilai_akhir, n.predikat " +
            "FROM b1.nilai n " +
            "JOIN b1.detail_krs dk   ON n.id_detail_krs = dk.id_detail_krs " +
            "JOIN b1.krs k           ON dk.id_krs = k.id_krs " +
            "JOIN b1.kelas_kuliah kk ON dk.id_kelas_kuliah = kk.id_kelas_kuliah " +
            "JOIN b1.mata_kuliah mk  ON kk.id_mata_kuliah = mk.id_mata_kuliah " +
            "WHERE k.nim = ? " +
            "AND kk.id_tahun = ?";

        try (Connection conn = Koneksi.connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nim); ps.setString(2, idTahun);

            ResultSet rs = ps.executeQuery();

            java.util.List<String[]> rows = new java.util.ArrayList<>();

            int totalSKS = 0;
            double totalMutu = 0;

            while (rs.next()) {
                int sks = Integer.parseInt(rs.getString("sks"));
                String predikat = rs.getString("predikat");
                double bobot = getBobot(predikat);

                totalSKS += sks;
                totalMutu += bobot * sks;

                rows.add(new String[]{
                    rs.getString("nama_mata_kuliah"),
                    String.valueOf(sks),
                    rs.getString("nilai_akhir"),
                    predikat
                });
            }

            System.out.println("\n=== KARTU HASIL STUDI ===");

            if (rows.isEmpty()) {
                Tampilan.peringatan("\nBelum ada nilai pada semester ini.");
                return;
            }

            String[] headers = {"Mata Kuliah", "SKS", "Nilai Akhir", "Predikat"};
            Tampilan.tampilTabel(headers, rows.toArray(new String[0][]));

            double ips = 0;

            if (totalSKS > 0) {
                ips = totalMutu / totalSKS;
            }

            System.out.println("Total SKS : " + totalSKS);
            System.out.printf("IP Semester: %.2f%n", ips);

        } catch (Exception e) {
            Tampilan.gagal("Gagal menampilkan KHS!");
            e.printStackTrace();
        }
    }

    private double getBobot(String predikat) {
        switch (predikat) {
            case "A":   return 4.0;
            case "A-":  return 3.7;
            case "B+":  return 3.3;
            case "B":   return 3.0;
            case "B-":  return 2.7;
            case "C+":  return 2.3;
            case "C":   return 2.0;
            case "D":   return 1.0;
            default:    return 0.0;
        }
    }

    public void pilihKHSMahasiswa(String nim) {
    String sql =
        "SELECT DISTINCT ta.id_tahun, " +
        "ta.tahun, ta.semester_aktif " +
        "FROM b1.krs k " +
        "JOIN b1.detail_krs dk     ON k.id_krs = dk.id_krs " +
        "JOIN b1.kelas_kuliah kk   ON dk.id_kelas_kuliah = kk.id_kelas_kuliah " +
        "JOIN b1.tahun_akademik ta ON kk.id_tahun = ta.id_tahun " +
        "WHERE k.nim = ? " +
        "ORDER BY ta.tahun";

    try (Connection conn = Koneksi.connect();
        PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, nim);

        ResultSet rs = ps.executeQuery();

        java.util.List<String[]> semesterList = new java.util.ArrayList<>();

        int no = 1;

        System.out.println("\n=== PILIH SEMESTER KHS ===");

        while (rs.next()) {
            semesterList.add(new String[]{
                rs.getString("id_tahun"),
                rs.getString("tahun"),
                rs.getString("semester_aktif")
            });

            System.out.println(
                no + ". " +
                rs.getString("tahun") +
                " - " +
                rs.getString("semester_aktif")
            );

            no++;
        }

        if (semesterList.isEmpty()) {
            Tampilan.peringatan("\nBelum ada data semester.");
            return;
        }

        Scanner input = new Scanner(System.in);

        System.out.print("Pilih Semester: ");     int pilihan = Integer.parseInt(input.nextLine());

        if (pilihan < 1 || pilihan > semesterList.size()) {
            Tampilan.gagal("Pilihan tidak valid.");
            return;
        }

        String idTahun = semesterList.get(pilihan - 1)[0];

        lihatKHSMahasiswa(nim, idTahun);

    } catch (Exception e) {
        Tampilan.gagal("Gagal memuat semester KHS!");
        e.printStackTrace();
    }
}

    // ===================== TRANSKRIP NILAI =====================
    public void lihatTranskrip(String nim) {
        String sql =
            "SELECT mk.nama_mata_kuliah, mk.sks, " +
            "n.nilai_akhir, n.predikat " +
            "FROM b1.nilai n " +
            "JOIN b1.detail_krs dk ON n.id_detail_krs = dk.id_detail_krs " +
            "JOIN b1.krs k ON dk.id_krs = k.id_krs " +
            "JOIN b1.kelas_kuliah kk ON dk.id_kelas_kuliah = kk.id_kelas_kuliah " +
            "JOIN b1.mata_kuliah mk ON kk.id_mata_kuliah = mk.id_mata_kuliah " +
            "WHERE k.nim = ? " +
            "ORDER BY mk.nama_mata_kuliah";

        try (Connection conn = Koneksi.connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nim);

            ResultSet rs = ps.executeQuery();

            java.util.List<String[]> rows = new java.util.ArrayList<>();

            int totalSKS = 0;
            double totalMutu = 0;

            while (rs.next()) {
                int sks = Integer.parseInt(rs.getString("sks"));
                String predikat = rs.getString("predikat");

                double bobot = getBobot(predikat);

                totalSKS += sks;
                totalMutu += (bobot * sks);

                rows.add(new String[]{
                    rs.getString("nama_mata_kuliah"),
                    String.valueOf(sks),
                    rs.getString("nilai_akhir"),
                    predikat
                });
            }

            System.out.println("\n=== TRANSKRIP NILAI ===");

            if (rows.isEmpty()) {
                Tampilan.peringatan("\nBelum ada data nilai.");
                return;
            }

            String[] headers = {"Mata Kuliah", "SKS", "Nilai Akhir", "Predikat"};
            Tampilan.tampilTabel(headers, rows.toArray(new String[0][]));

            double ipk = 0;

            if (totalSKS > 0) {
                ipk = totalMutu / totalSKS;
            }

            System.out.println("Total SKS : " + totalSKS);
            System.out.printf("IPK       : %.2f%n", ipk);

        } catch (Exception e) {
            Tampilan.gagal("Gagal menampilkan transkrip!");
            e.printStackTrace();
        }
    }
}