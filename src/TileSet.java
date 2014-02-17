public enum TileSet{
    /*TileSets that correspond to robot movements*/
    
    CENTER(Tile.BLACK, Tile.WHITE, Tile.BLACK),
    LEFT(Tile.WHITE, Tile.WHITE, Tile.BLACK),
    RIGHT(Tile.BLACK, Tile.WHITE, Tile.WHITE),
    
    /*The robot has reached a dead end*/
    INTERSECTION(Tile.WHITE, Tile.WHITE, Tile.WHITE),
    DEAD_END(Tile.BLACK, Tile.BLACK, Tile.BLACK),
    
    /*For when the end square has been detected (right, left, or in front)
      When end is detected right, tile on left doesn't matter, hence null 
      Vice versa for END_LEFT*/
    
    END_RIGHT(null, Tile.WHITE, Tile.RED), 
    END_LEFT(Tile.RED, Tile.BLACK, null),
    END(Tile.BLACK, Tile.RED, Tile.BLACK);
	
	Tile left, center, right;
     
    TileSet(Tile left, Tile center, Tile right){
        this.left = left;
        this.center = center;
        this.right = right;
	}
     
     public static TileSet getTileSet(Tile left, Tile right, Tile center){
		for(TileSet t : TileSet.values()){
			if((left == t.left || t.left == null) && center == t.center 
                && (right == t.right || t.right == null))
                    return t;				
		}
          return null;
	}
}