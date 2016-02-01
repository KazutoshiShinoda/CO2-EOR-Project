/*
 *  1st:試作版
 *  速度:常に最速
 *  天候:常に良好
 *  船　:一隻
 *  船のサイズ:500と150の公倍数が好ましい。
 */
public class first {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Ship ship1 = new Ship(3000);
		System.out.println("Ship1's status is "+ship1.status);
		int Limit = 10*24;		//10年間を時間に変換
		int timeHarbor = 0;		//ship1の港での滞在時間
		int timeOperation = 0;	//ship1のプラントでの滞在時間
		
		for(int t = 0; t<=Limit; t++){
			System.out.print("T="+t+", ");
			System.out.print("Status='"+ship1.status+"', ");
			System.out.print("x="+ship1.x+"(km), ");
			System.out.print("v="+ship1.v+"(km/h), ");
			System.out.print("load="+ship1.load+"(t)");
			System.out.print("\n");		

			if(ship1.status=="Harbor"){
				//timeHarborが1増える間にする処理
				if(timeHarbor>=2 && ship1.load<ship1.Loadage){
					ship1.load += 500;			//生産は考慮せず。
				}
				//船が港にいる時
				timeHarbor+=1;
				if(timeHarbor>=2+ship1.Loadage/500+1){	//二酸化炭素のタンク貯蔵量に依存。あとで変更する。
					timeHarbor=0;
					ship1.v=20*1.852;	//(km/h)
					ship1.x+=ship1.v;	//最初の一歩
					ship1.setStatus();
				}
			}else if(ship1.status=="Going"){
				//船がプラントに向かう時
				if(ship1.x+ship1.v>800){
					//プラントに着いた
					ship1.x = 800d;
					ship1.v = 0d;
					ship1.setStatus();
				}else{
					ship1.x+=ship1.v;
				}
			}else if(ship1.status=="Return"){
				//船が港か向かう時
				if(ship1.x+ship1.v<0){
					ship1.x=0d;
					ship1.v=0d;
					ship1.setStatus();
				}else{
					ship1.x+=ship1.v;
				}
			}else if(ship1.status=="Operation"){
				//timeOperationが1増える間の処理
				if(timeOperation>=5 && ship1.load>0){
					ship1.load -= 150;
				}
				
				timeOperation++;
				//処理が終わった
				if(timeOperation>=5+ship1.Loadage/150.0){
					timeOperation=0;
					ship1.v=-20*1.852;
					ship1.x+=ship1.v;
					ship1.setStatus();
				}
			}
		}
	}
}



class Ship{
	double x;	//座標(km)
	double v;	//速度(km/h)
	int Loadage;	//積載量(t)
	int load;	//輸送量(t)
	String status;
	
	Ship(int loadage){		//コンストラクタ
		Loadage = loadage;
		x = 0d;
		v = 0d;
		load = 0;
		status = "Harbor";
	}

	public void setStatus(){
		if(x==0){
			status = "Harbor";
		}else if(x==800){
			status = "Operation";
		}else if(0.0<x && x<800.0 && v>0.0){
			status = "Going";
		}else if(0.0<x && x<800.0 && v<0.0){
			status = "Return";
		}
	}
}