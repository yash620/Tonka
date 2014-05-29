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

	public Machinegun(Tank t) {
		super(t, 3, t.getCenter(), 25, 3, 20);
		int[] x = {0,	6,	6,	3,	2, -2,	-3,	-6,	-6};
		int[] y = {-3,	-3,	2,	2,	15,	15,	2,	2,	-3};
		setWeaponShape(new Polygon(x, y, 9));
		//Rotate it to its initial location
		setWeaponShape(Transform.transform(getWeaponShape(), t.getCenter().getX(),
				t.getCenter().getY(), Math.toRadians(-90), t.getCenter().getX(), t.getCenter().getY()));
	}

	@Override
	public void replenishAmmo() {
		this.setAmmo(15);
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
			if (getAmmo() == 14){
				this.addTimer(new Timer(150, Action.AMMO));
			}
			Random die = new Random();
			ArrayList<Projectile> missiles = new ArrayList<Projectile>(1);
			missiles.add(new BasicMissile(this.getCenter(),
					(die.nextInt(2)*2-1)*die.nextDouble()*this.getSpread() + getAngle(),this, this.getTank().getGame()));
			return missiles;
		}
		return null;
	}

}
