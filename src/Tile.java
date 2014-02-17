public enum Tile{
    BLACK(0,36),
    //BLUE(30,35),
    RED(37,45),
    WHITE(47,60);
	
	float minReading, maxReading;
	Tile(int minReading, int maxReading){
	    this.minReading = minReading;
	    this.maxReading = maxReading;
	}
     public static Tile parse(int light){
		for(Tile t : Tile.values()){
			if(light <= t.maxReading && light >= t.minReading){
				return t;
			}				
		}
          return null;
	}
}