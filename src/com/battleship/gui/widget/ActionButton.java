package com.battleship.gui.widget;

import javax.swing.JButton;

public class ActionButton extends JButton {
	private enum ButtonMode { ResetMode, ConfirmMode };
	private ButtonMode button_mode;
	
	public ActionButton() {
		setResetMode();
	}
	
	public boolean isResetMode() {
		return button_mode == ButtonMode.ResetMode;
	}

	public boolean isConfirmMode() {
		return button_mode == ButtonMode.ConfirmMode;
	}
	
	public void setResetMode() {
		setText("RESET");
		button_mode = ButtonMode.ResetMode;
	}

	public void setConfirmMode() {
		setText("CONFIRM");
		button_mode = ButtonMode.ConfirmMode;
	}

}
