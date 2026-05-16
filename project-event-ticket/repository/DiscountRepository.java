package repository;

import config.Koneksi;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DiscountRepository {

        /*
     * =========================
     * CARI DISCOUNT
     * =========================
     */
    public ResultSet cariDiscount(
            Connection conn,
            String code
    ) {

        String sql = """
            SELECT *
            FROM event_platform.discounts
            WHERE code = ?
            AND status = 'active'
            AND CURRENT_DATE
                BETWEEN start_date
                AND end_date
        """;

        try {

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(1, code);

            return ps.executeQuery();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }
    
    /*
     * =========================
     * TAMBAH DISCOUNT
     * =========================
     */
    public boolean tambahDiscount(

            String code,
            int organizerId,
            int eventId,
            String type,
            double value,
            double minPurchase,
            int usageLimit,
            String startDate,
            String endDate

    ) {

        String sql = """
            INSERT INTO
            event_platform.discounts
            (
                code,
                organizer_id,
                event_id,
                discount_type,
                discount_value,
                min_purchase,
                usage_limit,
                status,
                start_date,
                end_date
            )
            VALUES
            (
                ?,
                ?,
                ?,
                CAST(? AS discount_type),
                ?,
                ?,
                ?,
                CAST(? AS discount_status),
                ?,
                ?
            )
        """;

        try (

                Connection conn =
                        Koneksi.getKoneksi();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setString(1, code);

            ps.setInt(2, organizerId);

            ps.setInt(3, eventId);

            ps.setString(4, type);

            ps.setDouble(5, value);

            ps.setDouble(6, minPurchase);

            ps.setInt(7, usageLimit);

            ps.setString(8, "active");

            ps.setDate(
                    9,
                    Date.valueOf(startDate)
            );

            ps.setDate(
                    10,
                    Date.valueOf(endDate)
            );

            int hasil =
                    ps.executeUpdate();

            return hasil > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    /*
     * =========================
     * TAMPIL DISCOUNT
     * =========================
     */
    public void tampilDiscount(
            int organizerId
    ) {

        String sql = """
            SELECT
                d.discount_id,
                e.event_name,
                d.code,
                d.discount_type,
                d.discount_value,
                d.min_purchase,
                d.usage_limit,
                d.status,
                d.start_date,
                d.end_date
            FROM event_platform.discounts d

            JOIN event_platform.events e
                ON d.event_id = e.event_id

            WHERE d.organizer_id = ?

            ORDER BY d.discount_id DESC
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
                    "\n===== DATA DISCOUNT ====="
            );

            boolean ada = false;

            while (rs.next()) {

                ada = true;

                System.out.println(
                        "\nDiscount ID : "
                        + rs.getInt("discount_id")
                );

                System.out.println(
                        "Event       : "
                        + rs.getString("event_name")
                );

                System.out.println(
                        "Code        : "
                        + rs.getString("code")
                );

                System.out.println(
                        "Type        : "
                        + rs.getString("discount_type")
                );

                System.out.println(
                        "Value       : "
                        + rs.getDouble("discount_value")
                );

                System.out.println(
                        "Min Purchase: Rp"
                        + rs.getDouble("min_purchase")
                );

                System.out.println(
                        "Usage Limit : "
                        + rs.getInt("usage_limit")
                );

                System.out.println(
                        "Status      : "
                        + rs.getString("status")
                );

                System.out.println(
                        "Start Date  : "
                        + rs.getDate("start_date")
                );

                System.out.println(
                        "End Date    : "
                        + rs.getDate("end_date")
                );
            }

            if (!ada) {

                System.out.println(
                        "\nBelum ada discount."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /*
     * =========================
     * HAPUS DISCOUNT
     * =========================
     */
    public boolean hapusDiscount(
            int discountId
    ) {

        String sql = """
            DELETE FROM
            event_platform.discounts
            WHERE discount_id = ?
        """;

        try (

                Connection conn =
                        Koneksi.getKoneksi();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(1, discountId);

            int hasil =
                    ps.executeUpdate();

            return hasil > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }
}