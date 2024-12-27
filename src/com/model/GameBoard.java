package com.model;

import java.util.HashMap;

import com.model.Ship.ShipEvents;
import com.util.CopyUtil;

public class GameBoard implements ShipEvents {

  private final int boardSize;
  private GameBoardCell[][] board;
  private HashMap<ShipType, Ship> ships;
  private int sunkShipsCounter;
  
  public interface BoardEvents {
    public void onAllShipSunk(GameBoard board);
    public void onShipAdded(final Ship ship);
    public void onShipMoved(final Ship ship);
    public void onShipRotate(final Ship ship);
    public void onShotHit(final Ship ship, int x, int y);
    public void onShotMiss(int x, int y);
  }
  
  private BoardEvents boardEvents;

  public GameBoard(int boardSize) {
    this(boardSize, null);
  }

  public GameBoard(int boardSize, BoardEvents boardEventHandler) {
    this.boardSize = boardSize;
    this.board = new GameBoardCell[boardSize][boardSize];
    this.ships = new HashMap<ShipType, Ship>();
    this.sunkShipsCounter = 0;
    this.boardEvents= boardEventHandler;

    initBoard(board, boardSize);
  }

  private static void initBoard(GameBoardCell[][] board, int boardSize) {
    for (int y = 0; y < boardSize; y++) {
      for (int x = 0; x < boardSize; x++) {
        board[y][x] = new GameBoardCell();
      }
    }
  }

  public boolean shipExists(ShipType type) {
    return ships.containsKey(type);
  }

  private boolean placementValid(Ship ship) {
    for (int[] point : ship.getShipBody()) {
      if (!pointInBounds(point[0],point[1])) return false;

      for (ShipType k : ships.keySet()) {
        if (ships.get(k).pointIntersectBody(point[0], point[1])) return false;
      }

    }
    return true;
  }
  
  private boolean placeShip(Ship ship) {

    if (!placementValid(ship))
      return false;

    GameBoardCell[][] tmpBoard = new GameBoardCell[this.boardSize][this.boardSize];

    CopyUtil.deepCopyMatrix(this.board, tmpBoard, GameBoardCell::new);

    try {
      for (int[] point : ship.getShipBody()) {
        int px = point[0], py = point[1];
        tmpBoard[py][px].setShip(ship.getShipType());
      }

      ships.put(ship.getShipType(), ship);

      CopyUtil.deepCopyMatrix(tmpBoard, this.board, GameBoardCell::new);

      if (boardEvents != null) boardEvents.onShipAdded(ship);

    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  public boolean placeShip(int x, int y, ShipType type, ShipOrientation orientation) {
    Ship ship = new Ship(x, y, type, orientation, this);
    return placeShip(ship);
  }

  // TODO: Can this be simpler?
  public void reloadBoard() {
    GameBoardCell[][] tmpBoard = new GameBoardCell[this.boardSize][this.boardSize];
    initBoard(tmpBoard, boardSize);

    for (ShipType k : ships.keySet()) {
      Ship ship = ships.get(k);
      for (int[] point : ship.getShipBody()) {
        int px = point[0], py = point[1];
        tmpBoard[py][px].setShip(ship.getShipType());
      }
    }

    CopyUtil.deepCopyMatrix(tmpBoard, this.board, GameBoardCell::new);
  }
  
  public void rotateShip(ShipType type) throws Exception {
    Ship ship = getShip(type); 
    if (ship != null) {
      removeShip(ship);

      ship.rotateRight();

      if(!placementValid(ship)) {
          ship.restorePrevOrientation();
        if(!placementValid(ship)) {
          System.err.println("Failed to restore ship after rotate failure.");
          System.exit(-1);
        }
        placeShip(ship);

        throw new Exception("Invalid Rotation");
      }

      placeShip(ship);

      if (boardEvents != null) boardEvents.onShipRotate(ship);
    }
  }

  public int getBoardSize() {
    return boardSize;
  }

  public Ship getShip(ShipType type) {
    return ships.getOrDefault(type,null);
  }

  public GameBoardCell cellAt(int line, int column)
      throws IndexOutOfBoundsException, NullPointerException {
    GameBoardCell cell = board[column][line];
    if (cell == null)
      throw new NullPointerException(
          String.format("cell at board[%d][%d] is null", column, line));
    return cell;
  }

  public boolean hasShipAtCell(int line, int column)
      throws IndexOutOfBoundsException, NullPointerException {
    GameBoardCell cell = cellAt(line, column);
    return cell.containsAShip();
  }

  public boolean shotIsValid(int x, int y) {
    if (x < 0 || x > boardSize || y < 0 || y > boardSize)
      return false;

    try {
      GameBoardCell cell = cellAt(x, y);
      if (cell.isMarkHit() || cell.isMarkMiss()) {
        return false;
      }
    } catch (IndexOutOfBoundsException e) {
      return false;
    } catch (NullPointerException e) {
      e.printStackTrace();
      System.exit(-1);
    }

    return true;
  }

  public boolean shootAt(int x, int y) {
    if (!shotIsValid(x, y))
      return false;

    try {
      GameBoardCell cell = cellAt(x, y);

      if (cell.containsAShip()) {
        cell.setMarkHit();
        Ship ship = getShip(cell.ship);
        ship.takeHit();
        if(boardEvents != null) boardEvents.onShotHit(ship, x, y);
      } else {
        cell.setMarkMiss();
        if(boardEvents != null) boardEvents.onShotMiss(x,y);
      }
      
    } catch (IndexOutOfBoundsException e) {
      System.err.println(String.format("invalid shot %d,%d",x,y ));
      e.printStackTrace();
      return false;
    } catch (NullPointerException e) {
      e.printStackTrace();
      System.exit(-1);
    }
    
    
    return true;
  }
  
  public GameBoardCell[][] getBoard() {
    return board;
  }

  @Override
  public String toString() {
    String boardStr = "";

    for (GameBoardCell[] line : board) {
      for (GameBoardCell cell : line) {

        boardStr += "[";
        if (cell.containsAShip() && cell.isMarkNone()) {
          boardStr += cell.ship.toString().charAt(0);
        } else if (cell.isMarkHit()) {
          boardStr += "X";
        } else if (cell.isMarkMiss()) {
          boardStr += "?";
        } else if (cell.isMarkNone()) {
          boardStr += " ";
        }
        boardStr += "]";
      }
      boardStr += "\n";
    }

    return boardStr;
  }
  public boolean pointInBounds(int x, int y) {
    return (x >= 0 && x < boardSize) && (y >= 0 && y < boardSize);
  }
  
  public void moveShip(ShipType type, int x, int y) throws Exception {
    Ship ship = getShip(type);
    if (ship != null) {
      removeShip(ship);

      ship.moveHead(x, y);

      if(!placementValid(ship)) {
          ship.restorePrevPos();
//        if(!placementValid(ship)) {
//          System.err.println("Failed to restore ship after move failure.");
//          System.exit(-1);
//        }
        placeShip(ship);
        throw new Exception("Invalid Move to " + x + "," + y);
      }

      placeShip(ship);

      if (boardEvents != null) boardEvents.onShipMoved(ship);
    }

  }
  
  public void reset() {
    ships.clear();
    sunkShipsCounter=0;
    for (GameBoardCell[] line : board) {
      for (GameBoardCell cell : line) {
        cell.removeShip();
        cell.setMarkNone();
      }
    }
  }
  
  public void removeShip(Ship ship) {
    ships.remove(ship.getShipType());
    for (int[] point : ship.getShipBody()) {
      GameBoardCell cell = cellAt(point[0], point[1]);
      cell.setMarkNone();
      cell.removeShip();
    }
  }

  public boolean allShipSunk() {
    return sunkShipsCounter >= ships.size();
  }

  @Override
  public void onShipSunk(Ship ship) {
    if (!allShipSunk()) {
      ++sunkShipsCounter;

      if (boardEvents != null && allShipSunk()){
       boardEvents.onAllShipSunk(this); 
      }

      System.err.println("SUNK: " + ship);
      System.err.println("SUNK: " + sunkShipsCounter + "/" + ships.size());
    }
  }

  @Override
  public void onShipMove(Ship ship) {
    reloadBoard();
    boardEvents.onShipMoved(ship);
  }

  @Override
  public void onShipRotate(Ship ship) {
    reloadBoard();
  }

}