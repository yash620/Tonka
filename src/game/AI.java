package game;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;

import util.KeyInput;

public class AI {
	private Tank t;
	private Game game;
	private int right;
	public AI(Tank t, Game g){
		this.t = t;
		this.game = g;
	}
	
	public KeyInput getInputs(){
		ArrayList<Tank> players = this.getEnemyTanks();
		ArrayList<Block> blocks = this.getBlocks();
		int minDist = Integer.MAX_VALUE;
		for (Block b : blocks){
			Rectangle bounds = b.getBoundingBox();
			int dist = (int) AI.distToRect(bounds, t.getCenter());
			if (dist < minDist){
				minDist = dist;
			}
		}
		if (minDist < 100 && right == 0){
			if (Math.random() < .5){
				right = 1;
			} else {
				right = -1;
			}
		} else if (minDist >= 100) {
			right = 0;
		}
		return new KeyInput(1, right, new Point((int)players.get(0).getCenter().getX(), (int)players.get(0).getCenter().getY()), true);
	}
	
	public ArrayList<Block> getBlocks(){
		ArrayList<Block> blocks = game.getBlocks();
		Collections.sort(blocks, new Comparator<Block>(){

			@Override
			public int compare(Block o1, Block o2) {
				return (int) o1.getCenter().distanceSq(o2.getCenter());
			}
			
		});
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
		return enemies;
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
