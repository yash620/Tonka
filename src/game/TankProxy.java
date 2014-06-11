package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.io.Serializable;
import java.util.ArrayList;

import util.Drawable;
import weapon.Weapon;


public class TankProxy implements Serializable, Drawable {
	private final Color color;
	private final Shape tankShape;
	private final double xcenter;
	private final double ycenter;
	private final double hp;
	private final ArrayList<Drawable> myWeapons;
	private final int team;
	
	public TankProxy(Color c, Shape s, double x, double y, double hp, ArrayList<Weapon> weps, int team){
		this.color = c;
		this.tankShape = s;
		this.xcenter = x;
		this.ycenter = y;
		this.hp = hp;
		myWeapons = new ArrayList<Drawable>();
		for (Weapon w : weps){
			myWeapons.add(w.getProxyClass());
		}
		this.team = team;
	}

	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(color);
		g2.fill(tankShape);
		g2.setColor(Color.black);
		g2.draw(tankShape);
		g2.setColor(Color.red);
		g2.fillRect((int)xcenter - 25, (int)ycenter - 30, (int)(((double)this.hp)/100 * 50), 5);
		g2.setColor(Color.black);
		g2.drawRect((int)xcenter - 25, (int)ycenter - 30, 50, 5);
		//g2.drawString("HP" + this.hp, (int)xcenter, (int)ycenter);
		g2.setColor(color);
		for (Drawable d : myWeapons){
			d.draw(g2);
		}
	}
	public int getTeam() {
		return team;
	}
	public double getX() {
		return this.xcenter;
	}
	public double getY() {
		return this.ycenter;
	}
}