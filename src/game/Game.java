package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.io.Serializable;
import java.util.ArrayList;

import map.Map;

import util.Collidable;
import util.Drawable;
import util.KeyInput;
import util.Updatable;
import weapon.BasicTurret;
import weapon.Weapon;

public class Game implements Drawable, Serializable {
	private static ArrayList<Collidable> collidables;
	private static ArrayList<Drawable> drawables;
	private static ArrayList<Updatable> updatables;
	private Map map;
	
	/*
	 * Tanks are special because their update method needs params
	 */
	private static ArrayList<Tank> allTanks;
	private ArrayList<Tank> playerTanks;
	
	//Prevent Concurrent Modification Errors
	private static ArrayList<Object> removeQue;
	private static ArrayList<Object> addQue;
	
	public Game(int playerNum){
		collidables = new ArrayList<Collidable>();
		drawables = new ArrayList<Drawable>();
		updatables = new ArrayList<Updatable>();
		allTanks = new ArrayList<Tank>();
		playerTanks = new ArrayList<Tank>();
		removeQue = new ArrayList<Object>();
		addQue = new ArrayList<Object>();
		map = new Map();
		map.basicMap();
		for(Block b: map.showBlocks()){
			addObject(b);
		}
		//addObject(map.showBlocks());

		for (int i = 0;i<playerNum;i++){
			ArrayList<Weapon> weapon = new ArrayList<Weapon>();
			Tank t = new Tank(100,100, weapon);
			weapon.add(new BasicTurret(t));
			playerTanks.add(t);
			addObject(t);
			addObject(new Block());
			addObject(new Tank(150,150, weapon));
		}
	}
	
	// Test method, draw whatever you want on the panel
	private Shape test;
	public void setTestDraw(Shape s){
		test = s;
	}
	
	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(Color.black);
		for (Drawable d : drawables){
			d.draw(g2);
		}
		if (test != null){
			g2.draw(test);
		}
	}
	
	private void tick(){
		for (Updatable u : updatables){
			u.update();
		}
		
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
	//Single player update
	public void update(int down, int right, Point clickpoint, boolean shoot){
		playerTanks.get(0).movement(down, right, clickpoint, shoot);
		this.tick();
	}
	//MultiPlayer update
	public void update(KeyInput i, int player){
		playerTanks.get(player).movement(i.getDown(), i.getRight(), i.getClickPoint(), i.isShoot());
		this.tick();
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
	public ArrayList<Drawable> getDrawables(){
		return drawables;
	}
}
