import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
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
		scanner.close();
		
		// Setup the simulation
		Thread[] threadPool = new Thread[5];
		for (int i = 0; i < 5; ++i){
			threadPool[i] = new Thread(new Elevator(i, manager));
			threadPool[i].start();
		}
		while (SimClock.getTime() <= totalSimulationTime){
			//System.out.println(SimClock.getTime());
			for (int i = 0; i < tracker.size(); ++i){
				for (int j = 0; j < tracker.get(i).size(); ++j){
					if (tracker.get(i).get(j).getExpectedTimeOfArrival() == SimClock.getTime()){
						PassengerArrival behavior = tracker.get(i).get(j);
						System.out.println("Time " + SimClock.getTime() + ": " + behavior.getNumPassengers() + " people [REQUEST Floor " + i + " -> Floor "  + behavior.getDestinationFloor() + "]");
						manager.newPassengerArrival(i,  behavior);
						behavior.setExpectedTimeOfArrival(SimClock.getTime() + behavior.getTimePeriod());
					}
				}
			}
			Thread.sleep(simulatedSecondRate);
			SimClock.tick();
		}
		System.out.println("bye");
		for (Thread t: threadPool)
			t.interrupt();
		Thread.sleep(250);
		printBuildingState(manager);
			
		/* did i read in the input correctly maybe maybe not
		System.out.println(tracker.size());
		for (int i = 0; i < tracker.size(); ++i){
			for (int j = 0; j < tracker.get(i).size(); ++j){
				PassengerArrival poop = tracker.get(i).get(j);
				System.out.println("at f" + i + "- np: " + poop.getNumPassengers() +  " df: " + poop.getDestinationFloor() + " tp: " + poop.getTimePeriod());
			}
		}*/

		
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
	
	private void printBuildingState(BuildingManager manager){
		BuildingFloor[] floors = manager.getFloors();
		for (int i = 0; i < floors.length; ++i){
			System.out.printf("Floor %d info: \n Total Destination Requests: %s \n Total Arrived Passengers: %s \n Current Passenger Requests: %s\n",
				i, Arrays.toString(floors[i].getTotalDestinationRequests()), Arrays.toString(floors[i].getArrivedPassengers()), Arrays.toString(floors[i].getPassengerRequests()));
		}
		
	}
}
