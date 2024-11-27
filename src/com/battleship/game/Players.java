package com.battleship.game;

import com.battleship.board.Board;

public class Players {
	private final Board player_board, opponent_board;
	private int board_width, board_height;
	
	public Players(Players other) {
		this.player_board = other.getPlayer_board();
		this.opponent_board = other.getOpponent_board();
	}
	
	public Players(int width, int height) {
		this.player_board = new Board(width,height);
		this.opponent_board = new Board(width,height);
		this.board_width = width;
		this.board_height = height;
	}
	
	public Board getPlayer_board() {
		return player_board;
	}
	
	public Board getOpponent_board() {
		return opponent_board;
	}

	public int getBoardWidth() {
		return board_width;
	}

	public int getBoardHeight() {
		return board_height;
	}
}
