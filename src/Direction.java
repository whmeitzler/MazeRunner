/* Direction
** Abstracts all moves made by the robot in a maze into one class
** Supports angular turns and linear movement. 
** Offers several useful operations for angles in the robot
*/
public enum Direction{
    STAY         ( 0,   0, 0),
    SPIN_BACK    ( 0, 180, 0),
    FORWARD      ( 0,   0, 1),
    BACKWARD     ( 0,   0,-1),
    ARC_LEFT     (-1,  -90, 0),
    ARC_RIGHT    ( 1,  90, 0),
    SPIN_LEFT     ( 0,  -90, 0),
    SPIN_RIGHT   ( 0, 90, 0);
    
    public final int LEFT = -1, RIGHT = 1, CENTER = 0;
    public int radius;
    public int angle;
    public int travel;

    Direction(int radius, int angle, int travel){
       this.radius = radius;
       this.angle = angle;
       this.travel = travel;
    }
    //Returns number of 90-degree turns performed by the robot
    //Positive values turn right, negative values turn left
    public int rightAngles{
        return this.angle / 90;        
    }
    /*Inverts every direction. Some are blatantly obvious 
    ** (specifically STAY)
    */
    public Direction invertAll(){
     switch(this){
          case STAY:
                return STAY;
          case SPIN_BACK:
               return SPIN_BACK;
          case FORWARD:
               return BACKWARD;
          case BACKWARD:
               return FORWARD;
          case SPIN_LEFT:
               return SPIN_RIGHT;  
          case SPIN_RIGHT:
               return SPIN_LEFT;
          case ARC_LEFT:
                return ARC_RIGHT;
          case ARC_RIGHT:
                return ARC_LEFT;    
          default:
               return this;                   
          }
    }
    /*Inverts ONLY directions that turn
    */
    public Direction invertTurns(){
     switch(this){
          case SPIN_BACK:
               return SPIN_BACK;
          case SPIN_LEFT:
               return SPIN_RIGHT;  
          case SPIN_RIGHT:
               return SPIN_LEFT;
           case ARC_LEFT:
         return ARC_RIGHT;
        case ARC_RIGHT:
          return ARC_LEFT;    
          default:
               return this;                   
          }
    }
}
