import javax.swing.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.function.Consumer;
import java.net.InetAddress;


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
       PlayerActionDisplay actionDisplay = new PlayerActionDisplay(frame);
          // Pass the shared instance to ImageCountdown
        ImageCountdown countdown = new ImageCountdown(actionDisplay); //NEW

          // Start listening for UDP events
              new Thread(() -> {
                  System.out.println("Starting UDP listener..."); // Debug line
                  UDPReceive.listenForHits(event -> {
                      System.out.println("Received event: " + event); // Debug line
                      SwingUtilities.invokeLater(() -> {
                          System.out.println("Processing event on Swing thread: " + event); // Debug line
                          actionDisplay.processEvent(event);
                      });
                  });
              }).start();
              System.out.println("UDP listener thread started MAIN.");

          // Set the game start callback
          playerScreen.setOnGameStart(() -> {
            countdown.startCountdown(frame); //NEW
            actionDisplay.showActionDisplay();
          });

          // Show the player screen
          playerScreen.showPlayerScreen();
          frame.setVisible(true);
      });
      timer.setRepeats(false);
      timer.start();
  }
}



