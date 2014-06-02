package weapon;

import game.Tank;
import game.Transform;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import util.Timer;
import util.Timer.Action;

public class Machinegun extends Weapon {

	public Machinegun(Tank t, double atot, double dtot){
		super(t, 2, t.getCenter(), 30, 1, 6, atot, dtot);
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
	
	public Machinegun(Point2D t, double atot, double dtot){
		super(null, 3, t, 20, 2, 7, atot, dtot);
		int[] x = {0,	6,	6,	3,	2, -2,	-3,	-6,	-6};
		int[] y = {-3,	-3,	2,	2,	15,	15,	2,	2,	-3};
		setWeaponShape(new Polygon(x, y, 9));
		this.setCenter(new Point2D.Double(t.getX(), t.getY()));
		//Rotate it to its initial location
		setWeaponShape(Transform.transform(getWeaponShape(), t.getX(), t.getY(), Math.toRadians(-90),
				t.getX(), t.getY()));

	}
	@Override
	public void updateSpread() {

	}

	@Override
	public ArrayList<Projectile> shoot() {
		if (canShoot()){
			this.setCanFire(false);
			this.setAmmo(getAmmo()-1);
			this.addTimer(new Timer((int) this.getFirerate(), Action.FIRE));
			if (getAmmo() == this.getMAXAMMO()-1){
				this.addTimer(new Timer(0, Action.AMMO));
			}
			Random die = new Random();
			ArrayList<Projectile> missiles = new ArrayList<Projectile>(1);
			missiles.add(new BasicMissile(this.getCenter(),
					(die.nextInt(2)*2-1)*die.nextDouble()*this.getSpread() + getAngle(), 2.5, this, this.getTank().getGame()));
			return missiles;
		}
		return null;
	}

}
