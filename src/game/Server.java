package game;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;

import util.Drawable;
import util.KeyInput;

public class Server implements ActionListener, Runnable {
	
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
	
	private JFrame frame;
		
	public Server(){
		frame = new JFrame();
		JButton button = new JButton();
		button.setText("PLAY");
		frame.add(button);
		button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				startGame();
			}
			
		});
		button.setPreferredSize(new Dimension(50,50));
		frame.pack();
		frame.setVisible(true);
		allconnections = new ArrayList<Connection>();
		ti = new Timer(17, this);
		serverThread = new Thread(this);
        try {
			serversocket = new ServerSocket(serverport);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startGame(){
		System.out.println("Start");
		game = new Game(allconnections.size());
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
			this.writeToFile(sends, "sends.txt");
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
