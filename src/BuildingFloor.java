
public class BuildingFloor
{
	public int[] getTotalDestinationRequests()
	{
		return totalDestinationRequests;
	}
	public void setTotalDestinationRequests(int[] totalDestinationRequests)
	{
		this.totalDestinationRequests = totalDestinationRequests;
	}
	public int[] getArrivedPassengers()
	{
		return arrivedPassengers;
	}
	public void setArrivedPassengers(int[] arrivedPassengers)
	{
		this.arrivedPassengers = arrivedPassengers;
	}
	public int[] getPassengerRequests()
	{
		return passengerRequests;
	}
	public void setPassengerRequests(int[] passengerRequests)
	{
		this.passengerRequests = passengerRequests;
	}
	private int[] totalDestinationRequests = {0, 0, 0, 0, 0};
	private int[] arrivedPassengers = {0, 0, 0, 0, 0};
	private int[] passengerRequests = {0, 0, 0, 0, 0};
	private int approachingElevator = -1;
	
	public int getTotalDestReqAtFloor(int index)
	{
		return totalDestinationRequests[index];
	}
	public void setTotalDestReqAtFloor(int index, int num)
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
	public int getNumRequestsToFloor(int index)
	{
		return passengerRequests[index];
	}
	public void setNumRequestsToFloor(int index, int num)
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
