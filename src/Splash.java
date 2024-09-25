import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Splash {
    private static BufferedImage splashImage;

    // Displays splash screen
    public static void showSplashScreen(JFrame mainFrame) {
        // Load splash image
        imageLoad();

        // Create splash window
        JWindow window = new JWindow();
        window.setSize(800, 600);
        window.setLocationRelativeTo(null);

        // Create panel to draw image
        JPanel imagePanel = new JPanel() {
            // Draw splash image
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (splashImage != null) {
                    g.drawImage(splashImage, 0, 0, this.getWidth(), this.getHeight(), this);
                }
            }
        };

        // Add image panel to window
        window.getContentPane().add(imagePanel, BorderLayout.CENTER);
        window.setVisible(true);

        // Show splash screen for five seconds
        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.dispose();
                // Make the main frame invisible until player screen is shown
                mainFrame.setVisible(false);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    // Load Splash Image
    private static void imageLoad() {
        try {
            splashImage = ImageIO.read(Splash.class.getResourceAsStream("/assets/images/splash.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}