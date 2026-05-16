package repository;

import config.Koneksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LaporanOrganizerRepository {

    /*
     * =========================
     * LAPORAN PENJUALAN
     * =========================
     */
    public void laporanPenjualan(
            int organizerId
    ) {

        String sql = """
            SELECT
                e.event_name,
                COUNT(b.booking_id) total_booking,
                COALESCE(
                    SUM(b.total_amount),
                    0
                ) total_pendapatan
            FROM event_platform.events e

            LEFT JOIN
                event_platform.bookings b
                    ON e.event_id =
                       b.event_id

            WHERE e.organizer_id = ?

            GROUP BY
                e.event_id,
                e.event_name

            ORDER BY
                total_pendapatan DESC
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
                    "\n===== LAPORAN PENJUALAN ====="
            );

            boolean ada = false;

            while (rs.next()) {

                ada = true;

                System.out.println(
                        "\nEvent : "
                        + rs.getString(
                                "event_name"
                        )
                );

                System.out.println(
                        "Total Booking : "
                        + rs.getInt(
                                "total_booking"
                        )
                );

                System.out.println(
                        "Pendapatan : Rp"
                        + rs.getDouble(
                                "total_pendapatan"
                        )
                );
            }

            if (!ada) {

                System.out.println(
                        "\nBelum ada data penjualan."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /*
     * =========================
     * STATISTIK TICKET
     * =========================
     */
    public void statistikTicket(
            int organizerId
    ) {

        String sql = """
            SELECT
                e.event_name,
                t.ticket_type,
                t.total_quota,
                COALESCE(
                    SUM(bd.quantity),
                    0
                ) total_terjual
            FROM event_platform.tickets t

            JOIN event_platform.events e
                ON t.event_id =
                   e.event_id

            LEFT JOIN
                event_platform.booking_details bd
                    ON t.ticket_id =
                       bd.ticket_id

            WHERE e.organizer_id = ?

            GROUP BY
                e.event_name,
                t.ticket_type,
                t.total_quota

            ORDER BY
                e.event_name
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
                    "\n===== STATISTIK TICKET ====="
            );

            boolean ada = false;

            while (rs.next()) {

                ada = true;

                System.out.println(
                        "\nEvent : "
                        + rs.getString(
                                "event_name"
                        )
                );

                System.out.println(
                        "Ticket Type : "
                        + rs.getString(
                                "ticket_type"
                        )
                );

                System.out.println(
                        "Quota : "
                        + rs.getInt(
                                "total_quota"
                        )
                );

                System.out.println(
                        "Terjual : "
                        + rs.getInt(
                                "total_terjual"
                        )
                );
            }

            if (!ada) {

                System.out.println(
                        "\nBelum ada statistik."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}