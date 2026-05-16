package repository;

import config.Koneksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RiwayatBookingRepository {

    public void tampilRiwayat(
            int userId
    ) {

        String sql = """
            SELECT
                b.booking_id,
                e.event_name,
                b.total_amount,
                b.status,
                b.booking_at
            FROM event_platform.bookings b
            JOIN event_platform.events e
            ON b.event_id = e.event_id
            WHERE b.user_id = ?
            ORDER BY b.booking_at DESC
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
                    "\n===== RIWAYAT TIKET ====="
            );

            boolean ada = false;

            while (rs.next()) {

                ada = true;

                System.out.println(
                        "\nBooking ID : "
                        + rs.getInt("booking_id")
                );

                System.out.println(
                        "Event      : "
                        + rs.getString("event_name")
                );

                System.out.println(
                        "Total      : Rp"
                        + rs.getDouble("total_amount")
                );

                System.out.println(
                        "Status     : "
                        + rs.getString("status")
                );

                System.out.println(
                        "Tanggal    : "
                        + rs.getTimestamp("booking_at")
                );
            }

            if (!ada) {

                System.out.println(
                        "\nBelum ada riwayat booking."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}