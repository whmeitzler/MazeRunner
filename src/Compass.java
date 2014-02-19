public class Compass{
	public final int NORTH = 0, EAST = 1, SOUTH = 2, WEST = 3;
	private static final int[] degrees  = {0,90,180,270};
	private int heading;
	
	public Compass(){
		heading = NORTH;
	}
	public void turnLeft(){
		heading--;
		if(heading < 0)
			heading += 4;
	}
	public void turnRight(){
		heading = (heading + 1) % 4;
	}
	public int goNorth(){
		return degrees[heading];
	}
}