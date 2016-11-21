import java.io.FileNotFoundException;

public class Lab4 {
	public static void main(String[] args)
	{
		try {
			ElevatorSimulation sim = new ElevatorSimulation();
			sim.start();
		} catch (FileNotFoundException e) {
			System.out.println("ElevatorConfig.txt file not found.");
		} catch (InterruptedException e) {
			System.out.println("Simulation interrupted.");
		}
	}
}
