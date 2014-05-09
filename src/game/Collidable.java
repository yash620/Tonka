package game;

import java.awt.Shape;

public interface Collidable {
	public boolean collisionCheck(Shape s);
	public Shape getShape();
}
