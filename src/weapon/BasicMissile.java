package weapon;

import game.Game;
import game.Transform;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;

public class BasicMissile extends Projectile{
	private int time;
	private int timerDelay;
	private final double deltax, deltay;
	
	public BasicMissile(Point startpoint, double theta, Game game){
		this(startpoint.x, startpoint.y, 9, theta, game);
	}

	public BasicMissile(int xstart, int ystart, double velocity, double theta, Game game){
		super(new Polygon(), xstart, ystart, velocity, theta, game);
		int[] x = new int[3];
		int[] y = new int[3];
		x[0] = xstart+15;
		y[0] = ystart;
		x[1] = xstart-5;
		y[1] = ystart+3;
		x[2] = xstart-5;
		y[2] = ystart-3;
		this.projectileShape = new Polygon(x, y, 3);
		this.game = game;
		deltax = Math.cos(Math.toRadians(theta))*velocity;
		deltay = Math.sin(Math.toRadians(theta))*velocity;
		projectileShape = Transform.transform(projectileShape, 0, 0, Math.toRadians(theta), xstart, ystart);
	}
	public void update(){
		time++;
		this.projectileShape = Transform.transform(projectileShape, deltax, deltay, 0, this.center.getX(), this.center.getY());
//		projectileShape = translate.createTransformedShape(projectileShape);
		double xcenter = center.getX() + deltax;
		double ycenter = center.getY() + deltay;
		center.setLocation(xcenter, ycenter);
	}
	@Override
	public void draw(Graphics2D g2) {
		g2.draw(projectileShape);
//		g2.drawOval((int)this.center.getX()-5, (int)this.center.getY()-5, 10, 10);
	}
	public Shape getShape(){
		return projectileShape;
	}
	public Shape getDestroyed(){
		int sides = 20;
		int[] xarr = new int[sides];
		int[] yarr = new int[sides];
		for (int i = 0;i<sides;i++){
			xarr[i] = (int)(((Math.random()*4)+4)*Math.cos(Math.toRadians(i*360/sides)) + this.center.getX() + deltax/20);
			yarr[i] = (int)(((Math.random()*4)+4)*Math.sin(Math.toRadians(i*360/sides)) + this.center.getY() + deltay/20);
		}
		Polygon destroyed = new Polygon(xarr, yarr, sides);
		return destroyed;
	}
	public void collided(){
		if (timerDelay == 0){
			timerDelay = time;
		}
		if (timerDelay + 1 <= time){
			game.removeObject(this);
		}
	}
}
