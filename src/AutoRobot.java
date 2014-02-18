import java.util.Stack; 
import lejos.nxt.*;
import lejos.robotics.navigation.DifferentialPilot;

public class AutoRobot {

    private Stack<Direction> path;
      
    private static final int TILE_SIZE = 115;
    
    public static TileSet tiles;
    
    private static LightSensor lCenter = new LightSensor(SensorPort.S1);
    private static LightSensor lRight = new LightSensor(SensorPort.S2);
    private static LightSensor lLeft = new LightSensor(SensorPort.S3);
                                                                                                       
    private static DifferentialPilot pilot;

    public AutoRobot() {
        
        path = new Stack<Direction>(); 
        
        pilot = new DifferentialPilot(56f, 107f, Motor.B, Motor.C);
        pilot.setTravelSpeed(100);
        pilot.setRotateSpeed(80);
    }
    
    public static void main(String[] args){
     AutoRobot bot = new AutoRobot();
     bot.checkPath(Direction.FORWARD);
    }
    
    public void scanTiles() {
        Tile left, right, center;
        
        left = Tile.parse(lLeft.readValue());
        center =  Tile.parse(lCenter.readValue());
        right = Tile.parse(lRight.readValue());
        
        tiles = TileSet.getTileSet(left,right,center);
    }

	public void move(Direction d){
		pilot.arc(d.radius * TILE_SIZE, d.angle);//angular component
		pilot.travel(d.travel * TILE_SIZE);//linear component
	}
    
    public boolean checkPath(Direction direction) {
        int count; 		//Tracks # of moves into each path
        boolean correctPath = true;
        
        if(tiles == TileSet.DEAD_END)
            return false;
        
        move(direction);
        path.push(direction);
        count = 1;
        System.out.println(count);
        
        while(true) {
            try{Thread.sleep(200);}catch(Exception e){}
            scanTiles();
            
            if(tiles == TileSet.CENTER) {
                move(Direction.FORWARD);
                path.push(Direction.FORWARD);
            }
            
            else if(tiles == TileSet.INTERSECTION) {
               System.out.println("Intersection Found.");
                move(Direction.FORWARD);
                path.push(Direction.FORWARD);
                checkPath(Direction.SPIN_RIGHT);
                System.out.println("Checking forward.");
                checkPath(Direction.FORWARD);
                System.out.println("Checking left.");
                checkPath(Direction.SPIN_LEFT); 
            }
            
            else if(tiles == TileSet.RIGHT || tiles == TileSet.END_RIGHT) {
               System.out.println("Right Turn Found.");
                move(Direction.FORWARD);
                path.push(Direction.FORWARD);
                checkPath(Direction.SPIN_RIGHT);
                checkPath(Direction.FORWARD);
            }
            
            else if(tiles == TileSet.LEFT || tiles == TileSet.END_LEFT) {
               System.out.println("Left Turn Found.");
                move(Direction.FORWARD);
                path.push(Direction.FORWARD);
                checkPath(Direction.SPIN_LEFT);
                checkPath(Direction.FORWARD);
            }
            
            else if(tiles == TileSet.END){
               System.out.println("End found.");
               move(Direction.FORWARD);
               path.push(Direction.FORWARD);
               exitMaze();
            }
            
            else if (tiles == TileSet.DEAD_END) {
               System.out.println("Dead End found.");
                reversePath(count);
                if(direction == Direction.FORWARD) {
                    move(Direction.SPIN_BACK);
                }
                break;
            }
            count++;
            System.out.println(direction + ": " + count);
        }
        return correctPath;
    }   
    
    public void reversePath(int count) {
        Direction d;
        move(Direction.SPIN_BACK);
        
        
        for(int i=count; i>0; i--) {
            move(path.pop());
        }    
    }                            
     
    public void exitMaze() {
        Direction d;
        
        move(Direction.SPIN_BACK);
        while(!path.isEmpty()) {
            move(path.pop().invertTurns());
            try{Thread.sleep(200);} catch(Exception e) {}   
        }
        
        System.out.println("Done.");
        Button.ENTER.waitForPressAndRelease();
        System.exit(0);
    }    
}
