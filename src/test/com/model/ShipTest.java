package test.com.model;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;

import org.junit.jupiter.api.Test;

import main.com.model.Ship;
import main.com.model.ShipOrientation;
import main.com.model.ShipType;

class ShipTest {

  @Test
  void shipInittest() {
    Ship ship = new Ship(0,0,ShipType.CARRIER,ShipOrientation.NORTH);
    
    for (Point p: ship.getShipBody()) {
      if (p.x != 0 || p.y < 0 || p.y > ShipType.CARRIER.getLength())
        fail("incorrect ship body coordinate");
    }
    
  }
  
  void testShipHeadTailPos(Ship ship, int testHeadx, int testHeady,
      int testTailx, int testTaily ) {
    
    Point head = ship.getHead();
    Point tail = ship.getTail();

    if (head.x != testHeadx) 
      fail("Incorrect Head X Point Values: " + head.x);

    if (head.y != testHeady) 
      fail("Incorrect Head Y Point Values: " + head.y);

    if (tail.x != testTailx) 
      fail("Incorrect Tail X Point Values: " + tail.x);

    if (tail.y != testTaily) 
      fail("Incorrect Tail Y Point Values: " + tail.y);

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
