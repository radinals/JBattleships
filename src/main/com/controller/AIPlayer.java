package main.com.controller;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.random.RandomGenerator;

import main.com.model.GameBoard;
import main.com.model.Ship;
import main.com.model.ShipOrientation;
import main.com.model.ShipType;

public class AIPlayer implements GameBoard.BoardEvents {

  private GameCore gameCore;
  
  private ArrayDeque<Point> shotGuesses;
  
  private int aiLevel = 1;
  private final int baseGuessWeight = 10;

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
  
  public void setAiLevel(int n) {
    aiLevel += (aiLevel + n <= 10) ? n : 0;
  }
  
  private HashMap<Point, Integer> assignGuessWeights(Point[] points) {
    
    HashMap<Point, Integer> weightedGuess = new HashMap<Point, Integer>();
    
    for(Point p : points) {
      int w = this.baseGuessWeight;
      if (isCellAtPointAHit(p)) {
        w += (this.aiLevel * this.baseGuessWeight);
      } 
      weightedGuess.put(p, w);
    }

    System.out.println("GUESS WEIGHTS: ");
    for(Map.Entry<Point, Integer> e : weightedGuess.entrySet()) {
      System.out.printf("POINT(%d,%d) (%d)\n", e.getKey().x, e.getKey().y, e.getValue());
    }
    
    
    return weightedGuess;
  }
  
  private int getTotalGuessMapWeights(HashMap<Point, Integer> weightedGuessMap) {
    int total = 0;
    for(Integer e: weightedGuessMap.values()) total += e;
    return total;
  }
  
  private Point pickBestGuess(HashMap<Point, Integer> weightedGuessMap) {
    final int totalWeight = getTotalGuessMapWeights(weightedGuessMap);

    Point pick = null;
    do {

      for(int i=aiLevel; i >= 0; i--) {
        int rand = randomIntegerRange(0, totalWeight);

        for(Map.Entry<Point, Integer> e : weightedGuessMap.entrySet()) {
          if (rand > e.getValue()) {
            rand -= e.getValue();
          } else if (rand < e.getValue()) {
            pick = e.getKey();
            break;
          }

        }

      }

    } while(pick == null);
    
    return pick;
    
  }

  public void generateGuess(Point lastSuccessfulShot) {
    int lastx = lastSuccessfulShot.x;
    int lasty = lastSuccessfulShot.y;

    Point[] guesses = { new Point(lastx, lasty + 1),
        new Point(lastx, lasty - 1), new Point(lastx + 1, lasty),
        new Point(lastx - 1, lasty), };
    
    HashMap<Point, Integer> weightedGuess = assignGuessWeights(guesses);

    Point bestGuess = pickBestGuess(weightedGuess);
    
    System.out.printf("PICKED POINT(%d,%d)\n", bestGuess.x, bestGuess.y);

    shotGuesses.add(bestGuess);
  }

  public static int randomIntegerRange(int min, int max) {
    RandomGenerator randomGenerator = RandomGenerator.of("Random");
    return randomGenerator.nextInt(min, max);
  }
  
  private boolean isCellAtPointAHit(final Point point) {
    GameBoard board = gameCore.getPlayerBoard();
    return (board.shotIsValid(point.x, point.y) && board.cellAt(point.x, point.y).containsAShip());
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
        System.out.println("SHOOTING FROM BEST GUESSS");
        shot = shotGuesses.poll();
      } else {
        System.out.println("SHOOTING FROM RANDOM GUESSS");
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
