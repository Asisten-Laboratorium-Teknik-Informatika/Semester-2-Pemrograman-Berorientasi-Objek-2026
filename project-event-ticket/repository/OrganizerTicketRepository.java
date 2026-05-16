package repository;

import config.Koneksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class OrganizerTicketRepository {

    /*
     * =========================
     * TAMBAH TICKET
     * =========================
     */
    public boolean tambahTicket(

            int eventId,
            String type,
            double harga,
            int quota,
            String saleStart,
            String saleEnd

    ) {

        String sql = """
            INSERT INTO
            event_platform.tickets
            (
                event_id,
                ticket_type,
                price,
                total_quota,
                sale_start,
                sale_end,
                status
            )
            VALUES
            (
                ?,
                CAST(? AS ticket_type),
                ?,
                ?,
                ?,
                ?,
                CAST(? AS ticket_status)
            )
        """;

        try (

                Connection conn =
                        Koneksi.getKoneksi();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(1, eventId);

            ps.setString(2, type);

            ps.setDouble(3, harga);

            ps.setInt(4, quota);

            /*
             * =========================
             * FIX TIMESTAMP
             * =========================
             */
            ps.setTimestamp(
                    5,
                    Timestamp.valueOf(
                            saleStart
                    )
            );

            ps.setTimestamp(
                    6,
                    Timestamp.valueOf(
                            saleEnd
                    )
            );

            ps.setString(7, "available");

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
     * TAMPIL TICKET
     * =========================
     */
    public void tampilTicketOrganizer(
            int organizerId
    ) {

        String sql = """
            SELECT
                t.ticket_id,
                e.event_name,
                t.ticket_type,
                t.price,
                t.total_quota,
                t.sale_start,
                t.sale_end,
                t.status
            FROM event_platform.tickets t

            JOIN event_platform.events e
                ON t.event_id = e.event_id

            WHERE e.organizer_id = ?

            ORDER BY t.ticket_id DESC
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
                    "\n===== DATA TICKET ====="
            );

            boolean ada = false;

            while (rs.next()) {

                ada = true;

                System.out.println(
                        "\nTicket ID : "
                        + rs.getInt("ticket_id")
                );

                System.out.println(
                        "Event     : "
                        + rs.getString("event_name")
                );

                System.out.println(
                        "Type      : "
                        + rs.getString("ticket_type")
                );

                System.out.println(
                        "Harga     : Rp"
                        + rs.getDouble("price")
                );

                System.out.println(
                        "Quota     : "
                        + rs.getInt("total_quota")
                );

                System.out.println(
                        "Sale Start: "
                        + rs.getTimestamp("sale_start")
                );

                System.out.println(
                        "Sale End  : "
                        + rs.getTimestamp("sale_end")
                );

                System.out.println(
                        "Status    : "
                        + rs.getString("status")
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

    /*
     * =========================
     * UPDATE STATUS
     * =========================
     */
    public boolean updateStatusTicket(
            int ticketId,
            String status
    ) {

        String sql = """
            UPDATE event_platform.tickets
            SET
                status =
                CAST(? AS ticket_status)
            WHERE ticket_id = ?
        """;

        try (

                Connection conn =
                        Koneksi.getKoneksi();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setString(1, status);

            ps.setInt(2, ticketId);

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
     * HAPUS TICKET
     * =========================
     */
    public boolean hapusTicket(
            int ticketId
    ) {

        String sql = """
            DELETE FROM
            event_platform.tickets
            WHERE ticket_id = ?
        """;

        try (

                Connection conn =
                        Koneksi.getKoneksi();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(1, ticketId);

            int hasil =
                    ps.executeUpdate();

            return hasil > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }
}