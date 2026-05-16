package repository;

import config.Koneksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EventRepository {

    /*
     * =========================
     * GET ORGANIZER ID
     * =========================
     */
    public int getOrganizerId(
            int userId
    ) {

        String sql = """
            SELECT organizer_id
            FROM event_platform.organizers
            WHERE user_id = ?
        """;

        try (

                Connection conn =
                        Koneksi.getKoneksi();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(1, userId);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                return rs.getInt(
                        "organizer_id"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return 0;
    }

    /*
     * =========================
     * TAMPIL VENUE
     * =========================
     */
    public void tampilVenue() {

        String sql = """
            SELECT
                venue_id,
                venue_name,
                city,
                capacity
            FROM event_platform.venues
            ORDER BY venue_name ASC
        """;

        try (

                Connection conn =
                        Koneksi.getKoneksi();

                PreparedStatement ps =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()

        ) {

            System.out.println(
                    "\n===== DAFTAR VENUE ====="
            );

            while (rs.next()) {

                System.out.println(
                        "\nID Venue : "
                        + rs.getInt("venue_id")
                );

                System.out.println(
                        "Nama     : "
                        + rs.getString("venue_name")
                );

                System.out.println(
                        "Kota     : "
                        + rs.getString("city")
                );

                System.out.println(
                        "Kapasitas: "
                        + rs.getInt("capacity")
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /*
     * =========================
     * TAMBAH EVENT
     * =========================
     */
    public boolean tambahEvent(

            int organizerId,
            int venueId,
            String namaEvent,
            String deskripsi,
            String tanggal,
            String jam

    ) {

        String sql = """
            INSERT INTO event_platform.events
            (
                organizer_id,
                venue_id,
                event_name,
                description,
                event_date,
                event_time,
                status
            )
            VALUES
            (
                ?,
                ?,
                ?,
                ?,
                ?,
                ?,
                'draft'
            )
        """;

        try (

                Connection conn =
                        Koneksi.getKoneksi();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(1, organizerId);

            ps.setInt(2, venueId);

            ps.setString(3, namaEvent);

            ps.setString(4, deskripsi);

            ps.setDate(
                    5,
                    java.sql.Date.valueOf(
                            tanggal
                    )
            );

            ps.setTime(
                    6,
                    java.sql.Time.valueOf(
                            jam + ":00"
                    )
            );

            int hasil =
                    ps.executeUpdate();

            return hasil > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    /*
     * =========================
     * TAMPIL EVENT
     * =========================
     */
    public void tampilEvent() {

        String sql = """
            SELECT
                e.event_id,
                e.event_name,
                e.event_date,
                e.event_time,
                e.status,
                v.venue_name
            FROM event_platform.events e

            JOIN event_platform.venues v
                ON e.venue_id = v.venue_id

            ORDER BY e.event_date ASC
        """;

        try (

                Connection conn =
                        Koneksi.getKoneksi();

                PreparedStatement ps =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()

        ) {

            System.out.println(
                    "\n===== DAFTAR EVENT ====="
            );

            boolean ada = false;

            while (rs.next()) {

                ada = true;

                System.out.println(
                        "\nID Event   : "
                        + rs.getInt("event_id")
                );

                System.out.println(
                        "Nama Event : "
                        + rs.getString("event_name")
                );

                System.out.println(
                        "Venue      : "
                        + rs.getString("venue_name")
                );

                System.out.println(
                        "Tanggal    : "
                        + rs.getString("event_date")
                );

                System.out.println(
                        "Jam        : "
                        + rs.getString("event_time")
                );

                System.out.println(
                        "Status     : "
                        + rs.getString("status")
                );
            }

            if (!ada) {

                System.out.println(
                        "\nBelum ada event."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}