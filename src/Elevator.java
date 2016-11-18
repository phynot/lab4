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
	public void run()
	{
		ElevatorEvent todo;
		while(true && !Thread.interrupted()){
			int origin;
			// idle elevator state
			if (numPassengers == 0){
				int prospectiveFloor = -1;
				while (prospectiveFloor == -1)
					prospectiveFloor = findDudes();
				// we gon get it
				moveQueue.add(createElevatorEvent(prospectiveFloor, 0));
			}
			while (!moveQueue.isEmpty()){
				todo = moveQueue.get(0);
				origin = currentFloor;
				currentFloor = todo.getDestination();
				System.out.printf("Time %d: Elevator %d [TRAVERSING Floor %d -> %d] for %s\n", 
								SimClock.getTime(), elevatorID, origin, currentFloor, (numPassengers == 0 ? "PICKUP" : "DROPOFF"));
				while (SimClock.getTime() != todo.getExpectedArrival()){
					// busy wait
					if (Thread.interrupted())
						return;
					}
				// arrived at destination
				// pickup mode
				if (numPassengers == 0)
					pickUpDudes();
				// dropoff mode
				else
					dropOffDudes(origin);
				moveQueue.remove(0);
			}

		}

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
	
	private int findDudes(){
		// scan from floor 0 -> current floor
		int prospectiveFloor;
		prospectiveFloor = manager.whoWantsUp(elevatorID);
		if (prospectiveFloor == -1)
			// scan from current floor -> floor 0
			prospectiveFloor = manager.whoWantsDown(elevatorID);
		return prospectiveFloor;
	}
	
	private void pickUpDudes(){
		int ETA_delay = 0;
		for (int i = currentFloor + 1; i < 5; ++i){
			// if there are people who want to go up
			if (manager.getNumPassengers(currentFloor, i) > 0){
				loadIntoElevator(currentFloor, i, ETA_delay);
				ETA_delay += 10;
			}
		}
		// only executes if no one wanted to go up
		if (numPassengers == 0){
			for (int i = currentFloor - 1; i >= 0 ; --i){
				// if there are people who want to go down
				if (manager.getNumPassengers(currentFloor, i) > 0){
					loadIntoElevator(currentFloor, i, ETA_delay);
					ETA_delay += 10;
				}
			}
		}
		totalLoadedPassengers += numPassengers;
		System.out.println("Time " + SimClock.getTime() + ": Elevator " + elevatorID + " [PICKED UP " + numPassengers + " dudes from Floor " + currentFloor + "]");
		manager.freeThatFloor(currentFloor);
	}
	
	private void dropOffDudes(int origin){
		System.out.println("Time " + SimClock.getTime() + ": Elevator " + elevatorID + " [DROPPED OFF " + passengerDestinations[currentFloor] + " dudes on Floor " + currentFloor + "]");
		manager.unloadAtFloor(currentFloor, origin, passengerDestinations[currentFloor]);
		numPassengers -= passengerDestinations[currentFloor];
		totalUnloadedPassengers += passengerDestinations[currentFloor];
		passengerDestinations[currentFloor] = 0;
	}
}
