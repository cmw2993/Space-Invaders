package Space_Invaders.chapter3;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public enum SoundFX implements LineListener {
	

	WIN("audio/win.wav"), LOSE("audio/lose.wav"),
	COLLIDE("audio/hit.wav"), STARTUP("audio/start.wav"),
	SHOOT("audio/laser.wav");
	
	
	
	
	
	
	
	public static enum Volume{
		Mute, Low, Medium, High
	}
	private Clip clip;
	private SoundFX(String resid){
		try{
		URL url = this.getClass().getClassLoader().getResource(resid);
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
		AudioFormat format = audioInputStream.getFormat();
		DataLine.Info info = new DataLine.Info(Clip.class, format);
		
		if(!AudioSystem.isLineSupported(info))
			new LineUnavailableException("Line is not supported!");
		
		clip = (Clip) AudioSystem.getLine(info);
		clip.addLineListener(this);
		
		clip.open(audioInputStream);
		}catch(UnsupportedAudioFileException e){
			e.printStackTrace();
		}catch (LineUnavailableException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void play(){
		final String caller = this.toString();
		
	Runnable playNote =	 new Runnable() {
			public void run() {
					clip.stop();
				clip.setFramePosition(0);
				clip.start();
			}
		};
	Thread playThread = new Thread(playNote);
	playThread.start();
	}
	
	
	public static void init(){
		values();
	}
	
	
	@Override
	public void update(LineEvent event) {
LineEvent.Type type = event.getType();		
if( type == LineEvent.Type.START){
	
}
if(type == LineEvent.Type.STOP){
	
}

	}
public static void main(String[] args){
	int n = 0;
	while (n< 5){
	SoundFX.SHOOT.play();
	try{
		Thread.sleep(2000);
		
	} catch (InterruptedException e){
		e.printStackTrace();
	}
	n++;
	}
}
}
