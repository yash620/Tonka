package game;


import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;

public class Map {
	
	private ArrayList<Block> blocks;
	private Game game;
	
	public Map(Game game){
		this.game = game;
		blocks = new ArrayList<Block>();
	}
	
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
	
	public ArrayList<Block> showBlocks(){
		return blocks;
	}
	
	public void basicMap(){
		addBlock(new Block(new Rectangle(0,0,1280,20), false, game));
		addBlock(new Block(new Rectangle(0,700,1280,20), false, game));
		addBlock(new Block(new Rectangle(0,0,20,720), false, game));
		addBlock(new Block(new Rectangle(1260,0,20,720), false, game));
		addBlock(new Block(new Rectangle(300, 200, 75, 350), true, game));
		addBlock(new Block(new Rectangle(800, 200, 75, 350), true, game));

	}
}
