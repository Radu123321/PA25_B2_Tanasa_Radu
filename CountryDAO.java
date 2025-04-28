package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryDAO {
    public void create(String name, String code, int continentId) {
        try {
            Connection conn = Database.getConnection();

            PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM countries WHERE name = ? OR code = ?");
            checkStmt.setString(1, name);
            checkStmt.setString(2, code);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Țara '" + name + "' sau codul '" + code + "' există deja în baza de date.");
                return; // Dacă există deja, nu mai inserez
            }

            PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO countries (id, name, code, continent_id) VALUES (seq_country_id.NEXTVAL, ?, ?, ?)");
            insertStmt.setString(1, name);
            insertStmt.setString(2, code);
            insertStmt.setInt(3, continentId);
            insertStmt.executeUpdate();

            System.out.println("Țara '" + name + "' a fost inserată cu succes.");

        } catch (SQLException e) {
            System.err.println("Eroare la inserare țară: " + e.getMessage());
        }
    }

    public void findByName(String name) {
        try {
            Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT code FROM countries WHERE name = ?");
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String code = rs.getString(1);
                System.out.println("Țara găsită: Cod = " + code);
            } else {
                System.out.println("Țara '" + name + "' nu a fost găsită.");
            }
        } catch (SQLException e) {
            System.err.println("Eroare la căutare țară: " + e.getMessage());
        }
    }
}