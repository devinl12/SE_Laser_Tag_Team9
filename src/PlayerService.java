import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerService {

    private String dbUrl = "jdbc:postgresql://localhost:5432/photon";
    private String user = "student";
    private String pass = "student";

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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //Seeing if this works
        finally {
            // Close the resources manually
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //block of code

        return players;
    }
}
