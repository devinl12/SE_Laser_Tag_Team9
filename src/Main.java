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

        // Timer to remove splash screen after 5 seconds
        Timer timer = new Timer(5000, e -> {
            // Create PlayerScreen
            PlayerScreen playerScreen = new PlayerScreen(frame);

            // Create PlayerActionDisplay
            PlayerActionDisplay actionDisplay = new PlayerActionDisplay(frame);

            // Set the game start callback
            playerScreen.setOnGameStart(() -> {
                // Switch to PlayerActionDisplay when game starts
                actionDisplay.showActionDisplay();

                // Start listening for UDP events
                new Thread(() -> {
                    UDPReceive.listenForHits(event -> {
                        SwingUtilities.invokeLater(() -> {
                            actionDisplay.processEvent(event);
                        });
                    });
                }).start();
            });

            // Show the player screen
            playerScreen.showPlayerScreen();
            frame.setVisible(true);
        });
        timer.setRepeats(false);
        timer.start();
    }
}
