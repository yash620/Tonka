package UI;

import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import util.Drawable;
import weapon.*;
import game.Tank;

public class TankCreator extends JFrame{
	
	JColorChooser jcc;
	Tank tank;

	public TankCreator(){
		this.setSize(800,600);
		jcc = new JColorChooser();
		tank = new Tank(200,200,new ArrayList<Weapon>(), null);		//Need a pointer to game Glynn
	}
	
	public void chooseColor(){
		JFrame jfc = new JFrame();
		jfc.add(jcc);
		jfc.setVisible(true);
		jfc.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jfc.setSize(600,500);
	}
}
