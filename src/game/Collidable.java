package game;

import java.awt.Shape;

public interface Collidable {
	public boolean collisionCheck(Collidable c);
	public Shape getShape();
	public Shape getBoundingBox();
}
