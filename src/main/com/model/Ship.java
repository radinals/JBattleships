package main.com.model;

import java.awt.Point;

public class Ship {
  private Point head, tail, old;
  private ShipType shipType;
  private int hitcount;
  private ShipOrientation orientation, oldOrientation;
  private Point[] shipBody;

  public interface ShipEvents {
    public void onShipSunk(final Ship ship);

    public void onShipMove(final Ship ship);

    public void onShipRotate(final Ship ship);
  }

  private ShipEvents shipEvent;

  public Ship(int x, int y, ShipType shipType, ShipOrientation orientation) {
    this(x, y, shipType, orientation, null);
  }

  public Ship(int x, int y, ShipType shipType, ShipOrientation orientation,
      ShipEvents shipEventHandler) {
    this.head = new Point(x, y);
    this.old = new Point();
    this.tail = new Point();

    this.oldOrientation = null;
    this.shipType = shipType;
    this.hitcount = shipType.getLength();
    this.orientation = orientation;
    this.shipBody = new Point[shipType.getLength()];
    this.shipEvent = shipEventHandler;

    updateTailPos();
    generateBody();
  }

  public Ship(final Ship other) {
    this.head = new Point(getHead());
    this.shipType = getShipType();
    this.hitcount = 0;
    this.shipBody = new Point[shipType.getLength()];

    updateTailPos();
    generateBody();

  }

  public Point[] getShipBody() {
    return shipBody;
  }

  public int getHitcount() {
    return hitcount;
  }

  public ShipType getShipType() {
    return shipType;
  }

  public ShipOrientation getOrientation() {
    return orientation;
  }

  public Point getHead() {
    return head;
  }

  public Point getTail() {
    return tail;
  }

  public int getShipBodyIndex(int x, int y) {
    for (int i = 0; i < shipBody.length; i++) {
      Point p = shipBody[i];
      if (p.x == x && p.y == y) {
        return i;
      }
    }
    return -1;
  }

  public void restorePrevOrientation() {
    if (oldOrientation != null)
      this.orientation = oldOrientation;
    updateTailPos();
    generateBody();
  }

  public void restorePrevPos() {
    if (this.old.y >= 0 && this.old.x >= 0) {
      this.head.x = this.old.x;
      this.head.y = this.old.y;
    }
    updateTailPos();
    generateBody();
  }

  public void moveHead(int x, int y) {
    this.old = new Point(head);
    this.head.x = x;
    this.head.y = y;
    updateTailPos();
    generateBody();
    if (shipEvent != null)
      shipEvent.onShipMove(this);
  }

  private void generateBody() {

    // FIXME: NOT GOOD
    switch (orientation) {
      case NORTH: {
        int headY = head.y;
        for (int i = 0; i < shipType.getLength(); i++) {
          shipBody[i] = new Point(head.x, headY++);
        }

      }
        break;
      case SOUTH: {
        int headY = head.y;
        for (int i = 0; i < shipType.getLength(); i++) {
          shipBody[i] = new Point(head.x, headY--);
        }
      }
        break;
      case WEST: {
        int headX = head.x;
        for (int i = 0; i < shipType.getLength(); i++) {
          shipBody[i] = new Point(headX++, head.y);
        }
      }
        break;

      case EAST: {
        int headX = head.x;
        for (int i = 0; i < shipType.getLength(); i++) {
          shipBody[i] = new Point(headX--, head.y);
        }
      }
        break;
      default:
        break;
    }
  }

  private void updateTailPos() {
    switch (orientation) {
      case EAST:
        tail.x = head.x + shipType.getLength();
        tail.y = head.y;
        break;
      case NORTH:
        tail.x = head.x;
        tail.y = head.y + shipType.getLength();
        break;
      case SOUTH:
        tail.x = head.x;
        tail.y = head.y - shipType.getLength();
        break;
      case WEST:
        tail.x = head.x - shipType.getLength();
        tail.y = head.y;
        break;
      default:
        break;
    }

  }

  private void updateOrientation() {
    // Rotate right based on the current vector (x, y)
    oldOrientation = orientation;
    switch (orientation) {
      case EAST:
        orientation = ShipOrientation.SOUTH;
        break;
      case NORTH:
        orientation = ShipOrientation.EAST;
        break;
      case SOUTH:
        orientation = ShipOrientation.WEST;
        break;
      case WEST:
        orientation = ShipOrientation.NORTH;
        break;
      default:
        break;

    }
  }

  public boolean pointIntersectBody(int x, int y) {
    for (Point p : shipBody) {
      if (p.x == x && p.y == y)
        return true;
    }
    return false;
  }

  public void rotateRight() {
    updateOrientation();
    updateTailPos();
    generateBody();
    if (shipEvent != null)
      shipEvent.onShipRotate(this);
  }

  public boolean isSunk() {
    return hitcount <= 0;
  }

  public void takeHit() {
    if (!isSunk()) {
      --hitcount;
      if (shipEvent != null && isSunk()) {
        shipEvent.onShipSunk(this);
      }

    }
  }

  @Override
  public String toString() {

    String bodystr = "BODY: ";

    for (Point p : shipBody) {
      bodystr += String.format("(%d,%d)/", p.x, p.y);
    }

    return String.format("%s H:(%d,%d) T:(%d,%d) (c:%s) %s", shipType, head.x,
        head.y, tail.x, tail.y, orientation, bodystr);
  }
}
