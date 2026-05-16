package repository;

import config.Koneksi;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OrganizerSponsorRepository {

    /*
     * =========================
     * TAMBAH SPONSOR
     * =========================
     */
    public boolean tambahSponsor(

            int eventId,
            int tierId,
            String company,
            String contact,
            String email,
            double amount,
            String start,
            String end

    ) {

        String sql = """
            INSERT INTO
            event_platform.event_sponsors
            (
                event_id,
                tier_id,
                company_name,
                contact_person,
                contact_email,
                contribution_amount,
                start_at,
                end_at,
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
                ?,
                ?,
                CAST(? AS sponsor_status)
            )
        """;

        try (

                Connection conn =
                        Koneksi.getKoneksi();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(1, eventId);

            ps.setInt(2, tierId);

            ps.setString(3, company);

            ps.setString(4, contact);

            ps.setString(5, email);

            ps.setDouble(6, amount);

            /*
             * =========================
             * FIX DATE
             * =========================
             */
            ps.setDate(
                    7,
                    Date.valueOf(start)
            );

            ps.setDate(
                    8,
                    Date.valueOf(end)
            );

            ps.setString(9, "pending");

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
     * TAMPIL SPONSOR
     * =========================
     */
    public void tampilSponsorOrganizer(
            int organizerId
    ) {

        String sql = """
            SELECT
                s.sponsor_id,
                e.event_name,
                st.tier_name,
                s.company_name,
                s.contact_person,
                s.contact_email,
                s.contribution_amount,
                s.start_at,
                s.end_at,
                s.status
            FROM event_platform.event_sponsors s

            JOIN event_platform.events e
                ON s.event_id = e.event_id

            JOIN event_platform.sponsor_tier st
                ON s.tier_id = st.tier_id

            WHERE e.organizer_id = ?

            ORDER BY s.sponsor_id DESC
        """;

        try (

                Connection conn =
                        Koneksi.getKoneksi();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(1, organizerId);

            ResultSet rs =
                    ps.executeQuery();

            System.out.println(
                    "\n===== DATA SPONSOR ====="
            );

            boolean ada = false;

            while (rs.next()) {

                ada = true;

                System.out.println(
                        "\nSponsor ID : "
                        + rs.getInt("sponsor_id")
                );

                System.out.println(
                        "Event      : "
                        + rs.getString("event_name")
                );

                System.out.println(
                        "Tier       : "
                        + rs.getString("tier_name")
                );

                System.out.println(
                        "Perusahaan : "
                        + rs.getString("company_name")
                );

                System.out.println(
                        "Contact    : "
                        + rs.getString("contact_person")
                );

                System.out.println(
                        "Email      : "
                        + rs.getString("contact_email")
                );

                System.out.println(
                        "Contribution : Rp"
                        + rs.getDouble(
                                "contribution_amount"
                        )
                );

                System.out.println(
                        "Start Date : "
                        + rs.getDate("start_at")
                );

                System.out.println(
                        "End Date   : "
                        + rs.getDate("end_at")
                );

                System.out.println(
                        "Status     : "
                        + rs.getString("status")
                );
            }

            if (!ada) {

                System.out.println(
                        "\nBelum ada sponsor."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /*
     * =========================
     * UPDATE STATUS
     * =========================
     */
    public boolean updateStatusSponsor(
            int sponsorId,
            String status
    ) {

        String sql = """
            UPDATE event_platform.event_sponsors
            SET
                status =
                CAST(? AS sponsor_status),
                updated_at = now()
            WHERE sponsor_id = ?
        """;

        try (

                Connection conn =
                        Koneksi.getKoneksi();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setString(1, status);

            ps.setInt(2, sponsorId);

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
     * HAPUS SPONSOR
     * =========================
     */
    public boolean hapusSponsor(
            int sponsorId
    ) {

        String sql = """
            DELETE FROM
            event_platform.event_sponsors
            WHERE sponsor_id = ?
        """;

        try (

                Connection conn =
                        Koneksi.getKoneksi();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(1, sponsorId);

            int hasil =
                    ps.executeUpdate();

            return hasil > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }
}