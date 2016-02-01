import classes.*;

public class Tank {
	double stock;
	double stockMax;
	double supply;	//タンクに注がれるCO2(t/h)
	double demand;	//船に積み込むCO2(t/h)
	
	Tank(){
		stock = 0;
		stockMax = 5000*2;
		supply = 1200000.0/365.0/24.0;
		demand = supply;
	}
	
	public void setStock(Harbor tharbor){
		//一時間に一回実行される。ここで貯蔵量の更新を行う。
		if(stock+supply<stockMax){
			stock += supply;
		}else{
			stock = stockMax;
		}
		if(tharbor.isFree()){
			demand = 0;
		}else{
			if(stock >= 500){
				//船に最高速度でCO2を渡せる。
				stock -= 500;
				demand = 500;
			}else{
				//船に全部持ってかれる。
				demand = stock;
				stock = 0;
			}
			
			
		}
		
		if(stock > stockMax){
			//Max値の更新
			stockMax = stock;
		}
	}
}