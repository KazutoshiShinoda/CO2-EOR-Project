package classes;

public class Harbor{
	
	public int[] isWaiting;
	
	public Harbor(int numberOfShips){
		isWaiting = new int[numberOfShips];
		for(int i = 0; i<numberOfShips; i++){
			if(i==0){
				isWaiting[i]=-1;
			}else{
				isWaiting[i]=1;
			}
		}
	}
}