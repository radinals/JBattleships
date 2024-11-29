package com.battleship.gui.utils;

import java.awt.Dimension;
import java.awt.Point;

import com.battleship.utils.BoardCoordinate;

public class GridOperations {
	
	public static BoardCoordinate getCoordOnGrid(Point point, Point grid_start, Dimension grid_size, Dimension cell_size) {

	    if (point.getX() < grid_start.getX() || point.getY() < grid_start.getY()) {
	        throw new IllegalArgumentException("Click is outside the grid.");
	    }

	    double relativeX = point.getX() - grid_start.getX();
	    double relativeY = point.getY() - grid_start.getY();

	    if (relativeX >= grid_size.getWidth() || relativeY >= grid_size.getHeight()) {
	        throw new IllegalArgumentException("Click is outside the grid.");
	    }

	    int col = (int) (relativeX / cell_size.getWidth());
	    int row = (int) (relativeY / cell_size.getHeight());

	    return new BoardCoordinate(col, row);
	}
	
}
