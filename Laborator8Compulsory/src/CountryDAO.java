// src/CountryDAO.java
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryDAO {
    private final Connection connection;

    public CountryDAO() {
        this.connection = Database.getConnection();
    }

    public void create(String name, String code, int continentId) {
        String sql = "INSERT INTO countries (id, name, code, continent_id) VALUES (seq_country_id.NEXTVAL, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, code);
            stmt.setInt(3, continentId);
            stmt.executeUpdate();
            System.out.println("Country inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void findById(int id) {
        String sql = "SELECT * FROM countries WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Country found: " + rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void findByName(String name) {
        String sql = "SELECT * FROM countries WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Country found: Code = " + rs.getString("code"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
