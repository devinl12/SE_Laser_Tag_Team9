import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class PlayerActionDisplay {
    private JFrame frame;
    private final PlayerService playerService;
    private JLabel redTeamLabel;
    private JLabel greenTeamLabel;
    private JTextArea actionLog;
    private JLabel timerLabel;
    private String lastProcessedEvent = null;
    private JTable redTeamTable;
    private JTable greenTeamTable;
    private DefaultTableModel redTeamModel;
    private DefaultTableModel greenTeamModel;
    private Timer timer;
    private int gameLength = 360;
    private DefaultListModel<String> eventLogModel;
    private DatagramSocket acknowledgmentSocket;
    private Map<String, Integer> playerScores; // To store player scores
    private int redTeamScore;
    private int greenTeamScore;
    private List<String[]> players;
    private Timer flashingTimer; // Timer for flashing effect
    private JLabel flashingLabel; // The label that is currently flashing
    

    public PlayerActionDisplay(JFrame frame, PlayerService playerService) {
        this.frame = frame;
        this.playerService = playerService;
        playerScores = new HashMap<>(); // Initialize player scores
        redTeamScore = 0;
        greenTeamScore = 0;
        this.players = playerService.getPlayers();
        try {
            acknowledgmentSocket = new DatagramSocket(); // Reuse this socket for acknowledgments
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void showActionDisplay() {
        // Clear existing components and set up the layout
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        // Top panel for team scores
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        redTeamLabel = new JLabel("Red Team: 0", JLabel.CENTER);
        redTeamLabel.setForeground(Color.RED);
        greenTeamLabel = new JLabel("Green Team: 0", JLabel.CENTER);
        greenTeamLabel.setForeground(Color.GREEN);
        redTeamLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        greenTeamLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        topPanel.add(redTeamLabel);
        topPanel.add(greenTeamLabel);
        frame.add(topPanel, BorderLayout.NORTH);

        // Center panel for the teams' player tables
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));

        // Red Team Table
        String[] redTeamColumns = {"Red Team Players"};
        redTeamModel = new DefaultTableModel(redTeamColumns, 0);
        redTeamTable = new JTable(redTeamModel);
        JScrollPane redTeamScrollPane = new JScrollPane(redTeamTable);
        centerPanel.add(redTeamScrollPane);

        // Green Team Table
        String[] greenTeamColumns = {"Green Team Players"};
        greenTeamModel = new DefaultTableModel(greenTeamColumns, 0);
        greenTeamTable = new JTable(greenTeamModel);
        JScrollPane greenTeamScrollPane = new JScrollPane(greenTeamTable);
        centerPanel.add(greenTeamScrollPane);

        // Initialize event log panel
        eventLogModel = new DefaultListModel<>();
        JList<String> eventLogList = new JList<>(eventLogModel);
        JPanel eventLogPanel = new JPanel(new BorderLayout());
        JScrollPane eventLogScrollPane = new JScrollPane(eventLogList);
        eventLogPanel.add(new JLabel("Events"), BorderLayout.NORTH);
        eventLogPanel.add(eventLogScrollPane, BorderLayout.CENTER);

        // Adjust center panel layout to include the event log
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(eventLogPanel, BorderLayout.EAST);

        frame.add(mainPanel, BorderLayout.CENTER);

        // Bottom panel for the countdown timer and action log
        JPanel bottomPanel = new JPanel(new BorderLayout());
        timerLabel = new JLabel("Time Remaining: 6:00", JLabel.CENTER);
        timerLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        actionLog = new JTextArea();
        actionLog.setEditable(false);
        actionLog.setFont(new Font("SansSerif", Font.PLAIN, 16));
        JScrollPane actionLogScrollPane = new JScrollPane(actionLog);

        bottomPanel.add(timerLabel, BorderLayout.NORTH);
        bottomPanel.add(actionLogScrollPane, BorderLayout.CENTER);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.revalidate();
        frame.repaint();

        timer = new Timer(1000, new CountdownAction());
        timer.start();
    }

    public void populateTeams(List<String[]> redTeam, List<String[]> greenTeam) {
        // Clear existing data
        redTeamModel.setRowCount(0);
        greenTeamModel.setRowCount(0);

        // Add the "Score" column without showing EquipmentID
        redTeamModel.setColumnIdentifiers(new String[]{"Red Team Players", "Score", "EquipmentID"});
        greenTeamModel.setColumnIdentifiers(new String[]{"Green Team Players", "Score", "EquipmentID"});

        // Populate Red Team Table
        for (String[] player : redTeam) {
            playerScores.put(player[1], 0); // Initialize scores for each player
            redTeamModel.addRow(new Object[]{player[0], 0, player[1]}); // Add Player Name, Score, and EquipmentID
        }

        // Populate Green Team Table
        for (String[] player : greenTeam) {
            playerScores.put(player[1], 0); // Initialize scores for each player
            greenTeamModel.addRow(new Object[]{player[0], 0, player[1]}); // Add Player Name, Score, and EquipmentID
        }

        // Hide the EquipmentID column for both tables
        hideColumn(redTeamTable, 2); // 2 = EquipmentID column index
        hideColumn(greenTeamTable, 2);

        // Sort the tables by score
        sortTableByScore(redTeamModel);
        sortTableByScore(greenTeamModel);
    }



    public void addEvent(String event) {
        eventLogModel.addElement(event);
        if (eventLogModel.size() > 50) {
            eventLogModel.remove(0);
        }
    }

    public void processEvent(String event) {

        if (event.equals(lastProcessedEvent)) {
            try {
            InetAddress address = InetAddress.getByName("127.0.0.1");
            String ackMessage = "Acknowledged: " + event;
            byte[] buffer = ackMessage.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 7500);
            acknowledgmentSocket.send(packet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
            return; // Skip if this event is the same as the last one
        }
        lastProcessedEvent = event; // Update the last processed event

        if (event == null || event.isEmpty()) {
            System.out.println("Received an empty event, skipping...");
            return;
        }

        String[] parts = event.split(":");
        if (parts.length < 2) {
            System.out.println("Malformed event: " + event);
            return;
        }

        String attackerId = parts[0];
        String targetId = parts[1];



        if (targetId.equals("43")) { // Greenbase has been scored
            String whichTeam = getTeamForPlayer(attackerId);
            //System.out.println("AttackerId " + attackerId + "is on team" + whichTeam);
            if (whichTeam.equals("Red Team")){
                addBaseHit(attackerId);
                addPlayerScore(attackerId, 100, redTeamModel);
                redTeamScore += 100;
                addEvent("Red player " + attackerId + " hit the Green base!");
            }

        }
        else if (targetId.equals("53")) {
            String whichTeam = getTeamForPlayer(attackerId); //red base has been scored
            //System.out.println("AttackerId " + attackerId + "is on team" + whichTeam);
            if (whichTeam.equals("Green Team")){
                addBaseHit(attackerId);
                addPlayerScore(attackerId, 100, greenTeamModel);
                greenTeamScore += 100;
                addEvent("Green player " + attackerId + " hit the Green base!");
            } 
        }
        else {
            addEvent("Player " + attackerId + " tagged player " + targetId);
            String points = choosingScoreToAdd(attackerId, targetId);
            int scoreChange = 0;

            if (points.equals("SameRed")) {
                redTeamScore -= 10;
                scoreChange = -10;
                addPlayerScore(attackerId, scoreChange, redTeamModel);
            } else if (points.equals("SameGreen")) {
                greenTeamScore -= 10;
                scoreChange = -10;
                addPlayerScore(attackerId, scoreChange, greenTeamModel);
            } else if (points.equals("RedHitsGreen")) {
                redTeamScore += 10;
                scoreChange = 10;
                addPlayerScore(attackerId, scoreChange, redTeamModel);
            } else if (points.equals("GreenHitsRed")) {
                greenTeamScore += 10;
                scoreChange = 10;
                addPlayerScore(attackerId, scoreChange, greenTeamModel);
            }
        }

        // Update displayed scores
        updateTeamScores(redTeamScore, greenTeamScore);

        try {
            InetAddress address = InetAddress.getByName("127.0.0.1");
            String ackMessage = "Acknowledged: " + event;
            byte[] buffer = ackMessage.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 7500);
            acknowledgmentSocket.send(packet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    public void updateTimer() {
        int minutes = gameLength / 60;
        int seconds = gameLength % 60;
        String minuteString = String.valueOf(minutes);
        String secondString = String.format("%02d", seconds);
        timerLabel.setText("Time Remaining: " + minuteString + ":" + secondString);
    }

    private void updateTeamScores(int redTeamScore, int greenTeamScore) {
        redTeamLabel.setText("Red Team: " + redTeamScore);
        greenTeamLabel.setText("Green Team: " + greenTeamScore);

        // Determine which team has the higher score and start flashing the label
        if (redTeamScore > greenTeamScore) {
            startFlashing(redTeamLabel);
        } else if (greenTeamScore > redTeamScore) {
            startFlashing(greenTeamLabel);
        } else {
            stopFlashing(); // Stop flashing if scores are tied
        }
    }

    private void startFlashing(JLabel label) {
        if (flashingLabel == label) {
            return; // Already flashing this label
        }

        stopFlashing(); 

        flashingLabel = label;
        flashingTimer = new Timer(500, new ActionListener() { 
            private boolean visible = true;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (flashingLabel != null) {
                    flashingLabel.setVisible(visible);
                    visible = !visible; 
                }
            }
        });
        flashingTimer.start();
    }

    private void stopFlashing() {
        if (flashingTimer != null) {
            flashingTimer.stop();
            flashingTimer = null;
        }
        if (flashingLabel != null) {
            flashingLabel.setVisible(true); 
            flashingLabel = null;
        }
    }

    public void addActionLogEntry(String entry) {
        actionLog.append(entry + "\n");
    }

    private void addBaseHit(String attackerId) {
        // Determine the team of the player using getTeamForPlayer
        String team = getTeamForPlayer(attackerId);
        DefaultTableModel teamModel;

        // Select the appropriate team model based on the team
        if ("Red Team".equals(team)) {
            teamModel = redTeamModel;
        } else if ("Green Team".equals(team)) {
            teamModel = greenTeamModel;
        } else {
            System.out.println("Player with ID " + attackerId + " is not assigned to any team.");
            return; // Exit if the player is not found on any team
        }

        // Iterate through the team table to find the attacker's equipmentId
        for (int i = 0; i < teamModel.getRowCount(); i++) {
            if (attackerId.equals(teamModel.getValueAt(i, 2))) { // Match equipmentId in the third column
                String playerName = (String) teamModel.getValueAt(i, 0); // Get the player's name
                String newName = "<html>" + playerName + " <i>B</i></html>"; // Add italicized "B"
                teamModel.setValueAt(newName, i, 0); // Update the player's name in the first column
                return; // Exit after updating
            }
        }

        // If no match was found in the team model
        System.out.println("Could not find player with ID: " + attackerId + " in " + team);
    }

public String choosingScoreToAdd(String attackerId, String targetId) {
    String attackTeam = "none";
    String hitTeam = "none";

    // Determine attacker's team
    attackTeam = getTeamForPlayer(attackerId);

    // Determine target's team
    hitTeam = getTeamForPlayer(targetId);

    // Compare teams and return the result
    if ("Red Team".equals(attackTeam) && "Red Team".equals(hitTeam)) {
        return "SameRed"; // Both are in Red Team
    } else if ("Green Team".equals(attackTeam) && "Green Team".equals(hitTeam)) {
        return "SameGreen"; // Both are in Green Team
    } else if ("Red Team".equals(attackTeam) && "Green Team".equals(hitTeam)) {
        return "RedHitsGreen"; // Red hits Green
    } else if ("Green Team".equals(attackTeam) && "Red Team".equals(hitTeam)) {
        return "GreenHitsRed"; // Green hits Red
    }

    return "NoHit"; // Default case
}

    private void addPlayerScore(String attackerId, int scoreChange, DefaultTableModel model) {
    // Update score in the playerScores map
        int newScore = playerScores.getOrDefault(attackerId, 0) + scoreChange;
        playerScores.put(attackerId, newScore);

        // Update the score in the appropriate table
        boolean updated = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            if (attackerId.equals(model.getValueAt(i, 2))) { // Match equipment ID
                model.setValueAt(newScore, i, 1); // Update the "Score" column
                updated = true;
                break;
            }
        }

        if (!updated) {
            System.out.println("Could not match ID: " + attackerId);
            return;
        }

        // Sort the rows by score in descending order
        sortTableByScore(model);
    }

    private void sortTableByScore(DefaultTableModel model) {
        int rowCount = model.getRowCount();
        if (rowCount < 2) return; // No need to sort if there's only one row or none

        // Extract rows into a list for sorting
        List<Object[]> rows = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            Object[] row = new Object[model.getColumnCount()];
            for (int j = 0; j < model.getColumnCount(); j++) {
                row[j] = model.getValueAt(i, j);
            }
            rows.add(row);
        }

        // Sort rows by the "Score" column (index 1) in descending order
        rows.sort((r1, r2) -> Integer.compare((int) r2[1], (int) r1[1]));

        // Clear the model and re-add sorted rows
        model.setRowCount(0);
        for (Object[] row : rows) {
            model.addRow(row);
        }
    }
 

    private void sendGameEndSignal() {
        try {
            InetAddress address = InetAddress.getByName("127.0.0.1");
            String endMessage = "221";
            byte[] buffer = endMessage.getBytes();

            // Send the signal three times
            for (int i = 0; i < 3; i++) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 7500);
                acknowledgmentSocket.send(packet);
            }
            System.out.println("Sent game end signal: 221");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getTeamForPlayer(String equipmentId) {
        // Check Red Team
        for (int i = 0; i < redTeamModel.getRowCount(); i++) {
            if (equipmentId.equals(redTeamModel.getValueAt(i, 2))) { // Match equipment ID in the third column
                return "Red Team";
            }
        }

        // Check Green Team
        for (int i = 0; i < greenTeamModel.getRowCount(); i++) {
            if (equipmentId.equals(greenTeamModel.getValueAt(i, 2))) { // Match equipment ID in the third column
                return "Green Team";
            }
        }

        // If not found in either team
        return "Not assigned to any team";
    }
// Helper method to hide a column in a JTable
    private void hideColumn(JTable table, int columnIndex) {
        table.getColumnModel().getColumn(columnIndex).setMinWidth(0);
        table.getColumnModel().getColumn(columnIndex).setMaxWidth(0);
        table.getColumnModel().getColumn(columnIndex).setWidth(0);
    }


private class CountdownAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameLength > 0) {
            updateTimer();
            gameLength--;
        } else {
            timer.stop();
            timerLabel.setText("Game Complete!");
            sendGameEndSignal();
            showReturnToPlayerScreenButton(); // Add the button to return to the player entry screen
        }
    }
}

private void showReturnToPlayerScreenButton() {
    JPanel bottomPanel = new JPanel(new BorderLayout());
    JButton returnButton = new JButton("Return to Player Entry Screen");

    returnButton.setFont(new Font("SansSerif", Font.BOLD, 18));
    returnButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            PlayerScreen playerScreen = new PlayerScreen(frame, PlayerActionDisplay.this, playerService);
            playerScreen.showPlayerScreen();
        }
    });

    bottomPanel.add(returnButton, BorderLayout.CENTER);
    frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    frame.revalidate();
    frame.repaint();
}

}
