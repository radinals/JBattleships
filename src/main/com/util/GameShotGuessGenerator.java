package main.com.util;

import java.awt.Point;
import java.util.ArrayDeque;

import main.com.model.GameBoard;

public class GameShotGuessGenerator {
  private ArrayDeque<Point> shotGuesses;
  private RandomNumberGenerator rng;
  private GameBoard targetBoard;

  private int correctGuessBias = 1;
  private int incorrectGuessBias = 1;

  private BiasedRandomChooser randomChooser;

  public GameShotGuessGenerator(GameBoard targetBoard) {
    this.rng = new NativeRandomNumberGenerator();
    this.targetBoard = targetBoard;
    this.shotGuesses = new ArrayDeque<Point>();
    this.randomChooser = new SimpleBiasedRandomChooser(this.rng);
  }

  public void setAICorrectGuessBias(int n) {
    if (n <= 0)
      return;
    correctGuessBias = n;
  }

  public void setAIIncorrectGuessBias(int n) {
    if (n <= 0)
      return;
    incorrectGuessBias = n;
  }

  private Point getRandomGuess() {
    int x, y;
    do {
      x = rng.rangeI(0, targetBoard.getBoardSize());
      y = rng.rangeI(0, targetBoard.getBoardSize());
    } while (!targetBoard.shotIsValid(x, y));
    return new Point(x, y);
  }

  private void getBiasedGuess(Point lastShot) {
    final Point[] guesses = { new Point(lastShot.x, lastShot.y + 1),
        new Point(lastShot.x, lastShot.y - 1),
        new Point(lastShot.x + 1, lastShot.y),
        new Point(lastShot.x - 1, lastShot.y), };

    assignGuessWeights(guesses);
    Point bestGuess = (Point) randomChooser.randomPick();

    if (bestGuess != null) {
      System.out.printf("PICKED POINT(%d,%d)\n", bestGuess.x, bestGuess.y);
      shotGuesses.add(bestGuess);
    }
  }

  private boolean shotAtPosIsAHit(Point p) {
    return (targetBoard.shotIsValid(p.x, p.y)
        && targetBoard.cellAt(p.x, p.y).containsAShip());
  }

  private double calculateGuessWeight(Point p) {
    return (shotAtPosIsAHit(p)) ? correctGuessBias : incorrectGuessBias;
  }

  private void assignGuessWeights(Point[] guesses) {
    randomChooser.clearOptions();
    for (Point p : guesses) {
      if (!targetBoard.shotIsValid(p.x, p.y))
        continue;

      randomChooser.addOption(p, calculateGuessWeight(p));

      System.out.printf("POINT(%d,%d) (%f)\n", p.x, p.y,
          calculateGuessWeight(p));
    }
  }

  public Point getGuess() {
    Point shot = null;

    if (!shotGuesses.isEmpty()) {
      System.out.println("SHOOTING FROM BEST GUESSS");
      shot = shotGuesses.poll();
    } else {
      System.out.println("SHOOTING FROM RANDOM GUESSS");
      shot = getRandomGuess();
    }

    if (shotAtPosIsAHit(shot))
      getBiasedGuess(shot);

    return shot;
  }

}
