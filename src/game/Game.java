package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import util.Collidable;
import util.Drawable;
import util.KeyInput;
import util.Sendable;
import util.Updatable;
import weapon.BasicMissile;
import weapon.BasicTurret;
import weapon.GrenadeLauncher;
import weapon.Machinegun;
import weapon.Shotgun;
import weapon.Weapon;

public class Game implements Drawable {
	private HashSet<Collidable> collidables;
	private HashSet<Drawable> drawables;
	private HashSet<Updatable> updatables;
	private Map map;
	private CollisionMap collisions;
//	private ThreadHandler thHand;
	
	/*
	 * Tanks are special because their update method needs params
	 */
	private ArrayList<Tank> allTanks;
	private HashMap<Integer, Tank> playerTanks;
	
	//Prevent Concurrent Modification Errors
	private HashSet<Object> removeQue;
	private HashSet<Object> addQue;
	
	public Game(int playerNum){
		collidables = new HashSet<Collidable>();
		drawables = new HashSet<Drawable>();
		updatables = new HashSet<Updatable>();
		allTanks = new ArrayList<Tank>();
		playerTanks = new HashMap<Integer, Tank>();
		removeQue = new HashSet<Object>();
		addQue = new HashSet<Object>();
		collisions = new CollisionMap();
//		thHand = new ThreadHandler();
		map = new Map(this);
		map.basicMap();
		for(Block b: map.showBlocks()){
			addObject(b);
		}
		
		for (int i = 0;i<playerNum;i++){
			System.out.println(i);
			Tank t = new Tank(100,100 + 50*i, i + 1, this);
			double r = Math.random();
			if(r<.3) {
				t.addWeapon(new Machinegun(t, 0, 10));
			} else if(r<.6) {
				t.addWeapon(new GrenadeLauncher(t, 0, 10));
			} else if(r<.9) {
				t.addWeapon(new Shotgun(t, 0, 10));
			} else if(r<1) {
				t.addWeapon(new BasicTurret(t, 0, 10));
			}
			addObject(t);
			for (int j = 0;j<7;j++){
				Tank enemy = new Tank(900 + 100*i, 100*j + 100, 0, this);
				r = Math.random();
				if(r<.2) {
					enemy.addWeapon(new Machinegun(enemy, 0, 10));
				} else if(r<.3) {
					enemy.addWeapon(new GrenadeLauncher(enemy, 0, 10));
				} else if(r<.65) {
					enemy.addWeapon(new Shotgun(enemy, 0, 10));
				} else if(r<1) {
					enemy.addWeapon(new BasicTurret(enemy, 0, 10));
				}
				enemy.addAI(new AI(enemy, this));
				boolean colliding = false;
				for (Collidable c : collidables){
					if (enemy.isColliding(c) || enemy.isColliding(c)){
						colliding = true;
						break;
					}
				}
				if (colliding == false){
					this.addQueue(enemy);
				}
			}
		}
		
//		//Stress testing
//		for (int i = 9;i<13;i++){
//			for (int j = 6;j<13;j++){
//				Tank enemy = new Tank(j*100, 50*i, this);
//				enemy.addWeapon(new BasicTurret(enemy, 0, 10));
//				enemy.addAI(new AI(enemy, this));
//				boolean colliding = false;
//				for (Collidable c : collidables){
//					if (enemy.isColliding(c) || enemy.isColliding(c)){
//						colliding = true;
//						break;
//					}
//				}
//				if (colliding == false){
//					this.addQueue(enemy);
//				}
//			}
//		}
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
//			if(d instanceof Block){
//				Block b = (Block)d;
//				g2.setColor(Color.red);
//				if (allTanks.size() == 1){
//					g2.drawString(Double.toString(AI.angleToRect(b.getBoundingBox(),
//							allTanks.get(0).getCenter())), (int)b.getCenter().getX(),(int)b.getCenter().getY());
//				}
//			}
		}
		if (test != null){
			g2.draw(test);
		}
	}
	public void tick(){
//		long start = System.currentTimeMillis();

		for (Updatable u : updatables) {
			u.update();
		}
		//Updates everything with the thread Handler. This part is multithreaded
//		thHand.update(updatables);
//		long end = System.currentTimeMillis();
		//Updates all AI tanks
		for (Tank t : allTanks){
			if (t.isAI()){
//				System.out.println(t.getCenter());
				t.movement(null);
			}
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
		collisions.updateCollidables(collidables);
	}
	public boolean isFinished(){
		return playerTanks.size() == 0;
	}
	//Single player update
	public void update(int down, int right, Point clickpoint, boolean shoot){
//		System.out.println(playerTanks);
		if (isFinished() == false){
			playerTanks.get(1).movement(down, right, clickpoint, shoot);
		}
		this.tick();
	}
	//MultiPlayer update
	public void update(KeyInput i, int player){
		if (player < playerTanks.size()) {
			playerTanks.get(player+1).movement(i);
		}
	}
	//Called by other collidables to see who is colliding with the frame
	public HashSet<Collidable> getCollisions(Collidable init){
		return collisions.getCollisions(init);
//		HashSet<Collidable> collisions = new HashSet<Collidable>();
//		for (Collidable c : collidables){
//			if (c.isColliding(init)){
//				collisions.add(c);
//			}
//		}
//		return collisions;
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
				playerTanks.put(((Tank) o).getTeam(), (Tank)o);
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
			playerTanks.remove(((Tank) o).getTeam());
		}
	}
	public void addQueue(Object o){
		if (o instanceof Collection){
			addQue.addAll((Collection<? extends Object>) o);
		} else {
			addQue.add(o);
		}
	}
	public void removeQueue(Object o){
		removeQue.add(o);
	}
	public ArrayList<Tank> getTanks(){
		return new ArrayList<Tank>(allTanks);
	}
	public ArrayList<Block> getBlocks(){
		ArrayList<Block> blocks = new ArrayList<Block>();
		for (Collidable c : collidables){
			if (c instanceof Block){
				blocks.add((Block) c);
			}
		}
		return blocks;
	}
	
	public HashSet<Drawable> getSend() {
		HashSet<Drawable> sends = new HashSet<Drawable>();
		for (Drawable d : drawables){
			if (d instanceof Sendable){
				sends.add(((Sendable)d).getProxyClass());
			}
		}
		return sends;
	}
	
	public int getSize(){
		return updatables.size();
	}
	
	public int getNumPlayers() {
		return playerTanks.size();
	}
}
