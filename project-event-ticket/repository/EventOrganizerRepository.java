package repository;

import config.Koneksi;
import util.Tampilan;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;

public class EventOrganizerRepository {

    /*
     * =========================
     * TAMBAH EVENT
     * =========================
     */
    public boolean tambahEvent(

            int organizerId,
            int venueId,
            String nama,
            String deskripsi,
            String tanggal,
            String jam

    ) {

        try (

            Connection conn =
                    Koneksi.getKoneksi()

        ) {

            String sql = """
                INSERT INTO
                    event_platform.events
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
                    CAST('draft' AS event_status)
                )
            """;

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(
                    1,
                    organizerId
            );

            ps.setInt(
                    2,
                    venueId
            );

            ps.setString(
                    3,
                    nama
            );

            ps.setString(
                    4,
                    deskripsi
            );

            /*
             * DATE
             */
            ps.setDate(
                    5,
                    Date.valueOf(tanggal)
            );

            /*
             * TIME
             */
            ps.setTime(
                    6,
                    Time.valueOf(jam)
            );

            int result =
                    ps.executeUpdate();

            if (result > 0) {

                Tampilan.sukses(
        "Event berhasil ditambahkan"
);

                return true;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        System.out.println(
                "\nGagal tambah event!"
        );

        return false;
    }

    /*
     * =========================
     * TAMPIL EVENT ORGANIZER
     * =========================
     */
    public void tampilEventOrganizer(
            int organizerId
    ) {

        try (

            Connection conn =
                    Koneksi.getKoneksi()

        ) {

            String sql = """
                SELECT
                    event_id,
                    event_name,
                    description,
                    event_date,
                    event_time,
                    status
                FROM
                    event_platform.events
                WHERE
                    organizer_id = ?
                ORDER BY
                    event_id ASC
            """;

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(
                    1,
                    organizerId
            );

            ResultSet rs =
                    ps.executeQuery();

            System.out.println(
                    "\n===== EVENT SAYA ====="
            );

            boolean ada = false;

            while (rs.next()) {

                ada = true;

                System.out.println(
                        "\nEvent ID : "
                        + rs.getInt(
                                "event_id"
                        )
                );

                System.out.println(
                        "Nama Event : "
                        + rs.getString(
                                "event_name"
                        )
                );

                System.out.println(
                        "Deskripsi : "
                        + rs.getString(
                                "description"
                        )
                );

                System.out.println(
                        "Tanggal : "
                        + rs.getDate(
                                "event_date"
                        )
                );

                System.out.println(
                        "Jam : "
                        + rs.getTime(
                                "event_time"
                        )
                );

                System.out.println(
                        "Status : "
                        + rs.getString(
                                "status"
                        )
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

    /*
     * =========================
     * UPDATE STATUS EVENT
     * =========================
     */
    public boolean updateStatusEvent(

            int eventId,
            String status

    ) {

        try (

            Connection conn =
                    Koneksi.getKoneksi()

        ) {

            String sql = """
                UPDATE
                    event_platform.events
                SET
                    status = CAST(? AS event_status)
                WHERE
                    event_id = ?
            """;

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(
                    1,
                    status
            );

            ps.setInt(
                    2,
                    eventId
            );

            int result =
                    ps.executeUpdate();

            if (result > 0) {

                Tampilan.sukses(
        "Status event berhasil diupdate"
);

                return true;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        System.out.println(
                "\nGagal update status event!"
        );

        return false;
    }

    /*
     * =========================
     * HAPUS EVENT
     * =========================
     */
    public boolean hapusEvent(
            int eventId
    ) {

        try (

            Connection conn =
                    Koneksi.getKoneksi()

        ) {

            conn.setAutoCommit(false);

            /*
             * HAPUS ISSUED TICKETS
             */
            String issuedSql = """
                DELETE FROM
                    event_platform.issued_tickets
                WHERE
                    booking_id IN (
                        SELECT
                            booking_id
                        FROM
                            event_platform.bookings
                        WHERE
                            event_id = ?
                    )
            """;

            PreparedStatement issuedPs =
                    conn.prepareStatement(
                            issuedSql
                    );

            issuedPs.setInt(
                    1,
                    eventId
            );

            issuedPs.executeUpdate();

            /*
             * HAPUS PAYMENTS
             */
            String paymentSql = """
                DELETE FROM
                    event_platform.payments
                WHERE
                    booking_id IN (
                        SELECT
                            booking_id
                        FROM
                            event_platform.bookings
                        WHERE
                            event_id = ?
                    )
            """;

            PreparedStatement paymentPs =
                    conn.prepareStatement(
                            paymentSql
                    );

            paymentPs.setInt(
                    1,
                    eventId
            );

            paymentPs.executeUpdate();

            /*
             * HAPUS BOOKING DETAILS
             */
            String detailSql = """
                DELETE FROM
                    event_platform.booking_details
                WHERE
                    booking_id IN (
                        SELECT
                            booking_id
                        FROM
                            event_platform.bookings
                        WHERE
                            event_id = ?
                    )
            """;

            PreparedStatement detailPs =
                    conn.prepareStatement(
                            detailSql
                    );

            detailPs.setInt(
                    1,
                    eventId
            );

            detailPs.executeUpdate();

            /*
             * HAPUS BOOKINGS
             */
            String bookingSql = """
                DELETE FROM
                    event_platform.bookings
                WHERE
                    event_id = ?
            """;

            PreparedStatement bookingPs =
                    conn.prepareStatement(
                            bookingSql
                    );

            bookingPs.setInt(
                    1,
                    eventId
            );

            bookingPs.executeUpdate();

            /*
             * HAPUS WAITLIST
             */
            String waitlistSql = """
                DELETE FROM
                    event_platform.waitlist
                WHERE
                    ticket_id IN (
                        SELECT
                            ticket_id
                        FROM
                            event_platform.tickets
                        WHERE
                            event_id = ?
                    )
            """;

            PreparedStatement waitlistPs =
                    conn.prepareStatement(
                            waitlistSql
                    );

            waitlistPs.setInt(
                    1,
                    eventId
            );

            waitlistPs.executeUpdate();

            /*
             * HAPUS TICKETS
             */
            String ticketSql = """
                DELETE FROM
                    event_platform.tickets
                WHERE
                    event_id = ?
            """;

            PreparedStatement ticketPs =
                    conn.prepareStatement(
                            ticketSql
                    );

            ticketPs.setInt(
                    1,
                    eventId
            );

            ticketPs.executeUpdate();

            /*
             * HAPUS EVENT
             */
            String eventSql = """
                DELETE FROM
                    event_platform.events
                WHERE
                    event_id = ?
            """;

            PreparedStatement eventPs =
                    conn.prepareStatement(
                            eventSql
                    );

            eventPs.setInt(
                    1,
                    eventId
            );

            int result =
                    eventPs.executeUpdate();

            conn.commit();

            if (result > 0) {

                Tampilan.sukses(
        "Event berhasil dihapus"
);

                return true;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        System.out.println(
                "\nGagal hapus event!"
        );

        return false;
    }
}