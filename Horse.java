import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Horse implements Runnable {
	
	private String name;
	private Boolean health;
	private String warCry;
	private int distanceTravel=0;
	private final static int distanceBarnToGate=10;
	private static int distanceGateToFinishLine;
	private CyclicBarrier barrier=null;
	private static int place=1;
	private int placeHorse=1;
	private static int countThread=0;
	public static boolean countdown=false;
	public static boolean endRace=false;
	
	public Horse(String name,Boolean health, String warCry){
		this.name=name;
		this.health=health;
		this.warCry=warCry;
	}
	
	public String getName() {
		return name;
	}
			
	public Boolean getHealth() {
		return health;
	}
			
	public String getWarCry() {
		return warCry;
	}
	
	public void setBarrier(CyclicBarrier barrier) {
		this.barrier=barrier;
	}
	
	public int getPlace(){
		return placeHorse;
	}
	
	public synchronized int setPlace(){
		return place++;
	}
	
	public synchronized int getDistanceTravel(){
		return distanceTravel;
	}
	
	public synchronized int countThread(){
		return countThread++;
	}
	
	public synchronized void countdown(){
		countdown=true;	
	}
	
	public synchronized void endRace(){
		endRace=true;	
	}
		
	@Override
	public void run() {
		try {
			int left=0;
			System.out.printf("||   = = = = = = = %10s\tstarts to walk towards the gate.  time: %10s  = = = = = = = ||\n",getName(),getTime(System.currentTimeMillis()));
			barrier.await();
			boolean pass=false;
			while(countThread<MainHorse.numHealthy){
				int walk=walk();
				distanceTravel +=walk;
				left=distanceBarnToGate-distanceTravel;
				if(distanceTravel < distanceBarnToGate){
					System.out.printf("||%10s ||  hop:%3d meters  ||  distance:%3d meters  ||  left:%3d meters || time: %10s\n",getName(),walk,distanceTravel,left,getTime(System.currentTimeMillis())+" ||");	 
				}
				else{
					walk=walk-(distanceTravel-distanceBarnToGate);
					distanceTravel=10;
					left=0;
					if(pass==false){
						pass=true;
						System.out.printf("||%10s ||  hop:%3d meters  ||  distance:%3d meters  ||  left:%3d meters || time: %10s\n",getName(),walk,distanceTravel,left,getTime(System.currentTimeMillis())+" ||");	
						System.out.println("\t\t\t\t\t"+getName()+" arrived at the gate");
						countThread();
					}
				}
			barrier.await();
			}
			barrier.await();
			countdown();
			barrier.await();
			System.out.printf("||    = = = = = = = = = = = %10s starts running\tTime: %10s     = = = = = = = = = = = ||\n",getName(),getTime(System.currentTimeMillis()));
			barrier.await(); 
			countThread=0;
			distanceTravel = 0;
			pass=false;
			int hop=0;
			boolean boostHorse=false;
			distanceGateToFinishLine=MainHorse.distance;
			while(countThread<MainHorse.numHealthy){ 
					Boost boost = new Boost(distanceTravel,distanceGateToFinishLine);
					if(boostHorse==false){
						boostHorse=boost.travelCompare();
						if(boostHorse==true){
							hop = hopBoost();
						}
						else
							hop = hop();
					}
					else
						hop = hopBoost();
					distanceTravel += hop;
					left=distanceGateToFinishLine -distanceTravel;
					if(distanceTravel < distanceGateToFinishLine){
						System.out.printf("||%10s ||  hop:%3d meters  ||  distance:%3d meters  ||  left:%3d meters ||  time: %10s || %10s \n",getName(),hop,distanceTravel,left,getTime(System.currentTimeMillis()),boostHorse==true? "received Boost":"" );		
					}	
					else{
						hop=hop-(distanceTravel-distanceGateToFinishLine);
						distanceTravel = distanceGateToFinishLine;
						left=0;
						if(pass==false){
							System.out.printf("||%10s ||  hop:%3d meters  ||  distance:%3d meters  ||  left:%3d meters ||  time: %10s || %10s \n",getName(),hop,distanceTravel,left,getTime(System.currentTimeMillis()),boostHorse==true? "received Boost":"" );
							placeHorse=setPlace();
							System.out.println("\t\t\t\t"+getName()+" finished and says "+getWarCry());
							pass=true;
							countThread();
						}
					}
			barrier.await();
			}
		barrier.await();
		endRace();	
		barrier.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			System.out.println("race not finish!!!!");
		}
	}
	
	private int walk(){
		Random ranWalk= new Random();
		int upperBound=4;
		int lowerBound=1;
		return ranWalk.nextInt(upperBound - lowerBound)+lowerBound;
	}
	
	private int hop(){
		Random ran= new Random();
		int upperBound=10;
		int lowerBound=1;
		return ran.nextInt(upperBound - lowerBound)+lowerBound;
	}
	
	private int hopBoost(){
		Random ran= new Random();
		int upperBound=20;
		int lowerBound=1;
		return ran.nextInt(upperBound - lowerBound)+lowerBound;
	}
	
	public static String getTime(long time){
	    long millis=time%1000;
        long totalSeconds=time/1000;
        int second=(int)(totalSeconds%60);
        long totalMinutes=totalSeconds/60;
        int minute=(int)(totalMinutes%60);
        long totalHours=totalMinutes/60;
        int hour=(int)(totalHours%24)+8; 
	    String timeNow= String.format("%02d:%02d:%02d.%03d  %s", hour > 12 ? hour-12:hour, minute, second,millis,hour > 11 ? "PM":"AM");
	    return timeNow;
   }
	
}
