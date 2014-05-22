package weapon;

import game.AngleMath;
import game.Tank;
import game.Transform;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import util.Timer;
import util.Timer.Action;

public class BasicTurret extends Weapon {
	private AffineTransform old;
	private HashSet<Timer> allTimers;
	public BasicTurret(Tank t){
		super(t, 3, t.getCenter(), 5, 10, 2);
		this.setCanFire(true);
		int[] x = {0,	6,	6,	3,	2, -2,	-3,	-6,	-6};
		int[] y = {-3,	-3,	2,	2,	15,	15,	2,	2,	-3};
		setWeaponShape(new Polygon(x, y, 9));
		//Rotate it to its initial location
		setWeaponShape(Transform.transform(getWeaponShape(), t.getCenter().getX(), t.getCenter().getY(), Math.toRadians(-90), t.getCenter().getX(), t.getCenter().getY()));
		old = new AffineTransform();
		allTimers = new HashSet<Timer>();
	}

	@Override
	public void draw(Graphics2D g2) {
		old = g2.getTransform();
		g2.rotate(Math.toRadians(getAngle()), getCenter().getX(), getCenter().getY());
		g2.setColor(Color.black);
		g2.fill(getWeaponShape());
		g2.setTransform(old);
		for (int i = -1;i<=1;i++){
//			g2.drawLine((int)center.getX(), (int)center.getY(), (int)(1000 * Math.cos(Math.toRadians(angle + i*this.spread))+center.getX()), (int)(1000*Math.sin(Math.toRadians(angle + i*this.spread))+center.getY()));
		}
	}
	@Override
	public void clickPoint(Point tgt){
		double tgtAng = Math.atan2(tgt.y - getCenter().getY(), tgt.x - getCenter().getX());
		this.setTgtAngle(AngleMath.adjustAngle((int)Math.toDegrees(tgtAng)));
	}
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
		double deltax = getTank().getCenter().getX() - getCenter().getX();
		double deltay = getTank().getCenter().getY() - getCenter().getY();
		setCenter(getTank().getCenter());
		setWeaponShape(Transform.transform(getWeaponShape(), deltax, deltay, 0,
				getCenter().getX(), getCenter().getY()));
	}

//	private Point getProjectileSpawn() {
//		return new Point((int)(15 * Math.cos(Math.toRadians(angle+90)))+center.x, (int)(15*Math.sin(Math.toRadians(angle+90)))+center.y);
//	}

	@Override
	public Projectile shoot() {
		if (canShoot()){
			this.setCanFire(false);
			allTimers.add(new Timer((int) this.getFirerate(), Action.FIRE));
			Random die = new Random();
			return new BasicMissile(this.getCenter(),
					(die.nextInt(2)*2-1)*die.nextDouble()*this.getSpread() + getAngle(),this, this.getTank().getGame());
		}
		return null;
	}

	@Override
	public void replenishAmmo() {
		this.setAmmo(5);
	}

	@Override
	public void updateSpread() {
		
	}
}
