package main.com.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import main.com.model.GameBoard;
import main.com.model.GameBoardCell;
import main.com.model.Ship;

public class BoardView extends JPanel {

  public BoardView(GameBoard board) {
    this.board = board;
    this.cellScalingFactor = 3;
    this.cursorVisible = false;
    this.shipsVisible = true;

    setupFrame();

    this.cursorPos = new Point(0, 0);
    this.oldCursorPos = new Point(0, 0);

    revalidate();
    repaint();
  }

  private static final int baseCellSize = 16;
  private final static SpriteManager sprites = new SpriteManager();

  private float cellScalingFactor;

  private GameBoard board;
  private Point cursorPos, oldCursorPos;

  private boolean cursorVisible;

  private boolean shipsVisible;

  private void drawBoard(Graphics2D g) {
    Point bp = getBoardRenderStart();

    int cellSize = getCellSize();

    int rx = bp.x;
    int ry = bp.y;

    for (int y = 0; y < board.getBoardSize(); y++) {
      for (int x = 0; x < board.getBoardSize(); x++) {
        GameBoardCell cell = board.getBoard()[y][x];
        drawSprite(g, rx, ry, "sea", 0);
        if (cell.containsAShip() && cell.isMarkNone() && shipIsVisible()) {
          drawShip(g, x, y, rx, ry);
        } else if (cell.isMarkHit()) {
          if (shipIsVisible())
            drawShip(g, x, y, rx, ry);
          drawSprite(g, rx, ry, "hit", 0);
        } else if (cell.isMarkMiss()) {
          drawSprite(g, rx, ry, "miss", 0);
        } else if (cell.isMarkNone()) {
        }
        rx += cellSize;
      }
      rx = bp.x;
      ry += cellSize;
    }

  }

  private void drawCursor(Graphics2D g) {
    int cellSize = getCellSize();
    int x = getBoardRenderStart().x + (cursorPos.x * cellSize);
    int y = getBoardRenderStart().y + (cursorPos.y * cellSize);
    drawSprite(g, x, y, "cursor", 0);
  }

  private void drawRotatedImage(Graphics2D g, String spriteName, int idx, int x,
      int y, int width, int height, int angleInDegrees) {
    Image image = sprites.getSprite(spriteName)[idx].image;
    if (image == null) {
      return;
    }

    Graphics2D g2d = (Graphics2D) g;

    // Enable anti-aliasing for smoother rotation
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

    // Convert degrees to radians for rotation
    double angleInRadians = Math.toRadians(angleInDegrees);

    // Apply rotation
    AffineTransform transform = new AffineTransform();
    transform.translate(x + width / 2.0, y + height / 2.0); // Center of the
                                                            // target rectangle
    transform.rotate(angleInRadians);
    transform.translate(-width / 2.0, -height / 2.0);
    transform.scale((double) width / image.getWidth(this),
        (double) height / image.getHeight(this));

    // Draw the rotated and scaled image
    g2d.drawImage(image, transform, this);
  }

  private void drawShip(Graphics2D g, int col, int line, int x, int y) {
    GameBoardCell cell = board.getBoard()[line][col];
    Ship ship = board.getShip(cell.ship);
    String sprite_name = cell.ship.toString().toLowerCase();
    int idx = ship.getShipBodyIndex(col, line);

    int rotation = 0;
    switch (ship.getOrientation()) {
      case EAST:
        rotation = -270;
        break;
      case NORTH:
        rotation = 0;
        break;
      case SOUTH:
        rotation = 180;
        break;
      case WEST:
        rotation = 270;
        break;
      default:
        break;

    }
    drawRotatedImage(g, sprite_name, idx, x, y, getCellSize(), getCellSize(),
        rotation);
  }

  private void drawSprite(Graphics2D g, int x, int y, String spriteName,
      int n) {
    Sprite s = sprites.getSprite(spriteName)[n];
    g.drawImage(s.image, x, y, getCellSize(), getCellSize(), this);
  }

  public Point getBoardRenderStart() {
    // get the center
    final double cx = (getWidth() * 0.5);
    final double cy = (getHeight() * 0.5);

    // center the board
    final int bx = (int) (cx - getPreferedBoardDimension().getWidth() * 0.5);
    final int by = (int) (cy - getPreferedBoardDimension().getHeight() * 0.5);

    return new Point(bx, by);
  }

  public int getCellSize() {
    return (int) (baseCellSize * cellScalingFactor);
  }
  
  public void resetCursorPos() {
    this.cursorPos.x = 0;
    this.cursorPos.y = 0;
  }

  public Point getCursorPos() {
    return cursorPos;
  }

  public Dimension getPreferedBoardDimension() {
    int s = board.getBoardSize() * getCellSize();
    return new Dimension(s, s);
  }

  public void hideShips() {
    shipsVisible = false;
  }

  public boolean isCursorVisible() {
    return cursorVisible;
  }

  public void moveCursorDown() {
    oldCursorPos = new Point(cursorPos);
    cursorPos.y++;
    if (cursorPos.y >= board.getBoardSize())
      recoverPrevCursorPos();
  }

  public void moveCursorLeft() {
    oldCursorPos = new Point(cursorPos);
    cursorPos.x--;
    if (cursorPos.x < 0)
      recoverPrevCursorPos();
  }

  public void moveCursorRight() {
    oldCursorPos = new Point(cursorPos);
    cursorPos.x++;
    if (cursorPos.x >= board.getBoardSize())
      recoverPrevCursorPos();
  }

  public void moveCursorUp() {
    oldCursorPos = new Point(cursorPos);
    cursorPos.y--;
    if (cursorPos.y < 0)
      recoverPrevCursorPos();
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    Graphics2D g2 = (Graphics2D) g;
    drawBoard(g2);
    if (isCursorVisible())
      drawCursor(g2);
  }

  public void recoverPrevCursorPos() {
    cursorPos = new Point(oldCursorPos);
  }

  public void setCellScalingFactor(float f) {
    this.cellScalingFactor = f;
  }

  public void setCursorVisible(boolean v) {
    cursorVisible = v;
  }

  private void setupFrame() {
    setSize(getPreferedBoardDimension());
    setFocusable(false);
    setVisible(true);
  }

  public boolean shipIsVisible() {
    return shipsVisible;
  }

  public void showShips() {
    shipsVisible = true;
  }
}
