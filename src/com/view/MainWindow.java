package com.view;

import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JFrame;

import com.controller.GameCore;

public class MainWindow extends JFrame {

  BoardView playerBoardView, opponentBoardView;

  GameCore gameCore;

  public MainWindow(int width, int height, GameCore gameCore) {
    setTitle("Battleship");
    setSize(width, height);
    setLayout(new GridLayout(1, 2));
    setVisible(true);
    setLocationRelativeTo(null);
    setFocusable(true);
    requestFocus();

    this.gameCore = gameCore;
    playerBoardView = new BoardView(gameCore.getPlayerBoard());
    opponentBoardView = new BoardView(gameCore.getOpponentBoard());

    add(playerBoardView);
    add(opponentBoardView);

    revalidate();
    repaint();
  }

  public void recoverPlayerCursorPos() {
    playerBoardView.recoverPrevCursorPos();
  }

  public void recoverOpponentCursorPos() {
    opponentBoardView.recoverPrevCursorPos();
  }

  public void movePlayerCursorUp() {
    playerBoardView.moveCursorUp();
  }

  public void movePlayerCursorDown() {
    playerBoardView.moveCursorDown();
  }

  public void movePlayerCursorLeft() {
    playerBoardView.moveCursorLeft();
  }

  public void movePlayerCursorRight() {
    playerBoardView.moveCursorRight();
  }

  public void showPlayerShips() {
    playerBoardView.showShips();
  }

  public void hidePlayerShips() {
    playerBoardView.hideShips();
  }

  public void showOpponentShips() {
    opponentBoardView.showShips();
  }

  public void hideOpponentShips() {
    opponentBoardView.hideShips();
  }

  public void showPlayerCursor() {
    playerBoardView.setCursorVisible(true);
  }

  public void hidePlayerCursor() {
    playerBoardView.setCursorVisible(false);
  }

  public void showOpponentCursor() {
    opponentBoardView.setCursorVisible(true);
  }

  public void repaintPlayerBoardView() {
    playerBoardView.repaint();
  }

  public void repaintopponentBoardView() {
    opponentBoardView.repaint();
  }

  public void hideOpponentCursor() {
    opponentBoardView.setCursorVisible(false);
  }

  public void moveOpponentCursorUp() {
    opponentBoardView.moveCursorUp();
  }

  public void moveOpponentCursorDown() {
    opponentBoardView.moveCursorDown();
  }

  public void moveOpponentCursorLeft() {
    opponentBoardView.moveCursorLeft();
  }

  public void moveOpponentCursorRight() {
    opponentBoardView.moveCursorRight();
  }

  public Point getPlayerBoardCursorPos() {
    return playerBoardView.getCursorPos();
  }

  public Point getOpponentBoardCursorPos() {
    return opponentBoardView.getCursorPos();
  }

}
