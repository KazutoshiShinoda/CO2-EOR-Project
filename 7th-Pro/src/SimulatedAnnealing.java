/*
 * 焼きなまし法
 * 
 * 		変数をリストにするとなんかうまくいかないので、普通の数(double)にするべし。
 * 		※stateをリストにしてやると、bestStateがうまく引き継がれない問題が起きた。
 * 		※※常にbestState == stateになった。
 */

import java.util.Random;

public class SimulatedAnnealing {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Seventh eval = new Seventh();
		double T = 100.0;
		Random rnd = new Random();
		int startState;
		
		/*違うサイズの船を混在させる場合
		for(int i = 0; i<numOfShips; i++){
			startState[i] = rnd.nextInt(100000);
			System.out.println(i+" : "+startState[i]);
		}
		*/
		
		//船は同じサイズのみ
		startState=20000;
		int state = startState;
		double e = eval.seventh(state);
		int bestState = state;
		double bestE = e;
		int nextState;
		double nextE;
		int dif;
		for(double i = 0; i<T; i+=0.1){
			
			dif = rnd.nextInt(2)*2*1000-1000;
			
			if(state+dif < 0){
				nextState = state-dif;
			}else{
				nextState = state+dif;
			}
			
			nextE = eval.seventh(nextState);
			e = eval.seventh(state);
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
		
		state = bestState-1000;
		e = eval.seventh(state);
		
		//ステップの桁を落とす
		for(int i = 1; i<20; i++){
			state = state + 100;
			e = eval.seventh(state);
			if(bestE < e){
				bestState = state;
				bestE = e;
			}
			System.out.println("最高利益 = "+bestE+"ベストサイズ = "+bestState+", 利益 = "+e+", サイズ = "+state);
		}
		
		state = bestState-100;
		e = eval.seventh(state);
		for(int i = 1; i<20; i++){
			state = state + 10;
			e = eval.seventh(state);
			if(bestE < e){
				bestState = state;
				bestE = e;
			}
			System.out.println("最高利益 = "+bestE+"ベストサイズ = "+bestState+", 利益 = "+e+", サイズ = "+state);
		}
		
		state = bestState-10;
		e = eval.seventh(state);
		for(int i = 1; i<20; i++){
			state = state + 1;
			e = eval.seventh(state);
			if(bestE < e){
				bestState = state;
				bestE = e;
			}
			System.out.println("最高利益 = "+bestE+"ベストサイズ = "+bestState+", 利益 = "+e+", サイズ = "+state);
		}
		
		System.out.println("最高利益="+bestE+ ", サイズ = "+bestState);
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