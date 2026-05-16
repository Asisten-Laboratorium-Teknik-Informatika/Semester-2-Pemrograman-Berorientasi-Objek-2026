package repository;

import config.Koneksi;
import util.Tampilan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BookingRepository {

    TicketRepository ticketRepo =
            new TicketRepository();

    DiscountRepository discountRepo =
            new DiscountRepository();

    public boolean beliTicket(

            int userId,
            int eventId,
            int ticketId,
            int jumlah,
            String discountCode

    ) {

        Connection conn = null;

        try {

            conn =
                    Koneksi.getKoneksi();

            conn.setAutoCommit(false);

            ResultSet rs =
                    ticketRepo.cariTicket(
                            conn,
                            ticketId
                    );

            if (!rs.next()) {

                Tampilan.gagal(
        "Ticket tidak ditemukan"
);

                conn.rollback();

                return false;
            }

            int quota =
                    rs.getInt("total_quota");

            double harga =
                    rs.getDouble("price");

            String status =
                    rs.getString("status");

            if (status.equalsIgnoreCase("sold_out")) {

                Tampilan.gagal(
        "Ticket sold out"
);

                conn.rollback();

                return false;
            }

            if (quota < jumlah) {

                Tampilan.gagal(
        "Quota ticket tidak cukup"
);

                Tampilan.info(
        "Silahkan masuk waitlist"
);

                conn.rollback();

                return false;
            }

            double subtotal =
                    harga * jumlah;

            double discountAmount =
                    0;

            if (
                    discountCode != null
                    && !discountCode.trim().isEmpty()
            ) {

                ResultSet discountRs =
                        discountRepo.cariDiscount(
                                conn,
                                discountCode.trim()
                        );

                if (
                        discountRs != null
                        && discountRs.next()
                ) {

                    String type =
                            discountRs.getString(
                                    "discount_type"
                            );

                    double value =
                            discountRs.getDouble(
                                    "discount_value"
                            );

                    double minPurchase =
                            discountRs.getDouble(
                                    "min_purchase"
                            );

                    if (subtotal >= minPurchase) {

                        if (
                                type.equalsIgnoreCase(
                                        "percentage"
                                )
                        ) {

                            discountAmount =
                                    subtotal
                                    * value
                                    / 100;

                        } else if (
                                type.equalsIgnoreCase(
                                        "fixed"
                                )
                        ) {

                            discountAmount =
                                    value;
                        }

                        if (discountAmount > subtotal) {

                            discountAmount =
                                    subtotal;
                        }

                        Tampilan.sukses(
        "Discount digunakan : "
        + discountCode
);

                    } else {

                        System.out.println(
                                "\nMinimum pembelian tidak memenuhi discount!"
                        );

                        discountAmount =
                                0;
                    }

                } else {

                    Tampilan.gagal(
        "Kode discount tidak valid"
);

                    discountAmount =
                            0;
                }
            }

            double total =
                    subtotal - discountAmount;

            if (total < 0) {

                total =
                        0;
            }

            String bookingSql = """
                INSERT INTO
                    event_platform.bookings
                (
                    user_id,
                    event_id,
                    total_amount,
                    discount_amount,
                    status
                )
                VALUES
                (
                    ?,
                    ?,
                    ?,
                    ?,
                    'pending'
                )
            """;

            PreparedStatement bookingPs =
                    conn.prepareStatement(
                            bookingSql,
                            PreparedStatement.RETURN_GENERATED_KEYS
                    );

            bookingPs.setInt(1, userId);
            bookingPs.setInt(2, eventId);
            bookingPs.setDouble(3, total);
            bookingPs.setDouble(4, discountAmount);

            bookingPs.executeUpdate();

            ResultSet bookingRs =
                    bookingPs.getGeneratedKeys();

            int bookingId = 0;

            if (bookingRs.next()) {

                bookingId =
                        bookingRs.getInt(1);
            }

            String detailSql = """
                INSERT INTO
                    event_platform.booking_details
                (
                    booking_id,
                    ticket_id,
                    quantity,
                    price_snapshot
                )
                VALUES
                (
                    ?,
                    ?,
                    ?,
                    ?
                )
            """;

            PreparedStatement detailPs =
                    conn.prepareStatement(
                            detailSql
                    );

            detailPs.setInt(1, bookingId);
            detailPs.setInt(2, ticketId);
            detailPs.setInt(3, jumlah);
            detailPs.setDouble(4, harga);

            detailPs.executeUpdate();

            String paymentSql = """
                INSERT INTO
                    event_platform.payments
                (
                    booking_id,
                    amount,
                    status,
                    payment_method
                )
                VALUES
                (
                    ?,
                    ?,
                    'pending',
                    ?
                )
            """;

            PreparedStatement paymentPs =
                    conn.prepareStatement(
                            paymentSql
                    );

            paymentPs.setInt(1, bookingId);
            paymentPs.setDouble(2, total);
            paymentPs.setString(3, "belum_dipilih");

            paymentPs.executeUpdate();

            ticketRepo.updateQuota(
                    conn,
                    ticketId,
                    jumlah
            );

            conn.commit();

            Tampilan.judul(
        "PEMBELIAN BERHASIL"
);

            System.out.println(
                    "Harga Ticket : Rp "
                    + harga
            );

            System.out.println(
                    "Jumlah       : "
                    + jumlah
            );

            System.out.println(
                    "Subtotal     : Rp "
                    + subtotal
            );

            System.out.println(
                    "Discount     : Rp "
                    + discountAmount
            );

            System.out.println(
                    "Total Bayar  : Rp "
                    + total
            );

            return true;

        } catch (Exception e) {

            try {

                if (conn != null) {

                    conn.rollback();
                }

            } catch (Exception rollbackError) {

                rollbackError.printStackTrace();
            }

            e.printStackTrace();

        } finally {

            try {

                if (conn != null) {

                    conn.setAutoCommit(true);
                    conn.close();
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        return false;
    }
}