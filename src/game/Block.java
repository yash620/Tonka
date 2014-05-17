package game;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import util.Collidable;
import util.Drawable;
import weapon.Projectile;

public class Block implements Drawable, Collidable {
	private Area blockShape;

	public Block(Shape s) {
		blockShape = new Area(s);
	}

	public Block() {
		this(new Rectangle(600, 300, 50, 200));
	}

	private double getBoundsArea() {
		return blockShape.getBounds().getHeight()
				* blockShape.getBounds().getWidth();
	}

	@Override
	public void draw(Graphics2D g2) {
		g2.fill(blockShape);
	}

	public Shape getShape() {
		return blockShape;
	}

	@Override
	public void collision(Collidable s) {
		if (s instanceof Projectile) {
			s.collision(this);
			Area projectile = new Area(((Projectile) s).getDestroyed());
			blockShape.subtract(projectile);
			if (!blockShape.isSingular()) {
				Game.removeQueue(this);
				for (Block b : splitBlock()) {
					Game.addQueue(b);
				}
			}
			if (blockShape.isEmpty() || this.getBoundsArea() < 25) {
				Game.removeQueue(this);
			}
		}
	}

	@Override
	public Shape getBoundingBox() {
		return blockShape.getBounds();
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
							xCoords.size()));
					if (addBlock.getBoundsArea() > 25) {
						newBlocks.add(addBlock);
					}
					xCoords.clear();
					yCoords.clear();
				}
			} else {
				xCoords.add((int) p[1]);
				yCoords.add((int) p[2]);
			}
		}
		return newBlocks;
	}
}
