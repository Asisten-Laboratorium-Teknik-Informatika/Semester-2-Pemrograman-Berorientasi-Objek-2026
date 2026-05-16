package repository;

import config.Koneksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TicketBenefitRepository {

    /*
     * =========================
     * TAMBAH BENEFIT
     * =========================
     */
    public boolean tambahBenefit(

            String nama,
            String deskripsi,
            String kategori

    ) {

        String sql = """
            INSERT INTO
            event_platform.ticket_benefits
            (
                name,
                description,
                category
            )
            VALUES
            (
                ?,
                ?,
                CAST(? AS ticket_benefit)
            )
        """;

        try (

                Connection conn =
                        Koneksi.getKoneksi();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setString(1, nama);

            ps.setString(2, deskripsi);

            ps.setString(3, kategori);

            int hasil =
                    ps.executeUpdate();

            if (hasil > 0) {

                System.out.println(
                        "\nBenefit berhasil ditambahkan!"
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
     * TAMPIL BENEFIT
     * =========================
     */
    public void tampilBenefit() {

        String sql = """
            SELECT *
            FROM event_platform.ticket_benefits
            ORDER BY benefit_id
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
                    "\n===== DATA BENEFIT ====="
            );

            while (rs.next()) {

                System.out.println(
                        "\nBenefit ID : "
                        + rs.getInt(
                                "benefit_id"
                        )
                );

                System.out.println(
                        "Nama       : "
                        + rs.getString(
                                "name"
                        )
                );

                System.out.println(
                        "Category   : "
                        + rs.getString(
                                "category"
                        )
                );

                System.out.println(
                        "Description: "
                        + rs.getString(
                                "description"
                        )
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /*
     * =========================
     * MAP BENEFIT KE TICKET
     * =========================
     */
    public boolean tambahBenefitKeTicket(

            int ticketId,
            int benefitId

    ) {

        String sql = """
            INSERT INTO
            event_platform.ticket_benefit_map
            (
                ticket_id,
                benefit_id
            )
            VALUES
            (
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

            ps.setInt(1, ticketId);

            ps.setInt(2, benefitId);

            int hasil =
                    ps.executeUpdate();

            if (hasil > 0) {

                System.out.println(
                        "\nBenefit berhasil ditambahkan ke ticket!"
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
     * LIHAT BENEFIT TICKET
     * =========================
     */
    public void tampilBenefitTicket(
            int ticketId
    ) {

        String sql = """
            SELECT
                tb.name,
                tb.category,
                tb.description
            FROM event_platform.ticket_benefit_map tm

            JOIN event_platform.ticket_benefits tb
                ON tm.benefit_id =
                   tb.benefit_id

            WHERE tm.ticket_id = ?
        """;

        try (

                Connection conn =
                        Koneksi.getKoneksi();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(1, ticketId);

            ResultSet rs =
                    ps.executeQuery();

            System.out.println(
                    "\n===== BENEFIT TICKET ====="
            );

            boolean ada = false;

            while (rs.next()) {

                ada = true;

                System.out.println(
                        "\nNama : "
                        + rs.getString(
                                "name"
                        )
                );

                System.out.println(
                        "Category : "
                        + rs.getString(
                                "category"
                        )
                );

                System.out.println(
                        "Description : "
                        + rs.getString(
                                "description"
                        )
                );
            }

            if (!ada) {

                System.out.println(
                        "\nBelum ada benefit."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}