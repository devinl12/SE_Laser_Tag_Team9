import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PlayerScreen {

    private JFrame frame;
    private PlayerService playerService;
    private JTable team1Table;
    private JTable team2Table;
    private List<String[]> players;  // List of players from the database

    // Constructor
    public PlayerScreen(JFrame frame) {
        this.frame = frame;
        this.playerService = new PlayerService();
        this.players = playerService.getPlayers(); 
    }

    // Graphics for the player screen
    public void showPlayerScreen() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(1, 2));

        // Pink Team table
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        String[] team1Columns = {"Team Pink Players"};
        DefaultTableModel team1Model = new DefaultTableModel(team1Columns, 0);
        team1Table = new JTable(team1Model);
        JScrollPane team1ScrollPane = new JScrollPane(team1Table);
        leftPanel.add(team1ScrollPane, BorderLayout.CENTER);
        customizeTable(team1Table, new Color(250, 128, 114), new Color(255, 182, 193));

        // Add Player Button for Pink Team
        JButton addTeam1PlayerButton = new JButton("Add Player to Pink Team");
        addTeam1PlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPlayerToTeam(team1Model); 
            }
        });
        leftPanel.add(addTeam1PlayerButton, BorderLayout.SOUTH);

        // Green Team Table
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(152, 251, 152)); 
        rightPanel.setLayout(new BorderLayout());
        String[] team2Columns = {"Team Green Players"};
        DefaultTableModel team2Model = new DefaultTableModel(team2Columns, 0);
        team2Table = new JTable(team2Model);
        JScrollPane team2ScrollPane = new JScrollPane(team2Table);
        rightPanel.add(team2ScrollPane, BorderLayout.CENTER);
        customizeTable(team2Table, new Color(152, 251, 152), new Color(144, 238, 144));

        // Add Player Button for Green Table
        JButton addTeam2PlayerButton = new JButton("Add Player to Green Team");
        addTeam2PlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPlayerToTeam(team2Model); // Add player to team 2
            }
        });
        rightPanel.add(addTeam2PlayerButton, BorderLayout.SOUTH);

        // Add the two panels to the frame
        frame.add(leftPanel);
        frame.add(rightPanel);
        frame.revalidate();
        frame.repaint();
    }

    // Method to add a player based on ID input from the user
    private void addPlayerToTeam(DefaultTableModel teamModel) {
        // Prompt the user to input the player's ID
        String inputId = JOptionPane.showInputDialog(frame, "Enter Player ID:");

        if (inputId != null && !inputId.trim().isEmpty()) {
            String[] player = findPlayerById(inputId);

            if (player != null) {
                // Player ID found, add the player to the table
                teamModel.addRow(new Object[]{player[1]});  
            } else {
                // Player ID not found, prompt to add a new player
                String newCodename = JOptionPane.showInputDialog(frame, "ID not found. Enter new player's name:");

                if (newCodename != null && !newCodename.trim().isEmpty()) {
                    // Add the new player to the list and database
                    playerService.addNewPlayer(inputId, newCodename);
                    teamModel.addRow(new Object[]{newCodename});
                }
            }
        }
    }

    // Method to find a player by ID
    private String[] findPlayerById(String id) {
        for (String[] player : players) {
            if (player[0].equals(id)) {
                return player;  
            }
        }
        return null;  
    }

    // Method to customize the JTable
    private void customizeTable(JTable table, Color bgColor, Color selectionColor) {
        table.setBackground(bgColor);
        table.setForeground(Color.BLACK);
        table.setSelectionBackground(selectionColor);
        table.setSelectionForeground(Color.WHITE);

        // Set custom font for the table content and headers
        Font tableFont = new Font("SansSerif", Font.PLAIN, 16);  
        table.setFont(tableFont);                                
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 18));  
    }
}