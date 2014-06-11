package util;

import java.awt.Shape;
import java.awt.geom.AffineTransform;

public class Transform {
	private Transform(){
		
	}
	private static AffineTransform translate;
	private static AffineTransform rotate;
	public static Shape transform(Shape s, double deltax, double deltay,
			double dtheta, double xcenter, double ycenter){
		if (s == null){
			return null;
		}
		if (translate == null || rotate == null){
			translate = new AffineTransform();
			rotate = new AffineTransform();
		}
		translate.setToTranslation(deltax, deltay);
		rotate.setToRotation(dtheta, xcenter, ycenter);
		s = translate.createTransformedShape(s);
		s = rotate.createTransformedShape(s);
		return s;
	}
}
