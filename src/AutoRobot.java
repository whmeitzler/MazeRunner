import java.util.Stack;
import lejos.nxt.*;
import lejos.robotics.navigation.DifferentialPilot;

public class AutoRobot {

    private Stack<Integer> instList;
    
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
        
        instList = new Stack<Integer>(); 
        
        pilot = new DifferentialPilot(56f, 107f, Motor.B, Motor.C);
        pilot.setTravelSpeed(100);
        pilot.setRotateSpeed(80);
    }
    public void update(){
     lights[CENTER] = Tile.parse(lCenter.readValue());
     lights[RIGHT] = Tile.parse(lRight.readValue());
     //lights[LEFT] =  Tile.parse(lLeft.readValue()));
    }
    public static void main(String[] args){
     AutoRobot bot = new AutoRobot();
     bot.update();
     bot.enterMaze();
    }
 
    public void enterMaze() {
        while(true) {
            update(); 
            try{Thread.sleep(400);} catch(Exception e){}
            /*[ ][W][B]
            Robot travels forward*/
            if(lights[CENTER] == Tile.WHITE  && 
                         lights[RIGHT] == Tile.BLACK){
              System.out.println("- [ ] [W] [B] -");          
              pilot.travel(TILE_SIZE);
              instList.push(CENTER);
            }  
            /*[ ][W][W]
            Robot turns right to explore or go home*/
            if(lights[CENTER] == Tile.WHITE && 
                         (lights[RIGHT] == Tile.WHITE ||
                          lights[RIGHT] == Tile.RED)){
              System.out.println("- [ ] [W] [W] -");             
              //pilot.arcForward(-1*TILE_SIZE*1.5);            
              pilot.travel(TILE_SIZE*0.7);
              pilot.rotate(90);
              pilot.travel(TILE_SIZE*0.1);
              instList.push(CENTER);
                
              instList.push(RIGHT);
            } 
            /*[ ][B][B]
            Robot turns left to avoid wall*/
            if(lights[CENTER] == Tile.BLACK && 
                         lights[RIGHT] == Tile.BLACK){
              System.out.println("- [ ] [B] [B] -");              
              pilot.rotate(-90);  
              pilot.travel(20);                   
              instList.push(LEFT);
            }  
            /*[ ][R][B]
            Robot is home!*/
             if(lights[CENTER] == Tile.RED  && 
                         (lights[RIGHT] == Tile.BLACK)){
              System.out.println("- [ ] [R] [B] -");             
              System.out.println("HOME");
              pilot.travel(TILE_SIZE);
              instList.push(CENTER);
              Sound.beep();
              break;
            }            
                        
        }
    }       
}/*    
    public void exitMaze() {
        int temp;
        
        instList = invertTurns(instList);
        
        while(!instList.isEmpty()) {
            temp = (int) instList.pop();
            switch(temp) {
                case RIGHT:
                    System.out.print('R');
                    pilot.rotate(90);   
                    break;
                case LEFT:
                    System.out.print('L');
                    pilot.rotate(-90);
                    break;
                case CENTER:
                    System.out.print('F');
                    pilot.travel(TILE_SIZE);
                    break;
            }
            try{Thread.sleep(400);} catch(Exception e) {}   
        }
    }    
    
    public Tile[] scanTile() {
        Tile[] reading = new Tile[2];
        Tile[CENTER] = Tile.parse(centerL.readValue());
        Tile[RIGHT] = Tile.parse(rightL.readValue());
        
    }
    
    public int findTurn() {
        pilot.rotate(90);
        try{Thread.sleep(100);}catch(Exception e){}
        if(scanTile() == Tile.WHITE)
            return RIGHT;
        pilot.rotate(180);
        return LEFT;    
    }
    
    public Stack<Integer> invertTurns(Stack<Integer> instList) {
        Stack<Integer> tempStack = new Stack<Integer>();
        int temp;
        
        while(!instList.isEmpty()){
            tempStack.push((Integer) instList.peek());
            instList.pop();
        }
        
        while(!tempStack.isEmpty()) {
            temp = (int) tempStack.peek();
            
            if(temp == RIGHT)           //This block inverts directions:
                instList.push(LEFT);    // R=L, L=R, F=F
            else if(temp == LEFT)
                instList.push(RIGHT);    
            else
                instList.push(CENTER);
             
            tempStack.pop();               
        }
        
        return instList;    
    }
  
    public static void main(String[] args) {
        AutoRobot a = new AutoRobot();
        a.enterMaze();
        System.out.println();
        a.exitMaze();
        System.out.println();
        System.out.println("Done.");
    }
}*/