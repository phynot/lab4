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
			if (numPassengers == 0){
				int prospectiveFloor = -1;
				while (prospectiveFloor == -1){
					// scan from floor 0 -> current floor
					prospectiveFloor = manager.whoWantsUp();
					if (prospectiveFloor == -1)
						// scan from current floor -> floor 0
						prospectiveFloor = manager.whoWantsDown();
				}
				
				// we gon pick those peeps up
				manager.dibsOnThatFloor(prospectiveFloor, elevatorID);
				moveQueue.add(createElevatorEvent(prospectiveFloor));
			}
			
			if (!moveQueue.isEmpty()){
				todo = moveQueue.get(0);
				int ETA_delay = 0;
				int dest = todo.getDestination();
				
				// *** this is probably wrong ***
				// going up
				for (int i = currentFloor; i < dest; ++i){
					if (passengerDestinations[i] > 0){
						totalUnloadedPassengers += passengerDestinations[i];
						numPassengers -= passengerDestinations[i];
						manager.unloadAtFloor(i, currentFloor, passengerDestinations[i]);
						ETA_delay += 10;
					}
				}
				
				// going down
				for (int i = currentFloor; i > dest; --i){
					if (passengerDestinations[i] > 0){
						totalUnloadedPassengers += passengerDestinations[i];
						numPassengers -= passengerDestinations[i];
						manager.unloadAtFloor(i, currentFloor, passengerDestinations[i]);
						ETA_delay += 10;
					}
				}
				
				todo.setExpectedArrival(todo.getExpectedArrival() + ETA_delay);
				while (SimClock.getTime() != todo.getExpectedArrival()){
					//busy wait
				}
				// do stuff now 
				manager.unloadAtFloor(dest, currentFloor, numPassengers);
				manager.freeThatFloor(dest);

			}

		}

	}
	private ElevatorEvent createElevatorEvent(int destination){
		int ETA = SimClock.getTime() + Math.abs(currentFloor - destination) * 5 + 20;
		if (numPassengers == 0) // no unloading
			ETA -= 10;
		return new ElevatorEvent(destination, ETA);
	}

}
