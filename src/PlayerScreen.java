import javax.swing.*;
import java.awt.*;

public class PlayerScreen {

    private JFrame frame;

    // Constructor
    public playerScreen(JFrame frame) {
        this.frame = frame;
    }

    // Method to show the player screen
    public void showPlayerScreen() {
        // Clear the current content of the frame
        frame.getContentPane().removeAll();
        
        // Set layout to GridLayout to split the frame into two halves
        frame.setLayout(new GridLayout(1, 2));

        // Left panel (Team 1)
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(173, 216, 230)); // Pastel blue
        leftPanel.setLayout(new BorderLayout());

        // Team 1 Table
        JTable team1Table = new JTable(0, 2); // Placeholder table with 2 columns
        JScrollPane team1ScrollPane = new JScrollPane(team1Table);
        leftPanel.add(team1ScrollPane, BorderLayout.CENTER);

        // Add Player Button for Team 1
        JButton addTeam1PlayerButton = new JButton("Add Player to Team 1");
        leftPanel.add(addTeam1PlayerButton, BorderLayout.SOUTH);

        // Right panel (Team 2)
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(152, 251, 152)); // Pastel green
        rightPanel.setLayout(new BorderLayout());

        // Team 2 Table
        JTable team2Table = new JTable(0, 2); // Placeholder table with 2 columns
        JScrollPane team2ScrollPane = new JScrollPane(team2Table);
        rightPanel.add(team2ScrollPane, BorderLayout.CENTER);

        // Add Player Button for Team 2
        JButton addTeam2PlayerButton = new JButton("Add Player to Team 2");
        rightPanel.add(addTeam2PlayerButton, BorderLayout.SOUTH);

        // Add the two panels to the frame
        frame.add(leftPanel);
        frame.add(rightPanel);

        // Revalidate and repaint the frame to reflect changes
        frame.revalidate();
        frame.repaint();
    }
}