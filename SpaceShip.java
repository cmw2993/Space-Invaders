package Space_Invaders.chapter3;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class SpaceShip extends Entity implements ConstantValues {

	private Sprite sprite;
	private final float MAX_VELOCITY = 0.4f;
	private final float DELTA_VELOCITY = 0.02f;
	private final long FIRING_INTERVAL = 160;
	private long lastFired = System.currentTimeMillis();
	
	public SpaceShip(GameEventListener controller, Dimension dim){
		super(controller, dim);
		sprite = new Sprite("sprites/shipSpriteSheet.png", 3);
	}
	
	@Override
	public void move(long tm) {
		rx = rx + vx * tm;
		// TODO Auto-generated method stub
		// should move only horizontally
	}

	@Override
	public void draw(Graphics2D gc) {
		sprite.drawImage(gc, (int)rx, (int)ry, (int)rx + dimension.width, (int)ry + dimension.height, active);
	}

	@Override
	public void processKeys(Byte keys) {
		if ( (keys & LEFT_KEY) != 0){
			vx = -Math.abs(vx) - DELTA_VELOCITY;
			if ( (keys & (UP_KEY | DOWN_KEY)) == 0)
				vy = 0;
		}
		if ( (keys & RIGHT_KEY) != 0){
			vx = Math.abs(vx) + DELTA_VELOCITY;
			if ( (keys & (UP_KEY | DOWN_KEY)) == 0)
				vy = 0;
		}
		if ((keys & SPACE_KEY) != 0){
			
			if ((System.currentTimeMillis() - lastFired) < FIRING_INTERVAL)
				return;
			
			if (controller == null)
				throw new NullPointerException();
			
			controller.onFire(this);
			lastFired  = System.currentTimeMillis();
		}
		if ( (keys & 0x0F) == 0){
			vx = 0;
			vy = 0;
			active = false;
		} else
			active = true;
		
		if (vx < -MAX_VELOCITY)
			vx = -MAX_VELOCITY;
		if (vx > MAX_VELOCITY)
			vx = MAX_VELOCITY;
		// TODO Auto-generated method stub
		// left key => take abs vx and decrement by delta velocity
		// right key => take abs vx and increment by delta velocity
		// handle key release and vx limits as in Alien2
		// do not modify vertical velocity and ignore all other keys
		// if the space is pressed then call controller.onFire(this)
	}

	@Override
	public boolean inCollision(Entity e) {
		if (e instanceof Wall){
			Wall w = (Wall) e;
			Rectangle r = w.getBounds();
			
			if (rx <= r.x){
				vx = 0;
				rx = r.x;
			}
			if (rx >= (r.x + r.width) - dimension.width){
				vx = 0;
				rx = (r.x + r.width) - dimension.width;
			}
			
			// check if rx is within bounds of the wall
		}
		if(e instanceof MarchingAlien){
			if(getBounds().intersects(e.getBounds())){
				controller.onEndOfLife(this);
			
				return true;
			}
		}
		if(e instanceof BossAlien){
			if(getBounds().intersects(e.getBounds())){
				controller.onEndOfLife(this);
			
				return true;
			}
		}
		return false;
		
	}



}