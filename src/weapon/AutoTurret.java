package weapon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import util.Timer;
import util.Timer.Action;

import game.Tank;
import game.Transform;

public class AutoTurret extends Weapon {
	private boolean fire;
	private final int diameter = 500;
	private boolean mouseHeld;
	private int time = -1;

	public AutoTurret(Tank t, double atot, double dtot) {
		this(t, t.getCenter(), atot, dtot);
	}
	
	public AutoTurret(Tank t, Point2D center, double atot, double dtot) {
//		super(t, 3, center, 5, 17, 2, atot, dtot);
		super(t, 3, center, 20, 3, 0, atot, dtot);
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

	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2);
		g2.drawOval((int)this.getCenter().getX()-diameter/2, (int)this.getCenter().getY()-diameter/2,
				diameter, diameter);
		if (mouseHeld) {
			g2.setColor(Color.green);
		} else {
			g2.setColor(Color.red);
		}
		g2.fillOval((int)this.getCenter().getX()-4, (int)this.getCenter().getY()-4, 8, 8);
		g2.setColor(Color.black);
	}

	@Override
	public void clickPoint(Point2D tgt) {
		double minDist = Integer.MAX_VALUE;
		Tank minTank = null;
		for (Tank t : this.getTank().getGame().getTanks()) {
			if (t.getTeam() != this.getTank().getTeam()) {
				double dist = t.getCenter().distance(this.getTank().getCenter());
				if (dist < minDist) {
					minTank = t;
					minDist = dist;
				}
			}
		}
		if (minDist < diameter/2) {
			fire = true;
		} else {
			fire = false;
		}
		if (minTank != null) {
			super.clickPoint(minTank.getCenter());
		}
	}

	@Override
	public ArrayList<Projectile> shoot() {
		if (time == -1) {
			mouseHeld = !mouseHeld;
			time = 0;
		}
		if (time > 10) {
			time = -1;
		}
		return null;
	}
	
	private ArrayList<Projectile> missile() {
		this.setCanFire(false);
		this.setAmmo(getAmmo()-1);
		this.addTimer(new Timer((int) this.getFirerate(), Action.FIRE));
		if (getAmmo() == this.getMAXAMMO()-1){
			this.addTimer(new Timer(350, Action.AMMO));
		}
		Random die = new Random();
		ArrayList<Projectile> missiles = new ArrayList<Projectile>(1);
		missiles.add(new BasicMissile(this.getCenter(),
				(die.nextInt(2)*2-1)*die.nextDouble()*this.getSpread() + getAngle(),
				5, this, this.getTank().getGame()));
		return missiles;
	}

	@Override
	public boolean canShoot() {
		return fire && super.canShoot() && mouseHeld;
	}

	@Override
	public void update() {
		if (time != -1){
			time++;
		}
		super.update();
		if (canShoot()) {
			this.getTank().getGame().addQueue(this.missile());
		}
	}

	@Override
	public void updateSpread() {
		
	}
	
}
