import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BuildingManager
{
	private BuildingFloor[] floors; //represents state of all floors in the building
	private Lock floorLock;
	
	public BuildingManager(){
		System.out.println("hello");
		floors = new BuildingFloor[5];
		for (int i = 0; i < 5; ++i){
			floors[i] = new BuildingFloor();
		}
		floorLock = new ReentrantLock();
	}
	
	public synchronized int whoWantsUp(int caller){
		for (int i = 0; i < 5; ++i){
			for (int j = 4; j > i; --j){
				if (floors[i].getNumRequestsToFloor(j) > 0 && floors[i].getApproachingElevator() == -1){
					dibsOnThatFloor(i, caller);
					return i;
				}
			}
		}
		return -1;
	}
	
	public synchronized int whoWantsDown(int caller){
		for (int i = 0; i < 5; ++i){
			for (int j = 0; j < i; ++j){
				if (floors[i].getNumRequestsToFloor(j) > 0 && floors[i].getApproachingElevator() == -1){
					dibsOnThatFloor(i, caller);
					return i;
				}
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
	
	public void newPassengerArrival(int i, PassengerArrival behavior){
		floorLock.lock();
		int destination = behavior.getDestinationFloor();
		int currentNumRequests = floors[i].getTotalDestReqAtFloor(destination);
		int currentPassengerRequests = floors[i].getNumRequestsToFloor(destination);
		int incomingNumRequests = behavior.getNumPassengers();
		floors[i].setTotalDestReqAtFloor(destination, currentNumRequests + incomingNumRequests);
		floors[i].setNumRequestsToFloor(destination, currentPassengerRequests + incomingNumRequests);
		floorLock.unlock();
	}
	
	public int getNumPassengers(int floor, int dest){
		return floors[floor].getNumRequestsToFloor(dest);
	}
	
	public int pickUpGroup(int floor, int dest){
		int passengers = floors[floor].getNumRequestsToFloor(dest);
		floors[floor].setNumRequestsToFloor(dest, 0);
		return passengers;
	}
	
	public void unloadAtFloor(int dest, int origin, int numPassengers){
		floorLock.lock();
		floors[dest].incrementArrivedPassengersAtIndex(origin, numPassengers);
		int currentPassengerRequests = floors[origin].getNumRequestsToFloor(dest);
		floors[origin].setNumRequestsToFloor(origin, currentPassengerRequests - numPassengers);
		floorLock.unlock();

	}
	public BuildingFloor[] getFloors()
	{
		return floors;
	}
}