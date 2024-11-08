import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class Music {
    private Clip clip;

    public void playMusic(List<String> mp3Files) {
        
        Random random = new Random();
        int randomNum = random.nextInt(mp3Files.size());
        String filePath = mp3Files.get(randomNum);

            try {
                File musicFile = new File(filePath);
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicFile);
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.loop(Clip.LOOP_CONTINUOUSLY); 
                clip.start();
            }catch (Exception e) {
                System.err.println("Error playing audio");
            } 
        
    }

    public void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}
