package weapon;

import game.Drawable;
import game.Tank;
import java.awt.Point;
import java.awt.Shape;

public abstract class Weapon implements Drawable {
	protected Shape weaponShape;
	protected Projectile projectile;
	protected double turnSpeed;
	protected Point center;
	protected int angle;
	protected int tgtAngle;
	protected Tank t;
	protected int ammo;
	protected double spread;
	public abstract void update();
	public abstract void clickPoint(Point tgt);
//	public abstract Point getProjectileSpawn();
	public abstract Projectile shoot();
	public abstract boolean canShoot();
}
