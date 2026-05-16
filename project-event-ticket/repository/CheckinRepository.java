package repository;

import config.Koneksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CheckinRepository {

    /*
     * =========================
     * LIST TICKET AKTIF
     * =========================
     */
    public void tampilTicketAktif() {

        String sql = """
            SELECT
                it.ticket_code,
                it.status,
                e.event_name
            FROM event_platform.issued_tickets it

            JOIN event_platform.bookings b
                ON it.booking_id = b.booking_id

            JOIN event_platform.events e
                ON b.event_id = e.event_id

            WHERE it.status = 'active'

            ORDER BY e.event_name ASC
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
                    "\n===== DAFTAR TICKET AKTIF ====="
            );

            boolean ada = false;

            while (rs.next()) {

                ada = true;

                System.out.println(
                        "\nTicket Code : "
                        + rs.getString("ticket_code")
                );

                System.out.println(
                        "Event       : "
                        + rs.getString("event_name")
                );

                System.out.println(
                        "Status      : "
                        + rs.getString("status")
                );
            }

            if (!ada) {

                System.out.println(
                        "\nTidak ada ticket aktif."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /*
     * =========================
     * TAMPIL SEMUA TICKET
     * =========================
     */
    public void tampilSemuaTicket() {

        String sql = """
            SELECT
                it.ticket_code,
                it.status,

                u.name,

                e.event_name

            FROM event_platform.issued_tickets it

            JOIN event_platform.bookings b
                ON it.booking_id = b.booking_id

            JOIN event_platform.users u
                ON b.user_id = u.user_id

            JOIN event_platform.events e
                ON b.event_id = e.event_id

            ORDER BY it.issued_ticket_id DESC
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
                    "\n===== DATA TICKET ====="
            );

            boolean ada = false;

            while (rs.next()) {

                ada = true;

                System.out.println(
                        "\nTicket Code : "
                        + rs.getString("ticket_code")
                );

                System.out.println(
                        "User        : "
                        + rs.getString("name")
                );

                System.out.println(
                        "Event       : "
                        + rs.getString("event_name")
                );

                System.out.println(
                        "Status      : "
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
     * PROSES CHECK-IN
     * =========================
     */
    public boolean prosesCheckin(

            int staffId,
            String ticketCode,
            String metode

    ) {

        try (

                Connection conn =
                        Koneksi.getKoneksi()

        ) {

            conn.setAutoCommit(false);

            /*
             * CARI TICKET
             */
            String cari = """
                SELECT
                    issued_ticket_id,
                    status
                FROM event_platform.issued_tickets
                WHERE ticket_code = ?
            """;

            PreparedStatement psCari =
                    conn.prepareStatement(cari);

            psCari.setString(1, ticketCode);

            ResultSet rs =
                    psCari.executeQuery();

            /*
             * TICKET TIDAK ADA
             */
            if (!rs.next()) {

                System.out.println(
                        "\nTicket tidak ditemukan!"
                );

                return false;
            }

            int issuedTicketId =
                    rs.getInt("issued_ticket_id");

            String status =
                    rs.getString("status");

            /*
             * DUPLICATE
             */
            if (
                    status.equalsIgnoreCase("used")
            ) {

                insertCheckin(

                        conn,

                        issuedTicketId,

                        staffId,

                        metode,

                        "duplicate"
                );

                System.out.println(
                        "\nTicket sudah digunakan!"
                );

                return false;
            }

            /*
             * EXPIRED / CANCELLED
             */
            if (
                    status.equalsIgnoreCase("expired")
                    ||
                    status.equalsIgnoreCase("cancelled")
            ) {

                insertCheckin(

                        conn,

                        issuedTicketId,

                        staffId,

                        metode,

                        "failed"
                );

                System.out.println(
                        "\nTicket tidak valid!"
                );

                return false;
            }

            /*
             * UPDATE TICKET
             */
            String updateTicket = """
                UPDATE event_platform.issued_tickets
                SET
                    status = 'used',
                    used_at = now(),
                    updated_at = now()
                WHERE issued_ticket_id = ?
            """;

            PreparedStatement psUpdate =
                    conn.prepareStatement(updateTicket);

            psUpdate.setInt(
                    1,
                    issuedTicketId
            );

            psUpdate.executeUpdate();

            /*
             * INSERT SUCCESS
             */
            insertCheckin(

                    conn,

                    issuedTicketId,

                    staffId,

                    metode,

                    "success"
            );

            conn.commit();

            System.out.println(
                    "\nCheck-in berhasil!"
            );

            return true;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    /*
     * =========================
     * INSERT CHECKIN
     * =========================
     */
    private void insertCheckin(

            Connection conn,
            int issuedTicketId,
            int staffId,
            String metode,
            String status

    ) throws Exception {

        String sql = """
            INSERT INTO event_platform.checkin
            (
                issued_ticket_id,
                staff_id,
                method,
                status
            )
            VALUES
            (
                ?,
                ?,
                CAST(? AS checkin_method),
                CAST(? AS checkin_status)
            )
        """;

        PreparedStatement ps =
                conn.prepareStatement(sql);

        ps.setInt(1, issuedTicketId);

        ps.setInt(2, staffId);

        ps.setString(3, metode);

        ps.setString(4, status);

        ps.executeUpdate();
    }
}