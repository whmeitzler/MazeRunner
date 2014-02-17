import lejos.nxt.*;

public enum LightArray{
     FORWARD(0,SensorPort.S1),  
     RIGHT(1,SensorPort.S2);
     //LEFT(SensorPort.S3),
     public int index;
     public LightSensor sensor;
     
     LightArray(int index, SensorPort port){
          this.index = index;
          this.sensor = new LightSensor(port);  
     }
     public Tile[]read(){
          Tile[] results = new Tile[2];
          results[FORWARD.index] = Tile.parse(FORWARD.sensor.readValue());
          results[RIGHT.index] = Tile.parse(RIGHT.sensor.readValue());
          //results[2] = Tile.parse(LEFT.sensor.readValue());
          return results;
     }
}