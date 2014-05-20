package game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import util.Drawable;
import util.KeyInput;

public class Client implements Runnable {
	private String hostName;					//IP of host
//	private int port;							//Port of host
	private Socket socket;						//Socket of the connection
//	InputStream in;				//Input
//	OutputStream out;			//Output
	private ObjectInputStream clientIn;
	private ObjectOutputStream clientOut;
	private Thread clientThread;		//Thread for reading
	private ArrayList<Drawable> drawables;
	
	public Client(String hostname, int port){
		this.hostName = hostname;
//		this.port = port;
		//Socketing
		InetAddress hostIP = null;
		try {
			hostIP = InetAddress.getByName(hostName);
			System.out.println("Creating socket");
			socket = new Socket(hostIP, port);
			System.out.println("Creating output");
			clientOut = new ObjectOutputStream(socket.getOutputStream());
			clientOut.flush();
			System.out.println(clientOut);
			System.out.println("creating input");
			clientIn = new ObjectInputStream(socket.getInputStream());
			System.out.println("Created everything");	
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		clientThread = new Thread(this);
	}

	@Override
	public void run() {
		while (true){
			this.read();
		}
	}
	
	public void sendInputs(KeyInput keyinputs){
		try {
			clientOut.writeUnshared(keyinputs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void read(){
		try {
			Object o = clientIn.readUnshared();
			drawables = (ArrayList<Drawable>) o;
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void startThread(){
		clientThread.start();
	}
	
	public ArrayList<Drawable> getGame() {
		return drawables;
	}
}
