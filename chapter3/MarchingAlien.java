package Space_Invaders.chapter3;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class MarchingAlien extends Entity implements ConstantValues{

	private Sprite sprite;
	
	public MarchingAlien(GameEventListener controller, Dimension dim){
		super(controller, dim);
		sprite = new Sprite("sprites/alienSpriteSheet.png", 3);
	}

	@Override
	public void move(long tm) {
			rx = rx + vx * tm;
	}

	@Override
	public void draw(Graphics2D gc) {
		sprite.drawImage(gc, (int)rx, (int)ry, (int)rx + dimension.width, (int)ry + dimension.height, active);

	}
	
	public boolean inCollision (Entity e){

		if (e instanceof Wall){
			Wall w = (Wall) e;
			Rectangle r = w.getBounds();
   			if ((rx < r.x) || (rx > r.width - dimension.width)){
   				controller.requestLogic(this);
				return true;
			}
		}
		
		if (e instanceof Missile){
			Missile m = (Missile)e;
			Rectangle bounds = m.getBounds();
			if (bounds.intersects(this.getBounds())){
				Controller.alienCount = Controller.alienCount -1;
				controller.onEndOfLife(this);
				controller.onEndOfLife(m);
				return true;
			}
		}
		return false;
	}

	@Override
	public void processKeys(Byte keys) {
		// TODO Auto-generated method stub
		
	}

}
