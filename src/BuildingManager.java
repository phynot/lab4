import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BuildingManager
{
	private BuildingFloor[] floors; //represents state of all floors in the building
	private Lock floorLock;
	
	public BuildingManager(){
		floors = new BuildingFloor[5];
		floorLock = new ReentrantLock();
	}
	
	public int whoWantsUp(){
		for (int i = 0; i < 5; ++i){
			for (int j = 4; j > i; --j){
				if (floors[i].getPassengerRequestsAtIndex(j) > 0 && floors[i].getApproachingElevator() != -1)
					return i;
			}
		}
		return -1;
	}
	
	public int whoWantsDown(){
		for (int i = 0; i < 5; ++i){
			for (int j = 0; j < i; ++j){
				if (floors[i].getPassengerRequestsAtIndex(j) > 0 && floors[i].getApproachingElevator() != -1)
					return i;
			}
		}
		return -1;
	}
	
	public void dibsOnThatFloor(int i, int elevatorID){
		floorLock.lock();
		floors[i].setApproachingElevator(elevatorID);
		floorLock.unlock();
	}
	
	public void freeThatFloor(int i){
		floorLock.lock();
		floors[i].setApproachingElevator(-1);
		floorLock.unlock();
	}
	
	public void modifyFloorState(int i, PassengerArrival behavior){
		floorLock.lock();
		int destination = behavior.getDestinationFloor();
		int currentNumRequests = floors[i].getTotalDestinationRequestsAtIndex(destination);
		int currentPassengerRequests = floors[i].getPassengerRequestsAtIndex(destination);
		int incomingNumRequests = behavior.getNumPassengers();
		floors[i].setTotalDestinationRequestsAtIndex(destination, currentNumRequests + incomingNumRequests);
		floors[i].setPassengerRequestsAtIndex(destination, currentPassengerRequests + incomingNumRequests);
		floorLock.unlock();
	}
	
	public void unloadAtFloor(int dest, int origin, int numPassengers){
		floorLock.lock();
		floors[dest].incrementArrivedPassengersAtIndex(origin, numPassengers);
		int currentPassengerRequests = floors[origin].getPassengerRequestsAtIndex(dest);
		floors[origin].setPassengerRequestsAtIndex(origin, currentPassengerRequests - numPassengers);
		floorLock.unlock();

	}
	public BuildingFloor[] getFloors()
	{
		return floors;
	}
	public void setFloors(BuildingFloor[] floors)
	{
		this.floors = floors;
	}
}
//		floors[i].incrementArrivedPassengersAtIndex(i, behavior.getNumPassengers());
