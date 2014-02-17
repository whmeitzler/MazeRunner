public enum Direction{
	 STAY		( 0,   0, 0),
     SPIN_BACK	( 0, 180, 0),
     FORWARD	( 0,   0, 1),
     BACKWARD	( 0,   0,-1),
     ARC_LEFT	( 1,  90,-1),
     ARC_RIGHT  (-1,  90,-1),
     SPIN_LEFT	( 0,  90,-1),
	 SPIN_RIGHT	( 0, -90,-1);
	 
    public int radius;
	public int angle;
    public int travel;

    Direction(int radius, int angle, int travel){
        this.radius = radius;
		this.angle = angle;
	    this.travel = travel;
    }
    public Direction invert(){
     switch(this){
		  case STAY:
				return STAY;
          case SPIN_BACK:
               return SPIN_BACK;
          case FORWARD:
               return BACKWARD;
          case BACKWARD:
               return FORWARD;
          case LEFT:
               return RIGHT;  
          case RIGHT:
               return LEFT;
		  case ARC_LEFT:
				return ARC_RIGHT;
		  case ARC_RIGHT:
				return ARC_LEFT;	
          default:
               return null;                   
          }
    }
}