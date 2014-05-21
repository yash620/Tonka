package util;

import java.awt.Point;
import java.io.Serializable;

public final class KeyInput implements Serializable {
	private final int down, right;
	private final Point clickPoint;
	private final boolean shoot;
	public KeyInput(int down, int right, Point clickPoint, boolean shoot){
		this.down = down;
		this.right = right;
		this.clickPoint = clickPoint;
		this.shoot = shoot;
	}
	public int getRight() {
		return right;
	}
	public Point getClickPoint() {
		return clickPoint;
	}
	public boolean isShoot() {
		return shoot;
	}
	public int getDown() {
		return down;
	}
}
