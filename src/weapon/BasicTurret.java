package weapon;

import game.AngleMath;
import game.Tank;
import game.Transform;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import util.Timer;
import util.Timer.Action;

public class BasicTurret extends Weapon {
	public BasicTurret(Tank t){
		super(t, 3, t.getCenter(), 5, 10, 2);
		int[] x = {0,	6,	6,	3,	2, -2,	-3,	-6,	-6};
		int[] y = {-3,	-3,	2,	2,	15,	15,	2,	2,	-3};
		setWeaponShape(new Polygon(x, y, 9));
		//Rotate it to its initial location
		setWeaponShape(Transform.transform(getWeaponShape(), t.getCenter().getX(), t.getCenter().getY(), Math.toRadians(-90), t.getCenter().getX(), t.getCenter().getY()));
	}
	
	public BasicTurret(Point2D t, double atot, double dtot){
		super(null, 3, t, 5, 10, 2, atot, dtot);
		int[] x = {0,	6,	6,	3,	2, -2,	-3,	-6,	-6};
		int[] y = {-3,	-3,	2,	2,	15,	15,	2,	2,	-3};
		setWeaponShape(new Polygon(x, y, 9));
		this.setCenter(t);
		setWeaponShape(Transform.transform(getWeaponShape(), t.getX(), t.getY(), Math.toRadians(-90),
				t.getX(), t.getY()));
	}
	
	public BasicTurret(Tank t, double atot, double dtot){
		super(t, 3, t.getCenter(), 5, 10, 2, atot, dtot);
		int[] x = {0,	6,	6,	3,	2, -2,	-3,	-6,	-6};
		int[] y = {-3,	-3,	2,	2,	15,	15,	2,	2,	-3};
		setWeaponShape(new Polygon(x, y, 9));
		double xcenter = t.getCenter().getX() + dtot * Math.cos(Math.toRadians(t.getTheta() + atot));
		double ycenter = t.getCenter().getY() + dtot * Math.sin(Math.toRadians(t.getTheta() + atot));
		this.setCenter(new Point2D.Double(xcenter, ycenter));
		//Rotate it to its initial location
		setWeaponShape(Transform.transform(getWeaponShape(), xcenter, ycenter, Math.toRadians(-90),
				xcenter, ycenter));
	}
	
	public BasicTurret(double x, double y){
		super(null, 3, new Point2D.Double(x,y), 5, 10, 2);
		this.setCanFire(true);
		int[] x2 = {0,	6,	6,	3,	2, -2,	-3,	-6,	-6};
		int[] y2 = {-3,	-3,	2,	2,	15,	15,	2,	2,	-3};
		setWeaponShape(new Polygon(x2, y2, 9));
		//Rotate it to its initial location
		setWeaponShape(Transform.transform(getWeaponShape(), x, y, Math.toRadians(-90), x, y));
	}
	
	@Override
	public void update(){
		super.update();
	}
	
//	public void moveTo(Point2D next){
//		double deltax = next.getX() - getCenter().getX();
//		double deltay = next.getY() - getCenter().getY();
//		setCenter(new Point2D.Double(getCenter().getX() + deltax, getCenter().getY() + deltay));
//		setWeaponShape(Transform.transform(getWeaponShape(), deltax, deltay, 0,
//				getCenter().getX(), getCenter().getY()));
//		
//	}

//	private Point getProjectileSpawn() {
//		return new Point((int)(15 * Math.cos(Math.toRadians(angle+90)))+center.x, (int)(15*Math.sin(Math.toRadians(angle+90)))+center.y);
//	}

	@Override
	public ArrayList<Projectile> shoot() {
		if (canShoot()){
			this.setCanFire(false);
			this.setAmmo(getAmmo()-1);
			this.addTimer(new Timer((int) this.getFirerate(), Action.FIRE));
			if (getAmmo() == 4){
				this.addTimer(new Timer(150, Action.AMMO));
			}
			Random die = new Random();
			ArrayList<Projectile> missiles = new ArrayList<Projectile>(1);
			missiles.add(new BasicMissile(this.getCenter(),
					(die.nextInt(2)*2-1)*die.nextDouble()*this.getSpread() + getAngle(),10, this, this.getTank().getGame()));
			return missiles;
		}
		return null;
	}

	@Override
	public void updateSpread() {
		
	}
}
