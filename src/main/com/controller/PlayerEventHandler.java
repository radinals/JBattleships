package main.com.controller;

import java.awt.Point;

import main.com.model.GameBoard;
import main.com.model.Ship;

public class PlayerEventHandler implements GameBoard.BoardEvents {

  GameCore gameCore;

  public PlayerEventHandler(GameCore gameCore) {
    this.gameCore = gameCore;
  }

  @Override
  public void onAllShipSunk(GameBoard board) {
    try {
      gameCore.setWinnerOpponent();
      System.out.println("GAMEOVER OPPONENT WIN");
      gameCore.resetGame();
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }

  @Override
  public void onShotHit(Ship ship, int x, int y) {
    gameCore.generateAIGuess(new Point(x, y));
    gameCore.getMainWindow().repaintPlayerBoardView();
  }

  @Override
  public void onShotMiss(int x, int y) {
    gameCore.getMainWindow().repaintPlayerBoardView();
  }

  @Override
  public void onShipAdded(Ship ship) {
    gameCore.getMainWindow().repaintPlayerBoardView();
  }

  @Override
  public void onShipMoved(Ship ship) {
    gameCore.getMainWindow().repaintPlayerBoardView();
  }

  @Override
  public void onShipRotate(Ship ship) {
    gameCore.getMainWindow().repaintPlayerBoardView();
  }

}
