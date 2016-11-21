public class PassengerArrival
{
	private int numPassengers;
	private int destinationFloor;
	private int timePeriod;
	private int expectedTimeOfArrival;
	
	// Getters
	public int getNumPassengers() {
		return numPassengers;
	}

	public int getDestinationFloor() {
		return destinationFloor;
	}
	
	public int getTimePeriod() {
		return timePeriod;
	}
	
	public int getExpectedTimeOfArrival() {
		return expectedTimeOfArrival;
	}
	
	// Setters
	public void setNumPassengers(int numPassengers) {
		this.numPassengers = numPassengers;
	}
	
	public void setDestinationFloor(int destinationFloor) {
		this.destinationFloor = destinationFloor;
	}
	
	public void setTimePeriod(int timePeriod) {
		this.timePeriod = timePeriod;
	}
	
	public void setExpectedTimeOfArrival(int expectedTimeOfArrival) {
		this.expectedTimeOfArrival = expectedTimeOfArrival;
	}
}
