package util;

public class AngleMath {
	public static double adjustAngle(double angle){
		while (angle > 180){
			angle -= 360;
		}
		while (angle < -180){
			angle += 360;
		}
		return angle;
	}
	
	public static int adjustAngle(int angle){
		while (angle > 180){
			angle -= 360;
		}
		while (angle < -180){
			angle += 360;
		}
		return angle;
	}
}
