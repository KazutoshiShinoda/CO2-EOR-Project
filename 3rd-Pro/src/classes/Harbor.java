package classes;

public class Harbor{
	
	public int[] isWaiting;
	private boolean free;
	private int numOfShips;
	
	public Harbor(int numberOfShips){
		//初期状態
		numOfShips = numberOfShips;
		isWaiting = new int[numOfShips];
		free = false;
		for(int i = 0; i<numOfShips; i++){
			if(i==0){
				isWaiting[i]=-1;
			}else{
				isWaiting[i]=1;
			}
		}
		//1ならwaiting, 0なら港にいない, -1なら積込み中
		//isWaiting[index]とships[index]が対応
	}
	
	public boolean isFree(){
		free = true;
		for(int i = 0; i<numOfShips; i++){
			if(isWaiting[i]==1 || isWaiting[i]==-1){
				//一隻でも待機中だったり積込み中だったらtankはfreeではない。
				free = false;
			}
		}
		return free;
	}
}