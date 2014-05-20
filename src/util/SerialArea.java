package util;

import java.awt.Shape;
import java.awt.geom.Area;
import java.io.Serializable;

public class SerialArea extends Area implements Serializable {

	public SerialArea(Shape s) {
		super(s);
	}

}
