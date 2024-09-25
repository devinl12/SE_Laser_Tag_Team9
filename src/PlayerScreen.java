import javax.swing.*;
import java.awt.*;

public class PlayerScreen {

    private JFrame frame;

    // Constructor
    public PlayerScreen(JFrame frame) {
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
        leftPanel.setLayout(new BorderLayout());

        // Team 1 Table with sample data
        String[] team1Columns = {"Team Blue Players"};
        Object[][] team1Data = {{"Player 1"}, {"Player 2"}, {"Player 3"}}; // Sample data
        JTable team1Table = new JTable(team1Data, team1Columns);
        JScrollPane team1ScrollPane = new JScrollPane(team1Table);
        leftPanel.add(team1ScrollPane, BorderLayout.CENTER);

        // Set custom colors for the table
        team1Table.setBackground(new Color(224, 255, 255));  // Light cyan background
        team1Table.setForeground(Color.BLACK);               // Black text
        team1Table.setSelectionBackground(new Color(135, 206, 250));  // Light blue when selected
        team1Table.setSelectionForeground(Color.WHITE); 

        // Add Player Button for Team 1
        JButton addTeam1PlayerButton = new JButton("Add Player to Blue Team");
        leftPanel.add(addTeam1PlayerButton, BorderLayout.SOUTH);

        // Right panel (Team 2)
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(152, 251, 152)); // Pastel green
        rightPanel.setLayout(new BorderLayout());

        // Team 2 Table with sample data
        String[] team2Columns = {"Team Green Players"};
        Object[][] team2Data = {{"Player A"}, {"Player B"}, {"Player C"}}; // Sample data
        JTable team2Table = new JTable(team2Data, team2Columns);

        // Set custom colors for the table
        team2Table.setBackground(new Color(152, 251, 152));  
        team2Table.setForeground(Color.BLACK);               
        team2Table.setSelectionBackground(new Color(144, 238, 144));  
        team2Table.setSelectionForeground(Color.BLACK);
        JScrollPane team2ScrollPane = new JScrollPane(team2Table);
       
        rightPanel.add(team2ScrollPane, BorderLayout.CENTER);

        // Add Player Button for Team 2
        JButton addTeam2PlayerButton = new JButton("Add Player to Green Team");
        rightPanel.add(addTeam2PlayerButton, BorderLayout.SOUTH);

        // Add the two panels to the frame
        frame.add(leftPanel);
        frame.add(rightPanel);

        // Revalidate and repaint the frame to reflect changes
        frame.revalidate();
        frame.repaint();
    }
}