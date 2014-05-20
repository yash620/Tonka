package util;

import game.Explosion;

import java.awt.Color;
import java.util.HashMap;

public class ColorList {
	/*
	 * A HashMap is created with every single color shade that is used in the explosion
	 * The Key value is the shade of Green that is used in the Color, Red is always 255, blue is always 0
	 * This way, we don't have to create a new Color object every time we change the color
	 */
	private static HashMap<Integer, Color> colorList;
	private ColorList(){
		
	}
	
	public static Color getColor(int green){
		if (colorList == null){
			colorList = new HashMap<Integer, Color>();
			for (int i = 0;i<=255;i++){
				colorList.put(i, new Color(255,i,0));
			}
		}
		return colorList.get(green);
	}
}
