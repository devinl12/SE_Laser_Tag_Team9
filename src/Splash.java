import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;

public class Splash {
    public static void showSplashScreen(JFrame mainFrame) {
       

        //Create new window
        JWindow window = new JWindow();
        window.setBounds(100,100,400,300); 
  
        JLabel label = new JLabel(new ImageIcon("src/assets/images/splash.jpg"));
        window.getContentPane().add(label, BorderLayout.CENTER);
        window.setVisible(true);

        //Show splash screen for five seconds
        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.dispose(); 
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
}
