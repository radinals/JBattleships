package com.battleship.gui.widget.guiboard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.io.ObjectInputFilter.Config;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;

import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

import com.battleship.board.Board;
import com.battleship.board.BoardCell;
import com.battleship.board.BoardCellStatus;
import com.battleship.game.GameConfig;
import com.battleship.game.Players;
import com.battleship.gui.callback.ShotEvent;
import com.battleship.gui.event.SharedUIState;
import com.battleship.gui.utils.GridOperations;
import com.battleship.ship.Ship;
import com.battleship.utils.Direction;
import com.battleship.utils.BoardCoordinate;

public class GUIBoard extends JComponent implements MouseInputListener {
	private Board board;
	private Point gui_board_start;
	private Dimension gui_cell_size, gui_board_size, board_size;
	private int margin;
	private Map.Entry<String, Integer> placement_current_ship_piece;
	private Queue<Map.Entry<String, Integer>> placement_ship_piece_queue;
	private Ship placement_tmp_ship;
	private boolean polled_a_ship;
	private SharedUIState shared_state;
	private ShotEvent onShotEvent;
	
	private BoardRenderer boardRenderer;
	
	public GUIBoard(Board board, SharedUIState shared_state) {
		this.addMouseListener(this);
		this.board = board;

		this.shared_state = shared_state;
		this.placement_ship_piece_queue = new ArrayDeque<>();
		
		setDimensions();
		
		onShotEvent = null;
		placement_tmp_ship = null;

		addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
            	setDimensions();
                repaint(); // Optionally repaint to adjust layout on resize
            }
        });
		
		boardRenderer = new BoardRenderer(
				GameConfig.get().borderColor,
				GameConfig.get().cellColorHashMap,
				gui_cell_size,GameConfig.get().borderSize
		);
		
	}
	
	public void setOnShotEventCallback(ShotEvent callback) {
		this.onShotEvent = callback;
	}
	
	public void enqueShipPiece(Map.Entry<String, Integer> piece) {
		placement_ship_piece_queue.add(piece);
		System.out.println("ENQUIED " + piece.getKey());
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(
				gui_board_size.width + (margin * 2),
				gui_board_size.height + (margin * 2)
		);
	}

	public boolean hasQueuedShipPlacement() {
		return !placement_ship_piece_queue.isEmpty() || polled_a_ship;
	}

	public void loadNextShipInQueue() {
		if(!placement_ship_piece_queue.isEmpty()) {
			placement_current_ship_piece = placement_ship_piece_queue.poll();
			polled_a_ship = true;
		}
	}
	
	public BoardCoordinate mousePointToGridCoordinate(Point point){
		try {
			BoardCoordinate coord;
			coord = GridOperations.getCoordOnGrid(
					point,
					gui_board_start,
					gui_board_size,
					gui_cell_size);
			return coord;
		} catch (Exception e) {
			return null;
		}
	}

	private void onLeftMouseButtonClicked(BoardCoordinate coord) {
    	if(shared_state.inPlacementMode()) {
    		try {
				board.rotateShip(board.getShip(placement_current_ship_piece.getKey()));
				System.out.println(placement_tmp_ship);
				revalidate();
				repaint();
    		} catch (Exception e) {
    			return;
    		}
    		
    	} else if (shared_state.inBattleMode()) {
    		
    	}
	}
	
	private void placeShip(BoardCoordinate coord) {
		try {
			if (polled_a_ship) {
				board.addShip(new Ship(
						placement_current_ship_piece.getKey(),
						placement_current_ship_piece.getValue(),
						coord, Direction.South)
						);
				polled_a_ship = false;
			} else {
				System.out.println(board.getShip(placement_current_ship_piece.getKey()));
				board.moveShip(board.getShip(placement_current_ship_piece.getKey()), coord);
			}
			repaint();
			System.out.println(board);
		} catch (Exception e) {
			return;
		}
	}
	
	private void onRightMouseButtonClicked(BoardCoordinate coord) {
    	if(shared_state.inPlacementMode()) {
    		placeShip(coord);
    	} else if (shared_state.inBattleMode()) {
    		if (onShotEvent != null) onShotEvent.run(coord);
    	}
    }

	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
		System.out.println("RENDERGUI " + g);
        boardRenderer.renderBoard(board, gui_board_start, gui_cell_size, g);
    }

	public BoardCellStatus shootAtBoard(BoardCoordinate coord) {
		 BoardCell cell = board.shootAtCell(coord.getCollumn(),coord.getRow());
		 if (cell == null) return null;
		 BoardCellStatus status = cell.getStatus();
		 if (status != null) repaint();
		 return status;
	}

	private void setDimensions() {
		this.margin = (int)(getWidth() * (30.0 / 100));

		this.board_size = new Dimension(board.getBoardWidth(), board.getBoardHeight());

		this.gui_cell_size = new Dimension(
				(int)((getWidth() - margin) / ( board_size.getWidth())),
				(int)((getWidth() - margin) / (board_size.getHeight()))
		);

		this.gui_board_size = new Dimension(
				(int) (gui_cell_size.getWidth() * board_size.getWidth()),
				(int) (gui_cell_size.getHeight() * board_size.getHeight())
		);

		this.gui_board_start = new Point(
				(getWidth() / 2) - gui_board_size.width / 2,
				(getHeight() / 2) - gui_board_size.height / 2);
		
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		BoardCoordinate coord = mousePointToGridCoordinate(event.getPoint());
		
		if(coord == null) return;
		
		switch(event.getButton()) {
			case MouseEvent.BUTTON1:
				onLeftMouseButtonClicked(coord);
				break;
			case MouseEvent.BUTTON3:
				onRightMouseButtonClicked(coord);
				break;
		}
	}

	@Override public void mouseDragged(MouseEvent e) {}
	@Override public void mouseEntered(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {}
	@Override public void mouseMoved(MouseEvent e) {}
	@Override public void mousePressed(MouseEvent e) {}
	@Override public void mouseReleased(MouseEvent e) {}

}
