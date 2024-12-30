package main.com.util;

import java.awt.Point;

public class GridUtil {
  public static Point translateClickToCell(Point clickPoint, Point gridStart,
      int gridSize, int cellSize) {

    if (clickPoint.getX() < gridStart.getX()
        || clickPoint.getY() < gridStart.getY()) {
      throw new IllegalArgumentException("Click is outside the grid.");
    }

    double relativeX = clickPoint.getX() - gridStart.getX();
    double relativeY = clickPoint.getY() - gridStart.getY();

    if (relativeX >= gridSize || relativeY >= gridSize) {
      throw new IllegalArgumentException("Click is outside the grid.");
    }

    int col = (int) (relativeX / cellSize);
    int row = (int) (relativeY / cellSize);

    return new Point(col, row);
  }
}
