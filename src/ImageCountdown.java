import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ImageCountdown extends JPanel {
	private final String[] countdownImages = {
		"10.jpg", "9.jpg", "8.jpg", "7.jpg", "6.jpg",
		 "5.jpg", "4.jpg", "3.jpg", "2.jpg", "1.jpg", "0.jpg"
	};
	private final JLabel imageLabel = new JLabel();
	private int currentIndex = 0;
	private Timer timer;
	private final String backgroundImagePath = "assets/images/countdown_images/background.jpg";

	public ImageCountdown() {
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
	}

	public void startCountdown(JFrame frame) {
		frame.getContentPane().removeAll();
		frame.add(this);

		currentIndex = 0;
		timer = new Timer(1000, new CountdownAction());
		timer.start();

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
			} else {
				timer.stop();
				imageLabel.setText("");
				imageLabel.setIcon(null);
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

