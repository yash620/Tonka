package map;

import game.Block;

import java.io.Serializable;
import java.util.ArrayList;

public class Map implements Serializable{
	
	private ArrayList<Block> blocks;
	
	public Map(ArrayList<Block> blocker){
		blocks = new ArrayList<Block>();
		addBlock(blocker);
	}
	
	public void addBlock(ArrayList<Block> blocker){
		blocks.addAll(blocker);
	}
	
	public void addBlock(Block blocke){
		blocks.add(blocke);
	}
}
