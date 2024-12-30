package test.com.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import main.com.model.GameBoard;
import main.com.model.ShipOrientation;
import main.com.model.ShipType;

class GameBoardTest {

  @Test
  void shipPlacementTest() {
    GameBoard gameBoard = new GameBoard(10);
    
    try {
      gameBoard.placeShip(0, 0, ShipType.CARRIER, ShipOrientation.NORTH);
      
      if (gameBoard.getShip(ShipType.CARRIER) == null)
        throw new Exception("Added Ship Not Found in board");

      gameBoard.placeShip(0, 0, ShipType.BATTLESHIP, ShipOrientation.NORTH);

      if (gameBoard.getShip(ShipType.CARRIER) == null)
        throw new Exception("Valid Ship Not Found in board");

      if (gameBoard.getShip(ShipType.BATTLESHIP) != null)
        throw new Exception("Invalid Ship Found in board");
      
    } catch(Exception e) {
      fail(e);
    }
    
  }
  
  @Test
  void shipShootingTest() {
    GameBoard gameBoard = new GameBoard(10);
    
    gameBoard.placeShip(0, 0, ShipType.CARRIER, ShipOrientation.NORTH);
    
    
    if (gameBoard.getShip(ShipType.CARRIER).getHitcount() != ShipType.CARRIER.getLength()) {
     fail("hitcount has incorrect initial values");
    }

    gameBoard.shootAt(0, 0);
    
    if (gameBoard.getShip(ShipType.CARRIER).getHitcount() != ShipType.CARRIER.getLength() - 1) {
     fail("hitcounts not updating");
    }
    
    gameBoard.shootAt(0, 1);
    gameBoard.shootAt(0, 2);
    gameBoard.shootAt(0, 3);
    gameBoard.shootAt(0, 4);
    
    if (!gameBoard.getShip(ShipType.CARRIER).isSunk()) {
     fail("incorrect sink status");
    }

    if (!gameBoard.allShipSunk()) {
     fail("board has incorrect allshipsunk status");
    }
    
  }

}
