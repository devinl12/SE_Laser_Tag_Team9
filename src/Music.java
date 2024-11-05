import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Music {
    private Clip clip;

    public void playMusic(List<String> mp3Files) {
        for (String filePath : mp3Files) {
            try {
                File musicFile = new File(filePath);
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicFile);
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.loop(Clip.LOOP_CONTINUously); 
                clip.start();
            }catch (IOException e) {
                System.err.println("Error playing audio file: " + filePath);
            } 
        }
    }

    public void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}
