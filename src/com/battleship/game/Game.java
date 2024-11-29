package com.battleship.game;

import java.io.ObjectInputFilter.Status;
import java.util.HashMap;
import java.util.Map;
import java.util.random.RandomGenerator;

import javax.swing.SwingUtilities;

import com.battleship.board.BoardCellStatus;
import com.battleship.exception.InvalidShipPlacement;
import com.battleship.exception.OutOfBoundsCoordinate;
import com.battleship.gui.MainWindow;
import com.battleship.utils.BoardCoordinate;
import com.battleship.utils.Direction;

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
		
		placeCPUShips();

	}
	
	private BoardCoordinate randomCoordinate() {
		RandomGenerator randomGenerator = RandomGenerator.of("Random");
		return new BoardCoordinate(
				randomGenerator.nextInt(0, board_width),
				randomGenerator.nextInt(0, board_height)
		);
	}

	private static Direction randomDirection() {
		RandomGenerator randomGenerator = RandomGenerator.of("Random");
		Direction[] directions = Direction.values();
		return directions[randomGenerator.nextInt(directions.length)];
	}
	
	public void getCpuMove() {
		while(true) {
			try {
				BoardCoordinate coordinate = randomCoordinate();
				BoardCellStatus shotStatus = game_window.getPlayerBoard().shootAtBoard(coordinate);
				if (shotStatus == null) continue;
			} catch(Exception e) {
				continue;
			}
			break;
		}
	}
	
	private void placeCPUShips() {
		for (Map.Entry<String, Integer> entry : ship_pieces.entrySet()) {
			while(true) {
				try {
					players.getOpponent_board().addShip(entry.getKey(),entry.getValue(), randomCoordinate(), randomDirection());
				} catch (Exception e) {
					continue;
				};
				break;
			}
		}
	}
	
	private void onPlayerShot(BoardCoordinate coord) {
		if(game_window.getOpponentBoard().shootAtBoard(coord) != null) {
			if (players.getOpponent_board().isAllShipIsDestroyed()) {
				System.out.println("Player WIN");
				System.exit(1);
			} else {
				getCpuMove();
				if (players.getPlayer_board().isAllShipIsDestroyed()) {
					System.out.println("CPU WIN");
					System.exit(1);
				}
			}
		}
	}

	public void start() {
		SwingUtilities.invokeLater(() -> {
			game_window = new MainWindow(players, ship_pieces, this::onPlayerShot); // Create and show the main window
		});
	}

}
