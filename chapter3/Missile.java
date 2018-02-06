package Space_Invaders.chapter3;

import java.awt.Dimension;
import java.awt.Graphics2D;

public class Missile extends Entity {
 
private Sprite sprite;
private final float MAX_VELOCITY = -0.5f;
private final float ACCELERATION = -0.01f;
 
 
	public Missile(GameEventListener controller, Dimension dim){
		super(controller, dim);
		sprite = new Sprite("sprites/missileSpriteSheet.png", 3);
	}

@Override
	public void move(long tm) {
	ry = ry + vy * tm;
	if (vy < MAX_VELOCITY)
		vy = vy + ACCELERATION  * tm;
	if (vy >= MAX_VELOCITY)
		vy = MAX_VELOCITY;

	}

@Override
	public void draw(Graphics2D gc) {
		sprite.drawImage(gc, (int)rx, (int)ry, (int)rx + dimension.width, (int)ry + dimension.height, active);
	}

@Override
	public void processKeys(Byte keys) {
 
 
 
// TODO Auto-generated method stub

	}

@Override
	public boolean inCollision(Entity e) {
		if (e instanceof MarchingAlien){
			if (getBounds().intersects(e.getBounds())){
				controller.onEndOfLife(this);
				return true;
			}
		}
		return false;
	}
}
