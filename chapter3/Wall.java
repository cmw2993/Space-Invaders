package Space_Invaders.chapter3;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Wall extends Entity implements ConstantValues {

private Rectangle bounds;
	
	public Wall(int x, int y, Dimension dim, GameEventListener listener){
		super(listener);
		bounds = new Rectangle(x, y, dim.width, dim.height);
	}

	@Override
	public void move(long tm) {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(Graphics2D gc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processKeys(Byte keys) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean inCollision(Entity e) {
		if (e.getRx() + e.dimension.width > bounds.width + bounds.x)
			return true;
		if (e.getRx() < bounds.x)
			return true;
		if (e.getRy() > bounds.height + bounds.y)
			return true;
		if (e.getRy() < bounds.y)
			return true;
		
		return false;
		// return !bounds.contains(new Point((int)e.getRx(), (int)e.getRy()));
	}
	
	public Rectangle getBounds(){
		return new Rectangle(bounds);
	}

}