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

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import util.Startable;

public class SinglePlayer implements Startable {
	public static void main(String[] args) {
		new SinglePlayer();
	}

	private JFrame frame;
	private Timer ti;
	private Game game;
	private Settings settings;
	
	int frameMS;
	
	private double frequency;

	public static final int TIMESTEP = 17;

	public SinglePlayer() {
		frame = new JFrame();
<<<<<<< HEAD
=======
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
>>>>>>> 8ee743646ad6af89fbfc6e4a4267dac2f69c97f9
		frame.setVisible(true);
		settings = new Settings(this);
		frame.add(settings);
		frame.pack();
	}
	@SuppressWarnings("serial")
	public class MainDraw extends JPanel {
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			game.draw(g2);
<<<<<<< HEAD
			g2.drawString(cp.toString(), 100, 100);
			g2.drawString(Integer.toString(frameMS) + "   " + Integer.toString(game.getSize()), 1100,100);
=======
//			g2.drawString(cp.toString(), 100, 100);
//			g2.drawString(Integer.toString(frameMS) + "   " + Integer.toString(game.getSize()), 1100,100);
>>>>>>> 8ee743646ad6af89fbfc6e4a4267dac2f69c97f9
//			g2.drawString(Double.toString(AI.angleToPoint(game.getTanks().get(0).getCenter(),
//					cp)), 100,100);
		}
	}
	
	public Point cp = new Point();

	public class Listener implements KeyListener, ActionListener,
			MouseListener, MouseMotionListener {
		private int down, right;
		private Point clickpoint;
		private boolean shoot;

		public Listener() {
			clickpoint = new Point();
		}

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
			if (arg0.getKeyCode() == KeyEvent.VK_R){
				game = new Game(frequency);
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
			long start = System.currentTimeMillis();
			if (game.isFinished() == false){
			game.update(down, right, clickpoint, shoot);
			} else {
				game = new Game(frequency);
			}
			frame.repaint();

			long end = System.currentTimeMillis();
			frameMS = (int)(end - start);

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
			shoot = true;
			// clickpoint = arg0.getPoint();
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			shoot = false;
		}

		@Override
		public void mouseDragged(MouseEvent arg0) {
			clickpoint = arg0.getPoint();
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			clickpoint = arg0.getPoint();
			cp = arg0.getPoint();
		}

	}

	@Override
	public void startGame() {
		System.out.println("Start");
		frame.remove(settings);
		frequency = settings.getFrequency();
		game = new Game(frequency);
		Listener li = new Listener();
		frame.addKeyListener(li);
		JPanel mainDraw = new MainDraw();
		mainDraw.setPreferredSize(Game.windowSize);
		mainDraw.requestFocus();
		frame.requestFocus();
		frame.add(mainDraw);
		frame.pack();
		mainDraw.addMouseListener(li);
		mainDraw.addMouseMotionListener(li);
		frame.setVisible(true);
<<<<<<< HEAD
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
=======
>>>>>>> 8ee743646ad6af89fbfc6e4a4267dac2f69c97f9
		ti = new Timer(TIMESTEP, li);
		ti.start();
	}

}
