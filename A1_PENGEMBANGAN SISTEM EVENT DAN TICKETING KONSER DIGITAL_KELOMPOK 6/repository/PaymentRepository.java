package repository;

import config.Koneksi;
import util.Tampilan;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PaymentRepository {

    public void tampilPaymentPending(
            int userId
    ) {

        String sql = """
            SELECT
                p.payment_id,
                e.event_name,
                b.total_amount,
                p.status
            FROM event_platform.payments p
            JOIN event_platform.bookings b
                ON p.booking_id = b.booking_id
            JOIN event_platform.events e
                ON b.event_id = e.event_id
            WHERE b.user_id = ?
            AND p.status = 'pending'
            ORDER BY p.payment_id DESC
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

            Tampilan.judul(
        "PAYMENT PENDING"
);

            boolean ada = false;

            while (rs.next()) {

                ada = true;

                System.out.println(
                        "\nPayment ID : "
                        + rs.getInt("payment_id")
                );

                System.out.println(
                        "Event      : "
                        + rs.getString("event_name")
                );

                System.out.println(
                        "Total      : Rp "
                        + rs.getDouble("total_amount")
                );

                System.out.println(
                        "Status     : "
                        + Tampilan.status(
        rs.getString("status")
)
                );
            }

            if (!ada) {

                System.out.println(
                        "\nTidak ada payment pending."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void tampilPaymentBerhasil(
            int userId
    ) {

        String sql = """
            SELECT
                p.payment_id,
                e.event_name,
                b.total_amount,
                p.status
            FROM event_platform.payments p
            JOIN event_platform.bookings b
                ON p.booking_id = b.booking_id
            JOIN event_platform.events e
                ON b.event_id = e.event_id
            WHERE b.user_id = ?
            AND p.status = 'paid'
            ORDER BY p.payment_id DESC
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

            Tampilan.judul(
        "PAYMENT BERHASIL"
);

            boolean ada = false;

            while (rs.next()) {

                ada = true;

                System.out.println(
                        "\nPayment ID : "
                        + rs.getInt("payment_id")
                );

                System.out.println(
                        "Event      : "
                        + rs.getString("event_name")
                );

                System.out.println(
                        "Total      : Rp "
                        + rs.getDouble("total_amount")
                );

                System.out.println(
                        "Status     : "
                        + Tampilan.status(
        rs.getString("status")
)
                );
            }

            if (!ada) {

                System.out.println(
                        "\nBelum ada payment berhasil."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public boolean bayarTicket(

        int paymentId,
        String metode,
        double uangBayar

) {

    Connection conn = null;

    try {

        conn =
                Koneksi.getKoneksi();

        /*
         * =========================
         * AMBIL TOTAL TAGIHAN
         * =========================
         */
        String cekSql = """
            SELECT
                amount,
                status
            FROM event_platform.payments
            WHERE payment_id = ?
        """;

        PreparedStatement cekPs =
                conn.prepareStatement(cekSql);

        cekPs.setInt(1, paymentId);

        ResultSet rs =
                cekPs.executeQuery();

        if (!rs.next()) {

            System.out.println(
                    "\nPayment tidak ditemukan!"
            );

            return false;
        }

        double totalTagihan =
                rs.getDouble("amount");

        String status =
                rs.getString("status");

        /*
         * =========================
         * VALIDASI STATUS
         * =========================
         */
        if (status.equalsIgnoreCase("paid")) {

            System.out.println(
                    "\nPayment sudah dibayar!"
            );

            return false;
        }

        /*
         * =========================
         * VALIDASI PEMBAYARAN
         * =========================
         */
        if (uangBayar < totalTagihan) {

            double kurang =
                    totalTagihan - uangBayar;

            System.out.println(
                    "\nPembayaran gagal!"
            );

            System.out.println(
                    "Uang kurang : Rp "
                    + kurang
            );

            return false;
        }

        /*
         * =========================
         * HITUNG KEMBALIAN
         * =========================
         */
        double kembalian =
                uangBayar - totalTagihan;

        /*
         * =========================
         * UPDATE PAYMENT
         * =========================
         */
        String updateSql = """
            UPDATE event_platform.payments
            SET
                payment_method = ?,
                status = 'paid',
                paid_at = now()
            WHERE payment_id = ?
        """;

        PreparedStatement updatePs =
                conn.prepareStatement(updateSql);

        updatePs.setString(1, metode);
        updatePs.setInt(2, paymentId);

        int result =
                updatePs.executeUpdate();

        if (result > 0) {

            /*
             * AUTO GENERATE TICKET
             */
            generateIssuedTicket(
                    paymentId
            );

            Tampilan.sukses(
        "Pembayaran berhasil"
);

            System.out.println(
                    "Total Tagihan : Rp "
                    + totalTagihan
            );

            System.out.println(
                    "Uang Dibayar  : Rp "
                    + uangBayar
            );

            System.out.println(
                    "Kembalian     : Rp "
                    + kembalian
            );

            System.out.println(
                    "Metode        : "
                    + metode
            );

            return true;
        }

    } catch (Exception e) {

        e.printStackTrace();
    }

    Tampilan.gagal(
        "Pembayaran Gagal"
);

    return false;
}

    private void generateIssuedTicket(
        int paymentId
) {

    try (
            Connection conn =
                    Koneksi.getKoneksi()
    ) {

        String sql = """
            SELECT
                p.booking_id,
                bd.quantity,
                t.ticket_type
            FROM event_platform.payments p
            JOIN event_platform.bookings b
                ON p.booking_id = b.booking_id
            JOIN event_platform.booking_details bd
                ON b.booking_id = bd.booking_id
            JOIN event_platform.tickets t
                ON bd.ticket_id = t.ticket_id
            WHERE p.payment_id = ?
        """;

        PreparedStatement ps =
                conn.prepareStatement(sql);

        ps.setInt(1, paymentId);

        ResultSet rs =
                ps.executeQuery();

        while (rs.next()) {

            int bookingId =
                    rs.getInt("booking_id");

            int qty =
                    rs.getInt("quantity");

            String ticketType =
                    rs.getString("ticket_type");

            String prefix =
                    getPrefixTicket(ticketType);

            for (int i = 1; i <= qty; i++) {

                String code =
                        prefix
                        + "-"
                        + System.currentTimeMillis()
                        + "-"
                        + i;

                String insertSql = """
                    INSERT INTO event_platform.issued_tickets
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

                PreparedStatement insertPs =
                        conn.prepareStatement(insertSql);

                insertPs.setInt(1, bookingId);
                insertPs.setString(2, code);

                insertPs.executeUpdate();

                System.out.println(
                        "Ticket berhasil dibuat : "
                        + code
                );
            }
        }

    } catch (Exception e) {

        e.printStackTrace();
    }
}

    private String getPrefixTicket(
            String ticketType
    ) {

        if (ticketType == null) {

            return "TCK";
        }

        if (ticketType.equalsIgnoreCase("regular")) {

            return "REG";
        }

        if (ticketType.equalsIgnoreCase("vip")) {

            return "VIP";
        }

        if (ticketType.equalsIgnoreCase("vvip")) {

            return "VVIP";
        }

        return "TCK";
    }

    public void tampilTicketSaya(
        int userId
) {

    String sql = """
        SELECT
            it.ticket_code,
            e.event_name,
            e.event_date,
            e.event_time,
            t.ticket_type,
            it.status
        FROM event_platform.issued_tickets it

        JOIN event_platform.bookings b
            ON it.booking_id = b.booking_id

        JOIN event_platform.booking_details bd
            ON b.booking_id = bd.booking_id

        JOIN event_platform.tickets t
            ON bd.ticket_id = t.ticket_id

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
                    "\nTicket Code : "
                    + rs.getString("ticket_code")
            );

            System.out.println(
                    "Jenis       : "
                    + rs.getString("ticket_type")
            );

            System.out.println(
                    "Event       : "
                    + rs.getString("event_name")
            );

            System.out.println(
                    "Tanggal     : "
                    + rs.getDate("event_date")
            );

            System.out.println(
                    "Jam         : "
                    + rs.getTime("event_time")
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

    public boolean cetakTicketSaya(
        int userId
) {

    String sql = """
        SELECT
            it.ticket_code,
            e.event_name,
            e.event_date,
            e.event_time,
            t.ticket_type,
            it.status
        FROM event_platform.issued_tickets it

        JOIN event_platform.bookings b
            ON it.booking_id = b.booking_id

        JOIN event_platform.booking_details bd
            ON b.booking_id = bd.booking_id

        JOIN event_platform.tickets t
            ON bd.ticket_id = t.ticket_id

        JOIN event_platform.events e
            ON b.event_id = e.event_id

        WHERE b.user_id = ?

        ORDER BY it.issued_ticket_id DESC
    """;

    String namaFile =
            "ticket_user_"
            + userId
            + ".txt";

    try (
            Connection conn =
                    Koneksi.getKoneksi();

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            java.io.PrintWriter writer =
                    new java.io.PrintWriter(
                            new java.io.FileWriter(
                                    namaFile
                            )
                    )
    ) {

        ps.setInt(
                1,
                userId
        );

        ResultSet rs =
                ps.executeQuery();

        writer.println(
                "===================================="
        );
        writer.println(
                "             TICKET SAYA            "
        );
        writer.println(
                "===================================="
        );

        boolean ada = false;

        while (rs.next()) {

            ada = true;

            writer.println();
            writer.println(
                    "Ticket Code : "
                    + rs.getString("ticket_code")
            );

            writer.println(
                    "Jenis Tiket : "
                    + rs.getString("ticket_type")
            );

            writer.println(
                    "Event       : "
                    + rs.getString("event_name")
            );

            writer.println(
                    "Tanggal     : "
                    + rs.getDate("event_date")
            );

            writer.println(
                    "Jam         : "
                    + rs.getTime("event_time")
            );

            writer.println(
                    "Status      : "
                    + rs.getString("status")
            );

            writer.println(
                    "------------------------------------"
            );
        }

        if (!ada) {

            writer.println();
            writer.println(
                    "Belum ada ticket."
            );
        }

        System.out.println(
                "\nTicket berhasil dicetak ke file: "
                + namaFile
        );

        return true;

    } catch (Exception e) {

        e.printStackTrace();
    }

    System.out.println(
            "\nGagal mencetak ticket!"
    );

    return false;
}
}