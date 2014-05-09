package game;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;

public class Block implements Drawable, Collidable {
	private Area blockShape;
	public Block(Shape s){
		blockShape = new Area(s);
	}
	public Block(){
		this(new Rectangle(600,300,50,200));
	}
	@Override
	public void draw(Graphics2D g2){
		g2.fill(blockShape);
	}
	public void destroy(Shape s){
		blockShape.subtract(new Area(s));
	}
	public Shape getShape(){
		return blockShape;
	}
	@Override
	public boolean collisionCheck(Shape s) {
		// TODO Auto-generated method stub
		return false;
	}
}
