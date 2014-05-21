package weapon;

import game.Tank;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.io.Serializable;

import util.Drawable;
import util.Updatable;

public abstract class Weapon implements Drawable, Updatable, Serializable {
	private Shape weaponShape;
	private double turnSpeed;
	private Point2D center;
	private int angle;
	private int tgtAngle;
	private Tank t;
	private Projectile projectile;
	private int ammo;
	private double firerate;
	private double spread;

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

	public Shape getWeaponShape() {
		return weaponShape;
	}

	public void setWeaponShape(Shape weaponShape) {
		this.weaponShape = weaponShape;
	}

	public Point2D getCenter() {
		return center;
	}

	public void setCenter(Point2D center) {
		this.center = center;
	}

	public double getTurnSpeed() {
		return turnSpeed;
	}

	public void setTurnSpeed(double turnSpeed) {
		this.turnSpeed = turnSpeed;
	}

	public int getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}

	public int getTgtAngle() {
		return tgtAngle;
	}

	public void setTgtAngle(int tgtAngle) {
		this.tgtAngle = tgtAngle;
	}

	public int getAmmo() {
		return ammo;
	}

	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}

	public double getFirerate() {
		return firerate;
	}

	public void setFirerate(double firerate) {
		this.firerate = firerate;
	}

	public double getSpread() {
		return spread;
	}

	public void setSpread(double spread) {
		this.spread = spread;
	}
}
