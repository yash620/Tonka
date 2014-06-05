package weapon;

import game.Game;
import game.Transform;

import java.awt.geom.Point2D;

import util.Collidable;

public class RichardMissile extends BasicMissile{

	public RichardMissile(Point2D center, double theta, double damage,
			Weapon weapon, Game game) {
		super((int)(center.getX()), (int)(center.getY()), theta, 3, damage, weapon, game);
		// TODO Auto-generated constructor stub
	}
	
	public void update(){
		damage = damage + .1;
		//I'm not changing this
		deltax = deltax * 1.06;
		deltay = deltay * 1.06;
		super.update();
	}
}
