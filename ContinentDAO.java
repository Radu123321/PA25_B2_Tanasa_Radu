package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContinentDAO {
    public void create(String name) {
        try {
            Connection conn = Database.getConnection();

            PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM continents WHERE name = ?");
            checkStmt.setString(1, name);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Continentul '" + name + "' deja există în baza de date.");
                return; // Dacă există deja, nu mai inserăm
            }
            PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO continents (id, name) VALUES (seq_continent_id.NEXTVAL, ?)");
            insertStmt.setString(1, name);
            insertStmt.executeUpdate();

            System.out.println("Continentul '" + name + "' a fost inserat cu succes.");

        } catch (SQLException e) {
            System.err.println("Eroare la inserare continent: " + e.getMessage());
        }
    }

    public void findByName(String name) {
        try {
            Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT id FROM continents WHERE name = ?");
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                System.out.println("Continent găsit: ID = " + id);
            } else {
                System.out.println("Continentul '" + name + "' nu a fost găsit.");
            }
        } catch (SQLException e) {
            System.err.println("Eroare la căutare continent: " + e.getMessage());
        }
    }
}
