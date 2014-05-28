package UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import util.Drawable;
import weapon.*;
import game.Tank;

public class TankCreator extends JFrame{
	
	JColorChooser jcc = new JColorChooser();
	Weapon holding;
	Panel p;
	Tank tank;
	Listener l;
	Timer t;
	boolean focusoncolor;
	int wppts, spdpts, hppts, armrpts, ttlpts;

	public static void main(String[] args){
		new TankCreator();		
	}
	
	public TankCreator(){
		this.setSize(800,600);
		this.setVisible(true);
		this.setTitle("Tank Creator");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		p = new Panel();
		holding = null;
		tank = new Tank(200,200,new ArrayList<Weapon>(), null);		//Need a pointer to game Glynn
		this.add(p);
		l = new Listener();
		t = new Timer(10, l);
		t.start();
		this.addKeyListener(new Key());
		this.addMouseListener(new Mouse());
		ttlpts = 100;
		wppts = 0;
		spdpts = 0;
		hppts = 0;
		armrpts = 0;
	}
	
	public class Listener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			//System.out.println("Tick");
			if(jcc.getColor() != null){
				tank.setColor(jcc.getColor());
			}
			repaint();
		}
		
	}
	
	public class Panel extends JPanel{
		
		@Override
		public void paintComponent(Graphics g){
			Graphics2D g2 = (Graphics2D)g;
			tank.draw(g2);
		}
	}
	
	public void chooseColor(){
		JFrame jfc = new JFrame();
		jfc.add(jcc);
		jfc.setVisible(true);
		jfc.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jfc.setSize(600,500);
	}
	
	public class Key implements KeyListener{

		@Override
		public void keyPressed(KeyEvent arg0) {
			// TODO Auto-generated method stub
			//System.out.println("Enter Pressed");
			if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
				chooseColor();
			}
			if(arg0.getKeyCode() == KeyEvent.VK_UP){
				System.out.println("A");
			}
			if(arg0.getKeyCode() == KeyEvent.VK_DOWN){
			}
			if(arg0.getKeyCode() == KeyEvent.VK_LEFT){
			}
			if(arg0.getKeyCode() == KeyEvent.VK_RIGHT){
			}
			if(arg0.getKeyCode() == KeyEvent.VK_R){
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			if(arg0.getKeyCode() == KeyEvent.VK_UP){
			}
			if(arg0.getKeyCode() == KeyEvent.VK_DOWN){
			}
			if(arg0.getKeyCode() == KeyEvent.VK_LEFT){
			}
			if(arg0.getKeyCode() == KeyEvent.VK_RIGHT){
			}
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public class Mouse implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			if(arg0.getX() > 600){
				if(arg0.getY() <= 200){
					holding = new BasicTurret();
				}
				else if(arg0.getY() <= 400){
					
				}
				else{
					
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			if(holding != null){
				
			}
		}
		
	}

}
