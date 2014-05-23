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
	public AI(Tank t, Game g){
		this.t = t;
		this.game = g;
	}
	
	public KeyInput getInputs(){
		ArrayList<Tank> players = this.getEnemyTanks();
		ArrayList<Block> blocks = this.getBlocks();
		int right = 0;
		for (Block b : blocks){
			Rectangle bounds = b.getBoundingBox();
			int side = bounds.outcode(t.getCenter());
			System.out.println(side);
		}
		return new KeyInput(0, 0, new Point((int)players.get(0).getCenter().getX(), (int)players.get(0).getCenter().getY()), true);
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
}
