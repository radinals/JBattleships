package com.battleship.board;

import java.util.ArrayList;

import com.battleship.exception.BattleshipException;
import com.battleship.exception.InvalidShipPlacement;
import com.battleship.exception.OutOfBoundsCoordinate;
import com.battleship.ship.Ship;
import com.battleship.utils.Direction;
import com.battleship.utils.BoardCoordinate;

public class Board {
	private BoardCell[][] board;
	private int board_width, board_height;
	private ArrayList<Ship> fleet;

	public interface ForEachCellOperation { void run(BoardCell cell, BoardCoordinate coord); }
	public interface ForEachShipOperation { void run(Ship ship); }


	public Board(Board other) {
		copyBoard(other);
	}

	public Board(int width, int heigth) {
		fleet = new ArrayList<Ship>();
		board = new BoardCell[heigth][width];
		board_width = width;
		board_height = heigth;
		forEachCell((BoardCell cell, BoardCoordinate coord) -> {
			board[coord.getRow()][coord.getCollumn()] = new BoardCell();
		});
	}
	
	public void copyBoard(Board other) {
		board_width = other.getBoardWidth();
		board_height = other.getBoardHeight();

		fleet = new ArrayList<>(other.getFleet());
		board = new BoardCell[board_height][board_width];

		other.forEachCell((BoardCell cell, BoardCoordinate coord) -> {
		board[coord.getRow()][coord.getCollumn()] = new BoardCell(cell);
		});
	}

	public BoardCell shootAtCell(final BoardCoordinate coord) {
		return shootAtCell(coord.getCollumn(), coord.getRow());
	}

	public Ship getShip(String name) {
		for (Ship ship : fleet) {
			if (ship.getName() == name)
				return ship;
		}
		return null;
	}

	public void addShip(final Ship ship) throws InvalidShipPlacement, OutOfBoundsCoordinate, IllegalArgumentException {
		forEachShip((Ship s) -> {
			if (s.getName() == ship.getName()) {
				throw new IllegalArgumentException();
			}
		});
		placeShipInBoard(ship);
		fleet.add(ship);
	}

	public void addShip(final String name, int length, final BoardCoordinate head, final Direction direction) 
			throws InvalidShipPlacement, OutOfBoundsCoordinate, IllegalArgumentException {
		addShip(new Ship(name, length, head, direction));
	}

	public BoardCell cellAt(int x, int y) throws ArrayIndexOutOfBoundsException {
		return board[y][x];
	}

	public BoardCell cellAt(BoardCoordinate coord) throws ArrayIndexOutOfBoundsException {
		return cellAt(coord.getCollumn(), coord.getRow());
	}

	public void forEachCell(ForEachCellOperation operation) {
		for (int y = 0; y < board_height; y++) {
			for (int x = 0; x < board_width; x++) {
				operation.run(getCell(x, y), new BoardCoordinate(x, y));
			}
		}
	}

	public void forEachShip(ForEachShipOperation operation) {
		for (Ship ship : fleet) {
			operation.run(ship);
		}
	}

	public void forEachShipBody(Ship ship, ForEachCellOperation operation) throws ArrayIndexOutOfBoundsException, OutOfBoundsCoordinate {
		for (BoardCoordinate coord : getShipBodyCoordinate(ship)) {
			BoardCell cell = getCell(coord);
			operation.run(cell, coord);
		}
	}


	public boolean isAllShipIsDestroyed() {
		for (Ship s : fleet)
			if (!s.isSunk())
				return false;
		return true;
	}

	private void placeShipInBoard(final Ship ship) throws InvalidShipPlacement, OutOfBoundsCoordinate {

		ArrayList<BoardCoordinate> body_coordinate = getShipBodyCoordinate(ship);

		Board tmpBoard = new Board(this);

		for (BoardCoordinate coord : body_coordinate) {

			BoardCell cell = tmpBoard.getCell(coord);

			if (cell.getStatus() != BoardCellStatus.Empty)
				throw new InvalidShipPlacement();

			cell.setShip(ship);
		}
		
		this.copyBoard(tmpBoard);
		
	}

	public void resetBoard() {
		forEachCell((BoardCell cell, BoardCoordinate _) -> {
			cell.setStatus(BoardCellStatus.Empty);
		});
	}

	public void rotateShip(Ship ship) throws InvalidShipPlacement, OutOfBoundsCoordinate, IllegalArgumentException {
		BoardCoordinate head, tail;
		head = ship.getHead();
		tail = ship.getTail();

		if (head.getRow() == tail.getRow() && head.getCollumn() > tail.getCollumn())
			rotateShip(ship, Direction.North);
		else if (head.getRow() == tail.getRow() && head.getCollumn() < tail.getCollumn())
			rotateShip(ship, Direction.South);
		else if (head.getCollumn() == tail.getCollumn() && head.getRow() > tail.getRow())
			rotateShip(ship, Direction.West);
		else if (head.getCollumn() == tail.getCollumn() && head.getRow() < tail.getRow())
			rotateShip(ship, Direction.East);
	}

	public void rotateShip(Ship ship, Direction direction)
			throws InvalidShipPlacement, OutOfBoundsCoordinate, IllegalArgumentException {

		System.out.println("B: ROTATE " + ship);

		Direction oldh = ship.getDirection();

		try {
			removeShip(ship); // remove the ship from the board
			ship.setDirection(direction);
			ship.calcTail();
			if (ship.getTail().getRow() < 0 || ship.getTail().getRow() >= board_height || ship.getTail().getCollumn() < 0
					|| ship.getTail().getCollumn() >= board_width)
				throw new OutOfBoundsCoordinate();
			placeShipInBoard(ship);
		} catch (Exception e) {
			ship.setDirection(oldh);
			ship.calcTail();
			placeShipInBoard(ship);
			throw e;
		}
		System.out.println("E: ROTATE " + ship);
	}

	public void rotateShip(String name, Direction direction)
			throws InvalidShipPlacement, OutOfBoundsCoordinate, IllegalArgumentException {
		for (Ship ship : fleet) {
			if (ship.getName() == name) {
				rotateShip(ship, direction);
				break;
			}
		}
	}

	public void removeShip(Ship ship) throws OutOfBoundsCoordinate {
		forEachShipBody(ship, (BoardCell cell, BoardCoordinate _) -> {
			cell.setStatus(BoardCellStatus.Empty);
			cell.setShip(null);
		});
	}

	public void moveShip(String name, BoardCoordinate new_head) throws BattleshipException {
		for (Ship ship : fleet) {
			if (ship.getName() == name) {
				moveShip(ship, new_head);
				break;
			}
		}
	}

	public void moveShip(Ship ship, BoardCoordinate new_head) throws BattleshipException {
		removeShip(ship);
		BoardCoordinate oldh = new BoardCoordinate(ship.getHead());
		try {
			ship.setHead(new_head);
			ship.calcTail();
			if (ship.getHead().getRow() < 0 || ship.getHead().getRow() >= board_height || ship.getHead().getCollumn() < 0
					|| ship.getHead().getCollumn() >= board_width || ship.getTail().getRow() < 0
					|| ship.getTail().getRow() >= board_height || ship.getTail().getCollumn() < 0
					|| ship.getTail().getCollumn() >= board_width)
				throw new OutOfBoundsCoordinate();
			placeShipInBoard(ship);
			System.out.println("Moved " + oldh + " to " + new_head);
		} catch (InvalidShipPlacement | OutOfBoundsCoordinate e) {
			ship.setHead(oldh);
			ship.calcTail();
			placeShipInBoard(ship);
			throw e;
		}

	}

	public void setBoardHeight(int board_height) {
		this.board_height = board_height;
	}

	public void setBoardWidth(int board_width) {
		this.board_width = board_width;
	}

	public BoardCell shootAtCell(int x, int y) {
		BoardCell cell = cellAt(x, y);

		switch (cell.getStatus()) {
		case Empty:
			cell.setStatus(BoardCellStatus.Miss);
			return cell;
		case Ship:
			cell.setStatus(BoardCellStatus.Hit);
			cell.getShip().takeAHit();
			return cell;
		case Hit:
		case Miss:
		default:
			return null;
		}
	}

	public ArrayList<BoardCoordinate> getShipBodyCoordinate(final Ship ship) throws OutOfBoundsCoordinate {
		ArrayList<BoardCoordinate> ship_body = new ArrayList<>();

		BoardCoordinate body = new BoardCoordinate(ship.getHead());

		if (ship.getHead().getRow() < 0 || ship.getHead().getRow() >= board_height || ship.getHead().getCollumn() < 0 || ship.getHead().getCollumn() >= board_width)
			throw new OutOfBoundsCoordinate();

		if (ship.getTail().getRow() < 0 || ship.getTail().getRow() >= board_height || ship.getTail().getCollumn() < 0 || ship.getTail().getCollumn() >= board_width)
			throw new OutOfBoundsCoordinate();

		for (int i = 0; i < ship.getLength(); i++) {

			if (body.getRow() < 0 || body.getRow() >= board_height || body.getCollumn() < 0 || body.getCollumn() >= board_width)
				throw new OutOfBoundsCoordinate();

			ship_body.add(new BoardCoordinate(body));
			switch (ship.getDirection()) {
			case East:
				body.addCollumn(1);
				break;
			case North:
				body.subtractRow(1);
				break;
			case South:
				body.addRow(1);
				break;
			case West:
				body.subtractCollumn(1);
				break;
			}

		}
		return ship_body;
	}

	public BoardCell[][] getBoard() {
		return board;
	}

	public int getBoardHeight() {
		return board_height;
	}

	public int getBoardWidth() {
		return board_width;
	}

	public BoardCell getCell(int x, int y) throws ArrayIndexOutOfBoundsException {
		if ((x < 0 || x >= board_width) || (y < 0 || y >= board_height))
			throw new ArrayIndexOutOfBoundsException();
		return board[y][x];
	}

	public BoardCell getCell(BoardCoordinate coord) throws ArrayIndexOutOfBoundsException {
		return getCell(coord.getCollumn(), coord.getRow());
	}
	
	public BoardCellStatus getCellStatus(BoardCoordinate coord) {
			BoardCell cell =  getCell(coord.getCollumn(), coord.getRow());
			if (cell == null) return null;
			return cell.getStatus();
	}

	public ArrayList<Ship> getFleet() {
		return fleet;
	}

	public int getTotalShipsInFleet() {
		return fleet.size();
	}

	@Override
	public String toString() {
		String str = "";
		for (BoardCell[] line : board) {
			for (BoardCell cell : line) {
				str += cell.toString();
			}
			str += '\n';
		}
		return str;
	}

}
