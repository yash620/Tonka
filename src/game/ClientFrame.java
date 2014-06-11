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
import java.util.HashSet;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.Timer;

import util.Drawable;
import util.KeyInput;
import weapon.*;
import weapon.Weapon.WeaponList;

public class ClientFrame {
	public static void main(String[] args) {
		new ClientFrame();
	}

	private JFrame frame;
	private Timer ti;
	private Dimension windowSize;
	private Client client;
	private HashSet<Drawable> drawables;

	public static final int TIMESTEP = 17;

	public ClientFrame() {
		String ip = JOptionPane.showInputDialog("IP?");
		if (ip == null) {
			ip = "localhost";
		}
		client = new Client(ip, 34556);
		client.startThread();
		
		frame = new JFrame();
		windowSize = new Dimension(1280, 720);
		Listener li = new Listener();
		frame.addKeyListener(li);
		JPanel mainDraw = new MainDraw();
		frame.add(mainDraw);
		mainDraw.addMouseListener(li);
		mainDraw.addMouseMotionListener(li);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ti = new Timer(TIMESTEP, li);
		ti.start();
	}

	@SuppressWarnings("serial")
	public class MainDraw extends JPanel {
		public MainDraw() {
			this.setPreferredSize(windowSize);
		}
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			if (drawables != null){
				for (Drawable d : drawables){
					d.draw(g2);
					if (d instanceof Tank && ((Tank)d).isAI() == false){
//						System.out.println(((Tank)d).getCenter());
					}
				}
			}
		}
	}
	
	public class SelectPanel extends JPanel {
		public SelectPanel() {
			this.setPreferredSize(windowSize);
			this.setVisible(true);
			ButtonGroup bg = new ButtonGroup();
			for (WeaponList wl : Weapon.WeaponList.values()) {
				JRadioButton jrb = new JRadioButton(wl.name());
				bg.add(jrb);
				this.add(jrb);
			}
		}
	}

	public class Listener implements KeyListener, ActionListener,
			MouseListener, MouseMotionListener {
		
		private int down, right;
		private Point clickPoint = new Point();
		private boolean shoot;

		@Override
		public void keyPressed(KeyEvent arg0) {
			if (arg0.getKeyCode() == KeyEvent.VK_W) {
				down = -1;
			}
			if (arg0.getKeyCode() == KeyEvent.VK_S) {
				down = 1;
			}
			if (arg0.getKeyCode() == KeyEvent.VK_D) {
				right = 1;
			}
			if (arg0.getKeyCode() == KeyEvent.VK_A) {
				right = -1;
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
				right = 0;
			}
			if (arg0.getKeyCode() == KeyEvent.VK_W
					|| arg0.getKeyCode() == KeyEvent.VK_S) {
				down = 0;
			}
		}

		@Override
		public void keyTyped(KeyEvent arg0) {

		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
//			game.update(down, right, clickpoint);
			drawables = client.getGame();
			client.sendInputs(new KeyInput(down, right, clickPoint, shoot));
			frame.repaint();
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {

		}

		@Override
		public void mouseEntered(MouseEvent arg0) {

		}

		@Override
		public void mouseExited(MouseEvent arg0) {

		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			shoot = true;
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			shoot = false;
		}

		@Override
		public void mouseDragged(MouseEvent arg0) {
			clickPoint = arg0.getPoint();
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			clickPoint = arg0.getPoint();
		}

	}
}
