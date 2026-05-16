package repository;

import config.Koneksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class IssuedTicketRepository {

    /*
     * =========================
     * GENERATE TICKET
     * =========================
     */
    public boolean generateTicket(
            Connection conn,
            int bookingId
    ) {

        try {

            /*
             * =========================
             * CEK BOOKING
             * =========================
             */
            String cekSql = """
                SELECT *
                FROM event_platform.bookings
                WHERE booking_id = ?
            """;

            PreparedStatement cekPs =
                    conn.prepareStatement(cekSql);

            cekPs.setInt(1, bookingId);

            ResultSet cekRs =
                    cekPs.executeQuery();

            if (!cekRs.next()) {

                return false;
            }

            /*
             * =========================
             * GENERATE CODE
             * =========================
             */
            String ticketCode =
                    "EVT-"
                    + UUID.randomUUID()
                    .toString()
                    .substring(0, 8)
                    .toUpperCase();

            /*
             * =========================
             * INSERT ISSUED TICKET
             * =========================
             */
            String sql = """
                INSERT INTO
                event_platform.issued_tickets
                (
                    booking_id,
                    ticket_code,
                    status
                )
                VALUES
                (
                    ?,
                    ?,
                    'active'
                )
            """;

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1, bookingId);

            ps.setString(2, ticketCode);

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
     * TICKET SAYA
     * =========================
     */
    public void tampilTicketUser(
            int userId
    ) {

        String sql = """
            SELECT
                it.ticket_code,
                e.event_name,
                it.status,
                it.expired_at
            FROM event_platform.issued_tickets it

            JOIN event_platform.bookings b
                ON it.booking_id = b.booking_id

            JOIN event_platform.events e
                ON b.event_id = e.event_id

            WHERE b.user_id = ?

            ORDER BY it.issued_ticket_id DESC
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

            System.out.println(
                    "\n===== TICKET SAYA ====="
            );

            boolean ada = false;

            while (rs.next()) {

                ada = true;

                System.out.println(
                        "\nKode Ticket : "
                        + rs.getString(
                                "ticket_code"
                        )
                );

                System.out.println(
                        "Event        : "
                        + rs.getString(
                                "event_name"
                        )
                );

                System.out.println(
                        "Status       : "
                        + rs.getString(
                                "status"
                        )
                );

                System.out.println(
                        "Expired At   : "
                        + rs.getTimestamp(
                                "expired_at"
                        )
                );
            }

            if (!ada) {

                System.out.println(
                        "\nBelum ada ticket."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}