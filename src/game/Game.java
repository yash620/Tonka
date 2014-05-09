package game;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Area;
import java.util.ArrayList;

import weapon.BasicMissile;
import weapon.BasicTurret;
import weapon.Projectile;
import weapon.Weapon;

public class Game implements Drawable {
	private ArrayList<Block> allBlocks;
	private ArrayList<Projectile> allProjectiles;
	private ArrayList<Tank> allTanks;
	private Tank playerTank;
	
	public Game(){
		allBlocks = new ArrayList<Block>();
		allProjectiles = new ArrayList<Projectile>();
		allTanks = new ArrayList<Tank>();
		
		allBlocks.add(new Block());
		ArrayList<Weapon> weapon = new ArrayList<Weapon>();
		Tank t = new Tank(100,100, weapon, this);
		weapon.add(new BasicTurret(t));
		allTanks.add(t);
		playerTank = t;
	}
	
	private Shape test;
	
	@Override
	public void draw(Graphics2D g2) {
		for (Block b : allBlocks){
			b.draw(g2);
		}
		for (Projectile p : allProjectiles){
			p.draw(g2);
		}
		for (Tank t : allTanks){
			t.draw(g2);
		}
		if (test != null){
			g2.draw(test);
		}
	}
	
	public void update(int down, int right, Point clickpoint){
		for (Projectile p : allProjectiles){
			p.update();
		}
		for (Tank t : allTanks){
			t.movement(down, right, clickpoint);
		}
		collisions();
	}
	
	public void click(Point clickPoint){
//		allProjectiles.add(new BasicMissile(x, y, vel, theta, this));
		playerTank.shoot(clickPoint);
	}
	
	public void addProjectile(Projectile p){
		allProjectiles.add(p);
	}
	
	public void createProjectile(int x, int y, int angle){
		allProjectiles.add(new BasicMissile(new Point(x,y), angle, this));
	}
	
	private void collisions(){
		ArrayList<Projectile> removeThese = new ArrayList<Projectile>();
		for (Block b : allBlocks){
			for (Projectile p : allProjectiles){
				if (b.getShape().getBounds().intersects(p.getShape().getBounds())){
					Area projectile = new Area(p.getShape());
					Area block = new Area(b.getShape());
					block.intersect(projectile);
					if (!block.isEmpty()){
						Shape dest = p.getDestroyed();
						test = dest;
						b.destroy(dest);
						removeThese.add(p);
					}
				}
			}
		}
		for (Projectile p : removeThese){
			p.collided();
		}
	}
	public boolean removeObject(Object o){
		if (o instanceof Block){
			return allBlocks.remove(o);
		}
		if (o instanceof Projectile){
			return allProjectiles.remove(o);
		}
		return false;
	}
}
