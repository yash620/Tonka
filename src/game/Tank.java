package game;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;

import weapon.Weapon;

public class Tank implements Drawable {
	private int hp;
	private double speed;
	private int turnSpeed;
	private int theta;
	private ArrayList<Weapon> myWeapons;
	private Shape tankShape;
	private double xcenter;
	private double ycenter;
	private Game game;
	
	public Tank(double x, double y, ArrayList<Weapon> weapons, Game game){
		this.xcenter = x;
		this.ycenter = y;
		this.myWeapons = weapons;
		this.hp = 100;
		this.speed = 3;
		this.turnSpeed = 5;
		tankShape = new Rectangle((int)x-20, (int)y-10, 35,20);
		this.game = game;
	}
	
	@Override
	public void draw(Graphics2D g2) {
		g2.draw(tankShape);
		for (Weapon w : myWeapons){
			w.draw(g2);
		}
	}
	
	public void shoot(Point shootPoint){
		for (Weapon w : myWeapons){
			if (w.canShoot()){
				game.addProjectile(w.shoot());
			}
		}
	}
	
	public void movement(int down, int right, Point clickpoint){
		int tgtTheta = this.determineTheta(right);
		double tempSpeed = speed;
		if (down != 0 && right != 0){
			tempSpeed = tempSpeed/Math.sqrt(2);
		}
		double xvel = tempSpeed * Math.cos(Math.toRadians(tgtTheta)) * down;
		double yvel = tempSpeed * Math.sin(Math.toRadians(tgtTheta)) * down;
		xcenter += xvel;
		ycenter += yvel;
		
		tankShape = Transform.transform(tankShape, xvel, yvel,
				Math.toRadians(tgtTheta-getTheta()), xcenter, ycenter);
		setTheta(tgtTheta);
		for (Weapon w : myWeapons){
			w.clickPoint(clickpoint);
			w.update();
		}
	}
	private int determineTheta(int right){
		return getTheta() + right * turnSpeed;
	}

	public Point getCenter(){
		return new Point((int)xcenter, (int)ycenter);
	}

	public int getTheta() {
		return theta;
	}

	public void setTheta(int theta) {	
		this.theta = (int)AngleMath.adjustAngle(theta);
	}
	
	public Game getGame(){
		return game;
	}
}
