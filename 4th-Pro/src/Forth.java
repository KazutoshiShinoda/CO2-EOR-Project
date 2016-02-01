/*
 *  4th:試作版4
 *  速度:最速
 *  天候:考慮、予報なし
 *  船　:n隻
 *  船のサイズ:焼きなまし法(SimulatedAnnealing)
 *  船の出航順序:変更なし
 *  前作からの変更点:
 */

//import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
//import java.io.FileOutputStream;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
import java.io.IOException;
import classes.*;

public class Forth {
	public double forth(int[] state) {
		// TODO Auto-generated method stub
		final int numberOfShips = 2;		//船の数
		final int Limit = 10*365*24;		//10年間を時間に変換
		int[] capaOfShips = state;		//船の積載量を配列に格納
		//初期条件 javaは配列の要素一つ一つに代入するしかない。
		
		Plant plant = new Plant(numberOfShips);		//港オブジェクト
		Tank tank = new Tank();						//タンクオブジェクト
		Harbor harbor = new Harbor(numberOfShips);	//油田オブジェクト
		Ship[] ships = new Ship[numberOfShips];		//船オブジェクトの配列
		//shipsにShipクラスのオブジェクトが配列として複数格納
		for(int i = 0; i<numberOfShips; i++){
			ships[i] = new Ship(capaOfShips[i], plant, harbor);
		}
		//一発目はship[0]
		ships[0].status="Harbor";
		int timeHarbor = 0;		//ship1の港での滞在時間
		int timePlant = 0;	//ship1のプラントでの滞在時間
		double income = 0;
		double cost = 0;
		double profit = 0;
		final int oilRate = 370;	//	($/t)
		double costHarbor = 0;
		double costEnergy = 0;
		double costShips = 0;
		double costTank = 0;
		//double OPEX = 0;
		//double CAPEX = 0;
		Fuel fuel = new Fuel();
		double costDPS = 0;
		
		try{
			/*
			String filename = "3rd-Pro.csv";
			String charset = "SJIS";
			boolean append = false;
			FileOutputStream fos = new FileOutputStream(filename,append);
			OutputStreamWriter osw = new OutputStreamWriter(fos,charset);
			BufferedWriter bw = new BufferedWriter(osw);
			PrintWriter pw = new PrintWriter(bw);
			pw.print("時刻");
			for(int i = 1; i<numberOfShips+1; i++){
				pw.print(",船"+i+"の位置");
			}
			for(int i = 1; i<numberOfShips+1; i++){
				pw.print(",船"+i+"のstatus");
			}
			for(int i = 1; i<numberOfShips+1; i++){
				pw.print(",船"+i+"の輸送量");
			}
			pw.print(",タンクの貯蔵量");
			pw.println();
			*/
			//System.out.println("出力準備完了");
			
			String weatherfile = "seastate.csv";
			FileReader fr = new FileReader(weatherfile);
			BufferedReader br = new BufferedReader(fr);
			String line;
			boolean rainy = false;		//雨->true,晴->false
			
			//シミュレーション・スタート
			//処理を始めるときはstatusの更新を行う。
			//処理が終わった直後にisWaiting[i]の更新を行う。
			for(int t = 0; t<Limit; t++){
				//タンクの貯蔵状況の更新
				tank.setStock(harbor);
				
				//6時間に1回、天候情報の取得
				if(t%6 == 0){
					line = br.readLine();
					if(Integer.parseInt(line) == 1){
						//雨
						rainy = true;
					}else{
						//晴
						rainy = false;
					}
				}
				
				//CSVファイルに書き込む
				/*
				pw.print(t);
				for(int i = 0; i<numberOfShips; i++){
					pw.print(","+ships[i].x);
				}
				for(int i = 0; i<numberOfShips; i++){
					pw.print(","+ships[i].status);
				}
				for(int i = 0; i<numberOfShips; i++){
					pw.print(","+ships[i].load);
				}
				pw.print(","+tank.stock);
				pw.println();
				*/
				
				//船一隻ずつ考える
				for(int i = 0; i<numberOfShips; i++){
					//statusを変えるのは必ず時の変わり目
					ships[i].setStatus(i,harbor,plant);
					
					//---------------　港で待機　---------------
					if(ships[i].status=="Wait_H"){
						//一個前の船が港で作業中or待機中じゃなかったら作業に移る。
						//一個前の船がt=t1~t2の間に作業を終えて出港したら、t=t2に作業開始(=status変更)。
						if(harbor.isWaiting[(i+numberOfShips-1)%numberOfShips]==0){
							harbor.isWaiting[i]=-1;
						}
						
						
					//---------------　　港　　---------------
					}else if(ships[i].status=="Harbor"){
						//timeHarborが1増える間にする処理
						if(timeHarbor>=2 && ships[i].load<ships[i].Loadage){
							//積込み準備済&積込み中
							if(ships[i].load + tank.demand < ships[i].Loadage){
								ships[i].load += tank.demand;
							}else{
								ships[i].load = ships[i].Loadage;
							}
							costHarbor += 1000.0*ships[i].Loadage/5000;
						}
						timeHarbor+=1;
						
						//----- 戦略 1 (ifの条件)-----
						if(ships[i].Loadage == ships[i].load){			//満タンになったら出発
							if(timeHarbor >= 2){
								timeHarbor = 0;
							}else if(timeHarbor == 1){
								timeHarbor=0;
								ships[i].v=20*1.852;	//(km/h)
								harbor.isWaiting[i] = 0;
							}
						}
					
					//---------------　往路　---------------
					}else if(ships[i].status=="Going"){
						//船がプラントに向かう時
						if(ships[i].x+ships[i].v>800){
							//プラントに着いた
							costEnergy += fuel.fuel(ships[i])*(800-ships[i].x)/ships[i].v;
							ships[i].x = 800d;
							ships[i].v = 0d;
							//すでにプラントに他の船がいればisWaiting[i]を1に、
							//いなければ-1にする。
							if(plant.isFree()){
								plant.isWaiting[i] = -1;
							}else{
								plant.isWaiting[i] = 1;
							}
							
						}else{
							ships[i].x+=ships[i].v;
							costEnergy += fuel.fuel(ships[i]);
						}
					
					//---------------　帰路　---------------
					}else if(ships[i].status=="Return"){
						//船が港に向かう時
						if(ships[i].x+ships[i].v<0){
							costEnergy += fuel.fuel(ships[i])*ships[i].x/(-ships[i].v);
							ships[i].x=0d;
							ships[i].v=0d;
							if(harbor.isFree()){
								harbor.isWaiting[i] = -1;
							}else{
								harbor.isWaiting[i] = 1;
							}
							
						}else{
							ships[i].x+=ships[i].v;
							costEnergy += fuel.fuel(ships[i]);
						}
						
					//---------------　プラントで待機　---------------
					}else if(ships[i].status=="Wait_P"){
						if(plant.isWaiting[(i+numberOfShips-1)%numberOfShips]==0){
							plant.isWaiting[i]=-1;
						}
						costDPS += fuel.DPS(ships[i]);
					
					//---------------　プラント　---------------
					}else if(ships[i].status=="Plant"){
						if(rainy){
							//雨が降ったら圧入停止
							timePlant = 0;
							//----- 戦略 2 (早めに帰る) -----
							if(ships[i].load <= 750 && plant.Queue()){
								//雨で中断されたときに残り750を下回る かつ 行列ができてる -> 帰る
								ships[i].v=-20*1.852;
								plant.isWaiting[i]=0;
							}
						}else{
							//timeOperationが1増える間の処理
							if(timePlant>=5 && ships[i].load>=150){
								ships[i].load -= 150;
								income += 75*oilRate;
							}else if(timePlant>=5 && ships[i].load<150){
								income += ships[i].load*0.5*oilRate;
								ships[i].load = 0;
							}
							timePlant++;
							if(ships[i].load == 0){
								//Injection完了
								timePlant=0;
								ships[i].v=-20*1.852;
								plant.isWaiting[i]=0;
							}
						}
						costDPS += fuel.DPS(ships[i]);
					
					}
					//statusを変えるのは必ず時の変わり目
					ships[i].setStatus(i,harbor,plant);
				}
			}
			//シミュレーション・フィニッシュ
			
			//集計
			for(int i = 0; i<numberOfShips; i++){
				costShips += ships[i].cost;
			}
			costTank = 10000000*Math.ceil(tank.stockMax/5000.0);
			cost = costHarbor + costEnergy + costDPS +costShips/2 + costTank/4;
			profit = income - cost;
			
			//pw.close();
			br.close();
			//System.out.println("↓↓↓↓↓↓↓↓↓");
			//System.out.println(" 出力完了");
			//System.out.println();
			System.out.println("利益 = "+profit);
			//System.out.println("Plant滞在時間:"+totalTimePlant);
			//System.out.println("圧入準備時間:"+waitTimePlant);
			//System.out.println();
		}catch(IOException ex){
			//例外発生時の処理
			ex.printStackTrace();
		}
		return profit;
	}
}