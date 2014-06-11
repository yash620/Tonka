package game;

public class Performance {
	private static long start;
	private static int avg;
	private static int count;

	private Performance() {
		
	}
	
	public static void startRecord() {
		start = System.currentTimeMillis();
	}
	
	public static void stopRecord() {
		long end = System.currentTimeMillis();
		avg = ((int)(end - start) + count*avg) / (count + 1);
		count++;
		System.out.println(avg + " " + Long.toString(end - start));
	}

}
