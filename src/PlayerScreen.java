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

        // Add big title to the top of the frame
        JLabel titleLabel = new JLabel("Photon Laser Tag Teams", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 36)); // Set font and size for the title
        frame.add(titleLabel, BorderLayout.NORTH); // Add the title to the top
        
        // Set layout to GridLayout to split the frame into two halves
        frame.setLayout(new GridLayout(1, 2));

        // Left panel (Team 1)
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());

        // Team 1 Table with sample data
        String[] team1Columns = {"Team Pink Players"};
        Object[][] team1Data = {{"Player 1"}, {"Player 2"}, {"Player 3"}}; // Sample data
        JTable team1Table = new JTable(team1Data, team1Columns);
        JScrollPane team1ScrollPane = new JScrollPane(team1Table);
        leftPanel.add(team1ScrollPane, BorderLayout.CENTER);

        // Set custom colors for the table
        team1Table.setBackground(new Color(250, 128, 114));  
        team1Table.setForeground(Color.BLACK);               
        team1Table.setSelectionBackground(new Color(255, 182, 193));  
        team1Table.setSelectionForeground(Color.WHITE); 

        // Set custom font for the table content and headers
        Font tableFont = new Font("SansSerif", Font.PLAIN, 16); // Table content font
        team1Table.setFont(tableFont);                          // Set table font
        team1Table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 18)); // Set header font

        // Add Player Button for Team 1
        JButton addTeam1PlayerButton = new JButton("Add Player to Pink Team");
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

        // Set custom font for Team 2 table
        team2Table.setFont(tableFont);                          // Set table font
        team2Table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 18)); // Set header font
       
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