package weapon;

import java.util.Random;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Random die = new Random();
		for (int i = 0;i<100;i++){
			System.out.println(die.nextInt(2)*2-1);
		}
	}

}
