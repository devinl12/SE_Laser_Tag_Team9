import java.sql.*;
public class PSQLConn {
	public static void main(String[] args) {
		String db_url = "jdbc:postgresql://localhost:5432/photon";
		String user = "student";
		String pass = "student";
		String query = "select * from players;";
		try (Connection connection = DriverManager.getConnection(db_url, user, pass);
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
		) {
			while(rs.next()) {
				System.out.print("id: " + rs.getInt("id"));
				System.out.print("codname: " + rs.getString("codename"));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
