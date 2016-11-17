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
			int ETA_delay = 0;
			// idle elevator state
			if (numPassengers == 0){
				int prospectiveFloor = -1;
				while (prospectiveFloor == -1){

					// scan from floor 0 -> current floor
					prospectiveFloor = manager.whoWantsUp(elevatorID);
					if (prospectiveFloor == -1)
						// scan from current floor -> floor 0
						prospectiveFloor = manager.whoWantsDown(elevatorID);
				}
				System.out.println("Elevator " + elevatorID + " heading to floor " + prospectiveFloor + " from floor " + currentFloor);
				// we gon get it
				moveQueue.add(createElevatorEvent(prospectiveFloor, 0));
			}
			
			if (!moveQueue.isEmpty()){
				todo = moveQueue.get(0);
				int dest = todo.getDestination();
				
				// pickup mode
				if (numPassengers == 0){
					while (SimClock.getTime() != todo.getExpectedArrival()){
						// busy wait
						//System.out.println(SimClock.getTime());
					}
					// arrived at destination
					currentFloor = dest;
					for (int i = currentFloor + 1; i < 5; ++i){
						// if there are people who want to go up
						if (manager.getNumPassengers(currentFloor, i) > 0){
							ETA_delay += 10;
							moveQueue.add(createElevatorEvent(i, ETA_delay));
							numPassengers += manager.pickUpGroup(currentFloor, i);
						}
					}
					// no one wants to go up
					if (numPassengers == 0){
						for (int i = currentFloor - 1; i >= 0 ; --i){
							// if there are people who want to go down
							if (manager.getNumPassengers(currentFloor, i) > 0){
								ETA_delay += 10;
								moveQueue.add(createElevatorEvent(i, ETA_delay));
								numPassengers += manager.pickUpGroup(currentFloor, i);
							}
						}
					}
					totalLoadedPassengers += numPassengers;
					System.out.println("Elevator " + elevatorID + " picked up " + numPassengers + " dudes at " + SimClock.getTime());
					moveQueue.remove(0);
					manager.freeThatFloor(dest);
					
				}
				/*
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
				*/
			}

		}

	}
	private ElevatorEvent createElevatorEvent(int destination, int delay){
		int ETA = SimClock.getTime() + Math.abs(currentFloor - destination) * 5 + 20;
		if (numPassengers == 0) // no unloading
			ETA -= 10;
		return new ElevatorEvent(destination, ETA + delay);
	}

}
