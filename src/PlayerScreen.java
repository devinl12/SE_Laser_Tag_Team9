import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;

public class PlayerScreen extends JPanel {

    private String playerName;
    private int playerCode;
 
    // Constructor to initialize the player screen with player info
    public PlayerScreen(String playerName, int pc) {
        this.playerName = playerName;
        this.playerCode = pc;

        // Set the background color
        setBackground(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Call the superclass method to ensure the panel is drawn correctly

        // Set text color and font
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));

        // Draw player's name
        g.drawString("Player: " + playerName, 20, 40);  

        // Draw player's health
        g.drawString("Player Code: " + playerCode, 20, 80);  
    }
}