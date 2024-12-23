import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class PlayerScreen implements KeyListener {

    private JFrame frame;
    private final PlayerService playerService;
    private final PlayerActionDisplay actionDisplay;
    private JTable team1Table;
    private JTable team2Table;
    private List<String[]> players;  // List of players from the database
    private Runnable onGameStart;


    // Constructor
    public PlayerScreen(JFrame frame, PlayerActionDisplay actionDisplay, PlayerService playerService) {
        this.frame = frame;
        this.actionDisplay = actionDisplay;
        this.playerService = playerService;
        this.players = playerService.getPlayers();

        frame.addKeyListener(this);
        frame.setFocusable(true);
        frame.requestFocusInWindow();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set close operation
        frame.setSize(800, 600); // Set an appropriate frame size
    }


    public void showPlayerScreen() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        // Main panel for teams
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        // Red Team table
        JPanel leftPanel = new JPanel(new BorderLayout());
        String[] team1Columns = {"Team Red Players", "Equipment Code"};
        DefaultTableModel team1Model = new DefaultTableModel(team1Columns, 0);
        team1Table = new JTable(team1Model);
        JScrollPane team1ScrollPane = new JScrollPane(team1Table);
        leftPanel.add(team1ScrollPane, BorderLayout.CENTER);
        customizeTable(team1Table, new Color(255, 69, 0), new Color(220, 20, 60));

        // Hide the EquipmentID column for the red team
        hideColumn(team1Table, 1);

        // Add Player Button for Red Team
        JButton addTeam1PlayerButton = new JButton("Add Player to Red Team");
        addTeam1PlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPlayerToTeam(team1Model);
            }
        });
        leftPanel.add(addTeam1PlayerButton, BorderLayout.SOUTH);

        // Green Team Table
        JPanel rightPanel = new JPanel(new BorderLayout());
        String[] team2Columns = {"Team Green Players", "Equipment Code"};
        DefaultTableModel team2Model = new DefaultTableModel(team2Columns, 0);
        team2Table = new JTable(team2Model);
        JScrollPane team2ScrollPane = new JScrollPane(team2Table);
        rightPanel.add(team2ScrollPane, BorderLayout.CENTER);
        customizeTable(team2Table, new Color(119, 221, 119), new Color(144, 238, 144));

        // Hide the EquipmentID column for the green team
        hideColumn(team2Table, 1);

        // Add Player Button for Green Team
        JButton addTeam2PlayerButton = new JButton("Add Player to Green Team");
        addTeam2PlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPlayerToTeam(team2Model); // Add player to team 2
            }
        });
        rightPanel.add(addTeam2PlayerButton, BorderLayout.SOUTH);

        // Add the two panels to the main panel
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        // Bottom panel for instructions
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 10));

        JLabel f5Label = new JLabel("Press F5 to start game");
        f5Label.setFont(new Font("SansSerif", Font.BOLD, 16));
        f5Label.setForeground(Color.BLUE);

        JLabel f12Label = new JLabel("Press F12 to clear players");
        f12Label.setFont(new Font("SansSerif", Font.BOLD, 16));
        f12Label.setForeground(Color.RED);

        bottomPanel.add(f5Label);
        bottomPanel.add(f12Label);

        // Add main panel and bottom panel to the frame
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Revalidate and repaint to refresh the UI
        frame.revalidate();
        frame.repaint();
    }

// Helper method to hide a column in a JTable
    private void hideColumn(JTable table, int columnIndex) {
        table.getColumnModel().getColumn(columnIndex).setMinWidth(0);
        table.getColumnModel().getColumn(columnIndex).setMaxWidth(0);
        table.getColumnModel().getColumn(columnIndex).setWidth(0);
    }


    private void addPlayerToTeam(DefaultTableModel teamModel) {
        // Prompt the user to input the player's ID
        String inputId = JOptionPane.showInputDialog(frame, "Input Player ID:");
        frame.requestFocusInWindow();

        if (inputId != null && !inputId.trim().isEmpty()) {
            String[] player = findPlayerById(inputId);

            if (player != null) {
                // Player ID found, add the player to the table
                //teamModel.addRow(new Object[]{player[1]});

                // Prompt for the equipment code
                String equipmentCodeInput = JOptionPane.showInputDialog(frame, "Enter Equipment Code for " + player[1] + ":");
                if (equipmentCodeInput != null) {
                    try {
                        int equipmentCode = Integer.parseInt(equipmentCodeInput);
                        teamModel.addRow(new Object[]{player[1], equipmentCode});
                        // Transmit the equipment code via UDP
                        UDPTransmit.transmitEquipmentCode(equipmentCode);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(frame, "Invalid Equipment Code. Please enter a valid number.");
                    }
                }
            } 
            
            else {
                // Player ID not found, prompt to add a new player
                String newCodename = JOptionPane.showInputDialog(frame, "ID not found. Enter new player's name:");
                if (newCodename != null && !newCodename.trim().isEmpty()) {
                    // Add the new player to the list and database
                    playerService.addNewPlayer(inputId, newCodename);
                    //teamModel.addRow(new Object[]{newCodename});

                    // Prompt for the equipment code
                    String equipmentCodeInput = JOptionPane.showInputDialog(frame, "Enter Equipment Code for " + newCodename + ":");
                    if (equipmentCodeInput != null) {
                        try {
                            int equipmentCode = Integer.parseInt(equipmentCodeInput);
                            teamModel.addRow(new Object[]{player[1], equipmentCode});
                            // Transmit the equipment code via UDP
                            UDPTransmit.transmitEquipmentCode(equipmentCode);
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(frame, "Invalid Equipment Code. Please enter a valid number.");
                        }
                    }
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

    
    private List<String[]> getRedTeamPlayers() {
        List<String[]> redTeamPlayers = new ArrayList<>();
        DefaultTableModel model = (DefaultTableModel) team1Table.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            String codename = model.getValueAt(i, 0).toString();
            String equipmentCode = model.getValueAt(i, 1).toString(); // Second column
            redTeamPlayers.add(new String[]{codename, equipmentCode});
        }
        return redTeamPlayers;
    }

private List<String[]> getGreenTeamPlayers() {
    List<String[]> greenTeamPlayers = new ArrayList<>();
    DefaultTableModel model = (DefaultTableModel) team2Table.getModel();

    for (int i = 0; i < model.getRowCount(); i++) {
        String codename = model.getValueAt(i, 0).toString();
        String equipmentCode = model.getValueAt(i, 1).toString(); // Second column
        greenTeamPlayers.add(new String[]{codename, equipmentCode});
    }
    return greenTeamPlayers;
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

    // Function for clearing player entries
    private void clearPlayerEntries(DefaultTableModel teamModel) {
        int rowCount = teamModel.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            teamModel.removeRow(i);
        }
    }


    private void switchDisplay() {
    // Retrieve red and green team players
    List<String[]> redTeamPlayers = getRedTeamPlayers();
    List<String[]> greenTeamPlayers = getGreenTeamPlayers();

    // Start the countdown
    ImageCountdown countdown = new ImageCountdown(redTeamPlayers, greenTeamPlayers, actionDisplay);
    countdown.startCountdown(frame);
}

   

    public void setOnGameStart(Runnable onGameStart) {
        this.onGameStart = onGameStart;
    }

    // Key listener
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F12) {
            clearPlayerEntries((DefaultTableModel) team1Table.getModel());
            clearPlayerEntries((DefaultTableModel) team2Table.getModel());
            frame.revalidate();
            frame.repaint();
        } else if (e.getKeyCode() == KeyEvent.VK_F5) {
            if (onGameStart != null) {
                switchDisplay();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}

 
