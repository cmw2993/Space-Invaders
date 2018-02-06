package Space_Invaders.chapter3;

public interface GameEventListener {
	void onEndOfLife(Entity e);
	void onFire(Entity e);
	void requestLogic(Entity e);
}
