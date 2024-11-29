package com.battleship.ship;

import com.battleship.utils.Direction;
import com.battleship.utils.BoardCoordinate;

public class Ship {
	private int length, hitpoints;
	private String name; 
	private BoardCoordinate head, tail;
	private Direction direction;
	
	public Ship() {
		this.length = 0;
		this.hitpoints = 0;
		this.name = null;
		this.head = null;
		this.tail = null;
		this.direction = null;
	}
	
	public Ship(Ship other) {
		this.length = other.getLength();
		this.hitpoints = other.getHitpoints();
		this.name = other.getName();
		this.head = new BoardCoordinate(other.getHead());
		this.tail = new BoardCoordinate(other.getTail());
		this.direction = other.getDirection();
	}

	public Ship(final String name, int length, final BoardCoordinate head, Direction d) {
		this.length = length;
		this.name = name;
		this.head = new BoardCoordinate(head);
		this.direction = d;
		this.hitpoints = 0;
		calcTail();
	}

	public Ship(final String name, int length) {
		this.length = length;
		this.name = name;
		
		this.head = this.tail = null;
		this.direction = null;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public boolean isSunk() {
		return hitpoints >= length;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.hitpoints = this.length = length;
	}

	public int getHitpoints() {
		return hitpoints;
	}
	
	public void takeAHit() {
		++hitpoints;
	}
	
	public void resetHitpoint() {
		hitpoints = 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BoardCoordinate getHead() {
		return head;
	}

	public void setHead(BoardCoordinate head) {
		this.head = head;
	}

	public BoardCoordinate getTail() {
		return tail;
	}

	public void setTail(BoardCoordinate tail) {
		this.tail = tail;
	}

	public void calcTail() {
		BoardCoordinate tail = new BoardCoordinate(head);
		switch(direction) {
		case East:
			tail.addCollumn(length-1);
			break;
		case North:
			tail.subtractRow(length-1);
			break;
		case South:
			tail.addRow(length-1);
			break;
		case West:
			tail.subtractCollumn(length-1);
			break;
		}
		this.tail = tail;
	}
	
	public String toString() {
		return String.format("%s(%s,%s)", name,head,tail);
	}

}