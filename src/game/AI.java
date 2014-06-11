package game;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;

import util.KeyInput;
import weapon.Weapon;

public class AI {
	private Tank tank;
	private Game game;
	private int right;
	private int down = 1;
	private boolean fire;
	
	private Point2D prevCenter;
	private static final int ThreeDir = 3;
	private static final int TwoDir = 2;
	public AI(Tank t, Game g){
		this.tank = t;
		this.game = g;
		this.prevCenter = tank.getCenter();
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
		boolean hasAmmo = false;
		double weaponAngle = 0;
		for (Weapon w : tank.getWeapons()) {
			if (w.getAmmo() > 0) {
				hasAmmo = true;
			}
			weaponAngle = w.getAngle();
		}
		if (hasAmmo && fire == false) {
			double targetDist = tank.getCenter().distance(target);
			Line2D fireLine = new Line2D.Double(tank.getCenter().getX(), tank.getCenter().getY(),
					tank.getCenter().getX() + targetDist * Math.cos(Math.toRadians(weaponAngle)),
					tank.getCenter().getY() + targetDist * Math.sin(Math.toRadians(weaponAngle)));
			if (Math.abs(AI.angleToRect(players.get(0).getBoundingBox(), tank.getCenter()) -
					tank.getWeapons().get(0).getAngle()) > 30){
				fire = true;
			}
			for (Block b : game.getBlocks()) {
				if (fireLine.intersects(b.getBoundingBox())) {
					fire = false;
					break;
				}
			}
		}
		if (hasAmmo == false) {
			fire = false;
		}
		return new KeyInput(0, right, target, false);
	}
	
	public ArrayList<Block> getBlocks(){
		ArrayList<Block> blocks = game.getBlocks();
		return blocks;
	}
	
	public ArrayList<Tank> getEnemyTanks(){
		ArrayList<Tank> tanks = game.getTanks();
		ArrayList<Tank> enemies = new ArrayList<Tank>();
		for (Tank t : tanks){
			if (!t.isAI()){
				int index = enemies.size();
				for (Tank e : enemies) {
					if (tank.getCenter().distanceSq(e.getCenter()) >
						tank.getCenter().distanceSq(t.getCenter())) {
						index = enemies.indexOf(e)-1;
					}
				}
				if (index < 0) {
					index = 0;
				}
				enemies.add(index, t);
			}
		}
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
