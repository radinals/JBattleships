package com.battleship.game;

import java.util.HashMap;

import javax.swing.SwingUtilities;

import com.battleship.gui.MainWindow;

public class Game {

	private final HashMap<String, Integer> ship_pieces;
	private final int board_width, board_height;
	private Players players;
	private MainWindow game_window;

	public Game() {

		board_width = board_height = 10;

		players = new Players(board_width, board_height);

		ship_pieces = new HashMap<String, Integer>();

		ship_pieces.put("Carrier", 5);
		ship_pieces.put("Battleship", 4);
		ship_pieces.put("Destroyer", 3);
		ship_pieces.put("Submarine", 3);
		ship_pieces.put("Boat", 2);

	}

	public void start() {

		SwingUtilities.invokeLater(() -> {
			game_window = new MainWindow(players, ship_pieces); // Create and show the main window
		});

	}

}
