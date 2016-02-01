import java.util.Queue;
import java.util.ArrayDeque;

public class test3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Queue<Integer> queue = new ArrayDeque<Integer>();
		//int b = 5;
		for(int i = 0; i< 10; i++) queue.add(i);
		while(true){
			Integer a = queue.poll();
			if(a == null) break;
			System.out.println(queue.peek());
		}
	}

}
