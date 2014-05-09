package weapon;

import game.Drawable;
import game.Game;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;

public abstract class Projectile implements Drawable {
	protected Shape projectileShape;
	protected double velocity;
	protected final double theta;
	protected Game game;
	protected Point2D.Double center;

	public Projectile(Shape s, int xstart, int ystart, double velocity, double theta, Game game){
		this.projectileShape = s;
		this.velocity = velocity;
		this.theta = theta;
		this.game = game;
		this.center = new Point2D.Double(xstart, ystart);
	}
	public abstract void update();
	@Override
	public abstract void draw(Graphics2D g2);
	public Shape getShape(){
		return projectileShape;
	}
	public abstract Shape getDestroyed();
	public abstract void collided();
}
