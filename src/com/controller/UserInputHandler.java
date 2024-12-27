package com.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class UserInputHandler implements KeyListener {

  private GameCore gameCore;

  public UserInputHandler(GameCore gameCore) {
    this.gameCore = gameCore;
  }

  @Override
  public void keyPressed(KeyEvent e) {
    switch (e.getKeyCode()) {
      case KeyEvent.VK_UP:
        if (gameCore.inPlacementPhase())
          gameCore.getMainWindow().movePlayerCursorUp();
        else
          gameCore.getMainWindow().moveOpponentCursorUp();
        onCursorMovement();
        break;
      case KeyEvent.VK_DOWN:
        if (gameCore.inPlacementPhase())
          gameCore.getMainWindow().movePlayerCursorDown();
        else
          gameCore.getMainWindow().moveOpponentCursorDown();
        onCursorMovement();
        break;
      case KeyEvent.VK_LEFT:
        if (gameCore.inPlacementPhase())
          gameCore.getMainWindow().movePlayerCursorLeft();
        else
          gameCore.getMainWindow().moveOpponentCursorLeft();
        onCursorMovement();
        break;
      case KeyEvent.VK_RIGHT:
        if (gameCore.inPlacementPhase())
          gameCore.getMainWindow().movePlayerCursorRight();
        else
          gameCore.getMainWindow().moveOpponentCursorRight();
        onCursorMovement();
        break;
      case KeyEvent.VK_CONTROL:
        if (gameCore.inPlacementPhase()) {
          try {
            gameCore.rotatePlayerHeldShip();
            gameCore.getMainWindow().repaintPlayerBoardView();
          } catch (Exception e2) {
            return;
          }
        }
        break;
      case KeyEvent.VK_ENTER:
        onEnterKeyPress();
        break;
    }
  }

  private void onCursorMovement() {

    if (gameCore.inPlacementPhase()) {
      try {

        gameCore.placePlayerHeldShip();

      } catch (Exception e) {
        gameCore.getMainWindow().recoverPlayerCursorPos();
        gameCore.getMainWindow().repaintPlayerBoardView();
        e.printStackTrace();
        return;
      }

    } else if (gameCore.inBattlePhase()) {
      gameCore.getMainWindow().repaintopponentBoardView();
      return;
    }

  }

  private void onEnterKeyPress() {
    if (gameCore.inPlacementPhase()) {
      try {
        gameCore.confirmShipPlacment();

        if (!gameCore.placementQueueisEmpty())
          gameCore.loadNextPlayerShipPlacement();

      } catch (Exception e) {
        e.printStackTrace();
        return;
      }

      if (gameCore.placementQueueisEmpty() && !gameCore.playerHasHeldShip()) {
        gameCore.setToBattlePhase();
        gameCore.getMainWindow().showOpponentCursor();
        gameCore.getMainWindow().hidePlayerCursor();
        gameCore.getMainWindow().repaintopponentBoardView();
        gameCore.getMainWindow().repaintPlayerBoardView();
      }

    } else if (gameCore.inBattlePhase()) {
      if (!gameCore.isGameOver()) {
        try {
          gameCore.ShootAtOpponent();
        } catch (Exception e) {
          e.printStackTrace();
          return;
        }
        gameCore.nextPlayerTurn();
        gameCore.doOpponentMove();
        gameCore.nextPlayerTurn();
      }
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

}
