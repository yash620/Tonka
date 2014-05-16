package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.HashMap;

public class Explosion implements Drawable{

	private double timeAtStart;
	private double timeSinceStart;
	private boolean isDone;
	private boolean chained;
	private double maxSize;
	private double currSize;
	protected double xc, yc;
	private double[][] places;
	private double timelapse = .4;
	
	/*
	 * A HashMap is created with every single color shade that is used in the explosion
	 * The Key value is the shade of Green that is used in the Color, Red is always 225, blue is always 0
	 * This way, we don't have to create a new Color object every time we change the color
	 */
	private static HashMap<Integer, Color> colorList;
	
	public Explosion(double x2, double y2, double size){
		if (Explosion.colorList == null){
			Explosion.colorList = new HashMap<Integer, Color>();
			for (int i = 0;i<=225;i++){
				Explosion.colorList.put(i, new Color(225,i,0));
			}
		}
		xc = x2;
		yc = y2;
		places = new double[2][32];
		places[0][0] = xc;
		places[1][0] = yc;
		for(int d = 1; d < places[0].length; d++){
			places[0][d] = xc + (Math.random()*20 - 10);
			places[1][d] = yc + (Math.random()*20 - 10);
		}
		timeAtStart = ((double)System.currentTimeMillis())/1000;
		timeSinceStart = 0;
		chained = false;
		isDone = false;
		maxSize = size;
		currSize = 0;
	}
	public Explosion(double x2, double y2){
		if (Explosion.colorList == null){
			Explosion.colorList = new HashMap<Integer, Color>();
			for (int i = 0;i<=225;i++){
				Explosion.colorList.put(i, new Color(225,i,0));
			}
		}
		xc = x2;
		yc = y2;
		places = new double[2][32];
		places[0][0] = xc;
		places[1][0] = yc;
		for(int d = 1; d < places[0].length; d++){
			places[0][d] = xc + (Math.random()*20 - 10);
			places[1][d] = yc + (Math.random()*20 - 10);
		}
		timeAtStart = ((double)System.currentTimeMillis())/1000;
		timeSinceStart = 0;
		chained = false;
		isDone = false;
		maxSize = 30;
		currSize = 0;
	}
	public Explosion(Point2D center){
		if (Explosion.colorList == null){
			Explosion.colorList = new HashMap<Integer, Color>();
			for (int i = 0;i<=225;i++){
				Explosion.colorList.put(i, new Color(225,i,0));
			}
		}
		xc = center.getX();
		yc = center.getY();
		places = new double[2][32];
		places[0][0] = xc;
		places[1][0] = yc;
		for(int d = 1; d < places[0].length; d++){
			places[0][d] = xc + (Math.random()*20 - 10);
			places[1][d] = yc + (Math.random()*20 - 10);
		}
		timeAtStart = ((double)System.currentTimeMillis())/1000;
		timeSinceStart = 0;
		chained = false;
		isDone = false;
		maxSize = 30;
		currSize = 0;
	}
	
	public void update(){
		timeSinceStart = ((double)System.currentTimeMillis())/1000 - timeAtStart;
	}
	
	@Override
	public void draw(Graphics2D g2) {
		// TODO Auto-generated method stub
		double tc = 1 / timelapse;
		draw(g2, timeSinceStart - 0.2, 1, maxSize - 4);
		draw(g2, timeSinceStart - 0.2, 2, maxSize - 4);
		 if(timeSinceStart <= timelapse){
			//g2.setColor(Color.black);
//			g2.setColor(new Color(255, (int)(255 - 250 * (timeSinceStart * tc/2)), 0));
			 g2.setColor(colorList.get((int)(255 - 250 * (timeSinceStart * tc/2))));
			g2.fillOval((int)xc - (int)(.5*(Math.sqrt(Math.sqrt(timeSinceStart * tc)) * maxSize)), 
						(int)yc - (int)(.5*(Math.sqrt(Math.sqrt(timeSinceStart * tc)) * maxSize)), 
						(int)(Math.sqrt(Math.sqrt(timeSinceStart * tc)) * maxSize), 
						(int)(Math.sqrt(Math.sqrt(timeSinceStart * tc)) * maxSize));
		}
		else if(timeSinceStart <= timelapse * 2){
//			g2.setColor(new Color(255, (int)(255 - 250 * (timeSinceStart * tc/2)), 0));
			g2.setColor(colorList.get((int)(255 - 250 * (timeSinceStart * tc/2))));
			g2.fillOval((int)xc - (int)(.5*(maxSize - maxSize * (timeSinceStart - timelapse))), 
						(int)yc - (int)(.5*(maxSize - maxSize * (timeSinceStart - timelapse))), 
						(int)(maxSize - maxSize * (timeSinceStart - timelapse)), 
						(int)(maxSize - maxSize * (timeSinceStart - timelapse)));
		}
		else if(timeSinceStart >= timelapse * 6){
			isDone = true;
		}
	}
	public void draw(Graphics2D g2, double time, int place, double size) {
		// TODO Auto-generated method stub
		double tc = 1 / timelapse;
//		System.out.println(place + ": " + time);
		if(size > 18){
			draw(g2, time - 0.2, place*2 + 1, size - 4);
			draw(g2, time - 0.2, place * 2 + 2, size - 4);
		}
		if(time <= 0){
			
		}
		else if(time <= timelapse){
			//g2.setColor(Color.black);
//			g2.setColor(new Color(255, (int)(255 - 255 * (time * tc)), 0));
			g2.setColor(colorList.get((int)(255 - 255 * (time * tc))));
			g2.fillOval((int)(places[0][place]) - (int)(.5*(Math.sqrt(Math.sqrt(time * tc)) * size)), 
						(int)(places[1][place]) - (int)(.5*(Math.sqrt(Math.sqrt(time * tc)) * size)), 
						(int)(Math.sqrt(Math.sqrt(time * tc)) * size), 
						(int)(Math.sqrt(Math.sqrt(time * tc)) * size));
		}
		else if(time <= timelapse * 2){
//			g2.setColor(new Color(255, (int)(255 - 255 * (time/2 * tc)), 0));
			g2.setColor(colorList.get((int)(255 - 255 * (time/2 * tc))));
			g2.fillOval((int)(places[0][place]) - (int)(.5*(size - size * (time - timelapse))), 
						(int)(places[1][place]) - (int)(.5*(size - size * (time - timelapse))), 
						(int)(size - size * (time - timelapse)), 
						(int)(size - size * (time - timelapse)));
		}
//		else if(time >= timelapse * 8){
//			isDone = true;
//		}
	}
	
	/*public boolean timeToChain(){
		timeSinceStart = (double)System.currentTimeMillis()/1000 - timeAtStart;
		System.out.println(timeSinceStart);
		if(timeSinceStart > timelapse/2 && !chained){
			chained = true;
			return true;
		}
		return false;
	}
	
	public Explosion chain(){
		if(maxSize > 14){
			return new Explosion(xc + (Math.random()*20 - 10), yc + (Math.random()*20 - 10), maxSize - 4);
		}
		else{
			return null;
		}
	}
	*/
	public boolean done(){
		return isDone;
	}

}
