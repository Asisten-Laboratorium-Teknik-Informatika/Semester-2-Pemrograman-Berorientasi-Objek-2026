package repository;

import config.Koneksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminRepository {

    /*
     * =========================
     * TAMPIL REFUND
     * =========================
     */
    public void tampilRefund() {

        String sql = """
            SELECT
                r.refund_id,
                e.event_name,
                u.name,
                r.amount,
                r.reason,
                r.status,
                r.rejection_note,
                r.process_at
            FROM event_platform.refund r

            JOIN event_platform.payments p
                ON r.payment_id = p.payment_id

            JOIN event_platform.bookings b
                ON p.booking_id = b.booking_id

            JOIN event_platform.users u
                ON b.user_id = u.user_id

            JOIN event_platform.events e
                ON b.event_id = e.event_id

            ORDER BY r.refund_id ASC
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
                    "\n===== DATA REFUND ====="
            );

            boolean ada = false;

            while (rs.next()) {

                ada = true;

                System.out.println(
                        "\nRefund ID : "
                        + rs.getInt("refund_id")
                );

                System.out.println(
                        "User      : "
                        + rs.getString("name")
                );

                System.out.println(
                        "Event     : "
                        + rs.getString("event_name")
                );

                System.out.println(
                        "Amount    : Rp"
                        + rs.getDouble("amount")
                );

                System.out.println(
                        "Reason    : "
                        + rs.getString("reason")
                );

                System.out.println(
                        "Status    : "
                        + rs.getString("status")
                );

                System.out.println(
                        "Reject    : "
                        + rs.getString(
                                "rejection_note"
                        )
                );

                System.out.println(
                        "Process At: "
                        + rs.getTimestamp(
                                "process_at"
                        )
                );
            }

            if (!ada) {

                System.out.println(
                        "\nBelum ada refund."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /*
     * =========================
     * APPROVE REFUND
     * =========================
     */
    public boolean approveRefund(
            int refundId,
            int adminId
    ) {

        Connection conn = null;

        try {

            conn =
                    Koneksi.getKoneksi();

            conn.setAutoCommit(false);

            /*
             * AMBIL DATA PAYMENT
             */

            String getSql = """
                SELECT
                    r.payment_id,
                    p.booking_id
                FROM event_platform.refund r

                JOIN event_platform.payments p
                    ON r.payment_id = p.payment_id

                WHERE r.refund_id = ?
            """;

            PreparedStatement getPs =
                    conn.prepareStatement(getSql);

            getPs.setInt(
                    1,
                    refundId
            );

            ResultSet rs =
                    getPs.executeQuery();

            if (!rs.next()) {

                System.out.println(
                        "\nRefund tidak ditemukan!"
                );

                return false;
            }

            int paymentId =
                    rs.getInt("payment_id");

            int bookingId =
                    rs.getInt("booking_id");

            /*
             * UPDATE REFUND
             */

            String refundSql = """
                UPDATE event_platform.refund
                SET
                    status = CAST(? AS refund_status),
                    process_at = now(),
                    processed_by = ?
                WHERE refund_id = ?
            """;

            PreparedStatement refundPs =
                    conn.prepareStatement(refundSql);

            refundPs.setString(
                    1,
                    "approved"
            );

            refundPs.setInt(
                    2,
                    adminId
            );

            refundPs.setInt(
                    3,
                    refundId
            );

            refundPs.executeUpdate();

            /*
             * UPDATE PAYMENT
             */

            String paymentSql = """
                UPDATE event_platform.payments
                SET status =
                    CAST(? AS payment_status)
                WHERE payment_id = ?
            """;

            PreparedStatement paymentPs =
                    conn.prepareStatement(paymentSql);

            paymentPs.setString(
                    1,
                    "refunded"
            );

            paymentPs.setInt(
                    2,
                    paymentId
            );

            paymentPs.executeUpdate();

            /*
             * UPDATE BOOKING
             */

            String bookingSql = """
                UPDATE event_platform.bookings
                SET status =
                    CAST(? AS booking_status)
                WHERE booking_id = ?
            """;

            PreparedStatement bookingPs =
                    conn.prepareStatement(bookingSql);

            bookingPs.setString(
                    1,
                    "cancelled"
            );

            bookingPs.setInt(
                    2,
                    bookingId
            );

            bookingPs.executeUpdate();

            /*
             * CANCEL TICKET
             */

            String ticketSql = """
                UPDATE event_platform.issued_tickets
                SET status =
                    CAST(? AS issued_status)
                WHERE booking_id = ?
            """;

            PreparedStatement ticketPs =
                    conn.prepareStatement(ticketSql);

            ticketPs.setString(
                    1,
                    "cancelled"
            );

            ticketPs.setInt(
                    2,
                    bookingId
            );

            ticketPs.executeUpdate();

            conn.commit();

            System.out.println(
                    "\nRefund berhasil diapprove!"
            );

            return true;

        } catch (Exception e) {

            try {

                if (conn != null) {

                    conn.rollback();
                }

            } catch (Exception ex) {

                ex.printStackTrace();
            }

            e.printStackTrace();
        }

        return false;
    }

    /*
     * =========================
     * REJECT REFUND
     * =========================
     */
    public boolean rejectRefund(
            int refundId,
            String alasan,
            int adminId
    ) {

        String sql = """
            UPDATE event_platform.refund
            SET
                status = CAST(? AS refund_status),
                process_at = now(),
                rejection_note = ?,
                processed_by = ?
            WHERE refund_id = ?
        """;

        try (

                Connection conn =
                        Koneksi.getKoneksi();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setString(
                    1,
                    "rejected"
            );

            ps.setString(
                    2,
                    alasan
            );

            ps.setInt(
                    3,
                    adminId
            );

            ps.setInt(
                    4,
                    refundId
            );

            int hasil =
                    ps.executeUpdate();

            if (hasil > 0) {

                System.out.println(
                        "\nRefund berhasil ditolak!"
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
     * DATA USER
     * =========================
     */
    public void tampilUser() {

        String sql = """
            SELECT
                user_id,
                name,
                email,
                role
            FROM event_platform.users
            ORDER BY user_id ASC
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
                    "\n===== DATA USER ====="
            );

            while (rs.next()) {

                System.out.println(
                        "\nID    : "
                        + rs.getInt("user_id")
                );

                System.out.println(
                        "Nama  : "
                        + rs.getString("name")
                );

                System.out.println(
                        "Email : "
                        + rs.getString("email")
                );

                System.out.println(
                        "Role  : "
                        + rs.getString("role")
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /*
     * =========================
     * DATA ORGANIZER
     * =========================
     */
    public void tampilOrganizer() {

        String sql = """
            SELECT
                o.organizer_id,
                o.organizer_name,
                o.status,
                u.email
            FROM event_platform.organizers o

            JOIN event_platform.users u
                ON o.user_id = u.user_id

            ORDER BY o.organizer_id ASC
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
                    "\n===== DATA ORGANIZER ====="
            );

            while (rs.next()) {

                System.out.println(
                        "\nOrganizer ID : "
                        + rs.getInt("organizer_id")
                );

                System.out.println(
                        "Nama         : "
                        + rs.getString(
                                "organizer_name"
                        )
                );

                System.out.println(
                        "Email        : "
                        + rs.getString("email")
                );

                System.out.println(
                        "Status       : "
                        + rs.getString("status")
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /*
     * =========================
     * MONITORING PAYMENT
     * =========================
     */
    public void tampilMonitoringPayment() {

        String sql = """
            SELECT
                p.payment_id,
                u.name,
                e.event_name,
                p.amount,
                p.payment_method,
                p.status,
                p.created_at
            FROM event_platform.payments p

            JOIN event_platform.bookings b
                ON p.booking_id = b.booking_id

            JOIN event_platform.users u
                ON b.user_id = u.user_id

            JOIN event_platform.events e
                ON b.event_id = e.event_id

            ORDER BY p.payment_id DESC
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
                    "\n===== MONITORING PAYMENT ====="
            );

            boolean ada = false;

            while (rs.next()) {

                ada = true;

                System.out.println(
                        "\nPayment ID : "
                        + rs.getInt("payment_id")
                );

                System.out.println(
                        "User       : "
                        + rs.getString("name")
                );

                System.out.println(
                        "Event      : "
                        + rs.getString("event_name")
                );

                System.out.println(
                        "Amount     : Rp"
                        + rs.getDouble("amount")
                );

                System.out.println(
                        "Method     : "
                        + rs.getString(
                                "payment_method"
                        )
                );

                System.out.println(
                        "Status     : "
                        + rs.getString("status")
                );

                System.out.println(
                        "Created At : "
                        + rs.getTimestamp(
                                "created_at"
                        )
                );
            }

            if (!ada) {

                System.out.println(
                        "\nBelum ada payment."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}