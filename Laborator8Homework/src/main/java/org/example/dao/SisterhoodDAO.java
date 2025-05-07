package org.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class SisterhoodDAO {

    public void insert(int cityId1, int cityId2) {
        if (cityId1 == cityId2) return;
        if (cityId1 > cityId2) {
            int temp = cityId1;
            cityId1 = cityId2;
            cityId2 = temp;
        }

        String sql = "INSERT INTO sister_cities (city1_id, city2_id) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cityId1);
            stmt.setInt(2, cityId2);
            stmt.executeUpdate();
            System.out.printf("Sisterhood added: %d ↔ %d%n", cityId1, cityId2);
        } catch (SQLException e) {
            if (!e.getMessage().contains("unique constraint")) {
                System.err.println("Eroare la inserare sisterhood: " + e.getMessage());
            }
        }
    }

    public Set<Pair> generateRandomSisterhoods(List<City> cities, double probability) {
        Set<Pair> pairs = new HashSet<>();
        Random rand = new Random();

        for (int i = 0; i < cities.size(); i++) {
            for (int j = i + 1; j < cities.size(); j++) {
                if (rand.nextDouble() < probability) {
                    int id1 = cities.get(i).getId();
                    int id2 = cities.get(j).getId();
                    Pair pair = new Pair(id1, id2);
                    if (pairs.add(pair)) {
                        insert(id1, id2);
                    }
                }
            }
        }
        return pairs;
    }

    public static class Pair {
        private final int city1Id;
        private final int city2Id;

        public Pair(int city1Id, int city2Id) {
            if (city1Id < city2Id) {
                this.city1Id = city1Id;
                this.city2Id = city2Id;
            } else {
                this.city1Id = city2Id;
                this.city2Id = city1Id;
            }
        }

        public int getCity1Id() {
            return city1Id;
        }

        public int getCity2Id() {
            return city2Id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(city1Id, city2Id);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Pair)) return false;
            Pair p = (Pair) o;
            return city1Id == p.city1Id && city2Id == p.city2Id;
        }

        @Override
        public String toString() {
            return "(" + city1Id + ", " + city2Id + ")";
        }
    }
}
