package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.HashSet;

import map.Map;
import util.Collidable;
import util.Drawable;
import util.KeyInput;
import util.Updatable;
import weapon.BasicTurret;
import weapon.Weapon;

public class Game implements Drawable {
	private HashSet<Collidable> collidables;
	private HashSet<Drawable> drawables;
	private HashSet<Updatable> updatables;
	private Map map;
	
	/*
	 * Tanks are special because their update method needs params
	 */
	private ArrayList<Tank> allTanks;
	private ArrayList<Tank> playerTanks;
	
	//Prevent Concurrent Modification Errors
	private HashSet<Object> removeQue;
	private HashSet<Object> addQue;
	
	public Game(int playerNum){
		collidables = new HashSet<Collidable>();
		drawables = new HashSet<Drawable>();
		updatables = new HashSet<Updatable>();
		allTanks = new ArrayList<Tank>();
		playerTanks = new ArrayList<Tank>();
		removeQue = new HashSet<Object>();
		addQue = new HashSet<Object>();
		map = new Map(this);
		map.basicMap();
		for(Block b: map.showBlocks()){
			addObject(b);
		}
		//addObject(map.showBlocks());

		for (int i = 0;i<playerNum;i++){

			Tank t = new Tank(100,100, this);
			t.addWeapon(new BasicTurret(t));
			addObject(t);
			addObject(new Block(this));
			Tank enemy = new Tank(150,150, this);
			enemy.addWeapon(new BasicTurret(enemy));
			enemy.addAI(new AI(enemy, this));
			addObject(enemy);
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
//			if(d instanceof Block)
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
		playerTanks.get(player).movement(i);
		this.tick();
	}
	//Called by other collidables to see who is colliding with the frame
	public ArrayList<Collidable> getCollisions(Collidable init){
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
			if (!((Tank)o).isAI()){
				playerTanks.add((Tank) o);
			}
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
			playerTanks.remove((Tank) o);
		}
	}
	public void addQueue(Object o){
		addQue.add(o);
	}
	public void removeQueue(Object o){
		removeQue.add(o);
	}
	
	public ArrayList<Tank> getPlayerTanks(){
		return new ArrayList<Tank>(playerTanks);
	}
//	public Packet getDrawables(){
//		return new Packet(drawables);
//	}
}
