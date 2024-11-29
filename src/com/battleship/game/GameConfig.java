package com.battleship.game;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import com.battleship.board.BoardCellStatus;

public class GameConfig {
	
	private static GameConfig staticInstance = null;

	public final int board_rows, board_cols;
	
	public final HashMap<BoardCellStatus, Color> cellColorHashMap; 
	public final Color borderColor;
	public final int borderSize;
	
	public final String windowName;
	public final Rectangle windowSize;
	

	public static GameConfig get() {
		if (staticInstance == null) {
			staticInstance = new GameConfig();
		}
		return staticInstance;
	}
	
	private GameConfig() {
		board_rows = board_cols = 10;
		
		windowName = "Battleship";
				
		windowSize = new Rectangle(800,600);
		
		borderColor = Color.black;
		borderSize = 2;
		
		cellColorHashMap = new HashMap<>();
		
		cellColorHashMap.put(BoardCellStatus.Empty, Color.blue);
		cellColorHashMap.put(BoardCellStatus.Hit,   Color.red);
		cellColorHashMap.put(BoardCellStatus.Miss,  Color.white);
		cellColorHashMap.put(BoardCellStatus.Ship,  Color.green);
		
		for(Map.Entry<BoardCellStatus, Color> e : cellColorHashMap.entrySet()) {
			System.out.println(e);
			
		}
	}
}
