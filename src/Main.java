import javax.swing.*;

public class Main {


    public static void main(String[] args) {
    
        JFrame frame = new JFrame("Photon Laser Tag System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null); 

        // Show splash screen
        Splash.showSplashScreen(frame);

        // Show home screen
        JLabel label = new JLabel("Photon Laser Tag System", SwingConstants.CENTER);
        frame.add(label);
        frame.setVisible(true);
    }
}
