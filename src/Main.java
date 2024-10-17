import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Create main screen
        JFrame frame = new JFrame("Photon Laser Tag System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        // Show splash screen
        Splash.showSplashScreen(frame);

        // Timer to remove splash screen after 5 secs
        Timer timer = new Timer(5000, e -> {
            // Remove splash screen and show the player screen
            PlayerScreen playerScreen = new PlayerScreen(frame);
            playerScreen.showPlayerScreen();
            // Make the main frame visible after the splash screen
            frame.setVisible(true);
        });
        timer.setRepeats(false);
        timer.start();
    }
}
