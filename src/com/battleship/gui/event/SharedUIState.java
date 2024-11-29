package com.battleship.gui.event;

// TODO: REFACTOR THIS OUT
public class SharedUIState {
	private enum BoardUIMode { PlacementMode, BattleMode }

	private static BoardUIMode ui_mode;
	
	public SharedUIState() {
		setInPlacementMode();
	}

	public void setInPlacementMode() {
		ui_mode = BoardUIMode.PlacementMode;
	}
	
	public void setInBattleMode() {
		ui_mode = BoardUIMode.BattleMode;
	}
	
	public boolean inPlacementMode() {
		return ui_mode == BoardUIMode.PlacementMode;
	}

	public boolean inBattleMode() {
		return ui_mode == BoardUIMode.BattleMode;
	}
}
