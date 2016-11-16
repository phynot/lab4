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
			if (!moveQueue.isEmpty()){
				todo = moveQueue.remove(0);
				while (SimClock.getTime() != todo.getExpectedArrival()){
					//busy wait
				}
				// do stuff now 
			}
			if (numPassengers == 0){
				int prospectiveFloor = manager.whoWantsUp();
				if (prospectiveFloor == -1)
					prospectiveFloor = manager.whoWantsDown();
				if (prospectiveFloor != -1)
					manager.dibsOnThatFloor(prospectiveFloor, elevatorID);
				moveQueue.add(createElevatorEvent(prospectiveFloor));
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
