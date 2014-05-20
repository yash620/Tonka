package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;

import util.Collidable;
import util.Drawable;
import weapon.Projectile;
import weapon.Weapon;

public class Tank implements Drawable, Collidable, Serializable {
	private int hp;
	private double speed;
	private int turnSpeed;
	private int theta;
	private ArrayList<Weapon> myWeapons;
	private Shape tankShape;
	private double xcenter;
	private double ycenter;
	private Color color;
	
	public Tank(double x, double y, ArrayList<Weapon> weapons){
		this.xcenter = x;
		this.ycenter = y;
		this.myWeapons = weapons;
		this.hp = 100;
		this.speed = 3;
		this.turnSpeed = 5;
		setColor(Color.green);
		tankShape = new Rectangle((int)x-20, (int)y-10, 35,20);
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
		for (Weapon w : myWeapons){
			w.draw(g2);
		}
	}
	
	public void shoot(Point shootPoint){
		for (Weapon w : myWeapons){
			if (w.canShoot()){
				Game.addQueue(w.shoot());
			}
		}
	}
	
	public void movement(int down, int right, Point clickpoint, boolean shoot){
		int originalTheta = getTheta();
		int tgtTheta = this.determineTheta(right);
		double tempSpeed = speed;
		if (down != 0 && right != 0){
			tempSpeed = tempSpeed/Math.sqrt(2);
		}
		double xvel = tempSpeed * Math.cos(Math.toRadians(tgtTheta)) * down;
		double yvel = tempSpeed * Math.sin(Math.toRadians(tgtTheta)) * down;
		xcenter += xvel;
		ycenter += yvel;
		
		tankShape = Transform.transform(tankShape, xvel, yvel,
				Math.toRadians(tgtTheta-getTheta()), xcenter, ycenter);
		setTheta(tgtTheta);
		ArrayList<Collidable> collisions = Game.getCollisions(this);
		boolean blocked = false;
		for (Collidable c : collisions){
			c.collision(this);
			if (c instanceof Block || c instanceof Tank){
				blocked = true;
			}
		}
		if (blocked){
			xcenter -= xvel;
			ycenter -= yvel;
			tankShape = Transform.transform(tankShape, -xvel, -yvel, Math.toRadians(originalTheta - getTheta()), xcenter, ycenter);
			setTheta(originalTheta);
		}
		
		for (Weapon w : myWeapons){
			w.clickPoint(clickpoint);
			w.update();
		}
		
		if (shoot){
			this.shoot(clickpoint);
		}
	}
	private int determineTheta(int right){
		return getTheta() + right * turnSpeed;
	}

	public Point2D.Double getCenter(){
		return new Point2D.Double(xcenter, ycenter);
	}

	public int getTheta() {
		return theta;
	}

	public void setTheta(int theta) {	
		this.theta = (int)AngleMath.adjustAngle(theta);
	}

	@Override
	public void collision(Collidable c) {
		if (c instanceof Projectile){
			c.collision(this);
		}
		if(getHp() <= 0){
			Game.removeQueue(this);
			Game.addQueue(new Explosion(getCenter()));
		}
	}

	@Override
	public Shape getShape() {
		return this.tankShape;
	}

	@Override
	public Shape getBoundingBox() {
		return this.tankShape.getBounds();
	}

	@Override
	public boolean isColliding(Collidable c) {
		if (c.equals(this)){
			return false;
		}
		if (c.getBoundingBox().intersects((Rectangle2D) this.getBoundingBox())){
			Area tankArea = new Area(this.getShape());
			tankArea.intersect(new Area(c.getShape()));
			if (!tankArea.isEmpty()){
				return true;
			}
		}
		return false;
	}
	
	public int getHp(){
		return hp;
	}
	
	public void setHp(int hp){
		this.hp = hp;
	}
	
	public void setColor(Color c){
		color = c;
	}
}
