
public class ElevatorEvent
{
	private int destination;
	private int expectedArrival;
	
	public ElevatorEvent(int destination, int expectedArrival){
		this.destination = destination;
		this.expectedArrival = expectedArrival;
	}
	
	// Getters
	public int getDestination() {
		return destination;
	}
	
	public int getExpectedArrival() {
		return expectedArrival;
	}
	
	// Setters
	public void setDestination(int destination) {
		this.destination = destination;
	}

	public void setExpectedArrival(int expectedArrival) {
		this.expectedArrival = expectedArrival;
	}
}
