public class Fuel {
	double energy;	//1hあたりの燃費
	double e15;		//1dayあたりの15ktのときの燃費
	
	public double fuel(Ship ship){
		
		if(ship.Loadage<10000){
			e15 = 20.0+4.0*(ship.Loadage-5000.0)/5000.0;
		}else if(ship.Loadage>=10000 && ship.Loadage<30000){
			e15 = 24.0+8.0*(ship.Loadage-10000.0)/20000.0;
		}else if(ship.Loadage>=30000){
			e15 = 32.0+5.0*(ship.Loadage-30000.0)/20000.0;
		}
		
		energy = e15*Math.pow(ship.v,2)/Math.pow((15*1.852),2)/24.0;
		return energy;
	}
	
	public double DPS(Ship ship){
		if(ship.Loadage<10000){
			e15 = 20.0+4.0*(ship.Loadage-5000.0)/5000.0;
		}else if(ship.Loadage>=10000 && ship.Loadage<30000){
			e15 = 24.0+8.0*(ship.Loadage-10000.0)/20000.0;
		}else if(ship.Loadage>=30000){
			e15 = 32.0+5.0*(ship.Loadage-30000.0)/20000.0;
		}
		
		return e15/24.0*0.4;
	}
}
