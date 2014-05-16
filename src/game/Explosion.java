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
	private double timelapse = .4;
	
	public Explosion(double x2, double y2, double size){
		xc = x2;
		yc = y2;
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
		int currexpl = 0;
		double tc = 1 / timelapse;
		g2.setColor(new Color(255, (int)(255 - 250 * (timeSinceStart * tc/2)), 0));
		if(timeSinceStart <= timelapse){
			//g2.setColor(Color.black);
			g2.fillOval((int)xc - (int)(.5*(Math.sqrt(Math.sqrt(timeSinceStart * tc)) * maxSize)), 
						(int)yc - (int)(.5*(Math.sqrt(Math.sqrt(timeSinceStart * tc)) * maxSize)), 
						(int)(Math.sqrt(Math.sqrt(timeSinceStart * tc)) * maxSize), 
						(int)(Math.sqrt(Math.sqrt(timeSinceStart * tc)) * maxSize));
		}
		else if(timeSinceStart <= timelapse * 2){
			g2.fillOval((int)xc - (int)(.5*(maxSize - maxSize * (timeSinceStart - timelapse))), 
						(int)yc - (int)(.5*(maxSize - maxSize * (timeSinceStart - timelapse))), 
						(int)(maxSize - maxSize * (timeSinceStart - timelapse)), 
						(int)(maxSize - maxSize * (timeSinceStart - timelapse)));
		}
		else if(timeSinceStart >= timelapse * 2){
			isDone = true;
		}
	}
	public void draw(Graphics2D g2, double time, double xadd, double yadd) {
		// TODO Auto-generated method stub
		double tc = 1 / timelapse;
		g2.setColor(new Color(255, (int)(255 - 250 * (time * tc/2)), 0));
		if(time <= timelapse){
			//g2.setColor(Color.black);
			g2.fillOval((int)(xc + xadd) - (int)(.5*(Math.sqrt(Math.sqrt(time * tc)) * maxSize)), 
						(int)(yc + yadd) - (int)(.5*(Math.sqrt(Math.sqrt(time * tc)) * maxSize)), 
						(int)(Math.sqrt(Math.sqrt(time * tc)) * maxSize), 
						(int)(Math.sqrt(Math.sqrt(time * tc)) * maxSize));
		}
		else if(time <= timelapse * 2){
			g2.fillOval((int)(xc + xadd) - (int)(.5*(maxSize - maxSize * (time - timelapse))), 
						(int)(yc + yadd) - (int)(.5*(maxSize - maxSize * (time - timelapse))), 
						(int)(maxSize - maxSize * (time - timelapse)), 
						(int)(maxSize - maxSize * (time - timelapse)));
		}
		else if(time >= timelapse * 2){
			isDone = true;
		}
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
