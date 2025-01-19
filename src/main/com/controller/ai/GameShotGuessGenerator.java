package main.com.controller.ai;

import java.awt.Point;
import java.util.ArrayDeque;

import main.com.model.GameBoard;
import main.com.util.rng.BiasedRandomChooser;
import main.com.util.rng.NativeRandomNumberGenerator;
import main.com.util.rng.RandomNumberGenerator;
import main.com.util.rng.SimpleBiasedRandomChooser;

public class GameShotGuessGenerator {
  private ArrayDeque<Point> shotGuesses;
  private ArrayDeque<Point> shotGuessesHits;
  private ArrayDeque<Point> shotGuessesInvalidHits;
  private RandomNumberGenerator rng;
  private GameBoard targetBoard;

  private double correctGuessBias = 1.0;
  private double incorrectGuessBias = 1.0;

  private BiasedRandomChooser randomChooser;

  public GameShotGuessGenerator(GameBoard targetBoard) {
    this.rng = new NativeRandomNumberGenerator();
    this.targetBoard = targetBoard;
    this.shotGuesses = new ArrayDeque<Point>();
    this.randomChooser = new SimpleBiasedRandomChooser(this.rng);
    this.shotGuessesHits = new ArrayDeque<Point>();
    this.shotGuessesInvalidHits = new ArrayDeque<Point>();
  }

  public void setAICorrectGuessBias(double n) {
    if (n > 1.0)
      n = 1.0;
    if (n < -1.0)
      n = -1.0;
    correctGuessBias = n;
    incorrectGuessBias = 1.0 - correctGuessBias;
  }

  private Point getRandomGuess() {
    int x, y;

    do {
      x = rng.rangeI(0, targetBoard.getBoardSize());
      y = rng.rangeI(0, targetBoard.getBoardSize());
    } while (!targetBoard.shotIsValid(x, y));
    return new Point(x, y);
  }

  private void generateGuesses(Point lastShot) {
    if (shotGuessesInvalidHits.contains(lastShot))
      return;
    final Point[] guesses = { new Point(lastShot.x, lastShot.y + 1),
        new Point(lastShot.x, lastShot.y - 1),
        new Point(lastShot.x + 1, lastShot.y),
        new Point(lastShot.x - 1, lastShot.y), };

    assignGuessWeights(guesses);
    Point bestGuess = (Point) randomChooser.randomPick();

    if (bestGuess != null) {
      System.out.printf("PICKED POINT(%d,%d)\n", bestGuess.x, bestGuess.y);
      shotGuesses.add(bestGuess);
      if (targetBoard.checkIfShotAtCellIsAHit(bestGuess.x, bestGuess.y)) {
        generateGuesses(bestGuess);
      }
    } else {
      if (shotGuessesHits.contains(lastShot))
        shotGuessesInvalidHits.add(lastShot);
    }

  }

  private double calculateGuessWeight(Point p) {
    return (targetBoard.checkIfShotAtCellIsAHit(p.x, p.y)) ? correctGuessBias
        : incorrectGuessBias;
  }

  private void assignGuessWeights(Point[] guesses) {
    randomChooser.clearOptions();
    for (Point p : guesses) {
      if (!targetBoard.shotIsValid(p.x, p.y) || shotGuessesHits.contains(p)
          || shotGuesses.contains(p))
        continue;

      double w = calculateGuessWeight(p);

      if (w <= 0)
        continue;

      randomChooser.addOption(p, w);

      System.out.printf("POINT(%d,%d) (%f)\n", p.x, p.y,
          calculateGuessWeight(p));
    }
  }

  public Point getGuess() {
    Point shot = null;

    for (Point p : shotGuessesHits) {
      generateGuesses(p);
    }

    if (!shotGuesses.isEmpty()) {
      System.out.println("GENERATED");
      shot = shotGuesses.poll();
    } else {
      System.out.println("RANDOMIZED");
      shot = getRandomGuess();
    }

    if (targetBoard.checkIfShotAtCellIsAHit(shot.x, shot.y))
      shotGuessesHits.addFirst(shot);

    return shot;
  }

}
