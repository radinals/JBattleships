package com.battleship.gui.widget.guiboard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;

import com.battleship.board.Board;
import com.battleship.board.BoardCell;
import com.battleship.board.BoardCellStatus;

public class BoardRenderer {

	Color borderColor;
	HashMap<BoardCellStatus, Color> cellColorMap;

	int boardBorderSize;
	boolean hideShips;

	public BoardRenderer(Color borderColor, HashMap<BoardCellStatus, Color> cellColorMap,
						 Dimension cellDimension, int boardBorderSize, boolean hideShips) 
	{
		this.borderColor = borderColor;
		this.cellColorMap = cellColorMap;
		this.boardBorderSize = boardBorderSize;
		this.hideShips = hideShips;
	}

	public void renderCell(Point startPoint, Dimension cellDimension, Color cellColor, Graphics renderField) {
		renderField.setColor(Color.black);
		renderField.fillRect((int) startPoint.getX(), (int) startPoint.getY(), cellDimension.width,
				cellDimension.height);

		renderField.setColor(cellColor);
		renderField.fillRect((int) startPoint.getX() + boardBorderSize, (int) startPoint.getY() + boardBorderSize,
				cellDimension.width - boardBorderSize * 2, cellDimension.height - boardBorderSize * 2);
	}

	public void renderBoard(final Board board, Point boardStartPoint, Dimension cellDimension, Graphics renderField) {
		Point cell_start = new Point(boardStartPoint);
		for (int y = 0; y < board.getBoardHeight(); y++) {
			cell_start.x = (boardStartPoint.x); // reset x
			for (int x = 0; x < board.getBoardWidth(); x++) {
				BoardCell cell = board.getCell(x, y);
				Color color;
				if (hideShips && cell.getStatus() == BoardCellStatus.Ship) {
					color = cellColorMap.get(BoardCellStatus.Empty);
				} else {
					color = cellColorMap.get(cell.getStatus());
				}
				renderCell(cell_start, cellDimension, color, renderField);
				cell_start.x += cellDimension.width; // go to the right a cell width
			}
			cell_start.y += cellDimension.height; // go down a cell height
		}
	}
}
