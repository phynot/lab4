
public class SimClock
{
	private static volatile int simulatedTime = 0;
	
	//public SimClock(){
	//	simulatedTime = 0;
	//}
	
	public static void tick(){
		++simulatedTime;
	}
	
	public static int getTime(){
		return simulatedTime;
	}
}
