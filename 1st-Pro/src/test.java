import java.util.Random;


public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double state = 0;
		double e = func(state);
		Random rnd = new Random();
		double nextState;
		double nextE;
		double bestState = state;
		double bestE = e;
		double T = 100.0;
		
		for(double i = 0.0; i<T; i += 0.1){
			nextState = state + 0.1*(rnd.nextInt(2)*2-1);	//+0.1 or -0.1
			nextE = func(nextState);
			if(probability(e,nextE,i)>=rnd.nextDouble()){
				e = nextE;
				state = nextState;
			}
			if(bestE > e){
				bestE = e;
				bestState = state;
			}
			
			System.out.println("x="+state+", f="+e);
		}
		System.out.println("optx="+bestState+", min="+bestE);
	}
	
	public static double func(double x){
		return Math.pow(x,6)+2*Math.pow(x,5)+2*Math.pow(x, 4) - Math.pow(x, 3) - 2*Math.pow(x, 2) + 1;
	}
	
	public static double probability(double e1, double e2, double i){
		if(e2 < e1){
			return 1.0;
		}else{
			return Math.exp((e1 - e2)/Math.pow(0.8, i/100.0));
		}
	}

}