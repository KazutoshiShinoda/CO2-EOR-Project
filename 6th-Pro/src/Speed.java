import classes.*;

public class Speed {
	public double speed(Ship ship, Plant plant, int t){
		if(ship.status == "Return" || ship.status == "Plant"){
			return -12.0*1.852;
		}else if(ship.status == "Going" || ship.status == "Harbor"){
			if(plant.nextFreeTime > t){
				if((800.0-ship.x)/(plant.nextFreeTime-t) < 10*1.852){
					return 10*1.852;
				}else if((800.0-ship.x)/(plant.nextFreeTime-t) > 20*1.852){
					return 20*1.852;
				}else{
					return (800.0-ship.x)/(plant.nextFreeTime-t);
				}
			}else{
				return 20*1.852;
			}
		}else{
			return 0;
		}
	}
}
