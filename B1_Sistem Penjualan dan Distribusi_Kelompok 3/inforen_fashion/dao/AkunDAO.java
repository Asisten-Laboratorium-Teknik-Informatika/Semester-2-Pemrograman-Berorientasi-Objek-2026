package dao;

import model.*;
import java.sql.*;

public class AkunDAO {
    private Connection connection;

    public AkunDAO(Connection connection) {
        this.connection = connection;
    }

    public Akun login(String username, String password) {
        String query = "SELECT * FROM akun WHERE username = ? AND password = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                String role = rs.getString("role");
                int id = rs.getInt("id_akun");
                String nama = rs.getString("nama");
                String email = rs.getString("email");

                if (role.equalsIgnoreCase("PELANGGAN")) {
                    return new Pelanggan(id, username, password, nama, email);
                } else if (role.equalsIgnoreCase("PENJUAL")) {
                    return new Penjual(id, username, password, nama, email);
                } else if (role.equalsIgnoreCase("SUPPLIER")) {
                    return new Supplier(id, username, password, nama, email);
                }
            }
        } catch (SQLException e) {
            System.out.println("Gagal login: " + e.getMessage());
        }
        return null; 
    }

    public boolean daftar(String username, String password, String nama, String email, String role) {
        String query = "INSERT INTO akun (username, password, nama, email, role) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, nama);
            ps.setString(4, email);
            ps.setString(5, role.toUpperCase());
            
            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Gagal daftar akun: " + e.getMessage());
            return false;
        }
    }
}