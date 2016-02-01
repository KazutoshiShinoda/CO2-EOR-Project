/*
 * 焼きなまし法2:プラントからの戦略的撤退のボーダーを決める。
 * 
 */

import java.util.Random;

public class SimulatedAnnealing2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Fifth2 eval = new Fifth2();
		double T = 100.0;
		Random rnd = new Random();
		int startState = 1000;
		int state = startState;
		double e = eval.fifth2(state);
		int nextState;
		double nextE;
		int bestState = state;
		double bestE = e;
		int dif;
		
		for(double i = 0; i<T; i+=0.1){
			
			dif = rnd.nextInt(2)*2*100-100;
			
			if(state+dif < 0){
				nextState = state-dif;
			}else{
				nextState = state+dif;
			}
			
			nextE = eval.fifth2(nextState);
			e = eval.fifth2(state);
			if(rnd.nextDouble()<=probability(e,nextE,i/T)){
				state = nextState;
				e = nextE;
			}
			
			if(bestE < e){
				bestState = state;
				bestE = e;
			}
			System.out.println("最高利益 = "+bestE+"ベストサイズ = "+bestState+", 利益 = "+e+", サイズ = "+state);
		}
		
		state = bestState-100;
		e = eval.fifth2(state);
		
		//ステップの桁を落とす
		for(int i = 1; i<20; i++){
			state = state + 10;
			e = eval.fifth2(state);
			if(bestE < e){
				bestState = state;
				bestE = e;
			}
			System.out.println("最高利益 = "+bestE+"ベストサイズ = "+bestState+", 利益 = "+e+", サイズ = "+state);
		}
		System.out.println("最高利益 = "+bestE+", ベストサイズ = "+bestState);
	}
	
	public static double probability(double e1, double e2, double t){
		double arufa = 0.8;
		if(e1 <= e2){
			return 1.0;
		}else{
			return Math.exp((e2-e1)/100000000/Math.pow(arufa,t));
		}
	}
}