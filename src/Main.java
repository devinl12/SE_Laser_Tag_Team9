import javax.swing.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.function.Consumer;


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

                // Send the "202" start signal to the Python traffic generator
                try {
                    DatagramSocket socket = new DatagramSocket();
                    InetAddress address = InetAddress.getByName("127.0.0.1");
                    String startMessage = "202";
                    byte[] buffer = startMessage.getBytes();
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 7500);
                    socket.send(packet);
                    System.out.println("Sent start signal: " + startMessage);
                    socket.close(); // Close socket after sending
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

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

