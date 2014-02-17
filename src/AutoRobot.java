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
		path.push(d);//record movement
	}
    
    public boolean checkPath(Direction direction) {
        int temp;
        int count = 0; 		//Tracks # of moves into each path
        boolean correctPath = true;
        
        while(true) {
            try{Thread.sleep(300);}catch(Exception e){}
            scanTiles();

            if(tiles == TileSet.CENTER) {
                pilot.travel(TILE_SIZE);
                path.push(Direction.FORWARD);
            }
            
            else if(tiles == TileSet.INTERSECTION) {
                pilot.travel(TILE_SIZE/2);
                path.push(Direction.FORWARD);
                pilot.rotate(90);
                path.push(Direction.SPIN_RIGHT);
                checkPath(Direction.SPIN_RIGHT);
                checkPath(Direction.FORWARD);
                checkPath(Direction.SPIN_LEFT); 
            }
                    
            else if(tiles == TileSet.RIGHT || tiles == TileSet.END_RIGHT) {
                pilot.travel(TILE_SIZE/1.5);
                path.push(Direction.FORWARD);
                pilot.rotate(90);
                path.push(Direction.SPIN_RIGHT);
                checkPath(Direction.SPIN_RIGHT);
                checkPath(Direction.FORWARD);
            }
        
            else if(tiles == TileSet.LEFT || tiles == TileSet.END_LEFT) {
                pilot.travel(TILE_SIZE/2);
                path.push(Direction.FORWARD);
                pilot.rotate(-90);
                path.push(Direction.SPIN_LEFT);
                checkPath(Direction.SPIN_LEFT);
                checkPath(Direction.FORWARD);
            }
        
            else if (tiles == TileSet.END) {
                pilot.travel(TILE_SIZE);
                path.push(Direction.FORWARD);
                System.out.println("End found.");
                while(true){}
            }
            
            else if (tiles == TileSet.DEAD_END) {
                reversePath(count);
                switch(direction) {
                    case SPIN_RIGHT:
                        pilot.rotate(90);
                        break;
                    case SPIN_LEFT:
                        pilot.rotate(-90);
                        break;
                    case FORWARD:
                        pilot.rotate(180);
                        break;        
                }
                correctPath = false;
                break;
            }
        }
        
        return correctPath;
    }   
    
    public void reversePath(int count) {
        Direction d;
        pilot.rotate(180);
        
        while(count > 0) {
            d = path.pop();
            switch(d) {
                case SPIN_RIGHT:
                    pilot.rotate(90);   
                    break;
                case SPIN_LEFT:
                    pilot.rotate(-90);
                    break;
                case FORWARD:
                    pilot.travel(TILE_SIZE);
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
                case SPIN_RIGHT:
                    System.out.print('R');
                    pilot.rotate(90);   
                    break;
                case SPIN_LEFT:
                    System.out.print('L');
                    pilot.rotate(-90);
                    break;
                case FORWARD:
                    System.out.print('F');
                    pilot.travel(TILE_SIZE);
                    break;
            }
            try{Thread.sleep(400);} catch(Exception e) {}   
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
}