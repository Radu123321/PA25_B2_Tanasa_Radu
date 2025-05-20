package org.example.dao;

import org.example.model.Country;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<Country> findAllCountries() {
        List<Country> countries = new ArrayList<>();
        String sql = "SELECT id, name, code, continent_id FROM countries";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Country country = new Country(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("code"),
                        rs.getInt("continent_id")
                );
                countries.add(country);
            }

        } catch (SQLException e) {
            System.err.println("Eroare la extragerea țărilor: " + e.getMessage());
        }

        return countries;
    }
}
