package Space_Invaders.chapter3;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class Sprite {
	private int frameCount;
	private int[] srcIndex;
	private int curr = 0;
	private Image image;

	public Sprite(String resid, int frames) {
		
		frameCount = frames;
	
		try {
			URL url = this.getClass().getClassLoader().getResource(resid);
			if (url == null) {
				System.err.println("Sprite not found!");
			}
			image = ImageIO.read(url);
		} catch (IOException e) {
			System.err.println("failed to load Sprite!");
		}

		srcIndex = new int[frameCount];
		
		for (int i = 0; i < frameCount; i++){
			int w = image.getWidth(null)/frameCount;
			srcIndex[i] = i * w;
		}
		
	}

	public void drawImage(Graphics2D gc, int rx, int ry, int width, int height, boolean active) {
		int sw = image.getWidth(null)/frameCount + srcIndex[curr];
		gc.drawImage(image,  rx, ry, width, height, 
				srcIndex[curr], 0, sw, image.getHeight(null), null);
		if (active) {
			curr++;
			curr %= frameCount;
		}
	}

	
}
