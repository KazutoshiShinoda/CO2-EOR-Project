package classes;

public class Plant{
	
	public int[] isWaiting;
	
	public Plant(int numberOfShips){
		isWaiting = new int[numberOfShips];
		for(int i = 0; i<numberOfShips; i++){
			isWaiting[i]=0;
		}
		//1ならwaiting, 0なら港にいない, -1なら積込み中
		//isWaiting[index]とships[index]が対応
		//Harborクラスも同様。
	}
}