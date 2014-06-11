package weapon;

import game.Block;
import game.Explosion;
import game.Game;
import game.Tank;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;

import util.Collidable;

public class Homing extends Projectile {

	public Homing(Shape s, int xstart, int ystart, double velocity,
			double damage, double theta, Weapon weapon, Game game) {
		super(s, xstart, ystart, velocity, damage, theta, weapon, game);
		int[] x = new int[3];
		int[] y = new int[3];
		x[0] = xstart+15;
		y[0] = ystart;
		x[1] = xstart-5;
		y[1] = ystart+3;
		x[2] = xstart-5;
		y[2] = ystart-3;
		this.setProjectileShape(new Polygon(x,y,3));
	}

	@Override
	public void update() {
		int minDist = Integer.MAX_VALUE;
		Tank minTank;
		for (Tank t : game.getTanks()){
			double dist = this.getWeapon().getCenter().distanceSq(t.getCenter());
			if (dist < minDist){
				minDist = (int) dist;
				minTank = t;
			}
		}
	}

	@Override
	public Shape getDestroyed() {
		int sides = 20;
		int[] xarr = new int[sides];
		int[] yarr = new int[sides];
		for (int i = 0;i<sides;i++){
			xarr[i] = (int)(((Math.random()*6)+4)*Math.cos(Math.toRadians(i*360/sides)) + this.center.getX());
			yarr[i] = (int)(((Math.random()*6)+4)*Math.sin(Math.toRadians(i*360/sides)) + this.center.getY());
		}
		Polygon destroyed = new Polygon(xarr, yarr, sides);
		return destroyed;
	}

	@Override
	public void collision(Collidable c) {
		if (c instanceof Block){
			game.removeQueue(this);
			game.addQueue(new Explosion(this.center, game));
		}
		if (c instanceof Tank && !c.equals(this.getWeapon().getTank())){
			((Tank) c).takeDamage((int) this.damage);
			game.removeQueue(this);
			game.addQueue(new Explosion(this.center, game));
		}
	}
}
