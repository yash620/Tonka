package weapon;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import util.Timer;
import util.Timer.Action;

import game.AngleMath;
import game.Tank;
import game.Transform;

public class GrenadeLauncher extends Weapon {
	private double distance;
	
	public GrenadeLauncher(Tank t, double turnSpeed, Point2D center, int ammo,
			double firerate, double spread, double dtot, double atot) {
		super(t, turnSpeed, center, ammo, firerate, spread);
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

	public GrenadeLauncher(Tank t, double dtot, double atot) {
		this(t,3,t.getCenter(), 4, 40, 0, dtot, atot);
	}

	@Override
	public void clickPoint(Point tgt){
		double tgtAng = Math.atan2(tgt.y - getCenter().getY(), tgt.x - getCenter().getX());
		this.setTgtAngle(AngleMath.adjustAngle((int)Math.toDegrees(tgtAng)));
		this.distance = this.getCenter().distance(tgt);
	}
	
	@Override
	public ArrayList<Projectile> shoot(){
		if (canShoot()){
			this.setCanFire(false);
			this.setAmmo(getAmmo()-1);
			this.addTimer(new Timer((int) this.getFirerate(), Action.FIRE));
			if (getAmmo() == this.getMAXAMMO()-1){
				this.addTimer(new Timer(150, Action.AMMO));
			}
			ArrayList<Projectile> missiles = new ArrayList<Projectile>(1);
			missiles.add(new Grenade(this.getCenter(), distance, 30, this.getAngle(),
					this, this.getTank().getGame()));
			this.addTimer(new Timer((int) this.getFirerate(), Action.FIRE));
			return missiles;
		}
		return null;
	}

	@Override
	public void updateSpread() {

	}

}
