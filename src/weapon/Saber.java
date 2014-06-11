package weapon;

import game.Tank;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import util.Timer;
import util.Timer.Action;

public class Saber extends Weapon {
	Tank t;
	public Saber(Tank t, double dtot, double atot)
	{
		this(t, 0, t.getCenter(),Integer.MAX_VALUE,50, 0, dtot, atot);
		this.t = t;
		int[] x = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		int[] y = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		this.setWeaponShape(new Polygon(x, y, 10));
		
	}

	public Saber(Tank t, double turnSpeed, Point2D center, int ammo, double firerate, double spread,double dtot, double atot) {
		super(t, turnSpeed, center, ammo, firerate, spread);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateSpread() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Projectile> shoot() {
		// TODO Auto-generated method stub
		if(canShoot()){
			this.addTimer(new Timer((int) this.getFirerate(), Action.FIRE));
			ArrayList<Projectile> missiles = new ArrayList<Projectile>(1);
			missiles.add(new SwordProjectile(t.getCenter(),t.getTheta(),this,t.getGame()));
			this.addTimer(new Timer((int) this.getFirerate(), Action.FIRE));
			return missiles;
		}
		return null;
	}

}
