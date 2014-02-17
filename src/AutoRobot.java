import java.util.Stack;
import lejos.nxt.*;
import lejos.robotics.navigation.DifferentialPilot;

public class AutoRobot {

    private Stack<Direction> path;
    
    /*private static final int RIGHT = 1;
    private static final int LEFT = 2;
    private static final int CENTER = 0;*/
    
    private static final int TILE_SIZE = 115; //Measured in mm
    public static final int CENTER = 0, RIGHT = 1, LEFT = 2;
    public static Tile[] lights = new Tile[3];
    private static LightSensor lCenter = new LightSensor(SensorPort.S1);
    private static LightSensor lRight = new LightSensor(SensorPort.S2);
    private static DifferentialPilot pilot;
    /*Tile possiblities*/

    public AutoRobot() {
        
        path = new Stack<Direction>(); 
        
        pilot = new DifferentialPilot(56f, 107f, Motor.B, Motor.C);
        pilot.setTravelSpeed(100);
        pilot.setRotateSpeed(80);
    }
    public void update(){
     lights[CENTER] = Tile.parse(lCenter.readValue());
     lights[RIGHT] = Tile.parse(lRight.readValue());
     //lights[LEFT] =  Tile.parse(lLeft.readValue()));
    }

	public void move(Direction d){
		pilot.arc(d.radius * TILE_SIZE, d.angle);//angular component
		pilot.travel(d.travel * TILE_SIZE);//linear component
		path.push(d);//record movement
	}
    public static void main(String[] args){
     AutoRobot bot = new AutoRobot();
     bot.update();
     bot.enterMaze();
    }
}