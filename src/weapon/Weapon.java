package weapon;

import game.Tank;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.io.Serializable;

import util.Drawable;
import util.Updatable;

public abstract class Weapon implements Drawable, Updatable, Serializable {
	protected Shape weaponShape;
	protected double turnSpeed;
	protected Point2D center;
	protected int angle;
	protected int tgtAngle;
	private Tank t;
	private Projectile projectile;
	protected int ammo;
	protected double firerate;
	protected double spread;  //How large the spread of the weapon is

	//Called whenever the timer tics
	@Override
	public abstract void update();

	//Sets the target angle of the weapon
	public abstract void clickPoint(Point tgt);

	//Returns the projectile that the weapon shoots
	public Projectile shoot(){
		return projectile;
	}
	
	//Returns if the weapon can shoot or not
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
