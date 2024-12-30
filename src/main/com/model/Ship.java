package main.com.model;

public class Ship {
  private int hx, hy, tx, ty, oldx, oldy;
  private ShipType type;
  private int hitcount;
  private ShipOrientation orientation, oldOrientation;
  private int[][] shipBody;

  public interface ShipEvents {
    public void onShipSunk(final Ship ship);

    public void onShipMove(final Ship ship);

    public void onShipRotate(final Ship ship);
  }

  private ShipEvents shipEvent;

  public Ship(int hx, int hy, ShipType type, ShipOrientation orientation) {
    this(hx,hy,type,orientation,null);
  }

  public Ship(int hx, int hy, ShipType type, ShipOrientation orientation, ShipEvents shipEventHandler) {
    this.hx = hx;
    this.hy = hy;
    this.oldOrientation = null;
    this.type = type;
    this.oldx = -1;
    this.oldy = -1;
    this.hitcount = type.getLength();
    this.orientation = orientation;
    this.shipBody = new int[type.getLength()][2];
    this.shipEvent = shipEventHandler;
    updateTailPos();
    generateBody();
  }

  public Ship(final Ship other) {
    this.hx = getHeadX();
    this.hy = getHeadY();
    this.type = getShipType();
    this.hitcount = 0;
    this.shipBody = new int[type.getLength()][2];
    updateTailPos();

    for (int i = 0; i < other.getShipBody().length; i++) {
      this.shipBody[i][0] = other.getShipBody()[i][0];
      this.shipBody[i][1] = other.getShipBody()[i][1];
    }
  }

  public int getShipBodyIndex(int x, int y) {
    for (int i = 0; i < shipBody.length; i++) {
      int px = shipBody[i][0];
      int py = shipBody[i][1];
      if (px == x && py == y) {
        return i;
      }
    }
    return -1;
  }

  public int[][] getShipBody() {
    return shipBody;
  }
  
  public int getHitcount() {
   return hitcount; 
  }

  public ShipType getShipType() {
    return type;
  }

  public void restorePrevOrientation() {
    if (oldOrientation != null)
      this.orientation = oldOrientation;
    updateTailPos();
    generateBody();
  }

  public void restorePrevPos() {
    if (this.oldy >= 0 && this.oldx >= 0) {
      this.hx = this.oldx;
      this.hy = this.oldy;
    }
    updateTailPos();
    generateBody();
  }

  public void moveHead(int hx, int hy) {
    this.oldx = this.hx;
    this.oldy = this.hy;
    this.hx = hx;
    this.hy = hy;
    updateTailPos();
    generateBody();
    if (shipEvent != null)
      shipEvent.onShipMove(this);
  }

  private void generateBody() {

    //FIXME: NOT GOOD
    switch (orientation) {
      case NORTH: {
        int headY = hy;
        for(int i=0; i < type.getLength(); i++) {
          shipBody[i][0] = hx;
          shipBody[i][1] = headY++;
        }

      } break;
      case SOUTH: {
        int headY = hy;
        for(int i=0; i < type.getLength(); i++) {
          shipBody[i][0] = hx;
          shipBody[i][1] = headY--;
        }
      } break;
      case WEST:{
        int headX = hx;
        for(int i=0; i < type.getLength(); i++) {
          shipBody[i][0] = headX++;
          shipBody[i][1] = hy;
        }
      } break;

      case EAST: {
        int headX = hx;
        for(int i=0; i < type.getLength(); i++) {
          shipBody[i][0] = headX--;
          shipBody[i][1] = hy;
        }
      } break;
      default:
        break;
    }
  }

  private void updateTailPos() {
    switch (orientation) {
      case EAST:
        tx = hx + type.getLength();
        ty = hy;
        break;
      case NORTH:
        tx = hx;
        ty = hy + type.getLength();
        break;
      case SOUTH:
        tx = hx;
        ty = hy - type.getLength();
        break;
      case WEST:
        tx = hx - type.getLength();
        ty = hy;
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
    for (int[] point : shipBody) {
      if (point[0] == x && point[1] == y)
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

  public ShipOrientation getOrientation() {
    return orientation;
  }

  public int getHeadX() {
    return hx;
  }

  public int getHeadY() {
    return hy;
  }

  public int getTailX() {
    return tx;
  }

  public int getTailY() {
    return ty;
  }

  @Override
  public String toString() {

    String bodystr = "BODY: ";

    for (int[] point : shipBody) {
      bodystr += String.format("(%d,%d)/", point[0], point[1]);
    }

    return String.format("%s H:(%d,%d) T:(%d,%d) (c:%s) %s", type, hx, hy, tx,
        ty, orientation, bodystr);
  }
}
