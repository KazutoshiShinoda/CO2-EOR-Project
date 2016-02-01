/*
 *  2nd:試作版2
 *  速度:常に最速
 *  天候:常に良好
 *  船　:n隻
 *  船のサイズ:500と150の公倍数が好ましい。
 *  船の出航順序:一定
 */

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.IOException;
import classes.*;

public class Second {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int numberOfShips = 3;		//船の数
		int[] capaOfShips = new int[numberOfShips];		//船の積載量を配列に格納
		//初期条件 javaは配列の要素一つ一つに代入するしかない。
		capaOfShips[0]=3000;
		capaOfShips[1]=3000;
		capaOfShips[2]=3000;
		
		Plant plant = new Plant(numberOfShips);		//港オブジェクト
		Harbor harbor = new Harbor(numberOfShips);	//油田オブジェクト
		Ship[] ships = new Ship[numberOfShips];		//船オブジェクトの配列
		//shipsにShipクラスのオブジェクトが配列として複数格納
		for(int i = 0; i<numberOfShips; i++){
			ships[i] = new Ship(capaOfShips[i], plant, harbor);
		}
		//一発目はship[0]
		ships[0].status="Harbor";
		
		int Limit = 10*24;		//10日間を時間に変換
		int timeHarbor = 0;		//ship1の港での滞在時間
		int timePlant = 0;	//ship1のプラントでの滞在時間
		try{
			String filename = "2nd-Pro.csv";
			String charset = "SJIS";
			boolean append = false;
			FileOutputStream fos = new FileOutputStream(filename,append);
			OutputStreamWriter osw = new OutputStreamWriter(fos,charset);
			BufferedWriter bw = new BufferedWriter(osw);
			PrintWriter pw = new PrintWriter(bw);
			pw.print("時刻");
			for(int i = 1; i<numberOfShips+1; i++){
				pw.print(",船"+i+",船"+i+"のstatus");
			}
			pw.println();
			System.out.println("出力準備完了");
			
			//シミュレーション・スタート
			//処理を始めるときはstatusの更新、inProgressの変更を行う。
			//処理が終わった直後にisWaiting[i]の更新を行う。
			for(int t = 0; t<=Limit; t++){
				//CSVファイルに書き込む
				pw.print(t);
				for(int i = 0; i<numberOfShips; i++){
					pw.print(","+ships[i].x+","+ships[i].status);
				}
				pw.println();
				
				//船一隻ずつ考える
				for(int i = 0; i<numberOfShips; i++){
					//statusを変えるのは必ず時の変わり目
					ships[i].setStatus(i,harbor,plant);
					
					if(ships[i].status=="Wait_H"){
						//一個前の船が港で作業中or待機中じゃなかったら作業に移る。
						//一個前の船がt=t1~t2の間に作業を終えて出港したら、t=t2に作業開始(=status変更)。
						if(harbor.isWaiting[(i+numberOfShips-1)%numberOfShips]==0){
							harbor.isWaiting[i]=-1;
						}
						
					}else if(ships[i].status=="Harbor"){
						//timeHarborが1増える間にする処理
						if(timeHarbor>=2 && ships[i].load<ships[i].Loadage){
							ships[i].load += 500;			//生産は考慮せず。
						}
						timeHarbor+=1;
						if(timeHarbor >= 2+ships[i].Loadage/500+1){	//二酸化炭素のタンク貯蔵量に依存。あとで変更する。
							//Load完了
							timeHarbor=0;
							ships[i].v=20*1.852;	//(km/h)
							harbor.isWaiting[i] = 0;
						}
						
					}else if(ships[i].status=="Going"){
						//船がプラントに向かう時
						if(ships[i].x+ships[i].v>800){
							//プラントに着いた
							ships[i].x = 800d;
							ships[i].v = 0d;
							//すでにプラントに他の船がいればisWaiting[i]を1に、
							//いなければ-1にする。
							plant.isWaiting[i]=-1;
							for(int j = 0; j<numberOfShips; j++){
								if(plant.isWaiting[j]!=0){
									plant.isWaiting[i]=1;
									break;
								}
							}
						}else{
							ships[i].x+=ships[i].v;
						}
						
					}else if(ships[i].status=="Return"){
						//船が港に向かう時
						if(ships[i].x+ships[i].v<0){
							ships[i].x=0d;
							ships[i].v=0d;
							harbor.isWaiting[i]=-1;
							for(int j = 0; j<numberOfShips; j++){
								if(harbor.isWaiting[j]!=0){
									harbor.isWaiting[i]=1;
									break;
								}
							}
						}else{
							ships[i].x+=ships[i].v;
						}
						
					}else if(ships[i].status=="Wait_P"){
						if(plant.isWaiting[(i+numberOfShips-1)%numberOfShips]==0){
							plant.isWaiting[i]=-1;
						}
						
					}else if(ships[i].status=="Plant"){
						//timeOperationが1増える間の処理
						if(timePlant>=5 && ships[i].load>0){
							ships[i].load -= 150;
						}
						timePlant++;
						if(timePlant>=5+ships[i].Loadage/150.0){
							//Injection完了
							timePlant=0;
							ships[i].v=-20*1.852;
							plant.isWaiting[i]=0;
						}
					}
					//statusを変えるのは必ず時の変わり目
					ships[i].setStatus(i,harbor,plant);
				}
			}
			//シミュレーション・フィニッシュ
			pw.close();
			System.out.println("↓↓↓↓↓↓↓↓↓");
			System.out.println(" 出力完了");
		}catch(IOException ex){
			//例外発生時の処理
			ex.printStackTrace();
		}
	}
}