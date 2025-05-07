package org.example.dao;

import java.sql.*;

public class CountryDAO {

    public void create(String name, String code, int continentId) {
        String checkSql = "SELECT COUNT(*) FROM countries WHERE name = ? OR code = ?";
        String insertSql = "INSERT INTO countries (id, name, code, continent_id) VALUES (seq_country_id.NEXTVAL, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, name);
            checkStmt.setString(2, code);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Țara '" + name + "' sau codul '" + code + "' există deja în baza de date.");
                    return;
                }
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, name);
                insertStmt.setString(2, code);
                insertStmt.setInt(3, continentId);
                insertStmt.executeUpdate();
                System.out.println("Țara '" + name + "' a fost inserată cu succes.");
            }

        } catch (SQLException e) {
            System.err.println("Eroare la inserare țară: " + e.getMessage());
        }
    }

    public void findByName(String name) {
        String sql = "SELECT code FROM countries WHERE name = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Țara găsită: Cod = " + rs.getString(1));
                } else {
                    System.out.println("Țara '" + name + "' nu a fost găsită.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Eroare la căutare țară: " + e.getMessage());
        }
    }

    public int findIdByName(String name) {
        String sql = "SELECT id FROM countries WHERE name = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Eroare la căutare ID țară: " + e.getMessage());
        }
        return -1;
    }
}
