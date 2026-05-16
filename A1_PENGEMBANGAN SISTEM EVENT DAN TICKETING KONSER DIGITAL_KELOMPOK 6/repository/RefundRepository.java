package repository;

import config.Koneksi;
import util.Tampilan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RefundRepository {

    public boolean requestRefund(
            int paymentId,
            String alasan
    ) {

        String cekPaymentSql = """
            SELECT
                payment_id,
                amount,
                status
            FROM event_platform.payments
            WHERE payment_id = ?
        """;

        String cekRefundSql = """
            SELECT refund_id
            FROM event_platform.refund
            WHERE payment_id = ?
        """;

        String insertSql = """
            INSERT INTO event_platform.refund
            (
                payment_id,
                amount,
                reason,
                status
            )
            VALUES
            (
                ?,
                ?,
                ?,
                'requested'
            )
        """;

        try (
                Connection conn =
                        Koneksi.getKoneksi()
        ) {

            PreparedStatement cekPaymentPs =
                    conn.prepareStatement(cekPaymentSql);

            cekPaymentPs.setInt(1, paymentId);

            ResultSet paymentRs =
                    cekPaymentPs.executeQuery();

            if (!paymentRs.next()) {

                System.out.println(
                        "\nPayment tidak ditemukan!"
                );

                return false;
            }

            double amount =
                    paymentRs.getDouble("amount");

            String paymentStatus =
                    paymentRs.getString("status");

            if (!paymentStatus.equalsIgnoreCase("paid")) {

                System.out.println(
                        "\nRefund hanya bisa diajukan untuk payment yang sudah paid!"
                );

                return false;
            }

            if (amount <= 0) {

                System.out.println(
                        "\nNominal refund tidak valid!"
                );

                return false;
            }

            PreparedStatement cekRefundPs =
                    conn.prepareStatement(cekRefundSql);

            cekRefundPs.setInt(1, paymentId);

            ResultSet refundRs =
                    cekRefundPs.executeQuery();

            if (refundRs.next()) {

                System.out.println(
                        "\nRefund untuk payment ini sudah pernah diajukan!"
                );

                return false;
            }

            PreparedStatement insertPs =
                    conn.prepareStatement(insertSql);

            insertPs.setInt(1, paymentId);
            insertPs.setDouble(2, amount);
            insertPs.setString(3, alasan);

            int hasil =
                    insertPs.executeUpdate();

            if (hasil > 0) {

                Tampilan.sukses(
        "Refund berhasil diajukan"
);

                return true;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        System.out.println(
                "\nGagal mengajukan refund!"
        );

        return false;
    }

    public void tampilDataRefund() {

        String sql = """
            SELECT
                r.refund_id,
                r.payment_id,
                u.name,
                r.amount,
                r.reason,
                r.status,
                r.requested_at,
                r.process_at,
                r.rejection_note
            FROM event_platform.refund r
            JOIN event_platform.payments p
                ON r.payment_id = p.payment_id
            JOIN event_platform.bookings b
                ON p.booking_id = b.booking_id
            JOIN event_platform.users u
                ON b.user_id = u.user_id
            ORDER BY r.refund_id DESC
        """;

        try (
                Connection conn =
                        Koneksi.getKoneksi();

                PreparedStatement ps =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()
        ) {

            Tampilan.judul(
        "DATA REFUND"
);

            boolean ada = false;

            while (rs.next()) {

                ada = true;

                System.out.println(
                        "\nRefund ID : "
                        + rs.getInt("refund_id")
                );

                System.out.println(
                        "Payment ID : "
                        + rs.getInt("payment_id")
                );

                System.out.println(
                        "User       : "
                        + rs.getString("name")
                );

                System.out.println(
                        "Amount     : Rp "
                        + rs.getDouble("amount")
                );

                System.out.println(
                        "Reason     : "
                        + rs.getString("reason")
                );

                System.out.println(
                        "Status     : "
                        + Tampilan.status(
        rs.getString("status")
)
                );

                System.out.println(
                        "Requested  : "
                        + rs.getTimestamp("requested_at")
                );

                System.out.println(
                        "Processed  : "
                        + rs.getTimestamp("process_at")
                );

                System.out.println(
                        "Note       : "
                        + rs.getString("rejection_note")
                );
            }

            if (!ada) {

                System.out.println(
                        "\nBelum ada data refund."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void tampilRiwayatRefund(
            int userId
    ) {

        String sql = """
            SELECT
                r.refund_id,
                r.payment_id,
                r.amount,
                r.reason,
                r.status,
                r.requested_at,
                r.process_at,
                r.rejection_note
            FROM event_platform.refund r
            JOIN event_platform.payments p
                ON r.payment_id = p.payment_id
            JOIN event_platform.bookings b
                ON p.booking_id = b.booking_id
            WHERE b.user_id = ?
            ORDER BY r.refund_id DESC
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
                    "\n===== RIWAYAT REFUND ====="
            );

            boolean ada = false;

            while (rs.next()) {

                ada = true;

                System.out.println(
                        "\nRefund ID : "
                        + rs.getInt("refund_id")
                );

                System.out.println(
                        "Payment ID : "
                        + rs.getInt("payment_id")
                );

                System.out.println(
                        "Amount     : Rp "
                        + rs.getDouble("amount")
                );

                System.out.println(
                        "Reason     : "
                        + rs.getString("reason")
                );

                System.out.println(
                        "Status     : "
                        + rs.getString("status")
                );

                System.out.println(
                        "Requested  : "
                        + rs.getTimestamp("requested_at")
                );

                System.out.println(
                        "Processed  : "
                        + rs.getTimestamp("process_at")
                );

                System.out.println(
                        "Note       : "
                        + rs.getString("rejection_note")
                );
            }

            if (!ada) {

                System.out.println(
                        "\nBelum ada riwayat refund."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}