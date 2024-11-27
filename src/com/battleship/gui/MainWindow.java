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
import com.battleship.gui.state.SharedUIState;
import com.battleship.gui.widget.ActionButton;
import com.battleship.gui.widget.GUIBoard;

public class MainWindow extends JFrame {
	private GUIBoard player_board, opponent_board;
	private JPanel game_buttons;
	private JButton exit_button;
	private ActionButton action_button;
	private JTextArea log_widget;
	private SharedUIState shared_state;
	
	public MainWindow(Players players, HashMap<String, Integer> ship_pieces) {
		setTitle("Battleships");
		
        setVisible(true);
		setSize(800,600);
		setLayout(new GridLayout(2, 2, 10, 10)); // 1 row, 2 columns
		
		shared_state = new SharedUIState();

		player_board = new GUIBoard(players.getPlayer_board(), shared_state);
		opponent_board = new GUIBoard(players.getOpponent_board(), shared_state);
		
		for(Map.Entry<String, Integer> entry : ship_pieces.entrySet())
			player_board.enqueShipPiece(entry);
		
		player_board.loadNextShipInQueue();
		
		shared_state.setInPlacementMode();

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
					player_board.loadNextShipInQueue();
					if (!player_board.hasQueuedShipPlacement()) {
						action_button.setResetMode();
						shared_state.setInBattleMode();
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

// ############################################################## Getter/Setter
	
	@Override
	public Dimension getPreferredSize() {
		return this.getSize();
	}

}
