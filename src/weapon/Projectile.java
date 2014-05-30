package weapon;

import game.Game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import util.Collidable;
import util.Drawable;
import util.Updatable;

public abstract class Projectile implements Drawable, Collidable, Updatable, Serializable {
	private Shape projectileShape;
	protected double damage;
	protected double velocity;
	protected final double theta;
	protected Point2D.Double center;
	private Weapon weapon;
	protected Game game;

	public Projectile(Shape s, int xstart, int ystart, double velocity, double damage, double theta, Weapon weapon, Game game){
		this.setProjectileShape(s);
		this.velocity = velocity;
		this.damage = damage;
		this.theta = theta;
		this.center = new Point2D.Double(xstart, ystart);
		this.game = game;
		this.weapon = weapon;
	}
	
	//Update is called everytime the timer tics
	@Override
	public abstract void update();
	//Draws the missile
	@Override
	public void draw(Graphics2D g2){
		g2.setColor(Color.black);
		g2.draw(getProjectileShape());
	}
	public Shape getShape(){
		return getProjectileShape();
	}
	//Returns the same of the destroyed missile, used to create the block destruction patters
	public abstract Shape getDestroyed();
	public Weapon getWeapon() {
		return weapon;
	}
	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}
	public Shape getProjectileShape() {
		return projectileShape;
	}
	public void setProjectileShape(Shape projectileShape) {
		this.projectileShape = projectileShape;
	}

	/*This is called by the other collidable when it collides with the missile
	 * The missiles should call the other collidables collision method
	 */
	@Override
	public abstract void collision(Collidable c);

	//If the missile is colliding
	@Override
	public boolean isColliding(Collidable c) {
		if (c.equals(this) || c.equals(getWeapon().getTank()) || c instanceof Projectile) {
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

	//The bounding box of the missile, used for collision checking
	@Override
	public Rectangle getBoundingBox(){
		return this.projectileShape.getBounds();
	}
}
