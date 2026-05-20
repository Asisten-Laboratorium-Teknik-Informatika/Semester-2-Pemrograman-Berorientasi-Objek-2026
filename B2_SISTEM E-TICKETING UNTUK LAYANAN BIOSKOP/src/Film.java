import java.sql.*;
import java.util.Calendar;
import java.util.Scanner;

public class Film {

    // ======================================================
    // LIST FILM BOOKING
    // ======================================================

    public void listFilmBooking() {

        try {

            Connection c =
                Koneksi.connect();

            Statement s =
                c.createStatement();
            Statement sCek = c.createStatement();
            ResultSet rsCek = sCek.executeQuery("SELECT COUNT(*) AS total FROM film WHERE is_tayang = true");
            
            int totalManual = 0;
            if (rsCek.next()) totalManual = rsCek.getInt("total");

            ResultSet rs;
            if (totalManual > 0) {
                // Menampilkan film yang diatur manual oleh Admin
                rs = s.executeQuery("SELECT * FROM film WHERE is_tayang = true ORDER BY urutan ASC, id_film ASC");
            } else {
                // Rotasi Otomatis (Berganti 10 film setiap minggu kalender)
                int week = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
                
                Statement sTotal = c.createStatement();
                ResultSet rsTotal = sTotal.executeQuery("SELECT COUNT(*) AS total_semua FROM film");
                int totalFilm = 1;
                if (rsTotal.next()) totalFilm = rsTotal.getInt("total_semua");
                
                int maxMinggu = Math.max(1, (int) Math.ceil((double) totalFilm / 10));
                int offset = (week % maxMinggu) * 10;
                
                PreparedStatement ps = c.prepareStatement("SELECT * FROM film ORDER BY id_film ASC LIMIT 10 OFFSET ?");
                ps.setInt(1, offset);
                rs = ps.executeQuery();
            }

            System.out.println(UI.YELLOW);

            System.out.println(
                "\n==============================================================================================="
            );

            System.out.println(
                "                                          DAFTAR FILM"
            );

            System.out.println(
                "==============================================================================================="
            );

            System.out.println(UI.RESET);

            System.out.printf(
                "%-5s %-50s %-25s %-10s\n",
                "ID",
                "JUDUL FILM",
                "GENRE",
                "DURASI"
            );

            System.out.println(
                "-----------------------------------------------------------------------------------------------"
            );

            while (rs.next()) {

                String judul = rs.getString("judul");
                if (rs.getBoolean("is_imax")) {
                    judul += " [IMAX]";
                }

                System.out.printf(
                    "%-5d %-50s %-25s %-10s\n",
                    rs.getInt("id_film"),
                    judul,
                    rs.getString("genre"),
                    rs.getString("durasi") + " Menit"
                );
            }

            System.out.println(
                "==============================================================================================="
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // ======================================================
    // MENU DAFTAR FILM & TRAILER
    // ======================================================

    public void menuDaftarFilm() {
        Scanner input = new Scanner(System.in);
        
        while (true) {
            listFilmBooking();
            
            System.out.println("\nOpsi:");
            System.out.println("1. Putar Trailer Film");
            System.out.println("2. Kembali");
            System.out.print("\nPilih opsi : ");
            
            int opsi = input.nextInt();
            input.nextLine();
            
            if (opsi == 1) {
                System.out.print("Masukkan ID Film untuk melihat trailer : ");
                int id = input.nextInt();
                input.nextLine();
                
                try {
                    Connection c = Koneksi.connect();
                    PreparedStatement ps = c.prepareStatement("SELECT judul FROM film WHERE id_film = ?");
                    ps.setInt(1, id);
                    ResultSet rs = ps.executeQuery();
                    
                    if (rs.next()) {
                        String judul = rs.getString("judul");
                        
                        String baseDir = System.getProperty("user.dir");
                        String sep = java.io.File.separator;
                        
                        String[] kemungkinanPath = {
                            baseDir + sep + "trailer" + sep + judul + ".mp4",
                            baseDir + sep + "trailer" + sep + judul + ".mp4",
                            baseDir + sep + judul + ".mp4",
                            baseDir + sep + judul + ".mp4"
                        };
                        
                        java.io.File fileFilm = null;
                        for (String path : kemungkinanPath) {
                            java.io.File temp = new java.io.File(path);
                            if (temp.exists()) {
                                fileFilm = temp;
                                break;
                            }
                        }
                        
                        if (fileFilm != null && java.awt.Desktop.isDesktopSupported()) {
                            System.out.println(UI.GREEN + "\nMemutar trailer: " + judul + "..." + UI.RESET);
                            java.awt.Desktop.getDesktop().open(fileFilm);
                        } else {
                            System.out.println(UI.RED + "\nFile trailer tidak ditemukan!" + UI.RESET);
                            System.out.println(UI.YELLOW + "Aplikasi telah mencari di lokasi berikut:" + UI.RESET);
                            for (String path : kemungkinanPath) {
                                System.out.println("- " + path);
                            }
                        }
                    } else {
                        System.out.println(UI.RED + "\nFilm dengan ID " + id + " tidak ditemukan." + UI.RESET);
                    }
                } catch (Exception e) {
                    System.out.println(UI.RED + "\nGagal memutar trailer." + UI.RESET);
                }
                System.out.print("\nTekan ENTER untuk melanjutkan...");
                input.nextLine();
            } else if (opsi == 2) {
                break;
            } else {
                UI.error("Opsi tidak tersedia!");
            }
        }
    }

    // ======================================================
    // ATUR FILM TAYANG (ADMIN)
    // ======================================================

    public void aturFilmTayang() {

        try {
            Connection c = Koneksi.connect();
            Scanner input = new Scanner(System.in);
            
            System.out.println(UI.CYAN + "\n=== ATUR FILM TAYANG MINGGU INI ===" + UI.RESET);
            System.out.println("1. Reset ke Mode Rotasi Otomatis (Gilir per Minggu)");
            System.out.println("2. Atur Film Manual");
            System.out.println("3. Acak Film Tayang");
            System.out.print("\nPilih opsi : ");
            
            int opsi = input.nextInt();
            
            if (opsi == 1) {
                Statement s = c.createStatement();
                s.executeUpdate("UPDATE film SET is_tayang = false, urutan = 0");
                System.out.println(UI.GREEN + "\nBerhasil! Film akan digilir otomatis setiap minggunya." + UI.RESET);
            } else if (opsi == 2) {
                Statement s = c.createStatement();
                s.executeUpdate("UPDATE film SET is_tayang = false, urutan = 0");
                
                System.out.print("\nMasukkan jumlah film yang ingin ditayangkan: ");
                int jumlah = input.nextInt();
                
                for (int i = 1; i <= jumlah; i++) {
                    System.out.print("Masukkan ID Film ke-" + i + " : ");
                    int id = input.nextInt();
                    
                    PreparedStatement ps = c.prepareStatement("UPDATE film SET is_tayang = true, urutan = ? WHERE id_film = ?");
                    ps.setInt(1, i);
                    ps.setInt(2, id);
                    int updated = ps.executeUpdate();
                    if(updated == 0) {
                        System.out.println(UI.RED + "Peringatan: ID Film " + id + " tidak ditemukan!" + UI.RESET);
                    }
                }
                System.out.println(UI.GREEN + "\nBerhasil mengatur film tayang secara manual!" + UI.RESET);
            }
            else if (opsi == 3) {
                Statement s = c.createStatement();
                s.executeUpdate("UPDATE film SET is_tayang = false, urutan = 0");
                
                System.out.print("\nMasukkan jumlah film yang ingin diacak: ");
                int jumlah = input.nextInt();
                
                // Ambil ID secara acak terlebih dahulu
                PreparedStatement psGet = c.prepareStatement("SELECT id_film FROM film ORDER BY RANDOM() LIMIT ?");
                psGet.setInt(1, jumlah);
                ResultSet rs = psGet.executeQuery();
                
                int urutan = 1;
                int diupdate = 0;
                
                // Simpan urutan acaknya satu per satu ke database
                while (rs.next()) {
                    int id = rs.getInt("id_film");
                    PreparedStatement psUpdate = c.prepareStatement("UPDATE film SET is_tayang = true, urutan = ? WHERE id_film = ?");
                    psUpdate.setInt(1, urutan);
                    psUpdate.setInt(2, id);
                    psUpdate.executeUpdate();
                    urutan++;
                    diupdate++;
                }
                System.out.println(UI.GREEN + "\nBerhasil mengacak " + diupdate + " film tayang!" + UI.RESET);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}