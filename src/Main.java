import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
      
        //Create main screen
        JFrame frame = new JFrame("Photon Laser Tag System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        //Show splash screen
        Splash.showSplashScreen(frame);

        //Remove splash screen photo after 5 secs
        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Label main screen
                JLabel label = new JLabel("Photon Laser Tag System", SwingConstants.CENTER);
                frame.add(label);
                
                //Show main screen
                frame.setVisible(true);
            }
        });
        timer.setRepeats(false);
        timer.start();
    
}
}