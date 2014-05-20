package UI;

import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import util.Drawable;
import weapon.*;
import game.Tank;

public class TankCreator implements Drawable{
	
	JColorChooser jcc;
	Tank tank;

	public TankCreator(){
		jcc = new JColorChooser();
		tank = new Tank(200,200,new ArrayList<Weapon>());
	}
	
	public void chooseColor(){
		JFrame jfc = new JFrame();
		jfc.add(jcc);
		jfc.setVisible(true);
		jfc.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jfc.setSize(600,500);
	}

	@Override
	public void draw(Graphics2D g2) {
		// TODO Auto-generated method stub
		
	}
}
