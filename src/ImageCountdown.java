import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.function.Consumer;
import java.net.InetAddress;

public class ImageCountdown extends JPanel {
	private final String[] countdownImages = {
		"30.jpg", "29.jpg", "28.jpg", "27.jpg", "26.jpg",
		"25.jpg", "24.jpg", "23.jpg", "22.jpg", "21.jpg",
		"20.jpg", "19.jpg", "18.jpg", "17.jpg", "16.jpg",
		"15.jpg", "14.jpg", "13.jpg", "12.jpg", "11.jpg",
		"10.jpg", "9.jpg", "8.jpg", "7.jpg", "6.jpg",
		 "5.jpg", "4.jpg", "3.jpg", "2.jpg", "1.jpg", "0.jpg"
	};
	private final JLabel imageLabel = new JLabel();
	private int currentIndex = 0;
	private Timer timer;
	private final String backgroundImagePath = "assets/images/countdown_images/background.jpg";
	private Music music;
	private final PlayerActionDisplay actionDisplay;


	// Fields to hold the player lists 
	private List<String[]> redTeamPlayers;
	private List<String[]> greenTeamPlayers;



	public ImageCountdown(List<String[]> redTeamPlayers, List<String[]> greenTeamPlayers, PlayerActionDisplay actionDisplay) {
	   this.redTeamPlayers = redTeamPlayers;
	   this.greenTeamPlayers = greenTeamPlayers;
	   this.actionDisplay = actionDisplay;

	   setLayout(new BorderLayout());

	   BackgroundPanel backgroundPanel = new BackgroundPanel();
	   backgroundPanel.setLayout(new BorderLayout());

	   JPanel imagePanel = new JPanel(new BorderLayout());
	   imagePanel.setOpaque(false);
	   imagePanel.setBorder(BorderFactory.createEmptyBorder(100,0,0,0));

	   imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
	   imageLabel.setVerticalAlignment(SwingConstants.CENTER);
 
	   imagePanel.add(imageLabel, BorderLayout.CENTER);
	   backgroundPanel.add(imagePanel, BorderLayout.CENTER);
	   add(backgroundPanel);

	   music = new Music();
	}


	public void startCountdown(JFrame frame) {
		frame.getContentPane().removeAll();
		frame.add(this);

		currentIndex = 0;
		timer = new Timer(1000, new CountdownAction());
		timer.start();

		List<String> mp3Files = List.of(
            "assets/sounds/Track01.wav", "assets/sounds/Track02.wav", "assets/sounds/Track03.wav",
			"assets/sounds/Track04.wav", "assets/sounds/Track05.wav", "assets/sounds/Track06.wav",
			"assets/sounds/Track07.wav", "assets/sounds/Track08.wav"
        );
		
		//delays start of music for 13 seconds
		Timer musicTimer = new Timer(13000, new ActionListener() {
    			@Override
    			public void actionPerformed(ActionEvent e) {
       			 music.playMusic(mp3Files);
   			 }
		});
		musicTimer.setRepeats(false); 
		musicTimer.start();

		frame.revalidate();
		frame.repaint();
	}

	private class CountdownAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (currentIndex < countdownImages.length) {
				ImageIcon icon = new ImageIcon("assets/images/countdown_images/" + countdownImages[currentIndex]);
				imageLabel.setIcon(icon);
				currentIndex++;
				//
			} else {
				timer.stop();
				imageLabel.setText("");
				imageLabel.setIcon(null);

				// Send the "202" start signal
		// Send the "202" start signal
		try {
			//DatagramSocket socket = new DatagramSocket(); THIS
			//InetAddress address = InetAddress.getByName("127.0.0.1"); THIS
			//String startMessage = "202";
			//byte[] buffer = startMessage.getBytes();
			//DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 7500);
			//socket.send(packet);
			//socket.close();
			UDPTransmit.transmitEquipmentCode(202)
			System.out.println("Sent start signal IC: " + startMessage);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

				// Switch to PlayerActionDisplay after the countdown ends
				JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(ImageCountdown.this);
				actionDisplay.showActionDisplay();

				// Assuming redTeamPlayers and greenTeamPlayers are lists with player data collected earlier
                actionDisplay.populateTeams(redTeamPlayers, greenTeamPlayers);
				
				parentFrame.revalidate();
				parentFrame.repaint();
			}
		}
	}

	private class BackgroundPanel extends JPanel {
		private Image backgroundImage;

		public BackgroundPanel() {
			ImageIcon icon = new ImageIcon(getClass().getResource(backgroundImagePath));
			backgroundImage = icon.getImage();
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
		}
	}

}

