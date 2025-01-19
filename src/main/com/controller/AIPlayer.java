package main.com.controller;

import java.awt.Point;

import main.com.model.GameBoard;
import main.com.model.Ship;
import main.com.util.BoardRandomizer;
import main.com.util.GameShotGuessGenerator;

public class AIPlayer implements GameBoard.BoardEvents {

  private GameCore gameCore;
  private GameShotGuessGenerator shotGenerator;
  private BoardRandomizer boardRandomizer;
  
  public AIPlayer(GameCore gameCore) {
    this.gameCore = gameCore;

    if (gameCore.getPlayerBoard() == null) {
      System.exit(-1);
    }
    this.shotGenerator = new GameShotGuessGenerator(gameCore.getPlayerBoard());
    this.boardRandomizer = new BoardRandomizer();
  }

  public void shootAtBoard() {
    Point shot = null;
    do {
      shot = this.shotGenerator.getGuess();
    } while(!gameCore.getPlayerBoard().shootAt(shot.x, shot.y));
  }
  
  public GameShotGuessGenerator getShotGenerator() {
    return shotGenerator;
  }
  
  public void placeShips() {
    this.boardRandomizer.randomFillBoard(gameCore.getOpponentBoard());
  }

  @Override
  public void onAllShipSunk(GameBoard board) {
    try {
      gameCore.setWinnerPlayer();
      System.out.println("GAMEOVER PLAYER WIN");
      gameCore.resetGame();
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }

  @Override
  public void onShotHit(Ship ship, int x, int y) {
    gameCore.getMainWindow().repaintopponentBoardView();
  }

  @Override
  public void onShotMiss(int x, int y) {
    gameCore.getMainWindow().repaintopponentBoardView();
  }

  @Override
  public void onShipAdded(Ship ship) {
    gameCore.getMainWindow().repaintopponentBoardView();
  }

  @Override
  public void onShipMoved(Ship ship) {
    gameCore.getMainWindow().repaintopponentBoardView();
  }

  @Override
  public void onShipRotate(Ship ship) {
    gameCore.getMainWindow().repaintopponentBoardView();
  }
}
