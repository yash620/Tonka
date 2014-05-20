package game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import util.Drawable;
import util.KeyInput;
import weapon.BasicMissile;

public class ClientFrame {
	public static void main(String[] args) {
		new ClientFrame();
	}

	private JFrame frame;
	private Timer ti;
	private Dimension windowSize;
	private Client client;
	private ArrayList<Drawable> drawables;

	public static final int TIMESTEP = 17;

	public ClientFrame() {
		client = new Client("localhost", 34556);
		client.startThread();
		
		frame = new JFrame();
		windowSize = new Dimension(1280, 720);
		frame.setSize(windowSize);
		Listener li = new Listener();
		frame.addKeyListener(li);
		JPanel mainDraw = new MainDraw();
		frame.add(mainDraw);
		mainDraw.addMouseListener(li);
		mainDraw.addMouseMotionListener(li);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ti = new Timer(TIMESTEP, li);
		ti.start();
	}

	@SuppressWarnings("serial")
	public class MainDraw extends JPanel {
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			if (drawables != null){
				for (Drawable d : drawables){
					d.draw(g2);
					if (d instanceof BasicMissile){
						System.out.println(((BasicMissile)d).getDX());
					}
				}
			}
		}
	}

	public class Listener implements KeyListener, ActionListener,
			MouseListener, MouseMotionListener {
		
		private KeyInput inputs;
		
		public Listener() {
			inputs = new KeyInput(0,0,new Point(), false);
		}

		@Override
		public void keyPressed(KeyEvent arg0) {
			if (arg0.getKeyCode() == KeyEvent.VK_W) {
				inputs.setDown(-1);
			}
			if (arg0.getKeyCode() == KeyEvent.VK_S) {
				inputs.setDown(1);
			}
			if (arg0.getKeyCode() == KeyEvent.VK_D) {
				inputs.setRight(1);
			}
			if (arg0.getKeyCode() == KeyEvent.VK_A) {
				inputs.setRight(-1);
			}
			if (arg0.getKeyCode() == KeyEvent.VK_P) {
				if (ti.isRunning()) {
					ti.stop();
				} else {
					ti.start();
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			if (arg0.getKeyCode() == KeyEvent.VK_A
					|| arg0.getKeyCode() == KeyEvent.VK_D) {
				inputs.setRight(0);
			}
			if (arg0.getKeyCode() == KeyEvent.VK_W
					|| arg0.getKeyCode() == KeyEvent.VK_S) {
				inputs.setDown(0);
			}
		}

		@Override
		public void keyTyped(KeyEvent arg0) {

		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
//			game.update(down, right, clickpoint);
			drawables = client.getGame();
			client.sendInputs(inputs);
			frame.repaint();
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {

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
			inputs.setShoot(true);
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			inputs.setShoot(false);
		}

		@Override
		public void mouseDragged(MouseEvent arg0) {

		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			inputs.setClickPoint(arg0.getPoint());
		}

	}
}
