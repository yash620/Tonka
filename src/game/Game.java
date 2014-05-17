package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.util.ArrayList;

import util.Collidable;
import util.Drawable;
import util.Updatable;
import weapon.BasicTurret;
import weapon.Weapon;

public class Game implements Drawable {
	private static ArrayList<Collidable> collidables;
	private static ArrayList<Drawable> drawables;
	private static ArrayList<Updatable> updatables;
	
	/*
	 * Tanks are special because their update method needs params
	 */
	private static ArrayList<Tank> allTanks;
	private Tank playerTank;
	
	//Prevent Concurrent Modification Errors
	private static ArrayList<Object> removeQue;
	private static ArrayList<Object> addQue;
	
	public Game(){
		collidables = new ArrayList<Collidable>();
		drawables = new ArrayList<Drawable>();
		updatables = new ArrayList<Updatable>();
		allTanks = new ArrayList<Tank>();
		removeQue = new ArrayList<Object>();
		addQue = new ArrayList<Object>();

		ArrayList<Weapon> weapon = new ArrayList<Weapon>();
		Tank t = new Tank(100,100, weapon);
		weapon.add(new BasicTurret(t));
		addObject(t);
		addObject(new Block());
		playerTank = t;
		addObject(new Tank(150,150, weapon));
	}
	
	// Test method, draw whatever you want on the panel
	private Shape test;
	public void setTestDraw(Shape s){
		test = s;
	}
	
	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(Color.black);
//		g2.drawRect(500, 200, 5, 5);
		for (Drawable d : drawables){
			d.draw(g2);
		}
		if (test != null){
			g2.draw(test);
		}
	}
	
	public void update(int down, int right, Point clickpoint){
		for (Updatable u : updatables){
			u.update();
			if (u instanceof Explosion){
			}
		}
		playerTank.movement(down, right, clickpoint);
		
		//Adding and removing
		for (Object o : addQue){
			addObject(o);
		}
		addQue.clear();
		for (Object o : removeQue){
			removeObject(o);
		}
		removeQue.clear();
	}
	public void click(Point clickPoint){
		playerTank.shoot(clickPoint);
	}
	//Called by other collidables to see who is colliding with the frame
	public static ArrayList<Collidable> getCollisions(Collidable init){
		ArrayList<Collidable> collisions = new ArrayList<Collidable>();
		for (Collidable c : collidables){
			if (c.isColliding(init)){
				collisions.add(c);
			}
		}
		return collisions;
	}
	private void addObject(Object o){
		if (o instanceof Collidable){
			collidables.add((Collidable) o);
		}
		if (o instanceof Drawable){
			drawables.add((Drawable) o);
		}
		if (o instanceof Updatable){
			updatables.add((Updatable) o);
		}
		if (o instanceof Tank){
			allTanks.add((Tank) o);
		}
	}
	private void removeObject(Object o){
		if (o instanceof Collidable){
			collidables.remove((Collidable) o);
		}
		if (o instanceof Drawable){
			drawables.remove((Drawable) o);
		}
		if (o instanceof Updatable){
			updatables.remove((Updatable) o);
		}
		if (o instanceof Tank){
			allTanks.remove((Tank) o);
		}
	}
	public static void addQueue(Object o){
		addQue.add(o);
	}
	public static void removeQueue(Object o){
		removeQue.add(o);
	}
}
