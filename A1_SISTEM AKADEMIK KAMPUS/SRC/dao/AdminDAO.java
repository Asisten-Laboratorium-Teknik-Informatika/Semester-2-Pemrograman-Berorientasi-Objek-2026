package dao;

import java.sql.*;
import util.DatabaseConnection;

public class AdminDAO {

    public String loginAdmin(String username, String password) {
        String sql = "SELECT nama_admin FROM kelompok4.admin WHERE username=? AND password=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("nama_admin");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}