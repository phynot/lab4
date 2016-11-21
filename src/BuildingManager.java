public class BuildingManager
{
	private BuildingFloor[] floors; // Represents state of all floors in the building	

	public BuildingManager(){
		System.out.println("hello");
		floors = new BuildingFloor[5];
		for (int i = 0; i < 5; ++i)
			floors[i] = new BuildingFloor();
	}
	
	// Check all floors to see if there are any passengers
	// requesting to go UP
	public synchronized int getFloorWithUpRequests(int elevatorID) {
		for (int i = 0; i < 5; ++i) {
			for (int j = 4; j > i; --j) {
				// If found, set calling elevator to approach that floor
				if (floors[i].getNumRequestsToFloor(j) > 0 && floors[i].getApproachingElevator() == -1) {
					//dibsOnThatFloor(i, caller);
					floors[i].setApproachingElevator(elevatorID);
					return i;
				}
			}
		}
		return -1;
	}
	
	// Check all floors to see if there are any passengers
	// requesting to go DOWN
	public synchronized int getFloorWithDownRequests(int elevatorID) {
		for (int i = 0; i < 5; ++i) {
			for (int j = 0; j < i; ++j) {
				// If found, set calling elevator to approach that floor
				if (floors[i].getNumRequestsToFloor(j) > 0 && floors[i].getApproachingElevator() == -1) {
					//dibsOnThatFloor(i, caller);
					floors[i].setApproachingElevator(elevatorID);
					return i;
				}
			}
		}
		return -1;
	}

	// New passengers have arrived at a floor
	// Update the floor's properties appropriately
	public synchronized void newPassengerArrival(int floor, PassengerArrival behavior) {
		int destination = behavior.getDestinationFloor();
		int currentNumRequests = floors[floor].getTotalDestReqAtFloor(destination);
		int currentPassengerRequests = floors[floor].getNumRequestsToFloor(destination);
		int incomingNumRequests = behavior.getNumPassengers();
		floors[floor].setTotalDestReqAtFloor(destination, currentNumRequests + incomingNumRequests);
		floors[floor].setNumRequestsToFloor(destination, currentPassengerRequests + incomingNumRequests);
	}
	
	public synchronized int getNumPassengers(int floor, int dest) {
		return floors[floor].getNumRequestsToFloor(dest);
	}
	
	public int pickUpGroup(int floor, int dest) {
		int passengers = floors[floor].getNumRequestsToFloor(dest);
		floors[floor].setNumRequestsToFloor(dest, 0);
		return passengers;
	}
	
	public void unloadAtFloor(int dest, int origin, int elevatorID, int numPassengers) {
		floors[dest].incrementArrivedPassengersAtIndex(elevatorID, numPassengers);
	}
	
	public synchronized void freeFloor(int floor) {
		floors[floor].setApproachingElevator(-1);
	}
	
	/* ONLY FOR EASY SIMULATION STATE PRINTING */
	public BuildingFloor[] getFloors() {
		 return floors;
	}
}