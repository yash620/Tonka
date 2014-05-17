package weapon;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;

import util.Collidable;
import util.Drawable;
import util.Updatable;

public abstract class Projectile implements Drawable, Collidable, Updatable {
	private Shape projectileShape;
	protected double damage;
	protected double velocity;
	protected final double theta;
	protected Point2D.Double center;
	private Weapon weapon;

	public Projectile(Shape s, int xstart, int ystart, double velocity, double damage, double theta){
		this.setProjectileShape(s);
		this.velocity = velocity;
		this.damage = damage;
		this.theta = theta;
		this.center = new Point2D.Double(xstart, ystart);
	}
	
	//Update is called everytime the timer tics
	@Override
	public abstract void update();
	//Draws the missile
	@Override
	public abstract void draw(Graphics2D g2);
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
	public abstract boolean isColliding(Collidable c);

	//The bounding box of the missile, used for collision checking
	@Override
	public Shape getBoundingBox(){
		return this.projectileShape.getBounds();
	}
}
