import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ElevatorSimulation
{
	private Scanner scanner;
	private int totalSimulationTime;
	private int simulatedSecondRate;
	private ArrayList<ArrayList<PassengerArrival>> tracker;
	
	public ElevatorSimulation() throws FileNotFoundException {
		scanner = new Scanner(new File("ElevatorConfig.txt"));
		tracker = new ArrayList<ArrayList<PassengerArrival>>();
		for (int i = 0; i < 5; ++i){
			tracker.add(new ArrayList<PassengerArrival>());
		}
		// Load ElevatorConfig.txt file and setup behaviors of each floor
		getTotalSimulationTime();
		getSimulatedSecondRate();
		getPassengerArrivals();
		scanner.close();
	}
	
	public void start() throws InterruptedException {
		BuildingManager manager = new BuildingManager();

		// Create and start elevator threads
		Thread[] threadPool = new Thread[5];
		Elevator[] elevators = new Elevator[5];
		for (int i = 0; i < 5; ++i) {
			elevators[i] = new Elevator(i, manager);
			threadPool[i] = new Thread(elevators[i]);
			threadPool[i].start();
		}
		
		while (SimClock.getTime() <= totalSimulationTime) {
			/* Scan through each floor's behaviors and update events appropriately
			 * PassengerArrival.expectedTimeOfArrival == time:
			 * 		Passengers are arriving, signal arrival and increment expectedTimeOfArrival
			 */
			//System.out.println(SimClock.getTime());
			PassengerArrival behavior;
			
			for (int i = 0; i < tracker.size(); ++i) {
				for (int j = 0; j < tracker.get(i).size(); ++j) {
					behavior = tracker.get(i).get(j);
					if (behavior.getExpectedTimeOfArrival() == SimClock.getTime()) {
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
		// End all threads
		for (Thread t: threadPool)
			t.interrupt();
		for (Elevator e: elevators)
			e.printElevatorStats();
		
		Thread.sleep(250);
		printBuildingState(manager);
	}
	
	private void printBuildingState(BuildingManager manager){
		BuildingFloor[] floors = manager.getFloors();
		for (int i = 0; i < floors.length; ++i){
			System.out.printf("Floor %d info: \n Total Destination Requests: %s \n Total Arrived Passengers: %s \n Current Passenger Requests: %s\n",
				i, Arrays.toString(floors[i].getTotalDestinationRequests()), Arrays.toString(floors[i].getArrivedPassengers()), Arrays.toString(floors[i].getPassengerRequests()));
		}
	}
	
	// Private functions for reading ElevatorConfig.txt
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
