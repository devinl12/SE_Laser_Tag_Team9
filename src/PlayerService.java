import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerService {

    private String dbUrl = "jdbc:postgresql://localhost:5432/photon";
    private String user = "student";
    private String pass = "student";

    private List<String[]> players;  // Cache of players fetched from the database

    public PlayerService() {
        this.players = getPlayers();  // Fetch players initially when the service is created
    }

    // Method to fetch players from the database
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

                int equipmentId = rs.getInt("id");
                UDPTransmit.transmitEquipmentCode(equipmentId);  // Transmit the player's equipment code
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return players;
    }

    // Method to add a new player to the database
    public void addNewPlayer(String id, String codename) {
        String query = "INSERT INTO players (id, codename) VALUES (?, ?);";

        try (Connection connection = DriverManager.getConnection(dbUrl, user, pass);
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, Integer.parseInt(id));  // Set the player ID
            pstmt.setString(2, codename);           // Set the player codename

            // Execute the insert query
            pstmt.executeUpdate();

            // Also add the new player to the in-memory list
            players.add(new String[]{id, codename});

            int equipmentId = Integer.parseInt(id);
            UDPTransmit.transmitEquipmentCode(equipmentId);  // Transmit the new player's equipment code
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}