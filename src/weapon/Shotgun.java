package weapon;

import game.Tank;
import game.Transform;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import util.Timer;
import util.Timer.Action;

public class Shotgun extends Weapon {

	public Shotgun(Tank t, Point2D center, double atot, double dtot) {
		super(t, 5, t.getCenter(), 2, 10, 0, atot, dtot);
		int[] x = {0,   6, 6, 3, 3,   1, 1, -1, -1, -3, -3, -6, -6};
		int[] y = {-8, -5, 2, 4, 15, 15, 4,  4, 15, 15,  4,  2, -5};
		setWeaponShape(new Polygon(x, y, 13));
		double xcenter = t.getCenter().getX() + dtot * Math.cos(Math.toRadians(t.getTheta() + atot));
		double ycenter = t.getCenter().getY() + dtot * Math.sin(Math.toRadians(t.getTheta() + atot));
		this.setCenter(new Point2D.Double(xcenter, ycenter));
		//Rotate it to its initial location
		setWeaponShape(Transform.transform(getWeaponShape(), xcenter, ycenter, Math.toRadians(-90),
				xcenter, ycenter));
	}
	
	public Shotgun(Tank t, double atot, double dtot) {
		this(t, t.getCenter(), atot, dtot);
	}
	
	public Shotgun(Point2D t, double atot, double dtot){
		this(null, t, atot, atot);
	}
	
	@Override
	public ArrayList<Projectile> shoot() {
		if (canShoot()){
			this.setCanFire(false);
			this.setAmmo(getAmmo()-1);
			this.addTimer(new Timer((int) this.getFirerate(), Action.FIRE));
			if (getAmmo() == 1){
				this.addTimer(new Timer(150, Action.AMMO));
			}
			Random die = new Random();
			ArrayList<Projectile> missiles = new ArrayList<Projectile>();
			for (int i = -3;i<=3;i++){
				missiles.add(new BasicMissile(this.getCenter(),
						(die.nextInt(2)*2-1)*die.nextDouble()*this.getSpread() + getAngle()+(i*3),3, this, this.getTank().getGame()));
			}
			return missiles;
//			
		}
		return null;
	}
	@Override
	public void updateSpread() {
		// TODO Auto-generated method stub
		
	}
}
