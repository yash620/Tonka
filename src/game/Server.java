package game;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentListener;

import util.Drawable;
import util.KeyInput;
import util.Startable;
import weapon.Weapon.WeaponList;

public class Server implements ActionListener, Runnable, Startable {
	
	public static void main(String[] args) {
		new Server().startThread();
	}
	
	private int serverport = 34556; 
	private ServerSocket serversocket = null;
	private Socket sock = null;
	private Thread serverThread;
	private Timer ti;
	private ArrayList<Connection> allconnections;
//	private boolean start = false;
	private Game game;
	private int serverindex = 0;
	
	private int time;
	
	private double frequency;	//Frequency you get a double weapon
	
	private JFrame frame;
		
	public Server() {
		frame = new JFrame();
		Settings s = new Settings((Startable) this);
		frame.add(s);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		s.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE) {
					game = new Game(allconnections.size(), .03);
				}
			}
			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		allconnections = new ArrayList<Connection>();
		ti = new Timer(17, this);
		serverThread = new Thread(this);
        try {
			serversocket = new ServerSocket(serverport);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void startGame(){
<<<<<<< HEAD
		System.out.println("Start");
=======
>>>>>>> 8ee743646ad6af89fbfc6e4a4267dac2f69c97f9
		game = new Game(allconnections.size(), .03);
		ti.start();
	}
	public void startThread(){
		serverThread.start();
	}
	
	public void waitForConnection(){
		try {
			System.out.println("Waiting");
			sock = serversocket.accept();
			sock.setTcpNoDelay(true);
			sock.setPerformancePreferences(0, 1, 0);
			System.out.println("Connected");
			OutputStream out = sock.getOutputStream();
			InputStream in = sock.getInputStream();
			Connection connection = new Connection(in, out, serverindex);
			serverindex++;
			allconnections.add(connection);
			connection.startThread();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		while (true){
			this.waitForConnection();
		}
	}
	public void sendAll(){
		for (Connection c : allconnections){
			c.send(game);
		}
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		time++;
		if (time % 500 == 0) {
			time = 0;
			this.resetAll();
			System.out.println("Reset");
		}
		if (game.isFinished()) {
			game = new Game(allconnections.size(), .03);
		}
		for (Connection c : allconnections){
			game.update(c.getInputs(), c.getIndex());
		}
		game.tick();
		if (time % 2 == 0){
			this.sendAll();
		}
	}
	public void resetAll(){
		for (Connection c : allconnections){
			c.resetStream();
		}
	}
}

class Connection implements Runnable {
	private Thread connectionThread;
//	private OutputStream connectout;
//	private InputStream connectin;
	private ObjectOutputStream objectOut;
	private ObjectInputStream objectIn;
	private KeyInput input;
	private final int index;
	
	public Connection(InputStream in, OutputStream out, int playernum){
		this.index = playernum;
		connectionThread = new Thread(this);
		input = new KeyInput(0,0,new Point(), false);
//		this.connectout = out;
//		this.connectin = in;
		//Created the streams
		try {
			System.out.println("Streams Created");
			this.objectOut = new ObjectOutputStream(new BufferedOutputStream(out));
			objectOut.flush();
			this.objectIn = new ObjectInputStream(new BufferedInputStream(in));
			System.out.println("input Created");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Connection Created");
	}

	@Override
	public void run() {
		while (true){
			this.read();
		}
	}
	
	public void startThread(){
		connectionThread.start();
	}
	
	public void read(){
		try {
			Object o = objectIn.readObject();
			input = ((KeyInput)o);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public KeyInput getInputs(){
		return input;
	}
	
	int maxtime;
	public void send(Game game){
		HashSet<Drawable> sends = game.getSend();
//		long start = System.currentTimeMillis();
		try {
			objectOut.writeObject(sends);
<<<<<<< HEAD
			this.writeToFile(sends, "sends.txt");
=======
			objectOut.writeInt(index+1);
//			this.writeToFile(sends, "sends.txt");
>>>>>>> 8ee743646ad6af89fbfc6e4a4267dac2f69c97f9
			objectOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		long end = System.currentTimeMillis();
//		int time = (int) (end-start);
//		if (time > maxtime){
//			maxtime = time;
//		}
//		System.out.println(time + " " + maxtime  + " " + sends.size());
//		this.writeToFile(game.getDrawables(), "test.txt");

	}
	
	public void resetStream(){
		try {
			objectOut.reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getIndex(){
		return this.index;
	}
	
	public void writeToFile(Object object, String filename){
		FileOutputStream out = null;
		ObjectOutputStream objectout = null;
		try {
			out = new FileOutputStream(filename);
			objectout = new ObjectOutputStream(out);
			objectout.writeObject(object);
			objectout.flush();
//			System.out.println("recorded");
			objectout.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
