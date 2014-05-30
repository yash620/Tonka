package util;

import java.io.Serializable;

public class Timer implements Serializable {
	public enum Action{ AMMO, FIRE, SPREAD }
	private final int delay;
	private int time;
	private Action action;
	public Timer(int delay, Action action){
		this.delay = delay;
		time = 0;
		this.action = action;
	}
	public Action tick(){
		time++;
		if (time > delay){
			return action;
		}
		return null;
	}
}
