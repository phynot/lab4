
public class BuildingFloor
{
	private int[] totalDestinationRequests = new int[5];
	private int[] arrivedPassengers = new int[5];
	private int[] passengerRequests = new int[5];
	private int approachingElevator;
	
	public int getTotalDestinationRequestsAtIndex(int index)
	{
		return totalDestinationRequests[index];
	}
	public void setTotalDestinationRequestsAtIndex(int index, int num)
	{
		this.totalDestinationRequests[index] = num;
	}
	public int getArrivedPassengersAtIndex(int index)
	{
		return arrivedPassengers[index];
	}
	public void incrementArrivedPassengersAtIndex(int index, int num)
	{
		this.arrivedPassengers[index] += num;
	}
	public int getPassengerRequestsAtIndex(int index)
	{
		return passengerRequests[index];
	}
	public void setPassengerRequestsAtIndex(int index, int num)
	{
		this.passengerRequests[index] = num;
	}
	public int getApproachingElevator()
	{
		return approachingElevator;
	}
	public void setApproachingElevator(int approachingElevator)
	{
		this.approachingElevator = approachingElevator;
	}


}
