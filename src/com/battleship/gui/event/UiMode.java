package com.battleship.gui.event;

public class UiMode {
	private enum BoardUIMode { PlacementMode, BattleMode }

	private static BoardUIMode ui_mode;
	
	public UiMode() {
		setInPlacementMode();
	}

	public static void setInPlacementMode() {
		ui_mode = BoardUIMode.PlacementMode;
	}
	
	public static void setInBattleMode() {
		ui_mode = BoardUIMode.BattleMode;
	}
	
	public static boolean inPlacementMode() {
		return ui_mode == BoardUIMode.PlacementMode;
	}

	public static boolean inBattleMode() {
		return ui_mode == BoardUIMode.BattleMode;
	}
}
