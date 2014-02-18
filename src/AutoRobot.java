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
     bot.printStack();
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
		path.push(d);//record movement
	}
    
    public boolean checkPath(Direction direction) {
        int temp;
        int count = -1; 		//Tracks # of moves into each path
        boolean correctPath = true;
        
        if(tiles == TileSet.DEAD_END)
            return false;
        
        move(direction);
        path.push(direction);
        
        while(true) {
            try{Thread.sleep(200);}catch(Exception e){}
            scanTiles();
            
            if(tiles == TileSet.CENTER) {
                move(Direction.FORWARD);
                path.push(Direction.FORWARD);
            }
            
            else if(tiles == TileSet.INTERSECTION) {
                checkPath(Direction.ARC_RIGHT);
                checkPath(Direction.FORWARD);
                checkPath(Direction.ARC_LEFT); 
            }
                    
            else if(tiles == TileSet.RIGHT || tiles == TileSet.END_RIGHT) {
                checkPath(Direction.ARC_RIGHT);
                checkPath(Direction.FORWARD);
            }
        
            else if(tiles == TileSet.LEFT || tiles == TileSet.END_LEFT) {
                checkPath(Direction.ARC_LEFT);
                checkPath(Direction.FORWARD);
            }
        
            else if (tiles == TileSet.END) {
                move(Direction.FORWARD);
                path.push(Direction.FORWARD);
                System.out.println("End found.");
                Button.ENTER.waitForPressAndRelease();
            }
            
            else if (tiles == TileSet.DEAD_END) {
                reversePath(count);
                if(direction == Direction.FORWARD) {
                    move(Direction.SPIN_BACK);
                }
                else {
                    move(direction);
                }   
                correctPath = false;
                break;
            }
            count++;
        }
        
        return correctPath;
    }   
    
    public void reversePath(int count) {
        Direction d;
        move(Direction.SPIN_BACK);
        path.pop();     //Clears the forward move into the dead end.
        
        while(count > 0) {//ISSUE: What is count? Should it be path.size() instead?
            d = path.pop();
            switch(d) {
                case ARC_RIGHT:
                    move(Direction.ARC_LEFT);   
                    break;
                case ARC_LEFT:
                    move(Direction.ARC_RIGHT);
                    break;
                case FORWARD:
                    move(Direction.FORWARD);
                    break;
            }
            count--;
        }
    }                            
     
    public void exitMaze() {
        Direction d;
        
        path = invertTurns(path);
        
        while(!path.isEmpty()) {
            d = path.pop();
            switch(d) {
                case ARC_RIGHT:
                    System.out.print('R');
                    move(Direction.ARC_RIGHT);  
                    break;
                case SPIN_LEFT:
                    System.out.print('L');
                    move(Direction.ARC_LEFT);
                    break;
                case FORWARD:
                    System.out.print('F');
                    move(Direction.FORWARD);
                    break;
            }
            try{Thread.sleep(200);} catch(Exception e) {}   
        }
    }
    
    public Stack<Direction> invertTurns(Stack<Direction> path) {
        Stack<Direction> tempStack = new Stack<Direction>();
        Direction temp;
        
        while(!path.isEmpty()){
            tempStack.push(path.pop());
        }
        
        while(!tempStack.isEmpty()) {
            temp = tempStack.pop();
            
            if(temp == Direction.SPIN_RIGHT)           //This block inverts directions:
                path.push(Direction.SPIN_LEFT);    // R=L, L=R, F=F
            else if(temp == Direction.SPIN_LEFT)
                path.push(Direction.SPIN_RIGHT);    
            else
                path.push(Direction.FORWARD);               
        }
        
        return path;    
    }
    
    public void printStack() {
        while(!path.isEmpty()) {
            System.out.println(path.pop());
        }
    }
    
}
