package util;

import java.io.Serializable;
import java.util.ArrayList;

public class Packet implements Serializable {
	public ArrayList<Drawable> drawables;
	public Packet(ArrayList<Drawable> drawables){
		this.drawables = drawables;
	}
	public Packet(){
		this(new ArrayList<Drawable>());
	}
}
