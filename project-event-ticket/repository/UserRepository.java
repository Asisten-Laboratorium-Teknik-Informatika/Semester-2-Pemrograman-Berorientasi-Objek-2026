package repository;

import config.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.User;

public class UserRepository {

    public boolean registerUser(
            String nama,
            String email,
            String password,
            String phone
    ) {

        String sql = """
            INSERT INTO event_platform.users
            (
                name,
                email,
                password_hash,
                phone,
                role
            )
            VALUES
            (
                ?,
                ?,
                ?,
                ?,
                CAST(? AS user_role)
            )
        """;

        try (

            Connection conn = Koneksi.getKoneksi();

            PreparedStatement ps
            = conn.prepareStatement(sql)

        ) {

            ps.setString(1, nama);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, phone);
            ps.setString(5, "customer");

            int hasil = ps.executeUpdate();

            return hasil > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    public User login(
            String email,
            String password
    ) {

        User user = null;

        String sql = """
            SELECT
                user_id,
                name,
                email,
                phone,
                role
            FROM event_platform.users
            WHERE email = ?
            AND password_hash = ?
        """;

        try (

            Connection conn = Koneksi.getKoneksi();

            PreparedStatement ps
            = conn.prepareStatement(sql)

        ) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                user = new User();

                user.setUserId(
                        rs.getInt("user_id")
                );

                user.setNama(
                        rs.getString("name")
                );

                user.setEmail(
                        rs.getString("email")
                );

                user.setPhone(
                        rs.getString("phone")
                );

                user.setRole(
                        rs.getString("role")
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return user;
    }
}