// src/ContinentDAO.java
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContinentDAO {
    private final Connection connection;

    public ContinentDAO() {
        this.connection = Database.getConnection();
    }

    public void create(String name) {
        String sql = "INSERT INTO continents (id, name) VALUES (seq_continent_id.NEXTVAL, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
            System.out.println("Continent inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void findById(int id) {
        String sql = "SELECT * FROM continents WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Continent found: " + rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void findByName(String name) {
        String sql = "SELECT * FROM continents WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Continent found: ID = " + rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
