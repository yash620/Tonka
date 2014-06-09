package weapon;

import game.AngleMath;
import game.Tank;
import game.Transform;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import util.Drawable;
import util.Sendable;
import util.Timer;
import util.Updatable;
import util.Timer.Action;

public abstract class Weapon implements Drawable, Updatable, Sendable {
	private Shape weaponShape;
	private double turnSpeed;
	private Point2D center;
	private int angle;
	private int tgtAngle;
	private Tank t;
	private int ammo;
	private double firerate;
	private double spread;
	private boolean canFire = true;
	private int cost;
	private double dtot;
	private double atot;
	private HashSet<Timer> allTimers;
	private AffineTransform old;
	private final int MAXAMMO;

	
	public Weapon(Tank t, double turnSpeed, Point2D center, int ammo, double firerate,
			double spread, double angletotank, double distancetotank){
		this.t = t;
		this.turnSpeed = turnSpeed;
		this.center = center;
		this.ammo = ammo;
		this.firerate = firerate;
		this.spread = spread;
		allTimers = new HashSet<Timer>();
		old = new AffineTransform();
		atot = angletotank;
		dtot = distancetotank;
		this.MAXAMMO = ammo;
	}
	public Weapon(Tank t, double turnSpeed, Point2D center, int ammo, double firerate,
			double spread){
		this(t,turnSpeed, center, ammo, firerate, spread, 0, 0);
	}
	
	public void replenishAmmo() {
		this.setAmmo(MAXAMMO);
	}
	public abstract void updateSpread();
	
	@Override
	public void draw(Graphics2D g2) {
		old = g2.getTransform();
		g2.rotate(Math.toRadians(getAngle()), getCenter().getX(), getCenter().getY());
		g2.setColor(Color.black);
		g2.fill(getWeaponShape());
		g2.setTransform(old);
		//g2.drawOval((int)center.getX()-2, (int)center.getY()-2, 4, 4);
		g2.drawString(Integer.toString(ammo), (int)center.getX(), (int)center.getY()-10);
	}

	//Called whenever the timer tics
	@Override
	public void update(){
		Iterator<Timer> iter = allTimers.iterator();
		while (iter.hasNext()){
			Action a = iter.next().tick();
			if (a == null){
				continue;
			}
			if (a == Action.AMMO){
				this.replenishAmmo();
			}
			if (a == Action.FIRE) {
				this.setCanFire(true);
			}
			if (a == Action.SPREAD){
				this.updateSpread();
			}
			iter.remove();
		}
		int diffangle = AngleMath.adjustAngle(getTgtAngle() - getAngle());
//		double dtheta = 0;
		if (diffangle > 0){
			setAngle((int) (getAngle() + getTurnSpeed()));
//			dtheta = turnSpeed;
			if (AngleMath.adjustAngle(getTgtAngle() - getAngle()) < 0){
//				dtheta = AngleMath.adjustAngle(tgtAngle - angle);
				setAngle(getTgtAngle());
			}
		}
		if (diffangle < 0){
			setAngle((int) (getAngle() - getTurnSpeed()));
//			dtheta = -turnSpeed;
			if (AngleMath.adjustAngle(getTgtAngle() - getAngle()) > 0){
//				dtheta = AngleMath.adjustAngle(tgtAngle - angle);
				setAngle(getTgtAngle());
			}
		}
		double deltax = getTank().getCenter().getX() + (dtot * Math.cos(Math.toRadians(getTank().getTheta() + atot)) - getCenter().getX());
		double deltay = getTank().getCenter().getY() + (dtot * Math.sin(Math.toRadians(getTank().getTheta() + atot)) - getCenter().getY());
		setCenter(new Point2D.Double(getTank().getCenter().getX() + (dtot * Math.cos(Math.toRadians(getTank().getTheta() + atot))), getTank().getCenter().getY() + (dtot * Math.sin(Math.toRadians(getTank().getTheta() + atot)))));
		setWeaponShape(Transform.transform(getWeaponShape(), deltax, deltay, 0,
				getCenter().getX(), getCenter().getY()));
	}
	
	public void moveTo(Point2D point){
		double deltax = point.getX() - getCenter().getX();
		double deltay = point.getY() - getCenter().getY();
		setCenter(point);
		setWeaponShape(Transform.transform(getWeaponShape(), deltax, deltay, 0,
				getCenter().getX(), getCenter().getY()));
	}

	//Sets the target angle of the weapon
	public void clickPoint(Point2D tgt){
		double tgtAng = Math.atan2(tgt.getY() - getCenter().getY(), tgt.getX() - getCenter().getX());
		this.setTgtAngle(AngleMath.adjustAngle((int)Math.toDegrees(tgtAng)));
	}

//	//Returns the projectile that the weapon shoots
	public abstract ArrayList<Projectile> shoot();
	
	//Returns if the weapon can shoot or not
	public boolean canShoot() {
		return this.canFire && this.ammo > 0;
	}
	public Tank getTank(){
		return this.t;
	}
	public void setTank(Tank t) {
		this.t = t;
	}
//	public Projectile getProjectile() {
//		return projectile;
//	}
//	public void setProjectile(Projectile projectile) {
//		this.projectile = projectile;
//	}

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

	public void setCanFire(boolean canFire) {
		this.canFire = canFire;
	}
	
	public void addTimer(Timer t){
		allTimers.add(t);
	}
	
	public int getCost(){
		return cost;
	}
	
	public void attachToTank(Tank tank){
		t = tank;
		dtot = Math.sqrt((this.getCenter().getX() - t.getCenter().getX())*(this.getCenter().getX() - t.getCenter().getX()) + (this.getCenter().getY() - t.getCenter().getY())*(this.getCenter().getY() - t.getCenter().getY()));
		atot = Math.atan2(this.getCenter().getY() - t.getCenter().getY(), this.getCenter().getX() - t.getCenter().getX());
	}
	public int getMAXAMMO() {
		return MAXAMMO;
	}
	@Override
	public Drawable getProxyClass() {
		return new WeaponProxy(this.getAngle(), this.getCenter().getX(),
				this.getCenter().getY(), this.getWeaponShape(), this.getAmmo());
	}
}

class WeaponProxy implements Serializable, Drawable {
	private final double theta;
	private final double xcenter;
	private final double ycenter;
	private final Shape shape;
	private final int ammo;
	
	public WeaponProxy(double theta, double xcenter, double ycenter, Shape s, int ammo){
		this.theta = theta;
		this.xcenter = xcenter;
		this.ycenter = ycenter;
		this.shape = s;
		this.ammo = ammo;
	}

	@Override
	public void draw(Graphics2D g2) {
		AffineTransform old = g2.getTransform();
		g2.rotate(Math.toRadians(theta), xcenter, ycenter);
		g2.setColor(Color.black);
		g2.fill(shape);
		g2.setTransform(old);
		g2.drawOval((int)xcenter-2, (int)ycenter-2, 4, 4);
		g2.drawString(Integer.toString(ammo), (int)xcenter, (int)ycenter-10);
	}
	
}
