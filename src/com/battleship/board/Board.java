package com.battleship.board;

import java.util.ArrayList;

import com.battleship.exception.BattleshipException;
import com.battleship.exception.InvalidShipPlacement;
import com.battleship.exception.OutOfBoundsCoordinate;
import com.battleship.ship.Ship;
import com.battleship.utils.Direction;
import com.battleship.utils.Vec2D;

public class Board {
	public interface ForEachCellOperation {
		void run(BoardCell cell, Vec2D coord);
	}

	public interface ForEachShipOperation {
		void run(Ship ship);
	}

	public ArrayList<Vec2D> getShipBodyCoordinate(final Ship ship) {
		ArrayList<Vec2D> ship_body = new ArrayList<>();

		Vec2D body = new Vec2D(ship.getHead());

		for (int i = 0; i < ship.getLength(); i++) {

			if (body.getY() < 0 || body.getY() >= board_height || body.getX() < 0 || body.getX() >= board_width)
				return null;

			ship_body.add(new Vec2D(body));
			switch (ship.getDirection()) {
			case East:
				body.addX(1);
				break;
			case North:
				body.subtractY(1);
				break;
			case South:
				body.addY(1);
				break;
			case West:
				body.subtractX(1);
				break;
			}

		}

		return ship_body;
	}

	private BoardCell[][] board;

	private int board_width, board_height;

	private ArrayList<Ship> fleet;

	public Ship getShip(String name) {
		for (Ship ship : fleet) {
			if (ship.getName() == name)
				return ship;
		}
		return null;
	}

	public Board(Board other) {
		board_width = other.getBoardWidth();
		board_height = other.getBoardHeight();

		fleet = new ArrayList<>(other.getFleet());
		board = new BoardCell[board_height][board_width];

		other.forEachCell((BoardCell cell, Vec2D coord) -> {
			board[coord.getY()][coord.getX()] = new BoardCell(cell);
		});
	}

	public Board(int width, int heigth) {
		fleet = new ArrayList<Ship>();
		board = new BoardCell[heigth][width];
		board_width = width;
		board_height = heigth;
		forEachCell((BoardCell cell, Vec2D coord) -> {
			board[coord.getY()][coord.getX()] = new BoardCell();
		});
	}

	public void addShip(final Ship ship) throws InvalidShipPlacement, OutOfBoundsCoordinate {
		forEachShip((Ship s) -> {
			if (s.getName() == ship.getName()) {
				throw new IllegalArgumentException();
			}
		});
		placeShipInBoard(ship);
		fleet.add(ship);
	}

	public void addShip(final String name, int length, final Vec2D head, final Direction direction)
			throws InvalidShipPlacement, OutOfBoundsCoordinate {
		addShip(new Ship(name, length, head, direction));
	}

	public BoardCell cellAt(int x, int y) throws ArrayIndexOutOfBoundsException {
		return board[y][x];
	}

	public BoardCell cellAt(Vec2D coord) throws ArrayIndexOutOfBoundsException {
		return cellAt(coord.getX(), coord.getY());
	}

	public void forEachCell(ForEachCellOperation operation) {
		for (int y = 0; y < board_height; y++) {
			for (int x = 0; x < board_width; x++) {
				operation.run(getCell(x, y), new Vec2D(x, y));
			}
		}
	}

	public void forEachShip(ForEachShipOperation operation) {
		for (Ship ship : fleet) {
			operation.run(ship);
		}
	}

	public void forEachShipBody(Ship ship, ForEachCellOperation operation) {
		for (Vec2D coord : getShipBodyCoordinate(ship)) {
			BoardCell cell = getCell(coord);
			operation.run(cell, coord);
		}
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

	public BoardCell getCell(Vec2D coord) throws ArrayIndexOutOfBoundsException {
		return getCell(coord.getX(), coord.getY());
	}

	public ArrayList<Ship> getFleet() {
		return fleet;
	}

	public int getTotalShipsInFleet() {
		return fleet.size();
	}

	public boolean isAllShipIsDestroyed() {
		for (Ship s : fleet)
			if (!s.isSunk())
				return false;
		return true;
	}

	private void placeShipInBoard(final Ship ship) throws InvalidShipPlacement, OutOfBoundsCoordinate {
		ArrayList<Vec2D> body_coordinate = getShipBodyCoordinate(ship);
		if (body_coordinate == null)
			throw new OutOfBoundsCoordinate();
		for (Vec2D coord : body_coordinate) {

			System.out.println(coord);

			BoardCell cell = getCell(coord);

			if (cell.getStatus() != BoardCellStatus.Empty)
				throw new InvalidShipPlacement();

			cell.setStatus(BoardCellStatus.Ship);
			cell.setShip(ship);
		}
	}

	public void resetBoard() {
		forEachCell((BoardCell cell, Vec2D _) -> {
			cell.setStatus(BoardCellStatus.Empty);
		});
	}

	public void rotateShip(Ship ship) throws InvalidShipPlacement, OutOfBoundsCoordinate, IllegalArgumentException {
		Vec2D head, tail;
		head = ship.getHead();
		tail = ship.getTail();

		if (head.getY() == tail.getY() && head.getX() > tail.getX())
			rotateShip(ship, Direction.North);
		else if (head.getY() == tail.getY() && head.getX() < tail.getX())
			rotateShip(ship, Direction.South);
		else if (head.getX() == tail.getX() && head.getY() > tail.getY())
			rotateShip(ship, Direction.West);
		else if (head.getX() == tail.getX() && head.getY() < tail.getY())
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
			if (ship.getTail().getY() < 0 || ship.getTail().getY() >= board_height || ship.getTail().getX() < 0
					|| ship.getTail().getX() >= board_width)
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

	public void removeShip(Ship ship) {
		forEachShipBody(ship, (BoardCell cell, Vec2D _) -> {
			cell.setStatus(BoardCellStatus.Empty);
			cell.setShip(null);
		});
	}

	public void moveShip(String name, Vec2D new_head) throws BattleshipException {
		for (Ship ship : fleet) {
			if (ship.getName() == name) {
				moveShip(ship, new_head);
				break;
			}
		}
	}

	public void moveShip(Ship ship, Vec2D new_head) throws BattleshipException {

		removeShip(ship);
		Vec2D oldh = new Vec2D(ship.getHead());
		try {
			ship.setHead(new_head);
			ship.calcTail();
			if (ship.getHead().getY() < 0 || ship.getHead().getY() >= board_height || ship.getHead().getX() < 0
					|| ship.getHead().getX() >= board_width || ship.getTail().getY() < 0
					|| ship.getTail().getY() >= board_height || ship.getTail().getX() < 0
					|| ship.getTail().getX() >= board_width)
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
		case Hit:
		case Miss:
			return cell;
		case Empty:
			cell.setStatus(BoardCellStatus.Miss);
			return cell;
		case Ship:
			cell.setStatus(BoardCellStatus.Hit);
			cell.getShip().takeAHit();
			return cell;
		default:
			return null;
		}
	}

	public BoardCell shootAtCell(final Vec2D coord) {
		return shootAtCell(coord.getX(), coord.getY());
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
