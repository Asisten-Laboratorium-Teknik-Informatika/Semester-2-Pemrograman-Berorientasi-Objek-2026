package repository;

import config.Koneksi;
import util.Tampilan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TicketRepository {

    /*
     * =========================
     * TAMPIL EVENT ORGANIZER
     * =========================
     */
    public void tampilEventOrganizer(
            int organizerId
    ) {

        String sql = """
            SELECT
                event_id,
                event_name,
                event_date,
                status
            FROM event_platform.events
            WHERE organizer_id = ?
            ORDER BY event_date ASC
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
                    "\n===== EVENT SAYA ====="
            );

            boolean ada = false;

            while (rs.next()) {

                ada = true;

                System.out.println(
                        "\nID Event : "
                        + rs.getInt("event_id")
                );

                System.out.println(
                        "Nama     : "
                        + rs.getString("event_name")
                );

                System.out.println(
                        "Tanggal  : "
                        + rs.getString("event_date")
                );

                System.out.println(
                        "Status   : "
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

    /*
 * =========================
 * TAMBAH TICKET
 * =========================
 */
public boolean tambahTicket(

        int eventId,
        String ticketType,
        double price,
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

        conn.setAutoCommit(true);

        ps.setInt(
                1,
                eventId
        );

        ps.setString(
                2,
                ticketType
        );

        ps.setDouble(
                3,
                price
        );

        ps.setInt(
                4,
                quota
        );

        ps.setTimestamp(
                5,
                java.sql.Timestamp.valueOf(
                        saleStart
                )
        );

        ps.setTimestamp(
                6,
                java.sql.Timestamp.valueOf(
                        saleEnd
                )
        );

        ps.setString(
                7,
                "available"
        );

        int hasil =
                ps.executeUpdate();

        System.out.println(
                "\nRows inserted : "
                + hasil
        );

        if (hasil > 0) {

            Tampilan.sukses(
        "Ticket berhasil ditambahkan"
);

            return true;
        }

    } catch (Exception e) {

        e.printStackTrace();
    }

    Tampilan.gagal(
        "Gagal tambah ticket"
);

    return false;
}
    /*
     * =========================
     * LIHAT TICKET ORGANIZER
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
            FROM
                event_platform.tickets t

            JOIN
                event_platform.events e
                    ON t.event_id = e.event_id

            WHERE
                e.organizer_id = ?

            ORDER BY
                t.ticket_id DESC
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

            Tampilan.judul(
        "DATA TICKET"
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
     * UPDATE STATUS TICKET
     * =========================
     */
    public boolean updateStatusTicket(

            int ticketId,
            String status

    ) {

        String sql = """
            UPDATE
                event_platform.tickets
            SET
                status = CAST(? AS ticket_status)
            WHERE
                ticket_id = ?
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

            if (hasil > 0) {

                Tampilan.sukses(
        "Status ticket berhasil diupdate"
);

                return true;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        Tampilan.gagal(
        "Gagal update status ticket"
);

        return false;
    }

    /*
     * =========================
     * TAMPIL SEMUA EVENT
     * =========================
     */
    public void tampilSemuaEvent() {

        String sql = """
            SELECT
                event_id,
                event_name,
                event_date,
                status
            FROM event_platform.events
            WHERE status != 'cancelled'
            ORDER BY event_date ASC
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

            while (rs.next()) {

                System.out.println(
                        "\nID Event : "
                        + rs.getInt("event_id")
                );

                System.out.println(
                        "Nama     : "
                        + rs.getString("event_name")
                );

                System.out.println(
                        "Tanggal  : "
                        + rs.getString("event_date")
                );

                System.out.println(
                        "Status   : "
                        + Tampilan.status(rs.getString("status"))
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /*
     * =========================
     * TAMPIL TICKET BY EVENT
     * =========================
     */
    public void tampilTicketByEvent(
            int eventId
    ) {

        String sql = """
            SELECT
                ticket_id,
                ticket_type,
                price,
                total_quota,
                status
            FROM event_platform.tickets
            WHERE event_id = ?
        """;

        try (

                Connection conn =
                        Koneksi.getKoneksi();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(1, eventId);

            ResultSet rs =
                    ps.executeQuery();

            System.out.println(
                    "\n===== DAFTAR TICKET ====="
            );

            boolean ada = false;

            while (rs.next()) {

                ada = true;

                System.out.println(
                        "\nID Ticket : "
                        + rs.getInt("ticket_id")
                );

                System.out.println(
                        "Type      : "
                        + rs.getString("ticket_type")
                );

                System.out.println(
                        "Harga     : "
                        + rs.getDouble("price")
                );

                System.out.println(
                        "Quota     : "
                        + rs.getInt("total_quota")
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
     * CARI TICKET
     * =========================
     */
    public ResultSet cariTicket(

            Connection conn,
            int ticketId

    ) {

        try {

            String sql = """
                SELECT *
                FROM event_platform.tickets
                WHERE ticket_id = ?
            """;

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1, ticketId);

            return ps.executeQuery();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    /*
     * =========================
     * UPDATE QUOTA
     * =========================
     */
    public void updateQuota(

            Connection conn,
            int ticketId,
            int jumlah

    ) {

        try {

            String sql = """
                UPDATE event_platform.tickets
                SET total_quota =
                    total_quota - ?
                WHERE ticket_id = ?
            """;

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1, jumlah);

            ps.setInt(2, ticketId);

            ps.executeUpdate();

            /*
             * AUTO SOLD OUT
             */
            String soldOutSql = """
                UPDATE event_platform.tickets
                SET status = 'sold_out'
                WHERE ticket_id = ?
                AND total_quota <= 0
            """;

            PreparedStatement soldPs =
                    conn.prepareStatement(
                            soldOutSql
                    );

            soldPs.setInt(1, ticketId);

            soldPs.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}