import classes.*;

//船クラス
class Ship{
	double x;	//座標(km)
	double v;	//速度(km/h)
	int Loadage;	//積載量(t)
	int load;	//輸送量(t)
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
}