import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
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

        // Set up key bindings for the frame
        setupKeyBindings(frame);
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
        addTeam1PlayerButton.addActionListener(e -> addPlayerToTeam(team1Model));
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

        // Add Player Button for Green Team
        JButton addTeam2PlayerButton = new JButton("Add Player to Green Team");
        addTeam2PlayerButton.addActionListener(e -> addPlayerToTeam(team2Model));
        rightPanel.add(addTeam2PlayerButton, BorderLayout.SOUTH);

        // Add the two panels to the frame
        frame.add(leftPanel);
        frame.add(rightPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void addPlayerToTeam(DefaultTableModel teamModel) {
        String inputId = JOptionPane.showInputDialog(frame, "Input Player ID:");

        if (inputId != null && !inputId.trim().isEmpty()) {
            String[] player = findPlayerById(inputId);

            if (player != null) {
                teamModel.addRow(new Object[]{player[1]});

                String equipmentCodeInput = JOptionPane.showInputDialog(frame, "Enter Equipment Code for " + player[1] + ":");
                if (equipmentCodeInput != null) {
                    try {
                        int equipmentCode = Integer.parseInt(equipmentCodeInput);
                        UDPTransmit.transmitEquipmentCode(equipmentCode);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(frame, "Invalid Equipment Code. Please enter a valid number.");
                    }
                }
            } else {
                String newCodename = JOptionPane.showInputDialog(frame, "ID not found. Enter new player's name:");
                if (newCodename != null && !newCodename.trim().isEmpty()) {
                    playerService.addNewPlayer(inputId, newCodename);
                    teamModel.addRow(new Object[]{newCodename});

                    String equipmentCodeInput = JOptionPane.showInputDialog(frame, "Enter Equipment Code for " + newCodename + ":");
                    if (equipmentCodeInput != null) {
                        try {
                            int equipmentCode = Integer.parseInt(equipmentCodeInput);
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

    // Method to customize the JTable
    private void customizeTable(JTable table, Color bgColor, Color selectionColor) {
        table.setBackground(bgColor);
        table.setForeground(Color.BLACK);
        table.setSelectionBackground(selectionColor);
        table.setSelectionForeground(Color.WHITE);

        Font tableFont = new Font("SansSerif", Font.PLAIN, 16);  
        table.setFont(tableFont);                                
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 18));  
    }

    // Method for clearing player entries
    private void clearPlayerEntries(DefaultTableModel teamModel) {
        int rowCount = teamModel.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            teamModel.removeRow(i); 
        }
    }

    // Switch to game display
    private void switchDisplay() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JPanel gameScreen = new JPanel();
        JLabel gameLabel = new JLabel("Game Started", SwingConstants.CENTER);
        gameLabel.setForeground(Color.WHITE); 
        gameLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));

        gameScreen.setBackground(new Color(0, 0, 139));
        gameScreen.add(gameLabel);

        frame.add(gameScreen);
        frame.revalidate();
        frame.repaint();
    }

    // Setup key bindings
    private void setupKeyBindings(JFrame frame) {
        InputMap inputMap = frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = frame.getRootPane().getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0), "clearEntries");
        actionMap.put("clearEntries", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearPlayerEntries((DefaultTableModel) team1Table.getModel());
                clearPlayerEntries((DefaultTableModel) team2Table.getModel());
                frame.revalidate();
                frame.repaint();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "switchDisplay");
        actionMap.put("switchDisplay", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchDisplay();
            }
        });
    }
}