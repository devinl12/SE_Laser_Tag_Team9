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

public class PlayerActionDisplay {
    private JFrame frame;
    private JLabel redTeamLabel;
    private JLabel greenTeamLabel;
    private JTextArea actionLog;
    private JLabel timerLabel;
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
    

    public PlayerActionDisplay(JFrame frame) {
        this.frame = frame;
        playerScores = new HashMap<>(); // Initialize player scores
        redTeamScore = 0;
        greenTeamScore = 0;
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

        // Populate Red Team Table
        for (String[] player : redTeam) {
            redTeamModel.addRow(new Object[]{player[1]});
        }

        // Populate Green Team Table
        for (String[] player : greenTeam) {
            greenTeamModel.addRow(new Object[]{player[1]});
        }
    }


    public void addEvent(String event) {
        eventLogModel.addElement(event);
        if (eventLogModel.size() > 50) {
            eventLogModel.remove(0);
        }
    }

    public void processEvent(String event) {
        System.out.println("Processing event: " + event);

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

        if (targetId.equals("43")) { // Green team hits Red base
            if (isPlayerInTeam(attackerId, greenTeamModel)) {
                addBaseHit(attackerId, "red"); // Update player display
                greenTeamScore += 100; // Add 100 points to Green Team score
                addEvent("Green player " + attackerId + " hit the Red base!");
        }
        } else if (targetId.equals("53")) {
            if (isPlayerInTeam(attackerId, redTeamModel)) {
                addBaseHit(attackerId, "green"); // Update player display
                redTeamScore += 100; // Add 100 points to Red Team score
                addEvent("Red player " + attackerId + " hit the Green base!");
        } else {
            addEvent("Player " + attackerId + " tagged player " + targetId);
        }
          // Update displayed scores
        updateTeamScores(redTeamScore, greenTeamScore);

        try {
            InetAddress address = InetAddress.getByName("127.0.0.1");
            String ackMessage = "Acknowledged: " + event;
            byte[] buffer = ackMessage.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 7500);
            acknowledgmentSocket.send(packet);
            System.out.println("Sent acknowledgment: " + ackMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean isPlayerInTeam(String playerId, DefaultTableModel teamModel) {
    for (int i = 0; i < teamModel.getRowCount(); i++) {
        if (teamModel.getValueAt(i, 0).equals(playerId)) {
            return true;
        }
    }
    return false;
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
}

    public void addActionLogEntry(String entry) {
        actionLog.append(entry + "\n");
    }

    private void addBaseHit(String attackerId, String team) {
    DefaultTableModel model = team.equals("red") ? redTeamModel : greenTeamModel;
        for (int i = 0; i < model.getRowCount(); i++) {
            String playerName = model.getValueAt(i, 0).toString();
            if (playerName.equals(attackerId)) {
                model.setValueAt("B " + playerName, i, 0);
                break;
            }
        }
    }

    private void sendGameEndSignal() {
    try {
        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("127.0.0.1");
        String endMessage = "221";
        byte[] buffer = endMessage.getBytes();

        // Send the signal three times
        for (int i = 0; i < 3; i++) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 7500);
            socket.send(packet);
        }
        socket.close();
        System.out.println("Sent game end signal: 221");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
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
                PlayerScreen nextGame = new PlayerScreen(frame, PlayerActionDisplay.this);
                nextGame.showPlayerScreen();
                frame.setVisible(true);
            }
        }
    }
