package Entitas;

import Koneksi.Koneksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class Dosen {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("=== DATA DOSEN ===\n");
        System.out.print("ID Program Studi: ");                                   String id_program_studi = input.nextLine().trim();
        System.out.print("NIDN: ");                                               String nidn = input.nextLine().trim();
        System.out.print("Nama: ");                                               String nama = input.nextLine().trim();
        System.out.print("Akun E-Mail: ");                                        String email = input.nextLine().trim();
        System.out.print("Jenis Kelamin (Laki-laki/Perempuan): ");                String jenis_kelamin = input.nextLine().trim();
        System.out.print("Nomor Handphone (kosongkan jika tidak ingin mengisi): "); String no_hp = input.nextLine();

        if (id_program_studi.isEmpty()
            || nidn.isEmpty()
            || nama.isEmpty()
            || email.isEmpty()
            || jenis_kelamin.isEmpty()) {

            System.out.println("Semua data wajib diisi, kecuali nomor handphone.");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            System.out.println("Format email tidak valid!");
            return;
        }

        if (!jenis_kelamin.equalsIgnoreCase("Laki-Laki")
            && !jenis_kelamin.equalsIgnoreCase("Perempuan")) {

            System.out.println("Jenis kelamin harus Laki-Laki atau Perempuan!");
            return;
        }

        if (!no_hp.isEmpty() && !no_hp.matches("\\d+")) {
            System.out.println("Nomor handphone hanya boleh berisi angka!");
            return;
        }

        try {
            Connection conn = Koneksi.connect();
            
            if (conn == null) {
                System.out.println("Koneksi gagal!");
                return;
            }
            
            String query =
            "INSERT INTO b1.dosen(id_program_studi, nidn, nama, email, jenis_kelamin, no_hp) " +
            "VALUES(?, ?, ?, ?, ?::jenis_kelamin_dosen_enum, ?)";
            
            PreparedStatement ps_dosen = conn.prepareStatement(query);
            ps_dosen.setString(1, id_program_studi); ps_dosen.setString(2, nidn);
            ps_dosen.setString(3, nama);             ps_dosen.setString(4, email);
            ps_dosen.setString(5, jenis_kelamin);    ps_dosen.setString(6, no_hp);

            if (no_hp.isEmpty()) {
                ps_dosen.setNull(6, java.sql.Types.VARCHAR);
            } else {
                ps_dosen.setString(6, no_hp);
            }

            int baris = ps_dosen.executeUpdate();

            if (baris > 0) {
                System.out.println("\n=== DATA DOSEN BERHASIL DISIMPAN ===");
                System.out.println("NIDN : " + nidn);
                System.out.println("Nama : " + nama);
            } else {
                System.out.println("Data dosen gagal disimpan.");
            }

        } catch (Exception e) {
            System.out.println("=== Terjadi kesalahan pada data! ===\n" + e.getMessage());
            e.printStackTrace();
        }
    }
}