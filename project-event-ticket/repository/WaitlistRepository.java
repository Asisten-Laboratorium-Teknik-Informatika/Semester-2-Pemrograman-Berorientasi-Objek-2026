package repository;

import config.Koneksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class WaitlistRepository {

    /*
     * =========================
     * AMBIL POSISI TERAKHIR
     * =========================
     */
    public int getLastPosition(
            Connection conn,
            int ticketId
    ) {

        String sql = """
            SELECT COALESCE(MAX(position), 0)
            AS posisi
            FROM event_platform.waitlist
            WHERE ticket_id = ?
        """;

        try {

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1, ticketId);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                return rs.getInt(
                        "posisi"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return 0;
    }

    /*
     * =========================
     * MASUK WAITLIST
     * =========================
     */
    public boolean masukWaitlist(

            int userId,
            int ticketId

    ) {

        String cekSql = """
            SELECT *
            FROM event_platform.waitlist
            WHERE user_id = ?
            AND ticket_id = ?
            AND status = 'waiting'
        """;

        String insertSql = """
            INSERT INTO
            event_platform.waitlist
            (
                user_id,
                ticket_id,
                position,
                status
            )
            VALUES
            (
                ?,
                ?,
                ?,
                'waiting'
            )
        """;

        try (

                Connection conn =
                        Koneksi.getKoneksi()

        ) {

            /*
             * =========================
             * CEK DUPLIKAT
             * =========================
             */
            PreparedStatement cekPs =
                    conn.prepareStatement(cekSql);

            cekPs.setInt(1, userId);

            cekPs.setInt(2, ticketId);

            ResultSet cekRs =
                    cekPs.executeQuery();

            if (cekRs.next()) {

                System.out.println(
                        "\nKamu sudah masuk waitlist!"
                );

                return false;
            }

            /*
             * =========================
             * POSISI
             * =========================
             */
            int posisi =
                    getLastPosition(
                            conn,
                            ticketId
                    ) + 1;

            /*
             * =========================
             * INSERT WAITLIST
             * =========================
             */
            PreparedStatement ps =
                    conn.prepareStatement(insertSql);

            ps.setInt(1, userId);

            ps.setInt(2, ticketId);

            ps.setInt(3, posisi);

            int hasil =
                    ps.executeUpdate();

            if (hasil > 0) {

                System.out.println(
                        "\nBerhasil masuk waitlist!"
                );

                System.out.println(
                        "Posisi antrean : "
                        + posisi
                );

                return true;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    /*
     * =========================
     * LIHAT WAITLIST USER
     * =========================
     */
    public void tampilWaitlistUser(
            int userId
    ) {

        String sql = """
            SELECT
                e.event_name,
                t.ticket_type,
                w.position,
                w.status,
                w.created_at
            FROM event_platform.waitlist w

            JOIN event_platform.tickets t
                ON w.ticket_id = t.ticket_id

            JOIN event_platform.events e
                ON t.event_id = e.event_id

            WHERE w.user_id = ?

            ORDER BY w.created_at DESC
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
                    "\n===== WAITLIST SAYA ====="
            );

            boolean ada = false;

            while (rs.next()) {

                ada = true;

                System.out.println(
                        "\nEvent    : "
                        + rs.getString(
                                "event_name"
                        )
                );

                System.out.println(
                        "Ticket   : "
                        + rs.getString(
                                "ticket_type"
                        )
                );

                System.out.println(
                        "Posisi   : "
                        + rs.getInt(
                                "position"
                        )
                );

                System.out.println(
                        "Status   : "
                        + rs.getString(
                                "status"
                        )
                );

                System.out.println(
                        "Created  : "
                        + rs.getTimestamp(
                                "created_at"
                        )
                );
            }

            if (!ada) {

                System.out.println(
                        "\nBelum ada waitlist."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}