package classes;

public class Plant{
	public int[] isWaiting;
	private boolean free;
	private int numOfShips;
	private boolean queue;
	
	public Plant(int numberOfShips){
		isWaiting = new int[numberOfShips];
		numOfShips = numberOfShips;
		for(int i = 0; i<numberOfShips; i++){
			isWaiting[i]=0;
		}
		//1ならwaiting, 0なら港にいない, -1なら積込み中
		//isWaiting[index]とships[index]が対応
		//Harborクラスも同様。
	}
	
	public boolean isFree(){
		free = true;
		for(int i = 0; i<numOfShips; i++){
			if(isWaiting[i]!=0){
				free = false;
			}
		}
		return free;
	}
	
	public boolean Queue(){
		queue = false;
		for(int i = 0; i<numOfShips; i++){
			if(isWaiting[i]==1){
				queue = true;
			}
		}
		return queue;
	}
}