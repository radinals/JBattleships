package com.battleship.board;

import com.battleship.ship.Ship;

public class BoardCell {
	private BoardCellStatus status;
	private Ship ship;
	
	public BoardCell() {
		status = BoardCellStatus.Empty;
		ship = null;
	}

	public BoardCell(Ship ship) {
		this.status = BoardCellStatus.Ship;
		this.ship = ship;
	}

	public BoardCell(BoardCell other) {
		this.status = other.getStatus();
		this.ship = other.getShip();
	}

	public BoardCellStatus getStatus() {
		return status;
	}

	public void setStatus(BoardCellStatus status) {
		this.status = status;
	}

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}
	
	@Override
	public String toString() {
		switch(status) {
		case Empty:
			return "[ ]";
		case Hit:
			return "[X]";
		case Miss:
			return "[-]";
		case Ship:
			return "[#]";
		default:
			return null;
		
		}
	}
	
}
