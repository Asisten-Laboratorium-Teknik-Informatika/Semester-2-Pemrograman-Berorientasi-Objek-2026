package repository;

import config.Koneksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OrganizerStaffRepository {

    /*
     * =========================
     * TAMBAH STAFF
     * =========================
     */
    public boolean tambahStaff(

            int userId,
            int eventId,
            String role,
            String area

    ) {

        String sql = """
            INSERT INTO event_platform.event_staff
            (
                user_id,
                event_id,
                role,
                area
            )
            VALUES
            (
                ?,
                ?,
                CAST(? AS staff_role),
                ?
            )
        """;

        try (

                Connection conn =
                        Koneksi.getKoneksi();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(1, userId);
            ps.setInt(2, eventId);
            ps.setString(3, role);
            ps.setString(4, area);

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
     * LIHAT STAFF
     * =========================
     */
    public void tampilStaff(
            int organizerId
    ) {

        String sql = """
            SELECT
                es.staff_id,
                u.name,
                e.event_name,
                es.role,
                es.area
            FROM event_platform.event_staff es

            JOIN event_platform.users u
                ON es.user_id = u.user_id

            JOIN event_platform.events e
                ON es.event_id = e.event_id

            WHERE e.organizer_id = ?

            ORDER BY es.staff_id ASC
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
                    "\n===== DATA STAFF EVENT ====="
            );

            boolean ada = false;

            while (rs.next()) {

                ada = true;

                System.out.println(
                        "\nStaff ID : "
                        + rs.getInt("staff_id")
                );

                System.out.println(
                        "Nama     : "
                        + rs.getString("name")
                );

                System.out.println(
                        "Event    : "
                        + rs.getString("event_name")
                );

                System.out.println(
                        "Role     : "
                        + rs.getString("role")
                );

                System.out.println(
                        "Area     : "
                        + rs.getString("area")
                );
            }

            if (!ada) {

                System.out.println(
                        "\nBelum ada staff event."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /*
     * =========================
     * HAPUS STAFF
     * =========================
     */
    public boolean hapusStaff(
            int staffId
    ) {

        String sql = """
            DELETE FROM event_platform.event_staff
            WHERE staff_id = ?
        """;

        try (

                Connection conn =
                        Koneksi.getKoneksi();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(1, staffId);

            int hasil =
                    ps.executeUpdate();

            return hasil > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }
}