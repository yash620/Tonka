package game;

import java.awt.Rectangle;
import java.util.HashSet;

import util.Collidable;

public class CollisionMap {
	private CollisionCell[][] collisionArr;

	public CollisionMap() {
		collisionArr = new CollisionCell[5][4];
		for (int i = 0;i<collisionArr.length;i++){
			for (int j = 0;j<collisionArr[0].length;j++){
				collisionArr[i][j] = new CollisionCell(new Rectangle(i*256, j*180, 256, 180));
			}
		}
	}
	
	public void updateCollidables(HashSet<Collidable> c) {
		for (int i = 0;i<collisionArr.length;i++){
			for (int j = 0;j<collisionArr[0].length;j++){
				collisionArr[i][j].updateCollidable(c);
			}
		}
	}
	
	public HashSet<Collidable> getCollisions(Collidable init){
		HashSet<Collidable> allCollisions = new HashSet<Collidable>();
		for (int i = 0;i<collisionArr.length;i++){
			for (int j = 0;j<collisionArr[0].length;j++){
				if (collisionArr[i][j].intersectsRectangle(init)){
					allCollisions.addAll(collisionArr[i][j].getCollisions(init));
				}
			}
		}
		return allCollisions;
	}
}

class CollisionCell {
	private HashSet<Collidable> collidables;
	private final Rectangle bounds;
	public CollisionCell(Rectangle bounds) {
		this.bounds = bounds;
		collidables = new HashSet<Collidable>();
	}
	public HashSet<Collidable> getCollisions(Collidable init) {
		HashSet<Collidable> collisions = new HashSet<Collidable>();
		for (Collidable c : collidables){
			if (c.isColliding(init)){
				collisions.add(c);
			}
		}
		return collisions;
	}
	public void updateCollidable(HashSet<Collidable> collides) {
		collidables.clear();
		for (Collidable c : collides){
			if (bounds.intersects(c.getBoundingBox())) {
				collidables.add(c);
			}
		}
	}
	public boolean intersectsRectangle(Collidable init){
		return bounds.intersects(init.getBoundingBox());
	}
}