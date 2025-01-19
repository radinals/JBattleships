package main.com.controller.ai;

import main.com.model.GameBoard;
import main.com.model.ShipOrientation;
import main.com.model.ShipType;

public class BoardRandomizer {
  
  private RandomNumberGenerator rng;
  
  public BoardRandomizer() {
   this.rng = new NativeRandomNumberGenerator(); 
  }
  
  public void randomFillBoard(GameBoard board) {
    for (ShipType type : ShipType.values()) {
      int x, y;
      ShipOrientation orientation;
      do {
        x = rng.rangeI(0, board.getBoardSize());
        y = rng.rangeI(0, board.getBoardSize());
        orientation = ShipOrientation.values()[rng.rangeI(0, ShipOrientation.values().length)];
      } while (!board.placeShip(x, y, type, orientation));
    } } 
}
