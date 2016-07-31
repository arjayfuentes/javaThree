import java.util.List;

public class Boost {

	private int distanceGateToFinishLine;
	private int distanceTravel;
	public static List <Horse> list;
	
	public Boost(int distanceTravel, int distanceGateToFinishLine){
		this.distanceTravel=distanceTravel;
		this.distanceGateToFinishLine=distanceGateToFinishLine;
	}
	
	public boolean travelCompare(){
		list= new MainHorse().getList();
		int sumTravel=0;
		boolean boostHorse=false;
		for(int i=0;i<list.size();i++)
			sumTravel+=list.get(i).getDistanceTravel();
		int otherSumTravel=sumTravel-distanceTravel;
		int averageOtherSumTravel=otherSumTravel/(list.size()-1);
		int distanceLeftOther=distanceGateToFinishLine-averageOtherSumTravel;
		int distanceLeft=distanceGateToFinishLine-distanceTravel;
		if(distanceLeftOther<=10 && distanceLeft>=20){
			boostHorse=true;
		}
		return boostHorse;
	}
	
}
