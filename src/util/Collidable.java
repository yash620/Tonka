package util;

import java.awt.Rectangle;
import java.awt.Shape;

public interface Collidable {
	public void collision(Collidable c);
	public boolean isColliding(Collidable c);
	public Shape getShape();
	public Rectangle getBoundingBox();
}
