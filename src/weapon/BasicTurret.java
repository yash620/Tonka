package weapon;

import game.AngleMath;
import game.Tank;
import game.Transform;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.util.Random;

public class BasicTurret extends Weapon {
	private AffineTransform old;
			
	public BasicTurret(Tank t){
		this.setTank(t);
		this.setAmmo(5);
		this.setCenter(t.getCenter());
		this.setTurnSpeed(3);
		this.setSpread(2);
		int[] x = {0,	6,	6,	3,	2, -2,	-3,	-6,	-6};
		int[] y = {-3,	-3,	2,	2,	15,	15,	2,	2,	-3};
		setWeaponShape(new Polygon(x, y, 9));
		//Rotate it to its initial location
		setWeaponShape(Transform.transform(getWeaponShape(), t.getCenter().getX(), t.getCenter().getY(), Math.toRadians(-90), t.getCenter().getX(), t.getCenter().getY()));
		old = new AffineTransform();
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
		replenishAmmo();
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
	
	private void replenishAmmo(){
		
	}

//	private Point getProjectileSpawn() {
//		return new Point((int)(15 * Math.cos(Math.toRadians(angle+90)))+center.x, (int)(15*Math.sin(Math.toRadians(angle+90)))+center.y);
//	}

	@Override
	public Projectile shoot() {
		if (canShoot()){
			Random die = new Random();
			return new BasicMissile(this.getCenter(),
					(die.nextInt(2)*2-1)*die.nextDouble()*this.getSpread() + getAngle(),this, this.getTank().getGame());
		}
		return null;
	}

	@Override
	public boolean canShoot() {
		return getAmmo() > 0;
	}
}
