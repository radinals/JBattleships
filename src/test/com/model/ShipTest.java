package test.com.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import main.com.model.Ship;
import main.com.model.ShipOrientation;
import main.com.model.ShipType;

class ShipTest {

  @Test
  void shipInittest() {
    Ship ship = new Ship(0,0,ShipType.CARRIER,ShipOrientation.NORTH);
    
    for (int[] point : ship.getShipBody()) {
      int x = point[0];
      int y = point[1];
      
      if (x != 0 || y < 0 || y > ShipType.CARRIER.getLength())
        fail("incorrect ship body coordinate");
    }
    
  }
  
  void testShipHeadTailPos(Ship ship, int testHeadx, int testHeady,
      int testTailx, int testTaily ) {

    if (ship.getHeadX() != testHeadx) 
      fail("Incorrect Head X Point Values: " + ship.getHeadX());

    if (ship.getHeadY() != testHeady) 
      fail("Incorrect Head Y Point Values: " + ship.getHeadY());

    if (ship.getTailX() != testTailx) 
      fail("Incorrect Tail X Point Values: " + ship.getTailX());

    if (ship.getTailY() != testTaily) 
      fail("Incorrect Tail Y Point Values: " + ship.getTailY());

  }
  
  @Test
  void shipRotateTest() {
    Ship ship = new Ship(4,5,ShipType.SUBMARINE,ShipOrientation.NORTH);
    
    testShipHeadTailPos(ship, 4, 5, 4, 8); // FACING NORTH
    
    ship.rotateRight(); testShipHeadTailPos(ship, 4, 5, 7, 5); // FACING EAST
    ship.rotateRight(); testShipHeadTailPos(ship, 4, 5, 4, 2); // FACING SOUTH
    ship.rotateRight(); testShipHeadTailPos(ship, 4, 5, 1, 5); // FACING WEST
    ship.rotateRight(); testShipHeadTailPos(ship, 4, 5, 4, 8); // FACING NORTH

  }

}
