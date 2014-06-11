package weapon;

import game.Block;
import game.Explosion;
import game.Game;
import game.Tank;
import game.Transform;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Point2D;

import util.Collidable;

public class SwordProjectile extends Projectile{
	
	int xstartme;
	int ystartme;
	Point2D init;
	boolean remove;
	private double deltax;
	private double deltay;
	public SwordProjectile(Shape s, int xstart, int ystart, double velocity,
			double damage, double theta, Weapon weapon, Game game) {
		super(s, xstart, ystart, velocity, damage, theta, weapon, game);
		// TODO Auto-generated constructor stub
	}
	
	public SwordProjectile(Point2D center, double theta, Weapon weapon, Game game){
		super(null, (int)center.getX(), (int)center.getY(), 50, 10, theta, weapon, game);
		xstartme = (int)center.getX();
		ystartme =	(int)center.getY();
		init = center; 
		remove = false; 
		int sides = 10;
		int[] xarr = new int[sides];
		int[] yarr = new int[sides];
		for(int i = 0; i < sides; i++){
			xarr[i] = (int)(this.center.getX() + 1 * Math.cos(Math.PI/(sides/2) * i));
			yarr[i] = (int)(this.center.getY() + 1 * Math.sin(Math.PI/(sides/2) * i));
		}
		this.setProjectileShape(new Polygon(xarr,yarr,sides));

		deltax = 20*Math.cos(Math.toRadians(theta));
		deltay = 20*Math.sin(Math.toRadians(theta));
	}

	@Override
	public void update() {
		if(remove){
			game.removeQueue(this);
		}
		setProjectileShape(Transform.transform(getProjectileShape(), deltax, deltay, 0, this.center.getX(), this.center.getY()));
//		projectileShape = translate.createTransformedShape(projectileShape);
		double xcenter = center.getX() + deltax;
		double ycenter = center.getY() + deltay;
		center.setLocation(xcenter, ycenter);
		for (Collidable c : game.getCollisions(this)){
			c.collision(this);
		}
		if(Math.abs(xstartme - xcenter) > 20*Math.cos(Math.toRadians(theta)) && Math.abs(ystartme - ycenter) > 20*Math.sin(Math.toRadians(theta)))
		{
			remove = true;
		}
		// TODO Auto-generated method stub
		
	}

	@Override
	public Shape getDestroyed() {
		// TODO Auto-generated method stub
		//this.setProjectileShape(this.getDestroyed());
		return null;
	}

	@Override
	public void collision(Collidable c) {
		// TODO Auto-generated method stub
		if (c instanceof Tank && !c.equals(this.getWeapon().getTank())) {
			if (this.center.distance(init) >= 30){
				((Tank) c).takeDamage((int) this.damage);
				game.addQueue(new Explosion(this.center.getX(), this.center.getY(), 60, game));
			}
			game.removeQueue(this);
		}
		if (c instanceof Block){
			game.removeQueue(this);
			game.addQueue(new Explosion(this.center.getX(), this.center.getY(), 60, game));
		}
	}
		
	

}
