package repository;

import config.Koneksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SponsorBenefitRepository {

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
            event_platform.sponsor_benefits
            (
                name,
                description,
                category
            )
            VALUES
            (
                ?,
                ?,
                CAST(? AS sponsor_benefits)
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
                        "\nBenefit sponsor berhasil ditambahkan!"
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
     * LIHAT BENEFIT
     * =========================
     */
    public void tampilBenefit() {

        String sql = """
            SELECT *
            FROM event_platform.sponsor_benefits
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
                    "\n===== BENEFIT SPONSOR ====="
            );

            boolean ada = false;

            while (rs.next()) {

                ada = true;

                System.out.println(
                        "\nBenefit ID : "
                        + rs.getInt(
                                "benefit_id"
                        )
                );

                System.out.println(
                        "Nama : "
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
                        "\nBelum ada benefit sponsor."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /*
     * =========================
     * TAMBAH BENEFIT KE TIER
     * =========================
     */
    public boolean tambahBenefitTier(

            int tierId,
            int benefitId

    ) {

        /*
         * VALIDASI DUPLIKAT
         */
        String cek = """
            SELECT *
            FROM event_platform.sponsor_tier_benefit
            WHERE tier_id = ?
            AND benefit_id = ?
        """;

        /*
         * INSERT
         */
        String sql = """
            INSERT INTO
            event_platform.sponsor_tier_benefit
            (
                tier_id,
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
                        Koneksi.getKoneksi()

        ) {

            /*
             * =========================
             * CEK DUPLIKAT
             * =========================
             */
            PreparedStatement psCek =
                    conn.prepareStatement(cek);

            psCek.setInt(1, tierId);

            psCek.setInt(2, benefitId);

            ResultSet rs =
                    psCek.executeQuery();

            if (rs.next()) {

                System.out.println(
                        "\nBenefit sudah ada pada tier ini!"
                );

                return false;
            }

            /*
             * =========================
             * INSERT BENEFIT
             * =========================
             */
            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1, tierId);

            ps.setInt(2, benefitId);

            int hasil =
                    ps.executeUpdate();

            if (hasil > 0) {

                System.out.println(
                        "\nBenefit berhasil ditambahkan ke tier sponsor!"
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
     * LIHAT BENEFIT TIER
     * =========================
     */
    public void tampilBenefitTier(
            int tierId
    ) {

        String sql = """
            SELECT
                sb.name,
                sb.category,
                sb.description
            FROM
                event_platform.sponsor_tier_benefit stb

            JOIN
                event_platform.sponsor_benefits sb
                    ON stb.benefit_id =
                       sb.benefit_id

            WHERE stb.tier_id = ?
        """;

        try (

                Connection conn =
                        Koneksi.getKoneksi();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(1, tierId);

            ResultSet rs =
                    ps.executeQuery();

            System.out.println(
                    "\n===== BENEFIT TIER SPONSOR ====="
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
                        "\nBelum ada benefit pada tier ini."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}