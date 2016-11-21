public class BuildingFloor {
	private int[] totalDestinationRequests;
	private int[] arrivedPassengers;
	private int[] passengerRequests;
	private int approachingElevator;
	
	public BuildingFloor() {
		totalDestinationRequests = new int[5];
		arrivedPassengers = new int[5];
		passengerRequests = new int[5];
		approachingElevator = -1;
	}
	
	// Getters
	public int getTotalDestReqAtFloor(int index) {
		return totalDestinationRequests[index];
	}
	
	public int getArrivedPassengersAtIndex(int index) {
		return arrivedPassengers[index];
	}
	
	public int getNumRequestsToFloor(int index) {
		return passengerRequests[index];
	}
	
	public int getApproachingElevator() {
		return approachingElevator;
	}
	
	// Setters
	public void setTotalDestReqAtFloor(int index, int num) {
		this.totalDestinationRequests[index] = num;
	}
	
	public void incrementArrivedPassengersAtIndex(int index, int num) {
		this.arrivedPassengers[index] += num;
	}
	
	public void setNumRequestsToFloor(int index, int num) {
		this.passengerRequests[index] = num;
	}

	public void setApproachingElevator(int approachingElevator){
		this.approachingElevator = approachingElevator;
	}
	
	/* ONLY FOR EASY SIMULATION STATE PRINTING */
	public int[] getTotalDestinationRequests() {
		return totalDestinationRequests;
	}
	public int[] getArrivedPassengers() {
		return arrivedPassengers;
	}
	public int[] getPassengerRequests() {
		return passengerRequests;
	}
}
