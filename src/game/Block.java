package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;

import util.Collidable;
import util.Drawable;
import util.SerialArea;
import util.Updatable;
import weapon.Projectile;

public class Block implements Drawable, Collidable, Updatable, Serializable {
	private Color color;
	private SerialArea blockShape;
	private boolean destructible;
	private Game game;

	public Block(Shape s, boolean b, Color c, Game g){
		color = c;
		blockShape = new SerialArea(s);
		destructible = b;
		this.game = g;
	}
	
	public Block(Shape s, boolean b, Game g){
		this(s,b,Color.black, g);
	}
	
	public Block(Shape s, Game g) {
		this(s,true, g);
	}

	public Block(Game g) {
		this(new Rectangle(600, 300, 50, 200), g);
	}

	public double getBoundsArea() {
		return blockShape.getBounds().getHeight()
				* blockShape.getBounds().getWidth();
	}

	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(color);
		g2.fill(blockShape);
	}

	public Shape getShape() {
		return blockShape;
	}

	@Override
	public void collision(Collidable s) {
		if (s instanceof Projectile) {
			s.collision(this);
			if(destructible){
				Area projectile = new Area(((Projectile) s).getDestroyed());
				blockShape.subtract(projectile);
			}
		}
	}

	@Override
	public Rectangle getBoundingBox() {
		return blockShape.getBounds();
	}
	
	public Point getCenter(){
		return new Point((int)getBoundingBox().getCenterX(), (int) getBoundingBox().getCenterY());
	}

	@Override
	public boolean isColliding(Collidable c) {
		if (c.equals(this)) {
			return false;
		}
		if (c.getBoundingBox().intersects((Rectangle2D) this.getBoundingBox())) {
			Area tankArea = new Area(this.getShape());
			tankArea.intersect(new Area(c.getShape()));
			if (!tankArea.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	private ArrayList<Block> splitBlock() {
		/*
		 * If the area is seperated into several bits, then make two different
		 * blocks representing the bits
		 * 
		 * Store all of the path iterator points inside a double array in the
		 * form (type, x, y)
		 */
		PathIterator path = blockShape.getPathIterator(null);
		ArrayList<double[]> points = new ArrayList<double[]>();
		double[] coords = new double[6];
		while (!path.isDone()) {
			int pathType = path.currentSegment(coords);
			double[] pointArr = { pathType, coords[0], coords[1] };
			points.add(pointArr);
			path.next();
		}
		/*
		 * Creates new Polygons for each of the distinct paths It searches for
		 * the SEG_MOVETO, which is the start of a new path
		 */
		ArrayList<Integer> xCoords = new ArrayList<Integer>();
		ArrayList<Integer> yCoords = new ArrayList<Integer>();
		ArrayList<Block> newBlocks = new ArrayList<Block>();
		for (double[] p : points) {
			if (p[0] == PathIterator.SEG_CLOSE) {
				if (!xCoords.isEmpty()) {
					int[] xArr = new int[xCoords.size()];
					int[] yArr = new int[yCoords.size()];
					for (int i = 0; i < xCoords.size(); i++) {
						xArr[i] = xCoords.get(i).intValue();
						yArr[i] = yCoords.get(i).intValue();
					}
					Block addBlock = new Block((Shape) new Polygon(xArr, yArr,
							xCoords.size()), game);
					if (addBlock.getBoundsArea() > 50) {
						newBlocks.add(addBlock);
					}
				}
				xCoords.clear();
				yCoords.clear();
			} else {
				xCoords.add((int) p[1]);
				yCoords.add((int) p[2]);
			}
		}
		return newBlocks;
	}

	@Override
	public void update() {
		if (!blockShape.isSingular()) {
			for (Block b : splitBlock()) {
				game.addQueue(b);
			}
			game.removeQueue(this);
		}
		if (blockShape.isEmpty() || this.getBoundsArea() < 25) {
			game.removeQueue(this);
		}
	}
}
