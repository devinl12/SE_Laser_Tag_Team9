import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.*;

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
    private int gameLength = 20;

    public PlayerActionDisplay(JFrame frame) {
        this.frame = frame;
    }

    public void showActionDisplay() {
        // Clear existing components and set up the layout
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        // Top panel for team scores
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        redTeamLabel = new JLabel("Red Team: 0", JLabel.CENTER);
        greenTeamLabel = new JLabel("Green Team: 0", JLabel.CENTER);
        redTeamLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        greenTeamLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        //

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

        frame.add(centerPanel, BorderLayout.CENTER);

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

    private class CountdownAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (gameLength > 0) {
                updateTimer();
                gameLength--;
            } else {
                timer.stop();
		timerLabel.setText("Game Complete!");
		PlayerScreen nextGame = new PlayerScreen(frame);
		nextGame.showPlayerScreen();

		frame.setVisible(true);
            }
        }
    }

    // Method to populate the team tables
    public void populateTeams (List<String[]> redTeam, List<String[]> greenTeam) {
        // Clear existing data
        redTeamModel.setRowCount(0);
        greenTeamModel.setRowCount(0);
    
        // Add players to the Red Team table
        for (String[] player : redTeam) {
            redTeamModel.addRow(new Object[]{player[1]});
        }
    
        // Add players to the Green Team table
        for (String[] player : greenTeam) {
            greenTeamModel.addRow(new Object[]{player[1]});
        }
    }

    // Method to update the scores
    public void updateTeamScores(int redTeamScore, int greenTeamScore) {
        redTeamLabel.setText("Red Team: " + redTeamScore);
        greenTeamLabel.setText("Green Team: " + greenTeamScore);
    }

    // Method to add a new log entry
    public void addActionLogEntry(String entry) {
        actionLog.append(entry + "\n");
    }

    // Method to update the countdown timer display
    public void updateTimer() {
        int minutes = this.gameLength / 60;
	int seconds = this.gameLength % 60;
	String minuteString = Integer.toString(minutes);
        String secondString = Integer.toString(seconds);
        if (secondString.length() < 2) {
            secondString = "0" + secondString;
        }
        timerLabel.setText("Time Remaining: " + minuteString + ":" + secondString );
    }
}

