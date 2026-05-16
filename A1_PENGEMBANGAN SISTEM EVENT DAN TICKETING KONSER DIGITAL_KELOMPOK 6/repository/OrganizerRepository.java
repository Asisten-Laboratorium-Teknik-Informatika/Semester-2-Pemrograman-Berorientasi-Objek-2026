package repository;

import config.Koneksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OrganizerRepository {

    public int getOrganizerIdByUserId(
            int userId
    ) {

        String sql = """
            SELECT organizer_id
            FROM event_platform.organizers
            WHERE user_id = ?
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

            if (rs.next()) {

                return rs.getInt(
                        "organizer_id"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return 0;
    }
}