package game;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.util.ArrayList;

import weapon.BasicMissile;
import weapon.BasicTurret;
import weapon.Projectile;
import weapon.Weapon;

public class Game implements Drawable {
	private ArrayList<Block> allBlocks;
	private ArrayList<Projectile> allProjectiles;
	private ArrayList<Tank> allTanks;
	private ArrayList<Object> removeQue;		//Prevent Concurrent Modification Errors
	private ArrayList<Explosion> explosions;
	private ArrayList<Object> addQue;
	private Tank playerTank;
	
	public Game(){
		allBlocks = new ArrayList<Block>();
		allProjectiles = new ArrayList<Projectile>();
		allTanks = new ArrayList<Tank>();
		removeQue = new ArrayList<Object>();
		addQue = new ArrayList<Object>();
		explosions = new ArrayList<Explosion>();
		
		allBlocks.add(new Block());
		ArrayList<Weapon> weapon = new ArrayList<Weapon>();
		Tank t = new Tank(100,100, weapon, this);
		weapon.add(new BasicTurret(t));
		allTanks.add(t);
		playerTank = t;
		allTanks.add(new Tank(150,150, weapon, this));
	}
	
	private Shape test;
	// Test method, draw whatever you want on the panel
	public void setTestDraw(Shape s){
		test = s;
	}
	
	@Override
	public void draw(Graphics2D g2) {
//		g2.drawRect(500, 200, 5, 5);
		for (Block b : allBlocks){
			b.draw(g2);
		}
		for (Projectile p : allProjectiles){
			p.draw(g2);
		}
		for (Tank t : allTanks){
			t.draw(g2);
		}
		for (Explosion e: explosions){
			e.draw(g2);
		}
		if (test != null){
			g2.draw(test);
		}
	}
	
	public void update(int down, int right, Point clickpoint){
		for (Projectile p : allProjectiles){
			p.update();
		}
		playerTank.movement(down, right, clickpoint);
//		for (Tank t : allTanks){
//			t.movement(down, right, clickpoint);
//		}
		collisions();
		for (Tank t : allTanks){
			if(t.getHp() <= 0){
				removeQueue(t);
				addQueue(new Explosion(t.getCenter()));
			}
		}
		for(Explosion e: explosions){
			e.update();
			if(e.done()){
				removeQueue(e);
			}
		}
		addObjects();
		removeObjects();
	}
	
	public void click(Point clickPoint){
//		allProjectiles.add(new BasicMissile(x, y, vel, theta, this));
		playerTank.shoot(clickPoint);
	}
	
	public void addProjectile(Projectile p){
		allProjectiles.add(p);
	}
	
//	public void createProjectile(Projectileint x, int y, int angle, weapon){
//		allProjectiles.add(new BasicMissile(new Point(x,y), angle, this));
//	}
	
	private void collisions(){
		for (int i = allBlocks.size()-1;i >= 0;i--){
			Block b = allBlocks.get(i);
			if (b.isSplit()){
				ArrayList<Block> newBlocks = b.splitBlock();
				allBlocks.remove(b);
				allBlocks.addAll(newBlocks);
			}
			if (b.isEmpty()){
				allBlocks.remove(b);
			}
		}
	}
	public ArrayList<Collidable> getCollisions(Collidable init){
		ArrayList<Collidable> collisions = new ArrayList<Collidable>();
		for (Collidable c : allBlocks){
			if (c.isColliding(init)){
				collisions.add(c);
			}
		}
		for (Collidable c : allProjectiles){
			if (c.isColliding(init)){
				collisions.add(c);
			}
		}
		for (Collidable c : allTanks){
			if (c.isColliding(init)){
				collisions.add(c);
			}
		}
		return collisions;
	}
	private void removeObjects(){
		for (Object o : removeQue){
			if (o instanceof Block){
				allBlocks.remove(o);
			}
			if (o instanceof Projectile){
				allProjectiles.remove(o);
			}
			if (o instanceof Tank){
				allTanks.remove(o);
			}
			if (o instanceof Explosion){
				explosions.remove(o);
			}
		}
	}
	public void removeQueue(Object o){
		removeQue.add(o);
	}
	private void addObjects(){
		for (Object o : addQue){
			if (o instanceof Explosion){
				explosions.add((Explosion)o);
			}
		}
	}
	public void addQueue(Object o){
		addQue.add(o);
	}
}
