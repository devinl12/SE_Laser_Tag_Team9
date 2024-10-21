import javax.swing.*;
import java.awt.*;

public class PlayerActionDisplay {
    private JFrame frame;
    private JLabel redTeamLabel;
    private JLabel greenTeamLabel;
    private JTextArea actionLog;
    private JLabel timerLabel;

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

        // Center panel for game action log
        actionLog = new JTextArea();
        actionLog.setEditable(false);
        actionLog.setFont(new Font("SansSerif", Font.PLAIN, 16));
        JScrollPane actionLogScrollPane = new JScrollPane(actionLog);
        frame.add(actionLogScrollPane, BorderLayout.CENTER);

        // Bottom panel for the countdown timer
        JPanel bottomPanel = new JPanel();
        timerLabel = new JLabel("Time Remaining: 6:00", JLabel.CENTER);
        timerLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        bottomPanel.add(timerLabel);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.revalidate();
        frame.repaint();
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
    public void updateTimer(String timeRemaining) {
        timerLabel.setText("Time Remaining: " + timeRemaining);
    }
}
