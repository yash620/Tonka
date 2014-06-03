package game;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;

import util.KeyInput;

public class AI {
	private Tank tank;
	private Game game;
	private int right;
	private int down = 1;
	private Point2D prevCenter;
	private Rectangle previousRect;
	private static final int ThreeDir = 3;
	private static final int TwoDir = 2;
	public AI(Tank t, Game g){
		this.tank = t;
		this.game = g;
		this.prevCenter = tank.getCenter();
		this.previousRect = tank.getBoundingBox();
	}
	
	public KeyInput getInputs(){
		if (tank == null){
			return new KeyInput(0,0,new Point(), false);
		}
		ArrayList<Tank> players = this.getEnemyTanks();
		ArrayList<Block> blocks = this.getBlocks();
		int minDist = Integer.MAX_VALUE;
		Block minBlock = null;
		for (Block b : blocks){
			Rectangle bounds = b.getBoundingBox();
			int dist = (int) AI.distToRect(bounds, tank.getCenter());
			if (dist < minDist){
				minDist = dist;
				minBlock = b;
			}
		}
		if (minDist < 100 && right == 0){
			right = AI.randomDirection(AI.TwoDir);
		} else if (minBlock != null && Math.abs(AI.angleToRect(minBlock.getBoundingBox(),
				tank.getCenter()) - tank.getTheta()) > 135 || minDist >= 100) {
			right = 0;
		}
		if (down != 0 && this.prevCenter.equals(tank.getCenter())){
			down *= -1;
			right = AI.randomDirection(AI.ThreeDir);
		}
		this.prevCenter = tank.getCenter();
		Point target = new Point((int)players.get(0).getCenter().getX(),
				(int)players.get(0).getCenter().getY());
		return new KeyInput(down, right, target, true);
	}
	
	public ArrayList<Block> getBlocks(){
		ArrayList<Block> blocks = game.getBlocks();
		Collections.sort(blocks, new Comparator<Block>(){

			@Override
			public int compare(Block o1, Block o2) {
				return (int) o1.getCenter().distanceSq(o2.getCenter());
			}
			
		});
		Collections.reverse(blocks);
		return blocks;
	}
	
	public ArrayList<Tank> getEnemyTanks(){
		ArrayList<Tank> tanks = game.getTanks();
		ArrayList<Tank> enemies = new ArrayList<Tank>();
		for (Tank t : tanks){
			if (!t.isAI()){
				enemies.add(t);
			}
		}
		Collections.sort(enemies, new Comparator<Tank>(){
			
			@Override
			public int compare(Tank o1, Tank o2) {
				return (int) o1.getCenter().distanceSq(o2.getCenter());
			}
			
		});
		Collections.reverse(enemies);
		return enemies;
	}
	
	public ArrayList<Tank> getFriendlyTanks(){
		ArrayList<Tank> tanks = game.getTanks();
		ArrayList<Tank> friendlies = new ArrayList<Tank>();
		for (Tank t : tanks){
			if (t.isAI() && t != this.tank){
				friendlies.add(t);
			}
		}
		Collections.sort(friendlies, new Comparator<Tank>(){
			
			@Override
			public int compare(Tank o1, Tank o2) {
				return (int) o1.getCenter().distanceSq(o2.getCenter());
			}
			
		});
		Collections.reverse(friendlies);
		return friendlies;
	}
	
	public static double angleToRect(Rectangle rect, Point2D p2){
		int side = rect.outcode(p2);
		//Sides
		if (side == Rectangle.OUT_BOTTOM){
			return 90;
		}
		if (side == Rectangle.OUT_LEFT){
			return 180;
		}
		if (side == Rectangle.OUT_TOP){
			return -90;
		}
		if (side == Rectangle.OUT_RIGHT){
			return 0;
		}
		//Corners
		Point2D p = null;
		if (side == (Rectangle.OUT_TOP | Rectangle.OUT_LEFT)){
			p = new Point2D.Double(rect.getMinX(), rect.getMinY());
		}
		if (side == (Rectangle.OUT_TOP| Rectangle.OUT_RIGHT)){
			p = new Point2D.Double(rect.getMaxX(), rect.getMinY());
		}
		if (side == (Rectangle.OUT_BOTTOM | Rectangle.OUT_RIGHT)){
			p = new Point2D.Double(rect.getMaxX(), rect.getMaxY());
		}
		if (side == (Rectangle.OUT_BOTTOM | Rectangle.OUT_LEFT)){
			p = new Point2D.Double(rect.getMinX(), rect.getMaxY());
		}
		if (p == null){
			return 0;
		}
		return AngleMath.adjustAngle(Math.toDegrees(Math.atan2(p.getY()-p2.getY(), p.getX()-p2.getX()))+180);
	}
	
	public static double distToRect(Rectangle rect, Point2D p2){
		int side = rect.outcode(p2);
		//Sides
		if (side == Rectangle.OUT_BOTTOM || side == Rectangle.OUT_TOP) {
			return Math.min(Math.abs(rect.getMinY() - p2.getY()),
					Math.abs(rect.getMaxY() - p2.getY()));
		}
		if (side == Rectangle.OUT_LEFT || side == Rectangle.OUT_RIGHT) {
			return Math.min(Math.abs(rect.getMinX() - p2.getX()),
					Math.abs(rect.getMaxX() - p2.getX()));
		}
		//Corners
		if (side == (Rectangle.OUT_TOP | Rectangle.OUT_LEFT)){
			Point2D p = new Point2D.Double(rect.getMinX(), rect.getMinY());
			return p.distance(p2);
		}
		if (side == (Rectangle.OUT_TOP| Rectangle.OUT_RIGHT)){
			Point2D p = new Point2D.Double(rect.getMaxX(), rect.getMinY());
			return p.distance(p2);
		}
		if (side == (Rectangle.OUT_BOTTOM | Rectangle.OUT_RIGHT)){
			Point2D p = new Point2D.Double(rect.getMaxX(), rect.getMaxY());
			return p.distance(p2);
		}
		if (side == (Rectangle.OUT_BOTTOM | Rectangle.OUT_LEFT)){
			Point2D p = new Point2D.Double(rect.getMinX(), rect.getMaxY());
			return p.distance(p2);
		}
		return 0;
	}
	
	public static double angleToPoint(Point2D start, Point2D end){
		double tgtAng = Math.atan2(end.getY() - start.getY(), end.getX() - start.getX());
		return AngleMath.adjustAngle(Math.toDegrees(tgtAng)+180);
	}
	
	public static int randomDirection(int directions){
		if (directions == AI.ThreeDir){
			if (Math.random() < 1.0/3){
				return -1;
			}
			if (Math.random() < 2.0/3){
				return 0;
			}
			return 1;
		}
		if (directions == AI.TwoDir){
			if (Math.random() < .5){
				return 1;
			}
			return -1;
		}
		return 0;
	}
	
//	public double distToRect(Rectangle rect, Point2D p){
//		//This code basically tells us what side the point is on
//		int side = rect.outcode(t.getCenter());
//		//Sides
//		if (side == Rectangle.OUT_BOTTOM || side == Rectangle.OUT_TOP) {
//			return Math.min(Math.abs(rect.getMinY() - t.getCenter().getY()),
//					Math.abs(rect.getMaxY() - t.getCenter().getY()));
//		}
//		if (side == Rectangle.OUT_LEFT || side == Rectangle.OUT_RIGHT) {
//			return Math.min(Math.abs(rect.getMinX() - t.getCenter().getX()),
//					Math.abs(rect.getMaxX() - t.getCenter().getX()));
//		}
//		//Corners
//		if (side == (Rectangle.OUT_TOP | Rectangle.OUT_LEFT)){
//			Point2D p = new Point2D.Double(rect.getMinX(), rect.getMinY());
//			return p.distance(t.getCenter());
//		}
//		if (side == (Rectangle.OUT_TOP| Rectangle.OUT_RIGHT)){
//			Point2D p = new Point2D.Double(rect.getMaxX(), rect.getMinY());
//			return p.distance(t.getCenter());
//		}
//		if (side == (Rectangle.OUT_BOTTOM | Rectangle.OUT_RIGHT)){
//			Point2D p = new Point2D.Double(rect.getMaxX(), rect.getMaxY());
//			return p.distance(t.getCenter());
//		}
//		if (side == (Rectangle.OUT_BOTTOM | Rectangle.OUT_LEFT)){
//			Point2D p = new Point2D.Double(rect.getMinX(), rect.getMaxY());
//			return p.distance(t.getCenter());
//		}
//		return 0;
//	}
}
