package com.battleship.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.battleship.game.Players;
import com.battleship.gui.callback.ShotEvent;
import com.battleship.gui.event.UiMode;
import com.battleship.gui.widget.ActionButton;
import com.battleship.gui.widget.guiboard.GUIBoard;

public class MainWindow extends JFrame {
	private GUIBoard player_board, opponent_board;
	private JPanel game_buttons;
	private JButton exit_button;
	private ActionButton action_button;
	private JTextArea log_widget;
	private UiMode UiMode;
	
	public MainWindow(Players players, HashMap<String, Integer> ship_pieces, ShotEvent shotCallback) {
		setTitle("Battleships");
        setVisible(true);
		setSize(800,600);
		setLayout(new GridLayout(2, 2, 10, 10)); // 1 row, 2 columns
		
		UiMode = new UiMode();

		player_board = new GUIBoard(players.getPlayer_board(), UiMode, false);
		opponent_board = new GUIBoard(players.getOpponent_board(), UiMode, true);
	
		opponent_board.setOnShotEventCallback(shotCallback);
		
		for(Map.Entry<String, Integer> entry : ship_pieces.entrySet())
			player_board.enqueShipPiece(entry);
		
		player_board.loadNextShipInQueue();
		
		com.battleship.gui.event.UiMode.setInPlacementMode();

		log_widget = new JTextArea();
		game_buttons = new JPanel();
		
		action_button = new ActionButton();

		action_button.setConfirmMode();

		exit_button = new JButton(); exit_button.setText("EXIT");
		
		action_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (action_button.isResetMode()) {
					System.out.println("RESET BTN");
				} else {
					if (!player_board.hasQueuedShipPlacement()) {
						action_button.setResetMode();
						com.battleship.gui.event.UiMode.setInBattleMode();
					}
					System.out.println("CONFIRM BTN");
				}
				
			}
		});

		exit_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(NORMAL);
			}
		});

	    exit_button.setFont(new Font("Arial", Font.PLAIN, 30));
	    action_button.setFont(new Font("Arial", Font.PLAIN, 30));
	    log_widget.setFont(new Font("Arial", Font.PLAIN, 20));
		
		game_buttons.setLayout(new GridLayout(2, 1, 10,10));
		game_buttons.add(action_button);
		game_buttons.add(exit_button);
		
		add(player_board);
		add(opponent_board);
		add(log_widget);
		add(game_buttons);
	}
	
	
	public GUIBoard getPlayerBoard() {
		return player_board;
	}

	public GUIBoard getOpponentBoard() {
		return opponent_board;
	}
	
	@Override
	public Dimension getPreferredSize() {
		return this.getSize();
	}

}
