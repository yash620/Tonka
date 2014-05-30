package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;

import util.Collidable;
import util.Drawable;
import util.KeyInput;
import util.Updatable;
import weapon.Projectile;
import weapon.Weapon;

public class Tank implements Drawable, Collidable, Serializable, Updatable {
	private double hp;
	private double speed;
	private double turnSpeed;
	private double theta;
	private double prevtheta;
	private ArrayList<Weapon> myWeapons;
	private Shape tankShape;
	private double xcenter;
	private double ycenter;
	private Color color;
	private AI ai;
	private Game game;
	
	public Tank(double x, double y, Game game){
		this(x,y,new ArrayList<Weapon>(), game);
	}
	
	public Tank(double x, double y, ArrayList<Weapon> weapons, Game game){
		this.theta = 0;
		this.prevtheta = 0;
		this.xcenter = x;
		this.ycenter = y;
		this.myWeapons = weapons;
		this.hp = 100;
		this.speed = 3;
		this.turnSpeed = 5;
		setColor(Color.green);
		tankShape = new Rectangle((int)x-20, (int)y-10, 35,20);
		this.game = game;
	}
	
	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(color);
		g2.fill(tankShape);
		g2.setColor(Color.black);
		g2.draw(tankShape);
		g2.setColor(Color.red);
		g2.fillRect((int)xcenter - 25, (int)ycenter - 30, (int)(((double)this.hp)/100 * 50), 5);
		g2.setColor(Color.black);
		g2.drawRect((int)xcenter - 25, (int)ycenter - 30, 50, 5);
		//g2.drawString("HP" + this.hp, (int)xcenter, (int)ycenter);
		g2.setColor(color);
		for (Weapon w : myWeapons){
			w.draw(g2);
		}
		g2.drawString(Double.toString(this.getTheta()), (int)this.getCenter().getX() + 20, (int)this.getCenter().getY());
	}
	
	public void addWeapon(Weapon w){
		myWeapons.add(w);
	}
	
	public void shoot(Point shootPoint){
		for (Weapon w : myWeapons){
			if (w.canShoot()){
				game.addQueue(w.shoot());
			}
		}
	}
	
	public void movement(KeyInput i){
		this.movement(i.getDown(), i.getRight(), i.getClickPoint(), i.isShoot());
	}
	
	public void movement(int down, int right, Point clickpoint, boolean shoot){
		prevtheta = getTheta();
		double originalTheta = getTheta();
		double tgtTheta = this.determineTheta(right);
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
		ArrayList<Collidable> collisions = game.getCollisions(this);
		boolean blocked = false;
		for (Collidable c : collisions){
			c.collision(this);
			if (c instanceof Block || c instanceof Tank){
				blocked = true;
			}
		}
		if (blocked){
			xcenter -= xvel;
			ycenter -= yvel;
			tankShape = Transform.transform(tankShape, -xvel, -yvel, Math.toRadians(originalTheta - getTheta()), xcenter, ycenter);
			setTheta(originalTheta);
		}
		
		for (Weapon w : myWeapons){
			w.clickPoint(clickpoint);
			w.update();
		}
		
		if (shoot){
			this.shoot(clickpoint);
		}
	}
	private double determineTheta(int right){
		return getTheta() + right * turnSpeed;
	}

	public double getTheta() {
		return theta;
	}
	
	public double getPrevTheta(){
		return prevtheta;
	}

	public void setTheta(double theta) {
		this.theta = (int)AngleMath.adjustAngle(theta);
	}

	@Override
	public void collision(Collidable c) {
		if (c instanceof Projectile){
			c.collision(this);
		}
	}

	@Override
	public Shape getShape() {
		return this.tankShape;
	}

	@Override
	public Rectangle getBoundingBox() {
		return this.tankShape.getBounds();
	}
	
	public void addAI(AI ai){
		this.ai = ai;
	}
	
	public boolean isAI(){
		return ai != null;
	}

	public Point2D.Double getCenter(){
		return new Point2D.Double(xcenter, ycenter);
	}

	@Override
	public boolean isColliding(Collidable c) {
		if (c.equals(this)){
			return false;
		}
		if (c.getBoundingBox().intersects((Rectangle2D) this.getBoundingBox())){
			Area tankArea = new Area(this.getShape());
			tankArea.intersect(new Area(c.getShape()));
			if (!tankArea.isEmpty()){
				return true;
			}
		}
		return false;
	}
	
	public double getHp(){
		return hp;
	}
	
	public void takeDamage(int dmg){
		this.hp -= dmg;
	}
	
	public Color getColor(){
		return color;
	}
	
	public void setColor(Color c){
		color = c;
	}
	
	public Game getGame(){
		return game;
	}

	
	public void removeWeapon(Weapon w){
		this.myWeapons.remove(w);
	}
	
	@Override
	public void update() {
		if (this.isAI()){
			movement(ai.getInputs());
		}
		if(getHp() <= 0){
			game.removeQueue(this);
			game.addQueue(new Explosion(getCenter(), game));
		}
	}
	@Override
	public String toString(){
		return this.getCenter().toString();
	}
}

