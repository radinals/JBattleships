package main.com.controller;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.random.RandomGenerator;

import main.com.model.GameBoard;
import main.com.model.Ship;
import main.com.model.ShipOrientation;
import main.com.model.ShipType;

public class AIPlayer implements GameBoard.BoardEvents {

  private GameCore gameCore;

  private ArrayDeque<Point> shotGuesses;

  public AIPlayer(GameCore gameCore) {
    this.shotGuesses = new ArrayDeque<Point>();
    this.gameCore = gameCore;
  }

  public void placeShips() {
    for (ShipType type : ShipType.values()) {
      int x, y;
      ShipOrientation orientation;
      do {
        x = randomIntegerRange(0, gameCore.getPlayerBoard().getBoardSize());
        y = randomIntegerRange(0, gameCore.getPlayerBoard().getBoardSize());
        orientation = ShipOrientation.values()[randomIntegerRange(0,
            ShipOrientation.values().length)];
      } while (!gameCore.getOpponentBoard().placeShip(x, y, type, orientation));
    }
  }

  public void generateGuess(Point lastSuccessfulShot) {
    int lastx = lastSuccessfulShot.x;
    int lasty = lastSuccessfulShot.y;

    Point[] guesses = { new Point(lastx, lasty + 1),
        new Point(lastx, lasty - 1), new Point(lastx + 1, lasty),
        new Point(lastx - 1, lasty), };

    for (Point guess : guesses) {
      if (gameCore.getPlayerBoard().shotIsValid(guess.x, guess.y))
        shotGuesses.push(guess);
    }

  }

  public static int randomIntegerRange(int min, int max) {
    RandomGenerator randomGenerator = RandomGenerator.of("Random");
    return randomGenerator.nextInt(min, max);
  }
  
  public Point getRandomGuess() {
    int x, y;
    do {
      x = randomIntegerRange(0, gameCore.getPlayerBoard().getBoardSize());
      y = randomIntegerRange(0, gameCore.getPlayerBoard().getBoardSize());
    } while (!gameCore.getPlayerBoard().shotIsValid(x, y));
    return new Point(x, y);
  }

  public void shootAtBoard() {
    Point shot = null;
    do {
      if (!shotGuesses.isEmpty()) {
        shot = shotGuesses.poll();
      } else {
        shot = getRandomGuess();
      }
    // make sure the shot is valid
    } while(!gameCore.getPlayerBoard().shootAt(shot.x, shot.y));
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
