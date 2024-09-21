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
        System.out.print("Showing Spalsh Screen");

        // Timer to remove splash screen after 5 secs
        Timer timer = new Timer(5000, e -> {
            // Remove splash screen and show the player screen
            playerScreen playerScreen = new playerScreen(frame);
            playerScreen.showPlayerScreen();
        });
        timer.setRepeats(false);
        timer.start();
    }
}
