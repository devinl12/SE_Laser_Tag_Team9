import javax.sound.sampled.*;
import java.io.File;
import java.util.List;
import java.util.Random;
import java.io.IOException;


public class Music {
    
    private Random random;
    private Clip clip;


    public Music() {
        random = new Random();
    }

    // play a random mp3 track
     public void playMusic(List<String> mp3Files) {

        String track = mp3Files.get(random.nextInt(mp3Files.size()));

        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(track));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY); 
        } catch (IOException e) {} 
    }




    public void stopMusic() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }



}
