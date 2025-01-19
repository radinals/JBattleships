package main.com.controller;

import java.awt.Point;
import java.util.ArrayDeque;

import javax.swing.SwingUtilities;

import main.com.controller.ai.AIPlayer;
import main.com.model.GameBoard;
import main.com.model.ShipOrientation;
import main.com.model.ShipType;
import main.com.view.MainWindow;
import main.com.view.UserInputHandler;

public class GameCore {

  private enum GameWinner {
    NONE, Player, Opponent
  };

  private enum GamePhase {
    PLACEMENT, BATTLE, GAMEOVER
  };

  private enum GameTurn {
    Player, Opponent, NONE
  };

  private GameBoard playerBoard;
  private GameBoard opponentBoard;
  private MainWindow mainWindow;
  private AIPlayer aiPlayer;

  private GamePhase gamePhase;
  private GameTurn gameTurn;
  private GameWinner gameWinner;

  private enum AIDifficultyLevel {
    EASY, MEDIUM, HARD
  }

  private ArrayDeque<ShipType> playerShipPlacementQueue;
  private ShipType playerCurrentHeldShip;

  public GameCore() {

    this.playerBoard = new GameBoard(10, new PlayerEventHandler(this));

    this.aiPlayer = new AIPlayer(this);

    this.opponentBoard = new GameBoard(10, aiPlayer);

    this.setAIDifficultyLevel(AIDifficultyLevel.HARD);

    playerShipPlacementQueue = new ArrayDeque<ShipType>();
    playerCurrentHeldShip = null;

  }

  private void setAIDifficultyLevel(AIDifficultyLevel level) {
    switch (level) {
      case EASY:
        this.aiPlayer.getShotGenerator().setAICorrectGuessBias(1);
        this.aiPlayer.getShotGenerator().setAIIncorrectGuessBias(2);
        break;
      case MEDIUM:
        this.aiPlayer.getShotGenerator().setAICorrectGuessBias(1);
        this.aiPlayer.getShotGenerator().setAIIncorrectGuessBias(1);
        break;
      case HARD:
        this.aiPlayer.getShotGenerator().setAICorrectGuessBias(2);
        this.aiPlayer.getShotGenerator().setAIIncorrectGuessBias(1);
        break;
    }
  }

  public void run() {
    SwingUtilities.invokeLater(() -> {
      this.mainWindow = new MainWindow(1200, 600, this);
      this.mainWindow.addKeyListener(new UserInputHandler(this));
      mainWindow.hideOpponentShips();
      mainWindow.showPlayerCursor();
      setToPlacementPhase();
    });
  }

  public boolean isGameOver() {
    return gamePhase == GamePhase.GAMEOVER;
  }

  public boolean winnerIsOpponent() {
    return (gamePhase == GamePhase.GAMEOVER)
        && (gameWinner == GameWinner.Opponent);
  }

  public boolean winnerIsPlayer() {
    return (gamePhase == GamePhase.GAMEOVER)
        && (gameWinner == GameWinner.Player);
  }

  public void setWinnerPlayer() throws Exception {
    if (gamePhase == GamePhase.BATTLE) {
      this.gamePhase = GamePhase.GAMEOVER;
      this.gameWinner = GameWinner.Player;
      System.out.println("GAMEOVER");
    } else {
      throw new Exception("attempted to end game outside of battle phase");
    }
  }

  public void setWinnerOpponent() throws Exception {
    if (gamePhase == GamePhase.BATTLE) {
      this.gamePhase = GamePhase.GAMEOVER;
      this.gameWinner = GameWinner.Opponent;
      System.out.println("GAMEOVER");
    } else {
      throw new Exception("attempted to end game outside of battle phase");
    }
  }

  public boolean placementQueueisEmpty() {
    return playerShipPlacementQueue.isEmpty();
  }

  public void fillShipPlacementQueue() {
    if (!placementQueueisEmpty())
      return;
    for (ShipType t : ShipType.values()) {
      playerShipPlacementQueue.add(t);
    }
  }

  public void confirmShipPlacment() throws Exception {
    if (playerCurrentHeldShip == null) {
      throw new Exception("player is not holding any ships");
    }

    if (!playerBoard.shipExists(playerCurrentHeldShip)) {
      throw new Exception("ship has has not been placed");
    }

    playerCurrentHeldShip = null;
  }

  public void rotatePlayerHeldShip() throws Exception {
    if (playerCurrentHeldShip == null) {
      throw new Exception("player is not holding any ships");
    }

    if (!playerBoard.shipExists(playerCurrentHeldShip)) {
      throw new Exception("ship has has not been placed");
    }

    try {
      playerBoard.rotateShip(playerCurrentHeldShip);
    } catch (Exception e) {
      return;
    }

  }

  public boolean playerHasHeldShip() {
    return playerCurrentHeldShip != null;
  }

  public void loadNextPlayerShipPlacement() throws Exception {
    if (placementQueueisEmpty()) {
      throw new Exception("no ships in queue");
    }

    if (playerCurrentHeldShip != null) {
      throw new Exception("previous ship has not been placed");
    }

    playerCurrentHeldShip = playerShipPlacementQueue.poll();
  }

  public void placePlayerHeldShip() throws Exception {
    if (playerCurrentHeldShip == null) {
      throw new Exception("player is not holding any ships");
    }

    try {
      Point cursorPos = mainWindow.getPlayerBoardCursorPos();

      // TODO: Auto move cursor to an empty area after placing a ship
      if (!playerBoard.shipExists(playerCurrentHeldShip)) {
        playerBoard.placeShip(cursorPos.x, cursorPos.y, playerCurrentHeldShip,
            ShipOrientation.NORTH);
      } else {
        playerBoard.moveShip(playerCurrentHeldShip, cursorPos.x, cursorPos.y);
      }

    } catch (Exception e) {
      throw e;
    }

  }

  public void setToBattlePhase() {
    this.mainWindow.resetOpponentCursorPos();
    this.mainWindow.showOpponentCursor();
    this.mainWindow.hidePlayerCursor();

    this.gamePhase = GamePhase.BATTLE;
    this.gameTurn = GameTurn.Player;
  }

  public void nextPlayerTurn() {
    if (this.gameTurn == GameTurn.Player) {
      this.gameTurn = GameTurn.Opponent;
    } else if (this.gameTurn == GameTurn.Opponent) {
      this.gameTurn = GameTurn.Player;
    }
  }

  public void resetGame() {
    this.gameWinner = GameWinner.NONE;
    setToPlacementPhase();
    mainWindow.repaintopponentBoardView();
    mainWindow.repaintPlayerBoardView();
  }

  public boolean isPlayersTurn() {
    return this.gameTurn == GameTurn.Player;
  }

  public boolean isOpponentsTurn() {
    return this.gameTurn == GameTurn.Opponent;
  }

  public void doOpponentMove() {
    if (isOpponentsTurn())
      aiPlayer.shootAtBoard();
    mainWindow.repaintPlayerBoardView();
  }

  public void setToPlacementPhase() {
    this.mainWindow.resetPlayerCursorPos();
    this.mainWindow.showPlayerCursor();
    this.mainWindow.hideOpponentCursor();

    if (playerCurrentHeldShip != null)
      playerCurrentHeldShip = null;

    if (!playerShipPlacementQueue.isEmpty())
      playerShipPlacementQueue.clear();

    fillShipPlacementQueue();
    playerCurrentHeldShip = playerShipPlacementQueue.poll();

    this.gamePhase = GamePhase.PLACEMENT;

    playerBoard.reset();
    opponentBoard.reset();

    aiPlayer.placeShips();
  }

  public void ShootAtOpponent() throws Exception {
    Point pos = mainWindow.getOpponentBoardCursorPos();

    if (!opponentBoard.shootAt(pos.x, pos.y)) {
      throw new Exception("Invalid Shot");
    }
  }

  public MainWindow getMainWindow() {
    return mainWindow;
  }

  public GameBoard getPlayerBoard() {
    return playerBoard;
  }

  public boolean inPlacementPhase() {
    return gamePhase == GamePhase.PLACEMENT;
  }

  public boolean inBattlePhase() {
    return gamePhase == GamePhase.BATTLE;
  }

  public GameBoard getOpponentBoard() {
    return opponentBoard;
  }
}
