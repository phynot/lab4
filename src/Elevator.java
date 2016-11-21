import java.util.ArrayList;

public class Elevator implements Runnable
{
	private int elevatorID;
	private int currentFloor;
	private int numPassengers;
	private int totalLoadedPassengers;
	private int totalUnloadedPassengers;
	private ArrayList<ElevatorEvent> moveQueue;
	private int[] passengerDestinations;
	private BuildingManager manager;
	
	Elevator(int elevatorID, BuildingManager manager){
		this.elevatorID = elevatorID;
		this.manager = manager;
		
		currentFloor = 0;
		numPassengers = 0;
		totalLoadedPassengers = 0;
		totalUnloadedPassengers = 0;
		moveQueue = new ArrayList<ElevatorEvent>();
		passengerDestinations = new int[5];
	}
	
	public void run() {
		ElevatorEvent todo;
		int origin;
		int prospectiveFloor;
		
		while(!Thread.currentThread().isInterrupted()){
			origin = currentFloor;
			// IDLE ELEVATOR STATE
			// Continuously loops checking with the buildingManager
			// to see if any floor has passengers available for pickup
			if (numPassengers == 0) {
				prospectiveFloor = -1;
				while (prospectiveFloor == -1)
					prospectiveFloor = checkForPassengerRequests();
				
				moveQueue.add(createElevatorEvent(prospectiveFloor, 0));
			}
			// ACTIVE ELEVATOR STATE
			while (!moveQueue.isEmpty()){
				todo = moveQueue.get(0);
				currentFloor = todo.getDestination();
				System.out.printf("Time %d: Elevator %d [TRAVERSING Floor %d -> %d] for %s\n", 
					SimClock.getTime(), elevatorID, origin, currentFloor, (numPassengers == 0 ? "PICKUP" : "DROPOFF"));
				while (SimClock.getTime() != todo.getExpectedArrival()){
					// Busy wait to simulate elevator traveling
					if (Thread.interrupted()) {
						System.out.printf("Elevator %d stats: \n Total Loaded: %d \n Total Unloaded: %d\n", elevatorID, totalLoadedPassengers, totalUnloadedPassengers);
						return;
					}
				}
				// Arrived at destination
				// If elevator has no passengers, it has just arrived
				// for pickup
				if (numPassengers == 0){
					origin = currentFloor;
					pickUpPassengers();
				}
				// Dropoff mode
				else
					dropOffPassengers(origin, elevatorID);
				moveQueue.remove(0);
			}
		}
		System.out.println("Elevator " + elevatorID + " interrupted");
	}
	
	private ElevatorEvent createElevatorEvent(int destination, int delay){
		int ETA = SimClock.getTime() + Math.abs(currentFloor - destination) * 5 + 10;
		return new ElevatorEvent(destination, ETA + delay);
	}
	
	private void loadIntoElevator(int currFloor, int dest, int ETA_delay){
		moveQueue.add(createElevatorEvent(dest, ETA_delay));
		passengerDestinations[dest] += manager.getNumPassengers(currFloor, dest);
		numPassengers += manager.pickUpGroup(currFloor, dest);
	}
	
	private int checkForPassengerRequests(){
		// Scan floors for passenger requests
		int prospectiveFloor;
		prospectiveFloor = manager.getFloorWithUpRequests(elevatorID);
		if (prospectiveFloor == -1)
			prospectiveFloor = manager.getFloorWithDownRequests(elevatorID);
		return prospectiveFloor;
	}
	
	private void pickUpPassengers(){
		int ETA_delay = 0;
		// First checks if there are people who want to go up
		for (int i = currentFloor + 1; i < 5; ++i){
			if (manager.getNumPassengers(currentFloor, i) > 0){
				loadIntoElevator(currentFloor, i, ETA_delay);
				ETA_delay += 10;
			}
		}
		// Only executes if no one wanted to go up
		if (numPassengers == 0){
			for (int i = currentFloor - 1; i >= 0 ; --i){
				// If there are people who want to go down
				if (manager.getNumPassengers(currentFloor, i) > 0){
					loadIntoElevator(currentFloor, i, ETA_delay);
					ETA_delay += 10;
				}
			}
		}
		totalLoadedPassengers += numPassengers;
		System.out.println("Time " + SimClock.getTime() + ": Elevator " + elevatorID + " [PICKED UP " + numPassengers + " dudes from Floor " + currentFloor + "]");
		manager.freeFloor(currentFloor);
	}
	
	private void dropOffPassengers(int origin, int fromElevator){
		System.out.println("Time " + SimClock.getTime() + ": Elevator " + elevatorID + " [DROPPED OFF " + passengerDestinations[currentFloor] + " dudes on Floor " + currentFloor + "]");
		manager.unloadAtFloor(currentFloor, origin, fromElevator, passengerDestinations[currentFloor]);
		numPassengers -= passengerDestinations[currentFloor];
		totalUnloadedPassengers += passengerDestinations[currentFloor];
		passengerDestinations[currentFloor] = 0;
	}
}
