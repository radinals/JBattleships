package main.com.model;

public class GameBoardCell {

  public GameBoardMark mark;
  public ShipType ship;

  public GameBoardCell() {
    this.mark = GameBoardMark.NONE;
  }

  public GameBoardCell(GameBoardCell other) {
    this.mark = other.mark;
    this.ship = other.ship;
  }

  public void removeShip() {
    ship = null;
  }

  public boolean containsAShip() {
    return ship != null;
  }

  public void setShip(ShipType ship) {
    this.ship = ship;
  }

  public void setMarkHit() {
    this.mark = GameBoardMark.HIT;
  }

  public void setMarkMiss() {
    this.mark = GameBoardMark.MISS;
  }

  public void setMarkNone() {
    this.mark = GameBoardMark.NONE;
  }

  public boolean isMarkHit() {
    return mark == GameBoardMark.HIT;
  }

  public boolean isMarkMiss() {
    return mark == GameBoardMark.MISS;
  }

  public boolean isMarkNone() {
    return mark == GameBoardMark.NONE;
  }
}
