import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CyclicBarrier;

public class MainHorse {
	
	public static int numberOfHorses;
	public static int distance;
	public static int numHealthy=0;
	public static List <Horse> list;
	public static int nameCount=1;
	public static int barrierMessage=1;
	
	public static void main(String[] args) {
		while(numHealthy<2){
			list = new ArrayList<Horse>();
			numberOfHorses=checkNumHorse();
			distance=checkDistance();
			addToList();
			printHorse();
			numHealthy=countHealthy(); 	
		}
		list.removeIf(horse -> horse.getHealth().equals(false));   //filter out unhealthy horses		
		System.out.println("Filtered healthy horses. " +numHealthy+" horses can join the race");
		list.stream().forEach(horse -> System.out.println("  "+horse.getName())); //print horse who can join
		System.out.println("----------------------------------------------------");
		Runnable barrierAction = new Runnable() { 
     		public void run() { 
     			if(Horse.countdown==true&&barrierMessage==1){
					countDown();
					barrierMessage++;	
				}
				else if(Horse.endRace==true&&barrierMessage==2){
					finalRanking();
					barrierMessage++;
				}
				else {
				}
			} 
 		}; 
		CyclicBarrier barrier = new CyclicBarrier(numHealthy,barrierAction);
		list.forEach(horse -> horse.setBarrier(barrier));
		list.forEach(horse -> new Thread(horse).start());   //create and start the thread
	}
	
	public List<Horse> getList(){
    	return list;
	}

	public static void addToList(){
		for(int count=0;count<numberOfHorses;count++)
			list.add(new Horse(setHorseName(),setHorseHealth(),setHorseWarCry()));
	}

	public static String setHorseName(){
	    String nameto="HORSE"+String.valueOf(nameCount);
		nameCount++;
		return nameto;
	}
	
	public static Boolean setHorseHealth(){
		Random randomhealth = new Random();
		boolean healthCon=randomhealth.nextBoolean();
		Boolean health = Boolean.valueOf(healthCon);
		return health;
	}
	
	public static String setHorseWarCry(){
		Random ran= new Random();
		String warCry="";
		int numWarCry=ran.nextInt(5)+1;
		switch(numWarCry){
			case 1: warCry="Yahoo!";
					break;
			case 2: warCry="Yehey!";
					break;
			case 3: warCry="I'm Great!!";
					break;
			case 4: warCry="I made it.";
					break;
			case 5: warCry="Yes!";
					break;
			case 6: warCry="I did it!";
					break;
			default:warCry="Ok!";
					break;
		}
		return warCry;
	}
    
	public static int countHealthy(){
		int numHealthy=0;
		for (Horse horse : list) {
			if(horse.getHealth().equals(true))
				numHealthy++;
		}
		if(numHealthy<2){
			System.out.println("Can't continue the race. \nAt least two healthy Horse to start the race. \nReenter number of Horse. ");
			System.out.println("----------------------------------------------------");
			nameCount=1;
			numberOfHorses=0;
			distance=0;
		}
		return numHealthy;
	}
	
	public static void printHorse(){
		 System.out.println("----------------------------------------------------");
		 System.out.println("Horse Name\tHealthy?");
		 list.stream().forEach(horse -> System.out.printf("  %s\t %s\n",horse.getName(),horse.getHealth().equals(true)?"YES":"NO"));
		 System.out.println("----------------------------------------------------");	
	  }

	public static int checkNumHorse(){
		Scanner read = new Scanner(System.in);
		int min=2;
		int max=100;
		int number=0; 
		boolean correctInput=false;
		while(correctInput==false){
			try{
				System.out.print("Enter number of Horse: ");
				number=Integer.parseInt(read.next());
				if(number>=min&&number<=max){
					correctInput=true;
				}
				else
					System.out.println("Not in range");
			}catch(NumberFormatException ignore){
				System.out.println("Not a number");	
			}
		}
		return number;
	}
	
	public static int checkDistance(){
		Scanner read = new Scanner(System.in);
		int min=50;
		int max=100;
		int number=0; 
		boolean correctInput=false;
		while(correctInput==false){
			try{
				System.out.print("Enter the distance of Race: ");
				number=Integer.parseInt(read.next());
				if(number>=min&&number<=max){
					correctInput=true;
				}
				else
					System.out.println("Not in range");
			}catch(NumberFormatException ignore){
				System.out.println("Not a number");	
			}
		}
		return number;
	}
	
	public static void countDown(){
	    try {
	    	System.out.println("= = = = = = = = = = = = = = = = = =   ALL HORSES ARE IN THE GATE   = = = = = = = = = = = = = = = = = = = \n"); 
	    	Thread.sleep(1000);
	    	System.out.println("\t\t\t\t\tRace is about to begin");
	    	Thread.sleep(1000);
			System.out.print("\t\t\t\t\tREADY..."); 
			Thread.sleep(1000);
			System.out.print("GET SET..");
			Thread.sleep(1000);
			System.out.println("GO!!!!\n");
			Thread.sleep(1000);
			System.out.println("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = \n"); 
		} catch (InterruptedException e) {
			System.out.println("countdown not finished");
		}
	    
	}
	
	public static void finalRanking(){
		list.sort((Horse o1, Horse o2)->o1.getPlace()-o2.getPlace());
			System.out.println("\n\tFINAL RANKING: ");
			System.out.println("\t\tPLACE\t\t NAME");		
			list.stream().forEach(horse -> System.out.println("\t\t "+horse.getPlace()+"\t\t"+horse.getName()));		
	}
}
