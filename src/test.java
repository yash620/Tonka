import java.util.ArrayList;


public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new test().test();
	}
	
	public void test(){
		int[] hihi = {1,2,3,4};
//		ArrayList<Integer> newArr = new ArrayList<Integer>();
//		newArr.add(5);
		test2(hihi);
		System.out.println(hihi[0]);
	}
	
	public void test2(int[] arr){
		arr[0] = 0;
	}

}
