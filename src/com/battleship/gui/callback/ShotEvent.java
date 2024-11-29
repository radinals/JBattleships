package com.battleship.gui.callback;

import com.battleship.utils.BoardCoordinate;

public interface ShotEvent {
	void run(BoardCoordinate coord);
}
