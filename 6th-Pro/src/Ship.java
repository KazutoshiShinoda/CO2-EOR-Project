import classes.*;

//船クラス
class Ship{
	double x;	//座標(km)
	double v;	//速度(km/h)
	int Loadage;	//積載量(t)
	int load;	//輸送量(t)
	double cost;	//船代($)
	String status;
	Plant plant;
	Harbor harbor;
	
	Ship(int loadage, Plant tplant, Harbor tharbor){		//コンストラクタ
		Loadage = loadage;
		x = 0d;
		v = 0d;
		load = 0;
		status = "Wait_H";
		plant = tplant;
		harbor = tharbor;
		cost = cost();
	}

	public void setStatus(int i, Harbor tharbor, Plant tplant){
		if(v==0 && x==0){
			if(tharbor.isWaiting[i]==1){
				status = "Wait_H";
			}else if(tharbor.isWaiting[i]==-1){
				status = "Harbor";
			}
		}else if(v==0 && x==800){
			if(tplant.isWaiting[i]==1){
				status = "Wait_P";
			}else if(tplant.isWaiting[i]==-1){
				status = "Plant";
			}
		}else if(v>0.0){
			status = "Going";
		}else if(v<0.0){
			status = "Return";
		}
	}
	
	public double cost(){
		if(Loadage < 10000){
			cost = 42000000.0+9000000.0*(Loadage-3000.0)/7000.0;
		}else if(Loadage >= 10000 && Loadage < 30000){
			cost = 51000000.0+36000000.0*(Loadage-10000.0)/20000.0;
		}else if(Loadage >= 30000){
			cost = 87000000.0+36000000.0*(Loadage-30000.0)/20000.0;
		}
		
		return cost;
	}
}