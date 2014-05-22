package game;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import util.KeyInput;

public class AI {
	private Tank t;
	private Game game;
	public AI(Tank t, Game g){
		this.t = t;
		this.game = g;
	}
	
	public KeyInput getInputs(){
		ArrayList<Tank> players = game.getPlayerTanks();
		return new KeyInput(1, 0, new Point((int)players.get(0).getCenter().getX(), (int)players.get(0).getCenter().getY()), true);
	}
}
