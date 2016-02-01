package classes;

public class Plant{
	public int[] isWaiting;
	public int nextFreeTime;
	private boolean free;
	private int numOfShips;
	private boolean queue;
	private int timePlant;
	private int waitTimePlant;	//0~5
	private boolean rainy;
	private int border;
	
	public Plant(int numberOfShips, int tborder){
		isWaiting = new int[numberOfShips];
		numOfShips = numberOfShips;
		for(int i = 0; i<numberOfShips; i++){
			isWaiting[i]=0;
		}
		nextFreeTime = 0;
		//1ならwaiting, 0なら港にいない, -1なら積込み中
		//isWaiting[index]とships[index]が対応
		//Harborクラスも同様。
		border = tborder;
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
	
	// 72時間先までは天候データに従い、その後は晴れだとしてships[i].load = 0になる""時刻""を返す関数
	//weather:天候データ、amount:船の残りload、nowTime:ここに入った時のメイン関数での絶対的な時刻。
	public void setNextFT(int[] weather, double amount, int nowTime){
		timePlant = 0;		//ここに入ってからの相対的な時刻
		waitTimePlant = 0;	//0~5の値をとる。5のときのみ圧入する
		while(amount > 0){
			if(timePlant >=0 && timePlant < 6-nowTime%6){
				if(weather[0] == 1){
					rainy = true;
				}else{
					rainy = false;
				}
			}else{
				if((nowTime+timePlant)%6==0 && timePlant+nowTime%6 < 13*6){
					if(weather[(nowTime%6+timePlant)/6] == 1){
						rainy = true;
					}else{
						rainy = false;
					}
				}else if(timePlant+nowTime%6>=13*6){
					rainy = false;
				}
			}
			
			if(rainy){
				//雨が降ったら圧入停止、waitTimePlantのリセット
				waitTimePlant = 0;
				if(amount <= border){
					amount = 0;
				}
			}else{
				//ハレ晴れユカイ
				if(waitTimePlant>=5 && amount>150){
					amount -= 150;
				}else if(waitTimePlant>=5 && amount<=150){
					amount = 0;
				}
				waitTimePlant++;
			}
			if(amount == 0){
				//Injection完了
				waitTimePlant=0;
			}
			timePlant++;	//ループのカウント
		}
		nextFreeTime = nowTime+timePlant;
	}
}