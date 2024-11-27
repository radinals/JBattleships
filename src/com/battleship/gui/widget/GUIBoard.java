package com.battleship.gui.widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;

import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

import com.battleship.board.Board;
import com.battleship.board.BoardCell;
import com.battleship.board.BoardCellStatus;
import com.battleship.gui.state.SharedUIState;
import com.battleship.gui.utils.GridOperations;
import com.battleship.ship.Ship;
import com.battleship.utils.Direction;
import com.battleship.utils.Vec2D;

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
	
	public GUIBoard(Board board, SharedUIState shared_state) {
		
		this.addMouseListener(this);
		this.board = board;

		this.shared_state = shared_state;
		this.placement_ship_piece_queue = new ArrayDeque<>();
		
		setDimensions();
		
		placement_tmp_ship = null;

		addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
            	setDimensions();
                repaint(); // Optionally repaint to adjust layout on resize
            }
        });

	}
	
	
	private void drawBoard(final Graphics g) {

		final Color frame_color = Color.black;
		
		Vec2D coord = new Vec2D(getGuiBoardStart().x, getGuiBoardStart().y);
		
		for(int y=0; y < board.getBoardHeight(); y++) {
			coord.setX(getGuiBoardStart().x);
			for(int x=0; x < board.getBoardWidth(); x++) {

				BoardCell cell = board.getCell(x, y);

				Color cell_color;
				switch(cell.getStatus()) {
				case Hit:
					cell_color = Color.red;
					break;
				case Miss:
					cell_color = Color.yellow;
					break;
				case Ship:
					cell_color = Color.cyan;
					break;
				case Empty:
				default:
					cell_color = Color.white;
					break;
				}

				drawCell(coord, cell_color, frame_color, 2, g);

				coord.addX(gui_cell_size.width);
			}
			coord.addY(gui_cell_size.height);
		}		

	}
	
	private void drawCell(Vec2D coord,
			Color color,
			Color frame_color,
			int frame_width, Graphics g)
	{

		g.setColor(frame_color);
		g.fillRect(
				coord.getX(), coord.getY(),
				gui_cell_size.width, gui_cell_size.height
		); 

		g.setColor(color);
		g.fillRect(
				coord.getX() + frame_width,
				coord.getY() + frame_width,
				gui_cell_size.width - frame_width * 2,
				gui_cell_size.height - frame_width * 2
		); 
	}
	
	public void enqueShipPiece(Map.Entry<String, Integer> piece) {
		placement_ship_piece_queue.add(piece);
			System.out.println("ENQUED OUT " + piece.getKey());
	}
	

    public Board getBoard() {
		return board;
	}

	public Dimension getBoardSize() {
		setDimensions();
		return board_size;
	}
	
	public Dimension getGuiBoardSize() {
		setDimensions();
		return gui_board_size;
	}
	
	public Point getGuiBoardStart() {
		return gui_board_start;
	}

	public Dimension getGuiCellSize() {
		setDimensions();
		return gui_cell_size;
	}

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
			System.out.println("TOOK OUT " + placement_current_ship_piece.getKey());
			polled_a_ship = true;
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent event) {
		Vec2D coord = mousePointToGridCoordinate(event.getPoint());
		
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

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseEntered(MouseEvent event) {
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseMoved(MouseEvent event) {
		// TODO Auto-generated method stub
	}


	public Vec2D mousePointToGridCoordinate( Point point){
		try {
			Vec2D coord;
			coord = GridOperations.getCoordOnGrid(
					point,
					getGuiBoardStart(),
					getGuiBoardSize(),
					getGuiCellSize());
			return coord;
		} catch (Exception e) {
			return null;
		}
		
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	private void onLeftMouseButtonClicked(Vec2D coord) {
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

	private void onRightMouseButtonClicked(Vec2D coord) {
    	if(shared_state.inPlacementMode()) {
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
				revalidate();
				repaint();
				System.out.println(board);
			} catch (Exception e) {
				return;
			}

    	} else if (shared_state.inBattleMode()) {
    		
    		// FIXME: Doesn't work
    		shootAtBoard(coord);

    	}
    }

	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        
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

	public BoardCellStatus shootAtBoard(Vec2D coord) {
		return board.shootAtCell(coord.getX(),coord.getY()).getStatus();
	}

}
