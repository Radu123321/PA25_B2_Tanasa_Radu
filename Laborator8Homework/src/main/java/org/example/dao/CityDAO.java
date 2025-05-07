package org.example.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CityDAO {

    public void insert(City city) {
        if (findByName(city.getName()) != null) {
            System.out.println("Orașul '" + city.getName() + "' există deja. Se ignoră.");
            return;
        }

        String sql = """
                INSERT INTO cities (id, name, is_capital, latitude, longitude, country_id)
                VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, city.getId());
            stmt.setString(2, city.getName());
            stmt.setBoolean(3, city.isCapital());
            stmt.setDouble(4, city.getLatitude());
            stmt.setDouble(5, city.getLongitude());
            stmt.setInt(6, city.getCountryId());
            stmt.executeUpdate();
            System.out.println("Oraș inserat: " + city.getName());
        } catch (SQLException e) {
            System.err.println("Eroare la inserare oraș: " + e.getMessage());
        }
    }

    public City findByName(String name) {
        String sql = "SELECT * FROM cities WHERE name = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new City(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getBoolean("is_capital"),
                            rs.getDouble("latitude"),
                            rs.getDouble("longitude"),
                            rs.getInt("country_id")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Eroare la căutare oraș: " + e.getMessage());
        }
        return null;
    }

    public List<City> getAll() {
        List<City> cities = new ArrayList<>();
        String sql = "SELECT * FROM cities";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                City city = new City(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getBoolean("is_capital"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude"),
                        rs.getInt("country_id")
                );
                cities.add(city);
            }
        } catch (SQLException e) {
            System.err.println("Eroare la extragere orașe: " + e.getMessage());
        }

        return cities;
    }

    public static double calculateDistance(City a, City b) {
        final int R = 6371;
        double latDistance = Math.toRadians(b.getLatitude() - a.getLatitude());
        double lonDistance = Math.toRadians(b.getLongitude() - a.getLongitude());
        double lat1 = Math.toRadians(a.getLatitude());
        double lat2 = Math.toRadians(b.getLatitude());

        double aCalc = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(aCalc), Math.sqrt(1 - aCalc));
        return R * c;
    }
}
