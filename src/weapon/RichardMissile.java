package weapon;

import game.Game;
import game.SinglePlayer;
import game.Transform;

import java.awt.geom.Point2D;

import util.Collidable;

public class RichardMissile extends BasicMissile{
	private static int numOfRockets = 0;
	public RichardMissile(Point2D center, double theta, double damage,
			Weapon weapon, Game game) {
		super((int)(center.getX()), (int)(center.getY()), 1, damage, theta, weapon, game);
		numOfRockets++;
		// TODO Auto-generated constructor stub
	}
	
	public void update(){
		if ((int)this.getBoundingBox().getCenterX() > (int)Game.windowSize.width ||
				(int)this.getBoundingBox().getCenterX() < 0 &&
				this.getBoundingBox().getCenterY() > (int)Game.windowSize.height ||
				this.getBoundingBox().getCenterY() < 0){
			game.removeQueue(this);
			numOfRockets--;
			return;
		}
		damage = damage * 1.01;
		//I'm not changing this
		if (deltax < 50)
			deltax = deltax * 1.04;
		if (deltay < 50)
			deltay = deltay * 1.04;
		super.update();
	}
}
