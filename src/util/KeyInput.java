package util;

import java.awt.Point;
import java.io.Serializable;

public class KeyInput implements Serializable {
	private int down, right;
	private Point clickPoint;
	private boolean shoot;
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
	public void setDown(int down) {
		this.down = down;
	}
	public void setRight(int right) {
		this.right = right;
	}
	public void setClickPoint(Point clickPoint) {
		this.clickPoint = clickPoint;
	}
	public void setShoot(boolean shoot) {
		this.shoot = shoot;
	}
}
