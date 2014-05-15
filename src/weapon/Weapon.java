package weapon;

import game.Drawable;
import game.Tank;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Point2D;

public abstract class Weapon implements Drawable {
	protected Shape weaponShape;
	protected double turnSpeed;
	protected Point2D center;
	protected int angle;
	protected int tgtAngle;
	private Tank t;
	private Projectile projectile;
	protected int ammo;
	protected double spread;
	public abstract void update();
	public abstract void clickPoint(Point tgt);
//	public abstract Point getProjectileSpawn();
	public abstract Projectile shoot();
	public abstract boolean canShoot();
	public Tank getTank(){
		return this.t;
	}
	public void setTank(Tank t) {
		this.t = t;
	}
	public Projectile getProjectile() {
		return projectile;
	}
	public void setProjectile(Projectile projectile) {
		this.projectile = projectile;
	}
}
