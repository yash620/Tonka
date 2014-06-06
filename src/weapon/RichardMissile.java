package weapon;

import game.Game;
import game.Transform;

import java.awt.geom.Point2D;

import util.Collidable;

public class RichardMissile extends BasicMissile{

	public RichardMissile(Point2D center, double theta, double damage,
			Weapon weapon, Game game) {
		super((int)(center.getX()), (int)(center.getY()), 1, damage, theta, weapon, game);
		// TODO Auto-generated constructor stub
	}
	
	public void update(){
		damage = damage * 1.01;
		//I'm not changing this
		if (deltax < 50)
			deltax = deltax * 1.04;
		if (deltay < 50)
			deltay = deltay * 1.04;
		super.update();
	}
}
