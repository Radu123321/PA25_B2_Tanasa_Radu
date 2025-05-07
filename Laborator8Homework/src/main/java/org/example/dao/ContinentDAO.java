package org.example.dao;

import java.sql.*;

public class ContinentDAO {

    public void create(String name) {
        String checkSql = "SELECT COUNT(*) FROM continents WHERE name = ?";
        String insertSql = "INSERT INTO continents (id, name) VALUES (seq_continent_id.NEXTVAL, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, name);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Continentul '" + name + "' deja există în baza de date.");
                    return;
                }
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, name);
                insertStmt.executeUpdate();
                System.out.println("Continentul '" + name + "' a fost inserat cu succes.");
            }

        } catch (SQLException e) {
            System.err.println("Eroare la inserare continent: " + e.getMessage());
        }
    }

    public void findByName(String name) {
        String sql = "SELECT id FROM continents WHERE name = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Continent găsit: ID = " + rs.getInt(1));
                } else {
                    System.out.println("Continentul '" + name + "' nu a fost găsit.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Eroare la căutare continent: " + e.getMessage());
        }
    }
}
