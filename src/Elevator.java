import java.util.ArrayList;

public class Elevator implements Runnable
{
	private int elevatorID;
	private int currentfloor;
	private int numPassengers;
	private int totalLoadedPassengers;
	private int totalUnloadedPassengers;
	private ArrayList<ElevatorEvent> moveQueue;
	private int[] passengerDestinations;
	private BuildingManager manager;
	
	Elevator(int elevatorID, BuildingManager manager){
		this.elevatorID = elevatorID;
		this.manager = manager;
		
	}
	public void run()
	{
		// TODO Auto-generated method stub

	}

}
