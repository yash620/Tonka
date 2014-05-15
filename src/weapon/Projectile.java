package weapon;

import game.Collidable;
import game.Drawable;
import game.Game;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;

public abstract class Projectile implements Drawable, Collidable {
	private Shape projectileShape;
	protected double damage;
	protected double velocity;
	protected final double theta;
	protected Point2D.Double center;
	private Weapon weapon;
	private Game game;

	public Projectile(Shape s, int xstart, int ystart, double velocity, double damage, double theta, Game game){
		this.setProjectileShape(s);
		this.velocity = velocity;
		this.damage = damage;
		this.theta = theta;
		this.setGame(game);
		this.center = new Point2D.Double(xstart, ystart);
	}
	public abstract void update();
	@Override
	public abstract void draw(Graphics2D g2);
	public Shape getShape(){
		return getProjectileShape();
	}
	public abstract Shape getDestroyed();
	public Weapon getWeapon() {
		return weapon;
	}
	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	public Shape getProjectileShape() {
		return projectileShape;
	}
	public void setProjectileShape(Shape projectileShape) {
		this.projectileShape = projectileShape;
	}
}
