package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

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
	
	public Explosion(double x2, double y2, double size){
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
			g2.setColor(new Color(255, (int)(255 - 250 * (timeSinceStart * tc/2)), 0));
			g2.fillOval((int)xc - (int)(.5*(Math.sqrt(Math.sqrt(timeSinceStart * tc)) * maxSize)), 
						(int)yc - (int)(.5*(Math.sqrt(Math.sqrt(timeSinceStart * tc)) * maxSize)), 
						(int)(Math.sqrt(Math.sqrt(timeSinceStart * tc)) * maxSize), 
						(int)(Math.sqrt(Math.sqrt(timeSinceStart * tc)) * maxSize));
		}
		else if(timeSinceStart <= timelapse * 2){
			g2.setColor(new Color(255, (int)(255 - 250 * (timeSinceStart * tc/2)), 0));
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
		System.out.println(place + ": " + time);
		if(size > 18){
			draw(g2, time - 0.2, place*2 + 1, size - 4);
			draw(g2, time - 0.2, place * 2 + 2, size - 4);
		}
		if(time <= 0){
			
		}
		else if(time <= timelapse){
			//g2.setColor(Color.black);
			g2.setColor(new Color(255, (int)(255 - 255 * (time * tc)), 0));
			g2.fillOval((int)(places[0][place]) - (int)(.5*(Math.sqrt(Math.sqrt(time * tc)) * size)), 
						(int)(places[1][place]) - (int)(.5*(Math.sqrt(Math.sqrt(time * tc)) * size)), 
						(int)(Math.sqrt(Math.sqrt(time * tc)) * size), 
						(int)(Math.sqrt(Math.sqrt(time * tc)) * size));
		}
		else if(time <= timelapse * 2){
			g2.setColor(new Color(255, (int)(255 - 255 * (time/2 * tc)), 0));
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
