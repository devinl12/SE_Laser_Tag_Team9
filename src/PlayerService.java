import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerService {
    private String dbUrl = "jdbc:postgresql://localhost:5432/photon";
    private String user = "student";
    private String pass = "student";
    private int nextPlayerId = 1; // Start with the first player

    // Method to fetch all players from the database
    public List<String[]> getPlayers() {
        String query = "SELECT * FROM players;";
        List<String[]> players = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(dbUrl, user, pass);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String id = String.valueOf(rs.getInt("id"));
                String codename = rs.getString("codename");
                players.add(new String[]{id, codename});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return players;
    }

    // Method to fetch the next player based on the nextPlayerId
    public String[] getNextPlayer() {
        String query = "SELECT * FROM players WHERE id = ?;";
        String[] player = null;

        try (Connection connection = DriverManager.getConnection(dbUrl, user, pass);
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, nextPlayerId); // Set the next player ID to fetch
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String id = String.valueOf(rs.getInt("id"));
                String codename = rs.getString("codename");
                player = new String[]{id, codename}; // Returning player as array
                nextPlayerId++; // Increment for the next player
            } else {
                System.out.println("No more players available."); // No more players available
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return player; // Return null if no player found
    }
}
