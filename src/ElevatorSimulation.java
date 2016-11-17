import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ElevatorSimulation
{
	private static Scanner scanner;
	private int totalSimulationTime;
	private int simulatedSecondRate;
	private ArrayList<ArrayList<PassengerArrival>> tracker;
	
	public ElevatorSimulation() throws FileNotFoundException{
		scanner = new Scanner(new File("ElevatorConfig.txt"));
		tracker = new ArrayList<ArrayList<PassengerArrival>>();
		for (int i = 0; i < 5; ++i){
			tracker.add(new ArrayList<PassengerArrival>());
		}
	}
	public void start() throws InterruptedException{
		//SimClock clock = new SimClock();
		BuildingManager manager = new BuildingManager();
		getTotalSimulationTime();
		getSimulatedSecondRate();
		getPassengerArrivals();
		
		for (int i = 0; i < 5; ++i){
			Thread t = new Thread(new Elevator(i, manager));
			t.start();
		}
		while (SimClock.getTime() <= totalSimulationTime){
			//System.out.println(SimClock.getTime());
			for (int i = 0; i < tracker.size(); ++i){
				for (int j = 0; j < tracker.get(i).size(); ++j){
					if (tracker.get(i).get(j).getExpectedTimeOfArrival() == SimClock.getTime()){
						PassengerArrival behavior = tracker.get(i).get(j);
						System.out.println("At time " + SimClock.getTime() + ", Floor " + i + " has " + behavior.getNumPassengers() + " people requesting to go to floor " + behavior.getDestinationFloor());
						manager.modifyFloorState(i,  behavior);
						behavior.setExpectedTimeOfArrival(SimClock.getTime() + behavior.getTimePeriod());
					}
				}
			}
			Thread.sleep(simulatedSecondRate);
			SimClock.tick();
		}
			
			
		/* did i read in the input correctly maybe maybe not
		System.out.println(tracker.size());
		for (int i = 0; i < tracker.size(); ++i){
			for (int j = 0; j < tracker.get(i).size(); ++j){
				PassengerArrival poop = tracker.get(i).get(j);
				System.out.println("at f" + i + "- np: " + poop.getNumPassengers() +  " df: " + poop.getDestinationFloor() + " tp: " + poop.getTimePeriod());
			}
		}*/
		scanner.close();
		
	}
	
	private void getTotalSimulationTime(){
		totalSimulationTime = Integer.parseInt(scanner.nextLine());
	}
	private void getSimulatedSecondRate(){
		simulatedSecondRate = Integer.parseInt(scanner.nextLine());
		
	}
	
	private void getPassengerArrivals(){
		int currentFloor = 0;
		String[] stringsToProcess;
		String[] infoToProcess;
		PassengerArrival passengerBehavior;
		
		while (scanner.hasNextLine()){
			stringsToProcess = scanner.nextLine().split(";");
			for (int i = 0; i < stringsToProcess.length; ++i){
				passengerBehavior = new PassengerArrival();
				infoToProcess = stringsToProcess[i].split("\\s+");
				passengerBehavior.setNumPassengers(Integer.parseInt(infoToProcess[0]));
				passengerBehavior.setDestinationFloor(Integer.parseInt(infoToProcess[1]));
				passengerBehavior.setTimePeriod(Integer.parseInt(infoToProcess[2]));
				passengerBehavior.setExpectedTimeOfArrival(Integer.parseInt(infoToProcess[2]));
				tracker.get(currentFloor).add(passengerBehavior);
				}
			++currentFloor;
		}
	}

}
