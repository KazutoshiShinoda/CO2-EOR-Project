/*
 * 焼きなまし法
 * 
 */

import java.util.Random;

public class SimulatedAnnealing {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int numOfShips = 2;
		
		Forth eval = new Forth();
		double T = 100.0;
		Random rnd = new Random();
		int[] startState = new int[numOfShips];
		/*違うサイズの船を混在させる場合
		for(int i = 0; i<numOfShips; i++){
			startState[i] = rnd.nextInt(100000);
			System.out.println(i+" : "+startState[i]);
		}
		*/
		
		//船は同じサイズのみ
		startState[0]=rnd.nextInt(100000);
		for(int i = 0; i<numOfShips; i++){
			startState[i]=startState[0];
		}
		int[] state = startState;
		double e = eval.forth(state);
		int[] bestState = state;
		double bestE = e;
		int[] nextState = new int[numOfShips];
		double nextE;
		//int[] dif = new int[numOfShips];
		int dif;
		for(double i = 0; i<T; i+=0.1){
			dif = rnd.nextInt(2)*200-100;
			for(int j = 0; j<numOfShips; j++){
				nextState[j] = state[j]+dif;
			}
			nextE = eval.forth(nextState);
			if(nextE > bestE){
				bestState = nextState;
				bestE = nextE;
			}
			if(rnd.nextDouble()<=probability(e,nextE,i/T)){
				nextState = state;
				e = nextE;
			}
		}
		
		System.out.println("最高利益="+bestE);
		for(int i = 0; i<numOfShips; i++){
			System.out.println(i+" : "+bestState[i]);
		}
	}
	
	public static double probability(double e1, double e2, double t){
		if(e1 <= e2){
			return 1;
		}else{
			return Math.exp((e2-e1)/t);
		}
	}

}
