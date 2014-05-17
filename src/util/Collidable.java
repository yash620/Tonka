package util;

import java.awt.Shape;

public interface Collidable {
	public void collision(Collidable c);
	public boolean isColliding(Collidable c);
	public Shape getShape();
	public Shape getBoundingBox();
}
