import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PlayerScreen {
    private JFrame frame;

    public PlayerScreen(JFrame frame) {
        this.frame = frame;
    }

    public void showPlayerScreen() {
        PlayerService playerService = new PlayerService();
        List<String[]> players = playerService.getPlayers();

        // Column names for the table
        String[] columnNames = {"ID", "Codename"};

        // Convert list to 2D array for JTable
        String[][] playerData = players.toArray(new String[0][0]);

        // Create table with player data
        JTable table = new JTable(playerData, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        // Add table to the frame
        frame.getContentPane().removeAll(); // Clear existing components
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.revalidate(); // Refresh the frame
        frame.repaint();
        frame.setVisible(true);
    }
}